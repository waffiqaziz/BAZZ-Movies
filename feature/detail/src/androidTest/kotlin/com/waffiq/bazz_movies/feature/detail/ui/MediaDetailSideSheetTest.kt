package com.waffiq.bazz_movies.feature.detail.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
import com.waffiq.bazz_movies.feature.detail.R.id.btn_sidebar
import com.waffiq.bazz_movies.feature.detail.R.id.rv_keywords
import com.waffiq.bazz_movies.feature.detail.testutils.DataDumb.testMediaDetail
import com.waffiq.bazz_movies.feature.detail.testutils.DataDumb.testMediaItem
import com.waffiq.bazz_movies.feature.detail.testutils.MediaDetailActivityTestHelper
import com.waffiq.bazz_movies.feature.detail.testutils.MediaDetailActivityTestSetup
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.DetailUserPrefViewModel
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.MediaDetailViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import com.waffiq.bazz_movies.navigation.ListArgs
import com.waffiq.bazz_movies.navigation.ListType.BY_KEYWORD
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Instrumented test for [MediaDetailActivity] side sheet functionality.
 */
@HiltAndroidTest
class MediaDetailSideSheetTest :
  MediaDetailActivityTestSetup by MediaDetailActivityTestHelper() {

  @get:Rule
  var hiltRule = HiltAndroidRule(this)

  @BindValue
  @JvmField
  val mockNavigator: INavigator = mockk(relaxed = true)

  @BindValue
  @JvmField
  val mockMediaDetailViewModel: MediaDetailViewModel = mockk(relaxed = true)

  @BindValue
  @JvmField
  val mockPrefViewModel: DetailUserPrefViewModel = mockk(relaxed = true)

  @Before
  fun setup() {
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
    setupObservables(mockMediaDetailViewModel)
    setupPreferencesViewModelMocks(mockPrefViewModel)
    setupMediaDetailViewModelMocks(mockMediaDetailViewModel)
    setupNavigatorMocks(mockNavigator)
  }

  @Test
  fun sideSheet_whenPressBackButton_closeTheDialog() {
    context.launchMediaDetailActivity {
      onView(withId(btn_sidebar)).perform(click())

      // check some ui is shown correctly
      onView(withText("name")).check(matches(isDisplayed()))
      onView(withText("animation")).check(matches(isDisplayed()))
      onView(withText("action")).check(doesNotExist())

      // perform close dialog
      pressBack()
      shortDelay()

      // check that animation not exist
      onView(withText("animation")).check(doesNotExist())
    }
  }

  @Test
  fun sideSheet_whenKeywordClicked_openListPage() {
    context.launchMediaDetailActivity {
      onView(withId(btn_sidebar)).perform(click())
      onView(withId(rv_keywords)).perform(
        RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
      )

      verify {
        mockNavigator.openList(
          context = any(),
          args = ListArgs(
            listType = BY_KEYWORD,
            mediaType = testMediaItem.mediaType,
            title = testMediaDetail.keywords?.get(0)?.name ?: "",
            keywordId = testMediaDetail.keywords?.get(0)?.id ?: 0,
          )
        )
      }
    }
  }

  @Test
  fun sideSheet_whenNullKeywords_doesnShowsTheKeywords() {
    context.launchMediaDetailActivity {
      onView(withId(btn_sidebar)).perform(click())
      onView(withId(rv_keywords)).perform(
        RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
      )

      verify {
        mockNavigator.openList(
          context = any(),
          args = ListArgs(
            listType = BY_KEYWORD,
            mediaType = testMediaItem.mediaType,
            title = testMediaDetail.keywords?.get(0)?.name ?: "",
            keywordId = testMediaDetail.keywords?.get(0)?.id ?: 0,
          )
        )
      }
    }
  }
}
