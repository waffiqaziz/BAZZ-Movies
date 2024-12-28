package com.waffiq.bazz_movies.core.favoritewatchlist.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.database.domain.usecase.local_database.LocalDatabaseUseCase
import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.domain.Favorite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedDBViewModel @Inject constructor(
  private val localDatabaseUseCase: LocalDatabaseUseCase
) : ViewModel() {

  private val _dbResult = MutableLiveData<Event<DbResult<Int>>>()
  val dbResult: LiveData<Event<DbResult<Int>>> get() = _dbResult

  private val _undoDB = MutableLiveData<Event<Favorite>>()
  val undoDB: LiveData<Event<Favorite>> = _undoDB

  val favoriteTvFromDB =
    localDatabaseUseCase.favoriteTvFromDB.asLiveData().distinctUntilChanged()
  val favoriteMoviesFromDB =
    localDatabaseUseCase.favoriteMoviesFromDB.asLiveData().distinctUntilChanged()

  val watchlistMoviesDB =
    localDatabaseUseCase.watchlistMovieFromDB.asLiveData().distinctUntilChanged()
  val watchlistTvSeriesDB =
    localDatabaseUseCase.watchlistTvFromDB.asLiveData().distinctUntilChanged()

  fun insertToDB(fav: Favorite) {
    viewModelScope.launch {
      _dbResult.postValue(Event(localDatabaseUseCase.insertToDB(fav)))
    }
  }

  fun delFromFavoriteDB(fav: Favorite) {
    viewModelScope.launch {
      _dbResult.postValue(Event(localDatabaseUseCase.deleteFromDB(fav)))
    }
    _undoDB.value = Event(fav)
  }

  fun updateToFavoriteDB(fav: Favorite) {
    viewModelScope.launch {
      _dbResult.postValue(Event(localDatabaseUseCase.updateFavoriteItemDB(false, fav)))
    }
    _undoDB.value = Event(fav)
  }

  fun updateToWatchlistDB(fav: Favorite) {
    viewModelScope.launch {
      _dbResult.postValue(Event(localDatabaseUseCase.updateWatchlistItemDB(false, fav)))
    }
    _undoDB.value = Event(fav)
  }

  fun updateToRemoveFromWatchlistDB(fav: Favorite) {
    viewModelScope.launch {
      _dbResult.postValue(Event(localDatabaseUseCase.updateWatchlistItemDB(true, fav)))
    }
    _undoDB.value = Event(fav)
  }

  fun updateToRemoveFromFavoriteDB(fav: Favorite) {
    viewModelScope.launch {
      _dbResult.postValue(Event(localDatabaseUseCase.updateFavoriteItemDB(true, fav)))
    }
    _undoDB.value = Event(fav)
  }
}
