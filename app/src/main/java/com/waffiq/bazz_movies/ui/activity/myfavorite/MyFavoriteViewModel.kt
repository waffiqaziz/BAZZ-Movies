package com.waffiq.bazz_movies.ui.activity.myfavorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.waffiq.bazz_movies.data.local.datasource.LocalDataSourceInterface
import com.waffiq.bazz_movies.data.remote.SnackBarLoginData
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

  private val _snackBarAdded = MutableLiveData<Event<SnackBarLoginData>>()
  val snackBarAdded: LiveData<Event<SnackBarLoginData>> = _snackBarAdded

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
  fun getFavoriteMovies(sessionId: String) =
    getFavoriteMovieUseCase.getPagingFavoriteMovies(sessionId).cachedIn(viewModelScope).asLiveData()

  fun getFavoriteTvSeries(sessionId: String) =
    getFavoriteTvUseCase.getPagingFavoriteTv(sessionId).cachedIn(viewModelScope).asLiveData()

  fun postFavorite(sessionId: String, userId: Int, data: FavoritePostModel, title: String, position: Int) =
    viewModelScope.launch {
      val result = postMethodUseCase.postFavorite(sessionId, data, userId)
      when (result.status) {
        Status.SUCCESS -> {
          if (result.data?.statusCode == 1) _snackBarAdded.value =
            Event(SnackBarLoginData(title, data, null, position))
        }

        Status.ERROR -> {}
        Status.LOADING -> {}
      }
    }

  fun postWatchlist(sessionId: String, userId: Int, data: WatchlistPostModel, title: String, position: Int) =
    viewModelScope.launch {
      val result = postMethodUseCase.postWatchlist(sessionId, data, userId)
      when (result.status) {
        Status.SUCCESS -> {
          if (result.data?.statusCode == 1) _snackBarAdded.value =
            Event(SnackBarLoginData(title, null, data, position))
        }

        Status.ERROR -> {}
        Status.LOADING -> {}
      }
    }

  fun getStatedMovie(sessionId: String, id: Int, title: String) {
    viewModelScope.launch {
      getStatedMovieUseCase.getStatedMovie(sessionId, id).collect { networkResult ->
        when (networkResult.status) {
          Status.SUCCESS -> {
            if (networkResult.data?.watchlist == true)
              _snackBarAlready.value = Event(title)
            else _stated.value = networkResult.data
          }

          Status.LOADING -> {}
          Status.ERROR -> {}
        }
      }
    }
  }

  fun getStatedTv(sessionId: String, id: Int, title: String) {
    viewModelScope.launch {
      getStatedTvUseCase.getStatedTv(sessionId, id).collect { networkResult ->
        when (networkResult.status) {
          Status.SUCCESS -> {
            if (networkResult.data?.watchlist == true)
              _snackBarAlready.value = Event(title)
            else _stated.value = networkResult.data
          }

          Status.ERROR -> {}
          Status.LOADING -> {}
        }
      }
    }
  }
  // endregion NETWORK
}