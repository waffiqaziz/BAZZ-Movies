package com.waffiq.bazz_movies.ui.activity.mywatchlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.waffiq.bazz_movies.data.local.datasource.LocalDataSourceInterface
import com.waffiq.bazz_movies.data.local.model.Favorite
import com.waffiq.bazz_movies.data.local.model.FavoriteDB
import com.waffiq.bazz_movies.data.local.model.UserModel
import com.waffiq.bazz_movies.data.local.model.Watchlist
import com.waffiq.bazz_movies.data.repository.MoviesRepository
import com.waffiq.bazz_movies.utils.Event
import com.waffiq.bazz_movies.utils.LocalDatabaseResult
import kotlinx.coroutines.launch

class MyWatchlistViewModel(private val movieRepository: MoviesRepository) : ViewModel() {

  private val _localDatabaseResult = MutableLiveData<Event<LocalDatabaseResult>>()
  val localDatabaseResult: LiveData<Event<LocalDatabaseResult>> get() = _localDatabaseResult

  /**
   * Function for database
   */
  val getWatchlistMoviesDB =
    movieRepository.watchlistMovieFromDB.asLiveData().distinctUntilChanged()
  val getWatchlistTvSeriesDB =
    movieRepository.watchlistTvFromDB.asLiveData().distinctUntilChanged()

  fun undoDeleteDB() = movieRepository.undoDB

  fun insertToDB(fav: FavoriteDB) {
    viewModelScope.launch {
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

  fun delFromFavoriteDB(fav: FavoriteDB) =
    viewModelScope.launch { movieRepository.deleteFromDB(fav) }

  fun updateToFavoriteDB(fav: FavoriteDB) =
    viewModelScope.launch { movieRepository.updateFavoriteDB(false, fav) }

  fun updateToWatchlistDB(fav: FavoriteDB) =
    viewModelScope.launch { movieRepository.updateWatchlistDB(false, fav) }

  fun updateToRemoveFromWatchlistDB(fav: FavoriteDB) =
    viewModelScope.launch { movieRepository.updateWatchlistDB(true, fav) }

  fun updateToRemoveFromFavoriteDB(fav: FavoriteDB) =
    viewModelScope.launch { movieRepository.updateFavoriteDB(true, fav) }

  /**
   * Function for remote
   */
  fun getWatchlistMovies(sessionId: String) =
    movieRepository.getPagingWatchlistMovies(sessionId).cachedIn(viewModelScope).asLiveData()

  fun getWatchlistTvSeries(sessionId: String) =
    movieRepository.getPagingWatchlistTv(sessionId).cachedIn(viewModelScope).asLiveData()

  fun postFavorite(user: UserModel, data: Favorite) =
    movieRepository.postFavorite(user.token, data, user.userId)

  fun postWatchlist(user: UserModel, data: Watchlist) =
    movieRepository.postWatchlist(user.token, data, user.userId)

  fun getStated(sessionId: String, id: Int) = movieRepository.getStatedMovie(sessionId, id)
  fun getStated() = movieRepository.stated
}