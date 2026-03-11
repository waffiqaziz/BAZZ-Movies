package com.waffiq.bazz_movies.core.movie.domain.usecase.listtv

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.movie.domain.repository.IMoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetListTvInteractor @Inject constructor(private val movieRepository: IMoviesRepository) :
  GetListTvUseCase {
  override fun getPopularTv(region: String): Flow<PagingData<MediaItem>> =
    movieRepository.getPopularTv(region)

  override fun getAiringThisWeekTv(region: String): Flow<PagingData<MediaItem>> =
    movieRepository.getAiringThisWeekTv(region)

  override fun getAiringTodayTv(region: String): Flow<PagingData<MediaItem>> =
    movieRepository.getAiringTodayTv(region)

  override fun getTopRatedTv(): Flow<PagingData<MediaItem>> = movieRepository.getTopRatedTv()
}
