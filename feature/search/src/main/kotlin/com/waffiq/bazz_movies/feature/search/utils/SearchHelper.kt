package com.waffiq.bazz_movies.feature.search.utils

import android.content.Context
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_IMG_LINK_POSTER_W185
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_backdrop_error
import com.waffiq.bazz_movies.core.domain.ProfileImageable
import com.waffiq.bazz_movies.core.uihelper.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.core.utils.GeneralHelper.initLinearLayoutManagerVertical
import com.waffiq.bazz_movies.feature.search.domain.model.KnownForItem

object SearchHelper {

  /**
   * Sets up a vertical [RecyclerView] with a [PagingDataAdapter]
   * and a loading footer that supports retry.
   */
  fun RecyclerView.setupRecyclerView(context: Context, pagingAdapter: PagingDataAdapter<*, *>) {
    this.apply {
      layoutManager = initLinearLayoutManagerVertical(context)
      adapter = pagingAdapter.withLoadStateFooter(
        footer = LoadingStateAdapter { pagingAdapter.retry() },
      )
    }
  }

  /**
   * Builds a comma-separated string of known works.
   *
   * @return formatted string or empty if no valid data.
   */
  fun getKnownFor(knownForItem: List<KnownForItem>): String {
    var temp = ""
    knownForItem.forEach { item ->
      (item.title ?: item.name ?: item.originalName)
        ?.let { temp += "$it, " } // Uses title, falling back to name or originalName.
    }
    return if (temp.isNotEmpty()) temp.dropLast(2) else temp // Remove the trailing ", " if any
  }

  /**
   * Builds full profile image URL.
   *
   * @return full image URL or null if path is unavailable.
   */
  private val ProfileImageable.profileImageUrl: String?
    get() = profilePath
      ?.takeIf { it.isNotBlank() }
      ?.let { TMDB_IMG_LINK_POSTER_W185 + it }

  /**
   * Provides profile image source for UI.
   *
   * @return full image URL if available, otherwise fallback drawable.
   */
  val ProfileImageable.profileImageSource: Any
    get() = profileImageUrl ?: ic_backdrop_error
}
