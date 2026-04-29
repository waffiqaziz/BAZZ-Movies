package com.waffiq.bazz_movies.feature.list

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.paging.PagingData
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.Intents
import com.waffiq.bazz_movies.core.designsystem.R.id.btn_try_again
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performSwipeDown
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.doesNotExist
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isNotDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.CustomVisibilityMatchers.isVisible
import com.waffiq.bazz_movies.core.uihelper.state.UIState
import com.waffiq.bazz_movies.feature.list.R.id.btn_close
import com.waffiq.bazz_movies.feature.list.R.id.btn_toggle_layout
import com.waffiq.bazz_movies.feature.list.R.id.collapse
import com.waffiq.bazz_movies.feature.list.R.id.illustration_error
import com.waffiq.bazz_movies.feature.list.R.id.iv_picture
import com.waffiq.bazz_movies.feature.list.R.id.loading_indicator
import com.waffiq.bazz_movies.feature.list.R.id.rv_list
import com.waffiq.bazz_movies.feature.list.testutils.BaseListActivityTest
import com.waffiq.bazz_movies.feature.list.ui.viewmodel.ListViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.After
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
  fun listActivity_withGenreType_showsCorrectViews() {
    context.launchListActivity {
      collapse.isDisplayed()
      "Science Fiction".isVisible()
      shouldShowMovie()
    }
    context.launchListActivity(tvGenreArgs) {
      shouldShowTv()
    }
  }

  @Test
  fun listActivity_withKeywordsType_showsCorrectViews() {
    context.launchListActivity(movieKeywordsArgs) {
      "Post Apocalyptic".isVisible()
      shouldShowMovie()
    }
    context.launchListActivity(tvKeywordsArgs) {
      "Post Apocalyptic".isVisible()
      shouldShowTv()
    }
  }

  @Test
  fun listActivity_withNowPlayingType_showsCorrectViews() {
    context.launchListActivity(movieNowPlayingArgs) {
      shouldShowMovie()
    }
    context.launchListActivity(tvNowPlayingArgs) {
      shouldShowTv()
    }
  }

  @Test
  fun listActivity_withPopularType_showsCorrectViews() {
    context.launchListActivity(moviePopularArgs) {
      shouldShowMovie()
    }
    context.launchListActivity(tvPopularArgs) {
      shouldShowTv()
    }
  }

  @Test
  fun listActivity_withRecommendationType_showsCorrectViews() {
    context.launchListActivity(movieRecommendationArgs) {
      movieRecommendationArgs.title.isVisible()
      "Recommendation".isVisible()
    }
    context.launchListActivity(tvRecommendationArgs) {
      tvRecommendationArgs.title.isVisible()
      "Recommendation".isVisible()
    }
  }

  @Test
  fun listActivity_withTopRatedType_showsCorrectViews() {
    context.launchListActivity(movieTopRatedArgs) {
      shouldShowMovie()
    }
    context.launchListActivity(tvTopRatedArgs) {
      shouldShowTv()
    }
  }

  @Test
  fun listActivity_withUpcomingType_showsCorrectViews() {
    context.launchListActivity(movieUpcomingArgs) {
      shouldShowMovie()
    }
  }

  @Test
  fun listActivity_withAiringThisWeekType_showsCorrectViews() {
    context.launchListActivity(tvAiringThisWeekArgs) {
      shouldShowTv()
    }
  }

  @Test
  fun listActivity_toggleButtonPressed_changesTheLayout() {
    context.launchListActivity {
      // should use grid layout on initial
      "movie title".doesNotExist()

      // switch to linear layout
      triggerListButton()
      "movie title".isVisible()

      // back to grid layout
      btn_toggle_layout.performClick()
      "movie title".doesNotExist()
    }
  }

  @Test
  fun listActivity_performClickOnListLayout_changesTheLayout() {
    context.launchListActivity {
      triggerListButton()
    }
  }

  @Test
  fun swipeRefreshMovie_whenScroll_runsWithoutProblem() {
    context.launchListActivity {
      rv_list.performSwipeDown()
      rv_list.performSwipeDown()
    }
  }

  @Test
  fun onKeywordsLoadState_itemNotZero_showsBackdrop() {
    context.launchListActivity(movieKeywordsArgs) { scenario ->
      scenario.onActivity { activity ->
        activity.loadStateChanged()
      }
    }
  }

  @Test
  fun onKeywordsLoadState_withEmptyData_stillShowsImageView() {
    every { mockListViewModel.getByKeyword(any(), any()) } returns flowOf(PagingData.empty())

    context.launchListActivity(movieKeywordsArgs) { scenario ->
      iv_picture.isDisplayed() // image view shows but not the actual backdrop
      scenario.onActivity { activity ->
        activity.loadStateChanged()
      }
    }
  }

  @Test
  fun buttonClose_whenClicked_finishTheActivity() {
    context.launchListActivity { scenario ->
      btn_close.performClick()

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
      loading_indicator.isDisplayed()
      rv_list.isNotDisplayed()
      illustration_error.isNotDisplayed()
    }
  }

  @Test
  fun successState_showsList() {
    context.launchListActivity(movieKeywordsArgs) { scenario ->
      scenario.onActivity { activity ->
        activity.handleRefreshState(UIState.Success(Unit))
      }
      rv_list.isDisplayed()
      loading_indicator.isNotDisplayed()
    }
  }

  @Test
  fun errorState_showsRetryAndHidesList() {
    context.launchListActivity(movieKeywordsArgs) { scenario ->
      scenario.onActivity { activity ->
        activity.handleRefreshState(UIState.Error("Something went wrong"))
      }
      illustration_error.isDisplayed()
      rv_list.isNotDisplayed()
      btn_try_again.performClick()
    }
  }

  @Test
  fun extractDataFromIntent_returnsFalse_whenExtraMissing() {
    context.launchNullListActivity(args = null) { scenario ->
      assertTrue(scenario.state == Lifecycle.State.DESTROYED)
    }
  }

  private fun triggerListButton() {
    btn_toggle_layout.isDisplayed()
    btn_toggle_layout.performClick()
  }
}
