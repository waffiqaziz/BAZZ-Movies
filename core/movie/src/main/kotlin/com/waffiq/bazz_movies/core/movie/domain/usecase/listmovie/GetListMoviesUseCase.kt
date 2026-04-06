package com.waffiq.bazz_movies.core.movie.domain.usecase.listmovie

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaItem
import kotlinx.coroutines.flow.Flow

interface GetListMoviesUseCase {
  fun getTopRatedMovies(): Flow<PagingData<MediaItem>>
  fun getPopularMovies(): Flow<PagingData<MediaItem>>
  fun getTrendingThisWeek(): Flow<PagingData<MediaItem>>
  fun getTrendingToday(): Flow<PagingData<MediaItem>>
  fun getUpcomingMovies(): Flow<PagingData<MediaItem>>
  fun getPlayingNowMovies(): Flow<PagingData<MediaItem>>
  fun getMovieRecommendation(movieId: Int): Flow<PagingData<MediaItem>>
}
