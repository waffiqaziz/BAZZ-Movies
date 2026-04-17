package com.waffiq.bazz_movies.feature.detail.domain.usecase.getOmdbDetail

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvExternalIds
import com.waffiq.bazz_movies.feature.detail.domain.repository.IDetailRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import javax.inject.Inject

class GetOMDbDetailInteractor @Inject constructor(
  private val detailRepository: IDetailRepository,
) : GetOMDbDetailUseCase {
  override fun getOMDbDetails(imdbId: String): Flow<Outcome<OMDbDetails>> =
    detailRepository.getOMDbDetails(imdbId)

  override fun getTvAllScore(tvId: Int): Flow<Outcome<OMDbDetails>> =
    detailRepository.getTvExternalIds(tvId)
      // Intentionally filters out Loading/Error if external IDs fail, the flow completes empty.
      // This is expected: OMDb score is a supplementary rating view, so failures should silently
      // show nothing rather than trigger an error page.
      .filterIsInstance<Outcome.Success<TvExternalIds>>()
      .take(1)
      .flatMapConcat { outcome ->
        val imdbId = outcome.data.imdbId
        if (imdbId.isNullOrEmpty()) {
          flowOf(Outcome.Error(""))
        } else {
          detailRepository.getOMDbDetails(imdbId)
        }
      }
}
