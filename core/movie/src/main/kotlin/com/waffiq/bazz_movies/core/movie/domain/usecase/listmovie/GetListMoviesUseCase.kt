package com.waffiq.bazz_movies.core.movie.domain.usecase.listmovie

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaItem
import kotlinx.coroutines.flow.Flow

interface GetListMoviesUseCase {
  fun getTopRatedMovies(): Flow<PagingData<MediaItem>>
  fun getPopularMovies(): Flow<PagingData<MediaItem>>
  fun getTrendingThisWeek(region: String): Flow<PagingData<MediaItem>>
  fun getTrendingToday(region: String): Flow<PagingData<MediaItem>>
  fun getUpcomingMovies(region: String): Flow<PagingData<MediaItem>>
  fun getPlayingNowMovies(region: String): Flow<PagingData<MediaItem>>
}
