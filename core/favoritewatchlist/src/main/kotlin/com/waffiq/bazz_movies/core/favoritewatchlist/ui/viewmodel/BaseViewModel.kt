package com.waffiq.bazz_movies.core.favoritewatchlist.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BaseViewModel @Inject constructor() : ViewModel() {
  private val _isSnackbarShown = MutableLiveData<Boolean>()
  val isSnackbarShown: LiveData<Boolean> get() = _isSnackbarShown

  fun markSnackbarShown() {
    _isSnackbarShown.value = true
  }

  fun resetSnackbarShown() {
    _isSnackbarShown.value = false
  }
}
