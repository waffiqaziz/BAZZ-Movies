package com.waffiq.bazz_movies.ui.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.waffiq.bazz_movies.di.AppScope
import javax.inject.Inject
import javax.inject.Provider

@AppScope
class ViewModelFactory @Inject constructor(
  private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {
  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    val creator = creators[modelClass] ?: creators.entries.firstOrNull {
      modelClass.isAssignableFrom(it.key)
    }?.value ?: throw IllegalArgumentException("unknown model class $modelClass")
    return creator.get() as T
  }
}
