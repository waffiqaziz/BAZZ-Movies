package com.waffiq.bazz_movies.feature.favorite.domain.usecase.composite

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.feature.favorite.domain.model.WatchlistActionResult
import kotlinx.coroutines.flow.Flow

interface CheckAndAddToWatchlistUseCase {
  fun addMovieToWatchlist(movieId: Int): Flow<Outcome<WatchlistActionResult>>
  fun addTvToWatchlist(tvId: Int): Flow<Outcome<WatchlistActionResult>>
}
