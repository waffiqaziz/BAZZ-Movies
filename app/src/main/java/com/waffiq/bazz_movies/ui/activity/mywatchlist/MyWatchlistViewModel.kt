package com.waffiq.bazz_movies.ui.activity.mywatchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.waffiq.bazz_movies.data.local.model.Favorite
import com.waffiq.bazz_movies.data.local.model.FavoriteDB
import com.waffiq.bazz_movies.data.local.model.UserModel
import com.waffiq.bazz_movies.data.local.model.Watchlist
import com.waffiq.bazz_movies.data.repository.MoviesRepository

class MyWatchlistViewModel(private val movieRepository: MoviesRepository) : ViewModel() {

  /**
   * Function for database
   */
  val getWatchlistMoviesDB = movieRepository.getWatchlistMovieFromDB()
  val getWatchlistTvSeriesDB = movieRepository.getWatchlistTvFromDB()
  fun undoDeleteDB() = movieRepository.undoDB

  fun insertToDB(fav: FavoriteDB) = movieRepository.insertToDB(fav)
  fun delFromFavoriteDB(fav: FavoriteDB) = movieRepository.deleteFromDB(fav)
  fun updateToFavoriteDB(fav: FavoriteDB) = movieRepository.updateFavoriteDB(false, fav)
  fun updateToWatchlistDB(fav: FavoriteDB) = movieRepository.updateWatchlistDB(false, fav)
  fun updateToRemoveFromWatchlistDB(fav: FavoriteDB) = movieRepository.updateWatchlistDB(true, fav)
  fun updateToRemoveFromFavoriteDB(fav: FavoriteDB) = movieRepository.updateFavoriteDB(true, fav)

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