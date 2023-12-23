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
  fun getDetailMovie(id: Int) = movieRepository.getDetailMovie(id)

  fun getDetailTv(id: Int) = movieRepository.getDetailTv(id)

  // for tv
  fun getExternalId(id: Int) = movieRepository.getExternalId(id)

  fun getAllCreditMovies(movieId: Int) = movieRepository.getCreditMovies(movieId)

  fun getAllCreditTv(tvId: Int) = movieRepository.getCreditTv(tvId)

  fun getLinkMovie(movieId: Int) = movieRepository.getVideoMovies(movieId)

  fun getLinkTv(tvId: Int) = movieRepository.getVideoTv(tvId)

  fun getScore(apiKey: String, id: String) = movieRepository.getScoring(apiKey, id)

  fun getScore(id: String) = movieRepository.getDetailOMDb(id)

  fun getCreditsCastMovies() = movieRepository.creditCastMovies

  fun getCreditDirectorMovies() = movieRepository.creditCrewMovies

  fun getCreditsCastTv() = movieRepository.creditCastTv

  fun getCreditDirectorTv() = movieRepository.creditCrewTv

  fun getSnackBarText() = movieRepository.snackBarText

  fun score() = movieRepository.score

  fun getLinkMovie() = movieRepository.linkVideoMovie

  fun getLinkTv() = movieRepository.linkVideoTv

  fun getRecommendationMovie(movieId: Int) = movieRepository.getPagingMovieRecommendation(movieId)
    .cachedIn(viewModelScope).asLiveData()

  fun getRecommendationTv(tvId: Int) = movieRepository.getPagingTvRecommendation(tvId)
    .cachedIn(viewModelScope).asLiveData()

  fun detailOMDb() = movieRepository.detailOMDb

  fun detailTv() = movieRepository.detailTv

  fun detailMovie() = movieRepository.detailMovie

  fun externalId() = movieRepository.externalId

  fun getStatedMovie(sessionId: String, id: Int) = movieRepository.getStatedMovie(sessionId, id)

  fun getStatedTv(sessionId: String, id: Int) = movieRepository.getStatedTv(sessionId, id)

  fun stated() = movieRepository.stated


  // Local DB Function
  fun insertToFavoriteDB(fav: FavoriteDB) = movieRepository.insertDB(fav)

  fun removeFromFavoriteDB(fav: FavoriteDB) = movieRepository.deleteFromDB(fav)

  fun checkIsFavoriteDB(id: Int) = movieRepository.isFavoriteDB(id)

  fun checkIsWatchlistDB(id: Int) = movieRepository.isWatchlist(id)

  fun getIsWatchlist() = movieRepository.isWatchlist

  fun getIsFavorite() = movieRepository.isFavorite

  fun updateToFavoriteDB(id: Int) = movieRepository.updateFavorite(true, id)

  fun updateToRemoveFromFavoriteDB(id: Int) = movieRepository.updateFavorite(false, id)

  fun updateToWatchlist(id: Int) = movieRepository.updateWatchlist(true, id)

  fun updateToRemoveFromWatchlistDB(id: Int) = movieRepository.updateWatchlist(false, id)


  // favorite & watchlist function network
  fun postFavorite(sessionId: String, data: Favorite, userId: Int) =
    movieRepository.postFavorite(sessionId, data, userId)

  fun postWatchlist(sessionId: String, data: Watchlist, userId: Int) =
    movieRepository.postWatchlist(sessionId, data, userId)


  fun postResponse() = movieRepository.postResponse

  fun getLoading() = movieRepository.isLoading
}