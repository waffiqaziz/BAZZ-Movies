package com.waffiq.bazz_movies.domain.usecase.get_favorite

import androidx.paging.PagingData
import com.waffiq.bazz_movies.domain.model.ResultItem
import kotlinx.coroutines.flow.Flow

interface GetFavoriteTvUseCase {
  fun getPagingFavoriteTv(sessionId: String): Flow<PagingData<ResultItem>>
}