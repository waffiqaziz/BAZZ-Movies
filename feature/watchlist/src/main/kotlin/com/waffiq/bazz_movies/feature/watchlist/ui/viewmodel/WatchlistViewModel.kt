package com.waffiq.bazz_movies.feature.watchlist.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.domain.FavoriteParams
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.domain.WatchlistParams
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers.FavWatchlistHelper.launchAndHandleOutcome
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers.SnackBarUserLoginData
import com.waffiq.bazz_movies.core.movie.domain.usecase.composite.PostActionUseCase
import com.waffiq.bazz_movies.feature.watchlist.domain.model.FavoriteActionResult
import com.waffiq.bazz_movies.feature.watchlist.domain.usecase.composite.CheckAndAddToFavoriteUseCase
import com.waffiq.bazz_movies.feature.watchlist.domain.usecase.watchlistmovie.GetWatchlistMovieUseCase
import com.waffiq.bazz_movies.feature.watchlist.domain.usecase.watchlisttv.GetWatchlistTvUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(
  private val getWatchlistMovieUseCase: GetWatchlistMovieUseCase,
  private val getWatchlistTvUseCase: GetWatchlistTvUseCase,
  private val postActionUseCase: PostActionUseCase,
  private val checkAndAddToFavoriteUseCase: CheckAndAddToFavoriteUseCase,
) : ViewModel() {

  private val _snackBarAlready = MutableLiveData<Event<String>>()
  val snackBarAlready: LiveData<Event<String>> = _snackBarAlready

  private val _snackBarAdded = MutableLiveData<Event<SnackBarUserLoginData>>()
  val snackBarAdded: LiveData<Event<SnackBarUserLoginData>> = _snackBarAdded

  // region NETWORK
  fun watchlistMovies(sesId: String): Flow<PagingData<MediaItem>> =
    getWatchlistMovieUseCase.getWatchlistMovies(sesId).cachedIn(viewModelScope)

  fun watchlistTvSeries(sesId: String): Flow<PagingData<MediaItem>> =
    getWatchlistTvUseCase.getWatchlistTv(sesId).cachedIn(viewModelScope)

  fun postFavorite(data: FavoriteParams, title: String) {
    launchAndHandleOutcome(
      flow = postActionUseCase.postFavoriteWithAuth(data),
      onSuccess = {
        _snackBarAdded.value = Event(SnackBarUserLoginData(true, title, data, null))
      },
      onError = { onError(it) },
    )
  }

  fun postWatchlist(data: WatchlistParams, title: String) {
    launchAndHandleOutcome(
      flow = postActionUseCase.postWatchlistWithAuth(data),
      onSuccess = {
        _snackBarAdded.value = Event(SnackBarUserLoginData(true, title, null, data))
      },
      onError = { onError(it) },
    )
  }

  fun addMovieToFavorite(id: Int, title: String) {
    launchAndHandleOutcome(
      flow = checkAndAddToFavoriteUseCase.addMovieToFavorite(id),
      onSuccess = { result ->
        when (result) {
          FavoriteActionResult.Added -> {
            _snackBarAdded.value = Event(
              SnackBarUserLoginData(true, title, FavoriteParams(MOVIE_MEDIA_TYPE, id, true), null),
            )
          }

          FavoriteActionResult.AlreadyInFavorite -> already(title)
        }
      },
      onError = { onError(it) },
    )
  }

  fun addTvToFavorite(id: Int, title: String) {
    launchAndHandleOutcome(
      flow = checkAndAddToFavoriteUseCase.addTvToFavorite(id),
      onSuccess = { result ->
        when (result) {
          FavoriteActionResult.Added -> {
            _snackBarAdded.value = Event(
              SnackBarUserLoginData(true, title, FavoriteParams(TV_MEDIA_TYPE, id, true), null),
            )
          }

          FavoriteActionResult.AlreadyInFavorite -> already(title)
        }
      },
      onError = { onError(it) },
    )
  }
  // endregion NETWORK

  private fun already(title: String) {
    _snackBarAlready.value = Event(title)
  }

  private fun onError(message: String) {
    _snackBarAdded.value = Event(SnackBarUserLoginData(false, message, null, null))
  }
}
