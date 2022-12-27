package com.waffiq.bazz_movies.ui.activity.myfavorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.waffiq.bazz_movies.R
import com.waffiq.bazz_movies.data.local.model.Favorite
import com.waffiq.bazz_movies.data.repository.MoviesRepository
import com.waffiq.bazz_movies.utils.Event

class MyFavoriteViewModel(private val movieRepository: MoviesRepository) : ViewModel() {

  private val _snackbarText = MutableLiveData<Event<Int>>()
  val snackbarText: LiveData<Event<Int>> = _snackbarText

  private val _undo = MutableLiveData<Event<Favorite>>()
  val undo: LiveData<Event<Favorite>> = _undo

  val allFavorite = movieRepository.getAllFavorite()

  fun searchFavorite(name: String): LiveData<List<Favorite>> {
    return movieRepository.getSpecificFavorite(name)
  }

  fun deleteFav(favorite: Favorite) {
    movieRepository.delete(favorite)
    _snackbarText.value = Event(R.string.deleted)
    _undo.value = Event(favorite)
  }

  fun insertToFavorite(fav: Favorite) = movieRepository.insert(fav)
}