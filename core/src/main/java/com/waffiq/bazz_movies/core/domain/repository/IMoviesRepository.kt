package com.waffiq.bazz_movies.core.domain.repository

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.model.Favorite
import com.waffiq.bazz_movies.core.domain.model.ResultItem
import com.waffiq.bazz_movies.core.domain.model.Stated
import com.waffiq.bazz_movies.core.domain.model.post.Post
import com.waffiq.bazz_movies.core.domain.model.post.PostFavoriteWatchlist
import com.waffiq.bazz_movies.core.network.data.remote.post_body.FavoritePostModel
import com.waffiq.bazz_movies.core.network.data.remote.post_body.RatePostModel
import com.waffiq.bazz_movies.core.network.data.remote.post_body.WatchlistPostModel
import com.waffiq.bazz_movies.core.utils.result.DbResult
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import kotlinx.coroutines.flow.Flow

interface IMoviesRepository {

  // region PAGING FUNCTION
  fun getPagingFavoriteMovies(sessionId: String): Flow<PagingData<ResultItem>>

  fun getPagingFavoriteTv(sessionId: String): Flow<PagingData<ResultItem>>

  fun getPagingWatchlistMovies(sessionId: String): Flow<PagingData<ResultItem>>

  fun getPagingWatchlistTv(sessionId: String): Flow<PagingData<ResultItem>>
  // endregion PAGING FUNCTION

  suspend fun getStatedMovie(sessionId: String, movieId: Int): Flow<NetworkResult<Stated>>

  suspend fun getStatedTv(sessionId: String, tvId: Int): Flow<NetworkResult<Stated>>

  // region POST FAVORITE AND WATCHLIST
  suspend fun postFavorite(
    sessionId: String,
    fav: FavoritePostModel,
    userId: Int
  ): Flow<NetworkResult<PostFavoriteWatchlist>>

  suspend fun postWatchlist(
    sessionId: String,
    wtc: WatchlistPostModel,
    userId: Int
  ): Flow<NetworkResult<PostFavoriteWatchlist>>

  suspend fun postMovieRate(
    sessionId: String,
    data: RatePostModel,
    movieId: Int
  ): Flow<NetworkResult<Post>>

  suspend fun postTvRate(
    sessionId: String,
    data: RatePostModel,
    tvId: Int
  ): Flow<NetworkResult<Post>>
  // endregion POST FAVORITE AND WATCHLIST

  // region LOCAL DATABASE
  val favoriteMoviesFromDB: Flow<List<Favorite>>

  val watchlistMovieFromDB: Flow<List<Favorite>>

  val watchlistTvFromDB: Flow<List<Favorite>>

  val favoriteTvFromDB: Flow<List<Favorite>>

  suspend fun insertToDB(fav: Favorite): DbResult<Int>

  suspend fun deleteFromDB(fav: Favorite): DbResult<Int>

  suspend fun deleteAll(): DbResult<Int>

  suspend fun isFavoriteDB(id: Int, mediaType: String): DbResult<Boolean>

  suspend fun isWatchlistDB(id: Int, mediaType: String): DbResult<Boolean>

  suspend fun updateFavoriteItemDB(isDelete: Boolean, fav: Favorite): DbResult<Int>

  suspend fun updateWatchlistItemDB(isDelete: Boolean, fav: Favorite): DbResult<Int>
  // endregion LOCAL DATABASE
}
