package com.waffiq.bazz_movies.ui.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.waffiq.bazz_movies.di.AppScope
import com.waffiq.bazz_movies.domain.usecase.auth_tmdb_account.AuthTMDbAccountUseCase
import com.waffiq.bazz_movies.domain.usecase.get_region.GetRegionUseCase
import com.waffiq.bazz_movies.domain.usecase.user_pref.UserPrefUseCase
import com.waffiq.bazz_movies.ui.activity.more.MoreUserViewModel
import com.waffiq.bazz_movies.ui.viewmodel.AuthenticationViewModel
import com.waffiq.bazz_movies.ui.viewmodel.RegionViewModel
import com.waffiq.bazz_movies.ui.viewmodel.UserPreferenceViewModel
import javax.inject.Inject

@AppScope
class ViewModelUserFactory @Inject constructor(
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

      modelClass.isAssignableFrom(MoreUserViewModel::class.java) -> {
        MoreUserViewModel(authTMDbAccountUseCase) as T
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
}
