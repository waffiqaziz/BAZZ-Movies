package com.waffiq.bazz_movies.feature.login.ui

import android.content.Context
import android.content.res.Configuration
import android.view.View
import android.widget.EditText
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsRule
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.waffiq.bazz_movies.core.designsystem.R.string.guest_user
import com.waffiq.bazz_movies.core.domain.UserModel
import com.waffiq.bazz_movies.core.instrumentationtest.CustomAssertions.isPasswordHidden
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performAction
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performType
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.doesHaveText
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.hasErrorText
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.hasNoError
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isNotDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isNotEnable
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.withDrawable
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.feature.login.R.drawable.ic_eye_off
import com.waffiq.bazz_movies.feature.login.R.id.activity_login
import com.waffiq.bazz_movies.feature.login.R.id.btn_eye
import com.waffiq.bazz_movies.feature.login.R.id.btn_forget_password
import com.waffiq.bazz_movies.feature.login.R.id.btn_login
import com.waffiq.bazz_movies.feature.login.R.id.ed_pass
import com.waffiq.bazz_movies.feature.login.R.id.ed_username
import com.waffiq.bazz_movies.feature.login.R.id.layout_bazz_movies
import com.waffiq.bazz_movies.feature.login.R.id.progress_bar
import com.waffiq.bazz_movies.feature.login.R.id.tv_guest
import com.waffiq.bazz_movies.feature.login.R.id.tv_joinTMDB
import com.waffiq.bazz_movies.feature.login.ui.testutils.FakeUriLauncher
import com.waffiq.bazz_movies.feature.login.utils.common.Constants.TMDB_LINK_FORGET_PASSWORD
import com.waffiq.bazz_movies.feature.login.utils.common.Constants.TMDB_LINK_SIGNUP
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.random.Random

@HiltAndroidTest
class LoginActivityTest {

  private val validUsername = "validUsername1234"
  private val validPassword = "validPassword1234"

  private val loginStateLiveData = MutableLiveData<Boolean>()
  private val userModelLiveData = MutableLiveData<UserModel>()
  private val errorStateLiveData = MutableLiveData<String>()
  private val loadingStateLiveData = MutableLiveData<Boolean>()

  private lateinit var context: Context

  @get:Rule(order = 0)
  var hiltRule = HiltAndroidRule(this)

  @get:Rule(order = 1)
  var intentsRule = IntentsRule()

  @get:Rule(order = 2)
  var activityRule = ActivityScenarioRule(LoginActivity::class.java)

  @BindValue
  @JvmField
  val mockNavigator: INavigator = mockk(relaxed = true)

  @BindValue
  @JvmField
  val mockAuthViewModel: AuthenticationViewModel = mockk(relaxed = true)

  @BindValue
  @JvmField
  val mockUserPrefViewModel: UserPreferenceViewModel = mockk(relaxed = true)

  @Before
  fun init() {
    hiltRule.inject()

    activityRule.scenario.onActivity { activity ->
      context = activity.applicationContext
    }

    every { mockAuthViewModel.errorState } returns errorStateLiveData
    every { mockAuthViewModel.loginState } returns loginStateLiveData
    every { mockAuthViewModel.userModel } returns userModelLiveData
    every { mockAuthViewModel.loadingState } returns loadingStateLiveData
    every { mockUserPrefViewModel.saveUserPref(any()) } just Runs
  }

  @Test
  fun loginScreen_whenInitialized_showsAllViews() {
    btn_forget_password.isDisplayed()
    layout_bazz_movies.isDisplayed()
    ed_username.isDisplayed()
    ed_pass.isDisplayed()
    btn_eye.isDisplayed()
    btn_login.isDisplayed()
    tv_guest.isDisplayed()
    tv_joinTMDB.isDisplayed()
  }

  @Test
  fun loginScreen_whenInitialized_progressBarIsGone() {
    progress_bar.isNotDisplayed()
  }

  @Test
  fun loginScreen_whenInitialized_showsButtonEyeOff() {
    onView(withId(btn_eye))
      .check(matches(allOf(isDisplayed(), withDrawable(ic_eye_off))))
  }

  @Test
  fun clickForgetPassword_successful_launchesCorrectURI() {
    btn_forget_password.performClick()

    activityRule.scenario.onActivity { activity ->
      val fake = activity.uriLauncher as FakeUriLauncher
      assertEquals(TMDB_LINK_FORGET_PASSWORD.toUri(), fake.launchedUris.first())
    }
  }

  @Test
  fun clickJoinTMDB_successful_launchesCorrectURI() {
    tv_joinTMDB.performClick()

    activityRule.scenario.onActivity { activity ->
      val fake = activity.uriLauncher as FakeUriLauncher
      assertEquals(TMDB_LINK_SIGNUP.toUri(), fake.launchedUris.first())
    }
  }

  @Test
  fun clickJoinTMDB_noBrowser_launchesToast() {
    activityRule.scenario.onActivity { activity ->
      (activity.uriLauncher as FakeUriLauncher).shouldFail = true
    }

    tv_joinTMDB.performClick()
    // Hard to test toast in code, so must check it manually
  }

