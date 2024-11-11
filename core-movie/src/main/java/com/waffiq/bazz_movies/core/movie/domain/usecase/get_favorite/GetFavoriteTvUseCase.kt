package com.waffiq.bazz_movies.core.movie.domain.usecase.get_favorite

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.model.ResultItem
import kotlinx.coroutines.flow.Flow

interface GetFavoriteTvUseCase {
  fun getPagingFavoriteTv(sessionId: String): Flow<PagingData<ResultItem>>
}
