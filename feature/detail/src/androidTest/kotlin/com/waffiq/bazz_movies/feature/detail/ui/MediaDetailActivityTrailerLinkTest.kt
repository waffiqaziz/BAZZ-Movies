package com.waffiq.bazz_movies.feature.detail.ui

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.Intents
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isNotDisplayed
import com.waffiq.bazz_movies.core.utils.openurl.UriLauncher
import com.waffiq.bazz_movies.feature.detail.R.id.btn_play
import com.waffiq.bazz_movies.feature.detail.testutils.BaseMediaDetailActivityTest
import com.waffiq.bazz_movies.feature.detail.testutils.DataDumb.testMediaDetail
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.DetailUserPrefViewModel
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.MediaDetailViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.update
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

/**
 * Instrumented test for [MediaDetailActivity] trailer link functionality.
 * This test checks the behavior of the trailer link when different values are posted to it.
 */
@HiltAndroidTest
class MediaDetailActivityTrailerLinkTest : BaseMediaDetailActivityTest() {

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

  @Inject
  lateinit var mockUriLauncher: UriLauncher

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

    every { mockUriLauncher.launch(any()) } just Runs
  }

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
