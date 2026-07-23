package com.waffiq.bazz_movies.feature.detail.ui

import androidx.test.espresso.Espresso.pressBack
import com.waffiq.bazz_movies.core.instrumentationtest.CustomRecyclerViewActions.clickItemAt
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.doesNotExist
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
import com.waffiq.bazz_movies.feature.detail.R.id.btn_sidebar
import com.waffiq.bazz_movies.feature.detail.R.id.rv_keywords
import com.waffiq.bazz_movies.feature.detail.testutils.DataDumb.testMediaDetail
import com.waffiq.bazz_movies.feature.detail.testutils.DataDumb.testMediaItem
import com.waffiq.bazz_movies.feature.detail.testutils.basetest.BaseMediaDetailActivityTest
import com.waffiq.bazz_movies.navigation.ListArgs
import com.waffiq.bazz_movies.navigation.ListType.BY_KEYWORD
import com.waffiq.bazz_movies.navigation.MediaSource
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.verify
import org.junit.Test

/**
 * Instrumented test for [MediaDetailActivity] side sheet functionality.
 */
@HiltAndroidTest
class MediaDetailSideSheetTest : BaseMediaDetailActivityTest() {

  @Test
  fun sideSheet_whenPressBackButton_closeTheDialog() {
    context.launchMediaDetailActivity {
      btn_sidebar.performClick()
      shortDelay()

      // check some ui is shown correctly
      "music".isDisplayed()
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
            mediaType = MediaSource.Typed(testMediaItem.mediaType),
            title = testMediaDetail.keywords?.get(0)?.name.orEmpty(),
            id = testMediaDetail.keywords?.get(0)?.id ?: 0,
          ),
        )
      }
    }
  }
}
