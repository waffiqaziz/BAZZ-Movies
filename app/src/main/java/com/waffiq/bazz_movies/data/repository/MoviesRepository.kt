package com.waffiq.bazz_movies.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.waffiq.bazz_movies.R
import com.waffiq.bazz_movies.data.local.LocalDataSource
import com.waffiq.bazz_movies.data.local.model.Favorite
import com.waffiq.bazz_movies.data.local.model.FavoriteDB
import com.waffiq.bazz_movies.data.local.model.Rate
import com.waffiq.bazz_movies.data.local.model.Watchlist
import com.waffiq.bazz_movies.data.paging.AiringTodayTvPagingSource
import com.waffiq.bazz_movies.data.paging.FavoriteMoviePagingSource
import com.waffiq.bazz_movies.data.paging.FavoriteTvPagingSource
import com.waffiq.bazz_movies.data.paging.MultiTrendingDayPagingSource
import com.waffiq.bazz_movies.data.paging.MultiTrendingWeekPagingSource
import com.waffiq.bazz_movies.data.paging.OnTvPagingSource
import com.waffiq.bazz_movies.data.paging.PlayingNowMoviesPagingSource
import com.waffiq.bazz_movies.data.paging.PopularMoviePagingSource
import com.waffiq.bazz_movies.data.paging.PopularTvPagingSource
import com.waffiq.bazz_movies.data.paging.RecommendationMoviePagingSource
import com.waffiq.bazz_movies.data.paging.RecommendationTvPagingSource
import com.waffiq.bazz_movies.data.paging.SearchPagingSource
import com.waffiq.bazz_movies.data.paging.TopRatedMoviePagingSource
import com.waffiq.bazz_movies.data.paging.TopRatedTvPagingSource
import com.waffiq.bazz_movies.data.paging.UpcomingMoviesPagingSource
import com.waffiq.bazz_movies.data.paging.WatchlistMoviePagingSource
import com.waffiq.bazz_movies.data.paging.WatchlistTvPagingSource
import com.waffiq.bazz_movies.data.remote.response.omdb.OMDbDetailsResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.CastCombinedItem
import com.waffiq.bazz_movies.data.remote.response.tmdb.CastItem
import com.waffiq.bazz_movies.data.remote.response.tmdb.CombinedCreditResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.CrewItem
import com.waffiq.bazz_movies.data.remote.response.tmdb.DetailMovieResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.DetailPersonResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.DetailTvResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.ExternalIdResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.ImagePersonResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.MovieTvCreditsResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.PostRateResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.PostResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.ProfilesItem
import com.waffiq.bazz_movies.data.remote.response.tmdb.ResultItem
import com.waffiq.bazz_movies.data.remote.response.tmdb.ResultsItemSearch
import com.waffiq.bazz_movies.data.remote.response.tmdb.StatedResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.VideoResponse
import com.waffiq.bazz_movies.data.remote.retrofit.OMDbApiConfig
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
  private val localDataSource: LocalDataSource,
  private val appExecutors: AppExecutors
) {

  // detail
  private var _creditCastMovies = MutableLiveData<List<CastItem>>()
  val creditCastMovies: LiveData<List<CastItem>> = _creditCastMovies

  private var _creditsCrewMovies = MutableLiveData<List<CrewItem>>()
  val creditCrewMovies: LiveData<List<CrewItem>> = _creditsCrewMovies

  private var _creditCastTv = MutableLiveData<List<CastItem>>()
  val creditCastTv: LiveData<List<CastItem>> = _creditCastTv

  private var _creditsCrewTv = MutableLiveData<List<CrewItem>>()
  val creditCrewTv: LiveData<List<CrewItem>> = _creditsCrewTv

  private var _linkVideoMovie = MutableLiveData<String>()
  val linkVideoMovie: LiveData<String> = _linkVideoMovie

  private var _linkVideoTv = MutableLiveData<String>()
  val linkVideoTv: LiveData<String> = _linkVideoTv

  private val _externalId = MutableLiveData<ExternalIdResponse>()
  val externalId: LiveData<ExternalIdResponse> get() = _externalId

  private val _detailOMDb = MutableLiveData<OMDbDetailsResponse>()
  val detailOMDb: LiveData<OMDbDetailsResponse> get() = _detailOMDb

  private val _detailMovie = MutableLiveData<DetailMovieResponse>()
  val detailMovie: LiveData<DetailMovieResponse> get() = _detailMovie

  private val _ageRatingMovie = MutableLiveData<String>()
  val ageRatingMovie: LiveData<String> get() = _ageRatingMovie

  private val _detailTv = MutableLiveData<DetailTvResponse>()
  val detailTv: LiveData<DetailTvResponse> get() = _detailTv

  private val _ageRatingTv = MutableLiveData<String>()
  val ageRatingTv: LiveData<String> get() = _ageRatingTv

  private val _productionCountry = MutableLiveData<String>()
  val productionCountry: LiveData<String> get() = _productionCountry


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


  // future
  private val _postResponse = MutableLiveData<String>()
  val postResponse: LiveData<String> get() = _postResponse

  private val _snackBarText = MutableLiveData<Event<String>>()
  val snackBarText: LiveData<Event<String>> get() = _snackBarText

  private val _snackBarTextInt = MutableLiveData<Event<Int>>()
  val snackBarTextInt: LiveData<Event<Int>> get() = _snackBarTextInt

  private val _isLoading = MutableLiveData<Boolean>()
  val isLoading: LiveData<Boolean> = _isLoading

  private val _undoDB = MutableLiveData<Event<FavoriteDB>>()
  val undoDB: LiveData<Event<FavoriteDB>> = _undoDB


  // paging
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

  fun getPagingTrendingWeek(region: String): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(
        pageSize = 5
      ),
      pagingSourceFactory = {
        MultiTrendingWeekPagingSource(region, tmdbApiService)
      }
    ).flow
  }

  fun getPagingTrendingDay(region: String): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(
        pageSize = 5
      ),
      pagingSourceFactory = {
        MultiTrendingDayPagingSource(region, tmdbApiService)
      }
    ).flow
  }

  fun getPagingMovieRecommendation(movieId: Int): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(
        pageSize = 5
      ),
      pagingSourceFactory = {
        RecommendationMoviePagingSource(movieId, tmdbApiService)
      }
    ).flow
  }

  fun getPagingTvRecommendation(tvId: Int): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(
        pageSize = 5
      ),
      pagingSourceFactory = {
        RecommendationTvPagingSource(tvId, tmdbApiService)
      }
    ).flow
  }

  fun getPagingUpcomingMovies(region: String): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(
        pageSize = 20
      ),
      pagingSourceFactory = {
        UpcomingMoviesPagingSource(region, tmdbApiService)
      }
    ).flow
  }

  fun getPagingPlayingNowMovies(region: String): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(
        pageSize = 20
      ),
      pagingSourceFactory = {
        PlayingNowMoviesPagingSource(region, tmdbApiService)
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

  fun getPagingSearch(query: String): Flow<PagingData<ResultsItemSearch>> {
    return Pager(
      config = PagingConfig(
        pageSize = 20
      ),
      pagingSourceFactory = {
        SearchPagingSource(tmdbApiService, query)
      }
    ).flow
  }


  // detail movie from OMDb API
  fun getDetailOMDb(imdbId: String) {
    val client = OMDbApiConfig
      .getOMDBApiService()
      .getMovieDetailOMDb(imdbId)

    client.enqueue(object : Callback<OMDbDetailsResponse> {
      override fun onResponse(
        call: Call<OMDbDetailsResponse>,
        response: Response<OMDbDetailsResponse>
      ) {
        if (response.isSuccessful) {
          _detailOMDb.value = response.body()
        } else {
          Log.e(TAG, "onFailure: ${response.message()}")

          // get message error
          _snackBarText.value = Event(response.body()?.response.toString())
        }
      }

      override fun onFailure(call: Call<OMDbDetailsResponse>, t: Throwable) {
        Log.e(TAG, "onFailure: ${t.message}")
        _snackBarText.value = Event(t.message.toString())
      }
    })
  }

  // detail data from TMDB API
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
          val responseBody = response.body()
          if (responseBody != null) {
            var productionCountry: String
            try {
              productionCountry = responseBody.productionCountries?.get(0)?.iso31661.toString()
              _productionCountry.value = productionCountry
            } catch (e: IndexOutOfBoundsException) {
              _productionCountry.value = "N/A"
              productionCountry = ""
            }
            try {
              if (productionCountry.isEmpty()) {
                _ageRatingMovie.value = "N/A"
              } else {
                _ageRatingMovie.value = responseBody.releaseDates?.results?.filter {
                  it?.iso31661 == "US" || it?.iso31661 == productionCountry
                }?.map {
                  it?.releaseDateValue?.get(0)?.certification
                }.toString().replace("[", "").replace("]", "")
                  .replace(" ", "").replace(",", "")
              }
            } catch (e: NullPointerException) {
              _ageRatingMovie.value = "N/A"
            }
          }

        } else {
          Log.e(TAG, "onFailure: ${response.message()}")

          // get message error
          Log.e(TAG, "onFailure: ${response.message()}")
          val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
          val message = jsonObject.getString("status_message")
          _snackBarText.value = Event(message)
        }
      }

      override fun onFailure(call: Call<DetailMovieResponse>, t: Throwable) {
        Log.e(TAG, "onFailure: ${t.message}")
        _snackBarText.value = Event(t.message.toString())
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
          val responseBody = response.body()
          if (responseBody != null) {
            try { // get age rating
              val productionCountry = responseBody.productionCountries?.get(0)?.iso31661

              _productionCountry.value = productionCountry!!
              _ageRatingTv.value = responseBody.contentRatings?.results?.filter {
                it?.iso31661 == "US" || it?.iso31661 == productionCountry
              }?.map { it?.rating }.toString().replace("[", "").replace("]", "")
                .replace(" ", "").replace(",", "")
            } catch (e: NullPointerException) {
              _ageRatingTv.value = "N/A"
              _productionCountry.value = "N/A"
            } catch (e: IndexOutOfBoundsException) {
              _ageRatingTv.value = "N/A"
              _productionCountry.value = "N/A"
            }
          }

        } else {
          Log.e(TAG, "onFailure: ${response.message()}")

          // get message error
          Log.e(TAG, "onFailure: ${response.message()}")
          val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
          val message = jsonObject.getString("status_message")
          _snackBarText.value = Event(message)
        }
      }

      override fun onFailure(call: Call<DetailTvResponse>, t: Throwable) {
        Log.e(TAG, "onFailure: ${t.message}")
        _snackBarText.value = Event(t.message.toString())
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
        if (response.isSuccessful) _externalId.value = response.body()
        else {
          Log.e(TAG, "onFailure: ${response.message()}")

          // get message error
          Log.e(TAG, "onFailure: ${response.message()}")
          val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
          val message = jsonObject.getString("status_message")
          _snackBarText.value = Event(message)
        }
      }

      override fun onFailure(call: Call<ExternalIdResponse>, t: Throwable) {
        Log.e(TAG, "onFailure: ${t.message}")
        _snackBarText.value = Event(t.message.toString())
      }
    })
  }

  fun getVideoMovies(movieId: Int) {
    _isLoading.value = true
    val client = TMDBApiConfig
      .getApiService()
      .getVideoMovies(movieId)

    client.enqueue(object : Callback<VideoResponse> {
      override fun onResponse(
        call: Call<VideoResponse>,
        response: Response<VideoResponse>
      ) {
        _isLoading.value = false
        if (response.isSuccessful) {
          val responseBody = response.body()
          if (responseBody != null) {

            try { //select best trailer
              _linkVideoMovie.value = responseBody.results.filter {
                it.official == true && it.type.equals("Trailer")
              }.map { it.key }[0].toString().replace("[", "").replace("]", "")
            } catch (e: IndexOutOfBoundsException) {
              _linkVideoMovie.value = ""
            }
          }
        } else {
          Log.e(TAG, "onFailure: ${response.message()}")

          // get message error
          val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
          val message = jsonObject.getString("status_message")
          _snackBarText.value = Event(message)
        }
      }

      override fun onFailure(call: Call<VideoResponse>, t: Throwable) {
        _isLoading.value = false
        Log.e(TAG, "onFailure: ${t.message}")
        _snackBarText.value = Event(t.message.toString())
      }
    })
  }

  fun getVideoTv(tvId: Int) {
    _isLoading.value = true
    val client = TMDBApiConfig
      .getApiService()
      .getVideoTv(tvId)

    client.enqueue(object : Callback<VideoResponse> {
      override fun onResponse(
        call: Call<VideoResponse>,
        response: Response<VideoResponse>
      ) {
        _isLoading.value = false
        if (response.isSuccessful) {
          val responseBody = response.body()
          if (responseBody != null) {

            try { // select best trailer
              _linkVideoTv.value = responseBody.results.filter {
                it.official == true && it.type.equals("Trailer")
              }.map { it.key }[0].toString().replace("[", "").replace("]", "")
            } catch (e: IndexOutOfBoundsException) {
              _linkVideoTv.value = ""
            }
          }
        } else {
          Log.e(TAG, "onFailure: ${response.message()}")

          // get message error
          val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
          val message = jsonObject.getString("status_message")
          _snackBarText.value = Event(message)
        }
      }

      override fun onFailure(call: Call<VideoResponse>, t: Throwable) {
        _isLoading.value = false
        Log.e(TAG, "onFailure: ${t.message}")
        _snackBarText.value = Event(t.message.toString())
      }
    })
  }

  fun getCreditMovies(movieId: Int) {
    val client = TMDBApiConfig
      .getApiService()
      .getCreditMovies(movieId)

    client.enqueue(object : Callback<MovieTvCreditsResponse> {
      override fun onResponse(
        call: Call<MovieTvCreditsResponse>,
        response: Response<MovieTvCreditsResponse>
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
          _snackBarText.value = Event(message)
        }
      }

      override fun onFailure(call: Call<MovieTvCreditsResponse>, t: Throwable) {
        _isLoading.value = false
        Log.e(TAG, "onFailure: ${t.message}")
        _snackBarText.value = Event(t.message.toString())
      }
    })
  }

  fun getCreditTv(tvId: Int) {
    val client = TMDBApiConfig
      .getApiService()
      .getCreditTv(tvId)

    client.enqueue(object : Callback<MovieTvCreditsResponse> {
      override fun onResponse(
        call: Call<MovieTvCreditsResponse>,
        response: Response<MovieTvCreditsResponse>
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
          _snackBarText.value = Event(message)
        }
      }

      override fun onFailure(call: Call<MovieTvCreditsResponse>, t: Throwable) {
        Log.e(TAG, "onFailure: ${t.message}")
        _snackBarText.value = Event(t.message.toString())
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
        val responseBody = response.body()
        if (response.isSuccessful) {
          if (responseBody != null) {
            _stated.value = response.body()
          }
        } else {
          Log.e(TAG, "onFailure: ${response.message()}")

          // get message error
          Log.e(TAG, "onFailure: ${response.message()}")
          val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
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
        if (response.isSuccessful) {
          _stated.value = response.body()
        } else {
          Log.e(TAG, "onFailure: ${response.message()}")

          // get message error
          Log.e(TAG, "onFailure: ${response.message()}")
          val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
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


  // post favorite and watchlist
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
            _postResponse.value = responseBody.statusMessage!!
            if (!data.favorite!!) _snackBarTextInt.value = Event(R.string.deleted_from_favorite2)
          }
        } else {
          Log.e(TAG, "onFailure: ${response.message()}")

          // get message error
          Log.e(TAG, "onFailure: ${response.message()}")
          val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
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
          if (responseBody != null) {
            _postResponse.value = responseBody.statusMessage!!
          }
        } else {
          Log.e(TAG, "onFailure: ${response.message()}")

          // get message error
          Log.e(TAG, "onFailure: ${response.message()}")
          val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
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
          if (responseBody != null) {
            _postResponse.value = responseBody.statusMessage!!
          }
        } else {
          Log.e(TAG, "onFailure: ${response.message()}")

          // get message error
          Log.e(TAG, "onFailure: ${response.message()}")
          val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
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
          if (responseBody != null) {
            _postResponse.value = responseBody.statusMessage!!
          }
        } else {
          Log.e(TAG, "onFailure: ${response.message()}")

          // get message error
          Log.e(TAG, "onFailure: ${response.message()}")
          val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
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


  // person
  fun getDetailPerson(id: Int) {
    val client = TMDBApiConfig
      .getApiService()
      .getDetailPerson(id)

    client.enqueue(object : Callback<DetailPersonResponse> {
      override fun onResponse(
        call: Call<DetailPersonResponse>,
        response: Response<DetailPersonResponse>
      ) {
        if (response.isSuccessful) {
          _detailPerson.value = response.body()
        } else {
          Log.e(TAG, "onFailure: ${response.message()}")

          // get message error
          Log.e(TAG, "onFailure: ${response.message()}")
          val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
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
          if (responseBody != null) {
            _knownFor.value = responseBody.cast!!
          }
        } else {
          Log.e(TAG, "onFailure: ${response.message()}")

          // get message error
          val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
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
          if (responseBody != null) {
            _imagePerson.value = responseBody.profiles!!
          }
        } else {
          Log.e(TAG, "onFailure: ${response.message()}")

          // get message error
          val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
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


  // database function
  fun getFavoriteMoviesFromDB(): LiveData<List<FavoriteDB>> = localDataSource.getFavoriteMovies

  fun getWatchlistMovieFromDB(): LiveData<List<FavoriteDB>> = localDataSource.getWatchlistMovies

  fun getWatchlistTvFromDB(): LiveData<List<FavoriteDB>> = localDataSource.getWatchlistTv

  fun getFavoriteTvFromDB(): LiveData<List<FavoriteDB>> = localDataSource.getFavoriteTv

  fun getFavoriteDB(name: String): LiveData<List<FavoriteDB>> =
    localDataSource.getSpecificFavorite(name)

  fun insertToDB(fav: FavoriteDB) = appExecutors.diskIO().execute { localDataSource.insert(fav) }

  fun deleteFromDB(fav: FavoriteDB) {
    appExecutors.diskIO().execute { localDataSource.deleteItemFromDB(fav) }
    _snackBarTextInt.value = Event(R.string.deleted_from_favorite2)
    _undoDB.value = Event(fav)
  }

  fun deleteAll() = appExecutors.diskIO().execute { localDataSource.deleteALl() }

  fun isFavoriteDB(id: Int) {
    appExecutors.diskIO().execute {
      _isFavorite.postValue(localDataSource.isFavorite(id))
    }
  }

  fun isWatchlistDB(id: Int) {
    appExecutors.diskIO().execute {
      _isWatchlist.postValue(localDataSource.isWatchlist(id))
    }
  }

  fun updateFavoriteDB(isDelete: Boolean, fav: FavoriteDB) {
    // update set is_favorite = false, (for movie that want to delete, but already on watchlist)
    if (isDelete) {
      _snackBarTextInt.value = Event(R.string.deleted_from_favorite2)
      _undoDB.value = Event(fav)

      fav.isFavorite = false
      appExecutors.diskIO().execute { localDataSource.update(fav) }
    } else {  // update set is_favorite = true, (for movie that already on watchlist)
      _undoDB.value = Event(fav)

      fav.isFavorite = true
      appExecutors.diskIO().execute { localDataSource.update(fav) }
    }
  }

  fun updateWatchlistDB(isDelete: Boolean, fav: FavoriteDB) {
    if (isDelete) { // update set is_watchlist = false
      _snackBarTextInt.value = Event(R.string.deleted_from_watchlist2)
      _undoDB.value = Event(fav)

      fav.isWatchlist = false
      appExecutors.diskIO().execute { localDataSource.update(fav) }
    } else { // update set is_watchlist = true
      _undoDB.value = Event(fav)

      fav.isWatchlist = true
      appExecutors.diskIO().execute { localDataSource.update(fav) }
    }
  }


  companion object {
    private const val TAG = "MoviesRepository "

    @Volatile
    private var instance: MoviesRepository? = null

    fun getInstance(
      tmdbApiService: TMDBApiService,
      localData: LocalDataSource,
      appExecutors: AppExecutors
    ): MoviesRepository =
      instance ?: synchronized(this) {
        instance ?: MoviesRepository(tmdbApiService, localData, appExecutors)
      }
  }
}