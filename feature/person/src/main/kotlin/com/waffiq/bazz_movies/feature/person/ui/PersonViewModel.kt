package com.waffiq.bazz_movies.feature.person.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.feature.person.domain.model.CastItem
import com.waffiq.bazz_movies.feature.person.domain.model.DetailPerson
import com.waffiq.bazz_movies.feature.person.domain.model.ExternalIDPerson
import com.waffiq.bazz_movies.feature.person.domain.model.ProfilesItem
import com.waffiq.bazz_movies.feature.person.domain.usecase.GetDetailPersonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonViewModel @Inject constructor(
  private val getDetailPersonUseCase: GetDetailPersonUseCase,
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
      getDetailPersonUseCase.getDetailPerson((id)).collect { outcome ->
        when (outcome) {
          is Outcome.Success -> {
            _loadingState.value = false
            outcome.data.let { _detailPerson.value = it }
          }

          is Outcome.Loading -> _loadingState.value = true
          is Outcome.Error -> {
            _loadingState.value = false
            _errorState.value = Event(outcome.message)
          }
        }
      }
    }
  }

  fun getKnownFor(id: Int) {
    viewModelScope.launch {
      getDetailPersonUseCase.getKnownForPerson(id).collect { outcome ->
        when (outcome) {
          is Outcome.Success -> outcome.data.let { _knownFor.value = it }
          is Outcome.Loading -> {
            /* do nothing */
          }

          is Outcome.Error -> {
            _loadingState.value = false
            _errorState.value = Event(outcome.message)
          }
        }
      }
    }
  }

  fun getImagePerson(id: Int) {
    viewModelScope.launch {
      getDetailPersonUseCase.getImagePerson((id)).collect { outcome ->
        when (outcome) {
          is Outcome.Success -> outcome.data.let {
            _imagePerson.value = it.profiles ?: emptyList()
          }

          is Outcome.Loading -> {
            /* do nothing */
          }

          is Outcome.Error -> {
            _loadingState.value = false
            _errorState.value = Event(outcome.message)
          }
        }
      }
    }
  }

  fun getExternalIDPerson(id: Int) {
    viewModelScope.launch {
      getDetailPersonUseCase.getExternalIDPerson(id).collect { outcome ->
        when (outcome) {
          is Outcome.Success -> outcome.data.let { _externalIdPerson.value = it }
          is Outcome.Loading -> {
            /* do nothing */
          }

          is Outcome.Error -> {
            _loadingState.value = false
            _errorState.value = Event(outcome.message)
          }
        }
      }
    }
  }
}
