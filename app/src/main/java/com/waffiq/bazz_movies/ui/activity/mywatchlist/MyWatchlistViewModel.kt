package com.waffiq.bazz_movies.ui.activity.mywatchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.waffiq.bazz_movies.data.repository.MoviesRepository

class MyWatchlistViewModel(private val movieRepository: MoviesRepository) : ViewModel() {

  // db
  val getWatchlistMoviesDB = movieRepository.getWatchlistMovieFromDB()

  val getWatchlistTvSeriesDB = movieRepository.getWatchlistTvFromDB()

  // from network
  fun getWatchlistMovies(sessionId: String) =
    movieRepository.getPagingWatchlistMovies(sessionId).cachedIn(viewModelScope).asLiveData()

  fun getWatchlistTvSeries(sessionId: String) =
    movieRepository.getPagingWatchlistTv(sessionId).cachedIn(viewModelScope).asLiveData()
}