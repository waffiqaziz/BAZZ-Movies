package com.waffiq.bazz_movies.feature.list.domain.usecase

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.user.domain.usecase.userpreference.UserPrefUseCase
import com.waffiq.bazz_movies.feature.list.domain.repository.IListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.take
import javax.inject.Inject

class GetListInteractor @Inject constructor(
  private val listRepository: IListRepository,
  private val userPrefUseCase: UserPrefUseCase,
) : GetListUseCase {

  override fun getMovieByGenres(genres: String): Flow<PagingData<MediaItem>> =
    userPrefUseCase.getUserRegionPref().take(1).flatMapConcat {
      listRepository.getMovieByGenres(genres, it)
    }

  override fun getTvByGenres(genres: String): Flow<PagingData<MediaItem>> =
    userPrefUseCase.getUserRegionPref().take(1).flatMapConcat {
      listRepository.getTvByGenres(genres, it)
    }

  override fun getMovieByKeywords(keywords: String): Flow<PagingData<MediaItem>> =
    listRepository.getMovieByKeywords(keywords)

  override fun getTvByKeywords(keywords: String): Flow<PagingData<MediaItem>> =
    listRepository.getTvByKeywords(keywords)
}
