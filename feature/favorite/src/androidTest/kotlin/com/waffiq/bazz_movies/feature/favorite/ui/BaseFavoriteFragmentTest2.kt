package com.waffiq.bazz_movies.feature.favorite.ui

import android.view.View
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.viewpager2.widget.ViewPager2
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.viewmodel.SharedDBViewModel
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.waitFor
import com.waffiq.bazz_movies.core.instrumentationtest.launchFragmentInHiltContainer
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.feature.favorite.R.id.illustration_no_data_view
import com.waffiq.bazz_movies.feature.favorite.R.id.progress_bar
import com.waffiq.bazz_movies.feature.favorite.R.id.view_pager
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.userModel
import com.waffiq.bazz_movies.feature.favorite.testutils.DefaultFavoriteFragmentTestHelper
import com.waffiq.bazz_movies.feature.favorite.testutils.FavoriteFragmentTestHelper
import com.waffiq.bazz_movies.feature.favorite.ui.fragment.FavoriteChildFragment
import com.waffiq.bazz_movies.feature.favorite.ui.fragment.FavoriteFragment
import com.waffiq.bazz_movies.feature.favorite.ui.viewmodel.FavoriteViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class BaseFavoriteFragmentTest2 :
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
  }

  @Test
  fun loggedUser_noFavorite_showEmptyIllustration() = runTest {
    mockUserModel.postValue(userModel)

    val pager = Pager(PagingConfig(pageSize = 20, enablePlaceholders = false)) {
      object : PagingSource<Int, MediaItem>() {
        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MediaItem> {
          return LoadResult.Page(
            data = emptyList(),
            prevKey = null,
            nextKey = null
          )
        }

        override fun getRefreshKey(state: PagingState<Int, MediaItem>): Int? = null
      }
    }

    every { mockFavoriteViewModel.favoriteMovies(any()) } returns pager.flow

    // Launch parent fragment
    launchFragmentInHiltContainer<FavoriteFragment> {
      // Access the fragment in this block
      val viewPager = this.requireView().findViewById<ViewPager2>(view_pager)

      // Force ViewPager to create and show first fragment
      viewPager.setCurrentItem(0, false)

      // Post to ensure ViewPager has created the fragment
      viewPager.post {
        val childFragment = this.childFragmentManager.fragments
          .firstOrNull { it is FavoriteChildFragment }
        childFragment?.view?.visibility = View.VISIBLE
      }
    }

    // Wait for ViewPager + debounce + paging load states
    onView(isRoot()).perform(waitFor(3000))

    // Check empty view is displayed
    onView(withId(illustration_no_data_view)).check(matches(isDisplayed()))
    onView(withId(progress_bar)).check(matches(not(isDisplayed())))
  }
}
