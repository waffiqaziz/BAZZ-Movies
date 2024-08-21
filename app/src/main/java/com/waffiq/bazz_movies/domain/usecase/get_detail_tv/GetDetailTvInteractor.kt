package com.waffiq.bazz_movies.domain.usecase.get_detail_tv

import androidx.paging.PagingData
import com.waffiq.bazz_movies.domain.model.ResultItem
import com.waffiq.bazz_movies.domain.model.Stated
import com.waffiq.bazz_movies.domain.model.detail.MovieTvCredits
import com.waffiq.bazz_movies.domain.model.detail.tv.DetailTv
import com.waffiq.bazz_movies.domain.model.detail.tv.ExternalTvID
import com.waffiq.bazz_movies.domain.repository.IMoviesRepository
import com.waffiq.bazz_movies.utils.Helper
import com.waffiq.bazz_movies.utils.NetworkResult
import com.waffiq.bazz_movies.utils.Status
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetDetailTvInteractor(
  private val getDetailTvRepository: IMoviesRepository
) : GetDetailTvUseCase {
  override suspend fun getDetailTv(tvId: Int): Flow<NetworkResult<DetailTv>> =
    getDetailTvRepository.getDetailTv(tvId)

  override suspend fun getExternalTvId(tvId: Int): Flow<NetworkResult<ExternalTvID>> =
    getDetailTvRepository.getExternalTvId(tvId)

  override suspend fun getTrailerLinkTv(tvId: Int): Flow<NetworkResult<String>> =
    getDetailTvRepository.getTrailerLinkTv(tvId).map { networkResult ->
      when (networkResult.status) {
        Status.SUCCESS -> {
          // Extract and transform the data
          NetworkResult.success(
            networkResult.data?.let { Helper.transformLink(it) } ?: ""
          )
        }

        Status.ERROR -> NetworkResult.error(networkResult.message ?: "Unknown error")
        Status.LOADING -> NetworkResult.loading()
      }
    }

  override suspend fun getCreditTv(tvId: Int): Flow<NetworkResult<MovieTvCredits>> =
    getDetailTvRepository.getCreditTv(tvId)

  override suspend fun getStatedTv(sessionId: String, tvId: Int): Flow<NetworkResult<Stated>> =
    getDetailTvRepository.getStatedTv(sessionId, tvId)

  override fun getPagingTvRecommendation(tvId: Int): Flow<PagingData<ResultItem>> =
    getDetailTvRepository.getPagingTvRecommendation(tvId)
}