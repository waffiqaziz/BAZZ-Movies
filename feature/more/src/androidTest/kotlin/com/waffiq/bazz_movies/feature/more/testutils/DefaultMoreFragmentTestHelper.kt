package com.waffiq.bazz_movies.feature.more.testutils

import androidx.lifecycle.MutableLiveData
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.domain.UserModel
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.core.uihelper.state.UIState
import com.waffiq.bazz_movies.core.user.ui.viewmodel.RegionViewModel
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.feature.more.testutils.Helper.userModel
import com.waffiq.bazz_movies.feature.more.ui.MoreFragment
import com.waffiq.bazz_movies.feature.more.ui.MoreLocalViewModel
import com.waffiq.bazz_movies.feature.more.ui.MoreUserViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow

class DefaultMoreFragmentTestHelper : MoreFragmentTestHelper {
  override lateinit var moreFragment: MoreFragment

  override val mockRegionPref = MutableLiveData<String>()
  override val mockUIState = MutableStateFlow<UIState>(UIState.Idle)
  override val mockCountryCode = MutableLiveData<String>()
  override val mockUserModel = MutableLiveData<UserModel>()

  override fun setupMocks(
    mockNavigator: INavigator,
    mockSnackbar: ISnackbar,
  ) {
    every { mockNavigator.openLoginActivity(any()) } just Runs
    every { mockNavigator.openAboutActivity(any()) } just Runs
    every { mockSnackbar.showSnackbarWarning(any<Event<String>>()) } returns mockk(relaxed = true)
  }

  override fun setupViewModelMocks(
    mockMoreLocalViewModel: MoreLocalViewModel,
    mockUserViewModel: MoreUserViewModel,
    mockRegionViewModel: RegionViewModel,
    mockUserPrefViewModel: UserPreferenceViewModel,
  ) {
    mockUserModel.postValue(userModel)

    every { mockMoreLocalViewModel.state } returns mockUIState
    every { mockMoreLocalViewModel.deleteAll() } just Runs
    every { mockUserViewModel.state } returns mockUIState
    every { mockUserViewModel.deleteSession(any()) } just Runs
    every { mockUserViewModel.removeState() } just Runs
    every { mockRegionViewModel.countryCode } returns mockCountryCode
    // every { mockRegionViewModel.getCountryCode() } just Runs
    every { mockUserPrefViewModel.getUserPref() } returns mockUserModel
    every { mockUserPrefViewModel.getUserRegionPref() } returns mockRegionPref
    every { mockUserPrefViewModel.removeUserDataPref() } just Runs
    every { mockUserPrefViewModel.saveUserPref(userModel) } just Runs
  }
}
