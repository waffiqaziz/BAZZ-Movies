package com.waffiq.bazz_movies.feature.home.ui.fragment

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.waffiq.bazz_movies.core.common.utils.Constants.NAN
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performScrollTo
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performSwipeDown
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isNotDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.waitUntilVisible
import com.waffiq.bazz_movies.feature.home.R.id.btn_more_movie_playing_now_featured
import com.waffiq.bazz_movies.feature.home.R.id.btn_more_trending_featured
import com.waffiq.bazz_movies.feature.home.R.id.btn_more_upcoming_movie_featured
import com.waffiq.bazz_movies.feature.home.R.id.btn_trending_this_week
import com.waffiq.bazz_movies.feature.home.R.id.btn_trending_today
import com.waffiq.bazz_movies.feature.home.R.id.button_group
import com.waffiq.bazz_movies.feature.home.R.id.illustration_error_featured
import com.waffiq.bazz_movies.feature.home.R.id.img_main_featured
import com.waffiq.bazz_movies.feature.home.R.id.layout_header_movie_playing_now_featured
import com.waffiq.bazz_movies.feature.home.R.id.layout_header_trending_featured
import com.waffiq.bazz_movies.feature.home.R.id.layout_header_upcoming_movie_featured
import com.waffiq.bazz_movies.feature.home.R.id.rv_movie_playing_now_featured
import com.waffiq.bazz_movies.feature.home.R.id.rv_trending
import com.waffiq.bazz_movies.feature.home.R.id.rv_upcoming_movie_featured
import com.waffiq.bazz_movies.feature.home.R.id.swipe_refresh_featured
import com.waffiq.bazz_movies.feature.home.testutils.BaseHomeFragmentTest
import com.waffiq.bazz_movies.feature.home.ui.domain.TrendingPeriod
import com.waffiq.bazz_movies.navigation.ListArgs
import com.waffiq.bazz_movies.navigation.ListType
import com.waffiq.bazz_movies.navigation.MediaSource
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coVerify
import io.mockk.every
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Test

@HiltAndroidTest
class FeaturedFragmentTest : BaseHomeFragmentTest() {

  @Test
  fun setData_whenLoadSuccess_shouldShowContentAndHideError() {
    launchFragment()
    waitUntilVisible(withId(rv_trending))
    img_main_featured.isDisplayed()
    button_group.isDisplayed()
    illustration_error_featured.isNotDisplayed()
  }

  @Test
  fun setData_whenLoadErrorNoItems_shouldShowErrorIllustration() {
    every { mockMovieViewModel.trending } returns createErrorPagingFlow()

    launchFragment()

    waitUntilVisible(withId(illustration_error_featured))
    img_main_featured.isNotDisplayed()
    layout_header_trending_featured.isNotDisplayed()
    button_group.isNotDisplayed()
    btn_more_trending_featured.isNotDisplayed()
    rv_trending.isNotDisplayed()
    layout_header_upcoming_movie_featured.isNotDisplayed()
    rv_upcoming_movie_featured.isNotDisplayed()
    layout_header_movie_playing_now_featured.isNotDisplayed()
    rv_movie_playing_now_featured.isNotDisplayed()
  }

  @Test
  fun setData_whenLoading_shouldShowShimmer() {
    every { mockMovieViewModel.trending } returns createStuckLoadingFlow()

    launchFragment()
    shortDelay(1000)

    rv_trending.isNotDisplayed()
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
  fun btnTrending_whenClicked_shouldCallsCorrectFunction() {
    launchFragment()
    shortDelay(1000)

    btn_trending_today.performClick()
    InstrumentationRegistry.getInstrumentation().waitForIdleSync()
    verify { mockMovieViewModel.setTrendingPeriod(TrendingPeriod.TODAY) }

    every { mockMovieViewModel.trendingPeriod } returns MutableStateFlow(TrendingPeriod.TODAY)
    btn_more_trending_featured.performClick()
    InstrumentationRegistry.getInstrumentation().waitForIdleSync()
    verifyOpenList(mockNavigator, trendingArgs(ListType.TRENDING_TODAY))

    btn_trending_this_week.performClick()
    InstrumentationRegistry.getInstrumentation().waitForIdleSync()
    verify { mockMovieViewModel.setTrendingPeriod(TrendingPeriod.WEEK) }

    every { mockMovieViewModel.trendingPeriod } returns MutableStateFlow(TrendingPeriod.WEEK)
    btn_more_trending_featured.performClick()
    InstrumentationRegistry.getInstrumentation().waitForIdleSync()
    verifyOpenList(mockNavigator, trendingArgs(ListType.TRENDING_WEEK))
  }

  @Test
  fun btnTrending_whenClicked_shouldCallsCorrectFunction1() {
    launchFragment()
    shortDelay(1000)
    btn_trending_this_week.performClick()
    InstrumentationRegistry.getInstrumentation().waitForIdleSync()
    verify { mockMovieViewModel.setTrendingPeriod(TrendingPeriod.WEEK) }
  }

  @Test
  fun moreButton_whenClicked_shouldOpenListActivity() {
    launchFragment()
    btn_more_movie_playing_now_featured.performScrollTo()
    btn_more_movie_playing_now_featured.performClick()
    verifyOpenList(mockNavigator, movieArgs(ListType.NOW_PLAYING))

    btn_more_upcoming_movie_featured.performScrollTo()
    btn_more_upcoming_movie_featured.performClick()
    verifyOpenList(mockNavigator, movieArgs(ListType.UPCOMING))
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

  private fun trendingArgs(listType: ListType) = ListArgs(listType, MediaSource.Trending, "")
}
