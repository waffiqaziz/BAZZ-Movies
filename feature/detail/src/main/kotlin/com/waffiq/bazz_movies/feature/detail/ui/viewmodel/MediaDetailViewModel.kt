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
import com.waffiq.bazz_movies.core.domain.FavoriteParams
import com.waffiq.bazz_movies.core.domain.MediaData
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.WatchlistParams
import com.waffiq.bazz_movies.core.movie.domain.usecase.composite.PostActionUseCase
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCredits
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.UpdateMediaStateResult
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProvidersItem
import com.waffiq.bazz_movies.feature.detail.domain.usecase.composite.GetMediaStateWithUserUseCase
import com.waffiq.bazz_movies.feature.detail.domain.usecase.composite.GetMovieDataWithUserRegionUseCase
import com.waffiq.bazz_movies.feature.detail.domain.usecase.composite.GetTvAllScoreUseCase
import com.waffiq.bazz_movies.feature.detail.domain.usecase.composite.GetTvDataWithUserRegionUseCase
import com.waffiq.bazz_movies.feature.detail.domain.usecase.composite.PostRateUseCase
import com.waffiq.bazz_movies.feature.detail.domain.usecase.getMovieDetail.GetMovieDetailUseCase
import com.waffiq.bazz_movies.feature.detail.domain.usecase.getOmdbDetail.GetOMDbDetailUseCase
import com.waffiq.bazz_movies.feature.detail.domain.usecase.getTvDetail.GetTvDetailUseCase
import com.waffiq.bazz_movies.feature.detail.ui.state.WatchProvidersUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("TooManyFunctions", "LongParameterList")
@HiltViewModel
class MediaDetailViewModel @Inject constructor(
  private val getMovieDetailUseCase: GetMovieDetailUseCase,
  private val getTvDetailUseCase: GetTvDetailUseCase,
  private val localDatabaseUseCase: LocalDatabaseUseCase,
  private val postRateUseCase: PostRateUseCase,
  private val postActionUseCase: PostActionUseCase,
  private val getOMDbDetailUseCase: GetOMDbDetailUseCase,
  private val getMediaStateUseCase: GetMediaStateWithUserUseCase,
  private val getMovieDetailWithUserRegionUseCase: GetMovieDataWithUserRegionUseCase,
  private val getTvDetailWithUserRegionUseCase: GetTvDataWithUserRegionUseCase,
  private val getTvAllScoreUseCase: GetTvAllScoreUseCase,
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

  private val _errorState = MutableSharedFlow<String>(
    replay = 0,
    extraBufferCapacity = 1,
    onBufferOverflow = BufferOverflow.DROP_OLDEST,
  )
  val errorState: SharedFlow<String> get() = _errorState

  private val _rateState = MutableLiveData<Event<Boolean>>()
  val rateState: LiveData<Event<Boolean>> get() = _rateState

  private val _mediaStateResult = MutableLiveData<Event<UpdateMediaStateResult>>()
  val mediaStateResult: LiveData<Event<UpdateMediaStateResult>> get() = _mediaStateResult

  private val _linkVideo = MutableLiveData<String>()
  val linkVideo: LiveData<String> = _linkVideo

  private val _detailMedia = MutableLiveData<MediaDetail>()
  val detailMedia: LiveData<MediaDetail> get() = _detailMedia

  private val _recommendation = MutableLiveData<PagingData<MediaItem>>()
  val recommendation: LiveData<PagingData<MediaItem>> get() = _recommendation

  private val _watchProvidersUiState = MutableLiveData<WatchProvidersUiState>()
  val watchProvidersUiState: LiveData<WatchProvidersUiState> = _watchProvidersUiState
  // endregion OBSERVABLES

  // region MOVIE
  fun getMovieVideoLink(movieId: Int) {
    executeUseCase(
      flowProvider = { getMovieDetailUseCase.getMovieVideoLinks(movieId) },
      onSuccess = { _linkVideo.value = it },
    )
  }

  fun getMovieDetail(movieId: Int) {
    executeUseCase(
      flowProvider = { getMovieDetailWithUserRegionUseCase.getMovieDetailWithUserRegion(movieId) },
      onSuccess = { _detailMedia.value = it },
    )
  }

  fun getMovieCredits(movieId: Int) {
    executeUseCase(
      flowProvider = { getMovieDetailUseCase.getMovieCredits(movieId) },
      onSuccess = { _mediaCredits.value = it },
    )
  }

  fun getMovieRecommendation(movieId: Int) {
    viewModelScope.launch {
      getMovieDetailUseCase.getMovieRecommendationPagingData(movieId).collect {
        _recommendation.value = it
      }
    }
  }

  fun getMovieState(id: Int) {
    executeUseCase(
      flowProvider = { getMediaStateUseCase.getMovieStateWithUser(id) },
      onSuccess = { _itemState.value = it },
    )
  }

  fun getMovieWatchProviders(movieId: Int) {
    viewModelScope.launch {
      collectWatchProviders(
        getMovieDetailWithUserRegionUseCase.getMovieWatchProvidersWithUserRegion(movieId),
      )
    }
  }
  // endregion MOVIE

  // region TV-SERIES
  fun getTvTrailerLink(tvId: Int) {
    executeUseCase(
      flowProvider = { getTvDetailUseCase.getTvTrailerLink(tvId) },
      onSuccess = { _linkVideo.value = it },
    )
  }

  fun getTvDetail(tvId: Int) {
    executeUseCase(
      flowProvider = { getTvDetailWithUserRegionUseCase.getTvDetailWithUserRegion(tvId) },
      onSuccess = { _detailMedia.value = it },
    )
  }

  fun getTvCredits(tvId: Int) {
    executeUseCase(
      flowProvider = { getTvDetailUseCase.getTvCredits(tvId) },
      onSuccess = { _mediaCredits.value = it },
    )
  }

  fun getTvRecommendation(tvId: Int) {
    viewModelScope.launch {
      getTvDetailUseCase.getTvRecommendationPagingData(tvId).collect {
        _recommendation.value = it
      }
    }
  }

  fun getTvState(id: Int) {
    executeUseCase(
      flowProvider = { getMediaStateUseCase.getTvStateWithUser(id) },
      onSuccess = { _itemState.value = it },
    )
  }

  fun getTvWatchProviders(tvId: Int) {
    viewModelScope.launch {
      collectWatchProviders(
        getTvDetailWithUserRegionUseCase.getTvWatchProvidersWithUserRegion(tvId),
      )
    }
  }

  fun getTvAllScore(tvId: Int) {
    executeUseCase(
      flowProvider = { getTvAllScoreUseCase.getTvAllScore(tvId) },
      onSuccess = { _omdbResult.value = it },
    )
  }
  // endregion TV-SERIES

  private fun collectWatchProviders(flow: Flow<Outcome<WatchProvidersItem>>) {
    viewModelScope.launch {
      flow.collect { outcome ->
        _watchProvidersUiState.value = when (outcome) {
          is Outcome.Success -> WatchProvidersUiState.Success(
            ads = outcome.data.ads.orEmpty(),
            buy = outcome.data.buy.orEmpty(),
            flatrate = outcome.data.flatrate.orEmpty(),
            free = outcome.data.free.orEmpty(),
            rent = outcome.data.rent.orEmpty(),
          )

          is Outcome.Loading -> WatchProvidersUiState.Loading

          is Outcome.Error -> WatchProvidersUiState.Error(outcome.message)
        }
      }
    }
  }

  fun getOMDbDetails(imdbId: String) {
    executeUseCase(
      flowProvider = { getOMDbDetailUseCase.getOMDbDetails(imdbId) },
      onSuccess = { _omdbResult.value = it },
      onFinallySuccess = { _loadingState.value = false },
    )
  }

  // region DB FUNCTION
  fun handleBtnFavorite(
    favorite: Boolean,
    watchlist: Boolean,
    data: MediaItem,
  ) {
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

  fun handleBtnWatchlist(
    favorite: Boolean,
    watchlist: Boolean,
    data: MediaItem,
  ) {
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
    executeDbAction(
      action = { localDatabaseUseCase.insertToDB(fav) },
      onSuccess = {
        if (fav.isFavorite) {
          _isFavorite.value = true
          emitPostState(isDelete = false, isFavorite = true)
        } else {
          _isWatchlist.value = true
          emitPostState(isDelete = false, isFavorite = false)
        }
      },
    )
  }

  private fun updateToFavoriteDB(fav: Favorite) {
    executeDbAction(
      action = { localDatabaseUseCase.updateFavoriteItemDB(false, fav) },
      onSuccess = {
        _isFavorite.value = true
        emitPostState(isDelete = false, isFavorite = true)
      },
    )
  }

  private fun updateToRemoveFromFavoriteDB(fav: Favorite) {
    executeDbAction(
      action = { localDatabaseUseCase.updateFavoriteItemDB(true, fav) },
      onSuccess = {
        _isFavorite.value = false
        emitPostState(isDelete = true, isFavorite = true)
      },
    )
  }

  private fun updateToWatchlistDB(fav: Favorite) {
    executeDbAction(
      action = { localDatabaseUseCase.updateWatchlistItemDB(false, fav) },
      onSuccess = {
        _isWatchlist.value = true
        emitPostState(isDelete = false, isFavorite = false)
      },
    )
  }

  private fun updateToRemoveFromWatchlistDB(fav: Favorite) {
    executeDbAction(
      action = { localDatabaseUseCase.updateWatchlistItemDB(true, fav) },
      onSuccess = {
        _isWatchlist.value = false
        emitPostState(isDelete = true, isFavorite = false)
      },
    )
  }

  private fun delFromFavoriteDB(fav: Favorite) {
    executeDbAction(
      action = { localDatabaseUseCase.deleteFromDB(fav) },
      onSuccess = {
        _isFavorite.value = false
        _isWatchlist.value = false
        emitPostState(isDelete = true, isFavorite = fav.isFavorite)
      },
    )
  }
  // endregion DB FUNCTION

  // region POST FAVORITE, WATCHLIST, RATE
  fun postFavorite(data: FavoriteParams) {
    postItem(
      data = data,
      isFavorite = true,
      isChecked = data.favorite,

      // function reference = (FavoriteModel) -> Flow<Outcome<...>>
      // same as { item -> postMethodWithUserUseCase.postFavorite(item) }
      // only works if the function signatures match exactly.
      postAction = postActionUseCase::postFavoriteWithAuth,

      updateState = { value: Boolean -> _isFavorite.value = value },
    )
  }

  fun postWatchlist(data: WatchlistParams) {
    postItem(
      data = data,
      isFavorite = false,
      isChecked = data.watchlist,
      postAction = postActionUseCase::postWatchlistWithAuth,
      updateState = { value: Boolean -> _isWatchlist.value = value },
    )
  }

  fun postMovieRate(rating: Float, movieId: Int) {
    executeUseCase(
      flowProvider = { postRateUseCase.postMovieRate(rating, movieId) },
      onSuccess = { _rateState.value = Event(true) },
      onFinallySuccess = { _loadingState.value = false },
      onLoading = { _loadingState.value = true },
    )
  }

  fun postTvRate(rating: Float, tvId: Int) {
    executeUseCase(
      flowProvider = { postRateUseCase.postTvRate(rating, tvId) },
      onSuccess = { _rateState.value = Event(true) },
      onFinallySuccess = { _loadingState.value = false },
      onLoading = { _loadingState.value = true },
    )
  }
  // endregion POST FAVORITE, WATCHLIST, RATE

  /**
   * Helper to emit [UpdateMediaStateResult]
   */
  private fun emitPostState(
    isSuccess: Boolean = true,
    isDelete: Boolean,
    isFavorite: Boolean,
  ) {
    _mediaStateResult.value = Event(
      UpdateMediaStateResult(
        isSuccess = isSuccess,
        isDelete = isDelete,
        isFavorite = isFavorite,
      ),
    )
  }

  /**
   * Helper to database action
   */
  private fun executeDbAction(action: suspend () -> DbResult<Int>, onSuccess: () -> Unit) {
    viewModelScope.launch {
      when (val result = action()) {
        is DbResult.Error -> _errorState.emit(result.errorMessage)
        is DbResult.Success -> onSuccess()
      }
    }
  }

  /**
   * Helper to handle fetch from API
   */
  fun <T> executeUseCase(
    flowProvider: () -> Flow<Outcome<T>>,
    onSuccess: (T) -> Unit = { /* default do nothing */ },
    onFinallySuccess: () -> Unit = { /* default do nothing */ },
    onLoading: () -> Unit = { /* default do nothing */ },
    onFinallyError: () -> Unit = { /* default do nothing */ },
  ) {
    viewModelScope.launch {
      val flow = flowProvider()
      flow.collectLatest { outcome ->
        when (outcome) {
          is Outcome.Success -> {
            onSuccess(outcome.data)
            onFinallySuccess()
          }

          is Outcome.Loading -> onLoading()

          is Outcome.Error -> {
            _loadingState.value = false
            _errorState.emit(outcome.message)
            onFinallyError()
          }
        }
      }
    }
  }

  /**
   * Helper to handle post action
   */
  private fun <T : MediaData, R> postItem(
    data: T,
    isFavorite: Boolean,
    isChecked: Boolean,
    postAction: (T) -> Flow<Outcome<R>>,
    updateState: (Boolean) -> Unit,
  ) {
    executeUseCase(
      flowProvider = { postAction(data) },
      onSuccess = { emitPostState(true, !isChecked, isFavorite) },
      onFinallySuccess = {
        if (data.mediaType == MOVIE_MEDIA_TYPE) {
          getMovieState(data.mediaId)
        } else {
          getTvState(data.mediaId)
        }
        updateState(isChecked)
        _loadingState.value = false
      },
      onLoading = { _loadingState.value = true },
      onFinallyError = {
        _loadingState.value = false
        emitPostState(false, !isChecked, isFavorite)
      },
    )
  }
}
