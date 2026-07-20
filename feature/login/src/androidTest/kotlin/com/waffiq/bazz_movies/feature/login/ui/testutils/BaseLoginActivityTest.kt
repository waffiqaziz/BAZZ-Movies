package com.waffiq.bazz_movies.feature.login.ui.testutils

import android.content.Context
import android.view.View
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.intent.rule.IntentsRule
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performAction
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performType
import com.waffiq.bazz_movies.feature.login.R.id.btn_login
import com.waffiq.bazz_movies.feature.login.R.id.et_pass
import com.waffiq.bazz_movies.feature.login.R.id.et_username
import com.waffiq.bazz_movies.feature.login.ui.LoginActivity
import com.waffiq.bazz_movies.feature.login.ui.LoginViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.testing.HiltAndroidRule
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

abstract class BaseLoginActivityTest {

  protected val validUsername = "validUsername1234"
  protected val validPassword = "validPassword1234"

  protected val loginStateLiveData = MutableLiveData<Boolean>()
  protected val errorStateLiveData = MutableLiveData<String>()
  protected val loadingStateLiveData = MutableLiveData<Boolean>()

  protected lateinit var context: Context
  protected lateinit var scenario: ActivityScenario<LoginActivity>

  @get:Rule(order = 0)
  var hiltRule = HiltAndroidRule(this)

  @get:Rule(order = 1)
  var intentsRule = IntentsRule()

  @Inject
  lateinit var mockNavigator: INavigator

  @Inject
  lateinit var mockLoginViewModel: LoginViewModel

  @Before
  open fun setup() {
    hiltRule.inject()

    every { mockLoginViewModel.errorState } returns errorStateLiveData
    every { mockLoginViewModel.loginState } returns loginStateLiveData
    every { mockLoginViewModel.loadingState } returns loadingStateLiveData
    every { mockLoginViewModel.loadingState } returns loadingStateLiveData
    every { mockLoginViewModel.userLogin(any(), any()) } just Runs
    every { mockLoginViewModel.saveGuestUserPref(any(), any()) } just Runs

    scenario = ActivityScenario.launch(LoginActivity::class.java)

    scenario.onActivity { activity ->
      context = activity.applicationContext
    }
  }

  @After
  fun tearDown() {
    scenario.close()
  }

  protected fun clearForm() {
    et_username.performAction(clearText())
    et_pass.performAction(clearText())
  }

  protected fun typeUserName(userName: String) {
    et_username.performType(userName)
    closeSoftKeyboard()
  }

  protected fun typePassword(pass: String) {
    et_pass.performType(pass)
    closeSoftKeyboard()
  }

  protected fun typeValidCredentials() {
    typeUserName(validUsername)
    typePassword(validPassword)
  }

  protected fun performValidLogin() {
    typeValidCredentials()
    btn_login.performClick()
  }

  protected fun clickAtPosition(position: Int): ViewAction =
    object : ViewAction {
      override fun getConstraints(): Matcher<View> = isAssignableFrom(EditText::class.java)

      override fun getDescription(): String = "Click at position $position"

      override fun perform(uiController: UiController, view: View) {
        val editText = view as EditText
        editText.setSelection(position)
      }
    }
}
