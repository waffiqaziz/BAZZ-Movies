package com.waffiq.bazz_movies.core.utils.helpers

import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.waffiq.bazz_movies.R
import com.waffiq.bazz_movies.core.utils.common.Constants.SWIPE_THRESHOLD
import kotlin.math.abs

/**
 * A helper class that handles swipe actions on the favorite and watchlist fragments.
 * This class extends [ItemTouchHelper.Callback] to manage swipe gestures (left and right) on items
 * within a RecyclerView.
 *
 * @property isLogin A boolean flag indicating whether the user is logged in. This is used to determine
 *                   the behavior of swipe actions.
 * @property onSwipeLeft A callback function triggered when an item is swiped left. It takes three parameters:
 *  - A boolean indicating if the user is logged in.
 *  - The [RecyclerView.ViewHolder] being swiped.
 *  - The position of the item in the adapter.
 * @property onSwipeRight A callback function triggered when an item is swiped right.
 *                        It also takes the same parameters as [onSwipeLeft].
 * @property context The context used for accessing resources and drawing UI elements.
 * @property deleteIconResId The resource ID for the icon displayed during a right swipe (delete action).
 * @property actionIconResId The resource ID for the icon displayed during a left swipe
 *                           (custom action, such as adding/removing from watchlist).
 * @property deleteColor The color used for the background during a right swipe (delete).
 * @property actionColor The color used for the background during a left swipe (custom action).
 *
 * This class overrides methods from [ItemTouchHelper.Callback] to:
 *  - Set swipe directions (left/right) in [getMovementFlags].
 *  - Handle the swipe action in [onSwiped], invoking the appropriate callback based on swipe direction.
 *  - Draw custom background, color transition, and icons during the swipe in [onChildDraw].
 *  - Customize swipe behavior, such as setting a threshold in [getSwipeThreshold].
 */
class SwipeCallbackHelper(
  private val isLogin: Boolean,
  private val onSwipeLeft: (Boolean, RecyclerView.ViewHolder, Int) -> Unit,
  private val onSwipeRight: (Boolean, RecyclerView.ViewHolder, Int) -> Unit,
  private val context: Context,
  private val deleteIconResId: Int,
  private val actionIconResId: Int,
  private val deleteColor: Int,
  private val actionColor: Int
) : ItemTouchHelper.Callback() {

  override fun getMovementFlags(
    recyclerView: RecyclerView,
    viewHolder: RecyclerView.ViewHolder
  ): Int {
    val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
    return makeMovementFlags(0, swipeFlags)
  }

  override fun onMove(
    recyclerView: RecyclerView,
    viewHolder: RecyclerView.ViewHolder,
    target: RecyclerView.ViewHolder
  ): Boolean {
    return false
  }

  override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    val position = viewHolder.bindingAdapterPosition
    if (direction == ItemTouchHelper.START) { // swipe left
      onSwipeLeft(isLogin, viewHolder, position)
    } else if (direction == ItemTouchHelper.END) { // swipe right
      onSwipeRight(isLogin, viewHolder, position)
    }
  }

  override fun onChildDraw(
    c: Canvas,
    recyclerView: RecyclerView,
    viewHolder: RecyclerView.ViewHolder,
    dX: Float,
    dY: Float,
    actionState: Int,
    isCurrentlyActive: Boolean
  ) {
    val background = ColorDrawable()
    val itemView = viewHolder.itemView
    val itemHeight = itemView.bottom - itemView.top
    val itemWidth = itemView.right - itemView.left // Total width of the item
    val evaluator = ArgbEvaluator()

    // Calculate swipeFraction based on SWIPE_THRESHOLD
    val swipeFraction = (abs(dX) / (itemWidth * SWIPE_THRESHOLD)).coerceIn(0f, 1f)
    val gray = ContextCompat.getColor(context, R.color.gray_900) // Starting color

    if (dX > 0) { // swipe right
      val deleteIcon = ContextCompat.getDrawable(context, deleteIconResId)
      val intrinsicWidth = deleteIcon?.intrinsicWidth ?: 0
      val intrinsicHeight = deleteIcon?.intrinsicHeight ?: 0

      // Interpolate between gray and deleteColor
      background.color =
        evaluator.evaluate(swipeFraction, gray, ContextCompat.getColor(context, deleteColor)) as Int
      background.setBounds(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
      background.draw(c)

      deleteIcon?.setBounds(
        itemView.left + (itemHeight - intrinsicHeight) / 4,
        itemView.top + (itemHeight - intrinsicHeight) / 2,
        itemView.left + (itemHeight - intrinsicHeight) / 4 + intrinsicWidth,
        itemView.top + (itemHeight - intrinsicHeight) / 2 + intrinsicHeight
      )
      deleteIcon?.draw(c)
    } else if (dX < 0) { // swipe left
      val actionIcon = ContextCompat.getDrawable(context, actionIconResId)
      val intrinsicWidth = actionIcon?.intrinsicWidth ?: 0
      val intrinsicHeight = actionIcon?.intrinsicHeight ?: 0

      // Interpolate between gray and actionColor
      background.color =
        evaluator.evaluate(swipeFraction, gray, ContextCompat.getColor(context, actionColor)) as Int
      background
        .setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
      background.draw(c)

      actionIcon?.setBounds(
        itemView.right - (itemHeight - intrinsicHeight) / 4 - intrinsicWidth,
        itemView.top + (itemHeight - intrinsicHeight) / 2,
        itemView.right - (itemHeight - intrinsicHeight) / 4,
        itemView.top + (itemHeight - intrinsicHeight) / 2 + intrinsicHeight
      )
      actionIcon?.draw(c)
    }

    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
  }

  override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
    return SWIPE_THRESHOLD
  }
}
