package com.waffiq.bazz_movies.feature.search.domain.usecase

import androidx.paging.PagingData
import androidx.paging.filter
import com.waffiq.bazz_movies.feature.search.domain.model.MultiSearchItem
import com.waffiq.bazz_movies.feature.search.domain.repository.ISearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MultiSearchInteractor @Inject constructor(
  private val searchRepository: ISearchRepository
) : MultiSearchUseCase {
  override fun search(query: String): Flow<PagingData<MultiSearchItem>> =
    searchRepository.search(query)
      .map { pagingData ->
        pagingData.filter { data ->
          !data.backdropPath.isNullOrEmpty() ||
            !data.posterPath.isNullOrEmpty() ||
            !data.profilePath.isNullOrEmpty()
        }
      }
}
