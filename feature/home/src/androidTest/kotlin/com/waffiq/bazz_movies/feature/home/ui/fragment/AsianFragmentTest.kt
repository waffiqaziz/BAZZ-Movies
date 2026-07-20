package com.waffiq.bazz_movies.feature.home.ui.fragment

import androidx.lifecycle.Lifecycle
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.waffiq.bazz_movies.core.designsystem.R.string.asian
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performScrollTo
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performSwipeDown
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performTextClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isNotDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.waitUntilVisible
import com.waffiq.bazz_movies.feature.home.R.id.btn_anime_all_time
import com.waffiq.bazz_movies.feature.home.R.id.btn_anime_this_season
import com.waffiq.bazz_movies.feature.home.R.id.btn_more_anime
import com.waffiq.bazz_movies.feature.home.R.id.btn_more_costume_drama
import com.waffiq.bazz_movies.feature.home.R.id.btn_more_donghua
import com.waffiq.bazz_movies.feature.home.R.id.btn_more_reality_show
import com.waffiq.bazz_movies.feature.home.R.id.btn_more_romance_drama
import com.waffiq.bazz_movies.feature.home.R.id.illustration_error_asian
import com.waffiq.bazz_movies.feature.home.R.id.layout_header_anime
import com.waffiq.bazz_movies.feature.home.R.id.rv_anime
import com.waffiq.bazz_movies.feature.home.R.id.shimmer_layout_asian
import com.waffiq.bazz_movies.feature.home.R.id.swipe_refresh_asian
import com.waffiq.bazz_movies.feature.home.testutils.BaseHomeFragmentTest
import com.waffiq.bazz_movies.feature.home.ui.domain.AnimePeriod
import com.waffiq.bazz_movies.navigation.ListType
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Test

@HiltAndroidTest
class AsianFragmentTest : BaseHomeFragmentTest() {

  @Test
  fun setData_whenLoadSuccess_shouldShowContentAndHideError() {
    launchAsianFragment()
    waitUntilVisible(withId(rv_anime))
    rv_anime.isDisplayed()
    layout_header_anime.isDisplayed()
    illustration_error_asian.isNotDisplayed()
  }

  @Test
  fun setData_whenLoadErrorNoItems_shouldShowErrorIllustration() {
    every { mockAsianViewModel.getDonghua() } returns createErrorPagingFlow()

    launchAsianFragment()

    waitUntilVisible(withId(illustration_error_asian))
    rv_anime.isNotDisplayed()
  }

  @Test
  fun setData_whenLoading_shouldShowShimmer() {
    every { mockAsianViewModel.getDonghua() } returns createStuckLoadingFlow()

    launchAsianFragment()
    shortDelay(1000)

    shimmer_layout_asian.isDisplayed()
  }

  @Test
  fun btnAnime_whenClicked_shouldCallsCorrectFunction() {
    launchAsianFragment()
    shortDelay()

    btn_anime_all_time.performClick()
    InstrumentationRegistry.getInstrumentation().waitForIdleSync()
    verify { mockAsianViewModel.setAnimePeriod(AnimePeriod.ALL_TIME) }

    every { mockAsianViewModel.animePeriod } returns MutableStateFlow(AnimePeriod.ALL_TIME)
    btn_more_anime.performClick()
    InstrumentationRegistry.getInstrumentation().waitForIdleSync()
    verifyOpenList(mockNavigator, tvArgs(ListType.ANIME_ALL_TIME))

    btn_anime_this_season.performClick()
    InstrumentationRegistry.getInstrumentation().waitForIdleSync()
    verify { mockAsianViewModel.setAnimePeriod(AnimePeriod.THIS_SEASON) }

    every { mockAsianViewModel.animePeriod } returns MutableStateFlow(AnimePeriod.THIS_SEASON)
    btn_more_anime.performClick()
    InstrumentationRegistry.getInstrumentation().waitForIdleSync()
    verifyOpenList(mockNavigator, tvArgs(ListType.ANIME_THIS_SEASON))
  }

  @Test
  fun btnAnime_whenClicked_shouldCallsCorrectFunction1() {
    launchAsianFragment()
    shortDelay(1000)
    btn_anime_this_season.performClick()
    InstrumentationRegistry.getInstrumentation().waitForIdleSync()
    verify { mockAsianViewModel.setAnimePeriod(AnimePeriod.THIS_SEASON) }
  }

  @Test
  fun moreButton_whenClicked_shouldOpenListActivity() {
    launchAsianFragment()

    btn_more_costume_drama.performScrollTo()
    btn_more_costume_drama.performClick()
    verifyOpenList(mockNavigator, tvArgs(ListType.COSTUME_DRAMA))

    btn_more_donghua.performScrollTo()
    btn_more_donghua.performClick()
    verifyOpenList(mockNavigator, tvArgs(ListType.DONGHUA))

    btn_more_romance_drama.performScrollTo()
    btn_more_romance_drama.performClick()
    verifyOpenList(mockNavigator, tvArgs(ListType.ROMANCE_DRAMA))

    btn_more_reality_show.performScrollTo()
    btn_more_reality_show.performClick()
    verifyOpenList(mockNavigator, tvArgs(ListType.REALITY_SHOW))
  }

  @Test
  fun swipeScroll_whenRefresh_refreshData() {
    launchAsianFragment()
    swipe_refresh_asian.performSwipeDown()
  }

  @Test
  fun onPause_whenSnackbarAndJobAreNull_shouldNotCrash() {
    launchAsianFragment()
    scenario.moveToState(Lifecycle.State.CREATED)
  }

  @Test
  fun onDestroy_whenSnackbarAndJobAreNull_shouldNotCrash() {
    launchAsianFragment()
    scenario.moveToState(Lifecycle.State.DESTROYED)
  }

  private fun launchAsianFragment() {
    launchFragment()
    shortDelay()
    asian.performTextClick()
    InstrumentationRegistry.getInstrumentation().waitForIdleSync()
  }
}
