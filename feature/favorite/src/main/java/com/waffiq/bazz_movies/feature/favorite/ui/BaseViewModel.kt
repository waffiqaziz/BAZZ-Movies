package com.waffiq.bazz_movies.feature.favorite.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {
  private val _isSnackbarShown = MutableLiveData<Boolean>()
  val isSnackbarShown: LiveData<Boolean> get() = _isSnackbarShown

  fun markSnackbarShown() {
    _isSnackbarShown.value = true
  }

  fun resetSnackbarShown() {
    _isSnackbarShown.value = false
  }
}
