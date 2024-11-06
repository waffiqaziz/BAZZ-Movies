package com.waffiq.bazz_movies.navigation

import android.content.Context
import com.waffiq.bazz_movies.core.domain.model.MovieTvCastItem
import com.waffiq.bazz_movies.core.domain.model.ResultItem

interface Navigator {
  fun openPersonDetails(context: Context, cast: MovieTvCastItem)
  fun openDetails(context: Context, resultItem: ResultItem)
}