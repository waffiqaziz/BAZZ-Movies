package com.waffiq.bazz_movies.feature.more.ui

import android.net.Uri
import androidx.test.platform.app.InstrumentationRegistry
import com.waffiq.bazz_movies.core.designsystem.R.string.no
import com.waffiq.bazz_movies.core.designsystem.R.string.yes
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performTextClick
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
import com.waffiq.bazz_movies.core.uihelper.state.UIState
import com.waffiq.bazz_movies.feature.more.R.id.btn_backup
import com.waffiq.bazz_movies.feature.more.R.id.btn_restore
import com.waffiq.bazz_movies.feature.more.testutils.BaseMoreFragmentTest
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Test

@HiltAndroidTest
class MoreFragmentBackupTest : BaseMoreFragmentTest() {

  @Test
  fun buttonBackup_whenClicked_shouldCoverage() =
    runTest {
      setupGuestUser()

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

      mockRestoreState.emit(UIState.Success(Unit))
      shortDelay()

      mockRestoreState.emit(UIState.Error("Restore failed"))
      shortDelay()

      btn_restore.performClick()
    }

  @Test
  fun dialogRestore_performYes_shouldCallCorrectFunction() {
    setupGuestUser()

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
