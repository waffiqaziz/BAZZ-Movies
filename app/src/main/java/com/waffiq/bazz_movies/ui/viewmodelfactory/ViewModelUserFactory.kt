package com.waffiq.bazz_movies.ui.viewmodelfactory

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.waffiq.bazz_movies.di.modules.Injection
import com.waffiq.bazz_movies.domain.usecase.auth_tmdb_account.AuthTMDbAccountUseCase
import com.waffiq.bazz_movies.domain.usecase.get_region.GetRegionUseCase
import com.waffiq.bazz_movies.domain.usecase.user_pref.UserPrefUseCase
import com.waffiq.bazz_movies.ui.activity.more.MoreViewModelUser
import com.waffiq.bazz_movies.ui.viewmodel.AuthenticationViewModel
import com.waffiq.bazz_movies.ui.viewmodel.RegionViewModel
import com.waffiq.bazz_movies.ui.viewmodel.UserPreferenceViewModel

class ViewModelUserFactory(
  private val authTMDbAccountUseCase: AuthTMDbAccountUseCase,
  private val userPrefUseCase: UserPrefUseCase,
  private val getRegionUseCase: GetRegionUseCase
) : ViewModelProvider.NewInstanceFactory() {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return when {
      modelClass.isAssignableFrom(AuthenticationViewModel::class.java) -> {
        AuthenticationViewModel(authTMDbAccountUseCase) as T
      }

      modelClass.isAssignableFrom(MoreViewModelUser::class.java) -> {
        MoreViewModelUser(authTMDbAccountUseCase) as T
      }

      modelClass.isAssignableFrom(UserPreferenceViewModel::class.java) -> {
        UserPreferenceViewModel(userPrefUseCase) as T
      }

      modelClass.isAssignableFrom(RegionViewModel::class.java) -> {
        RegionViewModel(getRegionUseCase) as T
      }

      else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
  }

  companion object {
    @Volatile
    private var instance: ViewModelUserFactory? = null

    fun getInstance(dataStore: DataStore<Preferences>): ViewModelUserFactory =
      instance ?: synchronized(this) {
        instance ?: ViewModelUserFactory(
          Injection.provideAuthTMDbAccountUseCase(dataStore),
          Injection.provideUserPrefUseCase(dataStore),
          Injection.provideGetRegionUseCase(dataStore)
        )
      }
  }
}
