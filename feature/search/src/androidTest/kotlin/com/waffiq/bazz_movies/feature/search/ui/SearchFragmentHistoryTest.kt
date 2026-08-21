package com.waffiq.bazz_movies.feature.search.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import com.waffiq.bazz_movies.core.designsystem.R.string.clear_all
import com.waffiq.bazz_movies.core.instrumentationtest.CustomRecyclerViewActions.actionOnItemAt
import com.waffiq.bazz_movies.core.instrumentationtest.CustomRecyclerViewActions.clickItemAt
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performSwipeLeft
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performTextClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.clickChildViewWithId
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isNotDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
import com.waffiq.bazz_movies.feature.search.R.id.btn_delete
import com.waffiq.bazz_movies.feature.search.R.id.rv_search_history
import com.waffiq.bazz_movies.feature.search.R.id.rv_search_history_row1
import com.waffiq.bazz_movies.feature.search.R.id.search_view
import com.waffiq.bazz_movies.feature.search.testutils.BaseSearchFragmentTest
import com.waffiq.bazz_movies.feature.search.testutils.DummyData.history1
import com.waffiq.bazz_movies.feature.search.testutils.DummyData.history2
import com.waffiq.bazz_movies.feature.search.testutils.DummyData.history3
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.verify
import org.junit.Test

@HiltAndroidTest
class SearchFragmentHistoryTest : BaseSearchFragmentTest() {

  @Test
  fun searchWithHistory_whenSameQueryAsCurrentSearch_stillDelegatesToViewModel() {
    historyFlow.value = listOf(history1.copy(query = testQuery))

    performClickSearchAction()
    performTypeAndSearchAction()

    performClickSearchAction()
    rv_search_history.clickItemAt(0)

    verify(exactly = 2) { mockSearchViewModel.search(testQuery) }
  }

  @Test
  fun clickSearchHistory_whenHistoryAvailable_shouldSearchWithSelectedQuery() {
    performClickSearchAction()

    "dear".performClick(search_view)
    verify { mockSearchViewModel.search("dear") }
  }

  @Test
  fun deleteSearchHistory_whenHistoryAvailable_shouldDeleteTheSelected() {
    performClickSearchAction()

    "transformers".isDisplayed(search_view)

    every { mockSearchViewModel.deleteHistory(any()) } answers {
      historyFlow.value = listOf(history2, history3)
    }

    rv_search_history.actionOnItemAt(0, clickChildViewWithId(btn_delete))
    shortDelay()

    "transformers".isNotDisplayed(search_view)

    verify {
      mockSearchViewModel.deleteHistory(match { it.query == "transformers" })
    }
  }

  @Test
  fun clearAllHistory_whenHistoryAvailable_shouldDeleteAllTheHistory() {
    performClickSearchAction()

    // check initial history inside the search view
    "transformers".isDisplayed(search_view)

    // perform clear all
    every { mockSearchViewModel.deleteAllHistory() } answers {
      historyFlow.value = emptyList()
    }
    clear_all.performTextClick()
    shortDelay()

    // check the history is cleared
    "transformers".isNotDisplayed()

    // also check the history on browse page
    onView(withContentDescription("Back")).perform(click())
    shortDelay()
    "transformers".isNotDisplayed()

    verify { mockSearchViewModel.deleteAllHistory() }
  }

  @Test
  fun horizontalHistory_whenSwiping_shouldSwipeTogether() {
    rv_search_history_row1.performSwipeLeft()
  }
}
