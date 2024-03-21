package com.waffiq.bazz_movies.ui.activity.more

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waffiq.bazz_movies.data.local.datasource.LocalDataSourceInterface.Companion.SUCCESS
import com.waffiq.bazz_movies.data.repository.MoviesRepository
import com.waffiq.bazz_movies.utils.Event
import com.waffiq.bazz_movies.utils.LocalDatabaseResult
import kotlinx.coroutines.launch

class MoreViewModel(private val movieRepository: MoviesRepository) : ViewModel() {

  private val _deleteAllResult = MutableLiveData<Event<LocalDatabaseResult>>()
  val deleteAllResult: LiveData<Event<LocalDatabaseResult>> get() = _deleteAllResult

  fun deleteAll() {
    viewModelScope.launch {
      movieRepository.deleteAll { resultCode ->
        if (resultCode == SUCCESS) _deleteAllResult.postValue(Event(LocalDatabaseResult.Success))
        else _deleteAllResult.postValue(Event(LocalDatabaseResult.Error("Failed to delete all items")))
      }
    }
  }
}