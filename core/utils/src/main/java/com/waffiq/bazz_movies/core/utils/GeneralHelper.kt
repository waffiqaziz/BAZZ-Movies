package com.waffiq.bazz_movies.core.utils

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

/**
 * A utility object that provides general helper functions for commonly used UI components and behaviors.
 * Currently, it includes a function to initialize a vertical LinearLayoutManager.
 */
object GeneralHelper {

  /**
   * Initializes and returns a LinearLayoutManager with a vertical orientation.
   * This is useful for setting up RecyclerViews to display items in a vertical list.
   *
   * @param context The context to use for initializing the LinearLayoutManager.
   * @return A LinearLayoutManager configured for vertical scrolling.
   */
  fun initLinearLayoutManagerVertical(context: Context) =
    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
}
