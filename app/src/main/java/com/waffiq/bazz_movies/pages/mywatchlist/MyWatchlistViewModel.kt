package com.waffiq.bazz_movies.pages.mywatchlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.waffiq.bazz_movies.core.data.local.model.UserModel
import com.waffiq.bazz_movies.core.data.remote.post_body.FavoritePostModel
import com.waffiq.bazz_movies.core.data.remote.post_body.WatchlistPostModel
import com.waffiq.bazz_movies.core.domain.model.Favorite
import com.waffiq.bazz_movies.core.domain.model.ResultItem
import com.waffiq.bazz_movies.core.domain.usecase.get_stated.GetStatedMovieUseCase
import com.waffiq.bazz_movies.core.domain.usecase.get_stated.GetStatedTvUseCase
import com.waffiq.bazz_movies.core.domain.usecase.get_watchlist.GetWatchlistMovieUseCase
import com.waffiq.bazz_movies.core.domain.usecase.get_watchlist.GetWatchlistTvUseCase
import com.waffiq.bazz_movies.core.domain.usecase.local_database.LocalDatabaseUseCase
import com.waffiq.bazz_movies.core.domain.usecase.post_method.PostMethodUseCase
import com.waffiq.bazz_movies.core.utils.common.Event
import com.waffiq.bazz_movies.core.utils.result.DbResult
import com.waffiq.bazz_movies.core.utils.result.NetworkResult
import com.waffiq.bazz_movies.core.utils.result.SnackBarUserLoginData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyWatchlistViewModel @Inject constructor(
  private val getWatchlistMovieUseCase: GetWatchlistMovieUseCase,
  private val getWatchlistTvUseCase: GetWatchlistTvUseCase,
  private val postMethodUseCase: PostMethodUseCase,
  private val localDatabaseUseCase: LocalDatabaseUseCase,
  private val getStatedMovieUseCase: GetStatedMovieUseCase,
  private val getStatedTvUseCase: GetStatedTvUseCase
) : ViewModel() {

  private val _dbResult = MutableLiveData<Event<DbResult<Int>>>()
  val dbResult: LiveData<Event<DbResult<Int>>> get() = _dbResult

  private val _undoDB = MutableLiveData<Event<Favorite>>()
  val undoDB: LiveData<Event<Favorite>> = _undoDB

  private val _snackBarAlready = MutableLiveData<Event<String>>()
  val snackBarAlready: LiveData<Event<String>> = _snackBarAlready

  private val _snackBarAdded = MutableLiveData<Event<SnackBarUserLoginData>>()
  val snackBarAdded: LiveData<Event<SnackBarUserLoginData>> = _snackBarAdded

  // region LOCAL DATABASE
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
// endregion LOCAL DATABASE

  // region NETWORK
  fun watchlistMovies(sesId: String): Flow<PagingData<ResultItem>> =
    getWatchlistMovieUseCase.getPagingWatchlistMovies(sesId).cachedIn(viewModelScope)

  fun watchlistTvSeries(sesId: String): Flow<PagingData<ResultItem>> =
    getWatchlistTvUseCase.getPagingWatchlistTv(sesId).cachedIn(viewModelScope)

  fun postFavorite(sesId: String, userId: Int, data: FavoritePostModel, title: String) {
    viewModelScope.launch {
      postMethodUseCase.postFavorite(sesId, data, userId).collect { networkResult ->
        when (networkResult) {
          is NetworkResult.Success ->
            _snackBarAdded.value =
              Event(SnackBarUserLoginData(true, title, data, null))

          is NetworkResult.Error ->
            _snackBarAdded.value =
              Event(SnackBarUserLoginData(false, networkResult.message, null, null))

          is NetworkResult.Loading -> {}
        }
      }
    }
  }

  fun postWatchlist(sesId: String, userId: Int, data: WatchlistPostModel, title: String) {
    viewModelScope.launch {
      postMethodUseCase.postWatchlist(sesId, data, userId).collect { networkResult ->
        when (networkResult) {
          is NetworkResult.Success ->
            _snackBarAdded.value =
              Event(SnackBarUserLoginData(true, title, null, data))

          is NetworkResult.Error ->
            _snackBarAdded.value =
              Event(SnackBarUserLoginData(false, networkResult.message, null, null))

          is NetworkResult.Loading -> {}
        }
      }
    }
  }

  fun checkStatedThenPostFavorite(
    mediaType: String,
    user: UserModel,
    id: Int,
    title: String
  ) {
    viewModelScope.launch {
      if (mediaType == "movie") {
        getStatedMovieUseCase.getStatedMovie(user.token, id).collect { networkResult ->
          when (networkResult) {
            is NetworkResult.Success -> {
              if (networkResult.data.favorite) {
                _snackBarAlready.value = Event(title)
              } else {
                postFavorite(
                  user.token,
                  user.userId,
                  FavoritePostModel(mediaType, id, true),
                  title
                )
              }
            }

            is NetworkResult.Loading -> {}
            is NetworkResult.Error ->
              _snackBarAdded.value =
                Event(SnackBarUserLoginData(false, networkResult.message, null, null))
          }
        }
      } else {
        getStatedTvUseCase.getStatedTv(user.token, id).collect { networkResult ->
          when (networkResult) {
            is NetworkResult.Success -> {
              if (networkResult.data.favorite) {
                _snackBarAlready.value = Event(title)
              } else {
                postFavorite(
                  user.token,
                  user.userId,
                  FavoritePostModel(mediaType, id, true),
                  title
                )
              }
            }

            is NetworkResult.Loading -> {}
            is NetworkResult.Error ->
              _snackBarAdded.value =
                Event(SnackBarUserLoginData(false, networkResult.message, null, null))
          }
        }
      }
    }
  }
  // endregion NETWORK
}
