package com.waffiq.bazz_movies.feature.detail.domain.usecase.collection

import com.waffiq.bazz_movies.core.models.Outcome
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.DetailCollections
import com.waffiq.bazz_movies.feature.detail.domain.repository.IDetailRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovieCollectionInteractor @Inject constructor(
  private val detailRepository: IDetailRepository,
) : GetMovieCollectionUseCase {

  override fun getMovieCollection(collectionId: Int): Flow<Outcome<DetailCollections>> =
    detailRepository.getMovieCollection(collectionId)
}
