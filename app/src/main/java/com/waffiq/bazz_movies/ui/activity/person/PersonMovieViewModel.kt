package com.waffiq.bazz_movies.ui.activity.person

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waffiq.bazz_movies.domain.model.person.CastItem
import com.waffiq.bazz_movies.domain.model.person.DetailPerson
import com.waffiq.bazz_movies.domain.model.person.ExternalIDPerson
import com.waffiq.bazz_movies.domain.model.person.ProfilesItem
import com.waffiq.bazz_movies.domain.usecase.get_detail_person.GetDetailPersonUseCase
import com.waffiq.bazz_movies.utils.Status
import com.waffiq.bazz_movies.utils.common.Event
import kotlinx.coroutines.launch

class PersonMovieViewModel(
  private val getDetailPersonUseCase: GetDetailPersonUseCase
) : ViewModel() {

  private val _detailPerson = MutableLiveData<DetailPerson>()
  val detailPerson: LiveData<DetailPerson> get() = _detailPerson

  private val _knownFor = MutableLiveData<List<CastItem>>()
  val knownFor: LiveData<List<CastItem>> get() = _knownFor

  private val _imagePerson = MutableLiveData<List<ProfilesItem>>()
  val imagePerson: LiveData<List<ProfilesItem>> get() = _imagePerson

  private val _externalIdPerson = MutableLiveData<ExternalIDPerson>()
  val externalIdPerson: LiveData<ExternalIDPerson> get() = _externalIdPerson

  private val _errorState = MutableLiveData<Event<String>>()
  val errorState: LiveData<Event<String>> get() = _errorState

  private val _loadingState = MutableLiveData<Boolean>()
  val loadingState: LiveData<Boolean> get() = _loadingState

  fun getDetailPerson(id: Int) {
    viewModelScope.launch {
      getDetailPersonUseCase.getDetailPerson((id)).collect { networkResult ->
        when (networkResult.status) {
          Status.SUCCESS -> {
            _loadingState.value = false
            networkResult.data.let { _detailPerson.value = it }
          }

          Status.LOADING -> _loadingState.value = true
          Status.ERROR -> {
            _loadingState.value = false
            _errorState.value = Event(networkResult.message.toString())
          }
        }
      }
    }
  }

  fun getKnownFor(id: Int) {
    viewModelScope.launch {
      getDetailPersonUseCase.getKnownForPerson(id).collect { networkResult ->
        when (networkResult.status) {
          Status.SUCCESS -> networkResult.data.let { _knownFor.value = it }
          Status.LOADING -> {}
          Status.ERROR -> {
            _loadingState.value = false
            _errorState.value = Event(networkResult.message.toString())
          }
        }
      }
    }
  }

  fun getImagePerson(id: Int) {
    viewModelScope.launch {
      getDetailPersonUseCase.getImagePerson((id)).collect { networkResult ->
        when (networkResult.status) {
          Status.SUCCESS -> networkResult.data.let { _imagePerson.value = it?.profiles ?: emptyList() }
          Status.LOADING -> {}
          Status.ERROR -> {
            _loadingState.value = false
            _errorState.value = Event(networkResult.message.toString())
          }
        }
      }
    }
  }

  fun getExternalIDPerson(id: Int) {
    viewModelScope.launch {
      getDetailPersonUseCase.getExternalIDPerson(id).collect { networkResult ->
        when (networkResult.status) {
          Status.SUCCESS -> networkResult.data.let { _externalIdPerson.value = it }
          Status.LOADING -> {}
          Status.ERROR -> {
            _loadingState.value = false
            _errorState.value = Event(networkResult.message.toString())
          }
        }
      }
    }
  }

  companion object {
    const val TAG = "PersonMovieViewModel"
  }
}