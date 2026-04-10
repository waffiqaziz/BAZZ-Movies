package com.waffiq.bazz_movies.feature.detail.ui.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.database.domain.usecase.localdatabase.LocalDatabaseUseCase
import com.waffiq.bazz_movies.core.database.utils.DatabaseMapper.favFalseWatchlistTrue
import com.waffiq.bazz_movies.core.database.utils.DatabaseMapper.favTrueWatchlistFalse
import com.waffiq.bazz_movies.core.database.utils.DatabaseMapper.favTrueWatchlistTrue
import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.designsystem.R.string.rating_added_successfully
import com.waffiq.bazz_movies.core.domain.Favorite
import com.waffiq.bazz_movies.core.domain.FavoriteParams
import com.waffiq.bazz_movies.core.domain.MediaData
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.Rated
import com.waffiq.bazz_movies.core.domain.WatchlistParams
import com.waffiq.bazz_movies.core.movie.domain.usecase.composite.MediaStateUseCase
import com.waffiq.bazz_movies.core.movie.domain.usecase.composite.PostActionUseCase
import com.waffiq.bazz_movies.core.movie.domain.usecase.listmovie.GetListMoviesUseCase
import com.waffiq.bazz_movies.core.movie.domain.usecase.listtv.GetListTvUseCase
import com.waffiq.bazz_movies.feature.detail.domain.model.UpdateMediaStateResult
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProvidersItem
import com.waffiq.bazz_movies.feature.detail.domain.usecase.composite.GetMediaDetailUseCase
import com.waffiq.bazz_movies.feature.detail.domain.usecase.composite.PostRateUseCase
import com.waffiq.bazz_movies.feature.detail.domain.usecase.getOmdbDetail.GetOMDbDetailUseCase
import com.waffiq.bazz_movies.feature.detail.ui.state.MediaDetailUiState
import com.waffiq.bazz_movies.feature.detail.ui.state.WatchProvidersUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("TooManyFunctions", "LongParameterList")
@HiltViewModel
class MediaDetailViewModel @Inject constructor(
  private val getListMoviesUseCase: GetListMoviesUseCase,
  private val getListTvUseCase: GetListTvUseCase,
  private val localDatabaseUseCase: LocalDatabaseUseCase,
  private val postRateUseCase: PostRateUseCase,
  private val postActionUseCase: PostActionUseCase,
  private val getOMDbDetailUseCase: GetOMDbDetailUseCase,
  private val mediaStateUseCase: MediaStateUseCase,
  private val getMediaDetailUseCase: GetMediaDetailUseCase,
) : ViewModel() {

  private val _uiState = MutableStateFlow(MediaDetailUiState())
  val uiState: StateFlow<MediaDetailUiState> = _uiState.asStateFlow()

  private val _errorEvent = MutableSharedFlow<String>(extraBufferCapacity = 1)
  val errorEvent: SharedFlow<String> = _errorEvent.asSharedFlow()

  private val _toastEvent = MutableSharedFlow<Int>(extraBufferCapacity = 1)
  val toastEvent: SharedFlow<Int> = _toastEvent.asSharedFlow()

  private val _recommendations = MutableStateFlow<PagingData<MediaItem>>(PagingData.empty())
  val recommendations: StateFlow<PagingData<MediaItem>> = _recommendations.asStateFlow()

  // endregion OBSERVABLES

  // region MOVIE
  fun getMovieVideoLink(movieId: Int) {
    singleExecuteUseCase(
      flowProvider = { getMediaDetailUseCase.getMovieVideoLinks(movieId) },
      onSuccess = { copy(videoLink = it) },
    )
  }

  fun getMovieDetail(movieId: Int) {
    executeUseCase(
      flowProvider = { getMediaDetailUseCase.getMovieDetailWithUserRegion(movieId) },
      onSuccess = {
        updateState { copy(detail = it) }
        if (!it.imdbId.isNullOrEmpty()) {
          getOMDbDetails(it.imdbId)
        }
      },
    )
  }

  fun getMovieCredits(movieId: Int) {
    singleExecuteUseCase(
      flowProvider = { getMediaDetailUseCase.getMovieCredits(movieId) },
      onSuccess = { copy(credits = it) },
    )
  }

  fun getMovieRecommendation(movieId: Int) {
    viewModelScope.launch {
      getListMoviesUseCase.getMovieRecommendation(movieId).collect {
        _recommendations.value = it
      }
    }
  }

  fun getMovieState(id: Int) {
    singleExecuteUseCase(
      flowProvider = { mediaStateUseCase.getMovieStateWithUser(id) },
      onSuccess = { copy(itemState = it) },
    )
  }

  fun getMovieWatchProviders(movieId: Int) {
    viewModelScope.launch {
      collectWatchProviders(
        getMediaDetailUseCase.getMovieWatchProvidersWithUserRegion(movieId),
      )
    }
  }
  // endregion MOVIE

  // region TV-SERIES
  fun getTvTrailerLink(tvId: Int) {
    singleExecuteUseCase(
      flowProvider = { getMediaDetailUseCase.getTvTrailerLink(tvId) },
      onSuccess = { copy(videoLink = it) },
    )
  }

  fun getTvDetail(tvId: Int) {
    singleExecuteUseCase(
      flowProvider = { getMediaDetailUseCase.getTvDetailWithUserRegion(tvId) },
      onSuccess = { copy(detail = it) },
    )
  }

  fun getTvCredits(tvId: Int) {
    singleExecuteUseCase(
      flowProvider = { getMediaDetailUseCase.getTvCredits(tvId) },
      onSuccess = { copy(credits = it) },
    )
  }

  fun getTvRecommendation(tvId: Int) {
    viewModelScope.launch {
      getListTvUseCase.getTvRecommendation(tvId).collect {
        _recommendations.value = it
      }
    }
  }

  fun getTvState(id: Int) {
    singleExecuteUseCase(
      flowProvider = { mediaStateUseCase.getTvStateWithUser(id) },
      onSuccess = { copy(itemState = it) },
    )
  }

  fun getTvWatchProviders(tvId: Int) {
    viewModelScope.launch {
      collectWatchProviders(
        getMediaDetailUseCase.getTvWatchProvidersWithUserRegion(tvId),
      )
    }
  }

  fun getTvAllScore(tvId: Int) {
    executeUseCase(
      flowProvider = { getOMDbDetailUseCase.getTvAllScore(tvId) },
      onSuccess = { updateState { copy(omdbDetails = it) } },
      onLoading = { updateState { copy(isLoading = true) } },
    )
  }
  // endregion TV-SERIES

  private fun collectWatchProviders(flow: Flow<Outcome<WatchProvidersItem>>) {
    viewModelScope.launch {
      flow.collect { outcome ->
        when (outcome) {
          is Outcome.Success -> {
            updateState {
              copy(
                watchProviders =
                  WatchProvidersUiState.Success(
                    ads = outcome.data.ads.orEmpty(),
                    buy = outcome.data.buy.orEmpty(),
                    flatrate = outcome.data.flatrate.orEmpty(),
                    free = outcome.data.free.orEmpty(),
                    rent = outcome.data.rent.orEmpty(),
                  ),
              )
            }
          }

          is Outcome.Loading -> {
            updateState {
              copy(watchProviders = WatchProvidersUiState.Loading)
            }
          }

          is Outcome.Error -> {
            updateState {
              copy(watchProviders = WatchProvidersUiState.Error(outcome.message))
            }
          }
        }
      }
    }
  }

  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  fun getOMDbDetails(imdbId: String) {
    executeUseCase(
      flowProvider = { getOMDbDetailUseCase.getOMDbDetails(imdbId) },
      onSuccess = { updateState { copy(omdbDetails = it) } },
      onFinallySuccess = { updateState { copy(isLoading = false) } },
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
        is DbResult.Success -> if (result.data) updateState { copy(isFavorite = true) }
        is DbResult.Error -> _errorEvent.emit(result.errorMessage)
      }
    }
  }

  fun isWatchlistDB(id: Int, mediaType: String) {
    viewModelScope.launch {
      when (val result = localDatabaseUseCase.isWatchlistDB(id, mediaType)) {
        is DbResult.Success -> if (result.data) updateState { copy(isWatchlist = true) }
        is DbResult.Error -> _errorEvent.emit(result.errorMessage)
      }
    }
  }

  private fun insertToDB(fav: Favorite) {
    executeDbAction(
      action = { localDatabaseUseCase.insertToDB(fav) },
      onSuccess = {
        updateState {
          copy(
            isFavorite = fav.isFavorite,
            isWatchlist = !fav.isFavorite,
            mediaStateResult = UpdateMediaStateResult(
              isSuccess = true,
              isDelete = false,
              isFavorite = fav.isFavorite,
            ),
          )
        }
      },
    )
  }

  private fun updateToFavoriteDB(fav: Favorite) {
    executeDbAction(
      action = { localDatabaseUseCase.updateFavoriteItemDB(false, fav) },
      onSuccess = {
        updateState { copy(isFavorite = true) }
        emitPostState(isDelete = false, isFavorite = true)
      },
    )
  }

  private fun updateToRemoveFromFavoriteDB(fav: Favorite) {
    executeDbAction(
      action = { localDatabaseUseCase.updateFavoriteItemDB(true, fav) },
      onSuccess = {
        updateState { copy(isFavorite = false) }
        emitPostState(isDelete = true, isFavorite = true)
      },
    )
  }

  private fun updateToWatchlistDB(fav: Favorite) {
    executeDbAction(
      action = { localDatabaseUseCase.updateWatchlistItemDB(false, fav) },
      onSuccess = {
        updateState { copy(isWatchlist = true) }
        emitPostState(isDelete = false, isFavorite = false)
      },
    )
  }

  private fun updateToRemoveFromWatchlistDB(fav: Favorite) {
    executeDbAction(
      action = { localDatabaseUseCase.updateWatchlistItemDB(true, fav) },
      onSuccess = {
        updateState { copy(isWatchlist = false) }
        emitPostState(isDelete = true, isFavorite = false)
      },
    )
  }

  private fun delFromFavoriteDB(fav: Favorite) {
    executeDbAction(
      action = { localDatabaseUseCase.deleteFromDB(fav) },
      onSuccess = {
        updateState { copy(isFavorite = false, isWatchlist = false) }
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

      updateState = { value: Boolean ->
        updateState { copy(isFavorite = value) }
      },
    )
  }

  fun postWatchlist(data: WatchlistParams) {
    postItem(
      data = data,
      isFavorite = false,
      isChecked = data.watchlist,
      postAction = postActionUseCase::postWatchlistWithAuth,
      updateState = { value: Boolean ->
        updateState { copy(isWatchlist = value) }
      },
    )
  }

  fun postMovieRate(rating: Float, movieId: Int) {
    executeUseCase(
      flowProvider = { postRateUseCase.postMovieRate(rating, movieId) },
      onSuccess = {
        updateState {
          copy(
            isLoading = false,
            itemState = itemState?.copy(rated = Rated.Value(rating.toDouble())),
          )
        }
        _toastEvent.tryEmit(rating_added_successfully)
      },
      onLoading = { updateState { copy(isLoading = true) } },
      onFinallySuccess = { updateState { copy(isLoading = false) } },
      onFinallyError = { updateState { copy(isLoading = false) } },
    )
  }

  fun postTvRate(rating: Float, tvId: Int) {
    executeUseCase(
      flowProvider = { postRateUseCase.postTvRate(rating, tvId) },
      onSuccess = {
        updateState {
          copy(
            isLoading = false,
            itemState = itemState?.copy(rated = Rated.Value(rating.toDouble())),
          )
        }
        _toastEvent.tryEmit(rating_added_successfully)
      },
      onLoading = { updateState { copy(isLoading = true) } },
      onFinallySuccess = { updateState { copy(isLoading = false) } },
      onFinallyError = { updateState { copy(isLoading = false) } },
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
    updateState {
      copy(
        mediaStateResult =
          UpdateMediaStateResult(
            isSuccess = isSuccess,
            isDelete = isDelete,
            isFavorite = isFavorite,
          ),
      )
    }
  }

  /**
   * Helper to database action
   */
  private fun executeDbAction(action: suspend () -> DbResult<Int>, onSuccess: () -> Unit) {
    viewModelScope.launch {
      when (val result = action()) {
        is DbResult.Error -> _errorEvent.emit(result.errorMessage)
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
      flowProvider().collectLatest { outcome ->
        when (outcome) {
          is Outcome.Loading -> onLoading()

          is Outcome.Success -> {
            onSuccess(outcome.data)
            onFinallySuccess()
          }

          is Outcome.Error -> {
            updateState { copy(isLoading = false) }
            _errorEvent.emit(outcome.message)
            onFinallyError()
          }
        }
      }
    }
  }

  fun <T> singleExecuteUseCase(
    flowProvider: () -> Flow<Outcome<T>>,
    onSuccess: MediaDetailUiState.(T) -> MediaDetailUiState,
  ) {
    executeUseCase(
      flowProvider = flowProvider,
      onSuccess = { updateState { onSuccess(it) } },
    )
  }

  private fun updateState(block: MediaDetailUiState.() -> MediaDetailUiState) =
    _uiState.update { it.block() }

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
        updateState { copy(isLoading = false) }
      },
      onLoading = { updateState { copy(isLoading = true) } },
      onFinallyError = {
        updateState { copy(isLoading = false) }
        emitPostState(false, !isChecked, isFavorite)
      },
    )
  }

  fun consumeMediaStateResult() {
    updateState { copy(mediaStateResult = null) }
  }
}
