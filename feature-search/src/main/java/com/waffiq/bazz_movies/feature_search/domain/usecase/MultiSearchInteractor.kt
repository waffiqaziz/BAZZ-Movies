package com.waffiq.bazz_movies.feature_search.domain.usecase

import androidx.paging.PagingData
import androidx.paging.filter
import com.waffiq.bazz_movies.core.domain.model.search.ResultsItemSearch
import com.waffiq.bazz_movies.core.domain.repository.IMoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MultiSearchInteractor @Inject constructor(
  private val multiSearchRepository: IMoviesRepository
) : MultiSearchUseCase {
  override fun search(query: String): Flow<PagingData<ResultsItemSearch>> =
    multiSearchRepository.getPagingSearch(query)
      .map { pagingData ->
        pagingData.filter { data ->
          !data.backdropPath.isNullOrEmpty() ||
            !data.posterPath.isNullOrEmpty() ||
            !data.profilePath.isNullOrEmpty()
        }
      }
}
