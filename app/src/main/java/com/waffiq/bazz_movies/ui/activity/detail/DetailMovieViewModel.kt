package com.waffiq.bazz_movies.ui.activity.detail

import androidx.lifecycle.ViewModel
import com.waffiq.bazz_movies.data.local.model.Favorite
import com.waffiq.bazz_movies.data.repository.MoviesRepository

class DetailUserViewModel(
  private val movieRepository: MoviesRepository,
) : ViewModel() {

//  private val favoriteRepository: FavoriteRepository = FavoriteRepository(application)

  fun getAllCredits(movieId: Int) = movieRepository.getCredits(movieId)

  fun getCreditsCast() = movieRepository.creditCast

  fun getCreditsDirector() = movieRepository.creditCrew

  fun insertToFavorite(fav: Favorite) = movieRepository.insert(fav)

  fun removeFromFavorite(fav: Favorite) =  movieRepository.delete(fav)

  fun checkIsFavorite(id: Int) = movieRepository.isFavorite(id)
}