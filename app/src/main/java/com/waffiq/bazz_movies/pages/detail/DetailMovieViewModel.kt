package com.waffiq.bazz_movies.pages.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.waffiq.bazz_movies.core.data.remote.post_body.FavoritePostModel
import com.waffiq.bazz_movies.core.data.remote.post_body.RatePostModel
import com.waffiq.bazz_movies.core.data.remote.post_body.WatchlistPostModel
import com.waffiq.bazz_movies.core.domain.model.Favorite
import com.waffiq.bazz_movies.core.domain.model.ResultItem
import com.waffiq.bazz_movies.core.domain.model.Stated
import com.waffiq.bazz_movies.core.domain.model.detail.DetailMovieTvUsed
import com.waffiq.bazz_movies.core.domain.model.detail.MovieTvCredits
import com.waffiq.bazz_movies.core.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.core.domain.usecase.get_detail_movie.GetDetailMovieUseCase
import com.waffiq.bazz_movies.core.domain.usecase.get_detail_omdb.GetDetailOMDbUseCase
import com.waffiq.bazz_movies.core.domain.usecase.get_detail_tv.GetDetailTvUseCase
import com.waffiq.bazz_movies.core.domain.usecase.get_stated.GetStatedMovieUseCase
import com.waffiq.bazz_movies.core.domain.usecase.get_stated.GetStatedTvUseCase
import com.waffiq.bazz_movies.core.domain.usecase.local_database.LocalDatabaseUseCase
import com.waffiq.bazz_movies.core.domain.usecase.post_method.PostMethodUseCase
import com.waffiq.bazz_movies.core.utils.common.Event
import com.waffiq.bazz_movies.core.utils.mappers.DatabaseMapper.favFalseWatchlistTrue
import com.waffiq.bazz_movies.core.utils.mappers.DatabaseMapper.favTrueWatchlistFalse
import com.waffiq.bazz_movies.core.utils.mappers.DatabaseMapper.favTrueWatchlistTrue
import com.waffiq.bazz_movies.core.utils.result.DbResult
import com.waffiq.bazz_movies.core.utils.result.NetworkResult
import com.waffiq.bazz_movies.core.domain.model.PostModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailMovieViewModel @Inject constructor(
  private val getDetailMovieUseCase: GetDetailMovieUseCase,
  private val getDetailTvUseCase: GetDetailTvUseCase,
  private val localDatabaseUseCase: LocalDatabaseUseCase,
  private val postMethodUseCase: PostMethodUseCase,
  private val getDetailOMDbUseCase: GetDetailOMDbUseCase,
  private val getStatedMovieUseCase: GetStatedMovieUseCase,
  private val getStatedTvUseCase: GetStatedTvUseCase
) : ViewModel() {

  // region OBSERVABLES
  private val _isFavorite = MutableLiveData<Boolean>()
  val isFavorite: LiveData<Boolean> = _isFavorite

  private val _isWatchlist = MutableLiveData<Boolean>()
  val isWatchlist: LiveData<Boolean> = _isWatchlist

  private val _itemState = MutableLiveData<Stated>()
  val itemState: LiveData<Stated> get() = _itemState

  private val _movieTvCreditsResult = MutableLiveData<MovieTvCredits>()
  val movieTvCreditsResult: LiveData<MovieTvCredits> get() = _movieTvCreditsResult

  private val _omdbResult = MutableLiveData<OMDbDetails>()
  val omdbResult: LiveData<OMDbDetails> get() = _omdbResult

  private val _loadingState = MutableLiveData<Boolean>()
  val loadingState: LiveData<Boolean> get() = _loadingState

  private val _errorState = MutableSharedFlow<String>(replay = 0)
  val errorState: SharedFlow<String> get() = _errorState

  private val _rateState = MutableLiveData<Event<Boolean>>()
  val rateState: LiveData<Event<Boolean>> get() = _rateState

  private val _postModelState = MutableLiveData<Event<PostModelState>>()
  val postModelState: LiveData<Event<PostModelState>> get() = _postModelState

  private var _linkVideo = MutableLiveData<String>()
  val linkVideo: LiveData<String> = _linkVideo

  private val _detailMovieTv = MutableLiveData<DetailMovieTvUsed>()
  val detailMovieTv: LiveData<DetailMovieTvUsed> get() = _detailMovieTv

  private val _tvImdbID = MutableLiveData<String>()
  val tvImdbID: LiveData<String> get() = _tvImdbID

  private val _recommendation = MutableLiveData<PagingData<ResultItem>>()
  val recommendation: LiveData<PagingData<ResultItem>> get() = _recommendation
  // endregion OBSERVABLES

  // region MOVIE
  fun getLinkVideoMovie(movieId: Int) {
    viewModelScope.launch {
      getDetailMovieUseCase.getLinkVideoMovies(movieId).collect { networkResult ->
        when (networkResult) {
          is NetworkResult.Success -> networkResult.data.let { _linkVideo.value = it }
          is NetworkResult.Loading -> {}
          is NetworkResult.Error -> {
            _loadingState.value = false
            _errorState.emit(networkResult.message)
          }
        }
      }
    }
  }

  fun detailMovie(id: Int, userRegion: String) {
    viewModelScope.launch {
      getDetailMovieUseCase.getDetailMovie(id, userRegion).collect { networkResult ->
        when (networkResult) {
          is NetworkResult.Success -> networkResult.data.let { _detailMovieTv.value = it }
          is NetworkResult.Loading -> {}
          is NetworkResult.Error -> {
            _loadingState.value = false
            _errorState.emit(networkResult.message)
          }
        }
      }
    }
  }

  fun getMovieCredits(movieId: Int) {
    viewModelScope.launch {
      getDetailMovieUseCase.getCreditMovies(movieId).collect { networkResult ->
        when (networkResult) {
          is NetworkResult.Success -> networkResult.data.let { _movieTvCreditsResult.value = it }
          is NetworkResult.Loading -> {}
          is NetworkResult.Error -> {
            _loadingState.value = false
            _errorState.emit(networkResult.message)
          }
        }
      }
    }
  }

  fun getRecommendationMovie(movieId: Int) {
    viewModelScope.launch {
      getDetailMovieUseCase.getPagingMovieRecommendation(movieId).collectLatest {
        _recommendation.value = it
      }
    }
  }

  fun getStatedMovie(sessionId: String, id: Int) {
    viewModelScope.launch {
      getStatedMovieUseCase.getStatedMovie(sessionId, id).collect { networkResult ->
        when (networkResult) {
          is NetworkResult.Success -> networkResult.data.let { _itemState.value = it }
          is NetworkResult.Loading -> {}
          is NetworkResult.Error -> {
            _loadingState.value = false
            _errorState.emit(networkResult.message)
          }
        }
      }
    }
  }
  // endregion MOVIE

  // region TV-SERIES
  private fun getLinkTv(tvId: Int) {
    viewModelScope.launch {
      getDetailTvUseCase.getTrailerLinkTv(tvId).collect { networkResult ->
        when (networkResult) {
          is NetworkResult.Success -> networkResult.data.let { _linkVideo.value = it }
          is NetworkResult.Loading -> {}
          is NetworkResult.Error -> {
            _loadingState.value = false
            _errorState.emit(networkResult.message)
          }
        }
      }
    }
  }

  fun detailTv(id: Int, userRegion: String) {
    viewModelScope.launch {
      getDetailTvUseCase.getDetailTv(id, userRegion).collect { networkResult ->
        when (networkResult) {
          is NetworkResult.Success -> networkResult.data.let { _detailMovieTv.value = it }
          is NetworkResult.Loading -> {}
          is NetworkResult.Error -> {
            _loadingState.value = false
            _errorState.emit(networkResult.message)
          }
        }
      }
    }
  }

  fun getTvCredits(tvId: Int) {
    viewModelScope.launch {
      getDetailTvUseCase.getCreditTv(tvId).collect { networkResult ->
        when (networkResult) {
          is NetworkResult.Success -> networkResult.data.let { _movieTvCreditsResult.value = it }
          is NetworkResult.Loading -> {}
          is NetworkResult.Error -> {
            _loadingState.value = false
            _errorState.emit(networkResult.message)
          }
        }
      }
    }
  }

  fun getImdbVideoTv(id: Int) {
    viewModelScope.launch {
      getDetailTvUseCase.getExternalTvId(id).collect { networkResult ->
        when (networkResult) {
          is NetworkResult.Success -> {
            networkResult.data.imdbId.let { _tvImdbID.value = it }
            networkResult.data.let {
              if (it.imdbId != null) {
                getScoreOMDb(it.imdbId)
                getLinkTv(id)
              }
            }
          }

          is NetworkResult.Loading -> {}
          is NetworkResult.Error -> {
            _errorState.emit(networkResult.message)
          }
        }
      }
    }
  }

  fun getRecommendationTv(tvId: Int): LiveData<PagingData<ResultItem>> =
    getDetailTvUseCase.getPagingTvRecommendation(tvId).cachedIn(viewModelScope).asLiveData()

  fun getStatedTv(sessionId: String, id: Int) {
    viewModelScope.launch {
      getStatedTvUseCase.getStatedTv(sessionId, id).collect { networkResult ->
        when (networkResult) {
          is NetworkResult.Success -> networkResult.data.let { _itemState.value = it }
          is NetworkResult.Loading -> {}
          is NetworkResult.Error -> {
            _loadingState.value = false
            _errorState.emit(networkResult.message)
          }
        }
      }
    }
  }
  // endregion TV-SERIES

  fun getScoreOMDb(imdbId: String) {
    viewModelScope.launch {
      getDetailOMDbUseCase.getDetailOMDb(imdbId).collect { networkResult ->
        when (networkResult) {
          is NetworkResult.Success -> {
            networkResult.data.let { _omdbResult.value = it }
            _loadingState.value = false
          }

          is NetworkResult.Loading -> {}
          is NetworkResult.Error -> {
            _loadingState.value = false
            _errorState.emit(networkResult.message)
          }
        }
      }
    }
  }

  // region DB FUNCTION
  fun handleBtnFavorite(favorite: Boolean, watchlist: Boolean, data: ResultItem) {
    when {
      // If in the watchlist but not a favorite, update to set as favorite.
      !favorite && watchlist -> updateToFavoriteDB(favTrueWatchlistTrue(data))

      // If neither a favorite nor in the watchlist, insert item with favorite set to true.
      !favorite && !watchlist -> insertToDB(favTrueWatchlistFalse(data))

      // If both a favorite and in the watchlist, update to remove as a favorite.
      favorite && watchlist -> updateToRemoveFromFavoriteDB(favFalseWatchlistTrue(data))

      // If not a favorite, remove it from the database.
      else -> delFromFavoriteDB(favTrueWatchlistFalse(data))
    }
  }

  fun handleBtnWatchlist(favorite: Boolean, watchlist: Boolean, data: ResultItem) {
    when {
      // If marked as a favorite but not in the watchlist, update watchlist to true.
      favorite && !watchlist -> updateToWatchlistDB(favTrueWatchlistTrue(data))

      // If it's neither a favorite nor in the watchlist, insert the item and set watchlist to true.
      !favorite && !watchlist -> insertToDB(favFalseWatchlistTrue(data))

      // If both favorite and in the watchlist, update watchlist to false.
      favorite && watchlist -> updateToRemoveFromWatchlistDB(favTrueWatchlistFalse(data))

      // If not a favorite, remove it from favorites.
      else -> delFromFavoriteDB(favFalseWatchlistTrue(data))
    }
  }

  fun isFavoriteDB(id: Int, mediaType: String) {
    viewModelScope.launch {
      when (val result = localDatabaseUseCase.isFavoriteDB(id, mediaType)) {
        is DbResult.Success -> result.data.let { _isFavorite.value = it }
        is DbResult.Error -> _errorState.emit(result.errorMessage)
      }
    }
  }

  fun isWatchlistDB(id: Int, mediaType: String) {
    viewModelScope.launch {
      when (val result = localDatabaseUseCase.isWatchlistDB(id, mediaType)) {
        is DbResult.Success -> result.data.let { _isWatchlist.value = it }
        is DbResult.Error -> _errorState.emit(result.errorMessage)
      }
    }
  }

  fun insertToDB(fav: Favorite) {
    viewModelScope.launch {
      when (val result = localDatabaseUseCase.insertToDB(fav)) {
        is DbResult.Error -> _errorState.emit(result.errorMessage)
        is DbResult.Success -> {
          if (fav.isFavorite) {
            _isFavorite.value = true
          } else if (fav.isWatchlist) _isWatchlist.value = true
          _postModelState.value = Event(
            PostModelState(
              isSuccess = true,
              isDelete = false,
              isFavorite = fav.isFavorite,
            )
          )
        }
      }
    }
  }

  private fun updateToFavoriteDB(fav: Favorite) = viewModelScope.launch {
    when (val result = localDatabaseUseCase.updateFavoriteItemDB(false, fav)) {
      is DbResult.Error -> _errorState.emit(result.errorMessage)
      is DbResult.Success -> {
        _isFavorite.value = true
        _postModelState.value = Event(
          PostModelState(
            isSuccess = true,
            isDelete = false,
            isFavorite = true
          )
        )
      }
    }
  }

  private fun updateToRemoveFromFavoriteDB(fav: Favorite) = viewModelScope.launch {
    when (val result = localDatabaseUseCase.updateFavoriteItemDB(true, fav)) {
      is DbResult.Error -> _errorState.emit(result.errorMessage)
      is DbResult.Success -> {
        _isFavorite.value = false
        _postModelState.value = Event(
          PostModelState(
            isSuccess = true,
            isDelete = true,
            isFavorite = true,
          )
        )
      }
    }
  }

  private fun updateToWatchlistDB(fav: Favorite) = viewModelScope.launch {
    when (val result = localDatabaseUseCase.updateWatchlistItemDB(false, fav)) {
      is DbResult.Error -> _errorState.emit(result.errorMessage)
      is DbResult.Success -> {
        _isWatchlist.value = true
        _postModelState.value = Event(
          PostModelState(
            isSuccess = true,
            isDelete = false,
            isFavorite = false
          )
        )
      }
    }
  }

  private fun updateToRemoveFromWatchlistDB(fav: Favorite) = viewModelScope.launch {
    when (val result = localDatabaseUseCase.updateWatchlistItemDB(true, fav)) {
      is DbResult.Error -> _errorState.emit(result.errorMessage)
      is DbResult.Success -> {
        _isWatchlist.value = false
        _postModelState.value = Event(
          PostModelState(
            isSuccess = true,
            isDelete = true,
            isFavorite = false
          )
        )
      }
    }
  }

  private fun delFromFavoriteDB(fav: Favorite) = viewModelScope.launch {
    when (val result = localDatabaseUseCase.deleteFromDB(fav)) {
      is DbResult.Error -> _errorState.emit(result.errorMessage)
      is DbResult.Success -> {
        _isFavorite.value = false
        _isWatchlist.value = false
        _postModelState.value = Event(
          PostModelState(
            isSuccess = true,
            isDelete = true,
            isFavorite = fav.isFavorite
          )
        )
      }
    }
  }
  // endregion DB FUNCTION

  // region POST FAVORITE, WATCHLIST, RATE
  fun postFavorite(sessionId: String, data: FavoritePostModel, userId: Int) {
    viewModelScope.launch {
      postMethodUseCase.postFavorite(sessionId, data, userId).collect { networkResult ->
        when (networkResult) {
          is NetworkResult.Success -> {
            if (data.mediaType == "movie") {
              getStatedMovie(sessionId, data.mediaId)
            } else {
              getStatedTv(sessionId, data.mediaId)
            }
            _postModelState.value = Event(
              PostModelState(
                isSuccess = true,
                isDelete = !data.favorite,
                isFavorite = true,
              )
            )
            _loadingState.value = false
          }

          is NetworkResult.Loading -> _loadingState.value = true
          is NetworkResult.Error -> {
            _postModelState.value = Event(
              PostModelState(
                isSuccess = false,
                isDelete = !data.favorite,
                isFavorite = true,
              )
            )
            data.favorite.let { _isFavorite.value = it }
            _errorState.emit(networkResult.message)
            _loadingState.value = false
          }
        }
      }
    }
  }

  fun postWatchlist(sessionId: String, data: WatchlistPostModel, userId: Int) {
    viewModelScope.launch {
      postMethodUseCase.postWatchlist(sessionId, data, userId).collect { networkResult ->
        when (networkResult) {
          is NetworkResult.Success -> {
            if (data.mediaType == "movie") {
              getStatedMovie(sessionId, data.mediaId)
            } else {
              getStatedTv(sessionId, data.mediaId)
            }
            _postModelState.value = Event(
              PostModelState(
                isSuccess = true,
                isDelete = !data.watchlist,
                isFavorite = false
              )
            )
            data.watchlist.let { _isWatchlist.value = it }
            _loadingState.value = false
          }

          is NetworkResult.Loading -> _loadingState.value = true
          is NetworkResult.Error -> {
            _postModelState.value = Event(
              PostModelState(
                isSuccess = false,
                isDelete = !data.watchlist,
                isFavorite = false
              )
            )
            _errorState.emit(networkResult.message)
            _loadingState.value = false
          }
        }
      }
    }
  }

  fun postMovieRate(sessionId: String, data: RatePostModel, movieId: Int) {
    viewModelScope.launch {
      postMethodUseCase.postMovieRate(sessionId, data, movieId).collect { networkResult ->
        when (networkResult) {
          is NetworkResult.Success -> {
            _rateState.value = Event(true)
            _loadingState.value = false
          }

          is NetworkResult.Loading -> _loadingState.value = true
          is NetworkResult.Error -> {
            _loadingState.value = false
            _rateState.value = Event(false)
            _errorState.emit(networkResult.message)
          }
        }
      }
    }
  }

  fun postTvRate(sessionId: String, data: RatePostModel, tvId: Int) {
    viewModelScope.launch {
      postMethodUseCase.postTvRate(sessionId, data, tvId).collect { networkResult ->
        when (networkResult) {
          is NetworkResult.Success -> {
            _rateState.value = Event(true)
            _loadingState.value = false
          }

          is NetworkResult.Loading -> _loadingState.value = true
          is NetworkResult.Error -> {
            _loadingState.value = false
            _rateState.value = Event(false)
            _errorState.emit(networkResult.message)
          }
        }
      }
    }
  }
  // endregion POST FAVORITE, WATCHLIST, RATE
}
