package com.waffiq.bazz_movies.ui.activity.myfavorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.waffiq.bazz_movies.data.local.datasource.LocalDataSourceInterface
import com.waffiq.bazz_movies.data.local.model.FavoriteDB
import com.waffiq.bazz_movies.data.local.model.UserModel
import com.waffiq.bazz_movies.data.remote.Favorite
import com.waffiq.bazz_movies.data.remote.Watchlist
import com.waffiq.bazz_movies.data.remote.response.tmdb.StatedResponse
import com.waffiq.bazz_movies.data.repository.MoviesRepository
import com.waffiq.bazz_movies.utils.Event
import com.waffiq.bazz_movies.utils.LocalResult
import com.waffiq.bazz_movies.utils.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyFavoriteViewModel(private val movieRepository: MoviesRepository) : ViewModel() {

  private val _localResult = MutableLiveData<Event<LocalResult>>()
  val localResult: LiveData<Event<LocalResult>> get() = _localResult

  private val _stated = MutableLiveData<StatedResponse?>()
  val stated: LiveData<StatedResponse?> get() = _stated.distinctUntilChanged()

  private val _undoDB = MutableLiveData<Event<FavoriteDB>>()
  val undoDB: LiveData<Event<FavoriteDB>> = _undoDB

  private val _snackBarAlready = MutableLiveData<Event<String>>()
  val snackBarAlready: LiveData<Event<String>> = _snackBarAlready

  private val _snackBarAdded = MutableLiveData<Event<SnackBarLoginData>>()
  val snackBarAdded: LiveData<Event<SnackBarLoginData>> = _snackBarAdded

  // region LOCAL DATABASE
  val favoriteTvFromDB =
    movieRepository.favoriteTvFromDB.asLiveData().distinctUntilChanged()
  val favoriteMoviesFromDB =
    movieRepository.favoriteMoviesFromDB.asLiveData().distinctUntilChanged()

  fun insertToDB(fav: FavoriteDB) {
    viewModelScope.launch(Dispatchers.IO) {
      movieRepository.insertToDB(fav) { resultCode ->
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

  fun delFromFavoriteDB(fav: FavoriteDB) {
    viewModelScope.launch(Dispatchers.IO) {
      movieRepository.deleteFromDB(fav)
    }
    _undoDB.value = Event(fav)
  }

  fun updateToFavoriteDB(fav: FavoriteDB) {
    viewModelScope.launch(Dispatchers.IO) { movieRepository.updateFavoriteItemDB(false, fav) }
    _undoDB.value = Event(fav)
  }

  fun updateToWatchlistDB(fav: FavoriteDB) {
    viewModelScope.launch(Dispatchers.IO) { movieRepository.updateWatchlistItemDB(false, fav) }
    _undoDB.value = Event(fav)
  }

  fun updateToRemoveFromWatchlistDB(fav: FavoriteDB) {
    viewModelScope.launch(Dispatchers.IO) { movieRepository.updateWatchlistItemDB(true, fav) }
    _undoDB.value = Event(fav)
  }

  fun updateToRemoveFromFavoriteDB(fav: FavoriteDB) {
    viewModelScope.launch(Dispatchers.IO) { movieRepository.updateFavoriteItemDB(true, fav) }
    _undoDB.value = Event(fav)
  }
  // endregion LOCAL DATABASE

  // region NETWORK
  fun getFavoriteMovies(sessionId: String) =
    movieRepository.getPagingFavoriteMovies(sessionId).cachedIn(viewModelScope).asLiveData()

  fun getFavoriteTvSeries(sessionId: String) =
    movieRepository.getPagingFavoriteTv(sessionId).cachedIn(viewModelScope).asLiveData()

  fun postFavorite(user: UserModel, data: Favorite, title: String, position: Int) =
    viewModelScope.launch {
      val result = movieRepository.postFavorite(user.token, data, user.userId)
      when (result.status) {
        Status.SUCCESS -> {
          if (result.data?.statusCode == 1) _snackBarAdded.value =
            Event(SnackBarLoginData(title, data, null, position))
        }

        Status.ERROR -> {}
        Status.LOADING -> {}
      }
    }

  fun postWatchlist(user: UserModel, data: Watchlist,title: String, position: Int) =
    viewModelScope.launch {
      val result = movieRepository.postWatchlist(user.token, data, user.userId)
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
      movieRepository.getStatedMovie(sessionId, id).collect { networkResult ->
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
      movieRepository.getStatedTv(sessionId, id).collect { networkResult ->
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

  data class SnackBarLoginData(
    val title: String,
    val favorite: Favorite?,
    val watchlist: Watchlist?,
    val position: Int
  )
}

