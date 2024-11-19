package com.waffiq.bazz_movies.feature.detail.domain.usecase.getDetailTv

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import com.waffiq.bazz_movies.feature.detail.domain.model.DetailMovieTvUsed
import com.waffiq.bazz_movies.feature.detail.domain.model.MovieTvCredits
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.ExternalTvID
import kotlinx.coroutines.flow.Flow

interface GetDetailTvUseCase {
  suspend fun getDetailTv(tvId: Int, userRegion: String): Flow<NetworkResult<DetailMovieTvUsed>>
  suspend fun getExternalTvId(tvId: Int): Flow<NetworkResult<ExternalTvID>>
  suspend fun getTrailerLinkTv(tvId: Int): Flow<NetworkResult<String>>
  suspend fun getCreditTv(tvId: Int): Flow<NetworkResult<MovieTvCredits>>
  fun getPagingTvRecommendation(tvId: Int): Flow<PagingData<com.waffiq.bazz_movies.core.data.ResultItem>>
}
