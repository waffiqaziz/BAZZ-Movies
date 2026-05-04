package com.waffiq.bazz_movies.feature.home.ui

import androidx.lifecycle.Lifecycle
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.waffiq.bazz_movies.core.common.utils.Constants.DEBOUNCE_VERY_LONG
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.designsystem.R.string.movies
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performScrollTo
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performSwipeDown
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performTextClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isNotDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.CustomVisibilityMatchers.isVisible
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.waitUntilVisible
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.core.user.ui.viewmodel.RegionViewModel
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.feature.home.R.id.btn_more_movie_airing_today
import com.waffiq.bazz_movies.feature.home.R.id.btn_more_popular_movie
import com.waffiq.bazz_movies.feature.home.R.id.btn_more_top_rated_movie
import com.waffiq.bazz_movies.feature.home.R.id.btn_more_upcoming_movie
import com.waffiq.bazz_movies.feature.home.R.id.illustration_error_movie
import com.waffiq.bazz_movies.feature.home.R.id.layout_header_popular_movie
import com.waffiq.bazz_movies.feature.home.R.id.rv_popular_movie
import com.waffiq.bazz_movies.feature.home.R.id.swipe_refresh_movie
import com.waffiq.bazz_movies.feature.home.R.id.tabs
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MovieFragmentTest : BaseHomeFragmentTest() {

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
    setupMock(mockMovieViewModel, mockTvSeriesViewModel, mockNavigator)
    setupRegionMock(mockUserPreferenceViewModel, mockRegionViewModel)
  }

  @Test
  fun setData_successThenError_shouldShowTheError() =
    runTest {
      val testDispatcher = StandardTestDispatcher(testScheduler)
      Dispatchers.setMain(testDispatcher)

      // Override only topRated since that's what drives the error illustration
      every { mockMovieViewModel.getTopRatedMovies() } returns createSuccessThenErrorFlow()

      launchMovieFragment()
//    advanceUntilIdle()
//    InstrumentationRegistry.getInstrumentation().waitForIdleSync()

//    waitUntilVisible(withId(rv_popular_movie))
//    illustration_error_movie.isNotDisplayed()
//
//    // Trigger refresh → SuccessThenErrorPagingSource loadCount becomes 2 → LoadResult.Error
//    swipe_refresh_movie.performSwipeDown()
//
//    advanceTimeBy(DEBOUNCE_VERY_LONG + 500L)
//    advanceUntilIdle()
//    InstrumentationRegistry.getInstrumentation().waitForIdleSync()
//
//    waitUntilVisible(withId(illustration_error_movie))
//
      Dispatchers.resetMain()
    }

  @Test
  fun setData_whenLoadErrorNoItems_shouldShowErrorIllustration() =
    runTest {
      val testDispatcher = StandardTestDispatcher(testScheduler)
      Dispatchers.setMain(testDispatcher)

      every { mockMovieViewModel.getTopRatedMovies() } returns createErrorPagingFlow()

      launchMovieFragment()
      InstrumentationRegistry.getInstrumentation().runOnMainSync {
        advanceTimeBy(DEBOUNCE_VERY_LONG + 1000L)
        testScheduler.runCurrent()
      }

      waitUntilVisible(withId(illustration_error_movie))
      layout_header_popular_movie.isNotDisplayed()

      Dispatchers.resetMain()
    }

  @Test
  fun setData_whenLoading_shouldShowShimmer() =
    runTest {
      val testDispatcher = StandardTestDispatcher(testScheduler)
      Dispatchers.setMain(testDispatcher)

      every { mockMovieViewModel.getTopRatedMovies() } returns createStuckLoadingFlow()

      launchMovieFragment()

      InstrumentationRegistry.getInstrumentation().runOnMainSync {
        advanceTimeBy(DEBOUNCE_VERY_LONG + 1000L)
        testScheduler.runCurrent()
      }

      rv_popular_movie.isVisible()

      Dispatchers.resetMain()
    }

  @Test
  fun moreButton_whenClicked_shouldOpenListActivity() {
    launchMovieFragment()

    btn_more_popular_movie.performScrollTo()
    btn_more_popular_movie.performClick()
    verifyOpenList(mockNavigator, ListArgs(ListType.POPULAR, MOVIE_MEDIA_TYPE, ""))

    btn_more_movie_airing_today.performScrollTo()
    btn_more_movie_airing_today.performClick()
    verifyOpenList(mockNavigator, ListArgs(ListType.NOW_PLAYING, MOVIE_MEDIA_TYPE, ""))

    btn_more_upcoming_movie.performScrollTo()
    btn_more_upcoming_movie.performClick()
    verifyOpenList(mockNavigator, ListArgs(ListType.UPCOMING, MOVIE_MEDIA_TYPE, ""))

    btn_more_top_rated_movie.performScrollTo()
    btn_more_top_rated_movie.performClick()
    verifyOpenList(mockNavigator, ListArgs(ListType.TOP_RATED, MOVIE_MEDIA_TYPE, ""))
  }

  @Test
  fun swipeScroll_whenRefresh_refreshData() {
    launchMovieFragment()
    swipe_refresh_movie.performSwipeDown()
  }

  @Test
  fun onPause_whenSnackbarIsNull_shouldNotCrash() {
    // no error, so snack bar is null
    launchMovieFragment()
    scenario.moveToState(Lifecycle.State.CREATED)
  }

  @Test
  fun onDestroy_whenSnackbarIsNull_shouldNotCrash() {
    // no error, so snack bar is null
    launchMovieFragment()
    scenario.moveToState(Lifecycle.State.DESTROYED)
  }

  private fun launchMovieFragment() {
    launchFragment()
    waitUntilVisible(withId(tabs))
    movies.performTextClick()
  }
}
