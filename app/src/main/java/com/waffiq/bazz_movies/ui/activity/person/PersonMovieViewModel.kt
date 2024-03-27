package com.waffiq.bazz_movies.ui.activity.person

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waffiq.bazz_movies.data.remote.response.tmdb.CastCombinedItem
import com.waffiq.bazz_movies.data.remote.response.tmdb.DetailPersonResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.ExternalIDPersonResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.ProfilesItem
import com.waffiq.bazz_movies.data.repository.MoviesRepository
import com.waffiq.bazz_movies.utils.Status
import kotlinx.coroutines.launch

class PersonMovieViewModel(private val movieRepository: MoviesRepository) : ViewModel() {

  private val _detailPerson = MutableLiveData<DetailPersonResponse>()
  val detailPerson: LiveData<DetailPersonResponse> get() = _detailPerson

  private val _knownFor = MutableLiveData<List<CastCombinedItem>>()
  val knownFor: LiveData<List<CastCombinedItem>> get() = _knownFor

  private val _imagePerson = MutableLiveData<List<ProfilesItem>>()
  val imagePerson: LiveData<List<ProfilesItem>> get() = _imagePerson

  private val _externalIdPerson = MutableLiveData<ExternalIDPersonResponse>()
  val externalIdPerson: LiveData<ExternalIDPersonResponse> get() = _externalIdPerson

  private val _loadingState = MutableLiveData<Boolean>()
  val loadingState: LiveData<Boolean> get() = _loadingState

  fun getDetailPerson(id: Int) {
    viewModelScope.launch {
      movieRepository.getDetailPerson((id)).collect { response ->
        when (response.status) {
          Status.SUCCESS -> {
            _loadingState.value = false
            response.data.let { _detailPerson.value = it }
          }

          Status.LOADING -> _loadingState.value = true
          Status.ERROR -> _loadingState.value = false
        }
      }
    }
  }

  fun getKnownFor(id: Int) {
    viewModelScope.launch {
      movieRepository.getKnownForPerson(id).collect { response ->
        when (response.status) {
          Status.SUCCESS -> response.data.let { _knownFor.value = it?.cast ?: emptyList() }
          Status.LOADING -> {}
          Status.ERROR -> {}
        }
      }
    }
  }

  fun getImagePerson(id: Int) {
    viewModelScope.launch {
      movieRepository.getImagePerson((id)).collect { response ->
        when (response.status) {
          Status.SUCCESS -> response.data.let { _imagePerson.value = it?.profiles ?: emptyList() }
          Status.LOADING -> {}
          Status.ERROR -> {}
        }
      }
    }
  }

  fun getExternalIDPerson(id: Int) {
    viewModelScope.launch {
      movieRepository.getExternalIDPerson(id).collect { response ->
        when (response.status) {
          Status.SUCCESS -> response.data.let { _externalIdPerson.value = it }
          Status.LOADING -> {}
          Status.ERROR -> {}
        }
      }
    }
  }

  fun getSnackbar() = movieRepository.snackBarText

  companion object {
    const val TAG = "PersonMovieViewModel"
  }
}