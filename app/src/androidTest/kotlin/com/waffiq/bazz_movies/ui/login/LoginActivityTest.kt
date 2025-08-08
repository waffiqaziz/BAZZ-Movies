package com.waffiq.bazz_movies.ui.login

import android.content.Context
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.waffiq.bazz_movies.R.id.activity_main
import com.waffiq.bazz_movies.core.designsystem.R.string.please_enter_a_password
import com.waffiq.bazz_movies.core.designsystem.R.string.please_enter_a_username
import com.waffiq.bazz_movies.feature.login.R.id.activity_login
import com.waffiq.bazz_movies.feature.login.R.id.btn_eye
import com.waffiq.bazz_movies.feature.login.R.id.btn_forget_password
import com.waffiq.bazz_movies.feature.login.R.id.btn_login
import com.waffiq.bazz_movies.feature.login.R.id.ed_pass
import com.waffiq.bazz_movies.feature.login.R.id.ed_username
import com.waffiq.bazz_movies.feature.login.R.id.layout_bazz_movies
import com.waffiq.bazz_movies.feature.login.R.id.tv_guest
import com.waffiq.bazz_movies.feature.login.R.id.tv_joinTMDB
import com.waffiq.bazz_movies.feature.login.ui.LoginActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class LoginActivityTest {

  @get:Rule
  var hiltRule = HiltAndroidRule(this)

  @get:Rule
  var activityRule = ActivityScenarioRule(LoginActivity::class.java)

  private lateinit var context: Context

  @Before
  fun init() {
    hiltRule.inject()

    activityRule.scenario.onActivity { activity ->
      context = activity.applicationContext
    }
  }

  @Test
  fun allView_isVisible() {
    // Verify that all UI elements are visible
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
  fun login_withoutUsernameAndPassword_showsErrorMessage() {
    // Click login button without entering username/password
    onView(withId(btn_login)).perform(click())

    // Verify that error messages are shown
    onView(withId(ed_username))
      .check(matches(withErrorText(context.getString(please_enter_a_username))))
    onView(withId(ed_pass))
      .check(matches(withErrorText(context.getString(please_enter_a_password))))
  }

  @Test
  fun triggerPasswordToggle_showsPassword() {
    onView(withId(ed_pass)).perform(typeText("password123"))

    // unmask the edit text
    onView(withId(btn_eye)).perform(click())
    onView(withId(ed_pass)).check(matches(withText("password123")))

    onView(withId(btn_eye)).perform(click())
    onView(withId(ed_pass)).check(matches(isPasswordHidden())) // Masked password
  }

//  @Test
//  fun testLoginWithValidCredentials() {
//    // Enter valid credentials
//    onView(withId(ed_username)).perform(typeText(TEST_USERNAME))
//    closeSoftKeyboard()
//    onView(withId(ed_pass)).perform(ViewActions.replaceText(TEST_PASS))
//    closeSoftKeyboard()
//
//    onView(withId(btn_login)).perform(click())
//    onView(withId(activity_main)).check(matches(isDisplayed()))
//    Thread.sleep(500L)
//  }

  @Test
  fun login_withInvalidCredential_returnToLoginActivity() {
    // Enter valid credentials
    onView(withId(ed_username)).perform(typeText("random"))
    closeSoftKeyboard()
    onView(withId(ed_pass)).perform(ViewActions.replaceText("random"))
    closeSoftKeyboard()

    onView(withId(btn_login)).perform(click())
    onView(withId(activity_login)).check(matches(isDisplayed()))
  }

  @Test
  fun login_asGuest_openMainActivity() {
    onView(withId(tv_guest)).perform(click())
    onView(withId(activity_main)).check(matches(isDisplayed()))
    onView(isRoot()).perform(waitFor())
  }

  private fun withErrorText(expectedError: String): Matcher<View> {
    return object : BoundedMatcher<View, EditText>(EditText::class.java) {
      override fun describeTo(description: Description) {
        description.appendText("with error text: $expectedError")
      }

      override fun matchesSafely(editText: EditText): Boolean =
        editText.error?.toString() == expectedError
    }
  }

  private fun isPasswordHidden(): Matcher<View> {
    return object : BoundedMatcher<View, EditText>(EditText::class.java) {
      override fun describeTo(description: Description) {
        description.appendText("Password is hidden")
      }

      // returns true if password is hidden
      override fun matchesSafely(editText: EditText): Boolean =
        editText.transformationMethod is PasswordTransformationMethod
    }
  }

  private fun waitFor(delay: Long = 1000L): ViewAction = object : ViewAction {
    override fun getConstraints(): Matcher<View> = isRoot()
    override fun getDescription(): String = "Wait for $delay milliseconds."
    override fun perform(uiController: UiController, view: View) {
      uiController.loopMainThreadForAtLeast(delay)
    }
  }
}
