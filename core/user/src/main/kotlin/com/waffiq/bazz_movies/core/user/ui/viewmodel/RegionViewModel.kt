package com.waffiq.bazz_movies.core.user.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waffiq.bazz_movies.core.models.Outcome
import com.waffiq.bazz_movies.core.user.domain.usecase.getregion.GetRegionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegionViewModel @Inject constructor(private val getRegionUseCase: GetRegionUseCase) :
  ViewModel() {

  private val _countryCode = MutableLiveData<String>()
  val countryCode: LiveData<String> get() = _countryCode

  private val _errorState = MutableLiveData<String>()
  val errorState: LiveData<String> get() = _errorState

  fun getCountryCode() {
    viewModelScope.launch {
      getRegionUseCase.getCountryCode().collect { outcome ->
        when (outcome) {
          is Outcome.Success -> {
            outcome.data.let {
              _countryCode.value = it.country.orEmpty()
            }
          }

          is Outcome.Loading -> {
            /* do nothing */
          }

          is Outcome.Error -> {
            _countryCode.value = ""
            _errorState.value = outcome.message
          }
        }
      }
    }
  }
}
