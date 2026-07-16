package com.waffiq.bazz_movies.core.favoritewatchlist.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.waffiq.bazz_movies.core.database.domain.usecase.FavoriteLocalDatabaseUseCase
import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.favoritewatchlist.domain.sort.GuestFavoriteSortOption
import com.waffiq.bazz_movies.core.favoritewatchlist.domain.sort.sortedByOption
import com.waffiq.bazz_movies.core.models.Favorite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedDBViewModel @Inject constructor(
  private val localDatabaseUseCase: FavoriteLocalDatabaseUseCase,
) : ViewModel() {

  private val _dbResult = MutableSharedFlow<DbResult<*>>(extraBufferCapacity = 1)
  val dbResult: SharedFlow<DbResult<*>> = _dbResult.asSharedFlow()

  private val _currentSort = MutableStateFlow(GuestFavoriteSortOption.RECENTLY_ADDED)
  val currentSort: StateFlow<GuestFavoriteSortOption> = _currentSort.asStateFlow()

  val favoriteTvFromDB =
    combine(localDatabaseUseCase.favoriteTvFromDB, _currentSort) { movies, sortOption ->
      movies.sortedByOption(sortOption)
    }.distinctUntilChanged().asLiveData()

  val favoriteMoviesFromDB =
    combine(localDatabaseUseCase.favoriteMoviesFromDB, _currentSort) { movies, sortOption ->
      movies.sortedByOption(sortOption)
    }.distinctUntilChanged().asLiveData()

  fun updateSort(option: GuestFavoriteSortOption) {
    if (_currentSort.value != option) _currentSort.value = option
  }

  val watchlistMoviesDB =
    combine(localDatabaseUseCase.watchlistMovieFromDB, _currentSort) { movies, sortOption ->
      movies.sortedByOption(sortOption)
    }.distinctUntilChanged().asLiveData()

  val watchlistTvSeriesDB =
    combine(localDatabaseUseCase.watchlistTvFromDB, _currentSort) { movies, sortOption ->
      movies.sortedByOption(sortOption)
    }.distinctUntilChanged().asLiveData()

  fun insertToDB(fav: Favorite) {
    viewModelScope.launch {
      _dbResult.emit(localDatabaseUseCase.insertToDB(fav))
    }
  }

  fun deleteFromDB(fav: Favorite) {
    viewModelScope.launch {
      _dbResult.emit(localDatabaseUseCase.deleteFromDB(fav.mediaId, fav.mediaType))
    }
  }

  fun updateDB(fav: Favorite) {
    viewModelScope.launch {
      _dbResult.emit(localDatabaseUseCase.update(fav))
    }
  }
}
