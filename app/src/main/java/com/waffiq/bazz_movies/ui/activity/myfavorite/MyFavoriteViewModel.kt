package com.waffiq.bazz_movies.ui.activity.myfavorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
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

class MyFavoriteViewModel(private val movieRepository: MoviesRepository) : ViewModel() {

  private val _localDatabaseResult = MutableLiveData<Event<LocalDatabaseResult>>()
  val localDatabaseResult: LiveData<Event<LocalDatabaseResult>> get() = _localDatabaseResult

  /**
   * Function for database
   */
  val getFavoriteTvFromDB = movieRepository.getFavoriteTvFromDB()
  val getFavoriteMoviesFromDB = movieRepository.getFavoriteMoviesFromDB()
  fun undoDeleteDB() = movieRepository.undoDB

  fun insertToDB(fav: FavoriteDB) {
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
  fun delFromFavoriteDB(fav: FavoriteDB) = movieRepository.deleteFromDB(fav)
  fun updateToFavoriteDB(fav: FavoriteDB) = movieRepository.updateFavoriteDB(false, fav)
  fun updateToWatchlistDB(fav: FavoriteDB) = movieRepository.updateWatchlistDB(false, fav)
  fun updateToRemoveFromWatchlistDB(fav: FavoriteDB) = movieRepository.updateWatchlistDB(true, fav)
  fun updateToRemoveFromFavoriteDB(fav: FavoriteDB) = movieRepository.updateFavoriteDB(true, fav)

  // fun searchFavorite(name: String) = movieRepository.getFavoriteDB(name)

  // fun getSnackBarTextInt() = movieRepository.snackBarTextInt

  /**
   * Function for remote
   */
  fun getFavoriteMovies(sessionId: String) =
    movieRepository.getPagingFavoriteMovies(sessionId).cachedIn(viewModelScope).asLiveData()

  fun getFavoriteTvSeries(sessionId: String) =
    movieRepository.getPagingFavoriteTv(sessionId).cachedIn(viewModelScope).asLiveData()

  fun postFavorite(user: UserModel, data: Favorite) =
    movieRepository.postFavorite(user.token, data, user.userId)

  fun postWatchlist(user: UserModel, data: Watchlist) =
    movieRepository.postWatchlist(user.token, data, user.userId)

  fun getStated(sessionId: String, id: Int) = movieRepository.getStatedMovie(sessionId, id)
  fun getStated() = movieRepository.stated
}