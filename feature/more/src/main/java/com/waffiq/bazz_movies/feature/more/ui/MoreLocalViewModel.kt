package com.waffiq.bazz_movies.feature.more.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waffiq.bazz_movies.core.database.domain.usecase.local_database.LocalDatabaseUseCase
import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.movie.utils.common.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoreLocalViewModel @Inject constructor(
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
