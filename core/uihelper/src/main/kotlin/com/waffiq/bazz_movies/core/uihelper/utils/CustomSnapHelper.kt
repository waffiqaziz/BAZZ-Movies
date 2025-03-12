package com.waffiq.bazz_movies.core.uihelper.utils

import android.view.View
import androidx.annotation.VisibleForTesting
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * Custom implementation of [LinearSnapHelper] that adjusts the snap behavior
 * by applying an offset to the calculated snap position.
 *
 * This helper class is used to modify the default behavior of snapping to a specific
 * position in a horizontally scrolling [RecyclerView]. By default, it applies an offset
 * to the snap position, which can be customized via the [offsetPx] parameter.
 *
 * The custom offset allows for fine-tuning the snap position, which can be useful
 * in various scenarios such as when the snapping should not occur exactly at the
 * edges of the items, but slightly off-center or to accommodate margins/paddings.
 */
class CustomSnapHelper(private val offsetPx: Int = DEFAULT_OFFSET) : LinearSnapHelper() {

  companion object {
    // default offset value used if no custom value is provided
    private const val DEFAULT_OFFSET = -20
  }

  /**
   * Calculates the distance to the final snap position for a target view.
   * This method is overridden to apply a custom offset to the calculated
   * snap distance, which allows adjusting the snapping behavior.
   *
   * @param layoutManager The [RecyclerView.LayoutManager] used by the [RecyclerView].
   * @param targetView The [View] that should be snapped into place.
   *
   * @return An array of integers representing the distance (x and y) to move
   *         in order to snap the target view into the final position. The
   *         x-distance is adjusted by the custom [offsetPx] value if the
   *         layout manager supports horizontal scrolling.
   */
  @Suppress("ReturnCount")
  override fun calculateDistanceToFinalSnap(
    layoutManager: RecyclerView.LayoutManager,
    targetView: View
  ): IntArray? {
    // ensure the targetView is a child of the RecyclerView
    if (layoutManager.childCount == 0) {
      return null
    }

    // get the default snap distances using the base class implementation
    val distances = getSuperDistances(layoutManager, targetView) ?: return null

    // if the layout manager allows horizontal scrolling, apply the custom offset
    if (canLayoutManagerScrollHorizontally(layoutManager)) {
      distances[0] -= offsetPx
    }

    return distances
  }

  // methods to make easier for testing
  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  fun getSuperDistances(
    layoutManager: RecyclerView.LayoutManager,
    targetView: View
  ): IntArray? {
    return super.calculateDistanceToFinalSnap(layoutManager, targetView)
  }

  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  fun canLayoutManagerScrollHorizontally(
    layoutManager: RecyclerView.LayoutManager
  ): Boolean {
    return layoutManager.canScrollHorizontally()
  }
}
