package com.waffiq.bazz_movies.ui.activity.myfolder

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.waffiq.bazz_movies.R

class MyFolderViewModel(application: Application) : AndroidViewModel(application){

  private val _text = MutableLiveData<String>().apply {
    value = application.getString(R.string.feature_not_ready)
  }
  val text: LiveData<String> = _text
}