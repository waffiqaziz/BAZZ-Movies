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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
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
    executeUseCase(
      flowProvider = { getDetailPersonUseCase.getDetailPerson(id) },
      onSuccess = { _detailPerson.value = it },
      onFinallySuccess = { _loadingState.value = false },
      onLoading = { _loadingState.value = true }
    )
  }

  fun getKnownFor(id: Int) {
    executeUseCase(
      flowProvider = { getDetailPersonUseCase.getKnownForPerson(id) },
      onSuccess = { _knownFor.value = it },
    )
  }

  fun getImagePerson(id: Int) {
    executeUseCase(
      flowProvider = { getDetailPersonUseCase.getImagePerson(id) },
      onSuccess = { _imagePerson.value = it.profiles ?: emptyList() }
    )
  }

  fun getExternalIDPerson(id: Int) {
    executeUseCase(
      flowProvider = { getDetailPersonUseCase.getExternalIDPerson(id) },
      onSuccess = { _externalIdPerson.value = it },
    )
  }

  fun <T> executeUseCase(
    flowProvider: suspend () -> Flow<Outcome<T>>,
    onSuccess: (T) -> Unit,
    onFinallySuccess: () -> Unit = { /* default do nothing */ },
    onLoading: () -> Unit = { /* default do nothing */ },
  ) {
    viewModelScope.launch {
      val flow = flowProvider()
      flow.collectLatest { outcome ->
        when (outcome) {
          is Outcome.Success -> {
            onSuccess(outcome.data)
            onFinallySuccess()
          }

          is Outcome.Loading -> onLoading()
          is Outcome.Error -> {
            _loadingState.value = false
            _errorState.value = Event(outcome.message)
          }
        }
      }
    }
  }
}
