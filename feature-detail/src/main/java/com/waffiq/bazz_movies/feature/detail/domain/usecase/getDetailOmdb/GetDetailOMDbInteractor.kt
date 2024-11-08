package com.waffiq.bazz_movies.feature.detail.domain.usecase.getDetailOmdb

import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import com.waffiq.bazz_movies.feature.detail.domain.repository.IDetailRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDetailOMDbInteractor @Inject constructor(
  private val detailRepository: IDetailRepository
) : GetDetailOMDbUseCase {
  override suspend fun getDetailOMDb(imdbId: String): Flow<NetworkResult<OMDbDetails>> =
    detailRepository.getDetailOMDb(imdbId)
}
