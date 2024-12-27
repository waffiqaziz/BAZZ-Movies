package com.waffiq.bazz_movies.core.movie.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.waffiq.bazz_movies.core.domain.FavoriteModel
import com.waffiq.bazz_movies.core.domain.Post
import com.waffiq.bazz_movies.core.domain.ResultItem
import com.waffiq.bazz_movies.core.domain.Stated
import com.waffiq.bazz_movies.core.domain.WatchlistModel
import com.waffiq.bazz_movies.core.mappers.PostMapper.toPost
import com.waffiq.bazz_movies.core.mappers.StateMapper.toStated
import com.waffiq.bazz_movies.core.movie.domain.model.post.PostFavoriteWatchlist
import com.waffiq.bazz_movies.core.movie.domain.repository.IMoviesRepository
import com.waffiq.bazz_movies.core.movie.utils.mappers.Mapper.toPostFavoriteWatchlist
import com.waffiq.bazz_movies.core.movie.utils.mappers.Mapper.toResultItem
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

  // region PAGING FUNCTION
  override fun getPagingFavoriteMovies(sessionId: String): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingFavoriteMovies(sessionId).map { pagingData ->
      pagingData.map { it.toResultItem() }
    }

  override fun getPagingFavoriteTv(sessionId: String): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingFavoriteTv(sessionId).map { pagingData ->
      pagingData.map { it.toResultItem() }
    }

  override fun getPagingWatchlistMovies(sessionId: String): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingWatchlistMovies(sessionId).map { pagingData ->
      pagingData.map { it.toResultItem() }
    }

  override fun getPagingWatchlistTv(sessionId: String): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingWatchlistTv(sessionId).map { pagingData ->
      pagingData.map { it.toResultItem() }
    }
  // endregion PAGING FUNCTION

  override suspend fun getStatedMovie(
    sessionId: String,
    movieId: Int
  ): Flow<NetworkResult<Stated>> =
    movieDataSource.getStatedMovie(sessionId, movieId).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> NetworkResult.Success(networkResult.data.toStated())
        is NetworkResult.Error -> NetworkResult.Error(networkResult.message)
        is NetworkResult.Loading -> NetworkResult.Loading
      }
    }

  override suspend fun getStatedTv(sessionId: String, tvId: Int): Flow<NetworkResult<Stated>> =
    movieDataSource.getStatedTv(sessionId, tvId).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> NetworkResult.Success(networkResult.data.toStated())
        is NetworkResult.Error -> NetworkResult.Error(networkResult.message)
        is NetworkResult.Loading -> NetworkResult.Loading
      }
    }
  // endregion DETAIL

  // region POST FAVORITE AND WATCHLIST
  override suspend fun postFavorite(
    sessionId: String,
    fav: FavoriteModel,
    userId: Int
  ): Flow<NetworkResult<PostFavoriteWatchlist>> =
    movieDataSource.postFavorite(sessionId, fav.toFavoritePostModel(), userId)
      .map { networkResult ->
        when (networkResult) {
          is NetworkResult.Success -> NetworkResult.Success(networkResult.data.toPostFavoriteWatchlist())
          is NetworkResult.Error -> NetworkResult.Error(networkResult.message)
          is NetworkResult.Loading -> NetworkResult.Loading
        }
      }

  override suspend fun postWatchlist(
    sessionId: String,
    wtc: WatchlistModel,
    userId: Int
  ): Flow<NetworkResult<PostFavoriteWatchlist>> =
    movieDataSource.postWatchlist(sessionId, wtc.toWatchlistPostModel(), userId)
      .map { networkResult ->
        when (networkResult) {
          is NetworkResult.Success -> NetworkResult.Success(networkResult.data.toPostFavoriteWatchlist())
          is NetworkResult.Error -> NetworkResult.Error(networkResult.message)
          is NetworkResult.Loading -> NetworkResult.Loading
        }
      }

  override suspend fun postMovieRate(
    sessionId: String,
    rating: Float,
    movieId: Int
  ): Flow<NetworkResult<Post>> =
    movieDataSource.postMovieRate(sessionId, rating, movieId).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> NetworkResult.Success(networkResult.data.toPost())
        is NetworkResult.Error -> NetworkResult.Error(networkResult.message)
        is NetworkResult.Loading -> NetworkResult.Loading
      }
    }

  override suspend fun postTvRate(
    sessionId: String,
    rating: Float,
    tvId: Int
  ): Flow<NetworkResult<Post>> =
    movieDataSource.postTvRate(sessionId, rating, tvId).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> NetworkResult.Success(networkResult.data.toPost())
        is NetworkResult.Error -> NetworkResult.Error(networkResult.message)
        is NetworkResult.Loading -> NetworkResult.Loading
      }
    }
  // endregion POST FAVORITE AND WATCHLIST
}
