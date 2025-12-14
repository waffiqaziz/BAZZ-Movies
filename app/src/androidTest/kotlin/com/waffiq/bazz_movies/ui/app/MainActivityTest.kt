package com.waffiq.bazz_movies.ui.app

import android.view.View
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.waffiq.bazz_movies.MainActivity
import com.waffiq.bazz_movies.R.id.bottom_navigation
import com.waffiq.bazz_movies.R.id.nav_host_fragment_activity_home
import com.waffiq.bazz_movies.R.id.navigation_home
import com.waffiq.bazz_movies.R.id.navigation_more
import com.waffiq.bazz_movies.R.id.navigation_my_favorite
import com.waffiq.bazz_movies.R.id.navigation_my_watchlist
import com.waffiq.bazz_movies.R.id.navigation_search
import com.waffiq.bazz_movies.feature.favorite.R.id.fragment_favorite
import com.waffiq.bazz_movies.feature.home.R.id.fragment_featured
import com.waffiq.bazz_movies.feature.more.R.id.fragment_more
import com.waffiq.bazz_movies.feature.search.R.id.fragment_search
import com.waffiq.bazz_movies.feature.watchlist.R.id.fragment_watchlist
import com.waffiq.bazz_movies.testrule.CleanDataStoreTestRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MainActivityTest {

  @get:Rule(order = 0)
  val hiltRule = HiltAndroidRule(this)

  @get:Rule(order = 1)
  val activityRule = ActivityScenarioRule(MainActivity::class.java)

  @get:Rule(order = 2)
  val cleanDataStoreTestRule = CleanDataStoreTestRule()

  @Before
  fun init() {
    hiltRule.inject()
  }

  @Test
  fun testActivityInitialization() {
    activityRule.scenario.onActivity { activity ->
      assertNotNull(activity.findViewById<BottomNavigationView>(bottom_navigation))
      assertNotNull(activity.supportFragmentManager.findFragmentById(nav_host_fragment_activity_home))
    }
  }

  @Test
  fun testBottomNavigationSetup() {
    activityRule.scenario.onActivity { activity ->
      val navController = findNavController(activity, nav_host_fragment_activity_home)
      val bottomNavigationView = activity.findViewById<BottomNavigationView>(bottom_navigation)

      assertEquals(navController.graph.startDestinationId, bottomNavigationView.selectedItemId)
    }
  }

  private fun findNavController(activity: MainActivity, navHostFragmentId: Int): NavController {
    val navHostFragment = activity.supportFragmentManager
      .findFragmentById(navHostFragmentId) as NavHostFragment
    return navHostFragment.navController
  }

  @Test
  fun testFragmentContainerViewInitialization() {
    activityRule.scenario.onActivity { activity ->
      // Get the FragmentContainerView
      val fragmentContainerView =
        activity.findViewById<FragmentContainerView>(nav_host_fragment_activity_home)
      assertNotNull(fragmentContainerView) // Ensure it's not null

      // Get the hosted fragment and verify its type
      val navHostFragment =
        activity.supportFragmentManager.findFragmentById(nav_host_fragment_activity_home)
      assertNotNull(navHostFragment)
      assertTrue(navHostFragment is NavHostFragment)
    }
  }

  @Test
  fun testBottomNavigationMenuItems() {
    activityRule.scenario.onActivity { activity ->
      val bottomNavigationView = activity.findViewById<BottomNavigationView>(bottom_navigation)
      val menu = bottomNavigationView.menu

      assertNotNull(menu.findItem(navigation_home))
      assertNotNull(menu.findItem(navigation_search))
      assertNotNull(menu.findItem(navigation_my_favorite))
      assertNotNull(menu.findItem(navigation_my_watchlist))
      assertNotNull(menu.findItem(navigation_more))
    }
  }

  @Test
  fun testBottomNavigationNavigation() {
    // Home navigation
    onView(withId(bottom_navigation)).perform(selectMenuItem(navigation_home))
    onView(withId(fragment_featured)).check(matches(isDisplayed()))
    Thread.sleep(DELAY_TIME)

    // Search navigation
    onView(withId(bottom_navigation)).perform(selectMenuItem(navigation_search))
    onView(withId(fragment_search)).check(matches(isDisplayed()))
    Thread.sleep(DELAY_TIME)

    // My Favorite navigation
    onView(withId(bottom_navigation)).perform(selectMenuItem(navigation_my_favorite))
    onView(withId(fragment_favorite)).check(matches(isDisplayed()))
    Thread.sleep(DELAY_TIME)

    // My Watchlist navigation
    onView(withId(bottom_navigation)).perform(selectMenuItem(navigation_my_watchlist))
    onView(withId(fragment_watchlist)).check(matches(isDisplayed()))
    Thread.sleep(DELAY_TIME)

    // More navigation
    onView(withId(bottom_navigation)).perform(selectMenuItem(navigation_more))
    onView(withId(fragment_more)).check(matches(isDisplayed()))
    Thread.sleep(DELAY_TIME)
  }

  private fun selectMenuItem(menuItemId: Int): ViewAction =
    object : ViewAction {
      override fun getConstraints(): Matcher<View> =
        isAssignableFrom(BottomNavigationView::class.java)

      override fun getDescription(): String =
        "Click on menu item with id $menuItemId"

      override fun perform(uiController: UiController, view: View) {
        val bottomNavigationView = view as BottomNavigationView
        bottomNavigationView.selectedItemId = menuItemId
      }
    }

  companion object {
    const val DELAY_TIME = 300L
  }
}
