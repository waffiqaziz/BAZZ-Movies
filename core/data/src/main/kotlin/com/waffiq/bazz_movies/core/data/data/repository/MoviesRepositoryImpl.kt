package com.waffiq.bazz_movies.core.data.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.data.domain.repository.IMoviesRepository
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.PostResult
import com.waffiq.bazz_movies.core.mappers.MediaItemMapper.toMediaItem
import com.waffiq.bazz_movies.core.mappers.MediaStateMapper.toMediaState
import com.waffiq.bazz_movies.core.mappers.NetworkResultMapper.toOutcome
import com.waffiq.bazz_movies.core.mappers.PostMapper.toPostResult
import com.waffiq.bazz_movies.core.network.data.remote.datasource.movie.MovieRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRepositoryImpl @Inject constructor(
  private val movieRemoteDataSource: MovieRemoteDataSource,
) : IMoviesRepository {

  override fun getTopRatedMovies(): Flow<PagingData<MediaItem>> =
    movieRemoteDataSource.getTopRatedMovies().map { pagingData ->
      pagingData.map { it.toMediaItem().copy(mediaType = MOVIE_MEDIA_TYPE) }
    }

  override fun getPopularMovies(): Flow<PagingData<MediaItem>> =
    movieRemoteDataSource.getPopularMovies().map { pagingData ->
      pagingData.map { it.toMediaItem().copy(mediaType = MOVIE_MEDIA_TYPE) }
    }

  override fun getUpcomingMovies(region: String): Flow<PagingData<MediaItem>> =
    movieRemoteDataSource.getUpcomingMovies(region).map { pagingData ->
      pagingData.map { it.toMediaItem().copy(mediaType = MOVIE_MEDIA_TYPE) }
    }

  override fun getPlayingNowMovies(region: String): Flow<PagingData<MediaItem>> =
    movieRemoteDataSource.getPlayingNowMovies(region).map { pagingData ->
      pagingData.map { it.toMediaItem().copy(mediaType = MOVIE_MEDIA_TYPE) }
    }

  override fun getMovieState(sessionId: String, movieId: Int): Flow<Outcome<MediaState>> =
    movieRemoteDataSource.getMovieState(sessionId, movieId)
      .toOutcome { it.toMediaState() }

  override fun getMovieRecommendation(movieId: Int): Flow<PagingData<MediaItem>> =
    movieRemoteDataSource.getMovieRecommendation(movieId).map { pagingData ->
      pagingData.map { it.toMediaItem() }
    }

  override fun postMovieRate(
    sessionId: String,
    rating: Float,
    movieId: Int,
  ): Flow<Outcome<PostResult>> =
    movieRemoteDataSource.postMovieRate(sessionId, rating, movieId)
      .toOutcome { it.toPostResult() }
}
