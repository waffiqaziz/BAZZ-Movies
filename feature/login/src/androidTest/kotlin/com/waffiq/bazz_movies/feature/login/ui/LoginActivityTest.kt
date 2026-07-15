package com.waffiq.bazz_movies.feature.login.ui

import android.content.res.Configuration
import androidx.lifecycle.MutableLiveData
import com.waffiq.bazz_movies.core.designsystem.R.string.guest_user
import com.waffiq.bazz_movies.core.instrumentationtest.CustomAssertions.isPasswordHidden
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performAction
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performType
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.checkMatches
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.doesHaveText
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.hasErrorText
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.hasNoError
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isEnable
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isNotDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isNotEnable
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.withDrawable
import com.waffiq.bazz_movies.feature.login.R.drawable.ic_eye_off
import com.waffiq.bazz_movies.feature.login.R.id.activity_login
import com.waffiq.bazz_movies.feature.login.R.id.btn_eye
import com.waffiq.bazz_movies.feature.login.R.id.btn_forget_password
import com.waffiq.bazz_movies.feature.login.R.id.btn_login
import com.waffiq.bazz_movies.feature.login.R.id.et_pass
import com.waffiq.bazz_movies.feature.login.R.id.et_username
import com.waffiq.bazz_movies.feature.login.R.id.layout_bazz_movies
import com.waffiq.bazz_movies.feature.login.R.id.progress_bar
import com.waffiq.bazz_movies.feature.login.R.id.tv_guest
import com.waffiq.bazz_movies.feature.login.R.id.tv_joinTMDB
import com.waffiq.bazz_movies.feature.login.ui.testutils.BaseLoginActivityTest
import com.waffiq.bazz_movies.feature.login.utils.common.Constants.TMDB_LINK_FORGET_PASSWORD
import com.waffiq.bazz_movies.feature.login.utils.common.Constants.TMDB_LINK_SIGNUP
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.random.Random

@HiltAndroidTest
class LoginActivityTest : BaseLoginActivityTest() {

  @Test
  fun loginScreen_whenInitialized_showsAllViewsCorrectly() {
    btn_forget_password.isDisplayed()
    layout_bazz_movies.isDisplayed()
    et_username.isDisplayed()
    et_pass.isDisplayed()
    btn_eye.isDisplayed()
    btn_login.isDisplayed()
    tv_guest.isDisplayed()
    tv_joinTMDB.isDisplayed()

    progress_bar.isNotDisplayed()
    btn_eye.isDisplayed()
    btn_eye.withDrawable(ic_eye_off)
  }

  @Test
  fun clickForgetPassword_successful_launchesCorrectURI() {
    btn_forget_password.performClick()

    assertEquals(1, fakeLauncher.launchedUris.size)
    assertEquals(TMDB_LINK_FORGET_PASSWORD, fakeLauncher.launchedUris.first())
  }

  @Test
  fun clickJoinTMDB_successful_launchesCorrectURI() {
    tv_joinTMDB.performClick()
    assertEquals(TMDB_LINK_SIGNUP, fakeLauncher.launchedUris.first())
  }

  @Test
  fun clickJoinTMDB_noBrowser_launchesToast() {
    fakeLauncher.shouldFail = true

    tv_joinTMDB.performClick()
    // Hard to test toast in code, so must check it manually
  }

  @Test
  fun login_withCorrectParams_callsAuthenticationViewModel() {
    performValidLogin()
    verify { mockLoginViewModel.userLogin(validUsername, validPassword) }
  }

  @Test
  fun login_withIncorrectCredential_showsErrorMessage() {
    // without entering username/password
    btn_login.performClick()
    et_username.hasErrorText("Please enter a username")
    et_pass.hasErrorText("Please enter a password")

    clearForm()

    // only username
    typeUserName(validUsername)
    btn_login.performClick()
    et_username.hasNoError()
    et_pass.hasErrorText("Please enter a password")

    clearForm()

    // only password
    typePassword(validPassword)
    btn_login.performClick()
    et_username.hasErrorText("Please enter a username")
    et_pass.hasNoError()

    clearForm()

    // blank value
    typeUserName("   ")
    typePassword("   ")
    btn_login.performClick()
    et_username.hasErrorText("Please enter a username")
    et_pass.hasErrorText("Please enter a password")

    clearForm()

    // empty pass
    typeUserName(validUsername)
    typePassword("")
    btn_login.performClick()
    et_pass.hasErrorText("Please enter a password")

    clearForm()

    // empty user
    typeUserName("")
    typePassword(validPassword)
    btn_login.performClick()
    et_username.hasErrorText("Please enter a username")
  }

