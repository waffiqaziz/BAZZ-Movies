package com.waffiq.bazz_movies.core.uihelper.utils

import android.R.anim.fade_out
import android.content.Context
import android.graphics.text.LineBreaker
import android.os.Build
import android.text.Layout
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.waffiq.bazz_movies.core.common.utils.Constants.DEBOUNCE_VERY_LONG
import kotlin.collections.forEach

/**
 * A utility object that provides various helper functions for UI customization and behavior
 */
object Helpers {

  /**
   * Justifies the text in a [TextView] based on the Android version. It uses
   * `LineBreaker.JUSTIFICATION_MODE_INTER_WORD` on Android Q and above, and
   * `Layout.JUSTIFICATION_MODE_INTER_WORD` on Android O to P.
   *
   * @param textView The [TextView] whose text will be justified.
   */
  @Suppress("WrongConstant")
  fun justifyTextView(textView: TextView) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      textView.justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      textView.justificationMode = Layout.JUSTIFICATION_MODE_INTER_WORD
    }
  }

  /**
   * Returns a long-duration fade-out animation.
   *
   * @param context The [Context] used to load the animation.
   * @return The [Animation] object with the specified duration.
   */
  fun animFadeOutLong(context: Context): Animation {
    val animation = AnimationUtils.loadAnimation(context, fade_out)
    animation.duration = DEBOUNCE_VERY_LONG
    return animation
  }

  /**
   * Sets up a list of RecyclerViews with horizontal snap behavior using a [CustomSnapHelper].
   * This enables smooth snapping of items when the user scrolls the RecyclerView.
   *
   * @param recyclerViews A list of [RecyclerView]s to apply the snap behavior.
   * @param layoutManager An optional [LinearLayoutManager] for configuring the layout of the
   *                      RecyclerView.
   */
  fun setupRecyclerViewsWithSnap(
    recyclerViews: List<RecyclerView>,
    layoutManager: LinearLayoutManager? = null,
  ) {
    recyclerViews.forEach { recyclerView ->

      // Safely attach SnapHelper
      if (recyclerView.onFlingListener == null) {
        recyclerView.layoutManager = layoutManager ?: LinearLayoutManager(
          recyclerView.context,
          LinearLayoutManager.HORIZONTAL,
          false,
        )
        CustomSnapHelper().attachToRecyclerView(recyclerView)
      }
    }
  }

  /**
   * Sets up a list of RecyclerViews with horizontal snap behavior using a [GridLayoutManager].
   * This enables snapping with a grid layout configuration.
   *
   * @param n The number of columns in the grid layout (default is 2).
   * @param recyclerViews A list of [RecyclerView]s to apply the snap behavior.
   * @param layoutManager An optional [LinearLayoutManager] for configuring the layout of the
   *        RecyclerView.
   */
  fun setupRecyclerViewsWithSnapGridLayout(
    n: Int = 2,
    recyclerViews: List<RecyclerView>,
    layoutManager: LinearLayoutManager? = null,
  ) {
    recyclerViews.forEach { recyclerView ->

      // Safely attach SnapHelper
      if (recyclerView.onFlingListener == null) {
        recyclerView.layoutManager =
          layoutManager ?: GridLayoutManager(
            recyclerView.context,
            n,
            GridLayoutManager.HORIZONTAL,
            false,
          )
        CustomSnapHelper().attachToRecyclerView(recyclerView)
      }
    }
  }
}
