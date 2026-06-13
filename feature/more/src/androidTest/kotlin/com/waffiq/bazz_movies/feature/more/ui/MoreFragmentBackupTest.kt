package com.waffiq.bazz_movies.feature.more.ui

import android.net.Uri
import androidx.test.platform.app.InstrumentationRegistry
import com.waffiq.bazz_movies.core.designsystem.R.string.no
import com.waffiq.bazz_movies.core.designsystem.R.string.yes
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performTextClick
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.core.uihelper.state.UIState
import com.waffiq.bazz_movies.core.user.ui.viewmodel.RegionViewModel
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.feature.more.R.id.btn_backup
import com.waffiq.bazz_movies.feature.more.R.id.btn_restore
import com.waffiq.bazz_movies.feature.more.testutils.DefaultMoreFragmentTestHelper
import com.waffiq.bazz_movies.feature.more.testutils.MoreFragmentTestHelper
import com.waffiq.bazz_movies.feature.more.ui.viewmodel.MoreLocalViewModel
import com.waffiq.bazz_movies.feature.more.ui.viewmodel.MoreUserViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MoreFragmentBackupTest : MoreFragmentTestHelper by DefaultMoreFragmentTestHelper() {

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
  fun buttonBackup_whenClicked_shouldCoverage() =
    runTest {
      setupGuestUser()
      shortDelay(500)

      mockBackupState.emit(UIState.Success(Unit))
      shortDelay()

      mockBackupState.emit(UIState.Error("Bakcup error"))
      shortDelay()

      btn_backup.performClick()
    }

  @Test
  fun buttonRestore_whenClicked_shouldCoverage() =
    runTest {
      setupGuestUser()
      shortDelay(500)

      mockRestoreState.emit(UIState.Success(Unit))
      shortDelay()

      mockRestoreState.emit(UIState.Error("Restore failed"))
      shortDelay()

      btn_restore.performClick()
    }

  @Test
  fun dialogRestore_performYes_shouldCallCorrectFunction() {
    setupGuestUser()
    shortDelay()

    val testUri = Uri.parse("content://test/bazz_movies_backup.json")
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      moreFragment.handleRestoreUri(testUri)
    }
    InstrumentationRegistry.getInstrumentation().waitForIdleSync()
    shortDelay()
    yes.performTextClick()

    shortDelay(500)
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      moreFragment.handleRestoreUri(testUri)
    }
    InstrumentationRegistry.getInstrumentation().waitForIdleSync()
    shortDelay()
    no.performTextClick()
  }
}