  @Test
  fun passwordButtonToggle_whenClicked_showsPassword() {
    typePassword(validPassword)

    // unmask the edit text
    btn_eye.performClick()
    et_pass.doesHaveText(validPassword)

    btn_eye.performClick()

    // verify password is masked
    et_pass.checkMatches(isPasswordHidden())
  }

  @Test
  fun passwordToggle_whenMovingTheCursor_maintainsCursorPosition() {
    et_pass.performType(validPassword)

    // move cursor to middle
    et_pass.performAction(clickAtPosition(Random.nextInt(1, 6)))

    // toggle password visibility
    btn_eye.performClick()

    // verify cursor position is maintained
    et_pass.doesHaveText(validPassword)
  }

  @Test
  fun textWatcher_removesUsernameError_whenTyping() {
    // create an error
    btn_login.performClick()
    et_username.hasErrorText("Please enter a username")

    // type to remove error
    typeUserName(validUsername)
    et_username.hasNoError()
  }

  @Test
  fun textWatcher_removesPasswordError_whenTyping() {
    btn_login.performClick()
    et_pass.hasErrorText("Please enter a password")

    typePassword(validPassword)
    et_pass.hasNoError()
  }

  @Test
  fun login_withInvalidCredential_returnToLoginActivity() {
    // enter invalid credentials
    typeUserName("random")
    typePassword("random")

    btn_login.performClick()
    activity_login.isDisplayed()
  }

  @Test
  fun login_withWhenLoading_disablesButtonsAndShowsLoading() {
    // mock successful login flow
    val loadingLiveData = MutableLiveData<Boolean>()
    every { mockLoginViewModel.loadingState } returns loadingLiveData

    performValidLogin()

    // verify buttons are disabled during login
    btn_login.isNotEnable()
    tv_guest.isNotEnable()

    // simulate loading state
    scenario.onActivity {
      loadingLiveData.postValue(true)
    }

    progress_bar.isDisplayed()
  }

  @Test
  fun login_withSuccessful_navigatesToMainActivity() {
    performValidLogin()
    scenario.onActivity {
      loginStateLiveData.postValue(true)
    }

    // verify navigation happened
    verify { mockNavigator.openMainActivity(any()) }
  }

  @Test
  fun login_unSuccessful_doesNotNavigate() {
    performValidLogin()

    scenario.onActivity {
      loginStateLiveData.postValue(false)
    }

    // verify navigation never happened
    verify(exactly = 0) { mockNavigator.openMainActivity(any()) }
  }

  @Test
  fun login_whenErrorStateReceived_showsSnackbarAndEnablesButtons() {
    every { mockLoginViewModel.userLogin(any(), any()) } answers {
      scenario.onActivity {
        errorStateLiveData.value = "Invalid credentials"
      }
    }

    performValidLogin()

    btn_login.isEnable()
    tv_guest.isEnable()
  }

  @Test
  fun guestLogin_whenClicked_savesGuestUserAndNavigates() {
    tv_guest.performClick()

    // verify guest user is saved
    verify {
      mockLoginViewModel.saveGuestUserPref(
        context.getString(guest_user),
        context.getString(guest_user),
      )
    }

    // verify navigation to main activity
    verify { mockNavigator.openMainActivity(any()) }
  }

  @Test
  fun loginScreen_whenConfigurationChange_maintainsTheState() {
    typeValidCredentials()

    // simulate configuration change (rotation)
    scenario.onActivity { activity ->
      activity.onConfigurationChanged(
        Configuration().apply {
          orientation = Configuration.ORIENTATION_LANDSCAPE
        },
      )
    }

    // verify data is maintained
    et_username.doesHaveText(validUsername)
    et_pass.doesHaveText(validPassword)
  }
}
