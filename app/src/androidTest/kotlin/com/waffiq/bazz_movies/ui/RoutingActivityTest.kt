package com.waffiq.bazz_movies.ui

import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.waffiq.bazz_movies.MainActivity
import com.waffiq.bazz_movies.RoutingActivity
import com.waffiq.bazz_movies.core.user.data.model.UserModel
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.feature.login.ui.LoginActivity
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class RoutingActivityTest {

  @get:Rule
  var hiltRule = HiltAndroidRule(this)

  @BindValue
  lateinit var mockViewModel: UserPreferenceViewModel

  private val mockLiveData = MutableLiveData<UserModel>()

  @Before
  fun setUp() {
    hiltRule.inject()

    // Mock ViewModel with mockk
    mockViewModel = mockk(relaxed = true)

    // Stub LiveData behavior
    every { mockViewModel.getUserPref() } returns mockLiveData
  }

  @Test
  fun test_navigates_to_MainActivity_when_user_is_logged_in() {
    Intents.init()

    // Provide mock LiveData value
    mockLiveData.postValue(
      UserModel(
        userId = 111,
        name = "name_testing",
        username = "username_testing",
        password = "password_testing",
        region = "JP",
        token = "token_testing",
        isLogin = true,
        gravatarHast = null,
        tmdbAvatar = null
      )
    )

    // Launch activity
    val scenario = ActivityScenario.launch(RoutingActivity::class.java)

    // Verify navigation to MainActivity
    intended(hasComponent(MainActivity::class.java.name))

    Intents.release()
    scenario.close()
  }

  @Test
  fun test_navigates_to_MainActivity_when_user_is_not_logged_in() {
    Intents.init()

    // Provide mock LiveData value
    mockLiveData.postValue(
      UserModel(
        userId = 0,
        name = "not_logged",
        username = "not_logged",
        password = "not_logged",
        region = "not_logged",
        token = "not_logged",
        isLogin = false,
        gravatarHast = null,
        tmdbAvatar = null
      )
    )

    // Launch activity
    val scenario = ActivityScenario.launch(RoutingActivity::class.java)

    // Verify navigation to MainActivity
    intended(hasComponent(LoginActivity::class.java.name))

    Intents.release()
    scenario.close()
  }
}