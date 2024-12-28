package com.waffiq.bazz_movies.feature.watchlist.ui

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
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.ResultItem
import com.waffiq.bazz_movies.core.domain.UserModel
import com.waffiq.bazz_movies.core.domain.WatchlistModel
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers.SnackBarUserLoginData
import com.waffiq.bazz_movies.core.movie.domain.usecase.getstated.GetStatedMovieUseCase
import com.waffiq.bazz_movies.core.movie.domain.usecase.getstated.GetStatedTvUseCase
import com.waffiq.bazz_movies.core.movie.domain.usecase.postmethod.PostMethodUseCase
import com.waffiq.bazz_movies.feature.watchlist.domain.usecase.GetWatchlistMovieUseCase
import com.waffiq.bazz_movies.feature.watchlist.domain.usecase.GetWatchlistTvUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyWatchlistViewModel @Inject constructor(
  private val getWatchlistMovieUseCase: GetWatchlistMovieUseCase,
  private val getWatchlistTvUseCase: GetWatchlistTvUseCase,
  private val postMethodUseCase: PostMethodUseCase,
  private val getStatedMovieUseCase: GetStatedMovieUseCase,
  private val getStatedTvUseCase: GetStatedTvUseCase
) : ViewModel() {

  private val _snackBarAlready = MutableLiveData<Event<String>>()
  val snackBarAlready: LiveData<Event<String>> = _snackBarAlready

  private val _snackBarAdded = MutableLiveData<Event<SnackBarUserLoginData>>()
  val snackBarAdded: LiveData<Event<SnackBarUserLoginData>> = _snackBarAdded

  // region NETWORK
  fun watchlistMovies(sesId: String): Flow<PagingData<ResultItem>> =
    getWatchlistMovieUseCase.getPagingWatchlistMovies(sesId).cachedIn(viewModelScope)

  fun watchlistTvSeries(sesId: String): Flow<PagingData<ResultItem>> =
    getWatchlistTvUseCase.getPagingWatchlistTv(sesId).cachedIn(viewModelScope)

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

          is Outcome.Loading -> {}
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

          is Outcome.Loading -> {}
        }
      }
    }
  }

  fun checkTvStatedThenPostFavorite(
    user: UserModel,
    id: Int,
    title: String
  ) {
    viewModelScope.launch {
      getStatedTvUseCase.getStatedTv(user.token, id).collect { outcome ->
        when (outcome) {
          is Outcome.Success -> {
            if (outcome.data.favorite) {
              _snackBarAlready.value = Event(title)
            } else {
              postFavorite(
                user.token,
                user.userId,
                FavoriteModel(TV_MEDIA_TYPE, id, true),
                title
              )
            }
          }

          is Outcome.Loading -> {}
          is Outcome.Error ->
            _snackBarAdded.value =
              Event(SnackBarUserLoginData(false, outcome.message, null, null))
        }
      }
    }
  }

  fun checkMovieStatedThenPostFavorite(
    user: UserModel,
    id: Int,
    title: String
  ) {
    viewModelScope.launch {
      getStatedMovieUseCase.getStatedMovie(user.token, id).collect { outcome ->
        when (outcome) {
          is Outcome.Success -> {
            if (outcome.data.favorite) {
              _snackBarAlready.value = Event(title)
            } else {
              postFavorite(
                user.token,
                user.userId,
                FavoriteModel(MOVIE_MEDIA_TYPE, id, true),
                title
              )
            }
          }

          is Outcome.Loading -> {}
          is Outcome.Error ->
            _snackBarAdded.value =
              Event(SnackBarUserLoginData(false, outcome.message, null, null))
        }
      }
    }
  }
  // endregion NETWORK
}
