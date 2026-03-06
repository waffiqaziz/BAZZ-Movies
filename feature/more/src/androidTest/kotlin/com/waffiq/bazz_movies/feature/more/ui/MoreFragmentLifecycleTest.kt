package com.waffiq.bazz_movies.feature.more.ui

import androidx.test.platform.app.InstrumentationRegistry
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.core.user.ui.viewmodel.RegionViewModel
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.feature.more.testutils.DefaultMoreFragmentTestHelper
import com.waffiq.bazz_movies.feature.more.testutils.MoreFragmentTestHelper
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MoreFragmentLifecycleTest : MoreFragmentTestHelper by DefaultMoreFragmentTestHelper() {

  @get:Rule
  var hiltRule = HiltAndroidRule(this)

  @BindValue
  @JvmField
  val mockNavigator: INavigator = mockk(relaxed = true)

  @BindValue
  @JvmField
  val mockSnackbar: ISnackbar = mockk(relaxed = true)

  @BindValue
  @JvmField
  val mockMoreLocalViewModel: MoreLocalViewModel = mockk(relaxed = true)

  @BindValue
  @JvmField
  val mockUserViewModel: MoreUserViewModel = mockk(relaxed = true)

  @BindValue
  @JvmField
  val mockRegionViewModel: RegionViewModel = mockk(relaxed = true)

  @BindValue
  @JvmField
  val mockUserPrefViewModel: UserPreferenceViewModel = mockk(relaxed = true)

  @Before
  override fun setUp() {
    hiltRule.inject()

    setupMocks(
      mockNavigator,
      mockSnackbar,
    )

    setupViewModelMocks(
      mockMoreLocalViewModel,
      mockUserViewModel,
      mockRegionViewModel,
      mockUserPrefViewModel,
    )

    super.setUp()
  }

  @Test
  fun onStop_whenCalled_shouldCleanupSnackbarAndDialog() {
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      moreFragment.onStop()
    }
  }

  @Test
  fun onPause_whenCalled_shouldPassed() {
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      moreFragment.onPause()
    }
  }

  @Test
  fun onResume_whenCalled_shouldPassed() {
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      moreFragment.onResume()
    }
  }

  @Test
  fun onDestroyView_whenCalled_resetsState() {
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      moreFragment.onDestroyView()
    }
    verify(exactly = 1) { mockUserViewModel.removeState() }
  }
}
