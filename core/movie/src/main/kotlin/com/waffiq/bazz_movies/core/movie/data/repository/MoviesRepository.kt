package com.waffiq.bazz_movies.core.movie.data.repository

import com.waffiq.bazz_movies.core.domain.FavoriteModel
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.Post
import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.core.domain.WatchlistModel
import com.waffiq.bazz_movies.core.mappers.PostMapper.toPost
import com.waffiq.bazz_movies.core.mappers.MediaStateMapper.toMediaState
import com.waffiq.bazz_movies.core.movie.domain.model.post.PostFavoriteWatchlist
import com.waffiq.bazz_movies.core.movie.domain.repository.IMoviesRepository
import com.waffiq.bazz_movies.core.movie.utils.mappers.Mapper.toPostFavoriteWatchlist
import com.waffiq.bazz_movies.core.network.data.remote.datasource.MovieDataSource
import com.waffiq.bazz_movies.core.network.utils.mappers.NetworkMapper.toFavoritePostModel
import com.waffiq.bazz_movies.core.network.utils.mappers.NetworkMapper.toWatchlistPostModel
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRepository @Inject constructor(
  private val movieDataSource: MovieDataSource
) : IMoviesRepository {

  override suspend fun getStatedMovie(
    sessionId: String,
    movieId: Int
  ): Flow<Outcome<MediaState>> =
    movieDataSource.getMovieState(sessionId, movieId).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> Outcome.Success(networkResult.data.toMediaState())
        is NetworkResult.Error -> Outcome.Error(networkResult.message)
        is NetworkResult.Loading -> Outcome.Loading
      }
    }

  override suspend fun getStatedTv(sessionId: String, tvId: Int): Flow<Outcome<MediaState>> =
    movieDataSource.getTvState(sessionId, tvId).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> Outcome.Success(networkResult.data.toMediaState())
        is NetworkResult.Error -> Outcome.Error(networkResult.message)
        is NetworkResult.Loading -> Outcome.Loading
      }
    }

  // region POST FAVORITE AND WATCHLIST
  override suspend fun postFavorite(
    sessionId: String,
    fav: FavoriteModel,
    userId: Int
  ): Flow<Outcome<PostFavoriteWatchlist>> =
    movieDataSource.postFavorite(sessionId, fav.toFavoritePostModel(), userId)
      .map { networkResult ->
        when (networkResult) {
          is NetworkResult.Success -> Outcome.Success(networkResult.data.toPostFavoriteWatchlist())
          is NetworkResult.Error -> Outcome.Error(networkResult.message)
          is NetworkResult.Loading -> Outcome.Loading
        }
      }

  override suspend fun postWatchlist(
    sessionId: String,
    wtc: WatchlistModel,
    userId: Int
  ): Flow<Outcome<PostFavoriteWatchlist>> =
    movieDataSource.postWatchlist(sessionId, wtc.toWatchlistPostModel(), userId)
      .map { networkResult ->
        when (networkResult) {
          is NetworkResult.Success -> Outcome.Success(networkResult.data.toPostFavoriteWatchlist())
          is NetworkResult.Error -> Outcome.Error(networkResult.message)
          is NetworkResult.Loading -> Outcome.Loading
        }
      }

  override suspend fun postMovieRate(
    sessionId: String,
    rating: Float,
    movieId: Int
  ): Flow<Outcome<Post>> =
    movieDataSource.postMovieRate(sessionId, rating, movieId).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> Outcome.Success(networkResult.data.toPost())
        is NetworkResult.Error -> Outcome.Error(networkResult.message)
        is NetworkResult.Loading -> Outcome.Loading
      }
    }

  override suspend fun postTvRate(
    sessionId: String,
    rating: Float,
    tvId: Int
  ): Flow<Outcome<Post>> =
    movieDataSource.postTvRate(sessionId, rating, tvId).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> Outcome.Success(networkResult.data.toPost())
        is NetworkResult.Error -> Outcome.Error(networkResult.message)
        is NetworkResult.Loading -> Outcome.Loading
      }
    }
  // endregion POST FAVORITE AND WATCHLIST
}
