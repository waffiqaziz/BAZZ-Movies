package com.waffiq.bazz_movies.feature.detail.ui

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.waffiq.bazz_movies.core.designsystem.R.string.not_available
import com.waffiq.bazz_movies.feature.detail.R.id.tv_score_imdb
import com.waffiq.bazz_movies.feature.detail.R.id.tv_score_metascore
import com.waffiq.bazz_movies.feature.detail.R.id.tv_score_rotten_tomatoes
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.RatingsItem
import com.waffiq.bazz_movies.feature.detail.testutils.DataDumb.testOMDbDetails
import com.waffiq.bazz_movies.feature.detail.testutils.MediaDetailActivityTestHelper
import com.waffiq.bazz_movies.feature.detail.testutils.MediaDetailActivityTestSetup
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.DetailUserPrefViewModel
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.MediaDetailViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Instrumented test for [MediaDetailActivity] that checks the display of OMDb scores.
 * This test verifies that the OMDb scores are displayed correctly based on various conditions.
 */
@HiltAndroidTest
class MediaDetailActivityOMDbScoreTest :
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
  fun omdbScoreValue_withEmptyValue_showsOMDbScoreCorrectly() {
    // omdb score empty
    context.launchMediaDetailActivity {
      omdbResult.postValue(
        testOMDbDetails.copy(
          imdbRating = "",
          metascore = "",
          ratings = null
        )
      )
      onView(withId(tv_score_imdb))
        .check(matches(withText(context.getString(not_available))))
      onView(withId(tv_score_metascore))
        .check(matches(withText(context.getString(not_available))))
      onView(withId(tv_score_rotten_tomatoes))
        .check(matches(withText(context.getString(not_available))))
    }
  }

  @Test
  fun omdbScoreValue_withNullValue_showsOMDbScoreCorrectly() {
    context.launchMediaDetailActivity {
      omdbResult.postValue(
        testOMDbDetails.copy(
          imdbRating = null,
          metascore = null,
          ratings = null
        )
      )
      onView(withId(tv_score_imdb))
        .check(matches(withText(context.getString(not_available))))
      onView(withId(tv_score_metascore))
        .check(matches(withText(context.getString(not_available))))
      onView(withId(tv_score_rotten_tomatoes))
        .check(matches(withText(context.getString(not_available))))
    }
  }

  @Test
  fun omdbScoreValue_withEmptyRatings_showsOMDbScoreCorrectly() {
    context.launchMediaDetailActivity {
      omdbResult.postValue(
        testOMDbDetails.copy(
          imdbRating = null,
          metascore = null,
          ratings = emptyList()
        )
      )
      onView(withId(tv_score_imdb))
        .check(matches(withText(context.getString(not_available))))
      onView(withId(tv_score_metascore))
        .check(matches(withText(context.getString(not_available))))
      onView(withId(tv_score_rotten_tomatoes))
        .check(matches(withText(context.getString(not_available))))
    }
  }

  @Test
  fun omdbScoreValue_withValidRottenTomatoes_showsOMDbScoreCorrectly() {
    context.launchMediaDetailActivity {
      omdbResult.postValue(
        testOMDbDetails.copy(
          imdbRating = "",
          metascore = "",
          ratings = listOf(RatingsItem(source = "Rotten Tomatoes", value = "90%"))
        )
      )
      onView(withId(tv_score_rotten_tomatoes)).check(matches((isDisplayed())))
        .check(matches(withText("90%")))
    }
  }

  @Test
  fun omdbScoreValue_withRottenTomatoesNullValue_showsOMDbScoreCorrectly() {
    context.launchMediaDetailActivity {
      omdbResult.postValue(
        OMDbDetails(
          ratings = listOf(
            RatingsItem(source = "Rotten Tomatoes", value = null), // null value on rotten tomatoes
          )
        )
      )
      onView(withId(tv_score_rotten_tomatoes))
        .check(matches(withText(context.getString(not_available))))
    }
  }
}
