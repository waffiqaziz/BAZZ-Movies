package com.waffiq.bazz_movies.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.waffiq.bazz_movies.data.local.datasource.LocalDataSource
import com.waffiq.bazz_movies.data.local.model.Favorite
import com.waffiq.bazz_movies.data.local.model.FavoriteDB
import com.waffiq.bazz_movies.data.local.model.Rate
import com.waffiq.bazz_movies.data.local.model.Watchlist
import com.waffiq.bazz_movies.data.remote.datasource.RemoteDataSource
import com.waffiq.bazz_movies.data.remote.response.tmdb.PostRateResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.PostResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.ResultItem
import com.waffiq.bazz_movies.data.remote.response.tmdb.ResultsItemSearch
import com.waffiq.bazz_movies.data.remote.retrofit.TMDBApiConfig
import com.waffiq.bazz_movies.utils.Event
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesRepository(
  private val localDataSource: LocalDataSource,
  private val remoteDataSource: RemoteDataSource
) {

  // future
  private val _postResponse = MutableLiveData<String>()
  val postResponse: LiveData<String> get() = _postResponse

  private val _snackBarText = MutableLiveData<Event<String>>()
  val snackBarText: LiveData<Event<String>> get() = _snackBarText

//  private val _undoDB = MutableLiveData<Event<FavoriteDB>>()
//  val undoDB: LiveData<Event<FavoriteDB>> = _undoDB


  // region PAGING FUNCTION
  fun getPagingTopRatedMovies(): Flow<PagingData<ResultItem>> =
    remoteDataSource.getPagingTopRatedMovies()

  fun getPagingPopularMovies(): Flow<PagingData<ResultItem>> =
    remoteDataSource.getPagingPopularMovies()

  fun getPagingFavoriteMovies(sessionId: String): Flow<PagingData<ResultItem>> =
    remoteDataSource.getPagingFavoriteMovies(sessionId)

  fun getPagingFavoriteTv(sessionId: String): Flow<PagingData<ResultItem>> =
    remoteDataSource.getPagingFavoriteTv(sessionId)

  fun getPagingWatchlistTv(sessionId: String): Flow<PagingData<ResultItem>> =
    remoteDataSource.getPagingWatchlistTv(sessionId)

  fun getPagingWatchlistMovies(sessionId: String): Flow<PagingData<ResultItem>> =
    remoteDataSource.getPagingWatchlistMovies(sessionId)

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

  suspend fun getStatedTv(sessionId: String, id: Int) =
    remoteDataSource.getStatedTv(sessionId, id)
  // endregion DETAIL

  // region POST FAVORITE AND WATCHLIST
  fun postFavorite(sessionId: String, data: Favorite, userId: Int) {
    val client = TMDBApiConfig
      .getApiService()
      .postFavoriteTMDB(userId, sessionId, data)

    client.enqueue(object : Callback<PostResponse> {
      override fun onResponse(
        call: Call<PostResponse>,
        response: Response<PostResponse>
      ) {
        if (response.isSuccessful) {
          val responseBody = response.body()
          if (responseBody != null) {
            _postResponse.value = responseBody.statusMessage ?: "No Response"
          }
        } else {
          Log.e(TAG, "onFailure: ${response.message()}")

          // get message error
          Log.e(TAG, "onFailure: ${response.message()}")
          val jsonObject = JSONTokener(response.errorBody()?.string()).nextValue() as JSONObject
          val message = jsonObject.getString("status_message")
          _snackBarText.value = Event(message)
        }
      }

      override fun onFailure(call: Call<PostResponse>, t: Throwable) {
        Log.e(TAG, "onFailure: ${t.message}")
        _snackBarText.value = Event(t.message.toString())
      }
    })
  }

  fun postWatchlist(sessionId: String, data: Watchlist, userId: Int) {
    val client = TMDBApiConfig
      .getApiService()
      .postWatchlistTMDB(userId, sessionId, data)

    client.enqueue(object : Callback<PostResponse> {
      override fun onResponse(
        call: Call<PostResponse>,
        response: Response<PostResponse>
      ) {
        if (response.isSuccessful) {
          val responseBody = response.body()
          if (responseBody != null) _postResponse.value =
            responseBody.statusMessage ?: "No Response"
        } else {
          Log.e(TAG, "onFailure: ${response.message()}")

          // get message error
          Log.e(TAG, "onFailure: ${response.message()}")
          val jsonObject = JSONTokener(response.errorBody()?.string()).nextValue() as JSONObject
          val message = jsonObject.getString("status_message")
          _snackBarText.value = Event(message)
        }
      }

      override fun onFailure(call: Call<PostResponse>, t: Throwable) {
        Log.e(TAG, "onFailure: ${t.message}")
        _snackBarText.value = Event(t.message.toString())
      }
    })
  }

  fun postMovieRate(sessionId: String, data: Rate, movieId: Int) {
    val client = TMDBApiConfig
      .getApiService()
      .postMovieRate(movieId, sessionId, data)

    client.enqueue(object : Callback<PostRateResponse> {
      override fun onResponse(
        call: Call<PostRateResponse>,
        response: Response<PostRateResponse>
      ) {
        if (response.isSuccessful) {
          val responseBody = response.body()
          if (responseBody != null) _postResponse.value =
            responseBody.statusMessage ?: "No Response"
        } else {
          Log.e(TAG, "onFailure: ${response.message()}")

          // get message error
          Log.e(TAG, "onFailure: ${response.message()}")
          val jsonObject = JSONTokener(response.errorBody()?.string()).nextValue() as JSONObject
          val message = jsonObject.getString("status_message")
          _snackBarText.value = Event(message)
        }
      }

      override fun onFailure(call: Call<PostRateResponse>, t: Throwable) {
        Log.e(TAG, "onFailure: ${t.message}")
        _snackBarText.value = Event(t.message.toString())
      }
    })
  }

  fun postTvRate(sessionId: String, data: Rate, tvId: Int) {
    val client = TMDBApiConfig
      .getApiService()
      .postTvRate(tvId, sessionId, data)

    client.enqueue(object : Callback<PostRateResponse> {
      override fun onResponse(
        call: Call<PostRateResponse>,
        response: Response<PostRateResponse>
      ) {
        if (response.isSuccessful) {
          val responseBody = response.body()
          if (responseBody != null) _postResponse.value =
            responseBody.statusMessage ?: "No Response"
        } else {
          Log.e(TAG, "onFailure: ${response.message()}")

          // get message error
          Log.e(TAG, "onFailure: ${response.message()}")
          val jsonObject = JSONTokener(response.errorBody()?.string()).nextValue() as JSONObject
          val message = jsonObject.getString("status_message")
          _snackBarText.value = Event(message)
        }
      }

      override fun onFailure(call: Call<PostRateResponse>, t: Throwable) {
        Log.e(TAG, "onFailure: ${t.message}")
        _snackBarText.value = Event(t.message.toString())
      }
    })
  }
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
    // update set is_favorite = false, (for movie that want to delete, but already on watchlist)
    if (isDelete) {
      if (fav.isWatchlist != null && fav.mediaType != null) {
        localDataSource.update(
          isFavorite = false,
          isWatchlist = fav.isWatchlist,
          id = fav.mediaId,
          mediaType = fav.mediaType
        )
      } else Log.e(TAG, "favDB: $fav")
    } else {  // update set is_favorite = true, (for movie that already on watchlist)
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
    if (isDelete) { // update set is_watchlist = false
      if (fav.isFavorite != null && fav.mediaType != null) {
        localDataSource.update(
          isFavorite = fav.isFavorite,
          isWatchlist = false,
          id = fav.mediaId,
          mediaType = fav.mediaType
        )
      } else Log.e(TAG, "favDB: $fav")
    } else { // update set is_watchlist = true
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