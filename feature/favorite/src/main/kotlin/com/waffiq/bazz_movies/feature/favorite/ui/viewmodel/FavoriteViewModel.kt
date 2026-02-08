package com.waffiq.bazz_movies.feature.favorite.ui.viewmodel

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
import com.waffiq.bazz_movies.feature.favorite.domain.model.WatchlistActionResult
import com.waffiq.bazz_movies.feature.favorite.domain.usecase.composite.CheckAndAddToWatchlistUseCase
import com.waffiq.bazz_movies.feature.favorite.domain.usecase.favoritemovie.GetFavoriteMovieUseCase
import com.waffiq.bazz_movies.feature.favorite.domain.usecase.favoritetv.GetFavoriteTvUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
  private val getFavoriteMovieUseCase: GetFavoriteMovieUseCase,
  private val getFavoriteTvUseCase: GetFavoriteTvUseCase,
  private val postActionUseCase: PostActionUseCase,
  private val checkAndAddToWatchlistUseCase: CheckAndAddToWatchlistUseCase,
) : ViewModel() {

  private val _snackBarAlready = MutableLiveData<Event<String>>()
  val snackBarAlready: LiveData<Event<String>> = _snackBarAlready

  private val _snackBarAdded = MutableLiveData<Event<SnackBarUserLoginData>>()
  val snackBarAdded: LiveData<Event<SnackBarUserLoginData>> = _snackBarAdded

  fun favoriteMovies(sesId: String): Flow<PagingData<MediaItem>> =
    getFavoriteMovieUseCase.getFavoriteMovies(sesId).cachedIn(viewModelScope)

  fun favoriteTvSeries(sesId: String): Flow<PagingData<MediaItem>> =
    getFavoriteTvUseCase.getFavoriteTv(sesId).cachedIn(viewModelScope)

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

  fun addMovieToWatchlist(id: Int, title: String) {
    launchAndHandleOutcome(
      flow = checkAndAddToWatchlistUseCase.addMovieToWatchlist(id),
      onSuccess = { result ->
        when (result) {
          WatchlistActionResult.Added -> {
            _snackBarAdded.value = Event(
              SnackBarUserLoginData(true, title, null, WatchlistParams(MOVIE_MEDIA_TYPE, id, true)),
            )
          }

          WatchlistActionResult.AlreadyInWatchlist -> already(title)
        }
      },
      onError = { onError(it) },
    )
  }

  fun addTvToWatchlist(id: Int, title: String) {
    launchAndHandleOutcome(
      flow = checkAndAddToWatchlistUseCase.addTvToWatchlist(id),
      onSuccess = { result ->
        when (result) {
          WatchlistActionResult.Added -> {
            _snackBarAdded.value = Event(
              SnackBarUserLoginData(true, title, null, WatchlistParams(TV_MEDIA_TYPE, id, true)),
            )
          }

          WatchlistActionResult.AlreadyInWatchlist -> already(title)
        }
      },
      onError = { onError(it) },
    )
  }

  private fun already(title: String) {
    _snackBarAlready.value = Event(title)
  }

  private fun onError(message: String) {
    _snackBarAdded.value = Event(SnackBarUserLoginData(false, message, null, null))
  }
}
