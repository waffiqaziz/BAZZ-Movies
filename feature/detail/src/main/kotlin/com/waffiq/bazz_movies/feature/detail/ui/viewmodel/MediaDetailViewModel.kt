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
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.WatchlistModel
import com.waffiq.bazz_movies.core.movie.domain.usecase.getstated.GetMovieStateUseCase
import com.waffiq.bazz_movies.core.movie.domain.usecase.getstated.GetTvStateUseCase
import com.waffiq.bazz_movies.core.movie.domain.usecase.postmethod.PostMethodUseCase
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCredits
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.PostModelState
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvExternalIds
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProvidersItem
import com.waffiq.bazz_movies.feature.detail.domain.usecase.getMovieDetail.GetMovieDetailUseCase
import com.waffiq.bazz_movies.feature.detail.domain.usecase.getOmdbDetail.GetOMDbDetailUseCase
import com.waffiq.bazz_movies.feature.detail.domain.usecase.getTvDetail.GetTvDetailUseCase
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
class MediaDetailViewModel @Inject constructor(
  private val getMovieDetailUseCase: GetMovieDetailUseCase,
  private val getTvDetailUseCase: GetTvDetailUseCase,
  private val localDatabaseUseCase: LocalDatabaseUseCase,
  private val postMethodUseCase: PostMethodUseCase,
  private val getOMDbDetailUseCase: GetOMDbDetailUseCase,
  private val getMovieStateUseCase: GetMovieStateUseCase,
  private val getTvStateUseCase: GetTvStateUseCase,
) : ViewModel() {

  // region OBSERVABLES
  private val _isFavorite = MutableLiveData<Boolean>()
  val isFavorite: LiveData<Boolean> = _isFavorite

  private val _isWatchlist = MutableLiveData<Boolean>()
  val isWatchlist: LiveData<Boolean> = _isWatchlist

  private val _itemState = MutableLiveData<MediaState>()
  val itemState: LiveData<MediaState> get() = _itemState

  private val _mediaCredits = MutableLiveData<MediaCredits>()
  val mediaCredits: LiveData<MediaCredits> get() = _mediaCredits

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

  private val _detailMedia = MutableLiveData<MediaDetail>()
  val detailMedia: LiveData<MediaDetail> get() = _detailMedia

  private val _tvExternalID = MutableLiveData<TvExternalIds>()
  val tvExternalID: LiveData<TvExternalIds> get() = _tvExternalID

  private val _recommendation = MutableLiveData<PagingData<MediaItem>>()
  val recommendation: LiveData<PagingData<MediaItem>> get() = _recommendation

  private val _watchProvidersUiState = MutableLiveData<WatchProvidersUiState>()
  val watchProvidersUiState: LiveData<WatchProvidersUiState> = _watchProvidersUiState
  // endregion OBSERVABLES

  // region MOVIE
  fun getMovieVideoLink(movieId: Int) {
    viewModelScope.launch {
      getMovieDetailUseCase.getMovieVideoLinks(movieId).collectLatest { outcome ->
        when (outcome) {
          is Outcome.Success -> outcome.data.let { _linkVideo.value = it }
          is Outcome.Loading -> {
            /* do nothing */
          }

          is Outcome.Error -> {
            _loadingState.value = false
            _errorState.emit(outcome.message)
          }
        }
      }
    }
  }

  fun getMovieDetail(id: Int, userRegion: String) {
    viewModelScope.launch {
      getMovieDetailUseCase.getMovieDetail(id, userRegion).collectLatest { outcome ->
        when (outcome) {
          is Outcome.Success -> outcome.data.let { _detailMedia.value = it }
          is Outcome.Loading -> {
            /* do nothing */
          }

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
      getMovieDetailUseCase.getMovieCredits(movieId).collectLatest { outcome ->
        when (outcome) {
          is Outcome.Success -> outcome.data.let { _mediaCredits.value = it }
          is Outcome.Loading -> {
            /* do nothing */
          }

          is Outcome.Error -> {
            _loadingState.value = false
            _errorState.emit(outcome.message)
          }
        }
      }
    }
  }

  fun getMovieRecommendation(movieId: Int) {
    viewModelScope.launch {
      getMovieDetailUseCase.getMovieRecommendationPagingData(movieId).collect {
        _recommendation.value = it
      }
    }
  }

  fun getMovieState(sessionId: String, id: Int) {
    viewModelScope.launch {
      getMovieStateUseCase.getMovieState(sessionId, id).collectLatest { outcome ->
        when (outcome) {
          is Outcome.Success -> outcome.data.let { _itemState.value = it }
          is Outcome.Loading -> {
            /* do nothing */
          }

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
      collectWatchProviders(getMovieDetailUseCase.getMovieWatchProviders(countryCode, movieId))
    }
  }
  // endregion MOVIE

  // region TV-SERIES
  fun getTvTrailerLink(tvId: Int) {
    viewModelScope.launch {
      getTvDetailUseCase.getTvTrailerLink(tvId).collectLatest { outcome ->
        when (outcome) {
          is Outcome.Success -> outcome.data.let { _linkVideo.value = it }
          is Outcome.Loading -> {
            /* do nothing */
          }

          is Outcome.Error -> {
            _loadingState.value = false
            _errorState.emit(outcome.message)
          }
        }
      }
    }
  }

  fun getTvExternalIds(tvId: Int) {
    viewModelScope.launch {
      getTvDetailUseCase.getTvExternalIds(tvId).collectLatest { outcome ->
        when (outcome) {
          is Outcome.Success -> _tvExternalID.value = outcome.data
          is Outcome.Loading -> {
            /* do nothing */
          }

          is Outcome.Error -> _errorState.emit(outcome.message)
        }
      }
    }
  }

  fun getTvDetail(id: Int, userRegion: String) {
    viewModelScope.launch {
      getTvDetailUseCase.getTvDetail(id, userRegion).collectLatest { outcome ->
        when (outcome) {
          is Outcome.Success -> outcome.data.let { _detailMedia.value = it }
          is Outcome.Loading -> {
            /* do nothing */
          }

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
      getTvDetailUseCase.getTvCredits(tvId).collectLatest { outcome ->
        when (outcome) {
          is Outcome.Success -> outcome.data.let { _mediaCredits.value = it }
          is Outcome.Loading -> {
            /* do nothing */
          }

          is Outcome.Error -> {
            _loadingState.value = false
            _errorState.emit(outcome.message)
          }
        }
      }
    }
  }

  fun getTvRecommendation(tvId: Int) {
    viewModelScope.launch {
      getTvDetailUseCase.getTvRecommendationPagingData(tvId).collect {
        _recommendation.value = it
      }
    }
  }

  fun getTvState(sessionId: String, id: Int) {
    viewModelScope.launch {
      getTvStateUseCase.getTvState(sessionId, id).collectLatest { outcome ->
        when (outcome) {
          is Outcome.Success -> outcome.data.let { _itemState.value = it }
          is Outcome.Loading -> {
            /* do nothing */
          }

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
      collectWatchProviders(getTvDetailUseCase.getTvWatchProviders(countryCode, tvId))
    }
  }
  // endregion TV-SERIES

  private fun collectWatchProviders(flow: Flow<Outcome<WatchProvidersItem>>) {
    viewModelScope.launch {
      flow.collect { outcome ->
        _watchProvidersUiState.value = when (outcome) {
          is Outcome.Loading -> WatchProvidersUiState.Loading
          is Outcome.Success -> WatchProvidersUiState.Success(
            ads = outcome.data.ads.orEmpty(),
            buy = outcome.data.buy.orEmpty(),
            flatrate = outcome.data.flatrate.orEmpty(),
            free = outcome.data.free.orEmpty(),
            rent = outcome.data.rent.orEmpty(),
          )

          is Outcome.Error -> WatchProvidersUiState.Error(outcome.message)
        }
      }
    }
  }

  fun getOMDbDetails(imdbId: String) {
    viewModelScope.launch {
      getOMDbDetailUseCase.getOMDbDetails(imdbId).collectLatest { outcome ->
        when (outcome) {
          is Outcome.Success -> {
            outcome.data.let { _omdbResult.value = it }
            _loadingState.value = false
          }

          is Outcome.Loading -> {
            /* do nothing */
          }

          is Outcome.Error -> {
            _loadingState.value = false
            _errorState.emit(outcome.message)
          }
        }
      }
    }
  }

  // region DB FUNCTION
  fun handleBtnFavorite(favorite: Boolean, watchlist: Boolean, data: MediaItem) {
    when {
      // If in the watchlist but not a favorite, update to set as favorite.
      !favorite && watchlist -> updateToFavoriteDB(favTrueWatchlistTrue(data))

      // If neither a favorite nor in the watchlist, insert the item and set favorite to true.
      !favorite -> insertToDB(favTrueWatchlistFalse(data))

      // If in favorite and watchlist, update to remove from favorite.
      watchlist -> updateToRemoveFromFavoriteDB(favFalseWatchlistTrue(data))

      // If item is a favorite and not in watchlist, remove it from the database.
      else -> delFromFavoriteDB(favTrueWatchlistFalse(data))
    }
  }

  fun handleBtnWatchlist(favorite: Boolean, watchlist: Boolean, data: MediaItem) {
    when {
      // If marked as a favorite but not in the watchlist, update to set as watchlist.
      favorite && !watchlist -> updateToWatchlistDB(favTrueWatchlistTrue(data))

      // If it's neither a favorite nor in the watchlist, insert the item and set watchlist to true.
      !watchlist -> insertToDB(favFalseWatchlistTrue(data))

      // If in favorite and watchlist, update to remove from watchlist.
      favorite -> updateToRemoveFromWatchlistDB(favTrueWatchlistFalse(data))

      // If is a watchlist and not in favorite,  remove it from the database.
      else -> delFromFavoriteDB(favFalseWatchlistTrue(data))
    }
  }

  fun isFavoriteDB(id: Int, mediaType: String) {
    viewModelScope.launch {
      when (val result = localDatabaseUseCase.isFavoriteDB(id, mediaType)) {
        is DbResult.Success -> if (result.data) _isFavorite.value = true
        is DbResult.Error -> _errorState.emit(result.errorMessage)
      }
    }
  }

  fun isWatchlistDB(id: Int, mediaType: String) {
    viewModelScope.launch {
      when (val result = localDatabaseUseCase.isWatchlistDB(id, mediaType)) {
        is DbResult.Success -> if (result.data) _isWatchlist.value = true
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
            _postModelState.value = Event(
              PostModelState(
                isSuccess = true,
                isDelete = false,
                isFavorite = true,
              )
            )
          } else {
            _isWatchlist.value = true
            _postModelState.value = Event(
              PostModelState(
                isSuccess = true,
                isDelete = false,
                isFavorite = false,
              )
            )
          }
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
      postMethodUseCase.postFavorite(sessionId, data, userId).collectLatest { outcome ->
        when (outcome) {
          is Outcome.Success -> {
            if (data.mediaType == MOVIE_MEDIA_TYPE) {
              getMovieState(sessionId, data.mediaId)
            } else {
              getTvState(sessionId, data.mediaId)
            }
            _postModelState.value = Event(
              PostModelState(
                isSuccess = true,
                isDelete = !data.favorite,
                isFavorite = true,
              )
            )
//            _isFavorite.value = data.favorite
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
            _errorState.emit(outcome.message)
            _loadingState.value = false
          }
        }
      }
    }
  }

  fun postWatchlist(sessionId: String, data: WatchlistModel, userId: Int) {
    viewModelScope.launch {
      postMethodUseCase.postWatchlist(sessionId, data, userId).collectLatest { outcome ->
        when (outcome) {
          is Outcome.Success -> {
            if (data.mediaType == MOVIE_MEDIA_TYPE) {
              getMovieState(sessionId, data.mediaId)
            } else {
              getTvState(sessionId, data.mediaId)
            }
            _postModelState.value = Event(
              PostModelState(
                isSuccess = true,
                isDelete = !data.watchlist,
                isFavorite = false
              )
            )
            _isWatchlist.value = data.watchlist
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
      postMethodUseCase.postMovieRate(sessionId, rating, movieId).collectLatest { outcome ->
        when (outcome) {
          is Outcome.Success -> {
            _rateState.value = Event(true)
            _loadingState.value = false
          }

          is Outcome.Loading -> _loadingState.value = true
          is Outcome.Error -> {
            _loadingState.value = false
            _errorState.emit(outcome.message)
          }
        }
      }
    }
  }

  fun postTvRate(sessionId: String, rating: Float, tvId: Int) {
    viewModelScope.launch {
      postMethodUseCase.postTvRate(sessionId, rating, tvId).collectLatest { outcome ->
        when (outcome) {
          is Outcome.Success -> {
            _rateState.value = Event(true)
            _loadingState.value = false
          }

          is Outcome.Loading -> _loadingState.value = true
          is Outcome.Error -> {
            _loadingState.value = false
            _errorState.emit(outcome.message)
          }
        }
      }
    }
  }
  // endregion POST FAVORITE, WATCHLIST, RATE
}
