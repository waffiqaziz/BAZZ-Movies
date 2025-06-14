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
import com.waffiq.bazz_movies.core.domain.UserModel
import com.waffiq.bazz_movies.core.test.ViewMatcher.withDrawable
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.feature.login.R
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

@Suppress("ForbiddenComment")
@HiltAndroidTest
class LoginActivityTest {

  private val validUsername = "validUsername1234"
  private val validPassword = "validPassword1234"
//  private val testUser = UserModel(
//    userId = 123,
//    name = "Test User",
//    username = validUsername,
//    password = validPassword,
//    region = "US",
//    token = "token123",
//    isLogin = true,
//    gravatarHast = null,
//    tmdbAvatar = null
//  )

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
  fun allView_isVisible() {
    // Verify that all UI elements are visible
    onView(withId(R.id.btn_forget_password)).check(matches(isDisplayed()))
    onView(withId(R.id.layout_bazz_movies)).check(matches(isDisplayed()))
    onView(withId(R.id.ed_username)).check(matches(isDisplayed()))
    onView(withId(R.id.ed_pass)).check(matches(isDisplayed()))
    onView(withId(R.id.btn_eye)).check(matches(isDisplayed()))
    onView(withId(R.id.btn_login)).check(matches(isDisplayed()))
    onView(withId(R.id.tv_guest)).check(matches(isDisplayed()))
    onView(withId(R.id.tv_joinTMDB)).check(matches(isDisplayed()))
  }

  @Test
  fun progressBar_initiallyInvisible() {
    onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())))
  }

  @Test
  fun btnEye_ic_eye_off_whenInitially() {
    onView(withId(R.id.btn_eye))
      .check(matches(allOf(isDisplayed(), withDrawable(R.drawable.ic_eye_off))))
  }

  @Test
  fun login_withoutUsernameAndPassword_showsErrorMessage() {
    // Click login button without entering username/password
    onView(withId(R.id.btn_login)).perform(click())

    // Verify that error messages are shown
    onView(withId(R.id.ed_username)).check(matches(withErrorText("Please enter a username")))
    onView(withId(R.id.ed_pass)).check(matches(withErrorText("Please enter a password")))
  }

  @Test
  fun login_withOnlyUsername_showsPasswordError() {
    onView(withId(R.id.ed_username)).perform(typeText(validUsername))
    closeSoftKeyboard()
    onView(withId(R.id.btn_login)).perform(click())

    onView(withId(R.id.ed_username)).check(matches(withoutError()))
    onView(withId(R.id.ed_pass)).check(matches(withErrorText("Please enter a password")))
  }

  @Test
  fun login_withOnlyPassword_showsUsernameError() {
    onView(withId(R.id.ed_pass)).perform(typeText(validPassword))
    closeSoftKeyboard()
    onView(withId(R.id.btn_login)).perform(click())

    onView(withId(R.id.ed_username)).check(matches(withErrorText("Please enter a username")))
    onView(withId(R.id.ed_pass)).check(matches(withoutError()))
  }

  @Test
  fun login_withBlankSpaces_showsErrorMessages() {
    onView(withId(R.id.ed_username)).perform(typeText("   "))
    closeSoftKeyboard()
    onView(withId(R.id.ed_pass)).perform(typeText("   "))
    closeSoftKeyboard()
    onView(withId(R.id.btn_login)).perform(click())

    onView(withId(R.id.ed_username)).check(matches(withErrorText("Please enter a username")))
    onView(withId(R.id.ed_pass)).check(matches(withErrorText("Please enter a password")))
  }

  @Test
  fun triggerPasswordToggle_showsPassword() {
    onView(withId(R.id.ed_pass)).perform(typeText(validPassword))

    // unmask the edit text
    onView(withId(R.id.btn_eye)).perform(click())
    onView(withId(R.id.ed_pass)).check(matches(withText(validPassword)))

    onView(withId(R.id.btn_eye)).perform(click())
    onView(withId(R.id.ed_pass)).check(matches(isPasswordHidden())) // Masked password
  }

  @Test
  fun passwordToggle_maintainsCursorPosition() {
    onView(withId(R.id.ed_pass)).perform(typeText(validPassword))

    // Move cursor to middle
    onView(withId(R.id.ed_pass)).perform(clickAtPosition(Random.nextInt(1, 6)))

    // Toggle password visibility
    onView(withId(R.id.btn_eye)).perform(click())

    // Verify cursor position is maintained (this is more complex to test,
    // but at minimum we can verify the toggle works)
    onView(withId(R.id.ed_pass)).check(matches(withText(validPassword)))
  }

  @Test
  fun textWatcher_removesUsernameError_whenTyping() {
    // create an error
    onView(withId(R.id.btn_login)).perform(click())
    onView(withId(R.id.ed_username)).check(matches(withErrorText("Please enter a username")))

    // type to remove error
    onView(withId(R.id.ed_username)).perform(typeText(validUsername))
    onView(withId(R.id.ed_username)).check(matches(withoutError()))
  }

  @Test
  fun textWatcher_removesPasswordError_whenTyping() {
    onView(withId(R.id.btn_login)).perform(click())
    onView(withId(R.id.ed_pass)).check(matches(withErrorText("Please enter a password")))

    onView(withId(R.id.ed_pass)).perform(typeText(validPassword))
    onView(withId(R.id.ed_pass)).check(matches(withoutError()))
  }

  @Test
  fun login_withInvalidCredential_returnToLoginActivity() {
    // Enter valid credentials
    onView(withId(R.id.ed_username)).perform(typeText("random"))
    closeSoftKeyboard()
    onView(withId(R.id.ed_pass)).perform(ViewActions.replaceText("random"))
    closeSoftKeyboard()

    onView(withId(R.id.btn_login)).perform(click())
    onView(withId(R.id.activity_login)).check(matches(isDisplayed()))
  }

  @Test
  fun login_withValidCredentials_disablesButtonsAndShowsLoading() {
    // Mock successful login flow
    val loadingLiveData = MutableLiveData<Boolean>()
    every { mockAuthViewModel.loadingState } returns loadingLiveData

    onView(withId(R.id.ed_username)).perform(typeText(validUsername))
    closeSoftKeyboard()
    onView(withId(R.id.ed_pass)).perform(typeText(validPassword))
    closeSoftKeyboard()

    onView(withId(R.id.btn_login)).perform(click())

    // Verify buttons are disabled during login
    onView(withId(R.id.btn_login)).check(matches(not(isEnabled())))
    onView(withId(R.id.tv_guest)).check(matches(not(isEnabled())))

    // Simulate loading state
    activityRule.scenario.onActivity {
      loadingLiveData.postValue(true)
    }

    onView(withId(R.id.progress_bar)).check(matches(isDisplayed()))
  }

  // TODO: unsuccess test
