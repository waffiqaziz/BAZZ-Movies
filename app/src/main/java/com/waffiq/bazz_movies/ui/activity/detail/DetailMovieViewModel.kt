package com.waffiq.bazz_movies.ui.activity.detail

import androidx.lifecycle.ViewModel
import com.waffiq.bazz_movies.data.local.model.Favorite
import com.waffiq.bazz_movies.data.repository.MoviesRepository

class DetailUserViewModel(
  private val movieRepository: MoviesRepository,
) : ViewModel() {

  fun getAllCreditMovies(movieId: Int) = movieRepository.getCreditMovies(movieId)

  fun getAllCreditTv(tvId: Int) = movieRepository.getCreditTv(tvId)

  fun getCreditsCastMovies() = movieRepository.creditCastMovies

  fun getCreditDirectorMovies() = movieRepository.creditCrewMovies

  fun getCreditsCastTv() = movieRepository.creditCastTv

  fun getCreditDirectorTv() = movieRepository.creditCrewTv

  fun insertToFavorite(fav: Favorite) = movieRepository.insert(fav)

  fun removeFromFavorite(fav: Favorite) =  movieRepository.delete(fav)

  fun checkIsFavorite(id: Int) = movieRepository.isFavorite(id)
}