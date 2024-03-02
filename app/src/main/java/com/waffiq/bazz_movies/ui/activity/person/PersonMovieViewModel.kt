package com.waffiq.bazz_movies.ui.activity.person

import androidx.lifecycle.ViewModel
import com.waffiq.bazz_movies.data.repository.MoviesRepository

class PersonMovieViewModel(private val movieRepository: MoviesRepository) : ViewModel() {

  fun getDetailPerson(id: Int) = movieRepository.getDetailPerson((id))
  fun getDetailPerson() = movieRepository.detailPerson

  fun getKnownFor(id: Int) = movieRepository.getKnownForPerson(id)
  fun getKnownFor() = movieRepository.knownFor

  fun getImagePerson(id: Int) = movieRepository.getImagePerson(id)
  fun getImagePerson() = movieRepository.imagePerson

  fun getExternalIDPerson(id: Int) = movieRepository.getExternalIDPerson(id)
  fun getExternalIDPerson() = movieRepository.externalIdPerson

  fun getLoading() = movieRepository.isLoading

  fun getSnackbar() = movieRepository.snackBarText
}