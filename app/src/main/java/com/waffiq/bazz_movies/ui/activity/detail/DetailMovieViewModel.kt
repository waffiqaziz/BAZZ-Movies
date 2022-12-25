package com.waffiq.bazz_movies.ui.activity.detail

import androidx.lifecycle.ViewModel
import com.waffiq.bazz_movies.data.repository.MoviesRepository

class DetailUserViewModel(
  private val movieRepository: MoviesRepository,
) : ViewModel() {

//  private val favoriteRepository: FavoriteRepository = FavoriteRepository(application)

  fun getAllCredits(movieId: Int) = movieRepository.getCredits(movieId)

  fun getCreditsCast() = movieRepository.creditCast

  fun getCreditsDirector() = movieRepository.creditCrew


//  fun insertToFavorite(fav: Favorite) {
//    viewModelScope.launch {
//      favoriteRepository.insert(fav)
//    }
//  }
//
//  fun removeFromFavorite(fav: Favorite) {
//    viewModelScope.launch {
//      favoriteRepository.delete(fav)
//    }
//  }
//
//  suspend fun checkIsFavorite(id: Int) = favoriteRepository.isFavorite(id)
}