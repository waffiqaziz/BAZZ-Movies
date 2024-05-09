package com.waffiq.bazz_movies.ui.activity.more

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.waffiq.bazz_movies.data.remote.SessionIDPostModel
import com.waffiq.bazz_movies.data.remote.response.tmdb.PostRateResponse
import com.waffiq.bazz_movies.data.repository.UserRepository
import com.waffiq.bazz_movies.utils.Event
import com.waffiq.bazz_movies.utils.NetworkResult
import com.waffiq.bazz_movies.utils.Status
import kotlinx.coroutines.launch

class MoreViewModelUser(private val userRepository: UserRepository) : ViewModel() {

  private val _signOutState = MutableLiveData<NetworkResult<PostRateResponse>>()
  val signOutState: LiveData<NetworkResult<PostRateResponse>> get() = _signOutState

  private val _countryCode = MutableLiveData<String>()
  val countryCode: LiveData<String> = _countryCode

  private val _errorState = MutableLiveData<Event<String>>()
  val errorState: LiveData<Event<String>> get() = _errorState

  fun getUserRegion() = userRepository.getUserRegion().asLiveData()
  fun saveUserRegion(region: String) {
    viewModelScope.launch {
      userRepository.saveRegion(region)
    }
  }

  fun deleteSession(data: SessionIDPostModel) =
    viewModelScope.launch {
      userRepository.deleteSession(data).collect { networkResult ->
        _signOutState.value = networkResult
      }
    }

  fun getCountryCode() =
    viewModelScope.launch {
      userRepository.getCountryCode().collect { networkResult ->
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