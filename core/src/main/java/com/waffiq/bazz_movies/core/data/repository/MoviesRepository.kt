package com.waffiq.bazz_movies.core.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.waffiq.bazz_movies.core.data.local.datasource.LocalDataSource
import com.waffiq.bazz_movies.core.network.data.remote.datasource.MovieDataSource
import com.waffiq.bazz_movies.core.network.data.remote.post_body.FavoritePostModel
import com.waffiq.bazz_movies.core.network.data.remote.post_body.RatePostModel
import com.waffiq.bazz_movies.core.network.data.remote.post_body.WatchlistPostModel
import com.waffiq.bazz_movies.core.domain.model.Favorite
import com.waffiq.bazz_movies.core.domain.model.ResultItem
import com.waffiq.bazz_movies.core.domain.model.Stated
import com.waffiq.bazz_movies.core.domain.model.post.Post
import com.waffiq.bazz_movies.core.domain.model.post.PostFavoriteWatchlist
import com.waffiq.bazz_movies.core.domain.repository.IMoviesRepository
import com.waffiq.bazz_movies.core.utils.mappers.DatabaseMapper.toFavorite
import com.waffiq.bazz_movies.core.utils.mappers.DatabaseMapper.toFavoriteEntity
import com.waffiq.bazz_movies.core.utils.mappers.PostMapper.toPost
import com.waffiq.bazz_movies.core.utils.mappers.PostMapper.toPostFavoriteWatchlist
import com.waffiq.bazz_movies.core.utils.mappers.StateMapper.toStated
import com.waffiq.bazz_movies.core.utils.mappers.UniversalMapper.toResultItem
import com.waffiq.bazz_movies.core.utils.result.DbResult
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRepository @Inject constructor(
  private val localDataSource: LocalDataSource,
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
    fav: FavoritePostModel,
    userId: Int
  ): Flow<NetworkResult<PostFavoriteWatchlist>> =
    movieDataSource.postFavorite(sessionId, fav, userId).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> NetworkResult.Success(networkResult.data.toPostFavoriteWatchlist())
        is NetworkResult.Error -> NetworkResult.Error(networkResult.message)
        is NetworkResult.Loading -> NetworkResult.Loading
      }
    }

  override suspend fun postWatchlist(
    sessionId: String,
    wtc: WatchlistPostModel,
    userId: Int
  ): Flow<NetworkResult<PostFavoriteWatchlist>> =
    movieDataSource.postWatchlist(sessionId, wtc, userId).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> NetworkResult.Success(networkResult.data.toPostFavoriteWatchlist())
        is NetworkResult.Error -> NetworkResult.Error(networkResult.message)
        is NetworkResult.Loading -> NetworkResult.Loading
      }
    }

  override suspend fun postMovieRate(
    sessionId: String,
    data: RatePostModel,
    movieId: Int
  ): Flow<NetworkResult<Post>> =
    movieDataSource.postMovieRate(sessionId, data, movieId).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> NetworkResult.Success(networkResult.data.toPost())
        is NetworkResult.Error -> NetworkResult.Error(networkResult.message)
        is NetworkResult.Loading -> NetworkResult.Loading
      }
    }

  override suspend fun postTvRate(
    sessionId: String,
    data: RatePostModel,
    tvId: Int
  ): Flow<NetworkResult<Post>> =
    movieDataSource.postTvRate(sessionId, data, tvId).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> NetworkResult.Success(networkResult.data.toPost())
        is NetworkResult.Error -> NetworkResult.Error(networkResult.message)
        is NetworkResult.Loading -> NetworkResult.Loading
      }
    }
  // endregion POST FAVORITE AND WATCHLIST

  // region DATABASE
  override val favoriteMoviesFromDB: Flow<List<Favorite>> =
    localDataSource.getFavoriteMovies.map { list ->
      list.map { it.toFavorite() }
    }

  override val watchlistMovieFromDB: Flow<List<Favorite>> =
    localDataSource.getWatchlistMovies.map { list ->
      list.map { it.toFavorite() }
    }

  override val watchlistTvFromDB: Flow<List<Favorite>> =
    localDataSource.getWatchlistTv.map { list ->
      list.map { it.toFavorite() }
    }

  override val favoriteTvFromDB: Flow<List<Favorite>> =
    localDataSource.getFavoriteTv.map { list ->
      list.map { it.toFavorite() }
    }

  override suspend fun insertToDB(fav: Favorite): DbResult<Int> =
    localDataSource.insert(fav.toFavoriteEntity())

  override suspend fun deleteFromDB(fav: Favorite): DbResult<Int> =
    localDataSource.deleteItemFromDB(fav.mediaId, fav.mediaType)

  override suspend fun deleteAll(): DbResult<Int> =
    localDataSource.deleteAll()

  override suspend fun isFavoriteDB(id: Int, mediaType: String): DbResult<Boolean> =
    localDataSource.isFavorite(id, mediaType)

  override suspend fun isWatchlistDB(id: Int, mediaType: String): DbResult<Boolean> =
    localDataSource.isWatchlist(id, mediaType)

  override suspend fun updateFavoriteItemDB(isDelete: Boolean, fav: Favorite): DbResult<Int> =
    if (isDelete) {
      // update set is_favorite = false (item on favorite to delete)
      localDataSource.update(
        isFavorite = false,
        isWatchlist = fav.isWatchlist,
        id = fav.mediaId,
        mediaType = fav.mediaType
      )
    } else {
      // update set is_favorite = true (add favorite item already on watchlist)
      localDataSource.update(
        isFavorite = true,
        isWatchlist = fav.isWatchlist,
        id = fav.mediaId,
        mediaType = fav.mediaType
      )
    }

  override suspend fun updateWatchlistItemDB(isDelete: Boolean, fav: Favorite): DbResult<Int> =
    if (isDelete) { // update set is_watchlist = false (item on watchlist to delete)
      localDataSource.update(
        isFavorite = fav.isFavorite,
        isWatchlist = false,
        id = fav.mediaId,
        mediaType = fav.mediaType
      )
    } else { // update set is_watchlist = true (add watchlist item already on favorite)
      localDataSource.update(
        isFavorite = fav.isFavorite,
        isWatchlist = true,
        id = fav.mediaId,
        mediaType = fav.mediaType
      )
    }
// endregion DATABASE
}
