package com.waffiq.bazz_movies.core.user.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.user.domain.usecase.getregion.GetRegionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * region = country
 * region is used by TMDB and for local operation
 * and country is used to get country code from country.is, network operation
 */
@HiltViewModel
class RegionViewModel @Inject constructor(
  private val getRegionUseCase: GetRegionUseCase,
) : ViewModel() {
  private val _countryCode = MutableLiveData<String>()
  val countryCode: LiveData<String> = _countryCode

  private val _errorState = MutableLiveData<Event<String>>()
  val errorState: LiveData<Event<String>> get() = _errorState

  fun getCountryCode() {
    viewModelScope.launch {
      getRegionUseCase.getCountryCode().collect { outcome ->
        when (outcome) {
          is Outcome.Success -> {
            outcome.data.let {
              _countryCode.value = it.country ?: ""
            }
          }

          is Outcome.Loading -> {
            /* do nothing */
          }

          is Outcome.Error -> {
            _countryCode.value = ""
            _errorState.value =
              Event(outcome.message)
          }
        }
      }
    }
  }
}