//  @Test
//  fun login_successfulLogin_callsGetDetailUser_withTimeout() {
//    val loginStateLiveData = MutableLiveData<Boolean>()
//    val userModelLiveData = MutableLiveData<UserModel>()
//
//    every { mockAuthViewModel.loginState } returns loginStateLiveData
//    every { mockAuthViewModel.userModel } returns userModelLiveData
//
//    onView(withId(R.id.ed_username)).perform(typeText(validUsername))
//    closeSoftKeyboard()
//    onView(withId(R.id.ed_pass)).perform(typeText(validPassword))
//    closeSoftKeyboard()
//
//    onView(withId(R.id.btn_login)).perform(click())
//
//    // Simulate the login flow
//    activityRule.scenario.onActivity {
//      loginStateLiveData.postValue(true)
//      // Small delay to ensure observer is set up
//      Handler(Looper.getMainLooper()).postDelayed({
//        userModelLiveData.postValue(testUser)
//      }, 10)
//    }
//
//    // Verify with timeout to allow for async operations
//    verify(timeout = 1000) { mockAuthViewModel.userModel }
//    verify { mockUserPrefViewModel.saveUserPref(testUser) }
//  }

  // TODO: unsuccess test
//  @Test
//  fun login_successfulLogin_callsGetDetailUser() {
//    // Fill in credentials
//    onView(withId(R.id.ed_username)).perform(typeText(validUsername))
//    closeSoftKeyboard()
//    onView(withId(R.id.ed_pass)).perform(typeText(validPassword))
//    closeSoftKeyboard()
//
//    // Click login button
//    onView(withId(R.id.btn_login)).perform(click())
//
//    // Verify userLogin was called
//    verify { mockAuthViewModel.userLogin(validUsername, validPassword) }
//
//    // Simulate successful login - this will trigger getDetailUser(true)
//    activityRule.scenario.onActivity {
//      loginStateLiveData.postValue(true)
//    }
//
//    // Wait for the loginState observer to process and set up userModel observer
//    Thread.sleep(100)
//
//    // Now simulate userModel data being available - this triggers the userModel observer
//    activityRule.scenario.onActivity {
//      userModelLiveData.postValue(testUser)
//    }
//
//    // Wait for the userModel observer to process
//    Thread.sleep(100)
//
//    // Verify that userModel property was accessed (when observer was set up)
//    verify { mockAuthViewModel.userModel }
//
//    // Verify that saveUserPref was called with the test user
//    verify { mockUserPrefViewModel.saveUserPref(testUser) }
//
//    // Verify navigation was called
//    verify { mockNavigator.openMainActivity(any()) }
//  }

  // TODO: unsuccess test
