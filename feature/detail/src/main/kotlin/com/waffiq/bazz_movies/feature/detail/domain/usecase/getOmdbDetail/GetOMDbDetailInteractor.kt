package com.waffiq.bazz_movies.feature.detail.domain.usecase.getOmdbDetail

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.feature.detail.domain.repository.IDetailRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOMDbDetailInteractor @Inject constructor(
  private val detailRepository: IDetailRepository,
) : GetOMDbDetailUseCase {
  override fun getOMDbDetails(imdbId: String): Flow<Outcome<OMDbDetails>> =
    detailRepository.getOMDbDetails(imdbId)
}
