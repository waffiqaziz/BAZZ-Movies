package com.waffiq.bazz_movies.ui.activity.myfavorite

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.waffiq.bazz_movies.R
import com.waffiq.bazz_movies.data.local.model.FavoriteDB
import com.waffiq.bazz_movies.data.repository.MoviesRepository
import com.waffiq.bazz_movies.utils.Event

class MyFavoriteViewModel(private val movieRepository: MoviesRepository) : ViewModel() {

  private val _snackBarText = MutableLiveData<Event<Int>>()
  val snackBarText: LiveData<Event<Int>> = _snackBarText

  private val _undo = MutableLiveData<Event<FavoriteDB>>()
  val undo: LiveData<Event<FavoriteDB>> = _undo

  val allFavoriteDB = movieRepository.getAllFavoriteFromDB()

  fun searchFavorite(name: String): LiveData<List<FavoriteDB>> {
    return movieRepository.getFavoriteDB(name)
  }

  fun deleteFavDB(favoriteDB: FavoriteDB) {
    movieRepository.deleteFromDB(favoriteDB)
    _snackBarText.value = Event(R.string.deleted_from_favorite)
    _undo.value = Event(favoriteDB)
  }

  fun insertToFavoriteDB(fav: FavoriteDB) = movieRepository.insertDB(fav)

  fun getFavoriteMovies(sessionId: String) =
    movieRepository.getPagingFavoriteMovies(sessionId).cachedIn(viewModelScope).asLiveData()


}