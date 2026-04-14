package com.waffiq.bazz_movies.core.data.domain.repository

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.PostResult
import kotlinx.coroutines.flow.Flow

interface IMoviesRepository {

  // region LIST
  fun getTopRatedMovies(): Flow<PagingData<MediaItem>>
  fun getPopularMovies(): Flow<PagingData<MediaItem>>
  fun getUpcomingMovies(region: String): Flow<PagingData<MediaItem>>
  fun getPlayingNowMovies(region: String): Flow<PagingData<MediaItem>>
  fun getMovieRecommendation(movieId: Int): Flow<PagingData<MediaItem>>
  // endregion LIST

  fun getMovieState(sessionId: String, movieId: Int): Flow<Outcome<MediaState>>

  fun postMovieRate(
    sessionId: String,
    rating: Float,
    movieId: Int,
  ): Flow<Outcome<PostResult>>
}
