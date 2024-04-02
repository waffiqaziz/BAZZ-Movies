package com.waffiq.bazz_movies.ui.activity.myfavorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.waffiq.bazz_movies.data.local.datasource.LocalDataSourceInterface
import com.waffiq.bazz_movies.data.remote.Favorite
import com.waffiq.bazz_movies.data.local.model.FavoriteDB
import com.waffiq.bazz_movies.data.local.model.UserModel
import com.waffiq.bazz_movies.data.remote.Watchlist
import com.waffiq.bazz_movies.data.remote.response.tmdb.StatedResponse
import com.waffiq.bazz_movies.data.repository.MoviesRepository
import com.waffiq.bazz_movies.utils.Event
import com.waffiq.bazz_movies.utils.LocalDatabaseResult
import com.waffiq.bazz_movies.utils.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyFavoriteViewModel(private val movieRepository: MoviesRepository) : ViewModel() {

  private val _localDatabaseResult = MutableLiveData<Event<LocalDatabaseResult>>()
  val localDatabaseResult: LiveData<Event<LocalDatabaseResult>> get() = _localDatabaseResult

  private val _stated = MutableLiveData<Event<StatedResponse?>>()
  val stated: LiveData<Event<StatedResponse?>> get() = _stated

  private val _undoDB = MutableLiveData<Event<FavoriteDB>>()
  val undoDB: LiveData<Event<FavoriteDB>> = _undoDB

  private val _snackBarAlready = MutableLiveData<Event<String>>()
  val snackBarAlready: LiveData<Event<String>> = _snackBarAlready

  private val _snackBarL = MutableLiveData<Event<SnackBarLoginData>>()
  val snackBarL: LiveData<Event<SnackBarLoginData>> = _snackBarL


  // region LOCAL DATABASE
  val favoriteTvFromDB =
    movieRepository.favoriteTvFromDB.asLiveData().distinctUntilChanged()
  val favoriteMoviesFromDB =
    movieRepository.favoriteMoviesFromDB.asLiveData().distinctUntilChanged()

  fun insertToDB(fav: FavoriteDB) {
    viewModelScope.launch(Dispatchers.IO) {
      movieRepository.insertToDB(fav) { resultCode ->
        val result = when (resultCode) {
          LocalDataSourceInterface.ERROR_DUPLICATE_ENTRY -> LocalDatabaseResult.Error("Duplicate entry")
          LocalDataSourceInterface.ERROR_UNKNOWN -> LocalDatabaseResult.Error("Unknown error")
          LocalDataSourceInterface.SUCCESS -> LocalDatabaseResult.Success
          else -> LocalDatabaseResult.Error("Unknown result code: $resultCode")
        }
        _localDatabaseResult.postValue(Event(result))
      }
    }
  }

  fun delFromFavoriteDB(fav: FavoriteDB) {
    viewModelScope.launch(Dispatchers.IO) { movieRepository.deleteFromDB(fav) }
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
  // fun searchFavorite(name: String) = movieRepository.getFavoriteDB(name)
  // endregion LOCAL DATABASE

  // region NETWORK
  fun getFavoriteMovies(sessionId: String) =
    movieRepository.getPagingFavoriteMovies(sessionId).cachedIn(viewModelScope).asLiveData()

  fun getFavoriteTvSeries(sessionId: String) =
    movieRepository.getPagingFavoriteTv(sessionId).cachedIn(viewModelScope).asLiveData()

  fun postFavorite(user: UserModel, data: Favorite) =
    viewModelScope.launch { movieRepository.postFavorite(user.token, data, user.userId) }

  fun postWatchlist(user: UserModel, data: Watchlist) =
    viewModelScope.launch { movieRepository.postWatchlist(user.token, data, user.userId) }

  fun getStatedMovie(sessionId: String, id: Int, title: String) {
    viewModelScope.launch {
      movieRepository.getStatedMovie(sessionId, id).collect { networkResult ->
        when (networkResult.status) {
          Status.SUCCESS -> {
            if (networkResult.data?.watchlist == true)
              _snackBarAlready.value = Event(title)
            else _stated.value = Event(networkResult.data)
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
            else _stated.value = Event(networkResult.data)
          }

          Status.ERROR -> {}
          Status.LOADING -> {}
        }
      }
    }
  }
  // endregion NETWORK

  data class SnackBarLoginData(
    val favorite: Favorite?,
    val watchlist: Watchlist?,
    val title: String,
    val position: Int?
  )
}

