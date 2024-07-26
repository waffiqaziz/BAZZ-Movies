package com.waffiq.bazz_movies.ui.activity.myfavorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.waffiq.bazz_movies.data.local.datasource.LocalDataSourceInterface
import com.waffiq.bazz_movies.data.local.model.UserModel
import com.waffiq.bazz_movies.data.remote.SnackBarUserLoginData
import com.waffiq.bazz_movies.data.remote.post_body.FavoritePostModel
import com.waffiq.bazz_movies.data.remote.post_body.WatchlistPostModel
import com.waffiq.bazz_movies.domain.model.Favorite
import com.waffiq.bazz_movies.domain.model.Stated
import com.waffiq.bazz_movies.domain.usecase.get_favorite.GetFavoriteMovieUseCase
import com.waffiq.bazz_movies.domain.usecase.get_favorite.GetFavoriteTvUseCase
import com.waffiq.bazz_movies.domain.usecase.get_stated.GetStatedMovieUseCase
import com.waffiq.bazz_movies.domain.usecase.get_stated.GetStatedTvUseCase
import com.waffiq.bazz_movies.domain.usecase.local_database.LocalDatabaseUseCase
import com.waffiq.bazz_movies.domain.usecase.post_method.PostMethodUseCase
import com.waffiq.bazz_movies.utils.LocalResult
import com.waffiq.bazz_movies.utils.Status
import com.waffiq.bazz_movies.utils.common.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyFavoriteViewModel(
  private val getFavoriteMovieUseCase: GetFavoriteMovieUseCase,
  private val getFavoriteTvUseCase: GetFavoriteTvUseCase,
  private val postMethodUseCase: PostMethodUseCase,
  private val localDatabaseUseCase: LocalDatabaseUseCase,
  private val getStatedMovieUseCase: GetStatedMovieUseCase,
  private val getStatedTvUseCase: GetStatedTvUseCase
) : ViewModel() {

  private val _localResult = MutableLiveData<Event<LocalResult>>()
  val localResult: LiveData<Event<LocalResult>> get() = _localResult

  private val _stated = MutableLiveData<Stated?>()
  val stated: LiveData<Stated?> get() = _stated.distinctUntilChanged()

  private val _undoDB = MutableLiveData<Event<Favorite>>()
  val undoDB: LiveData<Event<Favorite>> = _undoDB

  private val _snackBarAlready = MutableLiveData<Event<String>>()
  val snackBarAlready: LiveData<Event<String>> = _snackBarAlready

  private val _snackBarAdded = MutableLiveData<Event<SnackBarUserLoginData>>()
  val snackBarAdded: LiveData<Event<SnackBarUserLoginData>> = _snackBarAdded

  // region LOCAL DATABASE
  val favoriteTvFromDB =
    localDatabaseUseCase.favoriteTvFromDB.asLiveData().distinctUntilChanged()
  val favoriteMoviesFromDB =
    localDatabaseUseCase.favoriteMoviesFromDB.asLiveData().distinctUntilChanged()

  fun insertToDB(fav: Favorite) {
    viewModelScope.launch(Dispatchers.IO) {
      localDatabaseUseCase.insertToDB(fav) { resultCode ->
        val result = when (resultCode) {
          LocalDataSourceInterface.ERROR_DUPLICATE_ENTRY -> LocalResult.Error("Duplicate entry")
          LocalDataSourceInterface.ERROR_UNKNOWN -> LocalResult.Error("Unknown error")
          LocalDataSourceInterface.SUCCESS -> LocalResult.Success
          else -> LocalResult.Error("Unknown result code: $resultCode")
        }
        _localResult.postValue(Event(result))
      }
    }
  }

  fun delFromFavoriteDB(fav: Favorite) {
    viewModelScope.launch(Dispatchers.IO) {
      localDatabaseUseCase.deleteFromDB(fav)
    }
    _undoDB.value = Event(fav)
  }

  fun updateToFavoriteDB(fav: Favorite) {
    viewModelScope.launch(Dispatchers.IO) { localDatabaseUseCase.updateFavoriteItemDB(false, fav) }
    _undoDB.value = Event(fav)
  }

  fun updateToWatchlistDB(fav: Favorite) {
    viewModelScope.launch(Dispatchers.IO) { localDatabaseUseCase.updateWatchlistItemDB(false, fav) }
    _undoDB.value = Event(fav)
  }

  fun updateToRemoveFromWatchlistDB(fav: Favorite) {
    viewModelScope.launch(Dispatchers.IO) { localDatabaseUseCase.updateWatchlistItemDB(true, fav) }
    _undoDB.value = Event(fav)
  }

  fun updateToRemoveFromFavoriteDB(fav: Favorite) {
    viewModelScope.launch(Dispatchers.IO) { localDatabaseUseCase.updateFavoriteItemDB(true, fav) }
    _undoDB.value = Event(fav)
  }
  // endregion LOCAL DATABASE

  // region NETWORK
  fun getFavoriteMovies(sesId: String) =
    getFavoriteMovieUseCase.getPagingFavoriteMovies(sesId).cachedIn(viewModelScope).asLiveData()

  fun getFavoriteTvSeries(sesId: String) =
    getFavoriteTvUseCase.getPagingFavoriteTv(sesId).cachedIn(viewModelScope).asLiveData()

  fun postFavorite(sesId: String, userId: Int, data: FavoritePostModel, title: String) {
    viewModelScope.launch {
      postMethodUseCase.postFavorite(sesId, data, userId).collect { networkResult ->
        when (networkResult.status) {
          Status.SUCCESS -> _snackBarAdded.value =
            Event(SnackBarUserLoginData(true, title, data, null))

          Status.ERROR -> _snackBarAdded.value =
            Event(SnackBarUserLoginData(false, networkResult.message.toString(), null, null))

          Status.LOADING -> {}
        }
      }
    }
  }

  fun postWatchlist(sesId: String, userId: Int, data: WatchlistPostModel, title: String) {
    viewModelScope.launch {
      postMethodUseCase.postWatchlist(sesId, data, userId).collect { networkResult ->
        when (networkResult.status) {
          Status.SUCCESS -> _snackBarAdded.value =
            Event(SnackBarUserLoginData(true, title, null, data))

          Status.ERROR -> _snackBarAdded.value =
            Event(SnackBarUserLoginData(false, networkResult.message.toString(), null, null))

          Status.LOADING -> {}
        }
      }
    }
  }

  fun checkStatedThenPostFavorite(
    mediaType: String,
    user: UserModel,
    id: Int,
    title: String
  ) {
    viewModelScope.launch {
      getStatedMovieUseCase.getStatedMovie(user.token, id).collect { networkResult ->
        when (networkResult.status) {
          Status.SUCCESS -> {
            if (networkResult.data?.watchlist == true) _snackBarAlready.value = Event(title)
            else {
              postFavorite(
                user.token,
                user.userId,
                FavoritePostModel(mediaType, id, true),
                title
              )
            }
          }

          Status.LOADING -> {}
          Status.ERROR -> _snackBarAdded.value =
            Event(SnackBarUserLoginData(false, networkResult.message.toString(), null, null))
        }
      }
    }
  }

  fun checkStatedThenPostWatchlist(
    mediaType: String,
    user: UserModel,
    id: Int,
    title: String
  ) {
    viewModelScope.launch {
      if (mediaType == "movie") {
        getStatedMovieUseCase.getStatedMovie(user.token, id).collect { networkResult ->
          when (networkResult.status) {
            Status.SUCCESS -> {
              if (networkResult.data?.watchlist == true) _snackBarAlready.value = Event(title)
              else {
                postWatchlist(
                  user.token,
                  user.userId,
                  WatchlistPostModel(mediaType, id, true),
                  title
                )
              }
            }

            Status.LOADING -> {}
            Status.ERROR -> _snackBarAdded.value =
              Event(SnackBarUserLoginData(false, networkResult.message.toString(), null, null))
          }
        }
      } else {
        getStatedTvUseCase.getStatedTv(user.token, id).collect { networkResult ->
          when (networkResult.status) {
            Status.SUCCESS -> {
              if (networkResult.data?.watchlist == true) _snackBarAlready.value = Event(title)
              else {
                postWatchlist(
                  user.token,
                  user.userId,
                  WatchlistPostModel(mediaType, id, true),
                  title
                )
              }
            }

            Status.LOADING -> {}
            Status.ERROR -> _snackBarAdded.value =
              Event(SnackBarUserLoginData(false, networkResult.message.toString(), null, null))
          }
        }
      }
    }
  }
  // endregion NETWORK
}