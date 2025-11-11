package com.waffiq.bazz_movies.feature.favorite.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.viewpager2.widget.ViewPager2
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.viewmodel.SharedDBViewModel
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.waitFor
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.feature.favorite.R.id.rv_favorite
import com.waffiq.bazz_movies.feature.favorite.R.id.view_pager
import com.waffiq.bazz_movies.feature.favorite.testutils.DefaultFavoriteFragmentTestHelper
import com.waffiq.bazz_movies.feature.favorite.testutils.FavoriteFragmentTestHelper
import com.waffiq.bazz_movies.feature.favorite.ui.fragment.FavoriteMoviesFragment
import com.waffiq.bazz_movies.feature.favorite.ui.viewmodel.FavoriteViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class FavoriteMovieFragmentTest
  : FavoriteFragmentTestHelper by DefaultFavoriteFragmentTestHelper() {

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
  val mockUserPrefViewModel: UserPreferenceViewModel = mockk(relaxed = true)

  @BindValue
  @JvmField
  val mockFavoriteViewModel: FavoriteViewModel = mockk(relaxed = true)

  @BindValue
  @JvmField
  val mockSharedDBViewModel: SharedDBViewModel = mockk(relaxed = true)

  @Before
  override fun setUp() {
    hiltRule.inject()

    setupMocks(mockUserPrefViewModel)
    super.setUp()
  }

  @Test
  fun openFavorite_loggedUser_shouldShowsDataFromNetworks() {
    loggedUser()
    onView(withId(rv_favorite)).check(matches(isDisplayed()))
  }

  @Test
  fun openFavorite_guestUser_shouldShowsDataFromLocalDatabase() {
    guestUser(mockSharedDBViewModel)

    onView(withId(rv_favorite)).check(matches(isDisplayed()))
    onView(isRoot()).perform(waitFor(1000))
  }

  @Test
  fun test() {
    val viewPager = favoriteFragment.requireView().findViewById<ViewPager2>(view_pager)
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      viewPager.setCurrentItem(0, false)
    }

    val moviesFragment = favoriteFragment
      .childFragmentManager
      .fragments
      .filterIsInstance<FavoriteMoviesFragment>()
      .first()

    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      moviesFragment.onDestroyView()
    }
  }
}
