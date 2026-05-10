package com.waffiq.bazz_movies.feature.home.ui

import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.feature.home.R.id.layout_toolbar
import com.waffiq.bazz_movies.feature.home.R.id.tabs
import com.waffiq.bazz_movies.feature.home.R.id.view_pager
import com.waffiq.bazz_movies.feature.home.testutils.BaseHomeFragmentTest
import com.waffiq.bazz_movies.feature.home.ui.viewmodel.MovieViewModel
import com.waffiq.bazz_movies.feature.home.ui.viewmodel.TvSeriesViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class HomeFragmentTest : BaseHomeFragmentTest() {

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

  @Before
  fun setUp() {
    hiltRule.inject()
    setupMockNavigator(mockNavigator)
    setupMockViewModel(mockMovieViewModel, mockTvSeriesViewModel)
    launchFragment()
  }

  @Test
  fun fragment_whenCreated_shouldDisplayCorrectly() {
    layout_toolbar.isDisplayed()
    view_pager.isDisplayed()
    tabs.isDisplayed()
    shortDelay(1000)
  }
}
