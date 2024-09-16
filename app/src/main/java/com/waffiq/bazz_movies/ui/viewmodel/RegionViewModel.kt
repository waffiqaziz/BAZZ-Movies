package com.waffiq.bazz_movies.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waffiq.bazz_movies.domain.usecase.get_region.GetRegionUseCase
import com.waffiq.bazz_movies.utils.common.Event
import com.waffiq.bazz_movies.utils.resultstate.Status
import kotlinx.coroutines.launch

class RegionViewModel(
  private val getRegionUseCase: GetRegionUseCase
) : ViewModel() {
  private val _countryCode = MutableLiveData<String>()
  val countryCode: LiveData<String> = _countryCode

  private val _errorState = MutableLiveData<Event<String>>()
  val errorState: LiveData<Event<String>> get() = _errorState

  fun getCountryCode() =
    viewModelScope.launch {
      getRegionUseCase.getCountryCode().collect { networkResult ->
        when (networkResult.status) {
          Status.SUCCESS -> {
            networkResult.data.let {
              if (it != null) _countryCode.value = it.country ?: ""
            }
          }

          Status.LOADING -> {}
          Status.ERROR -> {
            _errorState.value =
              Event(networkResult.message ?: "Something went wrong. Please try again later.")
          }
        }
      }
    }
}
