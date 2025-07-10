package com.waffiq.bazz_movies.feature.login.ui

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.waffiq.bazz_movies.core.designsystem.R.string.guest_user
import com.waffiq.bazz_movies.core.domain.UserModel
import com.waffiq.bazz_movies.core.instrumentationtest.ViewMatcher.withDrawable
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
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.After
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

  @get:Rule
  var hiltRule = HiltAndroidRule(this)

  @get:Rule
  var activityRule = ActivityScenarioRule(LoginActivity::class.java)

  private lateinit var context: Context

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
    Intents.init()

    activityRule.scenario.onActivity { activity ->
      context = activity.applicationContext
    }

    every { mockAuthViewModel.errorState } returns errorStateLiveData
    every { mockAuthViewModel.loginState } returns loginStateLiveData
    every { mockAuthViewModel.userModel } returns userModelLiveData
    every { mockAuthViewModel.loadingState } returns loadingStateLiveData
    every { mockUserPrefViewModel.saveUserPref(any()) } just Runs
  }

  @After
  fun tearDown() {
    Intents.release()
  }

  @Test
  fun loginScreen_whenInitialized_showsAllViews() {
    onView(withId(btn_forget_password)).check(matches(isDisplayed()))
    onView(withId(layout_bazz_movies)).check(matches(isDisplayed()))
    onView(withId(ed_username)).check(matches(isDisplayed()))
    onView(withId(ed_pass)).check(matches(isDisplayed()))
    onView(withId(btn_eye)).check(matches(isDisplayed()))
    onView(withId(btn_login)).check(matches(isDisplayed()))
    onView(withId(tv_guest)).check(matches(isDisplayed()))
    onView(withId(tv_joinTMDB)).check(matches(isDisplayed()))
  }

  @Test
  fun loginScreen_whenInitialized_progressBarIsGone() {
    onView(withId(progress_bar)).check(matches(not(isDisplayed())))
  }

  @Test
  fun loginScreen_whenInitialized_showsButtonEyeOff() {
    onView(withId(btn_eye))
      .check(matches(allOf(isDisplayed(), withDrawable(ic_eye_off))))
  }

  @Test
  fun login_withoutUsernameAndPassword_showsErrorMessage() {
    // click login button without entering username/password
    onView(withId(btn_login)).perform(click())

    // verify that error messages are shown
    onView(withId(ed_username)).check(matches(withErrorText("Please enter a username")))
    onView(withId(ed_pass)).check(matches(withErrorText("Please enter a password")))
  }

  @Test
  fun login_withOnlyUsername_showsPasswordError() {
    onView(withId(ed_username)).perform(typeText(validUsername))
    closeSoftKeyboard()
    onView(withId(btn_login)).perform(click())

    onView(withId(ed_username)).check(matches(withoutError()))
    onView(withId(ed_pass)).check(matches(withErrorText("Please enter a password")))
  }

  @Test
  fun login_withOnlyPassword_showsUsernameError() {
    onView(withId(ed_pass)).perform(typeText(validPassword))
    closeSoftKeyboard()
    onView(withId(btn_login)).perform(click())

    onView(withId(ed_username)).check(matches(withErrorText("Please enter a username")))
    onView(withId(ed_pass)).check(matches(withoutError()))
  }

  @Test
  fun login_withBlankSpaces_showsErrorMessages() {
    onView(withId(ed_username)).perform(typeText("   "))
    closeSoftKeyboard()
    onView(withId(ed_pass)).perform(typeText("   "))
    closeSoftKeyboard()
    onView(withId(btn_login)).perform(click())

    onView(withId(ed_username)).check(matches(withErrorText("Please enter a username")))
    onView(withId(ed_pass)).check(matches(withErrorText("Please enter a password")))
  }

  @Test
  fun passwordButtonToggle_whenClicked_showsPassword() {
    onView(withId(ed_pass)).perform(typeText(validPassword))

    // unmask the edit text
    onView(withId(btn_eye)).perform(click())
    onView(withId(ed_pass)).check(matches(withText(validPassword)))

    onView(withId(btn_eye)).perform(click())
    onView(withId(ed_pass)).check(matches(isPasswordHidden())) // Masked password
  }

  @Test
  fun passwordToggle_whenMovingTheCursor_maintainsCursorPosition() {
    onView(withId(ed_pass)).perform(typeText(validPassword))

    // move cursor to middle
    onView(withId(ed_pass)).perform(clickAtPosition(Random.nextInt(1, 6)))

    // toggle password visibility
    onView(withId(btn_eye)).perform(click())

    // verify cursor position is maintained
    onView(withId(ed_pass)).check(matches(withText(validPassword)))
  }

  @Test
  fun textWatcher_removesUsernameError_whenTyping() {
    // create an error
    onView(withId(btn_login)).perform(click())
    onView(withId(ed_username)).check(matches(withErrorText("Please enter a username")))

    // type to remove error
    onView(withId(ed_username)).perform(typeText(validUsername))
    onView(withId(ed_username)).check(matches(withoutError()))
  }

  @Test
  fun textWatcher_removesPasswordError_whenTyping() {
    onView(withId(btn_login)).perform(click())
    onView(withId(ed_pass)).check(matches(withErrorText("Please enter a password")))

    onView(withId(ed_pass)).perform(typeText(validPassword))
    onView(withId(ed_pass)).check(matches(withoutError()))
  }

  @Test
  fun login_withInvalidCredential_returnToLoginActivity() {
    // enter invalid credentials
    onView(withId(ed_username)).perform(typeText("random"))
    closeSoftKeyboard()
    onView(withId(ed_pass)).perform(ViewActions.replaceText("random"))
    closeSoftKeyboard()

    onView(withId(btn_login)).perform(click())
    onView(withId(activity_login)).check(matches(isDisplayed()))
  }

  @Test
  fun login_withValidCredentials_disablesButtonsAndShowsLoading() {
    // mock successful login flow
    val loadingLiveData = MutableLiveData<Boolean>()
    every { mockAuthViewModel.loadingState } returns loadingLiveData

    onView(withId(ed_username)).perform(typeText(validUsername))
    closeSoftKeyboard()
    onView(withId(ed_pass)).perform(typeText(validPassword))
    closeSoftKeyboard()

    onView(withId(btn_login)).perform(click())

    // verify buttons are disabled during login
    onView(withId(btn_login)).check(matches(not(isEnabled())))
    onView(withId(tv_guest)).check(matches(not(isEnabled())))

    // simulate loading state
    activityRule.scenario.onActivity {
      loadingLiveData.postValue(true)
    }

    onView(withId(progress_bar)).check(matches(isDisplayed()))
  }

  @Test
  fun guestLogin_whenClicked_savesGuestUserAndNavigates() {
    onView(withId(tv_guest)).perform(click())

    // verify guest user is saved
    verify {
      mockUserPrefViewModel.saveUserPref(match { userModel ->
        userModel.userId == 0 &&
          userModel.name == context.getString(guest_user) &&
          userModel.username == context.getString(guest_user) && userModel.isLogin
      })
    }

    // verify navigation to main activity
    verify { mockNavigator.openMainActivity(any()) }
  }

  @Test
  fun forgetPasswordButton_whenClicked_opensWebBrowser() {
    // mock intent to verify URL opening
    val expectedIntent = hasAction(Intent.ACTION_VIEW)
    intending(expectedIntent).respondWith(ActivityResult(Activity.RESULT_OK, null))

    onView(withId(btn_forget_password)).perform(click())

    // verify intent was sent
    intended(expectedIntent)
  }

  @Test
  fun joinTMDBButton_whenClicked_opensWebBrowser() {
    // mock intent to verify URL opening
    val expectedIntent = hasAction(Intent.ACTION_VIEW)
    intending(expectedIntent).respondWith(ActivityResult(Activity.RESULT_OK, null))

    onView(withId(tv_joinTMDB)).perform(click())

    // verify intent was sent
    intended(expectedIntent)
  }

  @Test
  fun login_withCorrectParams_callsAuthenticationViewModel() {
    onView(withId(ed_username)).perform(typeText(validUsername))
    closeSoftKeyboard()
    onView(withId(ed_pass)).perform(typeText(validPassword))
    closeSoftKeyboard()

    onView(withId(btn_login)).perform(click())

    // verify the userLogin method is called with correct parameters
    verify { mockAuthViewModel.userLogin(validUsername, validPassword) }
  }

  @Test
  fun loginScreen_whenConfigurationChange_maintainsTheState() {
    onView(withId(ed_username)).perform(typeText(validUsername))
    closeSoftKeyboard()
    onView(withId(ed_pass)).perform(typeText(validPassword))
    closeSoftKeyboard()

    // simulate configuration change (rotation)
    activityRule.scenario.onActivity { activity ->
      activity.onConfigurationChanged(
        Configuration().apply {
          orientation = Configuration.ORIENTATION_LANDSCAPE
        }
      )
    }

    // verify data is maintained
    onView(withId(ed_username)).check(matches(withText(validUsername)))
    onView(withId(ed_pass)).check(matches(withText(validPassword)))
  }

  private fun withErrorText(expectedError: String): Matcher<View> {
    return object : BoundedMatcher<View, EditText>(EditText::class.java) {
      override fun describeTo(description: Description) {
        description.appendText("with error text: $expectedError")
      }

      override fun matchesSafely(editText: EditText): Boolean {
        return editText.error?.toString() == expectedError
      }
    }
  }

  private fun withoutError(): Matcher<View> {
    return object : BoundedMatcher<View, EditText>(EditText::class.java) {
      override fun describeTo(description: Description) {
        description.appendText("without error text")
      }

      override fun matchesSafely(editText: EditText): Boolean {
        return editText.error == null
      }
    }
  }

  private fun clickAtPosition(position: Int): ViewAction {
    return object : ViewAction {
      override fun getConstraints(): Matcher<View> {
        return isAssignableFrom(EditText::class.java)
      }

      override fun getDescription(): String {
        return "Click at position $position"
      }

      override fun perform(uiController: UiController, view: View) {
        val editText = view as EditText
        editText.setSelection(position)
      }
    }
  }

  private fun isPasswordHidden(): Matcher<View> {
    return object : BoundedMatcher<View, EditText>(EditText::class.java) {
      override fun describeTo(description: Description) {
        description.appendText("Password is hidden")
      }

      override fun matchesSafely(editText: EditText): Boolean {
        // returns true if password is hidden
        return editText.transformationMethod is PasswordTransformationMethod
      }
    }
  }
}
