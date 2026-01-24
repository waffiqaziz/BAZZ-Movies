package com.waffiq.bazz_movies.feature.detail.domain.usecase.composite

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvExternalIds
import com.waffiq.bazz_movies.feature.detail.domain.usecase.getOmdbDetail.GetOMDbDetailUseCase
import com.waffiq.bazz_movies.feature.detail.domain.usecase.getTvDetail.GetTvDetailUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import javax.inject.Inject

class GetTvAllScoreInteractor @Inject constructor(
  private val getTvDetailUseCase: GetTvDetailUseCase,
  private val getOMDbDetailUseCase: GetOMDbDetailUseCase,
): GetTvAllScoreUseCase {

  override fun getTvAllScore(tvId: Int): Flow<Outcome<OMDbDetails>> =
    getTvDetailUseCase.getTvExternalIds(tvId)
      .filterIsInstance<Outcome.Success<TvExternalIds>>() // Only passes or take Success
      .take(1)
      .flatMapConcat { outcome ->
        when(outcome) {
          is Outcome.Success -> {
            val imdbId = outcome.data.imdbId
            if (imdbId.isNullOrEmpty()) {
              flowOf(Outcome.Error(""))
            } else {
              getOMDbDetailUseCase.getOMDbDetails(imdbId)
            }
          }
        }
      }
}
