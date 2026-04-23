package com.waffiq.bazz_movies.ui.login

import android.content.Context
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.waffiq.bazz_movies.R.id.activity_main
import com.waffiq.bazz_movies.core.designsystem.R.string.please_enter_a_password
import com.waffiq.bazz_movies.core.designsystem.R.string.please_enter_a_username
import com.waffiq.bazz_movies.core.instrumentationtest.CustomAssertions.isPasswordHidden
import com.waffiq.bazz_movies.core.instrumentationtest.CustomAssertions.withErrorText
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performType
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.replaceWithText
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.doesHaveText
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
import com.waffiq.bazz_movies.feature.login.R.id.activity_login
import com.waffiq.bazz_movies.feature.login.R.id.btn_eye
import com.waffiq.bazz_movies.feature.login.R.id.btn_forget_password
import com.waffiq.bazz_movies.feature.login.R.id.btn_login
import com.waffiq.bazz_movies.feature.login.R.id.et_pass
import com.waffiq.bazz_movies.feature.login.R.id.et_username
import com.waffiq.bazz_movies.feature.login.R.id.layout_bazz_movies
import com.waffiq.bazz_movies.feature.login.R.id.tv_guest
import com.waffiq.bazz_movies.feature.login.R.id.tv_joinTMDB
import com.waffiq.bazz_movies.feature.login.ui.LoginActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
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
    btn_forget_password.isDisplayed()
    layout_bazz_movies.isDisplayed()
    et_username.isDisplayed()
    et_pass.isDisplayed()
    btn_eye.isDisplayed()
    btn_login.isDisplayed()
    tv_guest.isDisplayed()
    tv_joinTMDB.isDisplayed()
  }

  @Test
  fun login_withoutUsernameAndPassword_showsErrorMessage() {
    // Click login button without entering username/password
    btn_login.performClick()

    // Verify that error messages are shown
    onView(withId(et_username))
      .check(matches(withErrorText(context.getString(please_enter_a_username))))
    onView(withId(et_pass))
      .check(matches(withErrorText(context.getString(please_enter_a_password))))
  }

  @Test
  fun triggerPasswordToggle_showsPassword() {
    onView(withId(et_pass)).perform(typeText("password123"))

    // unmask the edit text
    btn_eye.performClick()
    et_pass.doesHaveText("password123")

    btn_eye.performClick()
    onView(withId(et_pass)).check(matches(isPasswordHidden())) // Masked password
  }

//  @Test
//  fun testLoginWithValidCredentials() {
//    // Enter valid credentials
//    onView(withId(ed_username)).perform(typeText(TEST_USERNAME))
//    closeSoftKeyboard()
//    onView(withId(ed_pass)).perform(replaceText(TEST_PASS))
//    closeSoftKeyboard()
//
//    btn_login.performClick()
//    onView(withId(activity_main.isDisplayed()
//    Thread.sleep(500L)
//  }

  @Test
  fun login_withInvalidCredential_returnToLoginActivity() {
    // Enter invalid credentials
    et_username.performType("random")
    closeSoftKeyboard()
    et_pass.replaceWithText("random")
    closeSoftKeyboard()

    btn_login.performClick()
    activity_login.isDisplayed()
  }

  @Test
  fun login_asGuest_openMainActivity() {
    tv_guest.performClick()
    activity_main.isDisplayed()
    shortDelay()
  }
}