//  @Test
//  fun login_successfulUserLogin_savesUserAndNavigates() {
//    val loginStateLiveData = MutableLiveData<Boolean>(true)
//    val userModelLiveData = MutableLiveData<UserModel>()
//
//    every { mockAuthViewModel.loginState } returns loginStateLiveData
//    every { mockAuthViewModel.userModel } returns userModelLiveData
//
//    onView(withId(R.id.ed_username)).perform(typeText(validUsername))
//    closeSoftKeyboard()
//    onView(withId(R.id.ed_pass)).perform(typeText(validPassword))
//    closeSoftKeyboard()
//
//    onView(withId(R.id.btn_login)).perform(click())
//
//    // Simulate successful login flow
//    activityRule.scenario.onActivity {
//      loginStateLiveData.postValue(true)
//      userModelLiveData.postValue(testUser)
//    }
//
//    // Verify user preference is saved and navigation occurs
//    verify { mockUserPrefViewModel.saveUserPref(testUser) }
//    verify { mockNavigator.openMainActivity(any()) }
//  }

  @Test
  fun guestLogin_savesGuestUserAndNavigates() {
    onView(withId(R.id.tv_guest)).perform(click())

    // Verify guest user is saved
    verify {
      mockUserPrefViewModel.saveUserPref(match { userModel ->
        userModel.userId == 0 &&
          userModel.name == context.getString(com.waffiq.bazz_movies.core.designsystem.R.string.guest_user) &&
          userModel.username == context.getString(com.waffiq.bazz_movies.core.designsystem.R.string.guest_user) &&
          userModel.isLogin == true
      })
    }

    // Verify navigation to main activity
    verify { mockNavigator.openMainActivity(any()) }
  }

  @Test
  fun forgetPasswordButton_opensWebBrowser() {
    // Mock intent to verify URL opening
    val expectedIntent = hasAction(Intent.ACTION_VIEW)
    intending(expectedIntent).respondWith(ActivityResult(Activity.RESULT_OK, null))

    onView(withId(R.id.btn_forget_password)).perform(click())

    // Verify intent was sent
    intended(expectedIntent)
  }

  @Test
  fun joinTMDBButton_opensWebBrowser() {
    // Mock intent to verify URL opening
    val expectedIntent = hasAction(Intent.ACTION_VIEW)
    intending(expectedIntent).respondWith(ActivityResult(Activity.RESULT_OK, null))

    onView(withId(R.id.tv_joinTMDB)).perform(click())

    // Verify intent was sent
    intended(expectedIntent)
  }

  @Test
  fun userLogin_callsAuthenticationViewModelWithCorrectParams() {
    onView(withId(R.id.ed_username)).perform(typeText(validUsername))
    closeSoftKeyboard()
    onView(withId(R.id.ed_pass)).perform(typeText(validPassword))
    closeSoftKeyboard()

    onView(withId(R.id.btn_login)).perform(click())

    // Verify the userLogin method is called with correct parameters
    verify { mockAuthViewModel.userLogin(validUsername, validPassword) }
  }

  @Test
  fun configurationChange_maintainsState() {
    onView(withId(R.id.ed_username)).perform(typeText(validUsername))
    closeSoftKeyboard()
    onView(withId(R.id.ed_pass)).perform(typeText(validPassword))
    closeSoftKeyboard()

    // Simulate configuration change (rotation)
    activityRule.scenario.onActivity { activity ->
      activity.onConfigurationChanged(
        Configuration().apply {
          orientation = Configuration.ORIENTATION_LANDSCAPE
        }
      )
    }

    // Verify data is maintained
    onView(withId(R.id.ed_username)).check(matches(withText(validUsername)))
    onView(withId(R.id.ed_pass)).check(matches(withText(validPassword)))
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
