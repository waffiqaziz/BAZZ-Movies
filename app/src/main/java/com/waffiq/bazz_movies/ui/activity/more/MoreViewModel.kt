package com.waffiq.bazz_movies.ui.activity.more

import androidx.lifecycle.ViewModel
import com.waffiq.bazz_movies.data.repository.MoviesRepository

class MoreViewModel(private val movieRepository: MoviesRepository) : ViewModel() {

  fun deleteAll() = movieRepository.deleteAll()
}