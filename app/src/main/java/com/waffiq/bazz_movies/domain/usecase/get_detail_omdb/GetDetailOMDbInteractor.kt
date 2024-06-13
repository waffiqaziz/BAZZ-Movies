package com.waffiq.bazz_movies.domain.usecase.get_detail_omdb

import com.waffiq.bazz_movies.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.domain.repository.IMoviesRepository
import com.waffiq.bazz_movies.utils.NetworkResult
import kotlinx.coroutines.flow.Flow

class GetDetailOMDbInteractor(
  private val getDetailOMDbRepository: IMoviesRepository
) : GetDetailOMDbUseCase {
  override suspend fun getDetailOMDb(imdbId: String): Flow<NetworkResult<OMDbDetails>> =
    getDetailOMDbRepository.getDetailOMDb(imdbId)
}