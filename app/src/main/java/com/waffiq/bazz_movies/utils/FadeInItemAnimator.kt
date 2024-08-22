package com.waffiq.bazz_movies.utils

import android.view.animation.AlphaAnimation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class FadeInItemAnimator : DefaultItemAnimator() {

  private var currentRecyclerView: RecyclerView? = null
  private var pendingRecyclerViews: MutableList<RecyclerView> = mutableListOf()

  override fun animateAdd(holder: RecyclerView.ViewHolder): Boolean {
    val view = holder.itemView
    val alphaAnimation = AlphaAnimation(0f, 1f).apply {
      duration = 2000 // Adjust the duration as per your preference
      fillAfter = true
      setAnimationListener(object : android.view.animation.Animation.AnimationListener {
        override fun onAnimationStart(animation: android.view.animation.Animation?) {
          dispatchAddStarting(holder)
        }

        override fun onAnimationEnd(animation: android.view.animation.Animation?) {
          dispatchAddFinished(holder)
          if (currentRecyclerView == null) {
            startNextAnimation()
          }
        }

        override fun onAnimationRepeat(animation: android.view.animation.Animation?) {
        }
      })
    }

    // Start the animation immediately if there's no ongoing animation
    if (currentRecyclerView == null) {
      currentRecyclerView = holder.itemView.parent as? RecyclerView
      view.startAnimation(alphaAnimation)
    } else {
      // Otherwise, queue the RecyclerView for animation
      val recyclerView = holder.itemView.parent as? RecyclerView
      if (recyclerView != null && recyclerView != currentRecyclerView) {
        pendingRecyclerViews.add(recyclerView)
      }
    }

    return true
  }

  private fun startNextAnimation() {
    currentRecyclerView = null
    if (pendingRecyclerViews.isNotEmpty()) {
      val nextRecyclerView = pendingRecyclerViews.removeAt(0)
      currentRecyclerView = nextRecyclerView

      // Get the first visible item position
      val layoutManager = nextRecyclerView.layoutManager
      val firstVisibleItemPosition = findFirstVisibleItemPosition(layoutManager)

      // If a visible item is found, animate its corresponding ViewHolder
      if (firstVisibleItemPosition != RecyclerView.NO_POSITION) {
        val holder = nextRecyclerView.findViewHolderForAdapterPosition(firstVisibleItemPosition)
        if (holder != null) {
          animateAdd(holder)
        }
      }
    }
  }

  private fun findFirstVisibleItemPosition(layoutManager: RecyclerView.LayoutManager?): Int {
    return when (layoutManager) {
      is LinearLayoutManager -> layoutManager.findFirstVisibleItemPosition()
      is StaggeredGridLayoutManager -> {
        val firstVisibleItems = layoutManager.findFirstVisibleItemPositions(null)
        return firstVisibleItems.minOrNull() ?: RecyclerView.NO_POSITION
      }
      // Add other layout managers as needed
      else -> RecyclerView.NO_POSITION
    }
  }
}