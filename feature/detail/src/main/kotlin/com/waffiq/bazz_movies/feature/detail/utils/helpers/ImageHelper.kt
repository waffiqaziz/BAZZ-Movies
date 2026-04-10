package com.waffiq.bazz_movies.feature.detail.utils.helpers

import com.waffiq.bazz_movies.core.common.utils.Constants.NOT_AVAILABLE
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_IMG_LINK_BACKDROP_ORIGINAL
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_IMG_LINK_POSTER_W500
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_backdrop_error_filled
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_poster_error
import com.waffiq.bazz_movies.core.domain.Imageble

object ImageHelper {

  private fun String?.isValidImagePath(): Boolean = !this.isNullOrBlank() && this != NOT_AVAILABLE

  private val Imageble.backdropOriginalUrl: String?
    get() = when {
      backdropPath.isValidImagePath() ->
        TMDB_IMG_LINK_BACKDROP_ORIGINAL + backdropPath

      posterPath.isValidImagePath() ->
        TMDB_IMG_LINK_POSTER_W500 + posterPath

      else -> null
    }

  val Imageble.backdropPathSource: String
    get() = listOf(backdropPath, posterPath)
      .firstOrNull { it.isValidImagePath() }
      .orEmpty()

  val Imageble.backdropOriginalSource: Any
    get() = backdropOriginalUrl ?: ic_backdrop_error_filled

  val Imageble.isBackdropNotAvailable: Boolean
    get() = backdropPath.isNullOrBlank() || backdropPath == NOT_AVAILABLE

  private val Imageble.posterUrl: String?
    get() = posterPath
      ?.takeIf { it.isNotBlank() && it != NOT_AVAILABLE }
      ?.let { TMDB_IMG_LINK_POSTER_W500 + it } // higher quality than on the list

  /** Used on detail page, to show poster in higher quality
   *
   * @return Poster URL if available, otherwise fallback to drawable
   */
  val Imageble.posterDetailSource: Any
    get() = posterUrl ?: ic_poster_error
}
