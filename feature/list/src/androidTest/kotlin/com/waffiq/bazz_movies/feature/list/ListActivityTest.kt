package com.waffiq.bazz_movies.feature.list

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.paging.PagingData
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.uihelper.state.UIState
import com.waffiq.bazz_movies.feature.list.R.id.btn_close
import com.waffiq.bazz_movies.feature.list.R.id.illustration_error
import com.waffiq.bazz_movies.feature.list.R.id.loading_indicator
import com.waffiq.bazz_movies.feature.list.R.id.rv_list
import com.waffiq.bazz_movies.feature.list.testutils.BaseListActivityTest
import com.waffiq.bazz_movies.feature.list.ui.viewmodel.ListViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import com.waffiq.bazz_movies.navigation.ListType
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class ListActivityTest : BaseListActivityTest() {

  private lateinit var context: Context

  @get:Rule
  var hiltRule = HiltAndroidRule(this)

  @BindValue
  @JvmField
  val mockNavigator: INavigator = mockk(relaxed = true)

  @BindValue
  @JvmField
  val mockListViewModel: ListViewModel = mockk(relaxed = true)

  @Before
  fun setup() {
    hiltRule.inject()
    Intents.init()
    context = ApplicationProvider.getApplicationContext()
    setupMock(mockListViewModel, mockNavigator)
  }

  @After
  fun tearDown() {
    Intents.release()
  }

  @Test
  fun listActivity_movieData_showsCorrectViews() {
    context.launchListActivity {
      onView(withId(R.id.collapse)).check(matches(isDisplayed()))
      onView(withText("Science Fiction"))
        .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
      onView(withText("MOVIE"))
        .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }
  }

  @Test
  fun listActivity_tvData_showsCorrectViews() {
    context.launchListActivity(movieGenreArgs.copy(mediaType = TV_MEDIA_TYPE)) {
      onView(withText("TV"))
        .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }
  }

  @Test
  fun listActivity_withKeywordsType_showsCorrectViews() {
    context.launchListActivity(movieKeywordsArgs) {
      onView(withText("Post Apocalyptic"))
        .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }
    context.launchListActivity(movieKeywordsArgs.copy(mediaType = TV_MEDIA_TYPE)) {
      onView(withText("Post Apocalyptic"))
        .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }
  }

  @Test
  fun listActivity_withTrendingType_showsCorrectViews() {
    context.launchListActivity(movieGenreArgs.copy(listType = ListType.TRENDING)) {
      onView(withText("title"))
        .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }
  }

  @Test
  fun swipeRefreshMovie_whenScroll_runsWithoutProblem() {
    context.launchListActivity {
      onView(withId(rv_list)).perform(swipeDown())
      onView(withId(rv_list)).perform(swipeDown())
    }
  }

  @Test
  fun listActivity_whenAdapterIsEmpty_doesNotLoadBackdrop() {
    every { mockListViewModel.getMovieByKeywords(any()) } returns flowOf(PagingData.empty())
    every { mockListViewModel.getTvByKeywords(any()) } returns flowOf(PagingData.empty())

    context.launchListActivity(movieKeywordsArgs) { scenario ->
      scenario.onActivity { activity ->
        assertEquals(0, activity.adapter.itemCount)
      }
    }
  }

  @Test
  fun onKeywordsLoadState_itemNotZero_showsBackdrop() {
    context.launchListActivity(movieKeywordsArgs) { scenario ->
      scenario.onActivity { activity ->
        activity.onKeywordsLoadStateChanged()
      }
    }
  }

  @Test
  fun onKeywordsLoadState_itemCountZero_skipsShowBackdrop() {
    every { mockListViewModel.getMovieByKeywords(any()) } returns flowOf(PagingData.empty())

    context.launchListActivity(movieKeywordsArgs) { scenario ->
      scenario.onActivity { activity ->
        activity.onKeywordsLoadStateChanged()
      }
    }
  }

  @Test
  fun buttonClose_whenClicked_finishTheActivity() {
    context.launchListActivity { scenario ->
      onView(withId(btn_close)).check(matches(isDisplayed())).perform(click())

      // check if the activity is finished
      scenario.moveToState(Lifecycle.State.DESTROYED)
      assertTrue(scenario.state == Lifecycle.State.DESTROYED)
    }
  }

  @Test
  fun loadingState_showsIndicatorAndHidesList() {
    context.launchListActivity(movieKeywordsArgs) { scenario ->
      scenario.onActivity { activity ->
        activity.handleRefreshState(UIState.Loading)
      }
      onView(withId(loading_indicator))
        .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
      onView(withId(rv_list))
        .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
      onView(withId(illustration_error))
        .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
    }
  }

  @Test
  fun successState_showsList() {
    context.launchListActivity(movieKeywordsArgs) { scenario ->
      scenario.onActivity { activity ->
        activity.handleRefreshState(UIState.Success(Unit))
      }
      onView(withId(rv_list))
        .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
      onView(withId(loading_indicator))
        .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
    }
  }

  @Test
  fun errorState_showsRetryAndHidesList() {
    context.launchListActivity(movieKeywordsArgs) { scenario ->
      scenario.onActivity { activity ->
        activity.handleRefreshState(UIState.Error("Something went wrong"))
      }
      onView(withId(illustration_error))
        .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
      onView(withId(rv_list))
        .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
    }
  }

  @Test
  fun extractDataFromIntent_returnsFalse_whenExtraMissing() {
    context.launchNullListActivity(args = null) { scenario ->
      assertTrue(scenario.state == Lifecycle.State.DESTROYED)
    }
  }
}
