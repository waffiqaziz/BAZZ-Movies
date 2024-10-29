package com.waffiq.bazz_movies.feature.detail.domain.usecase.getDetailTv

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.model.ResultItem
import com.waffiq.bazz_movies.core.domain.model.Stated
import com.waffiq.bazz_movies.core.domain.model.detail.DetailMovieTvUsed
import com.waffiq.bazz_movies.core.domain.model.detail.MovieTvCredits
import com.waffiq.bazz_movies.core.domain.model.detail.tv.ExternalTvID
import com.waffiq.bazz_movies.core.utils.result.NetworkResult
import kotlinx.coroutines.flow.Flow

interface GetDetailTvUseCase {
  suspend fun getDetailTv(tvId: Int, userRegion: String): Flow<NetworkResult<DetailMovieTvUsed>>
  suspend fun getExternalTvId(tvId: Int): Flow<NetworkResult<ExternalTvID>>
  suspend fun getTrailerLinkTv(tvId: Int): Flow<NetworkResult<String>>
  suspend fun getCreditTv(tvId: Int): Flow<NetworkResult<MovieTvCredits>>
  suspend fun getStatedTv(sessionId: String, tvId: Int): Flow<NetworkResult<Stated>>
  fun getPagingTvRecommendation(tvId: Int): Flow<PagingData<ResultItem>>
}
