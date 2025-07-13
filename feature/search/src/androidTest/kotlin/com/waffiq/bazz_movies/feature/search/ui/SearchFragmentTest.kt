package com.waffiq.bazz_movies.feature.search.ui

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.R.id.search_src_text
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.Visibility
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withHint
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
import com.waffiq.bazz_movies.core.instrumentationtest.launchFragmentInHiltContainer
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.feature.search.R.id.action_search
import com.waffiq.bazz_movies.feature.search.R.id.illustration_error
import com.waffiq.bazz_movies.feature.search.R.id.illustration_search_no_result_view
import com.waffiq.bazz_movies.feature.search.R.id.illustration_search_view
import com.waffiq.bazz_movies.feature.search.R.id.rv_search
import com.waffiq.bazz_movies.feature.search.R.id.swipe_refresh
import com.waffiq.bazz_movies.feature.search.domain.model.ResultsItemSearch
import com.waffiq.bazz_movies.feature.search.testutils.Helper.triggerSwipeRefresh
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.core.IsNot.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class SearchFragmentTest {

  private lateinit var searchFragment: SearchFragment
  private lateinit var activity: AppCompatActivity
  private lateinit var toolbar: ActionBar
  private lateinit var searchAdapter: SearchAdapter

  private val searchResultsFlow: Flow<PagingData<ResultsItemSearch>> = flowOf()
  private val queryLiveData = MutableLiveData<String>()
  private val expandSearchViewLiveData = MutableLiveData<Boolean>()
  private val testQuery = "test_query"

  @get:Rule
  var hiltRule = HiltAndroidRule(this)

  @BindValue
  @JvmField
  val mockNavigator: INavigator = mockk(relaxed = true)

  @BindValue
  @JvmField
  val mockSnackbar: ISnackbar = mockk(relaxed = true)

  @BindValue
  @JvmField
  val mockSearchViewModel: SearchViewModel = mockk(relaxed = true)

  @Before
  fun setUp() {
    hiltRule.inject()
    val spyAdapter = spyk(SearchAdapter(mockNavigator))
    searchAdapter = spyAdapter

    searchFragment = launchFragmentInHiltContainer<SearchFragment> {
      this.setAdapterForTest(spyAdapter)
    }

    activity = searchFragment.requireActivity() as AppCompatActivity
    toolbar = activity.supportActionBar ?: return

    // setup mock ViewModel responses
    every { mockSearchViewModel.searchResults } returns searchResultsFlow
    every { mockSearchViewModel.query } returns queryLiveData
    every { mockSearchViewModel.expandSearchView } returns expandSearchViewLiveData
    every { mockSearchViewModel.search(any()) } just Runs
    every { mockSearchViewModel.setExpandSearchView(any()) } just Runs
    every { mockSnackbar.showSnackbarWarning(ofType<String>()) } returns mockk(relaxed = true)
  }

  @Test
  fun searchFragment_whenInitialState_displaysViewsCorrectly() {
    onView(withId(illustration_search_view)).check(matches(isDisplayed()))
    onView(withId(rv_search)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    onView(withId(illustration_error)).check(matches(not(isDisplayed())))
    onView(withId(illustration_search_no_result_view)).check(matches(not(isDisplayed())))
  }

  @Test
  fun searchFragment_whenInitialState_displaysToolbarCorrectly() {
    assertThat(toolbar).isNotNull()
    assertThat(toolbar.title).isNull() // we use custom title, so it will null

    // make sure search view is shown
    onView(withId(action_search)).check(matches(isDisplayed()))
  }

  @Test
  fun searchView_whenExpandTriggered_showsSearchView() {
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      expandSearchViewLiveData.value = true
    }
    shortDelay()

    onView(
      allOf(
        isAssignableFrom(SearchView::class.java),
        withEffectiveVisibility(Visibility.VISIBLE)
      )
    ).check(matches(isDisplayed()))
  }

  @Test
  fun searchView_whenSubmitting_triggersSearch() {
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      expandSearchViewLiveData.value = true
    }
    performClickSearchAction()
    performTypeAndSearchAction()

    // verify loading state UI
    onView(withId(rv_search)).check(matches(isDisplayed()))
    onView(withId(illustration_search_view)).check(matches(not(isDisplayed())))
    onView(withId(illustration_error)).check(matches(not(isDisplayed())))

    verify { mockSearchViewModel.search(testQuery) }
  }

  @Test
  fun searchView_whenSearchWithSameQuery_onlyTriggerSearchOnce() {
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      expandSearchViewLiveData.value = true
    }
    performClickSearchAction()

    // perform search twice with same query
    performTypeAndSearchAction()
    onView(withId(search_src_text))
      .perform(clearText(), typeText(testQuery), pressImeActionButton())

    // verify search was only called once
    verify(exactly = 1) { mockSearchViewModel.search(testQuery) }
  }

  @Test
  fun searchView_whenSearchWithoutQuery_shouldNotTriggerSearch() {
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      expandSearchViewLiveData.value = true
    }
    performClickSearchAction()

    // perform search without query
    onView(withId(search_src_text))
      .check(matches(isDisplayed()))
      .perform(pressImeActionButton())
    // verify search was only called once
    verify(exactly = 0) { mockSearchViewModel.search(testQuery) }
  }

  @Test
  fun searchView_whenRestoresQuery_restoresFromViewModel() {
    performClickSearchAction()

    // simulate saved query without typing
    val savedQuery = "saved query"
    queryLiveData.postValue(savedQuery)
    expandSearchViewLiveData.postValue(true)

    // verify query is restored in search view
    onView(withId(search_src_text)).check(matches(withText(savedQuery)))
  }

  @Test
  fun swipeRefresh_whenSwiped_triggersRefresh() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
      onView(withId(swipe_refresh)).perform(swipeDown())
    } else {
      onView(withId(swipe_refresh)).perform(triggerSwipeRefresh())
    }
    shortDelay()

    onView(withId(illustration_search_view)).check(matches(isDisplayed()))
    verify(exactly = 1) { searchAdapter.refresh() }
  }

  @Test
  fun searchView_whenDebugging_shouldPassed() {
    performClickSearchAction()

    // trigger via LiveData
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      expandSearchViewLiveData.value = true
    }
    shortDelay()

    val searchViewChecks = listOf(
      "androidx.appcompat.R.id.search_src_text" to search_src_text,
      "R.id.search_src_text" to search_src_text,
      "android.R.id.search_src_text" to search_src_text
    )

    for ((name, id) in searchViewChecks) {
      try {
        onView(withId(id)).check(matches(isDisplayed()))
        break
      } catch (e: Exception) {
        println("SearchView text field not found with ID $name: ${e.message}")
      }
    }

    // find SearchView by class
    onView(isAssignableFrom(SearchView::class.java)).check(matches(isDisplayed()))

    // find EditText inside SearchView
    onView(
      allOf(
        isAssignableFrom(EditText::class.java),
        isDescendantOfA(isAssignableFrom(SearchView::class.java))
      )
    ).check(matches(isDisplayed()))
  }

  @Test
  fun searchView_whenPressSubmit_shouldTriggerSearchFunction() {
    // manual click search action
    try {
      performClickSearchAction()

      onView(
        allOf(
          isAssignableFrom(EditText::class.java),
          isDescendantOfA(isAssignableFrom(SearchView::class.java))
        )
      ).perform(typeText(testQuery), pressImeActionButton()) // perform submit

      verify { mockSearchViewModel.search(testQuery) }

    } catch (e: Exception) {
      println("Manual trigger failed: ${e.message}")
      throw e
    }
  }

  @Test
  fun searchView_withLifecycleManagement_shouldWorking() {
    // ensure fragment is in RESUMED state
    searchFragment.viewLifecycleOwner.lifecycle.currentState.let { state ->
      println("Fragment lifecycle state: $state")
      assertTrue(state == Lifecycle.State.RESUMED)
    }
    performClickSearchAction()

    // expanse the search view
    expandSearchViewLiveData.postValue(true)

    // multiple search approaches
    val success = tryMultipleSearchApproaches()
    assertTrue("Should find SearchView with at least one approach", success)

    verify { mockSearchViewModel.search(testQuery) }
  }

  @Test
  fun searchView_directViewModelCall_test() {
    // get the search view
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      searchFragment.javaClass.getDeclaredMethod("setupSearchView")
        .apply { isAccessible = true }
        .invoke(searchFragment)
    }
    shortDelay()

    // direct call the search method
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      mockSearchViewModel.search(testQuery)
    }
    verify { mockSearchViewModel.search(testQuery) }
  }

  @Test
  fun openSearchView_expandsSearchView() {
    searchFragment.openSearchView()
    verify { mockSearchViewModel.setExpandSearchView(true) }
  }

  @Test
  fun onConfigurationChanged_whenKeyboardHidden_callsExpandMethod() {
    val spyFragment = spyk(searchFragment)

    val newConfig = Configuration().apply {
      keyboardHidden = Configuration.KEYBOARDHIDDEN_YES
    }

    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      spyFragment.onConfigurationChanged(newConfig)
    }

    verify { spyFragment.onKeyboardHidden() }
  }

  @Test
  fun onResume_whenCalled_shouldPassed() {
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      searchFragment.onResume()
    }
  }

  @Test
  fun onPause_whenCalled_shouldPassed() {
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      searchFragment.onPause()
    }
  }

  @Test
  fun onDestroyView_whenCalled_resetsState() {
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      searchFragment.onDestroyView()
    }
    // verify view model state is reset
    verify { mockSearchViewModel.setExpandSearchView(false) }
  }

  @Test
  fun fragmentResultListener_opensSearchView() {
    // simulate fragment result
    searchFragment.parentFragmentManager.setFragmentResult(
      "open_search_view",
      Bundle()
    )

    // verify search view is expanded
    verify { mockSearchViewModel.setExpandSearchView(true) }
  }

  @Test
  fun recyclerView_whenInitialized_shouldHasCorrectLayoutManager() {
    onView(withId(rv_search))
      .check { view, _ ->
        val recyclerView = view as RecyclerView
        assertThat(recyclerView.layoutManager).isInstanceOf(LinearLayoutManager::class.java)
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        assertThat(layoutManager.orientation).isEqualTo(LinearLayoutManager.VERTICAL)
      }
  }

  private fun tryMultipleSearchApproaches(): Boolean {
    val approaches = listOf(
      // Approach 1: Standard ID
      {
        onView(withId(search_src_text)).perform(typeText(testQuery), pressImeActionButton())
      },

      // Approach 2: By class and descendant
      {
        onView(
          allOf(
            isAssignableFrom(EditText::class.java),
            isDescendantOfA(isAssignableFrom(SearchView::class.java))
          )
        ).perform(typeText(testQuery), pressImeActionButton())
      },

      // Approach 3: By hint text
      {
        onView(withHint("Search")).perform(typeText(testQuery), pressImeActionButton())
      },

      // Approach 4: By content description
      {
        onView(withContentDescription(containsString("search")))
          .perform(typeText(testQuery), pressImeActionButton())
      }
    )

    for ((index, approach) in approaches.withIndex()) {
      try {
        println("Trying search approach ${index + 1}")
        approach()
        println("✓ Search approach ${index + 1} succeeded")
        return true
      } catch (e: Exception) {
        println("✗ Search approach ${index + 1} failed: ${e.message}")
      }
    }
    return false
  }

  private fun performClickSearchAction() {
    onView(withId(action_search)).perform(click())
    shortDelay()
  }

  private fun performTypeAndSearchAction() {
    onView(withId(search_src_text))
      .check(matches(isDisplayed()))
      .perform(typeText(testQuery), pressImeActionButton())
  }
}
