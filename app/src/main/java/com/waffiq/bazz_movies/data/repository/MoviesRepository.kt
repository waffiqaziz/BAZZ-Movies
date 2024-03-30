package com.waffiq.bazz_movies.data.repository

import android.util.Log
import androidx.paging.PagingData
import com.waffiq.bazz_movies.data.local.datasource.LocalDataSource
import com.waffiq.bazz_movies.data.local.model.Favorite
import com.waffiq.bazz_movies.data.local.model.FavoriteDB
import com.waffiq.bazz_movies.data.local.model.Rate
import com.waffiq.bazz_movies.data.local.model.Watchlist
import com.waffiq.bazz_movies.data.remote.datasource.RemoteDataSource
import com.waffiq.bazz_movies.data.remote.response.tmdb.ResultItem
import com.waffiq.bazz_movies.data.remote.response.tmdb.ResultsItemSearch
import kotlinx.coroutines.flow.Flow

class MoviesRepository(
  private val localDataSource: LocalDataSource,
  private val remoteDataSource: RemoteDataSource
) {
  // region PAGING FUNCTION
  fun getPagingTopRatedMovies(): Flow<PagingData<ResultItem>> =
    remoteDataSource.getPagingTopRatedMovies()

  fun getPagingPopularMovies(): Flow<PagingData<ResultItem>> =
    remoteDataSource.getPagingPopularMovies()

  fun getPagingFavoriteMovies(sessionId: String): Flow<PagingData<ResultItem>> =
    remoteDataSource.getPagingFavoriteMovies(sessionId)

  fun getPagingFavoriteTv(sessionId: String): Flow<PagingData<ResultItem>> =
    remoteDataSource.getPagingFavoriteTv(sessionId)

  fun getPagingWatchlistMovies(sessionId: String): Flow<PagingData<ResultItem>> =
    remoteDataSource.getPagingWatchlistMovies(sessionId)

  fun getPagingWatchlistTv(sessionId: String): Flow<PagingData<ResultItem>> =
    remoteDataSource.getPagingWatchlistTv(sessionId)

  fun getPagingPopularTv(): Flow<PagingData<ResultItem>> =
    remoteDataSource.getPagingPopularTv()

  fun getPagingOnTv(): Flow<PagingData<ResultItem>> =
    remoteDataSource.getPagingOnTv()

  fun getPagingAiringTodayTv(): Flow<PagingData<ResultItem>> =
    remoteDataSource.getPagingAiringTodayTv()

  fun getPagingTrendingWeek(region: String): Flow<PagingData<ResultItem>> =
    remoteDataSource.getPagingTrendingWeek(region)

  fun getPagingTrendingDay(region: String): Flow<PagingData<ResultItem>> =
    remoteDataSource.getPagingTrendingDay(region)

  fun getPagingMovieRecommendation(movieId: Int): Flow<PagingData<ResultItem>> =
    remoteDataSource.getPagingMovieRecommendation(movieId)

  fun getPagingTvRecommendation(tvId: Int): Flow<PagingData<ResultItem>> =
    remoteDataSource.getPagingTvRecommendation(tvId)

  fun getPagingUpcomingMovies(region: String): Flow<PagingData<ResultItem>> =
    remoteDataSource.getPagingUpcomingMovies(region)

  fun getPagingPlayingNowMovies(region: String): Flow<PagingData<ResultItem>> =
    remoteDataSource.getPagingPlayingNowMovies(region)

  fun getPagingTopRatedTv(): Flow<PagingData<ResultItem>> =
    remoteDataSource.getPagingTopRatedTv()

  fun getPagingSearch(query: String): Flow<PagingData<ResultsItemSearch>> =
    remoteDataSource.getPagingSearch(query)
  // endregion PAGING FUNCTION

  // region DETAIL
  suspend fun getDetailOMDb(imdbId: String) = remoteDataSource.getDetailOMDb(imdbId)

  suspend fun getDetailMovie(id: Int) = remoteDataSource.getDetailMovie(id)

  suspend fun getDetailTv(tvId: Int) = remoteDataSource.getDetailTv(tvId)

  suspend fun getExternalTvId(tvId: Int) = remoteDataSource.getExternalTvId(tvId)

  suspend fun getVideoMovies(movieId: Int) = remoteDataSource.getVideoMovies(movieId)

  suspend fun getVideoTv(tvId: Int) = remoteDataSource.getVideoTv(tvId)

  suspend fun getCreditMovies(movieId: Int) = remoteDataSource.getCreditMovies(movieId)

  suspend fun getCreditTv(tvId: Int) = remoteDataSource.getCreditTv(tvId)

  suspend fun getStatedMovie(sessionId: String, id: Int) =
    remoteDataSource.getStatedMovie(sessionId, id)

  suspend fun getStatedTv(sessionId: String, id: Int) = remoteDataSource.getStatedTv(sessionId, id)
  // endregion DETAIL

  // region POST FAVORITE AND WATCHLIST
  suspend fun postFavorite(sessionId: String, fav: Favorite, userId: Int) =
    remoteDataSource.postFavorite(sessionId, fav, userId)

  suspend fun postWatchlist(sessionId: String, wtc: Watchlist, userId: Int) =
    remoteDataSource.postWatchlist(sessionId, wtc, userId)

  suspend fun postMovieRate(sessionId: String, data: Rate, movieId: Int) =
    remoteDataSource.postMovieRate(sessionId, data, movieId)

  suspend fun postTvRate(sessionId: String, data: Rate, tvId: Int) =
    remoteDataSource.postTvRate(sessionId, data, tvId)
  // endregion POST FAVORITE AND WATCHLIST

  // region PERSON
  suspend fun getDetailPerson(id: Int) = remoteDataSource.getDetailPerson(id)

  suspend fun getKnownForPerson(id: Int) = remoteDataSource.getKnownForPerson(id)

  suspend fun getImagePerson(id: Int) = remoteDataSource.getImagePerson(id)

  suspend fun getExternalIDPerson(id: Int) = remoteDataSource.getExternalIDPerson(id)
  // endregion PERSON

  // region DATABASE
  val favoriteMoviesFromDB: Flow<List<FavoriteDB>> = localDataSource.getFavoriteMovies

  val watchlistMovieFromDB: Flow<List<FavoriteDB>> = localDataSource.getWatchlistMovies

  val watchlistTvFromDB: Flow<List<FavoriteDB>> = localDataSource.getWatchlistTv

  val favoriteTvFromDB: Flow<List<FavoriteDB>> = localDataSource.getFavoriteTv

  suspend fun insertToDB(fav: FavoriteDB, callback: (Int) -> Unit) {
    val resultCode = localDataSource.insert(fav)
    callback.invoke(resultCode)
  }

  suspend fun deleteFromDB(fav: FavoriteDB) {
    if (fav.mediaType != null)
      localDataSource.deleteItemFromDB(fav.mediaId, fav.mediaType)
  }

  suspend fun deleteAll(callback: (Int) -> Unit) {
    val resultCode = localDataSource.deleteAll()
    callback.invoke(resultCode)
  }

  suspend fun isFavoriteDB(id: Int, mediaType: String) = localDataSource.isFavorite(id, mediaType)

  suspend fun isWatchlistDB(id: Int, mediaType: String) = localDataSource.isWatchlist(id, mediaType)

  suspend fun updateFavoriteItemDB(isDelete: Boolean, fav: FavoriteDB) {
    if (isDelete) { // update set is_favorite = false (item on favorite to delete)
      if (fav.isWatchlist != null && fav.mediaType != null) {
        localDataSource.update(
          isFavorite = false,
          isWatchlist = fav.isWatchlist,
          id = fav.mediaId,
          mediaType = fav.mediaType
        )
      } else Log.e(TAG, "favDB: $fav")
    } else {  // update set is_favorite = true (add favorite item already on watchlist)
      if (fav.isWatchlist != null && fav.mediaType != null) {
        localDataSource.update(
          isFavorite = true,
          isWatchlist = fav.isWatchlist,
          id = fav.mediaId,
          mediaType = fav.mediaType
        )
      } else Log.e(TAG, "favDB: $fav")
    }
  }

  suspend fun updateWatchlistItemDB(isDelete: Boolean, fav: FavoriteDB) {
    if (isDelete) { // update set is_watchlist = false (item on watchlist to delete)
      if (fav.isFavorite != null && fav.mediaType != null) {
        localDataSource.update(
          isFavorite = fav.isFavorite,
          isWatchlist = false,
          id = fav.mediaId,
          mediaType = fav.mediaType
        )
      } else Log.e(TAG, "favDB: $fav")
    } else { // update set is_watchlist = true (add watchlist item already on favorite)
      if (fav.isFavorite != null && fav.mediaType != null) {
        localDataSource.update(
          isFavorite = fav.isFavorite,
          isWatchlist = true,
          id = fav.mediaId,
          mediaType = fav.mediaType
        )
      } else Log.e(TAG, "favDB: $fav")
    }
  }
  // endregion DATABASE

  companion object {
    private const val TAG = "MoviesRepository "

    @Volatile
    private var instance: MoviesRepository? = null

    fun getInstance(
      localData: LocalDataSource,
      remoteDataSource: RemoteDataSource,
    ): MoviesRepository =
      instance ?: synchronized(this) {
        instance ?: MoviesRepository(localData, remoteDataSource)
      }
  }
}