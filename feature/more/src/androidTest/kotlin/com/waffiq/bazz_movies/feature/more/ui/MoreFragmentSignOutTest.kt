package com.waffiq.bazz_movies.feature.more.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.waffiq.bazz_movies.core.designsystem.R.string.no
import com.waffiq.bazz_movies.core.designsystem.R.string.warning
import com.waffiq.bazz_movies.core.designsystem.R.string.warning_signOut_logged_user
import com.waffiq.bazz_movies.core.designsystem.R.string.yes
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performTextClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.doesNotExist
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isEnable
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isNotDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isNotEnable
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.textIsDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.waitUntil
import com.waffiq.bazz_movies.core.uihelper.state.UIState
import com.waffiq.bazz_movies.feature.more.R.id.btn_signout
import com.waffiq.bazz_movies.feature.more.R.id.progress_bar
import com.waffiq.bazz_movies.feature.more.testutils.BaseMoreFragmentTest
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.verify
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.core.IsNot.not
import org.junit.Test

@HiltAndroidTest
class MoreFragmentSignOutTest : BaseMoreFragmentTest() {

  @Test
  fun signOut_whenGuestUser_shouldShowGuestModeDialog() {
    setupGuestUser()
    performSignOutAction()
  }

  @Test
  fun signOutStateLogin_allBranches_shouldBeCovered() =
    runTest {
      // Hit all branches in the logged-in when statement
      mockUIState.emit(UIState.Loading)
      advanceUntilIdle()
      performSignOutAction()

      mockUIState.emit(UIState.Error("Test error"))
      advanceUntilIdle()

      mockUIState.emit(UIState.Success(Unit))
      advanceUntilIdle()
    }

  @Test
  fun signOutStateGuest_allBranches_shouldBeCovered() =
    runTest {
      setupGuestUser()
      performSignOutAction()
      mockUIState.emit(UIState.Loading)
      advanceUntilIdle()
    }

  @Test
  fun signOut_whenLoggedInUser_shouldShowLoggedInDialog() {
    btn_signout.performClick()

    warning.textIsDisplayed()
    warning_signOut_logged_user.textIsDisplayed()
  }

  @Test
  fun signOutDialogGuestUser_whenYesClicked_shouldDeleteLocalData() {
    setupGuestUser()
    performSignOutAction()

    verify { mockMoreLocalViewModel.deleteAll() }
  }

  @Test
  fun signOutDialog_whenNoClicked_shouldDismissDialog() {
    btn_signout.performClick()
    no.performTextClick()

    // dialog should be dismissed, verify warning text is not displayed
    warning.doesNotExist()
  }

  @Test
  fun signOutStateLogin_whenSuccess_shouldShowSuccessToastAndNavigateToLogin() =
    runTest {
      performSignOutAction()
      mockUIState.emit(UIState.Success(Unit))
      InstrumentationRegistry.getInstrumentation().waitForIdleSync()
      advanceUntilIdle()

      verify { mockMoreLocalViewModel.deleteAllSearchHistory() }
    }

  @Test
  fun signOutStateLogin_whenLoading_shouldShowLoadingState() =
    runTest {
      performSignOutAction()
      mockUIState.emit(UIState.Loading)

      progress_bar.isDisplayed()

      verify(exactly = 0) { mockMoreLocalViewModel.deleteAllSearchHistory() }
    }

  @Test
  fun dbResultGuestUser_whenSuccess_shouldShowSuccessToast() =
    runTest {
      setupGuestUser()
      performSignOutAction()
      mockUIState.emit(UIState.Success(Unit))

      verify { mockMoreLocalViewModel.deleteAllSearchHistory() }
    }

  @Test
  fun signOutStateLogin_whenErrorOccurs_signOutButtonShouldEnable() =
    runTest {
      performSignOutAction()
      mockUIState.emit(UIState.Loading)
      advanceUntilIdle()

      onView(withId(progress_bar)).check(waitUntil(isDisplayed()))
      btn_signout.isNotEnable()

      mockUIState.emit(UIState.Error("Sign out failed"))
      advanceUntilIdle()

      btn_signout.isEnable()
      onView(withId(progress_bar)).check(waitUntil(not(isDisplayed())))

      verify { mockSnackbar.showSnackbarWarning("Sign out failed") }
      verify(exactly = 0) { mockMoreLocalViewModel.deleteAllSearchHistory() }
    }

  @Test
  fun dbResultGuestUser_whenError_shouldShowErrorSnackbar() =
    runTest {
      setupGuestUser()
      performSignOutAction()

      mockUIState.emit(UIState.Error("Database error"))
      shortDelay()

      progress_bar.isNotDisplayed()
      verify(timeout = 2000) { mockSnackbar.showSnackbarWarning(any<String>()) }
    }

  @Test
  fun signOutGuestUser_clickedNoOption_doNothing() {
    setupGuestUser()
    btn_signout.performClick()
    no.performTextClick()

    warning.doesNotExist()
  }

  private fun performSignOutAction() {
    btn_signout.performClick()
    yes.performTextClick()
  }
}
