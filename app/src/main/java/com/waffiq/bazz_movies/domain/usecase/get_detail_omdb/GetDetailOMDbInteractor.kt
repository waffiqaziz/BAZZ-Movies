package com.waffiq.bazz_movies.domain.usecase.get_detail_omdb

import com.waffiq.bazz_movies.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.domain.repository.IMoviesRepository
import com.waffiq.bazz_movies.utils.resultstate.NetworkResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDetailOMDbInteractor @Inject constructor(
  private val getDetailOMDbRepository: IMoviesRepository
) : GetDetailOMDbUseCase {
  override suspend fun getDetailOMDb(imdbId: String): Flow<NetworkResult<OMDbDetails>> =
    getDetailOMDbRepository.getDetailOMDb(imdbId)
}
