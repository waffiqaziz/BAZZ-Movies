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
import com.waffiq.bazz_movies.data.remote.response.tmdb.CastCombinedItem
import com.waffiq.bazz_movies.data.remote.response.tmdb.CombinedCreditResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.DetailPersonResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.ExternalIDPersonResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.ImagePersonResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.PostRateResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.PostResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.ProfilesItem
import com.waffiq.bazz_movies.data.remote.response.tmdb.ResultItem
import com.waffiq.bazz_movies.data.remote.response.tmdb.ResultsItemSearch
import com.waffiq.bazz_movies.data.remote.response.tmdb.StatedResponse
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

  // stated
  private val _stated = MutableLiveData<StatedResponse?>()
  val stated: LiveData<StatedResponse?> get() = _stated

  // for DB all
  private val _isFavorite = MutableLiveData<Boolean>()
  val isFavorite: LiveData<Boolean> = _isFavorite

  private val _isWatchlist = MutableLiveData<Boolean>()
  val isWatchlist: LiveData<Boolean> = _isWatchlist


  // person
  private val _detailPerson = MutableLiveData<DetailPersonResponse>()
  val detailPerson: LiveData<DetailPersonResponse> get() = _detailPerson

  private val _knownFor = MutableLiveData<List<CastCombinedItem>>()
  val knownFor: LiveData<List<CastCombinedItem>> get() = _knownFor

  private val _imagePerson = MutableLiveData<List<ProfilesItem>>()
  val imagePerson: LiveData<List<ProfilesItem>> get() = _imagePerson

  private val _externalIdPerson = MutableLiveData<ExternalIDPersonResponse>()
  val externalIdPerson: LiveData<ExternalIDPersonResponse> get() = _externalIdPerson


  // future
  private val _postResponse = MutableLiveData<String>()
  val postResponse: LiveData<String> get() = _postResponse

  private val _snackBarText = MutableLiveData<Event<String>>()
  val snackBarText: LiveData<Event<String>> get() = _snackBarText


  private val _isLoading = MutableLiveData<Boolean>()
  val isLoading: LiveData<Boolean> = _isLoading

  private val _undoDB = MutableLiveData<Event<FavoriteDB>>()
  val undoDB: LiveData<Event<FavoriteDB>> = _undoDB


  /**
   * PAGING FUNCTION
   */
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


  /**
   * DETAIL
   */
  // detail movie from OMDb API
  suspend fun getDetailOMDb(imdbId: String) = remoteDataSource.getDetailOMDb(imdbId)

  // detail data from TMDB API
  suspend fun getDetailMovie(id: Int) = remoteDataSource.getDetailMovie(id)

  suspend fun getDetailTv(tvId: Int) = remoteDataSource.getDetailTv(tvId)

  suspend fun getExternalTvId(tvId: Int) = remoteDataSource.getExternalTvId(tvId)

  suspend fun getVideoMovies(movieId: Int) = remoteDataSource.getVideoMovies(movieId)

  suspend fun getVideoTv(tvId: Int) = remoteDataSource.getVideoTv(tvId)

  suspend fun getCreditMovies(movieId: Int) = remoteDataSource.getCreditMovies(movieId)

  suspend fun getCreditTv(tvId: Int) = remoteDataSource.getCreditTv(tvId)

  fun getStatedMovie(sessionId: String, id: Int) {
    val client = TMDBApiConfig
      .getApiService()
      .getStatedMovie(id, sessionId)

    client.enqueue(object : Callback<StatedResponse> {
      override fun onResponse(
        call: Call<StatedResponse>,
        response: Response<StatedResponse>
      ) {
        val responseBody = response.body()
        if (response.isSuccessful) if (responseBody != null) _stated.value = response.body()
        else {
          Log.e(TAG, "onFailure: ${response.message()}")

          // get message error
          Log.e(TAG, "onFailure: ${response.message()}")
          val jsonObject = JSONTokener(response.errorBody()?.string()).nextValue() as JSONObject
          val message = jsonObject.getString("status_message")
          _snackBarText.value = Event(message)
        }
      }

      override fun onFailure(call: Call<StatedResponse>, t: Throwable) {
        Log.e(TAG, "onFailure: ${t.message}")
        _snackBarText.value = Event(t.message.toString())
      }
    })
  }

  fun getStatedTv(sessionId: String, id: Int) {
    val client = TMDBApiConfig
      .getApiService()
      .getStatedTv(id, sessionId)

    client.enqueue(object : Callback<StatedResponse> {
      override fun onResponse(
        call: Call<StatedResponse>,
        response: Response<StatedResponse>
      ) {
        if (response.isSuccessful) _stated.value = response.body()
        else {
          Log.e(TAG, "onFailure: ${response.message()}")

          // get message error
          Log.e(TAG, "onFailure: ${response.message()}")
          val jsonObject = JSONTokener(response.errorBody()?.string()).nextValue() as JSONObject
          val message = jsonObject.getString("status_message")
          _snackBarText.value = Event(message)
        }
      }

      override fun onFailure(call: Call<StatedResponse>, t: Throwable) {
        Log.e(TAG, "onFailure: ${t.message}")
        _snackBarText.value = Event(t.message.toString())
      }
    })
  }


  /**
   * post favorite and watchlist
   */
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


  /**
   * PERSON
   */
  fun getDetailPerson(id: Int) {
    val client = TMDBApiConfig
      .getApiService()
      .getDetailPerson(id)

    client.enqueue(object : Callback<DetailPersonResponse> {
      override fun onResponse(
        call: Call<DetailPersonResponse>,
        response: Response<DetailPersonResponse>
      ) {
        if (response.isSuccessful) _detailPerson.value = response.body()
        else {
          // get message error
          Log.e(TAG, "onFailure: ${response.message()}")
          val jsonObject = JSONTokener(response.errorBody()?.string()).nextValue() as JSONObject
          val message = jsonObject.getString("status_message")
          _snackBarText.value = Event(message)
        }
      }

      override fun onFailure(call: Call<DetailPersonResponse>, t: Throwable) {
        Log.e(TAG, "onFailure: ${t.message}")
        _snackBarText.value = Event(t.message.toString())
      }
    })
  }

  fun getKnownForPerson(id: Int) {
    _isLoading.value = true
    val client = TMDBApiConfig
      .getApiService()
      .getKnownForPersonCombinedMovieTv(id)

    client.enqueue(object : Callback<CombinedCreditResponse> {
      override fun onResponse(
        call: Call<CombinedCreditResponse>,
        response: Response<CombinedCreditResponse>
      ) {
        _isLoading.value = false
        if (response.isSuccessful) {
          val responseBody = response.body()
          if (responseBody != null)
            _knownFor.value = responseBody.cast ?: emptyList()
        } else {
          Log.e(TAG, "onFailure: ${response.message()}")

          // get message error
          val jsonObject = JSONTokener(response.errorBody()?.string()).nextValue() as JSONObject
          val message = jsonObject.getString("status_message")
          _snackBarText.value = Event(message)
        }
      }

      override fun onFailure(call: Call<CombinedCreditResponse>, t: Throwable) {
        _isLoading.value = false
        Log.e(TAG, "onFailure: ${t.message}")
        _snackBarText.value = Event(t.message.toString())
      }
    })
  }

  fun getImagePerson(id: Int) {
    val client = TMDBApiConfig
      .getApiService()
      .getImagePerson(id)

    client.enqueue(object : Callback<ImagePersonResponse> {
      override fun onResponse(
        call: Call<ImagePersonResponse>,
        response: Response<ImagePersonResponse>
      ) {
        if (response.isSuccessful) {
          val responseBody = response.body()
          if (responseBody != null) _imagePerson.value = responseBody.profiles ?: emptyList()
        } else {
          Log.e(TAG, "onFailure: ${response.message()}")

          // get message error
          val jsonObject = JSONTokener(response.errorBody()?.string()).nextValue() as JSONObject
          val message = jsonObject.getString("status_message")
          _snackBarText.value = Event(message)
        }
      }

      override fun onFailure(call: Call<ImagePersonResponse>, t: Throwable) {
        Log.e(TAG, "onFailure: ${t.message}")
        _snackBarText.value = Event(t.message.toString())
      }
    })
  }

  fun getExternalIDPerson(id: Int) {
    val client = TMDBApiConfig
      .getApiService()
      .getExternalIdPerson(id)

    client.enqueue(object : Callback<ExternalIDPersonResponse> {
      override fun onResponse(
        call: Call<ExternalIDPersonResponse>,
        response: Response<ExternalIDPersonResponse>
      ) {
        if (response.isSuccessful) {
          if (response.body() != null) _externalIdPerson.value = response.body()
        } else {
          Log.e(TAG, "onFailure: ${response.message()}")

          // get message error
          val jsonObject = JSONTokener(response.errorBody()?.string()).nextValue() as JSONObject
          val message = jsonObject.getString("status_message")
          _snackBarText.value = Event(message)
        }
      }

      override fun onFailure(call: Call<ExternalIDPersonResponse>, t: Throwable) {
        Log.e(TAG, "onFailure: ${t.message}")
        _snackBarText.value = Event(t.message.toString())
      }
    })
  }


  /**
   * DATABASE
   */
  val favoriteMoviesFromDB: Flow<List<FavoriteDB>> = localDataSource.getFavoriteMovies

  val watchlistMovieFromDB: Flow<List<FavoriteDB>> = localDataSource.getWatchlistMovies

  val watchlistTvFromDB: Flow<List<FavoriteDB>> = localDataSource.getWatchlistTv

  val favoriteTvFromDB: Flow<List<FavoriteDB>> = localDataSource.getFavoriteTv

  fun getSpecificFavorite(name: String): Flow<List<FavoriteDB>> =
    localDataSource.getSpecificFavorite(name)

  suspend fun insertToDB(fav: FavoriteDB, callback: (Int) -> Unit) {
    val resultCode = localDataSource.insert(fav)
    callback.invoke(resultCode)
  }

  suspend fun deleteFromDB(fav: FavoriteDB) {
    if (fav.mediaType != null)
      localDataSource.deleteItemFromDB(fav.mediaId, fav.mediaType)
    _undoDB.value = Event(fav)
  }

  suspend fun deleteAll(callback: (Int) -> Unit) {
    val resultCode = localDataSource.deleteAll()
    callback.invoke(resultCode)
  }

  suspend fun isFavoriteDB(id: Int, mediaType: String) {
    _isFavorite.postValue(localDataSource.isFavorite(id, mediaType))
  }

  suspend fun isWatchlistDB(id: Int, mediaType: String) {
    _isWatchlist.postValue(localDataSource.isWatchlist(id, mediaType))
  }

  suspend fun updateFavoriteDB(isDelete: Boolean, fav: FavoriteDB) {
    // update set is_favorite = false, (for movie that want to delete, but already on watchlist)
    if (isDelete) {
      _undoDB.value = Event(fav)

      if (fav.isWatchlist != null && fav.mediaType != null) {
        localDataSource.update(
          isFavorite = false,
          isWatchlist = fav.isWatchlist,
          id = fav.mediaId,
          mediaType = fav.mediaType
        )
      } else Log.e(TAG, "favDB: $fav")
    } else {  // update set is_favorite = true, (for movie that already on watchlist)
      _undoDB.value = Event(fav)

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

  suspend fun updateWatchlistDB(isDelete: Boolean, fav: FavoriteDB) {
    if (isDelete) { // update set is_watchlist = false
      _undoDB.value = Event(fav)

      if (fav.isFavorite != null && fav.mediaType != null) {
        localDataSource.update(
          isFavorite = fav.isFavorite,
          isWatchlist = false,
          id = fav.mediaId,
          mediaType = fav.mediaType
        )
      } else Log.e(TAG, "favDB: $fav")
    } else { // update set is_watchlist = true
      _undoDB.value = Event(fav)

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