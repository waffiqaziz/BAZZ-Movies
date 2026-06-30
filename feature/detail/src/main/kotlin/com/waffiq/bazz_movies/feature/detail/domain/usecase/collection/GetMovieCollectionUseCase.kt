package com.waffiq.bazz_movies.feature.detail.domain.usecase.collection

import com.waffiq.bazz_movies.core.models.Outcome
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.DetailCollections
import kotlinx.coroutines.flow.Flow

fun interface GetMovieCollectionUseCase {
  fun getMovieCollection(collectionId: Int): Flow<Outcome<DetailCollections>>
}
