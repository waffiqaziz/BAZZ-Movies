package com.waffiq.bazz_movies.data.repository

import androidx.paging.PagingData
import com.waffiq.bazz_movies.data.local.datasource.LocalDataSource
import com.waffiq.bazz_movies.data.remote.FavoritePostModel
import com.waffiq.bazz_movies.data.remote.RatePostModel
import com.waffiq.bazz_movies.data.remote.WatchlistPostModel
import com.waffiq.bazz_movies.data.remote.datasource.MovieDataSource
import com.waffiq.bazz_movies.data.remote.response.tmdb.ResultItem
import com.waffiq.bazz_movies.data.remote.response.tmdb.ResultsItemSearch
import com.waffiq.bazz_movies.domain.model.Favorite
import com.waffiq.bazz_movies.utils.DataMapper.mapDomainToEntityFavorite
import com.waffiq.bazz_movies.utils.DataMapper.mapEntitiesToDomainFavorite
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MoviesRepository(
  private val localDataSource: LocalDataSource,
  private val movieDataSource: MovieDataSource
) {
  // region PAGING FUNCTION
  fun getPagingTopRatedMovies(): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingTopRatedMovies()

  fun getPagingPopularMovies(): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingPopularMovies()

  fun getPagingFavoriteMovies(sessionId: String): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingFavoriteMovies(sessionId)

  fun getPagingFavoriteTv(sessionId: String): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingFavoriteTv(sessionId)

  fun getPagingWatchlistMovies(sessionId: String): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingWatchlistMovies(sessionId)

  fun getPagingWatchlistTv(sessionId: String): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingWatchlistTv(sessionId)

  fun getPagingPopularTv(): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingPopularTv()

  fun getPagingOnTv(): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingOnTv()

  fun getPagingAiringTodayTv(): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingAiringTodayTv()

  fun getPagingTrendingWeek(region: String): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingTrendingWeek(region)

  fun getPagingTrendingDay(region: String): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingTrendingDay(region)

  fun getPagingMovieRecommendation(movieId: Int): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingMovieRecommendation(movieId)

  fun getPagingTvRecommendation(tvId: Int): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingTvRecommendation(tvId)

  fun getPagingUpcomingMovies(region: String): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingUpcomingMovies(region)

  fun getPagingPlayingNowMovies(region: String): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingPlayingNowMovies(region)

  fun getPagingTopRatedTv(): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingTopRatedTv()

  fun getPagingSearch(query: String): Flow<PagingData<ResultsItemSearch>> =
    movieDataSource.getPagingSearch(query)
  // endregion PAGING FUNCTION

  // region DETAIL
  suspend fun getDetailOMDb(imdbId: String) = movieDataSource.getDetailOMDb(imdbId)

  suspend fun getDetailMovie(id: Int) = movieDataSource.getDetailMovie(id)

  suspend fun getDetailTv(tvId: Int) = movieDataSource.getDetailTv(tvId)

  suspend fun getExternalTvId(tvId: Int) = movieDataSource.getExternalTvId(tvId)

  suspend fun getVideoMovies(movieId: Int) = movieDataSource.getVideoMovies(movieId)

  suspend fun getVideoTv(tvId: Int) = movieDataSource.getVideoTv(tvId)

  suspend fun getCreditMovies(movieId: Int) = movieDataSource.getCreditMovies(movieId)

  suspend fun getCreditTv(tvId: Int) = movieDataSource.getCreditTv(tvId)

  suspend fun getStatedMovie(sessionId: String, id: Int) =
    movieDataSource.getStatedMovie(sessionId, id)

  suspend fun getStatedTv(sessionId: String, id: Int) = movieDataSource.getStatedTv(sessionId, id)
  // endregion DETAIL

  // region POST FAVORITE AND WATCHLIST
  suspend fun postFavorite(sessionId: String, fav: FavoritePostModel, userId: Int) =
    movieDataSource.postFavorite(sessionId, fav, userId)

  suspend fun postWatchlist(sessionId: String, wtc: WatchlistPostModel, userId: Int) =
    movieDataSource.postWatchlist(sessionId, wtc, userId)

  suspend fun postMovieRate(sessionId: String, data: RatePostModel, movieId: Int) =
    movieDataSource.postMovieRate(sessionId, data, movieId)

  suspend fun postTvRate(sessionId: String, data: RatePostModel, tvId: Int) =
    movieDataSource.postTvRate(sessionId, data, tvId)
  // endregion POST FAVORITE AND WATCHLIST

  // region PERSON
  suspend fun getDetailPerson(id: Int) = movieDataSource.getDetailPerson(id)

  suspend fun getKnownForPerson(id: Int) = movieDataSource.getKnownForPerson(id)

  suspend fun getImagePerson(id: Int) = movieDataSource.getImagePerson(id)

  suspend fun getExternalIDPerson(id: Int) = movieDataSource.getExternalIDPerson(id)
  // endregion PERSON

  // region DATABASE
  val favoriteMoviesFromDB: Flow<List<Favorite>> =
    localDataSource.getFavoriteMovies.map { list ->
      mapEntitiesToDomainFavorite(list)
    }

  val watchlistMovieFromDB: Flow<List<Favorite>> =
    localDataSource.getWatchlistMovies.map { list ->
      mapEntitiesToDomainFavorite(list)
    }

  val watchlistTvFromDB: Flow<List<Favorite>> =
    localDataSource.getWatchlistTv.map { list ->
      mapEntitiesToDomainFavorite(list)
    }

  val favoriteTvFromDB: Flow<List<Favorite>> =
    localDataSource.getFavoriteTv.map { list ->
      mapEntitiesToDomainFavorite(list)
    }

  suspend fun insertToDB(fav: Favorite, callback: (Int) -> Unit) {
    val resultCode = localDataSource.insert(mapDomainToEntityFavorite(fav))
    callback.invoke(resultCode)
  }

  suspend fun deleteFromDB(fav: Favorite) {
    localDataSource.deleteItemFromDB(fav.mediaId, fav.mediaType)
  }

  suspend fun deleteAll(callback: (Int) -> Unit) {
    val resultCode = localDataSource.deleteAll()
    callback.invoke(resultCode)
  }

  suspend fun isFavoriteDB(id: Int, mediaType: String) = localDataSource.isFavorite(id, mediaType)

  suspend fun isWatchlistDB(id: Int, mediaType: String) = localDataSource.isWatchlist(id, mediaType)

  suspend fun updateFavoriteItemDB(isDelete: Boolean, fav: Favorite) {
    if (isDelete) { // update set is_favorite = false (item on favorite to delete)
      localDataSource.update(
        isFavorite = false,
        isWatchlist = fav.isWatchlist,
        id = fav.mediaId,
        mediaType = fav.mediaType
      )
    } else {  // update set is_favorite = true (add favorite item already on watchlist)
      localDataSource.update(
        isFavorite = true,
        isWatchlist = fav.isWatchlist,
        id = fav.mediaId,
        mediaType = fav.mediaType
      )
    }
  }

  suspend fun updateWatchlistItemDB(isDelete: Boolean, fav: Favorite) {
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
  }
  // endregion DATABASE

  companion object {
    private const val TAG = "MoviesRepository "
  }
}