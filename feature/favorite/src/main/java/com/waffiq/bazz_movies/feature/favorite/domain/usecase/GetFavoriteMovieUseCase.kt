package com.waffiq.bazz_movies.feature.favorite.domain.usecase

import com.waffiq.bazz_movies.core.model.ResultItem
import kotlinx.coroutines.flow.Flow

interface GetFavoriteMovieUseCase {
  fun getPagingFavoriteMovies(sessionId: String): Flow<androidx.paging.PagingData<ResultItem>>
}
