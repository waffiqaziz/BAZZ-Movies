package com.waffiq.bazz_movies.feature.watchlist.domain.usecase.composite

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.feature.watchlist.domain.model.FavoriteActionResult
import kotlinx.coroutines.flow.Flow

interface CheckAndAddToFavoriteUseCase {
  fun addMovieToFavorite(movieId: Int): Flow<Outcome<FavoriteActionResult>>
  fun addTvToFavorite(tvId: Int): Flow<Outcome<FavoriteActionResult>>
}
