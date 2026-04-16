package com.waffiq.bazz_movies.feature.detail.ui

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.intent.Intents
import com.waffiq.bazz_movies.core.instrumentationtest.CustomRecyclerViewActions.clickItemAt
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.doesNotExist
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isDisplayed
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
      btn_sidebar.performClick()

      // check some ui is shown correctly
      "name".isDisplayed()
      "animation".isDisplayed()
      "action".doesNotExist()

      // perform close dialog
      pressBack()
      shortDelay()

      // check that animation not exist
      "animation".doesNotExist()
    }
  }

  @Test
  fun sideSheet_whenKeywordClicked_openListPage() {
    context.launchMediaDetailActivity {
      btn_sidebar.performClick()
      rv_keywords.clickItemAt(0)

      verify {
        mockNavigator.openList(
          context = any(),
          args = ListArgs(
            listType = BY_KEYWORD,
            mediaType = testMediaItem.mediaType,
            title = testMediaDetail.keywords?.get(0)?.name.orEmpty(),
            id = testMediaDetail.keywords?.get(0)?.id ?: 0,
          )
        )
      }
    }
  }

  @Test
  fun sideSheet_whenNullKeywords_doesnShowsTheKeywords() {
    context.launchMediaDetailActivity {
      btn_sidebar.performClick()
      rv_keywords.clickItemAt(0)

      verify {
        mockNavigator.openList(
          context = any(),
          args = ListArgs(
            listType = BY_KEYWORD,
            mediaType = testMediaItem.mediaType,
            title = testMediaDetail.keywords?.get(0)?.name.orEmpty(),
            id = testMediaDetail.keywords?.get(0)?.id ?: 0,
          )
        )
      }
    }
  }
}
