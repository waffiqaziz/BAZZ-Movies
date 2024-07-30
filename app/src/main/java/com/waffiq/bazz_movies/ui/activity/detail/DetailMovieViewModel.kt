package com.waffiq.bazz_movies.ui.activity.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.waffiq.bazz_movies.data.local.datasource.LocalDataSourceInterface.Companion.ERROR_DUPLICATE_ENTRY
import com.waffiq.bazz_movies.data.local.datasource.LocalDataSourceInterface.Companion.ERROR_UNKNOWN
import com.waffiq.bazz_movies.data.local.datasource.LocalDataSourceInterface.Companion.SUCCESS
import com.waffiq.bazz_movies.data.remote.post_body.FavoritePostModel
import com.waffiq.bazz_movies.data.remote.post_body.RatePostModel
import com.waffiq.bazz_movies.data.remote.post_body.WatchlistPostModel
import com.waffiq.bazz_movies.domain.model.Favorite
import com.waffiq.bazz_movies.domain.model.ResultItem
import com.waffiq.bazz_movies.domain.model.Stated
import com.waffiq.bazz_movies.domain.model.detail.DetailMovie
import com.waffiq.bazz_movies.domain.model.detail.MovieTvCredits
import com.waffiq.bazz_movies.domain.model.detail.tv.DetailTv
import com.waffiq.bazz_movies.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.domain.usecase.get_detail_movie.GetDetailMovieUseCase
import com.waffiq.bazz_movies.domain.usecase.get_detail_omdb.GetDetailOMDbUseCase
import com.waffiq.bazz_movies.domain.usecase.get_detail_tv.GetDetailTvUseCase
import com.waffiq.bazz_movies.domain.usecase.get_stated.GetStatedMovieUseCase
import com.waffiq.bazz_movies.domain.usecase.get_stated.GetStatedTvUseCase
import com.waffiq.bazz_movies.domain.usecase.local_database.LocalDatabaseUseCase
import com.waffiq.bazz_movies.domain.usecase.post_method.PostMethodUseCase
import com.waffiq.bazz_movies.utils.LocalResult
import com.waffiq.bazz_movies.utils.Status
import com.waffiq.bazz_movies.utils.common.Event
import com.waffiq.bazz_movies.utils.helper.PostModelState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailMovieViewModel(
  private val getDetailMovieUseCase: GetDetailMovieUseCase,
  private val getDetailTvUseCase: GetDetailTvUseCase,
  private val localDatabaseUseCase: LocalDatabaseUseCase,
  private val postMethodUseCase: PostMethodUseCase,
  private val getDetailOMDbUseCase: GetDetailOMDbUseCase,
  private val getStatedMovieUseCase: GetStatedMovieUseCase,
  private val getStatedTvUseCase: GetStatedTvUseCase
) : ViewModel() {

  // region OBSERVABLES
  private val _isFavorite = MutableLiveData<Boolean>()
  val isFavorite: LiveData<Boolean> = _isFavorite

  private val _isWatchlist = MutableLiveData<Boolean>()
  val isWatchlist: LiveData<Boolean> = _isWatchlist

  private val _localResult = MutableLiveData<Event<LocalResult>>()
  val localResult: LiveData<Event<LocalResult>> get() = _localResult

  private val _stated = MutableLiveData<Stated>()
  val stated: LiveData<Stated> get() = _stated

  private val _movieTvCreditsResult = MutableLiveData<MovieTvCredits>()
  val movieTvCreditsResult: LiveData<MovieTvCredits> get() = _movieTvCreditsResult

  private val _omdbResult = MutableLiveData<OMDbDetails>()
  val omdbResult: LiveData<OMDbDetails> get() = _omdbResult

  private val _loadingState = MutableLiveData<Boolean>()
  val loadingState: LiveData<Boolean> get() = _loadingState

  private val _errorState = MutableLiveData<Event<String>>()
  val errorState: LiveData<Event<String>> get() = _errorState

  private val _rateState = MutableLiveData<Event<Boolean>>()
  val rateState: LiveData<Event<Boolean>> get() = _rateState

  private val _postModelState = MutableLiveData<Event<PostModelState>>()
  val postModelState: LiveData<Event<PostModelState>> get() = _postModelState

  private var _linkVideo = MutableLiveData<String>()
  val linkVideo: LiveData<String> = _linkVideo

  private val _detailMovie = MutableLiveData<DetailMovie>()
  val detailMovie: LiveData<DetailMovie> get() = _detailMovie

  private val _detailTv = MutableLiveData<DetailTv>()
  val detailTv: LiveData<DetailTv> get() = _detailTv

  private val _productionCountry = MutableLiveData<String>()
  val productionCountry: LiveData<String> get() = _productionCountry

  private val _ageRating = MutableLiveData<String>()
  val ageRating: LiveData<String> get() = _ageRating

  private val _tmdbScore = MutableLiveData<String>()
  val tmdbScore: LiveData<String> get() = _tmdbScore

  private val _tvImdbID = MutableLiveData<String>()
  val tvImdbID: LiveData<String> get() = _tvImdbID
  // endregion OBSERVABLES

  // region MOVIE
  fun getLinkMovie(movieId: Int) {
    viewModelScope.launch {
      getDetailMovieUseCase.getVideoMovies(movieId).collect { networkResult ->
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

  fun detailMovie(id: Int, region: String) {
    viewModelScope.launch {
      getDetailMovieUseCase.getDetailMovie(id).collect { networkResult ->
        when (networkResult.status) {
          Status.SUCCESS -> {
            networkResult.data.let { _detailMovie.value = it }

            // production country
            var productionCountry: String
            try {
              productionCountry =
                networkResult.data?.listProductionCountriesItem?.get(0)?.iso31661.toString()
              _productionCountry.value = productionCountry
            } catch (e: IndexOutOfBoundsException) {
              _productionCountry.value = "N/A"
              productionCountry = ""
            }

            // age rating
            try {
              _ageRating.value =
                if (region == "N/A" && productionCountry.isEmpty()) "N/A"
                else {
                  networkResult.data?.releaseDates?.listReleaseDatesItem?.filter {
                    if (region != "N/A") it?.iso31661 == region
                    else if (productionCountry.isNotEmpty()) it?.iso31661 == productionCountry
                    else it?.iso31661 == "US"
                  }?.map {
                    it?.listReleaseDatesitemValue?.get(0)?.certification
                  }.toString().replace("[", "").replace("]", "")
                    .replace(" ", "").replace(",", ", ")
                }
            } catch (e: NullPointerException) {
              _ageRating.value = "N/A"
            }

            // tmdb score
            _tmdbScore.value = if (networkResult.data?.voteAverage == 0.0
              || networkResult.data?.voteAverage == null
              || networkResult.data.voteAverage.toString().isEmpty()
              || networkResult.data.voteAverage.toString().isBlank()
            ) ""
            else networkResult.data.voteAverage.toString()


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
      getDetailMovieUseCase.getCreditMovies(movieId).collect { networkResult ->
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

  fun getRecommendationMovie(movieId: Int): LiveData<PagingData<ResultItem>> =
    getDetailMovieUseCase.getPagingMovieRecommendation(movieId).cachedIn(viewModelScope)
      .asLiveData()

  fun getStatedMovie(sessionId: String, id: Int) {
    viewModelScope.launch {
      getStatedMovieUseCase.getStatedMovie(sessionId, id).collect { networkResult ->
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
      getDetailTvUseCase.getVideoTv(tvId).collect { networkResult ->
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
      getDetailTvUseCase.getDetailTv(id).collect { networkResult ->
        when (networkResult.status) {
          Status.SUCCESS -> {
            if (networkResult.data != null) {
              networkResult.data.let { _detailTv.value = it }
              try { // get age rating
                val productionCountry =
                  networkResult.data.listProductionCountriesItem?.get(0)?.iso31661
                _productionCountry.value = productionCountry ?: "N/A"
                _ageRating.value =
                  networkResult.data.contentRatingsResponse?.contentRatingsItemResponse?.filter {
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
      getDetailTvUseCase.getCreditTv(tvId).collect { networkResult ->
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
      getDetailTvUseCase.getExternalTvId(id).collect { networkResult ->
        when (networkResult.status) {
          Status.SUCCESS -> _tvImdbID.value = networkResult.data?.imdbId ?: ""
          Status.LOADING -> {}
          Status.ERROR -> {
            _errorState.value = Event(networkResult.message.toString())
          }
        }
      }
    }
  }

  fun getRecommendationTv(tvId: Int): LiveData<PagingData<ResultItem>> =
    getDetailTvUseCase.getPagingTvRecommendation(tvId).cachedIn(viewModelScope).asLiveData()

  fun getStatedTv(sessionId: String, id: Int) {
    viewModelScope.launch {
      getStatedTvUseCase.getStatedTv(sessionId, id).collect { networkResult ->
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
      getDetailOMDbUseCase.getDetailOMDb(imdbId).collect { networkResult ->
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
      _isFavorite.postValue(localDatabaseUseCase.isFavoriteDB(id, mediaType))
    }
  }

  fun isWatchlistDB(id: Int, mediaType: String) {
    viewModelScope.launch(Dispatchers.IO) {
      _isWatchlist.postValue(localDatabaseUseCase.isWatchlistDB(id, mediaType))
    }
  }

  fun insertToDB(fav: Favorite) {
    viewModelScope.launch(Dispatchers.IO) {
      localDatabaseUseCase.insertToDB(fav) { resultCode ->
        val result = when (resultCode) {
          ERROR_DUPLICATE_ENTRY -> LocalResult.Error("Duplicate entry")
          ERROR_UNKNOWN -> LocalResult.Error("Unknown error")
          SUCCESS -> LocalResult.Success
          else -> LocalResult.Error("Unknown result code: $resultCode")
        }
        _localResult.postValue(Event(result))
      }
    }
  }

  fun updateToFavoriteDB(fav: Favorite) =
    viewModelScope.launch(Dispatchers.IO) { localDatabaseUseCase.updateFavoriteItemDB(false, fav) }

  fun updateToRemoveFromFavoriteDB(fav: Favorite) =
    viewModelScope.launch(Dispatchers.IO) { localDatabaseUseCase.updateFavoriteItemDB(true, fav) }

  fun updateToWatchlistDB(fav: Favorite) =
    viewModelScope.launch(Dispatchers.IO) { localDatabaseUseCase.updateWatchlistItemDB(false, fav) }

  fun updateToRemoveFromWatchlistDB(fav: Favorite) =
    viewModelScope.launch(Dispatchers.IO) { localDatabaseUseCase.updateWatchlistItemDB(true, fav) }

  fun delFromFavoriteDB(fav: Favorite) =
    viewModelScope.launch(Dispatchers.IO) { localDatabaseUseCase.deleteFromDB(fav) }
  // endregion DB FUNCTION

  // region POST FAVORITE, WATCHLIST, RATE
  fun postFavorite(sessionId: String, data: FavoritePostModel, userId: Int) {
    viewModelScope.launch {
      postMethodUseCase.postFavorite(sessionId, data, userId).collect { networkResult ->
        when (networkResult.status) {
          Status.SUCCESS -> {
            if (data.favorite != null) {
              _postModelState.value = Event(
                PostModelState(
                  isSuccess = true,
                  isDelete = !data.favorite,
                  isFavorite = true,
                  isWatchlist = false
                )
              )
            } else _errorState.value = Event("Data is null")
            _loadingState.value = false
          }

          Status.LOADING -> _loadingState.value = true
          Status.ERROR -> {
            if (data.favorite != null) {
              _postModelState.value = Event(
                PostModelState(
                  isSuccess = false,
                  isDelete = !data.favorite,
                  isFavorite = true,
                  isWatchlist = false
                )
              )
            }
            _errorState.value = Event(networkResult.message.toString())
            _loadingState.value = false
          }
        }
      }
    }
  }

  fun postWatchlist(sessionId: String, data: WatchlistPostModel, userId: Int) {
    viewModelScope.launch {
      postMethodUseCase.postWatchlist(sessionId, data, userId).collect { networkResult ->
        when (networkResult.status) {
          Status.SUCCESS -> {
            if (data.watchlist != null) {
              _postModelState.value = Event(
                PostModelState(
                  isSuccess = true,
                  isDelete = !data.watchlist,
                  isFavorite = true,
                  isWatchlist = true
                )
              )
            } else _errorState.value = Event("WatchlistPostModel is Null")
            _loadingState.value = false
          }

          Status.LOADING -> _loadingState.value = true
          Status.ERROR -> {
            if (data.watchlist != null) {
              _postModelState.value = Event(
                PostModelState(
                  isSuccess = false,
                  isDelete = !data.watchlist,
                  isFavorite = true,
                  isWatchlist = true
                )
              )
            }
            _errorState.value = Event(networkResult.message.toString())
            _loadingState.value = false
          }
        }
      }
    }
  }

  fun postMovieRate(sessionId: String, data: RatePostModel, movieId: Int) {
    viewModelScope.launch {
      postMethodUseCase.postMovieRate(sessionId, data, movieId).collect { networkResult ->
        when (networkResult.status) {
          Status.SUCCESS -> {
            _rateState.value = Event(true)
            _loadingState.value = false
          }

          Status.LOADING -> _loadingState.value = true
          Status.ERROR -> {
            _loadingState.value = false
            _rateState.value = Event(false)
            _errorState.value = Event(networkResult.message.toString())
          }
        }
      }
    }
  }

  fun postTvRate(sessionId: String, data: RatePostModel, tvId: Int) {
    viewModelScope.launch {
      postMethodUseCase.postTvRate(sessionId, data, tvId).collect { networkResult ->
        when (networkResult.status) {
          Status.SUCCESS -> {
            _rateState.value = Event(true)
            _loadingState.value = false
          }

          Status.LOADING -> _loadingState.value = true
          Status.ERROR -> {
            _loadingState.value = false
            _rateState.value = Event(false)
            _errorState.value = Event(networkResult.message.toString())
          }
        }
      }
    }
  }
  // endregion POST FAVORITE, WATCHLIST, RATE
}