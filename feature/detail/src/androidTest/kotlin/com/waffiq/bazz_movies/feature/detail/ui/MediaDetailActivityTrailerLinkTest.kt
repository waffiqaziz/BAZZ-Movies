package com.waffiq.bazz_movies.feature.detail.ui

import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isNotDisplayed
import com.waffiq.bazz_movies.feature.detail.R.id.btn_play
import com.waffiq.bazz_movies.feature.detail.testutils.DataDumb.testMediaDetail
import com.waffiq.bazz_movies.feature.detail.testutils.basetest.BaseMediaDetailActivityTest
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.verify
import kotlinx.coroutines.flow.update
import org.junit.Test

/**
 * Instrumented test for [MediaDetailActivity] trailer link functionality.
 * This test checks the behavior of the trailer link when different values are posted to it.
 */
@HiltAndroidTest
class MediaDetailActivityTrailerLinkTest : BaseMediaDetailActivityTest() {

  @Test
  fun trailerLink_withMixedValue_showsTrailerCorrectly() {
    context.launchMediaDetailActivity {
      uiState.update { s -> s.copy(detail = testMediaDetail.copy(trailer = null)) }
      btn_play.isNotDisplayed()
      uiState.update { s -> s.copy(detail = testMediaDetail.copy(trailer = "")) }
      btn_play.isNotDisplayed()
      uiState.update { s -> s.copy(detail = testMediaDetail.copy(trailer = " ")) }
      btn_play.isNotDisplayed()
      uiState.update { s -> s.copy(detail = testMediaDetail) }
      btn_play.performClick()
      verify { mockUriLauncher.launch(any()) }
    }
  }
}
