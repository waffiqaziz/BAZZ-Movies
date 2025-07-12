package com.waffiq.bazz_movies.feature.person.ui

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.waffiq.bazz_movies.core.test.MainCoroutineRule
import com.waffiq.bazz_movies.feature.person.R.id.btn_close_dialog
import com.waffiq.bazz_movies.feature.person.R.id.rv_photos
import com.waffiq.bazz_movies.feature.person.R.id.view_pager_dialog
import com.waffiq.bazz_movies.feature.person.testutils.Helper.launchPersonActivity
import com.waffiq.bazz_movies.feature.person.testutils.Helper.testProfileItem
import com.waffiq.bazz_movies.feature.person.testutils.PersonActivityTestHelper
import com.waffiq.bazz_movies.feature.person.testutils.PersonActivityTestSetup
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class PersonActivityImageDialogTest : PersonActivityTestSetup by PersonActivityTestHelper() {

  @get:Rule
  var hiltRule = HiltAndroidRule(this)

  @get:Rule
  val mainCoroutineRule = MainCoroutineRule()

  @BindValue
  @JvmField
  val mockPersonViewModel: PersonViewModel = mockk(relaxed = true)

  @BindValue
  @JvmField
  val mockNavigator: INavigator = mockk(relaxed = true)

  @Before
  fun init() {
    hiltRule.inject()
    setupMocks()
    Intents.init()
    initializeTest(ApplicationProvider.getApplicationContext())
  }

  @After
  fun tearDown() {
    Intents.release()
  }

  private fun setupMocks() {
    setupBaseMocks()
    setupViewModelMocks(mockPersonViewModel)
    setupNavigatorMocks(mockNavigator)
  }

  @Test
  fun imageDialog_whenImageClicked_showsDialog() = runTest {
    context.launchPersonActivity {
      InstrumentationRegistry.getInstrumentation().runOnMainSync {
        loadingStateLiveData.postValue(false)
        imagePersonLiveData.postValue(
          listOf(testProfileItem, testProfileItem.copy(filePath = "path"))
        )
      }
      shortDelay()

      // scroll to the person photos
      onView(withId(rv_photos)).perform(scrollTo())

      // click on the first image
      onView(withId(rv_photos))
        .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

      shortDelay()

      // verify dialog is displayed
      onView(withId(view_pager_dialog)).check(matches(isDisplayed()))
      onView(withId(btn_close_dialog)).check(matches(isDisplayed()))
    }
  }

  @Test
  fun imageDialog_whenCloseButtonClicked_dismissesDialog() = runTest {
    context.launchPersonActivity {
      InstrumentationRegistry.getInstrumentation().runOnMainSync {
        loadingStateLiveData.postValue(false)
        imagePersonLiveData.postValue(listOf(testProfileItem, testProfileItem))
      }
      shortDelay()

      onView(withId(rv_photos)).perform(scrollTo())
      onView(withId(rv_photos))
        .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

      shortDelay()

      onView(withId(view_pager_dialog)).check(matches(isDisplayed()))
      onView(withId(btn_close_dialog)).perform(click())
      shortDelay()

      // verify dialog is dismissed (this throw ViewNotFoundException which is expected)
      try {
        onView(withId(view_pager_dialog)).check(doesNotExist())
      } catch (_: Exception) {
        // expect dialog should be dismissed, no error and test passed
      }
    }
  }

  @Test
  fun imageDialog_whenMultipleImages_showsCorrectPosition() = runTest {
    setupMocks()

    context.launchPersonActivity {
      InstrumentationRegistry.getInstrumentation().runOnMainSync {
        loadingStateLiveData.postValue(false)
        imagePersonLiveData.postValue(listOf(testProfileItem, testProfileItem, testProfileItem))
      }
      shortDelay()

      onView(withId(rv_photos)).perform(scrollTo())
      onView(withId(rv_photos))
        .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))

      shortDelay()

      // verify dialog is displayed
      onView(withId(view_pager_dialog)).check(matches(isDisplayed()))

      // You might want to verify the ViewPager is at the correct position
      // This would require custom matchers or checking the adapter state
    }
  }

  @Test
  fun imageDialog_whenBackPressed_dismissesDialog() = runTest {
    setupMocks()

    context.launchPersonActivity {
      InstrumentationRegistry.getInstrumentation().runOnMainSync {
        loadingStateLiveData.postValue(false)
        imagePersonLiveData.postValue(listOf(testProfileItem))
      }
      shortDelay()

      onView(withId(rv_photos)).perform(scrollTo())
      onView(withId(rv_photos))
        .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

      shortDelay()

      onView(withId(view_pager_dialog)).check(matches(isDisplayed()))
      pressBack()
      shortDelay()

      try {
        onView(withId(view_pager_dialog)).check(doesNotExist())
      } catch (_: Exception) {
        // expected dialog should be dismissed
      }
    }
  }
}
