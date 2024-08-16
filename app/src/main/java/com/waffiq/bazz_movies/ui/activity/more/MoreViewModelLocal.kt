package com.waffiq.bazz_movies.ui.activity.more

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waffiq.bazz_movies.domain.usecase.local_database.LocalDatabaseUseCase
import com.waffiq.bazz_movies.utils.common.Event
import com.waffiq.bazz_movies.utils.result_state.DbResult
import kotlinx.coroutines.launch

class MoreViewModelLocal(
  private val localDatabaseUseCase: LocalDatabaseUseCase
) : ViewModel() {

  private val _dbResult = MutableLiveData<Event<DbResult<Int>>>()
  val dbResult: LiveData<Event<DbResult<Int>>> get() = _dbResult

  fun deleteAll() {
    viewModelScope.launch {
      _dbResult.postValue(Event(localDatabaseUseCase.deleteAll()))
    }
  }
}