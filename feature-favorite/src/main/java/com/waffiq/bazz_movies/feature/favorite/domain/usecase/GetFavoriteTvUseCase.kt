package com.waffiq.bazz_movies.feature.favorite.domain.usecase

import com.waffiq.bazz_movies.core.model.ResultItem
import kotlinx.coroutines.flow.Flow

interface GetFavoriteTvUseCase {
  fun getPagingFavoriteTv(sessionId: String): Flow<androidx.paging.PagingData<ResultItem>>
}
