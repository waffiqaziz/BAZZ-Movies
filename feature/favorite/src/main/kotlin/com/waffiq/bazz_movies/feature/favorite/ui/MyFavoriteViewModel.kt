package com.waffiq.bazz_movies.feature.favorite.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.data.ResultItem
import com.waffiq.bazz_movies.core.database.data.model.Favorite
import com.waffiq.bazz_movies.core.database.domain.usecase.local_database.LocalDatabaseUseCase
import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.movie.domain.usecase.getstated.GetStatedMovieUseCase
import com.waffiq.bazz_movies.core.movie.domain.usecase.getstated.GetStatedTvUseCase
import com.waffiq.bazz_movies.core.movie.domain.usecase.postmethod.PostMethodUseCase
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers.SnackBarUserLoginData
import com.waffiq.bazz_movies.core.network.data.remote.post_body.FavoritePostModel
import com.waffiq.bazz_movies.core.network.data.remote.post_body.WatchlistPostModel
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import com.waffiq.bazz_movies.core.user.data.model.UserModel
import com.waffiq.bazz_movies.feature.favorite.domain.usecase.GetFavoriteMovieUseCase
import com.waffiq.bazz_movies.feature.favorite.domain.usecase.GetFavoriteTvUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyFavoriteViewModel @Inject constructor(
  private val getFavoriteMovieUseCase: GetFavoriteMovieUseCase,
  private val getFavoriteTvUseCase: GetFavoriteTvUseCase,
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
  val favoriteTvFromDB =
    localDatabaseUseCase.favoriteTvFromDB.asLiveData().distinctUntilChanged()
  val favoriteMoviesFromDB =
    localDatabaseUseCase.favoriteMoviesFromDB.asLiveData().distinctUntilChanged()

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
  fun favoriteMovies(sesId: String): Flow<PagingData<ResultItem>> =
    getFavoriteMovieUseCase.getPagingFavoriteMovies(sesId).cachedIn(viewModelScope)

  fun favoriteTvSeries(sesId: String): Flow<PagingData<ResultItem>> =
    getFavoriteTvUseCase.getPagingFavoriteTv(sesId).cachedIn(viewModelScope)

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

  fun checkStatedThenPostWatchlist(
    mediaType: String,
    user: UserModel,
    id: Int,
    title: String
  ) {
    viewModelScope.launch {
      if (mediaType == MOVIE_MEDIA_TYPE) {
        getStatedMovieUseCase.getStatedMovie(user.token, id).collect { networkResult ->
          when (networkResult) {
            is NetworkResult.Success -> {
              if (networkResult.data.watchlist) {
                _snackBarAlready.value = Event(title)
              } else {
                postWatchlist(
                  user.token,
                  user.userId,
                  WatchlistPostModel(mediaType, id, true),
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
              if (networkResult.data.watchlist) {
                _snackBarAlready.value = Event(title)
              } else {
                postWatchlist(
                  user.token,
                  user.userId,
                  WatchlistPostModel(mediaType, id, true),
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
