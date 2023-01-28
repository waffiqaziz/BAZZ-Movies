package com.waffiq.bazz_movies.ui.viewmodel

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.waffiq.bazz_movies.data.repository.UserRepository
import com.waffiq.bazz_movies.di.Injection
import com.waffiq.bazz_movies.ui.activity.more.MoreViewModel

class ViewModelUserFactory(private val userRepository: UserRepository) :
  ViewModelProvider.NewInstanceFactory() {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return when {
      modelClass.isAssignableFrom(MainViewModel::class.java) -> {
        MainViewModel(userRepository) as T
      }
      modelClass.isAssignableFrom(MoreViewModel::class.java) -> {
        MoreViewModel(userRepository) as T
      }
      else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
  }

  companion object {
    @Volatile
    private var instance: ViewModelUserFactory? = null

    fun getInstance(dataStore: DataStore<Preferences>): ViewModelUserFactory =
      instance
        ?: synchronized(this) {
          instance
            ?: ViewModelUserFactory(
              Injection.provideUserRepository(
                dataStore
              )
            )
        }
  }
}