package com.waffiq.bazz_movies.feature.list

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.paging.PagingData
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.Intents
import com.waffiq.bazz_movies.core.designsystem.R.id.btn_try_again
import com.waffiq.bazz_movies.core.designsystem.R.string.all_time
import com.waffiq.bazz_movies.core.designsystem.R.string.costume_drama
import com.waffiq.bazz_movies.core.designsystem.R.string.donghua
import com.waffiq.bazz_movies.core.designsystem.R.string.reality_show
import com.waffiq.bazz_movies.core.designsystem.R.string.romance_drama
import com.waffiq.bazz_movies.core.designsystem.R.string.this_season
import com.waffiq.bazz_movies.core.designsystem.R.style.Base_Theme_BAZZ_movies
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performSwipeDown
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.doesNotExist
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isNotDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.CustomVisibilityMatchers.isTextVisible
import com.waffiq.bazz_movies.core.instrumentationtest.CustomVisibilityMatchers.isVisible
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
import com.waffiq.bazz_movies.core.uihelper.state.UIState
import com.waffiq.bazz_movies.feature.list.R.id.btn_back
import com.waffiq.bazz_movies.feature.list.R.id.btn_toggle_layout
import com.waffiq.bazz_movies.feature.list.R.id.collapse
import com.waffiq.bazz_movies.feature.list.R.id.illustration_error
import com.waffiq.bazz_movies.feature.list.R.id.iv_picture
import com.waffiq.bazz_movies.feature.list.R.id.loading_indicator
import com.waffiq.bazz_movies.feature.list.R.id.rv_list
import com.waffiq.bazz_movies.feature.list.testutils.BaseListActivityTest
import com.waffiq.bazz_movies.feature.list.ui.viewmodel.ListViewModel
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
  val mockListViewModel: ListViewModel = mockk(relaxed = true)

  @Before
  fun setup() {
    hiltRule.inject()
    Intents.init()
    context = ApplicationProvider.getApplicationContext<Context>().apply {
      setTheme(Base_Theme_BAZZ_movies) // set the theme
    }
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
  fun listActivity_withTrendingTodayType_showsCorrectViews() {
    context.launchListActivity(trendingTodayArgs) {
      shouldShowTrending()
    }
  }

  @Test
  fun listActivity_withTrendingThisWeekType_showsCorrectViews() {
    context.launchListActivity(trendingThisWeekArgs) {
      shouldShowTrending()
    }
  }

  @Test
  fun listActivity_withAnimeAllTimeType_showsCorrectViews() {
    context.launchListActivity(animeAllTimeArgs) {
      all_time.isTextVisible()
    }
  }

  @Test
  fun listActivity_withAnimeThisSeasonType_showsCorrectViews() {
    context.launchListActivity(animeThisSeasonArgs) {
      this_season.isTextVisible()
    }
  }

  @Test
  fun listActivity_withCostumeDramaType_showsCorrectViews() {
    context.launchListActivity(costumeDramaArgs) {
      costume_drama.isTextVisible()
    }
  }

  @Test
  fun listActivity_withDonghuaType_showsCorrectViews() {
    context.launchListActivity(donghuaArgs) {
      donghua.isTextVisible()
    }
  }

  @Test
  fun listActivity_withRomanceDramaType_showsCorrectViews() {
    context.launchListActivity(romanceDramaArgs) {
      romance_drama.isTextVisible()
    }
  }

  @Test
  fun listActivity_withRealityShowType_showsCorrectViews() {
    context.launchListActivity(realityShow) {
      reality_show.isTextVisible()
    }
  }

  @Test
  fun listActivity_toggleButtonPressed_changesTheLayout() {
    context.launchListActivity {
      // should use grid layout on initial
      "movie title 1".doesNotExist()

      // switch to linear layout
      triggerListButton()
      "movie title 1".isVisible()

      // back to grid layout
      btn_toggle_layout.performClick()
      "movie title 1".doesNotExist()
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
      shortDelay()
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
      btn_back.performClick()

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
