package com.waffiq.bazz_movies.ui.activity.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.waffiq.bazz_movies.data.local.datasource.LocalDataSourceInterface.Companion.ERROR_DUPLICATE_ENTRY
import com.waffiq.bazz_movies.data.local.datasource.LocalDataSourceInterface.Companion.ERROR_UNKNOWN
import com.waffiq.bazz_movies.data.local.datasource.LocalDataSourceInterface.Companion.SUCCESS
import com.waffiq.bazz_movies.data.local.model.Favorite
import com.waffiq.bazz_movies.data.local.model.FavoriteDB
import com.waffiq.bazz_movies.data.local.model.Rate
import com.waffiq.bazz_movies.data.local.model.Watchlist
import com.waffiq.bazz_movies.data.repository.MoviesRepository
import com.waffiq.bazz_movies.utils.Event
import com.waffiq.bazz_movies.utils.LocalDatabaseResult

class DetailMovieViewModel(
  private val movieRepository: MoviesRepository,
) : ViewModel() {

  private val _localDatabaseResult = MutableLiveData<Event<LocalDatabaseResult>>()
  val localDatabaseResult: LiveData<Event<LocalDatabaseResult>> get() = _localDatabaseResult

  // Show Data
  fun detailMovie(id: Int) = movieRepository.getDetailMovie(id)
  fun detailMovie() = movieRepository.detailMovie
  fun ageRatingMovie() = movieRepository.ageRatingMovie
  fun getAllCreditMovies(movieId: Int) = movieRepository.getCreditMovies(movieId)
  fun getCreditsCastMovies() = movieRepository.creditCastMovies
  fun getCreditDirectorMovies() = movieRepository.creditCrewMovies
  fun getLinkMovie(movieId: Int) = movieRepository.getVideoMovies(movieId)
  fun getLinkMovie() = movieRepository.linkVideoMovie
  fun getProductionCountry() = movieRepository.productionCountry

  fun detailTv(id: Int) = movieRepository.getDetailTv(id)
  fun detailTv() = movieRepository.detailTv
  fun ageRatingTv() = movieRepository.ageRatingTv
  fun externalId(id: Int) = movieRepository.getExternalId(id)
  fun externalId() = movieRepository.externalId
  fun getAllCreditTv(tvId: Int) = movieRepository.getCreditTv(tvId)
  fun getCreditsCastTv() = movieRepository.creditCastTv
  fun getCreditDirectorTv() = movieRepository.creditCrewTv
  fun getLinkTv(tvId: Int) = movieRepository.getVideoTv(tvId)
  fun getLinkTv() = movieRepository.linkVideoTv

  fun getScoreOMDb(id: String) = movieRepository.getDetailOMDb(id)
  fun detailOMDb() = movieRepository.detailOMDb

  fun getRecommendationMovie(movieId: Int) =
    movieRepository.getPagingMovieRecommendation(movieId).cachedIn(viewModelScope).asLiveData()

  fun getRecommendationTv(tvId: Int) =
    movieRepository.getPagingTvRecommendation(tvId).cachedIn(viewModelScope).asLiveData()

  fun getStatedMovie(sessionId: String, id: Int) = movieRepository.getStatedMovie(sessionId, id)
  fun getStatedTv(sessionId: String, id: Int) = movieRepository.getStatedTv(sessionId, id)
  fun getStated() = movieRepository.stated

  // Local DB Function
  fun isFavoriteDB() = movieRepository.isFavorite
  fun isFavoriteDB(id: Int, mediaType: String) = movieRepository.isFavoriteDB(id, mediaType)
  fun isWatchlistDB(id: Int, mediaType: String) = movieRepository.isWatchlistDB(id, mediaType)
  fun isWatchlistDB() = movieRepository.isWatchlist

  fun insertToDB(fav: FavoriteDB) {
    movieRepository.insertToDB(fav) { resultCode ->
      val result = when (resultCode) {
        ERROR_DUPLICATE_ENTRY -> LocalDatabaseResult.Error("Duplicate entry")
        ERROR_UNKNOWN -> LocalDatabaseResult.Error("Unknown error")
        SUCCESS -> LocalDatabaseResult.Success
        else -> LocalDatabaseResult.Error("Unknown result code: $resultCode")
      }
      _localDatabaseResult.postValue(Event(result))
    }
  }

  fun updateToFavoriteDB(fav: FavoriteDB) = movieRepository.updateFavoriteDB(false, fav)
  fun updateToRemoveFromFavoriteDB(fav: FavoriteDB) = movieRepository.updateFavoriteDB(true, fav)
  fun updateToWatchlistDB(fav: FavoriteDB) = movieRepository.updateWatchlistDB(false, fav)
  fun updateToRemoveFromWatchlistDB(fav: FavoriteDB) = movieRepository.updateWatchlistDB(true, fav)
  fun delFromFavoriteDB(fav: FavoriteDB) = movieRepository.deleteFromDB(fav)

  // favorite & watchlist TMDB
  fun postFavorite(sessionId: String, data: Favorite, userId: Int) =
    movieRepository.postFavorite(sessionId, data, userId)

  fun postWatchlist(sessionId: String, data: Watchlist, userId: Int) =
    movieRepository.postWatchlist(sessionId, data, userId)

  // add rating
  fun postMovieRate(sessionId: String, data: Rate, movieId: Int) =
    movieRepository.postMovieRate(sessionId, data, movieId)

  fun postTvRate(sessionId: String, data: Rate, tvId: Int) =
    movieRepository.postTvRate(sessionId, data, tvId)

  fun getSnackBarText() = movieRepository.snackBarText
  fun getLoading() = movieRepository.isLoading
}