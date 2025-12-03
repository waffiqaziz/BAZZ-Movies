package com.waffiq.bazz_movies.feature.detail.domain.usecase.getOmdbDetail

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.OMDbDetails
import kotlinx.coroutines.flow.Flow

fun interface GetOMDbDetailUseCase {
  fun getOMDbDetails(imdbId: String): Flow<Outcome<OMDbDetails>>
}
