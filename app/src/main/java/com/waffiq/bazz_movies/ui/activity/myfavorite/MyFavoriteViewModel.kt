package com.waffiq.bazz_movies.ui.activity.myfavorite

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.waffiq.bazz_movies.data.local.model.Favorite
import com.waffiq.bazz_movies.data.local.model.FavoriteDB
import com.waffiq.bazz_movies.data.local.model.UserModel
import com.waffiq.bazz_movies.data.local.model.Watchlist
import com.waffiq.bazz_movies.data.repository.MoviesRepository

class MyFavoriteViewModel(private val movieRepository: MoviesRepository) : ViewModel() {

  /**
   * Function for database
   */
  val getFavoriteTvFromDB = movieRepository.getFavoriteTvFromDB()
  val getFavoriteMoviesFromDB = movieRepository.getFavoriteMoviesFromDB()
  fun isWatchlistDB(id: Int) = movieRepository.isWatchlistDB(id)
  fun isWatchlistDB() = movieRepository.isWatchlist
  fun undoDeleteDB() = movieRepository.undoDB

  fun insertToDB(fav: FavoriteDB) = movieRepository.insertToDB(fav)
  fun delFromFavoriteDB(fav: FavoriteDB) = movieRepository.deleteFromDB(fav)
  fun updateToFavoriteDB(fav: FavoriteDB) = movieRepository.updateFavoriteDB(false, fav)
  fun updateToWatchlistDB(fav: FavoriteDB) = movieRepository.updateWatchlistDB(false, fav)
  fun updateToRemoveFromWatchlistDB(fav: FavoriteDB) = movieRepository.updateWatchlistDB(true, fav)
  fun updateToRemoveFromFavoriteDB(fav: FavoriteDB) = movieRepository.updateFavoriteDB(true, fav)


  fun searchFavorite(name: String) = movieRepository.getFavoriteDB(name)

  fun getSnackBarTextInt() = movieRepository.snackBarTextInt
  fun getSnackBarTextInt2() = movieRepository.snackBarTextInt2
  fun getSnackBarTextInt3() = movieRepository.snackBarTextInt3

  /**
   * Function for network
   */
  fun getFavoriteMovies(sessionId: String) =
    movieRepository.getPagingFavoriteMovies(sessionId).cachedIn(viewModelScope).asLiveData()

  fun getFavoriteTvSeries(sessionId: String) =
    movieRepository.getPagingFavoriteTv(sessionId).cachedIn(viewModelScope).asLiveData()

  fun postFavoriteToDelete(user: UserModel, data: Favorite) =
    movieRepository.postFavorite(true, user.token, data, user.userId)

  fun postFavorite(user: UserModel, data: Favorite) =
    movieRepository.postFavorite(false, user.token, data, user.userId)

  fun postWatchlist(user: UserModel, data: Watchlist) =
    movieRepository.postWatchlist(false, user.token, data, user.userId)

  fun getStated(sessionId: String, id: Int) = movieRepository.getStatedMovie(sessionId, id)
  fun getStated() = movieRepository.stated
}