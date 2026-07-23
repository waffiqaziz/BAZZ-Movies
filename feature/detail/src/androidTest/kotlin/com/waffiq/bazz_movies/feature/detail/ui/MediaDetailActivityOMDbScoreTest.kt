package com.waffiq.bazz_movies.feature.detail.ui

import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.doesHaveText
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isNotDisplayed
import com.waffiq.bazz_movies.feature.detail.R.id.tv_score_imdb
import com.waffiq.bazz_movies.feature.detail.R.id.tv_score_rotten_tomatoes
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.RatingsItem
import com.waffiq.bazz_movies.feature.detail.testutils.DataDumb.testOMDbDetails
import com.waffiq.bazz_movies.feature.detail.testutils.basetest.BaseMediaDetailActivityTest
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.update
import org.junit.Test

/**
 * Instrumented test for [MediaDetailActivity] that checks the display of OMDb scores.
 * This test verifies that the OMDb scores are displayed correctly based on various conditions.
 */
@HiltAndroidTest
class MediaDetailActivityOMDbScoreTest : BaseMediaDetailActivityTest() {

  @Test
  fun omdbScoreValue_withEmptyValue_hidesTheScore() {
    // omdb score empty
    context.launchMediaDetailActivity {
      uiState.update { s ->
        s.copy(
          omdbDetails = testOMDbDetails.copy(
            imdbRating = "",
            metascore = "",
            ratings = null,
          ),
        )
      }
      scoreViewIsHidden()
    }
  }

  @Test
  fun omdbScoreValue_withNullValue_hidesTheScore() {
    context.launchMediaDetailActivity {
      uiState.update { s ->
        s.copy(
          omdbDetails = testOMDbDetails.copy(
            imdbRating = null,
            metascore = null,
            ratings = null,
          ),
        )
      }
      scoreViewIsHidden()
    }
  }

  @Test
  fun omdbScoreValue_withEmptyRatings_hidesTheScore() {
    context.launchMediaDetailActivity {
      uiState.update { s ->
        s.copy(
          omdbDetails = testOMDbDetails.copy(
            imdbRating = null,
            metascore = null,
            ratings = emptyList(),
          ),
        )
      }
      scoreViewIsHidden()
    }
  }

  @Test
  fun omdbScoreValue_withValidRottenTomatoes_showsOMDbScoreCorrectly() {
    context.launchMediaDetailActivity {
      uiState.update { s ->
        s.copy(
          omdbDetails = testOMDbDetails.copy(
            imdbRating = "",
            metascore = "",
            ratings = listOf(RatingsItem(source = "Rotten Tomatoes", value = "90%")),
          ),
        )
      }
      tv_score_rotten_tomatoes.doesHaveText("90%")
    }
  }

  @Test
  fun omdbScoreValue_withRottenTomatoesNull_hidesTheScore() {
    context.launchMediaDetailActivity {
      uiState.update { s ->
        s.copy(
          omdbDetails = OMDbDetails(
            ratings = listOf(
              RatingsItem(source = "Rotten Tomatoes", value = null),
            ),
          ),
        )
      }
      rottenTomatoesIsHidden()
    }
  }

  @Test
  fun omdbScoreValue_withRottenTomatoesEmpty_hidesTheScore() {
    context.launchMediaDetailActivity {
      uiState.update { s ->
        s.copy(
          omdbDetails = OMDbDetails(
            ratings = listOf(
              RatingsItem(source = "Rotten Tomatoes", value = ""),
            ),
          ),
        )
      }
      rottenTomatoesIsHidden()
    }
  }

  private fun scoreViewIsHidden() {
    tv_score_imdb.isNotDisplayed()
    rottenTomatoesIsHidden()
  }

  private fun rottenTomatoesIsHidden() {
    tv_score_rotten_tomatoes.isNotDisplayed()
  }
}
