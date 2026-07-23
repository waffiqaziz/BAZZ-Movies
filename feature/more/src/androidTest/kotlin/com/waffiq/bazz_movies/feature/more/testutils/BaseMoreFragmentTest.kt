package com.waffiq.bazz_movies.feature.more.testutils

import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.intent.Intents
import com.waffiq.bazz_movies.core.common.utils.Constants.NAN
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
import com.waffiq.bazz_movies.core.instrumentationtest.launchFragmentInHiltContainer
import com.waffiq.bazz_movies.core.models.UserModel
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.core.uihelper.state.UIState
import com.waffiq.bazz_movies.core.user.ui.viewmodel.RegionViewModel
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.core.utils.openurl.UriLauncher
import com.waffiq.bazz_movies.feature.more.testutils.Helper.userModel
import com.waffiq.bazz_movies.feature.more.ui.MoreFragment
import com.waffiq.bazz_movies.feature.more.ui.viewmodel.MoreLocalViewModel
import com.waffiq.bazz_movies.feature.more.ui.viewmodel.MoreUserViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.testing.HiltAndroidRule
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.After
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

abstract class BaseMoreFragmentTest {
  protected lateinit var moreFragment: MoreFragment

  protected val mockRegionPref = MutableLiveData<String>()
  protected val mockUIState = MutableStateFlow<UIState<Unit>>(UIState.Idle)
  protected val mockBackupState = MutableStateFlow<UIState<Unit>>(UIState.Idle)
  protected val mockRestoreState = MutableStateFlow<UIState<Unit>>(UIState.Idle)
  protected val mockCountryCode = MutableLiveData<String>()
  protected val mockUserModel = MutableLiveData<UserModel>()

  @get:Rule
  var hiltRule = HiltAndroidRule(this)

  @Inject
  lateinit var mockNavigator: INavigator

  @Inject
  lateinit var mockSnackbar: ISnackbar

  @Inject
  lateinit var mockUserPrefViewModel: UserPreferenceViewModel

  @Inject
  lateinit var mockRegionViewModel: RegionViewModel

  @Inject
  lateinit var mockMoreLocalViewModel: MoreLocalViewModel

  @Inject
  lateinit var mockUserViewModel: MoreUserViewModel

  @Inject
  lateinit var mockUriLauncher: UriLauncher

  @Before
  open fun setup() {
    hiltRule.inject()
    Intents.init()
    setupMocks()
    setupViewModelMocks()

    moreFragment = launchFragmentInHiltContainer<MoreFragment>().fragment
    shortDelay()
  }

  @After
  fun tearDown() {
    Intents.release()
  }

  protected fun setupMocks() {
    every { mockNavigator.openLoginActivity(any()) } just Runs
    every { mockNavigator.openAboutActivity(any()) } just Runs
    every { mockUriLauncher.launch(any()) } just Runs
    every { mockSnackbar.showSnackbarWarning(any<Event<String>>()) } returns mockk(relaxed = true)
    every { mockSnackbar.showSnackbarWarning(any<String>()) } returns mockk(relaxed = true)
  }

  protected fun setupViewModelMocks() {
    mockUserModel.postValue(userModel)

    every { mockMoreLocalViewModel.state } returns mockUIState
    every { mockMoreLocalViewModel.backupState } returns mockBackupState
    every { mockMoreLocalViewModel.restoreState } returns mockRestoreState
    every { mockMoreLocalViewModel.deleteAll() } just Runs
    every { mockMoreLocalViewModel.deleteAllSearchHistory() } just Runs
    every { mockMoreLocalViewModel.backupDatabase(any()) } just Runs
    every { mockMoreLocalViewModel.restoreDatabase(any()) } just Runs
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

  protected fun setupGuestUser() {
    mockUserModel.postValue(userModel.copy(token = NAN))
    shortDelay(500)
  }
}
