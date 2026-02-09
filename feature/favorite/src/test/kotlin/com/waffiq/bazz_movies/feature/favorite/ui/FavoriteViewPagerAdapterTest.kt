package com.waffiq.bazz_movies.feature.favorite.ui

import androidx.fragment.app.FragmentActivity
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.feature.favorite.ui.fragment.FavoriteChildFragment
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

// For this case, tested with junit due to Kotest incompatible with Robolectric
// https://github.com/kotest/kotest/issues/3505#issuecomment-1546691763

// there's extension but not production-ready and its archived
// https://github.com/kotest/kotest-extensions-robolectric

// another workaround https://github.com/LeoColman/kotest-android/
// but not support with latest Kotest 6.0.7 and one of contributor is dropped
// https://github.com/LeoColman/kotest-android/issues/46#issuecomment-3442427020
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class FavoriteViewPagerAdapterTest {

  private lateinit var activity: FragmentActivity
  private lateinit var adapter: FavoriteViewPagerAdapter

  @Before
  fun setup() {
    // use real FragmentActivity with lifecycle
    activity = Robolectric.buildActivity(FragmentActivity::class.java)
      .setup()
      .get()

    adapter = FavoriteViewPagerAdapter(activity.supportFragmentManager, activity.lifecycle)
  }

  @Test
  fun getItemCount_whenCalled_shouldReturnsTwo() {
    assertEquals(2, adapter.itemCount)
  }

  @Test
  fun createFragment_positionZero_returnsMovieFavoriteChildFragment() {
    val fragment = adapter.createFragment(0)
    assertTrue(fragment is FavoriteChildFragment)
    assertEquals(MOVIE_MEDIA_TYPE, (fragment as FavoriteChildFragment).internalMediaType)
  }

  @Test
  fun createFragment_positionOne_returnsTvFavoriteChildFragment() {
    val fragment = adapter.createFragment(1)
    assertTrue(fragment is FavoriteChildFragment)
    assertEquals(TV_MEDIA_TYPE, (fragment as FavoriteChildFragment).internalMediaType)
  }

  @Test(expected = IllegalStateException::class)
  fun createFragment_invalidIndex_throwsException() {
    adapter.createFragment(5)
  }
}
