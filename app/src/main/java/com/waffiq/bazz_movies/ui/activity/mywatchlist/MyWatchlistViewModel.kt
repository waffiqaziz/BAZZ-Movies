package com.waffiq.bazz_movies.ui.activity.mywatchlist

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

class MyWatchlistViewModel(private val movieRepository: MoviesRepository) : ViewModel() {

  private val _localDatabaseResult = MutableLiveData<Event<LocalDatabaseResult>>()
  val localDatabaseResult: LiveData<Event<LocalDatabaseResult>> get() = _localDatabaseResult

  private val _stated = MutableLiveData<Event<StatedResponse?>>()
  val stated: LiveData<Event<StatedResponse?>> get() = _stated

  private val _undoDB = MutableLiveData<Event<FavoriteDB>>()
  val undoDB: LiveData<Event<FavoriteDB>> = _undoDB

  /**
   * Function for database
   */
  val getWatchlistMoviesDB =
    movieRepository.watchlistMovieFromDB.asLiveData().distinctUntilChanged()
  val getWatchlistTvSeriesDB =
    movieRepository.watchlistTvFromDB.asLiveData().distinctUntilChanged()

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

  fun updateToFavoriteDB(fav: FavoriteDB) =
    viewModelScope.launch(Dispatchers.IO) { movieRepository.updateFavoriteItemDB(false, fav) }

  fun updateToWatchlistDB(fav: FavoriteDB) =
    viewModelScope.launch(Dispatchers.IO) { movieRepository.updateWatchlistItemDB(false, fav) }

  fun updateToRemoveFromWatchlistDB(fav: FavoriteDB) {
    viewModelScope.launch(Dispatchers.IO) { movieRepository.updateWatchlistItemDB(true, fav) }
    _undoDB.value = Event(fav)
  }

  fun updateToRemoveFromFavoriteDB(fav: FavoriteDB) {
    viewModelScope.launch(Dispatchers.IO) { movieRepository.updateFavoriteItemDB(true, fav) }
    _undoDB.value = Event(fav)
  }

  /**
   * Function for remote
   */
  fun getWatchlistMovies(sessionId: String) =
    movieRepository.getPagingWatchlistMovies(sessionId).cachedIn(viewModelScope).asLiveData()

  fun getWatchlistTvSeries(sessionId: String) =
    movieRepository.getPagingWatchlistTv(sessionId).cachedIn(viewModelScope).asLiveData()

  fun postFavorite(user: UserModel, data: Favorite) =
    viewModelScope.launch { movieRepository.postFavorite(user.token, data, user.userId) }

  fun postWatchlist(user: UserModel, data: Watchlist) =
    viewModelScope.launch { movieRepository.postWatchlist(user.token, data, user.userId) }

  fun getStatedMovie(sessionId: String, id: Int) {
    viewModelScope.launch {
      movieRepository.getStatedMovie(sessionId, id).collect { networkResult ->
        when (networkResult.status) {
          Status.SUCCESS -> _stated.value = Event(networkResult.data)
          Status.LOADING -> {}
          Status.ERROR -> {}
        }
      }
    }
  }

  fun getStatedTv(sessionId: String, id: Int) {
    viewModelScope.launch {
      movieRepository.getStatedTv(sessionId, id).collect { networkResult ->
        when (networkResult.status) {
          Status.SUCCESS -> _stated.value = Event(networkResult.data)
          Status.LOADING -> {}
          Status.ERROR -> {}
        }
      }
    }
  }
}