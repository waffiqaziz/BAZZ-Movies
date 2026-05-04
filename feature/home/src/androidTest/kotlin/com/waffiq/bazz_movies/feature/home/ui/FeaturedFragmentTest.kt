package com.waffiq.bazz_movies.feature.home.ui

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.waffiq.bazz_movies.core.common.utils.Constants.DEBOUNCE_VERY_LONG
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.NAN
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performScrollTo
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performSwipeDown
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isNotDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.CustomVisibilityMatchers.isVisible
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.waitUntilVisible
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.core.user.ui.viewmodel.RegionViewModel
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.feature.home.R.id.btn_more_movie_playing_now_featured
import com.waffiq.bazz_movies.feature.home.R.id.btn_more_upcoming_movie_featured
import com.waffiq.bazz_movies.feature.home.R.id.btn_trending_this_week
import com.waffiq.bazz_movies.feature.home.R.id.btn_trending_today
import com.waffiq.bazz_movies.feature.home.R.id.button_group
import com.waffiq.bazz_movies.feature.home.R.id.filter_scroll
import com.waffiq.bazz_movies.feature.home.R.id.illustration_error_featured
import com.waffiq.bazz_movies.feature.home.R.id.img_main_featured
import com.waffiq.bazz_movies.feature.home.R.id.rv_trending
import com.waffiq.bazz_movies.feature.home.R.id.swipe_refresh_featured
import com.waffiq.bazz_movies.feature.home.testutils.BaseHomeFragmentTest
import com.waffiq.bazz_movies.feature.home.ui.viewmodel.MovieViewModel
import com.waffiq.bazz_movies.feature.home.ui.viewmodel.TvSeriesViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import com.waffiq.bazz_movies.navigation.ListArgs
import com.waffiq.bazz_movies.navigation.ListType
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coVerify
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
class FeaturedFragmentTest : BaseHomeFragmentTest() {

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
  fun setData_whenLoadSuccess_shouldShowContentAndHideError() {
    launchFragment()
    rv_trending.isDisplayed()
    img_main_featured.isDisplayed()
    filter_scroll.isDisplayed()
    button_group.isDisplayed()
    illustration_error_featured.isNotDisplayed()
  }

  @Test
  fun setData_whenLoadErrorNoItems_shouldShowErrorIllustration() {
    every { mockMovieViewModel.getTrendingThisWeek() } returns createErrorPagingFlow()

    launchFragment()

    waitUntilVisible(withId(illustration_error_featured))
    img_main_featured.isNotDisplayed()
  }

  @Test
  fun setData_whenLoading_shouldShowShimmer() =
    runTest {
      val testDispatcher = StandardTestDispatcher(testScheduler)
      Dispatchers.setMain(testDispatcher)

      every { mockMovieViewModel.getTrendingThisWeek() } returns createStuckLoadingFlow()

      launchFragment()
      advanceTimeBy(DEBOUNCE_VERY_LONG + 1000L)
      testDispatcher.scheduler.runCurrent()

      rv_trending.isVisible()

      Dispatchers.resetMain()
    }

  @Test
  fun setRegion_whenUserHasNoRegion_shouldFetchFromCountryApi() {
    every { mockUserPreferenceViewModel.getUserRegionPref() } returns MutableLiveData(NAN)
    every { mockRegionViewModel.countryCode } returns MutableLiveData("JP")

    launchFragment()

    coVerify { mockRegionViewModel.getCountryCode() }
    coVerify { mockUserPreferenceViewModel.saveRegionPref("JP") }
  }

  @Test
  fun setRegion_whenCountryCodeIsEmpty_shouldFallbackToDeviceLocation() {
    every { mockUserPreferenceViewModel.getUserRegionPref() } returns MutableLiveData(NAN)
    every { mockRegionViewModel.countryCode } returns MutableLiveData("") // empty → device fallback

    launchFragment()

    coVerify { mockUserPreferenceViewModel.saveRegionPref(any()) }
  }

  @Test
  fun setRegion_whenUserAlreadyHasRegion_shouldNotCallCountryApi() {
    every { mockUserPreferenceViewModel.getUserRegionPref() } returns MutableLiveData("DE")

    launchFragment()

    coVerify(exactly = 0) { mockRegionViewModel.getCountryCode() }
  }

  @Test
  fun btnTrending_whenClickedMultipleTimes_shouldCancelPreviousJob() {
    launchFragment()
    btn_trending_today.performClick()
    btn_trending_today.performClick()
    coVerify(atLeast = 2) { mockMovieViewModel.getTrendingToday() }
  }

  @Test
  fun btnWeek_whenClickedMultipleTimes_shouldCancelPreviousJob() {
    launchFragment()
    btn_trending_this_week.performClick()
    btn_trending_this_week.performClick()
    coVerify(atLeast = 2) { mockMovieViewModel.getTrendingThisWeek() }
  }

  @Test
  fun moreButton_whenClicked_shouldOpenListActivity() {
    launchFragment()
    btn_more_movie_playing_now_featured.performScrollTo()
    btn_more_movie_playing_now_featured.performClick()
    verifyOpenList(mockNavigator, ListArgs(ListType.NOW_PLAYING, MOVIE_MEDIA_TYPE, ""))

    btn_more_upcoming_movie_featured.performScrollTo()
    btn_more_upcoming_movie_featured.performClick()
    verifyOpenList(mockNavigator, ListArgs(ListType.UPCOMING, MOVIE_MEDIA_TYPE, ""))
  }

  @Test
  fun swipeScroll_whenRefresh_refreshData() {
    launchFragment()
    swipe_refresh_featured.performSwipeDown()
    swipe_refresh_featured.performSwipeDown()
    swipe_refresh_featured.performSwipeDown()
  }

  @Test
  fun onPause_whenSnackbarAndJobAreNull_shouldNotCrash() {
    launchFragment()
    scenario.moveToState(Lifecycle.State.CREATED)
  }

  @Test
  fun onDestroy_whenSnackbarAndJobAreNull_shouldNotCrash() {
    launchFragment()
    scenario.moveToState(Lifecycle.State.DESTROYED)
  }
}
