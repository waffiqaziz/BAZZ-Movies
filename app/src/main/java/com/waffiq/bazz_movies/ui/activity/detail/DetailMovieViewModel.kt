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
import com.waffiq.bazz_movies.data.local.model.Favorite
import com.waffiq.bazz_movies.data.local.model.FavoriteDB
import com.waffiq.bazz_movies.data.local.model.Rate
import com.waffiq.bazz_movies.data.local.model.Watchlist
import com.waffiq.bazz_movies.data.remote.response.omdb.OMDbDetailsResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.DetailMovieResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.DetailTvResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.ExternalIdResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.MovieTvCreditsResponse
import com.waffiq.bazz_movies.data.repository.MoviesRepository
import com.waffiq.bazz_movies.utils.Event
import com.waffiq.bazz_movies.utils.LocalDatabaseResult
import com.waffiq.bazz_movies.utils.RemoteResponse
import kotlinx.coroutines.launch

class DetailMovieViewModel(
  private val movieRepository: MoviesRepository,
) : ViewModel() {

  private val _localDatabaseResult = MutableLiveData<Event<LocalDatabaseResult>>()
  val localDatabaseResult: LiveData<Event<LocalDatabaseResult>> get() = _localDatabaseResult

  private val _movieTvCreditsResult = MutableLiveData<RemoteResponse<MovieTvCreditsResponse>>()
  val movieTvCreditsResult: LiveData<RemoteResponse<MovieTvCreditsResponse>> get() = _movieTvCreditsResult

  private val _omdbResult = MutableLiveData<RemoteResponse<OMDbDetailsResponse>>()
  val omdbResult: LiveData<RemoteResponse<OMDbDetailsResponse>> get() = _omdbResult

  private val _loadingState = MutableLiveData<Boolean>()
  val loadingState: LiveData<Boolean> get() = _loadingState

  private var _linkVideo = MutableLiveData<RemoteResponse<String>>()
  val linkVideo: LiveData<RemoteResponse<String>> = _linkVideo

  private val _detailMovie = MutableLiveData<DetailMovieResponse>()
  val detailMovie: LiveData<DetailMovieResponse> get() = _detailMovie

  private val _detailTv = MutableLiveData<DetailTvResponse>()
  val detailTv: LiveData<DetailTvResponse> get() = _detailTv

  private val _productionCountry = MutableLiveData<String>()
  val productionCountry: LiveData<String> get() = _productionCountry

  private val _ageRating = MutableLiveData<String>()
  val ageRating: LiveData<String> get() = _ageRating

  private val _externalTvId = MutableLiveData<RemoteResponse<ExternalIdResponse>>()
  val externalTvId: LiveData<RemoteResponse<ExternalIdResponse>> get() = _externalTvId

  // Show Data
  fun detailMovie(id: Int) {
    viewModelScope.launch {
      movieRepository.getDetailMovie(id).collect { response ->
        when (response) {
          is RemoteResponse.Success -> {
            _detailMovie.value = response.data
            var productionCountry: String
            try {
              productionCountry = response.data.productionCountries?.get(0)?.iso31661.toString()
              _productionCountry.value = productionCountry
            } catch (e: IndexOutOfBoundsException) {
              _productionCountry.value = "N/A"
              productionCountry = ""
            }
            try {
              if (productionCountry.isEmpty()) _ageRating.value = "N/A"
              else {
                _ageRating.value = response.data.releaseDates?.results?.filter {
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

          is RemoteResponse.Loading -> RemoteResponse.Loading
          is RemoteResponse.Error -> RemoteResponse.Error(Exception("Failed to fetch details movie. Please try again later."))
          else -> _linkVideo.value = RemoteResponse.Error(Exception("Unknown Error"))
        }
      }
    }
  }

  fun getMovieCredits(movieId: Int) {
    viewModelScope.launch {
      try {
        when (val result = movieRepository.getCreditMovies(movieId)) {
          is RemoteResponse.Success -> _movieTvCreditsResult.value =
            RemoteResponse.Success(result.data)

          is RemoteResponse.Error -> {
            _movieTvCreditsResult.value =
              RemoteResponse.Error(Exception("Failed to fetch movie credits. Please try again later."))
            _loadingState.value = false
          }

          is RemoteResponse.Loading -> {
            _movieTvCreditsResult.value = RemoteResponse.Loading
            _loadingState.value = true
          }

          else -> {
            RemoteResponse.Error(Exception("Unknown Error"))
            _loadingState.value = false
          }
        }
      } catch (e: Exception) {
        _movieTvCreditsResult.value = RemoteResponse.Error(e)
        _loadingState.value = false
      }
    }
  }

  fun getLinkMovie(movieId: Int) {
    viewModelScope.launch {
      try {
        when (val result = movieRepository.getVideoMovies(movieId)) {
          is RemoteResponse.Success -> {
            try { //select best trailer
              val link = result.data.results.filter {
                it.official == true && it.type.equals("Trailer")
              }.map { it.key }[0].toString().replace("[", "").replace("]", "")

              if (link.isBlank() || link.isEmpty())
                _linkVideo.value =
                  RemoteResponse.Success(result.data.results.filter { it.official == true }
                    .map { it.key }[0].toString().replace("[", "").replace("]", ""))
              else
                _linkVideo.value = RemoteResponse.Success(link)

            } catch (e: IndexOutOfBoundsException) {
              _linkVideo.value =
                RemoteResponse.Empty
              _loadingState.value = false
            }
          }

          is RemoteResponse.Error -> {
            _linkVideo.value =
              RemoteResponse.Error(Exception("Failed to fetch movie credits. Please try again later."))
            _loadingState.value = false
          }

          is RemoteResponse.Loading -> {
            _linkVideo.value = RemoteResponse.Loading
            _loadingState.value = true
          }

          else -> {
            RemoteResponse.Error(Exception("Unknown Error"))
            _loadingState.value = false
          }
        }
      } catch (e: Exception) {
        _movieTvCreditsResult.value = RemoteResponse.Error(e)
        _loadingState.value = false
      }
    }

  }

  fun detailTv(id: Int) {
    viewModelScope.launch {
      movieRepository.getDetailTv(id).collect { response ->
        when (response) {
          is RemoteResponse.Success -> {
            _detailTv.value = response.data
            try { // get age rating
              val productionCountry = response.data.productionCountries?.get(0)?.iso31661
              _productionCountry.value = productionCountry ?: "N/A"
              _ageRating.value = response.data.contentRatings?.results?.filter {
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

          is RemoteResponse.Loading -> RemoteResponse.Loading
          is RemoteResponse.Error -> RemoteResponse.Error(Exception("Failed to fetch details movie. Please try again later."))
          else -> _linkVideo.value = RemoteResponse.Error(Exception("Unknown Error"))
        }
      }
    }
  }

  fun externalId(id: Int) {
    viewModelScope.launch {
      movieRepository.getExternalTvId(id).collect { response ->
        when (response) {
          is RemoteResponse.Success -> _externalTvId.value = RemoteResponse.Success(response.data)
          is RemoteResponse.Loading -> RemoteResponse.Loading
          is RemoteResponse.Empty -> RemoteResponse.Empty
          is RemoteResponse.Error -> RemoteResponse.Error(Exception("Failed to fetch details movie. Please try again later."))
        }
      }
    }

  }

  fun getTvCredits(tvId: Int) {
    viewModelScope.launch {
      try {
        when (val result = movieRepository.getCreditTv(tvId)) {
          is RemoteResponse.Success -> _movieTvCreditsResult.value =
            RemoteResponse.Success(result.data)

          is RemoteResponse.Error -> {
            _movieTvCreditsResult.value =
              RemoteResponse.Error(Exception("Failed to fetch credits. Please try again later."))
            _loadingState.value = false
          }

          is RemoteResponse.Loading -> {
            _movieTvCreditsResult.value = RemoteResponse.Loading
            _loadingState.value = true
          }

          else -> {
            RemoteResponse.Error(Exception("Unknown Error"))
            _loadingState.value = false
          }
        }
      } catch (e: Exception) {
        _movieTvCreditsResult.value = RemoteResponse.Error(e)
        _loadingState.value = false
      }
    }
  }

  fun getLinkTv(tvId: Int) {
    viewModelScope.launch {
      movieRepository.getVideoTv(tvId).collect { response ->
        when (response) {
          is RemoteResponse.Success -> {
            try {
              var link = response.data.results.filter {
                it.official == true && it.type.equals("Trailer")
              }.map { it.key }.firstOrNull()?.toString()?.replace("[", "")?.replace("]", "")

              if (link.isNullOrBlank()) {
                link = response.data.results.filter { it.official == false }
                  .map { it.key }.firstOrNull()?.toString()?.replace("[", "")?.replace("]", "")
                if (link != null) _linkVideo.value = RemoteResponse.Success(link)
                else _linkVideo.value = RemoteResponse.Empty
              } else _linkVideo.value = RemoteResponse.Success(link)
            } catch (e: Exception) {
              _linkVideo.value = RemoteResponse.Error(e)
            }
          }

          is RemoteResponse.Loading -> RemoteResponse.Loading
          is RemoteResponse.Error -> RemoteResponse.Error(Exception("Failed to fetch video link. Please try again later."))
          else -> _linkVideo.value = RemoteResponse.Error(Exception("Unknown Error"))
        }
      }
    }
  }

  fun getScoreOMDb(imdbId: String) {
    viewModelScope.launch {
      try {
        when (val result = movieRepository.getDetailOMDb(imdbId)) {
          is RemoteResponse.Success -> {
            _omdbResult.value =
              RemoteResponse.Success(result.data)
            _loadingState.value = false
          }

          is RemoteResponse.Error -> {
            _omdbResult.value =
              RemoteResponse.Error(Exception("Failed to fetch rating score. Please try again later."))
            _loadingState.value = false
          }

          is RemoteResponse.Loading -> {
            _omdbResult.value = RemoteResponse.Loading
            _loadingState.value = true
          }

          else -> {
            RemoteResponse.Error(Exception("Unknown Error"))
            _loadingState.value = false
          }
        }
      } catch (e: Exception) {
        _omdbResult.value = RemoteResponse.Error(e)
        _loadingState.value = false
      }
    }
  }

  fun getRecommendationMovie(movieId: Int) =
    movieRepository.getPagingMovieRecommendation(movieId).cachedIn(viewModelScope).asLiveData()

  fun getRecommendationTv(tvId: Int) =
    movieRepository.getPagingTvRecommendation(tvId).cachedIn(viewModelScope).asLiveData()

  fun getStatedMovie(sessionId: String, id: Int) = movieRepository.getStatedMovie(sessionId, id)
  fun getStatedTv(sessionId: String, id: Int) = movieRepository.getStatedTv(sessionId, id)
  fun getStated() = movieRepository.stated


  // Local DB Function
  fun isFavoriteDB() = movieRepository.isFavorite
  fun isFavoriteDB(id: Int, mediaType: String) =
    viewModelScope.launch { movieRepository.isFavoriteDB(id, mediaType) }

  fun isWatchlistDB() = movieRepository.isWatchlist
  fun isWatchlistDB(id: Int, mediaType: String) =
    viewModelScope.launch { movieRepository.isWatchlistDB(id, mediaType) }

  fun insertToDB(fav: FavoriteDB) {
    viewModelScope.launch {
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
    viewModelScope.launch { movieRepository.updateFavoriteDB(false, fav) }

  fun updateToRemoveFromFavoriteDB(fav: FavoriteDB) =
    viewModelScope.launch { movieRepository.updateFavoriteDB(true, fav) }

  fun updateToWatchlistDB(fav: FavoriteDB) =
    viewModelScope.launch { movieRepository.updateWatchlistDB(false, fav) }

  fun updateToRemoveFromWatchlistDB(fav: FavoriteDB) =
    viewModelScope.launch { movieRepository.updateWatchlistDB(true, fav) }

  fun delFromFavoriteDB(fav: FavoriteDB) =
    viewModelScope.launch { movieRepository.deleteFromDB(fav) }


  // favorite & watchlist TMDB
  fun postFavorite(sessionId: String, data: Favorite, userId: Int) =
    movieRepository.postFavorite(sessionId, data, userId)

  fun postWatchlist(sessionId: String, data: Watchlist, userId: Int) =
    movieRepository.postWatchlist(sessionId, data, userId)

  // add rating
  fun postMovieRate(sessionId: String, data: Rate, movieId: Int) =
    movieRepository.postMovieRate(sessionId, data, movieId)

  fun postTvRate(sessionId: String, data: Rate, tvId: Int) =
    movieRepository.postTvRate(sessionId, data, tvId)

  fun getSnackBarText() = movieRepository.snackBarText
}