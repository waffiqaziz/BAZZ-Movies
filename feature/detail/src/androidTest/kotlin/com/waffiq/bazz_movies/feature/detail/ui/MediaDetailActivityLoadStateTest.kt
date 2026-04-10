package com.waffiq.bazz_movies.feature.detail.ui

import androidx.lifecycle.lifecycleScope
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.waffiq.bazz_movies.feature.detail.R.id.rv_recommendation
import com.waffiq.bazz_movies.feature.detail.R.id.tv_recommendation_header
import com.waffiq.bazz_movies.feature.detail.testutils.DataDumb.testMediaItem
import com.waffiq.bazz_movies.feature.detail.testutils.MediaDetailActivityTestHelper
import com.waffiq.bazz_movies.feature.detail.testutils.MediaDetailActivityTestSetup
import com.waffiq.bazz_movies.feature.detail.ui.adapter.RecommendationAdapter
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.DetailUserPrefViewModel
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.MediaDetailViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import kotlinx.coroutines.launch
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Instrumented test for [MediaDetailActivity] that checks state for recommendation list.
 */
@HiltAndroidTest
class MediaDetailActivityLoadStateTest :
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
  fun recommendationVisibility_whenEmpty_hidesViews() {
    context.launchMediaDetailActivity {
      it.onActivity { activity ->
        activity.uiManager.adapterRecommendation.simulateLoadState(
          createCombinedLoadStates(
            appendState = LoadState.NotLoading(endOfPaginationReached = true),
          )
        )
      }
      checkRecommendation(false)
    }
  }

  @Test
  fun recommendationVisibility_whenNotEmpty_showsViews() {
    recommendations.value = PagingData.from(listOf(testMediaItem, testMediaItem))
    context.launchMediaDetailActivity {
      it.onActivity { activity ->
        activity.uiManager.adapterRecommendation.simulateLoadState(
          createCombinedLoadStates(
            appendState = LoadState.NotLoading(endOfPaginationReached = false),
          )
        )
      }
      checkRecommendation(true)
    }
  }

  @Test
  fun recommendationVisibility_whenLoadingState_showsViews() {
    recommendations.value = PagingData.from(listOf(testMediaItem, testMediaItem))
    context.launchMediaDetailActivity {
      it.onActivity { activity ->
        activity.uiManager.adapterRecommendation.simulateLoadState(
          createCombinedLoadStates(
            refreshState = LoadState.Loading,
          )
        )
      }
      checkRecommendation(true)
    }
  }

  @Test
  fun recommendationVisibility_whenEmptyWithEndOfPagination_hidesViews() {
    context.launchMediaDetailActivity {
//      it.onActivity { activity ->
//        val adapter = activity.uiManager.adapterRecommendation
//
//        // clear items first so itemCount = 0, this a must due to the base test already set the value
//        // `recommendation.postValue(PagingData.from(listOf(testMediaItem, testMediaItem)))`
//        // inside setupBaseMocks
//        activity.lifecycleScope.launch {
//          adapter.submitData(PagingData.empty())
//        }
//      }
//
//      Thread.sleep(500)

      it.onActivity { activity ->
        val adapter = activity.uiManager.adapterRecommendation
        println("itemCount after empty: ${adapter.itemCount}") // should be 0

        adapter.simulateLoadState(
          createCombinedLoadStates(
            refreshState = LoadState.NotLoading(endOfPaginationReached = false),
            appendState = LoadState.NotLoading(endOfPaginationReached = true),
          )
        )
      }
      checkRecommendation(false)
    }
  }

  private fun createCombinedLoadStates(
    refreshState: LoadState = LoadState.NotLoading(endOfPaginationReached = false),
    appendState: LoadState = LoadState.NotLoading(endOfPaginationReached = false),
  ): CombinedLoadStates = CombinedLoadStates(
    refresh = refreshState,
    prepend = LoadState.NotLoading(endOfPaginationReached = false),
    append = appendState,
    source = LoadStates(
      refresh = refreshState,
      prepend = LoadState.NotLoading(endOfPaginationReached = false),
      append = appendState,
    ),
  )

  private fun RecommendationAdapter.simulateLoadState(loadStates: CombinedLoadStates) {
    val differField = PagingDataAdapter::class.java
      .getDeclaredField("differ")
      .apply { isAccessible = true }
    val differ = differField.get(this)

    val listenersField = AsyncPagingDataDiffer::class.java
      .getDeclaredField("childLoadStateListeners")
      .apply { isAccessible = true }

    @Suppress("UNCHECKED_CAST")
    val listeners = listenersField.get(differ) as CopyOnWriteArrayList<(CombinedLoadStates) -> Unit>

    listeners.forEach { it(loadStates) }
  }

  private fun checkRecommendation(isVisible: Boolean) {
    if (isVisible) {
      onView(withId(rv_recommendation)).perform(scrollTo())
      onView(withId(rv_recommendation)).perform(swipeUp())
      onView(withId(tv_recommendation_header)).check(matches(isDisplayed()))
      onView(withId(rv_recommendation)).check(matches(isDisplayed()))
    } else {
      onView(withId(tv_recommendation_header)).check(matches(not(isDisplayed())))
      onView(withId(rv_recommendation)).check(matches(not(isDisplayed())))
    }
  }
}
