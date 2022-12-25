package com.waffiq.bazz_movies.ui.activity.home

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.waffiq.bazz_movies.data.local.model.Movie
import com.waffiq.bazz_movies.data.repository.MoviesRepository

class HomeViewModel(
  private val movieRepository: MoviesRepository
) : ViewModel() {

  private val _text = MutableLiveData<String>().apply {
    value = "This is home Fragment"
  }

  val text: LiveData<String> = _text

  fun getFirstMovieNowPlaying(): LiveData<List<Movie>> {
    return movieRepository.getMovieNowPlaying()
  }

  fun getTrending() = movieRepository.getPagingTrending().cachedIn(viewModelScope).asLiveData()

  fun getUpcomingMovies() = movieRepository.getPagingUpComingMovies().cachedIn(viewModelScope).asLiveData()
}