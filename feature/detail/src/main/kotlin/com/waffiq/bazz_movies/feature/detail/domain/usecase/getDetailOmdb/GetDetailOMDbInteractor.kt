package com.waffiq.bazz_movies.feature.detail.domain.usecase.getDetailOmdb

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.feature.detail.domain.repository.IDetailRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDetailOMDbInteractor @Inject constructor(
  private val detailRepository: IDetailRepository
) : GetDetailOMDbUseCase {
  override suspend fun getDetailOMDb(imdbId: String): Flow<Outcome<OMDbDetails>> =
    detailRepository.getDetailOMDb(imdbId)
}
