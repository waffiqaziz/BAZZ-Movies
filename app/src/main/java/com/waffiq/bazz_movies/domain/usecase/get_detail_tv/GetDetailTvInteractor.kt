package com.waffiq.bazz_movies.domain.usecase.get_detail_tv

import androidx.paging.PagingData
import com.waffiq.bazz_movies.domain.model.ResultItem
import com.waffiq.bazz_movies.domain.model.Stated
import com.waffiq.bazz_movies.domain.model.detail.tv.DetailTv
import com.waffiq.bazz_movies.domain.model.detail.tv.ExternalTvID
import com.waffiq.bazz_movies.domain.model.detail.MovieTvCredits
import com.waffiq.bazz_movies.domain.model.detail.Video
import com.waffiq.bazz_movies.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.domain.repository.IMoviesRepository
import com.waffiq.bazz_movies.utils.NetworkResult
import kotlinx.coroutines.flow.Flow

class GetDetailTvInteractor(
  private val getDetailTvRepository: IMoviesRepository
) : GetDetailTvUseCase {
  override suspend fun getDetailTv(tvId: Int): Flow<NetworkResult<DetailTv>> =
    getDetailTvRepository.getDetailTv(tvId)

  override suspend fun getExternalTvId(tvId: Int): Flow<NetworkResult<ExternalTvID>> =
    getDetailTvRepository.getExternalTvId(tvId)

  override suspend fun getVideoTv(tvId: Int): Flow<NetworkResult<Video>> =
    getDetailTvRepository.getVideoTv(tvId)

  override suspend fun getCreditTv(tvId: Int): Flow<NetworkResult<MovieTvCredits>> =
    getDetailTvRepository.getCreditTv(tvId)

  override suspend fun getStatedTv(sessionId: String, tvId: Int): Flow<NetworkResult<Stated>> =
    getDetailTvRepository.getStatedTv(sessionId, tvId)

  override fun getPagingTvRecommendation(tvId: Int): Flow<PagingData<ResultItem>> =
    getDetailTvRepository.getPagingTvRecommendation(tvId)
}