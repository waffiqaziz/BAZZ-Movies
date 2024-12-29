package com.waffiq.bazz_movies.feature.favorite.domain.repository

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.ResultItem
import kotlinx.coroutines.flow.Flow

interface IFavoriteRepository {

  fun getPagingFavoriteMovies(sessionId: String): Flow<PagingData<ResultItem>>

  fun getPagingFavoriteTv(sessionId: String): Flow<PagingData<ResultItem>>
}
