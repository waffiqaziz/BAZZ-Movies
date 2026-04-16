package com.waffiq.bazz_movies.feature.favorite.ui

import com.waffiq.bazz_movies.core.designsystem.R.string.tv_series
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.viewmodel.SharedDBViewModel
import com.waffiq.bazz_movies.core.instrumentationtest.ViewMatcher.performTextClick
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.feature.favorite.R.id.snackbar_anchor_test
import com.waffiq.bazz_movies.feature.favorite.testutils.DefaultFavoriteFragmentTestHelper
import com.waffiq.bazz_movies.feature.favorite.testutils.FavoriteFragmentTestHelper
import com.waffiq.bazz_movies.feature.favorite.ui.viewmodel.FavoriteViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@HiltAndroidTest
class TvSeriesChildFragmentTest :
  FavoriteFragmentTestHelper by DefaultFavoriteFragmentTestHelper() {

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
  fun setUp() {
    hiltRule.inject()
    setupMocks(mockUserPrefViewModel)
    every { mockNavigator.snackbarAnchor() } returns snackbar_anchor_test
  }

  @Test
  fun loggedUser_swipeAction_shouldPassed() {
    loggedUser(mockFavoriteViewModel)
    launchFragment()
    tv_series.performTextClick()
    performSwipeActions()
  }

  @Test
  fun loggedUser_pullToRefresh_shouldTriggerRefresh() {
    loggedUser(mockFavoriteViewModel)
    launchFragment()
    tv_series.performTextClick()
    performPullToRefresh()
  }

  @Test
  fun guestUser_swipeAction_shouldPassed() {
    guestUser(mockSharedDBViewModel)
    launchFragment()
    tv_series.performTextClick()
    performSwipeActions()
  }

  @Test
  fun guestUser_pullToRefresh_shouldTriggerRefresh() {
    guestUser(mockSharedDBViewModel)
    launchFragment()
    tv_series.performTextClick()
    performPullToRefresh()
  }

  @Test
  fun guestUser_pullToRefreshEmptyData_doNothing() {
    guestUser(mockSharedDBViewModel)

    // setup empty data
    mockFavoriteTvFromDB.postValue(emptyList())
    mockFavoriteMoviesFromDB.postValue(emptyList())

    launchFragment()

    tv_series.performTextClick()
    performPullToRefresh()
  }
}
