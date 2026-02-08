package com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers

import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.waffiq.bazz_movies.core.common.utils.Constants.SWIPE_THRESHOLD
import com.waffiq.bazz_movies.core.designsystem.R.color.gray_900
import com.waffiq.bazz_movies.core.designsystem.R.color.red_matte
import com.waffiq.bazz_movies.core.designsystem.R.color.yellow
import kotlin.math.abs

/**
 * A helper class that handles swipe actions on the favorite and watchlist fragments.
 * This class extends [ItemTouchHelper.Callback] to manage swipe gestures (left and right)
 *
 * @property onSwipeLeft A callback function when an item is swiped left.
 * @property onSwipeRight A callback function triggered when an item is swiped right.
 * @property context The context used for accessing resources and drawing UI elements.
 * @property deleteIconResId The resource ID for the icon for delete action
 * @property actionIconResId The resource ID for the icon adding to favorite/watchlist.
 *
 * This class overrides methods from [ItemTouchHelper.Callback] to:
 *  - Set swipe directions (left/right) in [getMovementFlags].
 *  - Handle the swipe action in [onSwiped], invoking callback based on swipe direction.
 *  - Draw custom background, color transition, and icons during the swipe in [onChildDraw].
 *  - Customize swipe behavior, such as setting a threshold in [getSwipeThreshold].
 */
class SwipeCallbackHelper(
  private val onSwipeLeft: (RecyclerView.ViewHolder, Int) -> Unit,
  private val onSwipeRight: (RecyclerView.ViewHolder, Int) -> Unit,
  private val context: Context,
  private val deleteIconResId: Int,
  private val actionIconResId: Int,
) : ItemTouchHelper.Callback() {

  override fun getMovementFlags(
    recyclerView: RecyclerView,
    viewHolder: RecyclerView.ViewHolder,
  ): Int {
    val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
    return makeMovementFlags(0, swipeFlags)
  }

  override fun onMove(
    recyclerView: RecyclerView,
    viewHolder: RecyclerView.ViewHolder,
    target: RecyclerView.ViewHolder,
  ): Boolean = false

  override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    val position = viewHolder.bindingAdapterPosition
    if (direction == ItemTouchHelper.START) { // swipe left
      onSwipeLeft(viewHolder, position)
    } else if (direction == ItemTouchHelper.END) { // swipe right
      onSwipeRight(viewHolder, position)
    }
  }

  override fun onChildDraw(
    c: Canvas,
    recyclerView: RecyclerView,
    viewHolder: RecyclerView.ViewHolder,
    dX: Float,
    dY: Float,
    actionState: Int,
    isCurrentlyActive: Boolean,
  ) {
    val background = ColorDrawable()
    val itemView = viewHolder.itemView
    val itemHeight = itemView.bottom - itemView.top
    val itemWidth = itemView.right - itemView.left // Total width of the item
    val evaluator = ArgbEvaluator()

    // Calculate swipeFraction based on SWIPE_THRESHOLD
    val swipeFraction = (abs(dX) / (itemWidth * SWIPE_THRESHOLD)).coerceIn(0f, 1f)
    val gray = ContextCompat.getColor(context, gray_900) // Starting color

    if (dX > 0) { // swipe right
      val deleteIcon = requireNotNull(ContextCompat.getDrawable(context, deleteIconResId))
      val intrinsicWidth = deleteIcon.intrinsicWidth
      val intrinsicHeight = deleteIcon.intrinsicHeight

      // Interpolate between gray and red matte for delete action
      background.color =
        evaluator.evaluate(swipeFraction, gray, ContextCompat.getColor(context, red_matte)) as Int
      background.setBounds(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
      background.draw(c)

      deleteIcon.setBounds(
        itemView.left + (itemHeight - intrinsicHeight) / BOUND_SIZE,
        itemView.top + (itemHeight - intrinsicHeight) / 2,
        itemView.left + (itemHeight - intrinsicHeight) / BOUND_SIZE + intrinsicWidth,
        itemView.top + (itemHeight - intrinsicHeight) / 2 + intrinsicHeight,
      )
      deleteIcon.draw(c)
    } else if (dX < 0) { // swipe left
      val actionIcon = requireNotNull(ContextCompat.getDrawable(context, actionIconResId))
      val intrinsicWidth = actionIcon.intrinsicWidth
      val intrinsicHeight = actionIcon.intrinsicHeight

      // Interpolate between gray and yellow for add action
      background.color =
        evaluator.evaluate(swipeFraction, gray, ContextCompat.getColor(context, yellow)) as Int
      background
        .setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
      background.draw(c)

      actionIcon.setBounds(
        itemView.right - (itemHeight - intrinsicHeight) / BOUND_SIZE - intrinsicWidth,
        itemView.top + (itemHeight - intrinsicHeight) / 2,
        itemView.right - (itemHeight - intrinsicHeight) / BOUND_SIZE,
        itemView.top + (itemHeight - intrinsicHeight) / 2 + intrinsicHeight,
      )
      actionIcon.draw(c)
    }

    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
  }

  override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float = SWIPE_THRESHOLD

  companion object {
    const val BOUND_SIZE = 4
  }
}
