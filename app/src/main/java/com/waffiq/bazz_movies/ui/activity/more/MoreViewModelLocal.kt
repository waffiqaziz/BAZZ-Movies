package com.waffiq.bazz_movies.ui.activity.more

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waffiq.bazz_movies.data.local.datasource.LocalDataSourceInterface.Companion.SUCCESS
import com.waffiq.bazz_movies.domain.usecase.local_database.LocalDatabaseUseCase
import com.waffiq.bazz_movies.utils.common.Event
import com.waffiq.bazz_movies.utils.LocalResult
import kotlinx.coroutines.launch

class MoreViewModelLocal(
  private val localDatabaseUseCase: LocalDatabaseUseCase
) : ViewModel() {

  private val _deleteAllResult = MutableLiveData<Event<LocalResult>>()
  val deleteAllResult: LiveData<Event<LocalResult>> get() = _deleteAllResult

  fun deleteAll() {
    viewModelScope.launch {
      localDatabaseUseCase.deleteAll { resultCode ->
        if (resultCode == SUCCESS) _deleteAllResult.postValue(Event(LocalResult.Success))
        else _deleteAllResult.postValue(Event(LocalResult.Error("Failed to delete all data items")))
      }
    }
  }
}