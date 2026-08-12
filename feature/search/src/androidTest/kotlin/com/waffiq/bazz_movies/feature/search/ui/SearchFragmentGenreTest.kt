package com.waffiq.bazz_movies.feature.search.ui

import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performScrollTo
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.doesNotExist
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isDisplayed
import com.waffiq.bazz_movies.feature.search.R.id.btn_movie
import com.waffiq.bazz_movies.feature.search.R.id.btn_tv
import com.waffiq.bazz_movies.feature.search.testutils.BaseSearchFragmentTest
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.verify
import org.junit.Test

@HiltAndroidTest
class SearchFragmentGenreTest : BaseSearchFragmentTest() {

  @Test
  fun searchFragment_performGenreClick_displaysViewsCorrectly() {
    // check tv-series genre
    btn_tv.performClick()
    "Kids".performScrollTo()
    "Kids".isDisplayed()
    "Horror".doesNotExist()

    // check movie genre
    btn_movie.performScrollTo()
    btn_movie.performClick()
    "Action & Adventure".doesNotExist() // tv-series genre should not show
    "Horror".performScrollTo()
    "Horror".isDisplayed() // movie genre should show

    "Adventure".performScrollTo()
    "Adventure".performClick()
    verify { mockNavigator.openList(any(), any()) }
  }
}
