package com.waffiq.bazz_movies.ui.activity.more

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MoreViewModel : ViewModel() {

  private val _text = MutableLiveData<String>().apply {
    value = "This is more Fragment"
  }
  val text: LiveData<String> = _text
}