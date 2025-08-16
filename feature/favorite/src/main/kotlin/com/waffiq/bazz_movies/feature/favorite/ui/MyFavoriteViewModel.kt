package com.waffiq.bazz_movies.feature.favorite.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.domain.FavoriteModel
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.UserModel
import com.waffiq.bazz_movies.core.domain.WatchlistModel
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers.SnackBarUserLoginData
import com.waffiq.bazz_movies.core.movie.domain.usecase.mediastate.GetMovieStateUseCase
import com.waffiq.bazz_movies.core.movie.domain.usecase.mediastate.GetTvStateUseCase
import com.waffiq.bazz_movies.core.movie.domain.usecase.postmethod.PostMethodUseCase
import com.waffiq.bazz_movies.feature.favorite.domain.usecase.GetFavoriteMovieUseCase
import com.waffiq.bazz_movies.feature.favorite.domain.usecase.GetFavoriteTvUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyFavoriteViewModel @Inject constructor(
  private val getFavoriteMovieUseCase: GetFavoriteMovieUseCase,
  private val getFavoriteTvUseCase: GetFavoriteTvUseCase,
  private val postMethodUseCase: PostMethodUseCase,
  private val getStatedMovieUseCase: GetMovieStateUseCase,
  private val getStatedTvUseCase: GetTvStateUseCase,
) : ViewModel() {

  private val _snackBarAlready = MutableLiveData<Event<String>>()
  val snackBarAlready: LiveData<Event<String>> = _snackBarAlready

  private val _snackBarAdded = MutableLiveData<Event<SnackBarUserLoginData>>()
  val snackBarAdded: LiveData<Event<SnackBarUserLoginData>> = _snackBarAdded

  // region NETWORK
  fun favoriteMovies(sesId: String): Flow<PagingData<MediaItem>> =
    getFavoriteMovieUseCase.getFavoriteMovies(sesId).cachedIn(viewModelScope)

  fun favoriteTvSeries(sesId: String): Flow<PagingData<MediaItem>> =
    getFavoriteTvUseCase.getFavoriteTv(sesId).cachedIn(viewModelScope)

  fun postFavorite(sesId: String, userId: Int, data: FavoriteModel, title: String) {
    viewModelScope.launch {
      postMethodUseCase.postFavorite(sesId, data, userId).collect { outcome ->
        when (outcome) {
          is Outcome.Success ->
            _snackBarAdded.value =
              Event(SnackBarUserLoginData(true, title, data, null))

          is Outcome.Error ->
            _snackBarAdded.value =
              Event(SnackBarUserLoginData(false, outcome.message, null, null))

          is Outcome.Loading -> {
            /* do nothing */
          }
        }
      }
    }
  }

  fun postWatchlist(sesId: String, userId: Int, data: WatchlistModel, title: String) {
    viewModelScope.launch {
      postMethodUseCase.postWatchlist(sesId, data, userId).collect { outcome ->
        when (outcome) {
          is Outcome.Success ->
            _snackBarAdded.value =
              Event(SnackBarUserLoginData(true, title, null, data))

          is Outcome.Error ->
            _snackBarAdded.value =
              Event(SnackBarUserLoginData(false, outcome.message, null, null))

          is Outcome.Loading -> {
            /* do nothing */
          }
        }
      }
    }
  }

  fun checkMovieStatedThenPostWatchlist(
    user: UserModel,
    id: Int,
    title: String,
  ) {
    viewModelScope.launch {
      getStatedMovieUseCase.getMovieState(user.token, id).collect { outcome ->
        when (outcome) {
          is Outcome.Success -> {
            if (outcome.data.watchlist) {
              _snackBarAlready.value = Event(title)
            } else {
              postWatchlist(
                user.token,
                user.userId,
                WatchlistModel(MOVIE_MEDIA_TYPE, id, true),
                title
              )
            }
          }

          is Outcome.Loading -> {
            /* do nothing */
          }

          is Outcome.Error ->
            _snackBarAdded.value =
              Event(SnackBarUserLoginData(false, outcome.message, null, null))
        }
      }
    }
  }

  fun checkTvStatedThenPostWatchlist(
    user: UserModel,
    id: Int,
    title: String,
  ) {
    viewModelScope.launch {
      getStatedTvUseCase.getTvState(user.token, id).collect { outcome ->
        when (outcome) {
          is Outcome.Success -> {
            if (outcome.data.watchlist) {
              _snackBarAlready.value = Event(title)
            } else {
              postWatchlist(
                user.token,
                user.userId,
                WatchlistModel(TV_MEDIA_TYPE, id, true),
                title
              )
            }
          }

          is Outcome.Loading -> {
            /* do nothing */
          }

          is Outcome.Error ->
            _snackBarAdded.value =
              Event(SnackBarUserLoginData(false, outcome.message, null, null))
        }
      }
    }
  }
  // endregion NETWORK
}
