package com.waffiq.bazz_movies.feature.search.utils

import android.content.Context
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.waffiq.bazz_movies.core.common.Genre
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_IMG_LINK_POSTER_W185
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_backdrop_error
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_action
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_adventure
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_animation
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_comedy
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_crime
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_documentary
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_drama
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_family
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_fantasy
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_historical
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_horror
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_kids
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_mistery
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_musical
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_news
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_romance
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_sci_fi
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_soap
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_talk
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_thriller
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_tv_movie
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_tv_show
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_war
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_western
import com.waffiq.bazz_movies.core.models.ProfileImageable
import com.waffiq.bazz_movies.core.uihelper.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.core.utils.LayoutHelper.initLinearLayoutManagerVertical
import com.waffiq.bazz_movies.feature.search.domain.model.KnownForItem

object SearchHelper {

  /**
   * Sets up a vertical [RecyclerView] with a [PagingDataAdapter]
   * and a loading footer that supports retry.
   */
  fun RecyclerView.setupRecyclerView(context: Context, pagingAdapter: PagingDataAdapter<*, *>) {
    this.apply {
      layoutManager = initLinearLayoutManagerVertical(context)
      itemAnimator = DefaultItemAnimator()
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

  @Suppress("CyclomaticComplexMethod")
  fun Genre.iconRes(): Int =
    when (this) {
      Genre.ACTION, Genre.ACTION_AND_ADVENTURE -> ic_genre_action
      Genre.ADVENTURE -> ic_genre_adventure
      Genre.ANIMATION -> ic_genre_animation
      Genre.COMEDY -> ic_genre_comedy
      Genre.CRIME -> ic_genre_crime
      Genre.DOCUMENTARY -> ic_genre_documentary
      Genre.DRAMA -> ic_genre_drama
      Genre.FAMILY -> ic_genre_family
      Genre.KIDS -> ic_genre_kids
      Genre.FANTASY, Genre.SCI_FI_AND_FANTASY -> ic_genre_fantasy
      Genre.HISTORY -> ic_genre_historical
      Genre.HORROR -> ic_genre_horror
      Genre.MUSIC -> ic_genre_musical
      Genre.MYSTERY -> ic_genre_mistery
      Genre.NEWS -> ic_genre_news
      Genre.TALK -> ic_genre_talk
      Genre.REALITY -> ic_genre_tv_show
      Genre.ROMANCE -> ic_genre_romance
      Genre.SCIENCE_FICTION -> ic_genre_sci_fi
      Genre.SOAP -> ic_genre_soap
      Genre.THRILLER -> ic_genre_thriller
      Genre.TV_MOVIE -> ic_genre_tv_movie
      Genre.WAR, Genre.WAR_AND_POLITICS -> ic_genre_war
      Genre.WESTERN -> ic_genre_western
    }
}
