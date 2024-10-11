package com.waffiq.bazz_movies.core.domain.usecase.get_favorite

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.model.ResultItem
import kotlinx.coroutines.flow.Flow

interface GetFavoriteMovieUseCase {
  fun getPagingFavoriteMovies(sessionId: String): Flow<PagingData<ResultItem>>
}
