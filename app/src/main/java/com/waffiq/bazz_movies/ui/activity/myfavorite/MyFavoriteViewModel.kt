package com.waffiq.bazz_movies.ui.activity.myfavorite

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.waffiq.bazz_movies.data.local.model.Favorite
import com.waffiq.bazz_movies.data.local.model.FavoriteDB
import com.waffiq.bazz_movies.data.local.model.UserModel
import com.waffiq.bazz_movies.data.repository.MoviesRepository

class MyFavoriteViewModel(private val movieRepository: MoviesRepository) : ViewModel() {

  // from db
  val getFavoriteTvFromDB = movieRepository.getFavoriteTvFromDB()
  val getFavoriteMoviesFromDB = movieRepository.getFavoriteMoviesFromDB()
  fun isWatchlistDB(id: Int) = movieRepository.isWatchlistDB(id)
  fun isWatchlistDB() = movieRepository.isWatchlist
  fun updateToRemoveFromFavoriteDB(fav: FavoriteDB) = movieRepository.updateFavoriteDB(true, fav)

  fun searchFavorite(name: String) = movieRepository.getFavoriteDB(name)

  fun deleteFavDB(favoriteDB: FavoriteDB) {
    movieRepository.deleteFromDB(favoriteDB)
  }

  fun undoDeleteDB() = movieRepository.undoDB

  fun getSnackBarTextInt() = movieRepository.snackBarTextInt

  fun insertToDB(fav: FavoriteDB) = movieRepository.insertToDB(fav)

  // from network
  fun getFavoriteMovies(sessionId: String) =
    movieRepository.getPagingFavoriteMovies(sessionId).cachedIn(viewModelScope).asLiveData()

  fun getFavoriteTvSeries(sessionId: String) =
    movieRepository.getPagingFavoriteTv(sessionId).cachedIn(viewModelScope).asLiveData()

  fun postFavorite(user: UserModel, data: Favorite) =
    movieRepository.postFavorite(user.token, data, user.userId)
}