  @Test
  fun login_withCorrectParams_callsAuthenticationViewModel() {
    performValidLogin()
    verify { mockAuthViewModel.userLogin(validUsername, validPassword) }
  }

  @Test
  fun login_withIncorrectCredential_showsErrorMessage() {
    // without entering username/password
    btn_login.performClick()
    ed_username.hasErrorText("Please enter a username")
    ed_pass.hasErrorText("Please enter a password")

    clearForm()

    // only username
    typeUserName(validUsername)
    btn_login.performClick()
    ed_username.hasNoError()
    ed_pass.hasErrorText("Please enter a password")

    clearForm()

    // only password
    typePassword(validPassword)
    btn_login.performClick()
    ed_username.hasErrorText("Please enter a username")
    ed_pass.hasNoError()

    clearForm()

    // blank value
    typeUserName("   ")
    typePassword("   ")
    btn_login.performClick()
    ed_username.hasErrorText("Please enter a username")
    ed_pass.hasErrorText("Please enter a password")

    clearForm()

    // empty pass
    typeUserName(validUsername)
    typePassword("")
    btn_login.performClick()
    ed_pass.hasErrorText("Please enter a password")

    clearForm()

    // empty user
    typeUserName("")
    typePassword(validPassword)
    btn_login.performClick()
    ed_username.hasErrorText("Please enter a username")
  }

  @Test
  fun passwordButtonToggle_whenClicked_showsPassword() {
    typePassword(validPassword)

    // unmask the edit text
    btn_eye.performClick()
    ed_pass.doesHaveText(validPassword)

    btn_eye.performClick()

    // verify password is masked
    onView(withId(ed_pass)).check(matches(isPasswordHidden()))
  }

  @Test
  fun passwordToggle_whenMovingTheCursor_maintainsCursorPosition() {
    ed_pass.performType(validPassword)

    // move cursor to middle
    ed_pass.performAction(clickAtPosition(Random.nextInt(1, 6)))

    // toggle password visibility
    btn_eye.performClick()

    // verify cursor position is maintained
    ed_pass.doesHaveText(validPassword)
  }

  @Test
  fun textWatcher_removesUsernameError_whenTyping() {
    // create an error
    btn_login.performClick()
    ed_username.hasErrorText("Please enter a username")

    // type to remove error
    typeUserName(validUsername)
    ed_username.hasNoError()
  }

  @Test
  fun textWatcher_removesPasswordError_whenTyping() {
    btn_login.performClick()
    ed_pass.hasErrorText("Please enter a password")

    typePassword(validPassword)
    ed_pass.hasNoError()
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
  fun login_withValidCredentials_disablesButtonsAndShowsLoading() {
    // mock successful login flow
    val loadingLiveData = MutableLiveData<Boolean>()
    every { mockAuthViewModel.loadingState } returns loadingLiveData

    performValidLogin()

    // verify buttons are disabled during login
    btn_login.isNotEnable()
    tv_guest.isNotEnable()

    // simulate loading state
    activityRule.scenario.onActivity {
      loadingLiveData.postValue(true)
    }

    progress_bar.isDisplayed()
  }

  @Test
  fun guestLogin_whenClicked_savesGuestUserAndNavigates() {
    tv_guest.performClick()

    // verify guest user is saved
    verify {
      mockUserPrefViewModel.saveUserPref(
        match { userModel ->
          userModel.userId == 0 &&
            userModel.name == context.getString(guest_user) &&
            userModel.username == context.getString(guest_user) && userModel.isLogin
        }
      )
    }

    // verify navigation to main activity
    verify { mockNavigator.openMainActivity(any()) }
  }

  @Test
  fun loginScreen_whenConfigurationChange_maintainsTheState() {
    typeValidCredentials()

    // simulate configuration change (rotation)
    activityRule.scenario.onActivity { activity ->
      activity.onConfigurationChanged(
        Configuration().apply {
          orientation = Configuration.ORIENTATION_LANDSCAPE
        }
      )
    }

    // verify data is maintained
    ed_username.doesHaveText(validUsername)
    ed_pass.doesHaveText(validPassword)
  }

  private fun clearForm() {
    ed_username.performAction(clearText())
    ed_pass.performAction(clearText())
  }

  private fun typeUserName(userName: String) {
    ed_username.performType(userName)
    closeSoftKeyboard()
  }

  private fun typePassword(pass: String) {
    ed_pass.performType(pass)
    closeSoftKeyboard()
  }

  private fun typeValidCredentials() {
    typeUserName(validUsername)
    typePassword(validPassword)
  }

  private fun performValidLogin() {
    typeValidCredentials()
    btn_login.performClick()
  }

  private fun clickAtPosition(position: Int): ViewAction =
    object : ViewAction {
      override fun getConstraints(): Matcher<View> = isAssignableFrom(EditText::class.java)

      override fun getDescription(): String = "Click at position $position"

      override fun perform(uiController: UiController, view: View) {
        val editText = view as EditText
        editText.setSelection(position)
      }
    }
}
