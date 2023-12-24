package com.waffiq.bazz_movies.ui.activity.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.waffiq.bazz_movies.data.local.model.Favorite
import com.waffiq.bazz_movies.data.local.model.FavoriteDB
import com.waffiq.bazz_movies.data.local.model.Watchlist
import com.waffiq.bazz_movies.data.repository.MoviesRepository

class DetailMovieViewModel(
  private val movieRepository: MoviesRepository,
) : ViewModel() {

  // Show Data
  fun detailMovie(id: Int) = movieRepository.getDetailMovie(id)
  fun detailMovie() = movieRepository.detailMovie

  fun detailTv(id: Int) = movieRepository.getDetailTv(id)
  fun detailTv() = movieRepository.detailTv

  fun externalId(id: Int) = movieRepository.getExternalId(id)
  fun externalId() = movieRepository.externalId

  fun scoreIMDbLib(apiKey: String, id: String) = movieRepository.getScoring(apiKey, id)
  fun scoreIMDbLib() = movieRepository.score

  fun getAllCreditMovies(movieId: Int) = movieRepository.getCreditMovies(movieId)
  fun getCreditsCastMovies() = movieRepository.creditCastMovies
  fun getCreditDirectorMovies() = movieRepository.creditCrewMovies
  fun getAllCreditTv(tvId: Int) = movieRepository.getCreditTv(tvId)
  fun getCreditsCastTv() = movieRepository.creditCastTv
  fun getCreditDirectorTv() = movieRepository.creditCrewTv

  fun getLinkMovie(movieId: Int) = movieRepository.getVideoMovies(movieId)
  fun getLinkMovie() = movieRepository.linkVideoMovie

  fun getLinkTv(tvId: Int) = movieRepository.getVideoTv(tvId)
  fun getLinkTv() = movieRepository.linkVideoTv

  fun getScoreOMDb(id: String) = movieRepository.getDetailOMDb(id)
  fun detailOMDb() = movieRepository.detailOMDb

  fun getRecommendationMovie(movieId: Int) = movieRepository.getPagingMovieRecommendation(movieId)
    .cachedIn(viewModelScope).asLiveData()
  fun getRecommendationTv(tvId: Int) = movieRepository.getPagingTvRecommendation(tvId)
    .cachedIn(viewModelScope).asLiveData()

  fun getStatedMovie(sessionId: String, id: Int) = movieRepository.getStatedMovie(sessionId, id)
  fun getStatedTv(sessionId: String, id: Int) = movieRepository.getStatedTv(sessionId, id)
  fun stated() = movieRepository.stated

  // Local DB Function
  fun isFavoriteDB() = movieRepository.isFavorite
  fun isFavoriteDB(id: Int) = movieRepository.isFavoriteDB(id)
  fun isWatchlistDB(id: Int) = movieRepository.isWatchlistDB(id)
  fun isWatchlistDB() = movieRepository.isWatchlist

  fun insertToFavoriteDB(fav: FavoriteDB) = movieRepository.insertDB(fav)
  fun updateToFavoriteDB(id: Int) = movieRepository.updateFavoriteDB(true, id)
  fun updateToRemoveFromFavoriteDB(id: Int) = movieRepository.updateFavoriteDB(false, id)
  fun updateToWatchlistDB(id: Int) = movieRepository.updateWatchlistDB(true, id)
  fun updateToRemoveFromWatchlistDB(id: Int) = movieRepository.updateWatchlistDB(false, id)
  fun removeFromFavoriteDB(fav: FavoriteDB) = movieRepository.deleteFromDB(fav)

  // favorite & watchlist TMDB
  fun postFavorite(sessionId: String, data: Favorite, userId: Int) =
    movieRepository.postFavorite(sessionId, data, userId)
  fun postWatchlist(sessionId: String, data: Watchlist, userId: Int) =
    movieRepository.postWatchlist(sessionId, data, userId)

  fun postResponse() = movieRepository.postResponse

  fun getSnackBarText() = movieRepository.snackBarText
  fun getLoading() = movieRepository.isLoading
}