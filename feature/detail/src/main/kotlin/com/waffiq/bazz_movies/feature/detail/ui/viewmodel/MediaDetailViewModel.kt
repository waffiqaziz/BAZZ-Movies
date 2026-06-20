package com.waffiq.bazz_movies.feature.detail.ui.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.data.domain.usecase.composite.MediaStateUseCase
import com.waffiq.bazz_movies.core.data.domain.usecase.composite.PostActionUseCase
import com.waffiq.bazz_movies.core.data.domain.usecase.listmovie.GetListMoviesUseCase
import com.waffiq.bazz_movies.core.data.domain.usecase.listtv.GetListTvUseCase
import com.waffiq.bazz_movies.core.database.domain.usecase.FavoriteLocalDatabaseUseCase
import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.database.utils.FavoriteMapper.toFavorite
import com.waffiq.bazz_movies.core.designsystem.R.string.rating_added_successfully
import com.waffiq.bazz_movies.core.models.Favorite
import com.waffiq.bazz_movies.core.models.FavoriteParams
import com.waffiq.bazz_movies.core.models.MediaData
import com.waffiq.bazz_movies.core.models.MediaItem
import com.waffiq.bazz_movies.core.models.Outcome
import com.waffiq.bazz_movies.core.models.Rated
import com.waffiq.bazz_movies.core.models.WatchlistParams
import com.waffiq.bazz_movies.feature.detail.domain.model.UpdateMediaStateResult
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProvidersItem
import com.waffiq.bazz_movies.feature.detail.domain.usecase.composite.GetMediaDetailUseCase
import com.waffiq.bazz_movies.feature.detail.domain.usecase.composite.PostRateUseCase
import com.waffiq.bazz_movies.feature.detail.domain.usecase.composite.RefreshMediaMetadataUseCase
import com.waffiq.bazz_movies.feature.detail.domain.usecase.getOmdbDetail.GetOMDbDetailUseCase
import com.waffiq.bazz_movies.feature.detail.ui.state.MediaDetailUiState
import com.waffiq.bazz_movies.feature.detail.ui.state.WatchProvidersUiState
import com.waffiq.bazz_movies.feature.detail.utils.mappers.BasicMediaDetailMapper.refreshWith
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
  private val localDatabaseUseCase: FavoriteLocalDatabaseUseCase,
  private val postRateUseCase: PostRateUseCase,
  private val postActionUseCase: PostActionUseCase,
  private val getOMDbDetailUseCase: GetOMDbDetailUseCase,
  private val mediaStateUseCase: MediaStateUseCase,
  private val getMediaDetailUseCase: GetMediaDetailUseCase,
  private val refreshMediaMetadataUseCase: RefreshMediaMetadataUseCase,
) : ViewModel() {

  private val _uiState = MutableStateFlow(MediaDetailUiState())
  val uiState: StateFlow<MediaDetailUiState> = _uiState.asStateFlow()

  private val _errorEvent = MutableSharedFlow<String>(extraBufferCapacity = 1)
  val errorEvent: SharedFlow<String> = _errorEvent.asSharedFlow()

  private val _toastEvent = MutableSharedFlow<Int>(extraBufferCapacity = 1)
  val toastEvent: SharedFlow<Int> = _toastEvent.asSharedFlow()

  private val _recommendations = MutableStateFlow<PagingData<MediaItem>>(PagingData.empty())
  val recommendations: StateFlow<PagingData<MediaItem>> = _recommendations.asStateFlow()

  private val currentState
    get() = _uiState.value

  private val isFavorite
    get() = currentState.isFavorite

  private val isWatchlist
    get() = currentState.isWatchlist

  // endregion OBSERVABLES

  // region MOVIE
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
      onSuccess = {
        copy(
          itemState = it,
          isFavorite = it.favorite,
          isWatchlist = it.watchlist,
        )
      },
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
      onSuccess = {
        copy(
          itemState = it,
          isFavorite = it.favorite,
          isWatchlist = it.watchlist,
        )
      },
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
  fun handleBtnFavorite(data: MediaItem) {
    when {
      !isFavorite && !isWatchlist -> {
        insertToDB(data.toFavorite(isWatchlist = false, isFavorite = true))
      }

      // item is favorited but not in watchlist, then delete
      isFavorite && !isWatchlist -> {
        deleteFromDB(data.id, data.mediaType, deleteFavorite = true)
      }

      else -> {
        updateDB(data.toFavorite(isFavorite = !isFavorite, isWatchlist = isWatchlist))
      }
    }
  }

  fun handleBtnWatchlist(data: MediaItem) {
    when {
      !isFavorite && !isWatchlist -> {
        insertToDB(data.toFavorite(isWatchlist = true, isFavorite = false))
      }

      // item watchlisted but not in favorite, then delete
      isWatchlist && !isFavorite -> {
        deleteFromDB(data.id, data.mediaType, deleteFavorite = false)
      }

      else -> {
        updateDB(data.toFavorite(isFavorite = isFavorite, isWatchlist = !isWatchlist))
      }
    }
  }

  fun getByMedia(mediaId: Int, mediaType: String) {
    viewModelScope.launch {
      val favorite = localDatabaseUseCase.getByMedia(mediaId, mediaType)
      if (favorite != null) {
        updateState { copy(isWatchlist = favorite.isWatchlist, isFavorite = favorite.isFavorite) }

        _uiState.value.detail.let { detail ->
          if (favorite.isStale() && detail != null) {
            localDatabaseUseCase.update(favorite.refreshWith(detail))
          }
        }
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

  private fun updateDB(fav: Favorite) {
    // determine if its favorite or watchlist changes
    // its checks before and after, if different its favorite, otherwise its watchlist
    val stateChanges = isFavorite == !fav.isFavorite

    // update delete action if between isFavorite and isWatchlist is different
    val isDelete = fav.isFavorite != fav.isWatchlist
    executeDbAction(
      action = { localDatabaseUseCase.update(fav) },
      onSuccess = {
        updateState { copy(isWatchlist = fav.isWatchlist, isFavorite = fav.isFavorite) }
        emitPostState(isDelete = isDelete, isFavorite = stateChanges)
      },
    )
  }

  private fun deleteFromDB(
    mediaId: Int,
    mediaType: String,
    deleteFavorite: Boolean,
  ) {
    executeDbAction(
      action = { localDatabaseUseCase.deleteFromDB(mediaId, mediaType) },
      onSuccess = {
        updateState { copy(isFavorite = false, isWatchlist = false) }
        emitPostState(isDelete = true, isFavorite = deleteFavorite)
      },
    )
  }
  // endregion DB FUNCTION

  // region POST FAVORITE, WATCHLIST, RATE
  fun postFavorite(mediaId: Int, mediaType: String) {
    val data = FavoriteParams(
      mediaType,
      mediaId,
      !isFavorite,
    )

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

  fun postWatchlist(mediaId: Int, mediaType: String) {
    val data = WatchlistParams(
      mediaType,
      mediaId,
      !isWatchlist,
    )

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

  fun refreshMedia(mediaId: Int, mediaType: String) {
    viewModelScope.launch {
      refreshMediaMetadataUseCase.refreshMedia(mediaId, mediaType)
    }
  }

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
  private fun executeDbAction(action: suspend () -> DbResult<*>, onSuccess: () -> Unit) {
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
