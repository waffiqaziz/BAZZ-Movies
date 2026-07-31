package com.waffiq.bazz_movies.core.testmodule

import androidx.lifecycle.MutableLiveData
import com.waffiq.bazz_movies.core.common.utils.Constants.NAN
import com.waffiq.bazz_movies.core.models.UserModel
import com.waffiq.bazz_movies.core.testmodule.DummyData.userModel
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MockUserPreferenceViewModelModule {

  private var mockUserModel = MutableLiveData(userModel)
  private val mockRegionPref = MutableLiveData("US")

  @Provides
  @Singleton
  fun provideMockUserPreferenceViewModel(): UserPreferenceViewModel =
    mockk<UserPreferenceViewModel>(relaxed = true).apply {
      every { getUserPref() } returns mockUserModel
      every { getUserRegionPref() } returns mockRegionPref
      every { saveUserPref(any()) } just Runs
      every { saveRegionPref(any()) } just Runs
      every { removeUserDataPref() } just Runs
    }

  fun setupLoggedUserModel() {
    mockUserModel.postValue(userModel)
  }

  fun setupGuestUserModel() {
    mockUserModel.postValue(userModel.copy(token = NAN))
  }

  fun setupUserModel(userModel: UserModel) {
    mockUserModel.postValue(userModel)
  }

  fun setupRegion(region: String) {
    mockRegionPref.postValue(region)
  }
}
