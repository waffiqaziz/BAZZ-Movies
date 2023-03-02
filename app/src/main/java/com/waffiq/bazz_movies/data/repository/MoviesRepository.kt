package com.waffiq.bazz_movies.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.waffiq.bazz_movies.data.local.LocalDataSource
import com.waffiq.bazz_movies.data.local.model.Favorite
import com.waffiq.bazz_movies.data.local.model.FavoriteDB
import com.waffiq.bazz_movies.data.local.model.Watchlist
import com.waffiq.bazz_movies.data.paging.*
import com.waffiq.bazz_movies.data.remote.response.*
import com.waffiq.bazz_movies.data.remote.retrofit.IMDBApiLibConfig
import com.waffiq.bazz_movies.data.remote.retrofit.IMDBApiLibService
import com.waffiq.bazz_movies.data.remote.retrofit.TMDBApiConfig
import com.waffiq.bazz_movies.data.remote.retrofit.TMDBApiService
import com.waffiq.bazz_movies.utils.AppExecutors
import com.waffiq.bazz_movies.utils.Event
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesRepository(
  private val tmdbApiService: TMDBApiService,
  private val imdbApiLibService: IMDBApiLibService,
  private val localDataSource: LocalDataSource,
  private val appExecutors: AppExecutors
) {
  private var _creditCastMovies = MutableLiveData<List<CastItem>>()
  val creditCastMovies: LiveData<List<CastItem>> = _creditCastMovies

  private var _creditsCrewMovies = MutableLiveData<List<CrewItem>>()
  val creditCrewMovies: LiveData<List<CrewItem>> = _creditsCrewMovies

  private var _creditCastTv = MutableLiveData<List<CastItem>>()
  val creditCastTv: LiveData<List<CastItem>> = _creditCastTv

  private var _creditsCrewTv = MutableLiveData<List<CrewItem>>()
  val creditCrewTv: LiveData<List<CrewItem>> = _creditsCrewTv

  private val _snackbarText = MutableLiveData<Event<String>>()
  val snackBarText: LiveData<Event<String>> get() = _snackbarText

  private val _score = MutableLiveData<ScoreRatingResponse>()
  val score: LiveData<ScoreRatingResponse> get() = _score

  private val _detailMovie = MutableLiveData<DetailMovieResponse>()
  val detailMovie: LiveData<DetailMovieResponse> get() = _detailMovie

  private val _detailTv = MutableLiveData<DetailTvResponse>()
  val detailTv: LiveData<DetailTvResponse> get() = _detailTv

  private val _externalId = MutableLiveData<ExternalIdResponse>()
  val externalId: LiveData<ExternalIdResponse> get() = _externalId

  private val _stated = MutableLiveData<StatedResponse>()
  val stated: LiveData<StatedResponse> get() = _stated

  private val _postResponse = MutableLiveData<PostFavoriteWatchlistResponse>()
  val postResponse: LiveData<PostFavoriteWatchlistResponse> get() = _postResponse

  private val _isLoading = MutableLiveData<Boolean>()
  val isLoading: LiveData<Boolean> = _isLoading

  fun getPagingTopRatedMovies(): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(
        pageSize = 5
      ),
      pagingSourceFactory = {
        TopRatedMoviePagingSource(tmdbApiService)
      }
    ).flow
  }

  fun getPagingPopularMovies(): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(
        pageSize = 5
      ),
      pagingSourceFactory = {
        PopularMoviePagingSource(tmdbApiService)
      }
    ).flow
  }

  fun getPagingFavoriteMovies(sessionId: String): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(
        pageSize = 5
      ),
      pagingSourceFactory = {
        FavoriteMoviePagingSource(sessionId, tmdbApiService)
      }
    ).flow
  }

  fun getPagingFavoriteTv(sessionId: String): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(
        pageSize = 5
      ),
      pagingSourceFactory = {
        FavoriteTvPagingSource(sessionId, tmdbApiService)
      }
    ).flow
  }

  fun getPagingWatchlistTv(sessionId: String): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(
        pageSize = 5
      ),
      pagingSourceFactory = {
        WatchlistTvPagingSource(sessionId, tmdbApiService)
      }
    ).flow
  }

  fun getPagingWatchlistMovies(sessionId: String): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(
        pageSize = 5
      ),
      pagingSourceFactory = {
        WatchlistMoviePagingSource(sessionId, tmdbApiService)
      }
    ).flow
  }

  fun getPagingPopularTv(): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(
        pageSize = 5
      ),
      pagingSourceFactory = {
        PopularTvPagingSource(tmdbApiService)
      }
    ).flow
  }

  fun getPagingOnTv(): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(
        pageSize = 5
      ),
      pagingSourceFactory = {
        OnTvPagingSource(tmdbApiService)
      }
    ).flow
  }

  fun getPagingAiringTodayTv(): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(
        pageSize = 5
      ),
      pagingSourceFactory = {
        AiringTodayTvPagingSource(tmdbApiService)
      }
    ).flow
  }

  fun getPagingTrending(): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(
        pageSize = 5
      ),
      pagingSourceFactory = {
        TrendingPagingSource(tmdbApiService)
      }
    ).flow
  }

  fun getPagingUpcomingMovies(): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(
        pageSize = 20
      ),
      pagingSourceFactory = {
        UpcomingMoviesPagingSource(tmdbApiService)
      }
    ).flow
  }

  fun getPagingPlayingNowMovies(): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(
        pageSize = 20
      ),
      pagingSourceFactory = {
        PlayingNowMoviesPagingSource(tmdbApiService)
      }
    ).flow
  }

  fun getPagingTopRatedTv(): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(
        pageSize = 5
      ),
      pagingSourceFactory = {
        TopRatedTvPagingSource(tmdbApiService)
      }
    ).flow
  }

  fun search(query: String): Flow<PagingData<ResultsItemSearch>> {
    return Pager(
      config = PagingConfig(
        pageSize = 20
      ),
      pagingSourceFactory = {
        SearchPagingSource(tmdbApiService, query)
      }
    ).flow
  }

  fun getCreditMovies(movieId: Int) {
    val client = TMDBApiConfig
      .getApiService()
      .getCreditMovies(movieId)

    client.enqueue(object : Callback<CreditsResponse> {
      override fun onResponse(
        call: Call<CreditsResponse>,
        response: Response<CreditsResponse>
      ) {
        if (response.isSuccessful) {
          val responseBody = response.body()
          if (responseBody != null) {
            _creditsCrewMovies.value = responseBody.crew
            _creditCastMovies.value = responseBody.cast
          }
        } else {
          Log.e(TAG, "onFailure: ${response.message()}")

          // get message error
          val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
          val message = jsonObject.getString("status_message")
          _snackbarText.value = Event(message)
        }
      }

      override fun onFailure(call: Call<CreditsResponse>, t: Throwable) {
        Log.e(TAG, "onFailure: ${t.message}")
        _snackbarText.value = Event(t.message.toString())
      }
    })
  }

  fun getCreditTv(tvId: Int) {
    val client = TMDBApiConfig
      .getApiService()
      .getCreditTv(tvId)

    client.enqueue(object : Callback<CreditsResponse> {
      override fun onResponse(
        call: Call<CreditsResponse>,
        response: Response<CreditsResponse>
      ) {
        if (response.isSuccessful) {
          val responseBody = response.body()
          if (responseBody != null) {
            _creditsCrewTv.value = responseBody.crew
            _creditCastTv.value = responseBody.cast
          }
        } else {
          Log.e(TAG, "onFailure: ${response.message()}")
          val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
          val message = jsonObject.getString("status_message")
          _snackbarText.value = Event(message)
        }
      }

      override fun onFailure(call: Call<CreditsResponse>, t: Throwable) {
        Log.e(TAG, "onFailure: ${t.message}")
        _snackbarText.value = Event(t.message.toString())
      }
    })
  }

  fun getScoring(apiKey: String, id: String) {
    _isLoading.value = true
    val client = IMDBApiLibConfig
      .getIMDBLibApiService()
      .getScore(apiKey, id)

    client.enqueue(object : Callback<ScoreRatingResponse> {
      override fun onResponse(
        call: Call<ScoreRatingResponse>,
        response: Response<ScoreRatingResponse>
      ) {
        _isLoading.value = false
        if (response.isSuccessful) {
          _score.value = response.body()
        } else {
          Log.e(TAG, "onFailure: ${response.message()}")

          // get message error
          _snackbarText.value = Event(response.body()?.errorMessage.toString())
        }
      }

      override fun onFailure(call: Call<ScoreRatingResponse>, t: Throwable) {
        _isLoading.value = false
        Log.e(TAG, "onFailure: ${t.message}")
        _snackbarText.value = Event(t.message.toString())
      }
    })
  }

  fun getDetailMovie(id: Int) {
    val client = TMDBApiConfig
      .getApiService()
      .getDetailMovie(id)

    client.enqueue(object : Callback<DetailMovieResponse> {
      override fun onResponse(
        call: Call<DetailMovieResponse>,
        response: Response<DetailMovieResponse>
      ) {
        if (response.isSuccessful) {

          _detailMovie.value = response.body()
        } else {
          Log.e(TAG, "onFailure: ${response.message()}")

          // get message error
          Log.e(TAG, "onFailure: ${response.message()}")
          val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
          val message = jsonObject.getString("status_message")
          _snackbarText.value = Event(message)
        }
      }

      override fun onFailure(call: Call<DetailMovieResponse>, t: Throwable) {
        Log.e(TAG, "onFailure: ${t.message}")
        _snackbarText.value = Event(t.message.toString())
      }
    })
  }

  fun getDetailTv(id: Int) {
    val client = TMDBApiConfig
      .getApiService()
      .getDetailTv(id)

    client.enqueue(object : Callback<DetailTvResponse> {
      override fun onResponse(
        call: Call<DetailTvResponse>,
        response: Response<DetailTvResponse>
      ) {
        if (response.isSuccessful) {
          _detailTv.value = response.body()
        } else {
          Log.e(TAG, "onFailure: ${response.message()}")

          // get message error
          Log.e(TAG, "onFailure: ${response.message()}")
          val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
          val message = jsonObject.getString("status_message")
          _snackbarText.value = Event(message)
        }
      }

      override fun onFailure(call: Call<DetailTvResponse>, t: Throwable) {
        Log.e(TAG, "onFailure: ${t.message}")
        _snackbarText.value = Event(t.message.toString())
      }
    })
  }

  fun getExternalId(id: Int) {
    val client = TMDBApiConfig
      .getApiService()
      .getExternalId(id)

    client.enqueue(object : Callback<ExternalIdResponse> {
      override fun onResponse(
        call: Call<ExternalIdResponse>,
        response: Response<ExternalIdResponse>
      ) {
        if (response.isSuccessful) {
          _externalId.value = response.body()
        } else {
          Log.e(TAG, "onFailure: ${response.message()}")

          // get message error
          Log.e(TAG, "onFailure: ${response.message()}")
          val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
          val message = jsonObject.getString("status_message")
          _snackbarText.value = Event(message)
        }
      }

      override fun onFailure(call: Call<ExternalIdResponse>, t: Throwable) {
        Log.e(TAG, "onFailure: ${t.message}")
        _snackbarText.value = Event(t.message.toString())
      }
    })
  }

  fun getStatedMovie(sessionId: String, id: Int) {
    val client = TMDBApiConfig
      .getApiService()
      .getStatedMovie(id, sessionId)

    client.enqueue(object : Callback<StatedResponse> {
      override fun onResponse(
        call: Call<StatedResponse>,
        response: Response<StatedResponse>
      ) {
        if (response.isSuccessful) {
          _stated.value = response.body()
        } else {
          Log.e(TAG, "onFailure: ${response.message()}")

          // get message error
          Log.e(TAG, "onFailure: ${response.message()}")
          val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
          val message = jsonObject.getString("status_message")
          _snackbarText.value = Event(message)
        }
      }

      override fun onFailure(call: Call<StatedResponse>, t: Throwable) {
        Log.e(TAG, "onFailure: ${t.message}")
        _snackbarText.value = Event(t.message.toString())
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
        if (response.isSuccessful) {
          _stated.value = response.body()
        } else {
          Log.e(TAG, "onFailure: ${response.message()}")

          // get message error
          Log.e(TAG, "onFailure: ${response.message()}")
          val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
          val message = jsonObject.getString("status_message")
          _snackbarText.value = Event(message)
        }
      }

      override fun onFailure(call: Call<StatedResponse>, t: Throwable) {
        Log.e(TAG, "onFailure: ${t.message}")
        _snackbarText.value = Event(t.message.toString())
      }
    })
  }

  fun postFavorite(sessionId: String, data: Favorite, userId: Int) {
    val client = TMDBApiConfig
      .getApiService()
      .postFavoriteTMDB(userId, sessionId, data)

    client.enqueue(object : Callback<PostFavoriteWatchlistResponse> {
      override fun onResponse(
        call: Call<PostFavoriteWatchlistResponse>,
        response: Response<PostFavoriteWatchlistResponse>
      ) {
        if (response.isSuccessful) {
          _postResponse.value = response.body()
        } else {
          Log.e(TAG, "onFailure: ${response.message()}")

          // get message error
          Log.e(TAG, "onFailure: ${response.message()}")
          val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
          val message = jsonObject.getString("status_message")
          _snackbarText.value = Event(message)
        }
      }

      override fun onFailure(call: Call<PostFavoriteWatchlistResponse>, t: Throwable) {
        Log.e(TAG, "onFailure: ${t.message}")
        _snackbarText.value = Event(t.message.toString())
      }
    })
  }

  fun postWatchlist(sessionId: String, data: Watchlist, userId: Int) {
    val client = TMDBApiConfig
      .getApiService()
      .postWatchlistTMDB(userId, sessionId, data)

    client.enqueue(object : Callback<PostFavoriteWatchlistResponse> {
      override fun onResponse(
        call: Call<PostFavoriteWatchlistResponse>,
        response: Response<PostFavoriteWatchlistResponse>
      ) {
        if (response.isSuccessful) {
          _postResponse.value = response.body()
        } else {
          Log.e(TAG, "onFailure: ${response.message()}")

          // get message error
          Log.e(TAG, "onFailure: ${response.message()}")
          val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
          val message = jsonObject.getString("status_message")
          _snackbarText.value = Event(message)
        }
      }

      override fun onFailure(call: Call<PostFavoriteWatchlistResponse>, t: Throwable) {
        Log.e(TAG, "onFailure: ${t.message}")
        _snackbarText.value = Event(t.message.toString())
      }
    })
  }

  fun getFavoriteMoviesFromDB(): LiveData<List<FavoriteDB>> = localDataSource.getFavoriteMovies

  fun getFavoriteTvFromDB(): LiveData<List<FavoriteDB>> = localDataSource.getFavoriteTv

  fun getWatchlistMovieFromDB(): LiveData<List<FavoriteDB>> = localDataSource.getWatchlistMovies

  fun getWatchlistTvFromDB(): LiveData<List<FavoriteDB>> = localDataSource.getWatchlistTv

  fun getFavoriteDB(name: String): LiveData<List<FavoriteDB>> =
    localDataSource.getSpecificFavorite(name)

  fun insertDB(fav: FavoriteDB) {
    appExecutors.diskIO().execute { localDataSource.insertFavorite(fav) }
  }

  fun deleteFromDB(fav: FavoriteDB) {
    appExecutors.diskIO().execute { localDataSource.deleteItemFromDB(fav) }
  }

  fun isFavoriteDB(id: Int) = localDataSource.isFavorite(id)

  fun isWatchlist(id: Int) = localDataSource.isWatchlist(id)

  fun updateFavorite(boolean: Boolean, id: Int) = localDataSource.updateFavorite(boolean, id)

  fun updateWatchlist(boolean: Boolean, id: Int) = localDataSource.updateWatchlist(boolean, id)

  companion object {
    private const val TAG = "MoviesRepository "

    @Volatile
    private var instance: MoviesRepository? = null

    fun getInstance(
      TMDBApiService: TMDBApiService,
      IMDBApiLibService: IMDBApiLibService,
      localData: LocalDataSource,
      appExecutors: AppExecutors
    ): MoviesRepository =
      instance ?: synchronized(this) {
        instance ?: MoviesRepository(TMDBApiService, IMDBApiLibService, localData, appExecutors)
      }
  }
}