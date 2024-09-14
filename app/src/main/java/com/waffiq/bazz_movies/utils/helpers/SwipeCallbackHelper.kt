package com.waffiq.bazz_movies.utils.helpers

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.waffiq.bazz_movies.utils.common.Constants.SWIPE_THRESHOLD

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
    c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
    dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
  ) {
    val background = ColorDrawable()
    val itemView = viewHolder.itemView
    val itemHeight = itemView.bottom - itemView.top

    if (dX > 0) { // swipe right
      val deleteIcon = ContextCompat.getDrawable(context, deleteIconResId)
      val intrinsicWidth = deleteIcon?.intrinsicWidth ?: 0
      val intrinsicHeight = deleteIcon?.intrinsicHeight ?: 0

      background.color = ContextCompat.getColor(context, deleteColor)
      background.setBounds(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
      background.draw(c)

      deleteIcon?.setBounds(
        itemView.left + (itemHeight - intrinsicHeight) / 2,
        itemView.top + (itemHeight - intrinsicHeight) / 2,
        itemView.left + (itemHeight - intrinsicHeight) / 2 + intrinsicWidth,
        itemView.top + (itemHeight - intrinsicHeight) / 2 + intrinsicHeight
      )
      deleteIcon?.draw(c)
    } else if (dX < 0) { // swipe left
      val actionIcon = ContextCompat.getDrawable(context, actionIconResId)
      val intrinsicWidth = actionIcon?.intrinsicWidth ?: 0
      val intrinsicHeight = actionIcon?.intrinsicHeight ?: 0

      background.color = ContextCompat.getColor(context, actionColor)
      background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
      background.draw(c)

      actionIcon?.setBounds(
        itemView.right - (itemHeight - intrinsicHeight) / 2 - intrinsicWidth,
        itemView.top + (itemHeight - intrinsicHeight) / 2,
        itemView.right - (itemHeight - intrinsicHeight) / 2,
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
