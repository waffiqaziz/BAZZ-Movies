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
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.domain.UserModel
import com.waffiq.bazz_movies.core.domain.WatchlistModel
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers.FavWatchlistHelper.launchAndHandleOutcome
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers.SnackBarUserLoginData
import com.waffiq.bazz_movies.core.movie.domain.usecase.mediastate.GetMovieStateUseCase
import com.waffiq.bazz_movies.core.movie.domain.usecase.mediastate.GetTvStateUseCase
import com.waffiq.bazz_movies.core.movie.domain.usecase.postmethod.PostMethodUseCase
import com.waffiq.bazz_movies.feature.watchlist.domain.usecase.GetWatchlistMovieUseCase
import com.waffiq.bazz_movies.feature.watchlist.domain.usecase.GetWatchlistTvUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MyWatchlistViewModel @Inject constructor(
  private val getWatchlistMovieUseCase: GetWatchlistMovieUseCase,
  private val getWatchlistTvUseCase: GetWatchlistTvUseCase,
  private val postMethodUseCase: PostMethodUseCase,
  private val getStatedMovieUseCase: GetMovieStateUseCase,
  private val getStatedTvUseCase: GetTvStateUseCase,
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

  fun postFavorite(
    sesId: String,
    userId: Int,
    data: FavoriteModel,
    title: String,
  ) {
    launchAndHandleOutcome(
      flow = postMethodUseCase.postFavorite(sesId, data, userId),
      onSuccess = {
        _snackBarAdded.value = Event(SnackBarUserLoginData(true, title, data, null))
      },
      onError = {
        _snackBarAdded.value = Event(SnackBarUserLoginData(false, it, null, null))
      },
    )
  }

  fun postWatchlist(
    sesId: String,
    userId: Int,
    data: WatchlistModel,
    title: String,
  ) {
    launchAndHandleOutcome(
      flow = postMethodUseCase.postWatchlist(sesId, data, userId),
      onSuccess = {
        _snackBarAdded.value = Event(SnackBarUserLoginData(true, title, null, data))
      },
      onError = {
        _snackBarAdded.value = Event(SnackBarUserLoginData(false, it, null, null))
      },
    )
  }

  fun checkTvStatedThenPostFavorite(
    user: UserModel,
    id: Int,
    title: String,
  ) {
    launchAndHandleOutcome(
      flow = getStatedTvUseCase.getTvState(user.token, id),
      onSuccess = {
        if (it.favorite) {
          _snackBarAlready.value = Event(title)
        } else {
          postFavorite(
            user.token,
            user.userId,
            FavoriteModel(TV_MEDIA_TYPE, id, true),
            title
          )
        }
      },
      onError = {
        _snackBarAdded.value = Event(SnackBarUserLoginData(false, it, null, null))
      },
    )
  }

  fun checkMovieStatedThenPostFavorite(
    user: UserModel,
    id: Int,
    title: String,
  ) {
    launchAndHandleOutcome(
      flow = getStatedMovieUseCase.getMovieState(user.token, id),
      onSuccess = {
        if (it.favorite) {
          _snackBarAlready.value = Event(title)
        } else {
          postFavorite(
            user.token,
            user.userId,
            FavoriteModel(MOVIE_MEDIA_TYPE, id, true),
            title
          )
        }
      },
      onError = {
        _snackBarAdded.value = Event(SnackBarUserLoginData(false, it, null, null))
      },
    )
  }
  // endregion NETWORK
}
