package com.waffiq.bazz_movies.feature.home.ui

import androidx.lifecycle.Lifecycle
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.designsystem.R.string.tv_series
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performScrollTo
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performSwipeDown
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performTextClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isNotDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.CustomVisibilityMatchers.isVisible
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.waitUntilVisible
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.core.user.ui.viewmodel.RegionViewModel
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.feature.home.R.id.btn_more_popular_tv_series
import com.waffiq.bazz_movies.feature.home.R.id.btn_more_top_rated_tv_series
import com.waffiq.bazz_movies.feature.home.R.id.btn_more_tv_series_airing_this_week
import com.waffiq.bazz_movies.feature.home.R.id.btn_more_tv_series_airing_today
import com.waffiq.bazz_movies.feature.home.R.id.illustration_error_tv_series
import com.waffiq.bazz_movies.feature.home.R.id.layout_header_popular_tv_series
import com.waffiq.bazz_movies.feature.home.R.id.rv_popular_tv_series
import com.waffiq.bazz_movies.feature.home.R.id.rv_tv_series_airing_today
import com.waffiq.bazz_movies.feature.home.R.id.swipe_refresh_tv_series
import com.waffiq.bazz_movies.feature.home.testutils.BaseHomeFragmentTest
import com.waffiq.bazz_movies.feature.home.ui.viewmodel.MovieViewModel
import com.waffiq.bazz_movies.feature.home.ui.viewmodel.TvSeriesViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import com.waffiq.bazz_movies.navigation.ListArgs
import com.waffiq.bazz_movies.navigation.ListType
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class TvSeriesFragmentTest : BaseHomeFragmentTest() {

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
  val mockMovieViewModel: MovieViewModel = mockk(relaxed = true)

  @BindValue
  @JvmField
  val mockTvSeriesViewModel: TvSeriesViewModel = mockk(relaxed = true)

  @BindValue
  @JvmField
  val mockUserPreferenceViewModel: UserPreferenceViewModel = mockk(relaxed = true)

  @BindValue
  @JvmField
  val mockRegionViewModel: RegionViewModel = mockk(relaxed = true)

  @Before
  fun setUp() {
    hiltRule.inject()
    setupMockNavigator(mockNavigator)
    setupMockRegion(mockUserPreferenceViewModel, mockRegionViewModel)
    setupMockViewModel(mockMovieViewModel, mockTvSeriesViewModel)
  }

  @Test
  fun setData_whenLoadSuccess_shouldShowContentAndHideError() {
    launchTvSeriesFragment()

    shortDelay(1000)
    waitUntilVisible(withId(rv_popular_tv_series))
    rv_tv_series_airing_today.isDisplayed()
    layout_header_popular_tv_series.isDisplayed()
    illustration_error_tv_series.isNotDisplayed()
  }

  @Test
  fun setData_whenLoadErrorNoItems_shouldShowErrorIllustration() {
    every { mockTvSeriesViewModel.getTopRatedTv() } returns createErrorPagingFlow()

    launchTvSeriesFragment()
    shortDelay(2000)

    waitUntilVisible(withId(illustration_error_tv_series))
    layout_header_popular_tv_series.isNotDisplayed()
  }

  @Test
  fun setData_whenLoading_shouldShowShimmer() {
    every { mockTvSeriesViewModel.getTopRatedTv() } returns createStuckLoadingFlow()

    launchTvSeriesFragment()
    rv_popular_tv_series.isVisible()
  }

  @Test
  fun moreButton_whenClicked_shouldOpenListActivity() {
    launchTvSeriesFragment()

    btn_more_popular_tv_series.performScrollTo()
    btn_more_popular_tv_series.performClick()
    verifyOpenList(mockNavigator, ListArgs(ListType.POPULAR, TV_MEDIA_TYPE, ""))

    btn_more_tv_series_airing_today.performScrollTo()
    btn_more_tv_series_airing_today.performClick()
    verifyOpenList(mockNavigator, ListArgs(ListType.NOW_PLAYING, TV_MEDIA_TYPE, ""))

    btn_more_tv_series_airing_this_week.performScrollTo()
    btn_more_tv_series_airing_this_week.performClick()
    verifyOpenList(mockNavigator, ListArgs(ListType.AIRING_THIS_WEEK, TV_MEDIA_TYPE, ""))

    btn_more_top_rated_tv_series.performScrollTo()
    btn_more_top_rated_tv_series.performClick()
    verifyOpenList(mockNavigator, ListArgs(ListType.TOP_RATED, TV_MEDIA_TYPE, ""))
  }

  @Test
  fun swipeScroll_whenRefresh_refreshData() {
    launchTvSeriesFragment()
    shortDelay(1000)
    waitUntilVisible(withId(swipe_refresh_tv_series))
    swipe_refresh_tv_series.performSwipeDown()
  }

  @Test
  fun onPause_whenSnackbarAndJobAreNull_shouldNotCrash() {
    launchTvSeriesFragment()
    scenario.moveToState(Lifecycle.State.CREATED)
  }

  @Test
  fun onDestroy_whenSnackbarAndJobAreNull_shouldNotCrash() {
    launchTvSeriesFragment()
    scenario.moveToState(Lifecycle.State.DESTROYED)
  }

  private fun launchTvSeriesFragment() {
    launchFragment()
    shortDelay()
    tv_series.performTextClick()
    InstrumentationRegistry.getInstrumentation().waitForIdleSync()
  }
}
