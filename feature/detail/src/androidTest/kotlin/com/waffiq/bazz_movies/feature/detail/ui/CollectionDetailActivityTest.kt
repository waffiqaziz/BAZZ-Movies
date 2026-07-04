package com.waffiq.bazz_movies.feature.detail.ui

import androidx.lifecycle.Lifecycle.State.DESTROYED
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.uiautomator.UiAutomatorTestScope
import androidx.test.uiautomator.textAsString
import androidx.test.uiautomator.uiAutomator
import com.waffiq.bazz_movies.core.designsystem.R.id.btn_try_again
import com.waffiq.bazz_movies.core.designsystem.R.string.popularity_asc
import com.waffiq.bazz_movies.core.designsystem.R.string.popularity_desc
import com.waffiq.bazz_movies.core.designsystem.R.string.rating_asc
import com.waffiq.bazz_movies.core.designsystem.R.string.rating_desc
import com.waffiq.bazz_movies.core.designsystem.R.string.release_date_newest
import com.waffiq.bazz_movies.core.designsystem.R.string.release_date_olders
import com.waffiq.bazz_movies.core.designsystem.R.string.sort
import com.waffiq.bazz_movies.core.designsystem.R.string.sort_by
import com.waffiq.bazz_movies.core.designsystem.R.string.title_az
import com.waffiq.bazz_movies.core.designsystem.R.string.title_za
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performTextClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isNotDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.CustomVisibilityMatchers.isTextVisible
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
import com.waffiq.bazz_movies.feature.detail.R.id.btn_back
import com.waffiq.bazz_movies.feature.detail.R.id.btn_sort
import com.waffiq.bazz_movies.feature.detail.R.id.header_layout
import com.waffiq.bazz_movies.feature.detail.R.id.rv_collection_parts
import com.waffiq.bazz_movies.feature.detail.R.id.rv_genre
import com.waffiq.bazz_movies.feature.detail.testutils.BaseCollectionDetailActivityTest
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.CollectionViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import io.mockk.verify
import org.hamcrest.Matchers.allOf
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Instrumented test for [CollectionDetailActivity] that checks the visibility of UI elements
 * and the correct display of media details based on various conditions.
 */
@HiltAndroidTest
class CollectionDetailActivityTest : BaseCollectionDetailActivityTest() {

  @get:Rule
  var hiltRule = HiltAndroidRule(this)

  @BindValue
  @JvmField
  val mockNavigator: INavigator = mockk(relaxed = true)

  @BindValue
  @JvmField
  val mockCollectionViewModel: CollectionViewModel = mockk(relaxed = true)

  @Before
  override fun setup() {
    hiltRule.inject()
    setupBaseMocks(mockCollectionViewModel)
    setupNavigatorMocks(mockNavigator)
    initializeTest(ApplicationProvider.getApplicationContext())
  }

  @Test
  fun detailScreen_whenAllDataProvided_showsAllViews() {
    context.launchCollectionDetailActivity {
      btn_back.isDisplayed()
      rv_genre.isDisplayed()
      header_layout.isDisplayed()
      rv_collection_parts.isDisplayed()
    }
  }

  @Test
  fun detailScreen_whenError_showsErrorViews() {
    context.launchCollectionDetailActivity {
      updateState { copy(name = "", isError = false, isLoading = false) }
      shortDelay()

      updateState { copy(name = "", isError = true, isLoading = true) }
      shortDelay()

      updateState { copy(isError = true) }
      shortDelay()

      updateState { copy(name = "something", isError = true, isLoading = false) }
      shortDelay()

      btn_back.isDisplayed()
      rv_genre.isNotDisplayed()
      header_layout.isNotDisplayed()
      rv_collection_parts.isNotDisplayed()

      btn_try_again.performClick()
      verify { mockCollectionViewModel.loadMovieCollection(any()) }
    }
  }

  @Test
  fun sortCollection_successful_runsCorrectly() {
    context.launchCollectionDetailActivity {
      val btnSort = onView(
        allOf(
          withId(btn_sort),
          isDescendantOfA(withId(header_layout)),
        ),
      )
      btnSort.perform(scrollTo())
      btnSort.perform(click())
      shortDelay()

      // check all sort option
      sort_by.isTextVisible()
      title_az.isTextVisible()
      title_za.isTextVisible()
      rating_desc.isTextVisible()
      rating_asc.isTextVisible()
      popularity_desc.isTextVisible()
      popularity_asc.isTextVisible()
      release_date_newest.isTextVisible()
      release_date_olders.isTextVisible()

      // perform sort
      popularity_asc.performTextClick()
      shortDelay(500)

      verify { mockCollectionViewModel.applySort(any()) }
    }
  }

  @Test
  fun scrollView_performScrolling_runsWithoutError() {
    // test the scroll behavior towards the collapse toolbar and collapse sort works as excpected
    context.launchCollectionDetailActivity {
      uiAutomator {
        // should run scroll down and up without error
        performScrollDown()
        performScrollUp()
      }
    }
  }

  @Test
  fun sorting_withCollapsedHeader_runsCorrectly() {
    // test the collapsed sort button is visible and clickable
    context.launchCollectionDetailActivity {
      uiAutomator {
        performScrollDown()

        onElement { textAsString() == context.getString(sort) }.click()
        device.waitForIdle()
        onElement { textAsString() == context.getString(title_az) }.click()
        device.waitForIdle()

        // wait for the debounce time
        shortDelay(500)

        verify { mockCollectionViewModel.applySort(any()) }
      }
    }
  }

  @Test
  fun buttonBack_whenClicked_finishTheActivity() {
    context.launchCollectionDetailActivity { scenario ->
      btn_back.performClick()

      scenario.moveToState(DESTROYED)
      assertTrue(scenario.state == DESTROYED)
    }
  }

  @Test
  fun extractDataFromIntent_returnsFalse_whenExtraMissing() {
    context.launchNullCollectionDetailActivity(null) { scenario ->
      assertTrue(scenario.state == DESTROYED)
    }
  }

  private fun UiAutomatorTestScope.performScrollDown() {
    device.swipe(
      device.displayWidth / 2,
      device.displayHeight * 3 / 4,
      device.displayWidth / 2,
      device.displayHeight / 4,
      20,
    )
    device.waitForIdle()
  }

  private fun UiAutomatorTestScope.performScrollUp() {
    device.swipe(
      device.displayWidth / 2,
      device.displayHeight / 3,
      device.displayWidth / 2,
      device.displayHeight * 9 / 10,
      30,
    )
    device.waitForIdle()
  }
}
