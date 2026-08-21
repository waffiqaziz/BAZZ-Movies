package com.waffiq.bazz_movies.feature.search.ui

import androidx.test.platform.app.InstrumentationRegistry
import com.waffiq.bazz_movies.core.common.MediaType
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performClick
import com.waffiq.bazz_movies.feature.search.R.id.action_filter
import com.waffiq.bazz_movies.feature.search.testutils.BaseSearchFragmentTest
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.verify
import org.junit.Test

@HiltAndroidTest
class SearchFragmentFilterTest : BaseSearchFragmentTest() {

  @Test
  fun filter_byMovie_runsCorrectly() {
    performMovieFilter()
    verify { mockSearchViewModel.setFilters(setOf(MediaType.MOVIE)) }
  }

  @Test
  fun filter_byPerson_runsCorrectly() {
    action_filter.performClick()
    "Person".performClick()
    "Apply".performClick()
    verify { mockSearchViewModel.setFilters(setOf(MediaType.PERSON)) }
  }

  @Test
  fun filter_byTv_runsCorrectly() {
    action_filter.performClick()
    "TV Series".performClick()
    "Apply".performClick()
    verify { mockSearchViewModel.setFilters(setOf(MediaType.TV)) }
  }

  @Test
  fun filter_byMultipleTypes_runsCorrectly() {
    val selectedMediaType = setOf(MediaType.MOVIE, MediaType.TV)

    action_filter.performClick()
    "Movie".performClick()
    "TV Series".performClick()

    "Apply".performClick()
    filterFLow.value = selectedMediaType

    verify { mockSearchViewModel.setFilters(selectedMediaType) }
  }

  @Test
  fun filter_selectNothing_runsCorrectly() {
    action_filter.performClick()
    "Apply".performClick()
    verify { mockSearchViewModel.setFilters(setOf(MediaType.MULTI)) }
  }

  @Test
  fun filter_sameBadge_runsTwice() {
    performMovieFilter()
    performMovieFilter()
    verify(exactly = 2) { mockSearchViewModel.setFilters(setOf(MediaType.MOVIE)) }
  }

  @Test
  fun updateFilterBadge_whenSecondNonEmptySelection_reusesExistingBadge() {
    filterFLow.value = setOf(MediaType.MOVIE)
    // wait and let collect() process the value
    InstrumentationRegistry.getInstrumentation().waitForIdleSync()
    filterFLow.value = setOf(MediaType.TV)
    InstrumentationRegistry.getInstrumentation().waitForIdleSync()
  }

  @Test
  fun updateFilterBadge_whenSelectionClearedAfterBeingShown_hidesBadge() {
    filterFLow.value = setOf(MediaType.MOVIE)
    InstrumentationRegistry.getInstrumentation().waitForIdleSync()
    filterFLow.value = setOf(MediaType.MULTI)
    InstrumentationRegistry.getInstrumentation().waitForIdleSync()
  }

  private fun performMovieFilter() {
    action_filter.performClick()
    "Movie".performClick()
    "Apply".performClick()
  }
}
