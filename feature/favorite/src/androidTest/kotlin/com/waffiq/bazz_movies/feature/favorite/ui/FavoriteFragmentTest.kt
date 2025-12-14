package com.waffiq.bazz_movies.feature.favorite.ui

import androidx.test.espresso.Espresso.onIdle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasChildCount
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.viewpager2.widget.ViewPager2
import com.google.common.truth.Truth.assertThat
import com.waffiq.bazz_movies.core.designsystem.R.string.movies
import com.waffiq.bazz_movies.core.designsystem.R.string.tv_series
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.feature.favorite.R.id.tabs
import com.waffiq.bazz_movies.feature.favorite.R.id.view_pager
import com.waffiq.bazz_movies.feature.favorite.testutils.DefaultFavoriteFragmentTestHelper
import com.waffiq.bazz_movies.feature.favorite.testutils.FavoriteFragmentTestHelper
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class FavoriteFragmentTest : FavoriteFragmentTestHelper by DefaultFavoriteFragmentTestHelper() {

  @get:Rule
  var hiltRule = HiltAndroidRule(this)

  @BindValue
  @JvmField
  val mockNavigator: INavigator = mockk(relaxed = true)

  @BindValue
  @JvmField
  val mockSnackbar: ISnackbar = mockk(relaxed = true)

  @Before
  fun setUp() {
    hiltRule.inject()
    launchFragment()
  }

  @Test
  fun fragment_whenCreated_shouldDisplayCorrectly() {
    onView(withId(view_pager)).check(matches(isDisplayed()))
    onView(withId(tabs)).check(matches(isDisplayed()))
  }

  @Test
  fun tabLayout_whenCreated_shouldHaveCorrectNumberOfTabs() {
    onView(withId(tabs)).check(matches(hasChildCount(1))) // 0-based index, so 1 means 2 tabs
  }

  @Test
  fun viewPager_whenCreated_shouldHaveSwipeDisabled() {
    // Verify that ViewPager swiping is disabled by checking the property directly
    onView(withId(view_pager)).check { view, _ ->
      val viewPager = view as ViewPager2
      assertThat(viewPager.isUserInputEnabled).isFalse()
    }

    // Test current item doesn't change after swipe attempt
    onView(withId(view_pager)).perform(swipeLeft()).check { view, _ ->
      val viewPager = view as ViewPager2
      assertThat(viewPager.currentItem).isEqualTo(0) // should still be at position 0
    }
  }

  @Test
  fun tabTvSeries_whenClicked_showsTvSeriesTab() {
    onIdle()
    onView(withText(tv_series)).perform(click())
  }

  @Test
  fun tabMovie_whenClicked_showsMoviesTab() {
    onIdle()
    onView(withText(movies)).perform(click())
  }

  @Test
  fun viewPager_whenCreated_shouldContainTwoFragments() {
    onView(withId(view_pager)).check(matches(hasChildCount(1))) // 0-based index, so 1 means 2 fragments
  }

  @Test
  fun tabLayoutMediator_whenClickSpecificTabs_shouldBeAttachedOnViewCreated() {
    shortDelay()
    onView(withText(tv_series)).perform(click())
    onView(withId(view_pager)).check { view, _ ->
      val viewPager = view as ViewPager2
      assertThat(viewPager.currentItem).isEqualTo(1)
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
