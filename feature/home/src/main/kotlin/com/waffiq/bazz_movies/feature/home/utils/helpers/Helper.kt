package com.waffiq.bazz_movies.feature.home.utils.helpers

import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_IMG_LINK_BACKDROP_W780
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_backdrop_error_filled
import com.waffiq.bazz_movies.core.models.Dateable
import com.waffiq.bazz_movies.core.models.Imageble

object Helper {

  val Dateable.year: String
    get() = releaseDate?.take(n = 4) ?: firstAirDate?.take(n = 4) ?: "N/A"

  val Float?.rating: Float
    get() = (this ?: 0F) / 2

  val Imageble.backdropSource: Any
    get() = when {
      !backdropPath.isNullOrEmpty() -> TMDB_IMG_LINK_BACKDROP_W780 + backdropPath
      else -> ic_backdrop_error_filled
    }
}
