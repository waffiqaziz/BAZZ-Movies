package com.waffiq.bazz_movies.feature.home.ui.fragment

import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
import com.waffiq.bazz_movies.feature.home.R.id.layout_toolbar
import com.waffiq.bazz_movies.feature.home.R.id.tabs
import com.waffiq.bazz_movies.feature.home.R.id.view_pager
import com.waffiq.bazz_movies.feature.home.testutils.BaseHomeFragmentTest
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
class HomeFragmentTest : BaseHomeFragmentTest() {

  @Before
  override fun setup() {
    super.setup()
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
