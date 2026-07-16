package com.waffiq.bazz_movies.feature.favorite.ui

import androidx.test.espresso.Espresso.onIdle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.waffiq.bazz_movies.core.designsystem.R.string.movies
import com.waffiq.bazz_movies.core.designsystem.R.string.tv_series
import com.waffiq.bazz_movies.core.favoritewatchlist.R.id.tabs
import com.waffiq.bazz_movies.core.favoritewatchlist.R.id.view_pager
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performSwipeLeft
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performTextClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
import com.waffiq.bazz_movies.feature.favorite.testutils.BaseFavoriteFragmentTestHelper
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
class FavoriteFragmentTest : BaseFavoriteFragmentTestHelper() {

  @Before
  override fun baseSetup() {
    super.baseSetup()
    launchFragment()
  }

  @Test
  fun fragment_whenCreated_shouldDisplayCorrectly() {
    view_pager.isDisplayed()
    tabs.isDisplayed()
  }

  @Test
  fun tabLayout_whenCreated_shouldHaveCorrectNumberOfTabs() {
    onView(withId(tabs)).check { view, _ ->
      val tabLayout = view as TabLayout
      assertEquals(2, tabLayout.tabCount)
    }
  }

  @Test
  fun viewPager_whenCreated_shouldHaveSwipeDisabled() {
    // Verify that ViewPager swiping is disabled by checking the property directly
    view_pager.assertViewPagerUserInputEnabled(false)

    // Test current item doesn't change after swipe attempt
    view_pager.performSwipeLeft()
    view_pager.assertViewPagerPosition(0) // should still be at position 0
  }

  @Test
  fun tabTvSeries_whenClicked_showsTvSeriesTab() {
    onIdle()
    tv_series.performTextClick()
  }

  @Test
  fun tabMovie_whenClicked_showsMoviesTab() {
    onIdle()
    movies.performTextClick()
  }

  @Test
  fun viewPager_whenCreated_shouldContainTwoFragments() {
    view_pager.assertViewPagerItemCount(2)
  }

  @Test
  fun tabLayoutMediator_whenClickSpecificTabs_shouldBeAttachedOnViewCreated() {
    shortDelay()
    tv_series.performTextClick()
    onView(withId(view_pager)).check { view, _ ->
      val viewPager = view as ViewPager2
      assertEquals(1, viewPager.currentItem)
    }
  }

  @Test
  fun onDestroyView_whenCalled_resetsState() {
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      favoriteFragment.requireActivity().supportFragmentManager
        .beginTransaction()
        .remove(favoriteFragment)
        .commitNow()
    }
  }
}
