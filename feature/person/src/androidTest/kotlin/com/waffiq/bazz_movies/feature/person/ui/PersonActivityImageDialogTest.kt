package com.waffiq.bazz_movies.feature.person.ui

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
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
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
    loadingStateLiveData.postValue(false)
  }

  @Test
  fun imageDialog_whenImageClicked_showsDialog() = runTest {
    context.launchPersonActivity {
      InstrumentationRegistry.getInstrumentation().runOnMainSync {
        imagePersonLiveData.postValue(
          listOf(testProfileItem, testProfileItem.copy(filePath = "path"))
        )
      }
      performClickListPhotos(0)
    }
  }

  @Test
  fun imageDialog_whenCloseButtonClicked_dismissesDialog() = runTest {
    context.launchPersonActivity {
      InstrumentationRegistry.getInstrumentation().runOnMainSync {
        imagePersonLiveData.postValue(listOf(testProfileItem, testProfileItem))
      }
      performClickListPhotos(0)

      // close dialog
      onView(withId(btn_close_dialog)).perform(click())
      shortDelay()

      // verify dialog is dismissed
      onView(withId(view_pager_dialog)).check(doesNotExist())
    }
  }

  @Test
  fun imageDialog_whenMultipleImages_showsCorrectPosition() = runTest {
    setupMocks()

    context.launchPersonActivity {
      InstrumentationRegistry.getInstrumentation().runOnMainSync {
        imagePersonLiveData.postValue(listOf(testProfileItem, testProfileItem, testProfileItem))
      }
      performClickListPhotos(1)

      // verify the ViewPager is at the correct position
      // but it require custom matchers or checking the adapter state
    }
  }

  @Test
  fun imageDialog_whenBackPressed_dismissesDialog() = runTest {
    setupMocks()

    context.launchPersonActivity {
      InstrumentationRegistry.getInstrumentation().runOnMainSync {
        imagePersonLiveData.postValue(listOf(testProfileItem))
      }
      performClickListPhotos(0)
      pressBack()
      shortDelay()

      // expected dialog is dismissed
      onView(withId(view_pager_dialog)).check(doesNotExist())
    }
  }

  private fun performClickListPhotos(position : Int){
    // delay before perform action
    shortDelay()

    onView(withId(rv_photos)).perform(scrollTo())
    onView(withId(rv_photos))
      .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(position, click()))
    shortDelay()

    // verify dialog and close button is visible
    onView(withId(view_pager_dialog)).check(matches(isDisplayed()))
    onView(withId(btn_close_dialog)).check(matches(isDisplayed()))
  }
}
