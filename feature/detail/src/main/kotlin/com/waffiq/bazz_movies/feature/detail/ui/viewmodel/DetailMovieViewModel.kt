package com.waffiq.bazz_movies.feature.detail.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.database.domain.usecase.localdatabase.LocalDatabaseUseCase
import com.waffiq.bazz_movies.core.database.utils.DatabaseMapper.favFalseWatchlistTrue
import com.waffiq.bazz_movies.core.database.utils.DatabaseMapper.favTrueWatchlistFalse
import com.waffiq.bazz_movies.core.database.utils.DatabaseMapper.favTrueWatchlistTrue
import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.domain.Favorite
import com.waffiq.bazz_movies.core.domain.FavoriteModel
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.ResultItem
import com.waffiq.bazz_movies.core.domain.Stated
import com.waffiq.bazz_movies.core.domain.WatchlistModel
import com.waffiq.bazz_movies.core.movie.domain.usecase.getstated.GetStatedMovieUseCase
import com.waffiq.bazz_movies.core.movie.domain.usecase.getstated.GetStatedTvUseCase
import com.waffiq.bazz_movies.core.movie.domain.usecase.postmethod.PostMethodUseCase
import com.waffiq.bazz_movies.feature.detail.domain.model.DetailMovieTvUsed
import com.waffiq.bazz_movies.feature.detail.domain.model.MovieTvCredits
import com.waffiq.bazz_movies.feature.detail.domain.model.PostModelState
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.ExternalTvID
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.CountryProviderData
import com.waffiq.bazz_movies.feature.detail.domain.usecase.getDetailMovie.GetDetailMovieUseCase
import com.waffiq.bazz_movies.feature.detail.domain.usecase.getDetailOmdb.GetDetailOMDbUseCase
import com.waffiq.bazz_movies.feature.detail.domain.usecase.getDetailTv.GetDetailTvUseCase
import com.waffiq.bazz_movies.feature.detail.ui.state.WatchProvidersUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("TooManyFunctions")
@HiltViewModel
class DetailMovieViewModel @Inject constructor(
  private val getDetailMovieUseCase: GetDetailMovieUseCase,
  private val getDetailTvUseCase: GetDetailTvUseCase,
  private val localDatabaseUseCase: LocalDatabaseUseCase,
  private val postMethodUseCase: PostMethodUseCase,
  private val getDetailOMDbUseCase: GetDetailOMDbUseCase,
  private val getStatedMovieUseCase: GetStatedMovieUseCase,
  private val getStatedTvUseCase: GetStatedTvUseCase,
) : ViewModel() {

  // region OBSERVABLES
  private val _isFavorite = MutableLiveData<Boolean>()
  val isFavorite: LiveData<Boolean> = _isFavorite

  private val _isWatchlist = MutableLiveData<Boolean>()
  val isWatchlist: LiveData<Boolean> = _isWatchlist

  private val _itemState = MutableLiveData<Stated>()
  val itemState: LiveData<Stated> get() = _itemState

  private val _movieTvCreditsResult = MutableLiveData<MovieTvCredits>()
  val movieTvCreditsResult: LiveData<MovieTvCredits> get() = _movieTvCreditsResult

  private val _omdbResult = MutableLiveData<OMDbDetails>()
  val omdbResult: LiveData<OMDbDetails> get() = _omdbResult

  private val _loadingState = MutableLiveData<Boolean>()
  val loadingState: LiveData<Boolean> get() = _loadingState

  private val _errorState = MutableSharedFlow<String>(replay = 0)
  val errorState: SharedFlow<String> get() = _errorState

  private val _rateState = MutableLiveData<Event<Boolean>>()
  val rateState: LiveData<Event<Boolean>> get() = _rateState

  private val _postModelState = MutableLiveData<Event<PostModelState>>()
  val postModelState: LiveData<Event<PostModelState>> get() = _postModelState

  private var _linkVideo = MutableLiveData<String>()
  val linkVideo: LiveData<String> = _linkVideo

  private val _detailMovieTv = MutableLiveData<DetailMovieTvUsed>()
  val detailMovieTv: LiveData<DetailMovieTvUsed> get() = _detailMovieTv

  private val _tvExternalID = MutableLiveData<ExternalTvID>()
  val tvExternalID: LiveData<ExternalTvID> get() = _tvExternalID

  private val _recommendation = MutableLiveData<PagingData<ResultItem>>()
  val recommendation: LiveData<PagingData<ResultItem>> get() = _recommendation

  private val _watchProvidersUiState = MutableLiveData<WatchProvidersUiState>()
  val watchProvidersUiState: LiveData<WatchProvidersUiState> = _watchProvidersUiState
  // endregion OBSERVABLES

  // region MOVIE
  fun getLinkVideoMovie(movieId: Int) {
    viewModelScope.launch {
      getDetailMovieUseCase.getLinkVideoMovies(movieId).collect { outcome ->
        when (outcome) {
          is Outcome.Success -> outcome.data.let { _linkVideo.value = it }
          is Outcome.Loading -> {}
          is Outcome.Error -> {
            _loadingState.value = false
            _errorState.emit(outcome.message)
          }
        }
      }
    }
  }

  fun detailMovie(id: Int, userRegion: String) {
    viewModelScope.launch {
      getDetailMovieUseCase.getDetailMovie(id, userRegion).collect { outcome ->
        when (outcome) {
          is Outcome.Success -> outcome.data.let { _detailMovieTv.value = it }
          is Outcome.Loading -> {}
          is Outcome.Error -> {
            _loadingState.value = false
            _errorState.emit(outcome.message)
          }
        }
      }
    }
  }

  fun getMovieCredits(movieId: Int) {
    viewModelScope.launch {
      getDetailMovieUseCase.getCreditMovies(movieId).collect { outcome ->
        when (outcome) {
          is Outcome.Success -> outcome.data.let { _movieTvCreditsResult.value = it }
          is Outcome.Loading -> {}
          is Outcome.Error -> {
            _loadingState.value = false
            _errorState.emit(outcome.message)
          }
        }
      }
    }
  }

  fun getRecommendationMovie(movieId: Int) {
    viewModelScope.launch {
      getDetailMovieUseCase.getPagingMovieRecommendation(movieId).collectLatest {
        _recommendation.value = it
      }
    }
  }

  fun getStatedMovie(sessionId: String, id: Int) {
    viewModelScope.launch {
      getStatedMovieUseCase.getStatedMovie(sessionId, id).collect { outcome ->
        when (outcome) {
          is Outcome.Success -> outcome.data.let { _itemState.value = it }
          is Outcome.Loading -> {}
          is Outcome.Error -> {
            _loadingState.value = false
            _errorState.emit(outcome.message)
          }
        }
      }
    }
  }

  fun getMovieWatchProviders(countryCode: String, movieId: Int) {
    viewModelScope.launch {
      collectWatchProviders(getDetailMovieUseCase.getWatchProvidersMovies(countryCode, movieId))
    }
  }
  // endregion MOVIE

  // region TV-SERIES
  fun getLinkTv(tvId: Int) {
    viewModelScope.launch {
      getDetailTvUseCase.getTrailerLinkTv(tvId).collect { outcome ->
        when (outcome) {
          is Outcome.Success -> outcome.data.let { _linkVideo.value = it }
          is Outcome.Loading -> {}
          is Outcome.Error -> {
            _loadingState.value = false
            _errorState.emit(outcome.message)
          }
        }
      }
    }
  }

  fun getExternalTvId(tvId: Int) {
    viewModelScope.launch {
      getDetailTvUseCase.getExternalTvId(tvId).collect { outcome ->
        when (outcome) {
          is Outcome.Success -> _tvExternalID.value = outcome.data
          is Outcome.Loading -> {}
          is Outcome.Error -> _errorState.emit(outcome.message)
        }
      }
    }
  }

  fun detailTv(id: Int, userRegion: String) {
    viewModelScope.launch {
      getDetailTvUseCase.getDetailTv(id, userRegion).collect { outcome ->
        when (outcome) {
          is Outcome.Success -> outcome.data.let { _detailMovieTv.value = it }
          is Outcome.Loading -> {}
          is Outcome.Error -> {
            _loadingState.value = false
            _errorState.emit(outcome.message)
          }
        }
      }
    }
  }

  fun getTvCredits(tvId: Int) {
    viewModelScope.launch {
      getDetailTvUseCase.getCreditTv(tvId).collect { outcome ->
        when (outcome) {
          is Outcome.Success -> outcome.data.let { _movieTvCreditsResult.value = it }
          is Outcome.Loading -> {}
          is Outcome.Error -> {
            _loadingState.value = false
            _errorState.emit(outcome.message)
          }
        }
      }
    }
  }

  fun getRecommendationTv(tvId: Int) {
    viewModelScope.launch {
      getDetailTvUseCase.getPagingTvRecommendation(tvId).collectLatest {
        _recommendation.value = it
      }
    }
  }

  fun getStatedTv(sessionId: String, id: Int) {
    viewModelScope.launch {
      getStatedTvUseCase.getStatedTv(sessionId, id).collect { outcome ->
        when (outcome) {
          is Outcome.Success -> outcome.data.let { _itemState.value = it }
          is Outcome.Loading -> {}
          is Outcome.Error -> {
            _loadingState.value = false
            _errorState.emit(outcome.message)
          }
        }
      }
    }
  }

  fun getTvWatchProviders(countryCode: String, tvId: Int) {
    viewModelScope.launch {
      collectWatchProviders(getDetailTvUseCase.getWatchProvidersTv(countryCode, tvId))
    }
  }
  // endregion TV-SERIES

  private fun collectWatchProviders(flow: Flow<Outcome<CountryProviderData>>) {
    viewModelScope.launch {
      flow.collect { outcome ->
        _watchProvidersUiState.value = when (outcome) {
          is Outcome.Loading -> WatchProvidersUiState.Loading
          is Outcome.Success -> WatchProvidersUiState.Success(
            flatrate = outcome.data.flatrate.orEmpty(),
            rent = outcome.data.rent.orEmpty(),
            buy = outcome.data.buy.orEmpty(),
            free = outcome.data.free.orEmpty(),
          )

          is Outcome.Error -> WatchProvidersUiState.Error(outcome.message)
        }
      }
    }
  }

  fun getScoreOMDb(imdbId: String) {
    viewModelScope.launch {
      getDetailOMDbUseCase.getDetailOMDb(imdbId).collectLatest { outcome ->
        when (outcome) {
          is Outcome.Success -> {
            outcome.data.let { _omdbResult.value = it }
            _loadingState.value = false
          }

          is Outcome.Loading -> {}
          is Outcome.Error -> {
            _loadingState.value = false
            _errorState.emit(outcome.message)
          }
        }
      }
    }
  }

  // region DB FUNCTION
  fun handleBtnFavorite(favorite: Boolean, watchlist: Boolean, data: ResultItem) {
    when {
      // If in the watchlist but not a favorite, update to set as favorite.
      !favorite && watchlist -> updateToFavoriteDB(favTrueWatchlistTrue(data))

      // If neither a favorite nor in the watchlist, insert item with favorite set to true.
      !favorite && !watchlist -> insertToDB(favTrueWatchlistFalse(data))

      // If both a favorite and in the watchlist, update to remove as a favorite.
      favorite && watchlist -> updateToRemoveFromFavoriteDB(favFalseWatchlistTrue(data))

      // If not a favorite, remove it from the database.
      else -> delFromFavoriteDB(favTrueWatchlistFalse(data))
    }
  }

  fun handleBtnWatchlist(favorite: Boolean, watchlist: Boolean, data: ResultItem) {
    when {
      // If marked as a favorite but not in the watchlist, update watchlist to true.
      favorite && !watchlist -> updateToWatchlistDB(favTrueWatchlistTrue(data))

      // If it's neither a favorite nor in the watchlist, insert the item and set watchlist to true.
      !favorite && !watchlist -> insertToDB(favFalseWatchlistTrue(data))

      // If both favorite and in the watchlist, update watchlist to false.
      favorite && watchlist -> updateToRemoveFromWatchlistDB(favTrueWatchlistFalse(data))

      // If not a favorite, remove it from favorites.
      else -> delFromFavoriteDB(favFalseWatchlistTrue(data))
    }
  }

  fun isFavoriteDB(id: Int, mediaType: String) {
    viewModelScope.launch {
      when (val result = localDatabaseUseCase.isFavoriteDB(id, mediaType)) {
        is DbResult.Success -> result.data.let { _isFavorite.value = it }
        is DbResult.Error -> _errorState.emit(result.errorMessage)
      }
    }
  }

  fun isWatchlistDB(id: Int, mediaType: String) {
    viewModelScope.launch {
      when (val result = localDatabaseUseCase.isWatchlistDB(id, mediaType)) {
        is DbResult.Success -> result.data.let { _isWatchlist.value = it }
        is DbResult.Error -> _errorState.emit(result.errorMessage)
      }
    }
  }

  private fun insertToDB(fav: Favorite) {
    viewModelScope.launch {
      when (val result = localDatabaseUseCase.insertToDB(fav)) {
        is DbResult.Error -> _errorState.emit(result.errorMessage)
        is DbResult.Success -> {
          if (fav.isFavorite) {
            _isFavorite.value = true
          } else if (fav.isWatchlist) _isWatchlist.value = true
          _postModelState.value = Event(
            PostModelState(
              isSuccess = true,
              isDelete = false,
              isFavorite = fav.isFavorite,
            )
          )
        }
      }
    }
  }

  private fun updateToFavoriteDB(fav: Favorite) = viewModelScope.launch {
    when (val result = localDatabaseUseCase.updateFavoriteItemDB(false, fav)) {
      is DbResult.Error -> _errorState.emit(result.errorMessage)
      is DbResult.Success -> {
        _isFavorite.value = true
        _postModelState.value = Event(
          PostModelState(
            isSuccess = true,
            isDelete = false,
            isFavorite = true
          )
        )
      }
    }
  }

  private fun updateToRemoveFromFavoriteDB(fav: Favorite) = viewModelScope.launch {
    when (val result = localDatabaseUseCase.updateFavoriteItemDB(true, fav)) {
      is DbResult.Error -> _errorState.emit(result.errorMessage)
      is DbResult.Success -> {
        _isFavorite.value = false
        _postModelState.value = Event(
          PostModelState(
            isSuccess = true,
            isDelete = true,
            isFavorite = true,
          )
        )
      }
    }
  }

  private fun updateToWatchlistDB(fav: Favorite) = viewModelScope.launch {
    when (val result = localDatabaseUseCase.updateWatchlistItemDB(false, fav)) {
      is DbResult.Error -> _errorState.emit(result.errorMessage)
      is DbResult.Success -> {
        _isWatchlist.value = true
        _postModelState.value = Event(
          PostModelState(
            isSuccess = true,
            isDelete = false,
            isFavorite = false
          )
        )
      }
    }
  }

  private fun updateToRemoveFromWatchlistDB(fav: Favorite) = viewModelScope.launch {
    when (val result = localDatabaseUseCase.updateWatchlistItemDB(true, fav)) {
      is DbResult.Error -> _errorState.emit(result.errorMessage)
      is DbResult.Success -> {
        _isWatchlist.value = false
        _postModelState.value = Event(
          PostModelState(
            isSuccess = true,
            isDelete = true,
            isFavorite = false
          )
        )
      }
    }
  }

  private fun delFromFavoriteDB(fav: Favorite) = viewModelScope.launch {
    when (val result = localDatabaseUseCase.deleteFromDB(fav)) {
      is DbResult.Error -> _errorState.emit(result.errorMessage)
      is DbResult.Success -> {
        _isFavorite.value = false
        _isWatchlist.value = false
        _postModelState.value = Event(
          PostModelState(
            isSuccess = true,
            isDelete = true,
            isFavorite = fav.isFavorite
          )
        )
      }
    }
  }
  // endregion DB FUNCTION

  // region POST FAVORITE, WATCHLIST, RATE
  fun postFavorite(sessionId: String, data: FavoriteModel, userId: Int) {
    viewModelScope.launch {
      postMethodUseCase.postFavorite(sessionId, data, userId).collect { outcome ->
        when (outcome) {
          is Outcome.Success -> {
            if (data.mediaType == MOVIE_MEDIA_TYPE) {
              getStatedMovie(sessionId, data.mediaId)
            } else {
              getStatedTv(sessionId, data.mediaId)
            }
            _postModelState.value = Event(
              PostModelState(
                isSuccess = true,
                isDelete = !data.favorite,
                isFavorite = true,
              )
            )
            _loadingState.value = false
          }

          is Outcome.Loading -> _loadingState.value = true
          is Outcome.Error -> {
            _postModelState.value = Event(
              PostModelState(
                isSuccess = false,
                isDelete = !data.favorite,
                isFavorite = true,
              )
            )
            data.favorite.let { _isFavorite.value = it }
            _errorState.emit(outcome.message)
            _loadingState.value = false
          }
        }
      }
    }
  }

  fun postWatchlist(sessionId: String, data: WatchlistModel, userId: Int) {
    viewModelScope.launch {
      postMethodUseCase.postWatchlist(sessionId, data, userId).collect { outcome ->
        when (outcome) {
          is Outcome.Success -> {
            if (data.mediaType == MOVIE_MEDIA_TYPE) {
              getStatedMovie(sessionId, data.mediaId)
            } else {
              getStatedTv(sessionId, data.mediaId)
            }
            _postModelState.value = Event(
              PostModelState(
                isSuccess = true,
                isDelete = !data.watchlist,
                isFavorite = false
              )
            )
            data.watchlist.let { _isWatchlist.value = it }
            _loadingState.value = false
          }

          is Outcome.Loading -> _loadingState.value = true
          is Outcome.Error -> {
            _postModelState.value = Event(
              PostModelState(
                isSuccess = false,
                isDelete = !data.watchlist,
                isFavorite = false
              )
            )
            _errorState.emit(outcome.message)
            _loadingState.value = false
          }
        }
      }
    }
  }

  fun postMovieRate(sessionId: String, rating: Float, movieId: Int) {
    viewModelScope.launch {
      postMethodUseCase.postMovieRate(sessionId, rating, movieId).collect { outcome ->
        when (outcome) {
          is Outcome.Success -> {
            _rateState.value = Event(true)
            _loadingState.value = false
          }

          is Outcome.Loading -> _loadingState.value = true
          is Outcome.Error -> {
            _loadingState.value = false
            _rateState.value = Event(false)
            _errorState.emit(outcome.message)
          }
        }
      }
    }
  }

  fun postTvRate(sessionId: String, rating: Float, tvId: Int) {
    viewModelScope.launch {
      postMethodUseCase.postTvRate(sessionId, rating, tvId).collect { outcome ->
        when (outcome) {
          is Outcome.Success -> {
            _rateState.value = Event(true)
            _loadingState.value = false
          }

          is Outcome.Loading -> _loadingState.value = true
          is Outcome.Error -> {
            _loadingState.value = false
            _rateState.value = Event(false)
            _errorState.emit(outcome.message)
          }
        }
      }
    }
  }
  // endregion POST FAVORITE, WATCHLIST, RATE
}
