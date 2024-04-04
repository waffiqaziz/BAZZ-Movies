package com.waffiq.bazz_movies.ui.activity.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.waffiq.bazz_movies.data.local.datasource.LocalDataSourceInterface.Companion.ERROR_DUPLICATE_ENTRY
import com.waffiq.bazz_movies.data.local.datasource.LocalDataSourceInterface.Companion.ERROR_UNKNOWN
import com.waffiq.bazz_movies.data.local.datasource.LocalDataSourceInterface.Companion.SUCCESS
import com.waffiq.bazz_movies.data.remote.Favorite
import com.waffiq.bazz_movies.data.local.model.FavoriteDB
import com.waffiq.bazz_movies.data.remote.Rate
import com.waffiq.bazz_movies.data.remote.Watchlist
import com.waffiq.bazz_movies.data.remote.response.omdb.OMDbDetailsResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.DetailMovieResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.DetailTvResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.ExternalIdResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.MovieTvCreditsResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.StatedResponse
import com.waffiq.bazz_movies.data.repository.MoviesRepository
import com.waffiq.bazz_movies.utils.Event
import com.waffiq.bazz_movies.utils.LocalDatabaseResult
import com.waffiq.bazz_movies.utils.NetworkResult
import com.waffiq.bazz_movies.utils.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailMovieViewModel(
  private val movieRepository: MoviesRepository,
) : ViewModel() {

  // region OBSERVABLES
  private val _isFavorite = MutableLiveData<Boolean>()
  val isFavorite: LiveData<Boolean> = _isFavorite

  private val _isWatchlist = MutableLiveData<Boolean>()
  val isWatchlist: LiveData<Boolean> = _isWatchlist

  private val _localDatabaseResult = MutableLiveData<Event<LocalDatabaseResult>>()
  val localDatabaseResult: LiveData<Event<LocalDatabaseResult>> get() = _localDatabaseResult

  private val _stated = MutableLiveData<StatedResponse>()
  val stated: LiveData<StatedResponse> get() = _stated

  private val _movieTvCreditsResult = MutableLiveData<MovieTvCreditsResponse>()
  val movieTvCreditsResult: LiveData<MovieTvCreditsResponse> get() = _movieTvCreditsResult

  private val _omdbResult = MutableLiveData<OMDbDetailsResponse>()
  val omdbResult: LiveData<OMDbDetailsResponse> get() = _omdbResult

  private val _loadingState = MutableLiveData<Boolean>()
  val loadingState: LiveData<Boolean> get() = _loadingState

  private val _errorState = MutableLiveData<Event<String>>()
  val errorState: LiveData<Event<String>> get() = _errorState

  private var _linkVideo = MutableLiveData<String>()
  val linkVideo: LiveData<String> = _linkVideo

  private val _detailMovie = MutableLiveData<DetailMovieResponse>()
  val detailMovie: LiveData<DetailMovieResponse> get() = _detailMovie

  private val _detailTv = MutableLiveData<DetailTvResponse>()
  val detailTv: LiveData<DetailTvResponse> get() = _detailTv

  private val _productionCountry = MutableLiveData<String>()
  val productionCountry: LiveData<String> get() = _productionCountry

  private val _ageRating = MutableLiveData<String>()
  val ageRating: LiveData<String> get() = _ageRating

  private val _externalTvId = MutableLiveData<NetworkResult<ExternalIdResponse>>()
  val externalTvId: LiveData<NetworkResult<ExternalIdResponse>> get() = _externalTvId
  // endregion OBSERVABLES

  // region MOVIE
  fun getLinkMovie(movieId: Int) {
    viewModelScope.launch {
      movieRepository.getVideoMovies(movieId).collect { networkResult ->
        when (networkResult.status) {
          Status.SUCCESS -> {
            try {
              if (networkResult.data != null) {
                var link = networkResult.data.results
                  .filter { it.official == true && it.type.equals("Trailer") }
                  .map { it.key }
                  .firstOrNull()

                link = link?.trim() ?: ""

                if (link.isBlank()) {
                  link = networkResult.data.results
                    .map { it.key }
                    .firstOrNull()
                    ?.trim()
                    ?: ""
                }

                @Suppress("USELESS_ELVIS")
                _linkVideo.value = link ?: ""
              } else {
                _linkVideo.value = ""
              }
            } catch (e: NullPointerException) {
              _linkVideo.value = ""
            }
          }

          Status.LOADING -> {}
          Status.ERROR -> {
            _loadingState.value = false
            _errorState.value = Event(networkResult.message.toString())
          }
        }
      }
    }
  }

  fun detailMovie(id: Int) {
    viewModelScope.launch {
      movieRepository.getDetailMovie(id).collect { networkResult ->
        when (networkResult.status) {
          Status.SUCCESS -> {
            networkResult.data.let { _detailMovie.value = it }
            var productionCountry: String
            try {
              productionCountry =
                networkResult.data?.productionCountries?.get(0)?.iso31661.toString()
              _productionCountry.value = productionCountry
            } catch (e: IndexOutOfBoundsException) {
              _productionCountry.value = "N/A"
              productionCountry = ""
            }
            try {
              if (productionCountry.isEmpty()) _ageRating.value = "N/A"
              else {
                _ageRating.value = networkResult.data?.releaseDates?.results?.filter {
                  it?.iso31661 == "US" || it?.iso31661 == productionCountry
                }?.map {
                  it?.releaseDateValue?.get(0)?.certification
                }.toString().replace("[", "").replace("]", "")
                  .replace(" ", "").replace(",", ", ")
              }
            } catch (e: NullPointerException) {
              _ageRating.value = "N/A"
            }
          }

          Status.LOADING -> {}
          Status.ERROR -> {
            _loadingState.value = false
            _errorState.value = Event(networkResult.message.toString())
          }
        }
      }
    }
  }

  fun getMovieCredits(movieId: Int) {
    viewModelScope.launch {
      movieRepository.getCreditMovies(movieId).collect { networkResult ->
        when (networkResult.status) {
          Status.SUCCESS -> {
            val responseData = networkResult.data
            responseData.let { _movieTvCreditsResult.value = it }
          }

          Status.LOADING -> {}
          Status.ERROR -> {
            _loadingState.value = false
            _errorState.value = Event(networkResult.message.toString())
          }
        }
      }
    }
  }

  fun getRecommendationMovie(movieId: Int) =
    movieRepository.getPagingMovieRecommendation(movieId).cachedIn(viewModelScope).asLiveData()

  fun getStatedMovie(sessionId: String, id: Int) {
    viewModelScope.launch {
      movieRepository.getStatedMovie(sessionId, id).collect { networkResult ->
        when (networkResult.status) {
          Status.SUCCESS -> networkResult.data.let { _stated.value = it }
          Status.LOADING -> {}
          Status.ERROR -> {
            _loadingState.value = false
            _errorState.value = Event(networkResult.message.toString())
          }
        }
      }
    }
  }
  // endregion MOVIE

  // region TV-SERIES
  fun getLinkTv(tvId: Int) {
    viewModelScope.launch {
      movieRepository.getVideoTv(tvId).collect { networkResult ->
        when (networkResult.status) {
          Status.SUCCESS -> {
            try {
              if (networkResult.data != null) {
                var link = networkResult.data.results
                  .filter { it.official == true && it.type.equals("Trailer") }
                  .map { it.key }
                  .firstOrNull()

                link = link?.trim() ?: ""

                if (link.isBlank()) {
                  link = networkResult.data.results
                    .map { it.key }
                    .firstOrNull()
                    ?.trim()
                    ?: ""
                }

                @Suppress("USELESS_ELVIS")
                _linkVideo.value = link ?: ""
              } else {
                _linkVideo.value = ""
              }
            } catch (e: NullPointerException) {
              _linkVideo.value = ""
            }
          }

          Status.LOADING -> {}
          Status.ERROR -> {
            _loadingState.value = false
            _errorState.value = Event(networkResult.message.toString())
          }
        }
      }
    }
  }

  fun detailTv(id: Int) {
    viewModelScope.launch {
      movieRepository.getDetailTv(id).collect { networkResult ->
        when (networkResult.status) {
          Status.SUCCESS -> {
            if (networkResult.data != null) {
              networkResult.data.let { _detailTv.value = it }
              try { // get age rating
                val productionCountry = networkResult.data.productionCountries?.get(0)?.iso31661
                _productionCountry.value = productionCountry ?: "N/A"
                _ageRating.value = networkResult.data.contentRatings?.results?.filter {
                  it?.iso31661 == "US" || it?.iso31661 == productionCountry
                }?.map { it?.rating }.toString().replace("[", "").replace("]", "")
                  .replace(" ", "").replace(",", ", ")
              } catch (e: NullPointerException) {
                _ageRating.value = "N/A"
                _productionCountry.value = "N/A"
              } catch (e: IndexOutOfBoundsException) {
                _ageRating.value = "N/A"
                _productionCountry.value = "N/A"
              }
            }
          }

          Status.LOADING -> {}
          Status.ERROR -> {
            _loadingState.value = false
            _errorState.value = Event(networkResult.message.toString())
          }
        }
      }
    }
  }

  fun getTvCredits(tvId: Int) {
    viewModelScope.launch {
      movieRepository.getCreditTv(tvId).collect { networkResult ->
        when (networkResult.status) {
          Status.SUCCESS -> networkResult.data.let { _movieTvCreditsResult.value = it }
          Status.LOADING -> {}
          Status.ERROR -> {
            _loadingState.value = false
            _errorState.value = Event(networkResult.message.toString())
          }
        }
      }
    }
  }

  fun externalId(id: Int) {
    viewModelScope.launch {
      movieRepository.getExternalTvId(id).collect { networkResult ->
        when (networkResult.status) {
          Status.SUCCESS -> _externalTvId.value = networkResult
          Status.LOADING -> {}
          Status.ERROR -> {
            _externalTvId.value = networkResult
            _errorState.value = Event(networkResult.message.toString())
          }
        }
      }
    }
  }

  fun getRecommendationTv(tvId: Int) =
    movieRepository.getPagingTvRecommendation(tvId).cachedIn(viewModelScope).asLiveData()

  fun getStatedTv(sessionId: String, id: Int) {
    viewModelScope.launch {
      movieRepository.getStatedTv(sessionId, id).collect { networkResult ->
        when (networkResult.status) {
          Status.SUCCESS -> networkResult.data.let { _stated.value = it }
          Status.LOADING -> {}
          Status.ERROR -> {
            _loadingState.value = false
            _errorState.value = Event(networkResult.message.toString())
          }
        }
      }
    }
  }
  // endregion TV-SERIES

  fun getScoreOMDb(imdbId: String) {
    viewModelScope.launch {
      movieRepository.getDetailOMDb(imdbId).collect { networkResult ->
        when (networkResult.status) {
          Status.SUCCESS -> {
            networkResult.data.let { _omdbResult.value = it }
            _loadingState.value = false
          }

          Status.LOADING -> {}
          Status.ERROR -> {
            _loadingState.value = false
            _errorState.value = Event(networkResult.message.toString())
          }
        }
      }
    }
  }

  // region DB FUNCTION
  fun isFavoriteDB(id: Int, mediaType: String) {
    viewModelScope.launch(Dispatchers.IO) {
      _isFavorite.postValue(movieRepository.isFavoriteDB(id, mediaType))
    }
  }

  fun isWatchlistDB(id: Int, mediaType: String) {
    viewModelScope.launch(Dispatchers.IO) {
      _isWatchlist.postValue(movieRepository.isWatchlistDB(id, mediaType))
    }
  }

  fun insertToDB(fav: FavoriteDB) {
    viewModelScope.launch(Dispatchers.IO) {
      movieRepository.insertToDB(fav) { resultCode ->
        val result = when (resultCode) {
          ERROR_DUPLICATE_ENTRY -> LocalDatabaseResult.Error("Duplicate entry")
          ERROR_UNKNOWN -> LocalDatabaseResult.Error("Unknown error")
          SUCCESS -> LocalDatabaseResult.Success
          else -> LocalDatabaseResult.Error("Unknown result code: $resultCode")
        }
        _localDatabaseResult.postValue(Event(result))
      }
    }
  }

  fun updateToFavoriteDB(fav: FavoriteDB) =
    viewModelScope.launch(Dispatchers.IO) { movieRepository.updateFavoriteItemDB(false, fav) }

  fun updateToRemoveFromFavoriteDB(fav: FavoriteDB) =
    viewModelScope.launch(Dispatchers.IO) { movieRepository.updateFavoriteItemDB(true, fav) }

  fun updateToWatchlistDB(fav: FavoriteDB) =
    viewModelScope.launch(Dispatchers.IO) { movieRepository.updateWatchlistItemDB(false, fav) }

  fun updateToRemoveFromWatchlistDB(fav: FavoriteDB) =
    viewModelScope.launch(Dispatchers.IO) { movieRepository.updateWatchlistItemDB(true, fav) }

  fun delFromFavoriteDB(fav: FavoriteDB) =
    viewModelScope.launch(Dispatchers.IO) { movieRepository.deleteFromDB(fav) }
  // endregion DB FUNCTION

  // region POST FAVORITE, WATCHLIST, RATE
  fun postFavorite(sessionId: String, data: Favorite, userId: Int) =
    viewModelScope.launch(Dispatchers.IO) { movieRepository.postFavorite(sessionId, data, userId) }

  fun postWatchlist(sessionId: String, data: Watchlist, userId: Int) =
    viewModelScope.launch(Dispatchers.IO) { movieRepository.postWatchlist(sessionId, data, userId) }

  fun postMovieRate(sessionId: String, data: Rate, movieId: Int) =
    viewModelScope.launch(Dispatchers.IO) {
      movieRepository.postMovieRate(sessionId, data, movieId)
    }

  fun postTvRate(sessionId: String, data: Rate, tvId: Int) =
    viewModelScope.launch(Dispatchers.IO) { movieRepository.postTvRate(sessionId, data, tvId) }
  // endregion POST FAVORITE, WATCHLIST, RATE
}

