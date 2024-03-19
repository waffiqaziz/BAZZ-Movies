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
import com.waffiq.bazz_movies.data.remote.datasource.MovieDataSource
import com.waffiq.bazz_movies.data.remote.response.omdb.OMDbDetailsResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.CastCombinedItem
import com.waffiq.bazz_movies.data.remote.response.tmdb.CastItem
import com.waffiq.bazz_movies.data.remote.response.tmdb.CombinedCreditResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.CrewItem
import com.waffiq.bazz_movies.data.remote.response.tmdb.DetailMovieResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.DetailPersonResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.DetailTvResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.ExternalIDPersonResponse
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
import com.waffiq.bazz_movies.utils.AppExecutors
import com.waffiq.bazz_movies.utils.Event
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesRepository(
  private val localDataSource: LocalDataSource,
  private val movieDataSource: MovieDataSource,
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

  private val _externalIdPerson = MutableLiveData<ExternalIDPersonResponse>()
  val externalIdPerson: LiveData<ExternalIDPersonResponse> get() = _externalIdPerson


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
  fun getPagingTopRatedMovies(): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingTopRatedMovies()

  fun getPagingPopularMovies(): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingPopularMovies()

  fun getPagingFavoriteMovies(sessionId: String): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingFavoriteMovies(sessionId)

  fun getPagingFavoriteTv(sessionId: String): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingFavoriteTv(sessionId)

  fun getPagingWatchlistTv(sessionId: String): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingWatchlistTv(sessionId)

  fun getPagingWatchlistMovies(sessionId: String): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingWatchlistMovies(sessionId)

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
        if (response.isSuccessful) _detailOMDb.value = response.body()
        else {
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
              if (productionCountry.isEmpty()) _ageRatingMovie.value = "N/A"
              else {
                _ageRatingMovie.value = responseBody.releaseDates?.results?.filter {
                  it?.iso31661 == "US" || it?.iso31661 == productionCountry
                }?.map {
                  it?.releaseDateValue?.get(0)?.certification
                }.toString().replace("[", "").replace("]", "")
                  .replace(" ", "").replace(",", ", ")
              }
            } catch (e: NullPointerException) {
              _ageRatingMovie.value = "N/A"
            }
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

              _productionCountry.value = productionCountry ?: "N/A"
              _ageRatingTv.value = responseBody.contentRatings?.results?.filter {
                it?.iso31661 == "US" || it?.iso31661 == productionCountry
              }?.map { it?.rating }.toString().replace("[", "").replace("]", "")
                .replace(" ", "").replace(",", ", ")
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
          val jsonObject = JSONTokener(response.errorBody()?.string()).nextValue() as JSONObject
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
          val jsonObject = JSONTokener(response.errorBody()?.string()).nextValue() as JSONObject
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
          val jsonObject = JSONTokener(response.errorBody()?.string()).nextValue() as JSONObject
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
          val jsonObject = JSONTokener(response.errorBody()?.string()).nextValue() as JSONObject
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
          val jsonObject = JSONTokener(response.errorBody()?.string()).nextValue() as JSONObject
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
          val jsonObject = JSONTokener(response.errorBody()?.string()).nextValue() as JSONObject
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


  // database function
  fun getFavoriteMoviesFromDB(): LiveData<List<FavoriteDB>> = localDataSource.getFavoriteMovies

  fun getWatchlistMovieFromDB(): LiveData<List<FavoriteDB>> = localDataSource.getWatchlistMovies

  fun getWatchlistTvFromDB(): LiveData<List<FavoriteDB>> = localDataSource.getWatchlistTv

  fun getFavoriteTvFromDB(): LiveData<List<FavoriteDB>> = localDataSource.getFavoriteTv

  fun getFavoriteDB(name: String): LiveData<List<FavoriteDB>> =
    localDataSource.getSpecificFavorite(name)

  fun insertToDB(fav: FavoriteDB, callback: (Int) -> Unit) {
    appExecutors.diskIO().execute {
      val resultCode = localDataSource.insert(fav)
      callback.invoke(resultCode)
    }
  }

  fun deleteFromDB(fav: FavoriteDB) {
    appExecutors.diskIO().execute {
      if (fav.mediaType != null)
        localDataSource.deleteItemFromDB(fav.mediaId, fav.mediaType)
    }
    _undoDB.value = Event(fav)
  }

  fun deleteAll(callback: (Int) -> Unit) {
    appExecutors.diskIO().execute {
      val resultCode = localDataSource.deleteAll()
      callback.invoke(resultCode)
    }
  }

  fun isFavoriteDB(id: Int, mediaType: String) {
    appExecutors.diskIO().execute {
      _isFavorite.postValue(localDataSource.isFavorite(id, mediaType))
    }
  }

  fun isWatchlistDB(id: Int, mediaType: String) {
    appExecutors.diskIO().execute {
      _isWatchlist.postValue(localDataSource.isWatchlist(id, mediaType))
    }
  }

  fun updateFavoriteDB(isDelete: Boolean, fav: FavoriteDB) {
    // update set is_favorite = false, (for movie that want to delete, but already on watchlist)
    if (isDelete) {
      _undoDB.value = Event(fav)

      if (fav.isWatchlist != null && fav.mediaType != null) {
        appExecutors.diskIO().execute {
          localDataSource.update(
            isFavorite = false,
            isWatchlist = fav.isWatchlist,
            id = fav.mediaId,
            mediaType = fav.mediaType
          )
        }
      } else Log.e(TAG, "favDB: $fav")
    } else {  // update set is_favorite = true, (for movie that already on watchlist)
      _undoDB.value = Event(fav)

      if (fav.isWatchlist != null && fav.mediaType != null) {
        appExecutors.diskIO().execute {
          localDataSource.update(
            isFavorite = true,
            isWatchlist = fav.isWatchlist,
            id = fav.mediaId,
            mediaType = fav.mediaType
          )
        }
      } else Log.e(TAG, "favDB: $fav")
    }
  }

  fun updateWatchlistDB(isDelete: Boolean, fav: FavoriteDB) {
    if (isDelete) { // update set is_watchlist = false
      _undoDB.value = Event(fav)

      if (fav.isFavorite != null && fav.mediaType != null) {
        appExecutors.diskIO().execute {
          localDataSource.update(
            isFavorite = fav.isFavorite,
            isWatchlist = false,
            id = fav.mediaId,
            mediaType = fav.mediaType
          )
        }
      } else Log.e(TAG, "favDB: $fav")
    } else { // update set is_watchlist = true
      _undoDB.value = Event(fav)

      if (fav.isFavorite != null && fav.mediaType != null) {
        appExecutors.diskIO().execute {
          localDataSource.update(
            isFavorite = fav.isFavorite,
            isWatchlist = true,
            id = fav.mediaId,
            mediaType = fav.mediaType
          )
        }
      } else Log.e(TAG, "favDB: $fav")
    }
  }


  companion object {
    private const val TAG = "MoviesRepository "

    @Volatile
    private var instance: MoviesRepository? = null

    fun getInstance(
      localData: LocalDataSource,
      movieDataSource: MovieDataSource,
      appExecutors: AppExecutors
    ): MoviesRepository =
      instance ?: synchronized(this) {
        instance ?: MoviesRepository(localData, movieDataSource, appExecutors)
      }
  }
}