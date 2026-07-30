package com.waffiq.bazz_movies.feature.more.testutils

import androidx.test.espresso.intent.Intents
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
import com.waffiq.bazz_movies.core.instrumentationtest.launchFragmentInHiltContainer
import com.waffiq.bazz_movies.core.testmodule.MockUserPreferenceViewModelModule.setupLoggedUserModel
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.core.uihelper.state.UIState
import com.waffiq.bazz_movies.core.user.ui.viewmodel.RegionViewModel
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.core.utils.openurl.UriLauncher
import com.waffiq.bazz_movies.feature.more.ui.MoreFragment
import com.waffiq.bazz_movies.feature.more.ui.viewmodel.MoreLocalViewModel
import com.waffiq.bazz_movies.feature.more.ui.viewmodel.MoreUserViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.testing.HiltAndroidRule
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.After
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

abstract class BaseMoreFragmentTest {
  protected lateinit var moreFragment: MoreFragment

  protected val mockUIState = MutableStateFlow<UIState<Unit>>(UIState.Idle)
  protected val mockBackupState = MutableStateFlow<UIState<Unit>>(UIState.Idle)
  protected val mockRestoreState = MutableStateFlow<UIState<Unit>>(UIState.Idle)

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
    setupViewModelMocks()

    moreFragment = launchFragmentInHiltContainer<MoreFragment>().fragment
    shortDelay()
  }

  @After
  fun tearDown() {
    Intents.release()
  }

  private fun setupViewModelMocks() {
    setupLoggedUserModel() // default setup is logged user

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
  }
}
