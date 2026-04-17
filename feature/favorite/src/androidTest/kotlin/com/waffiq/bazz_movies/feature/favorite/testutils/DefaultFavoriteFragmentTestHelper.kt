package com.waffiq.bazz_movies.feature.favorite.testutils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import androidx.test.espresso.Espresso.onIdle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.viewpager2.widget.ViewPager2
import com.google.common.truth.Truth.assertThat
import com.waffiq.bazz_movies.core.common.utils.Constants.NAN
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.designsystem.R.string.undo
import com.waffiq.bazz_movies.core.domain.Favorite
import com.waffiq.bazz_movies.core.domain.UserModel
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.viewmodel.SharedDBViewModel
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers.SnackBarUserLoginData
import com.waffiq.bazz_movies.core.instrumentationtest.CustomRecyclerViewActions.actionOnItemAt
import com.waffiq.bazz_movies.core.instrumentationtest.CustomRecyclerViewActions.scrollToPosition
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performTextClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.CustomVisibilityMatchers.isTextVisible
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
import com.waffiq.bazz_movies.core.instrumentationtest.launchFragmentInHiltContainer
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.feature.favorite.R.id.rv_favorite
import com.waffiq.bazz_movies.feature.favorite.R.id.swipe_refresh
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.favoriteMovie
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.favoriteMovie2
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.favoriteTv
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.testMediaItem
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.userModel
import com.waffiq.bazz_movies.feature.favorite.testutils.Helper.withCustomConstraints
import com.waffiq.bazz_movies.feature.favorite.ui.fragment.FavoriteFragment
import com.waffiq.bazz_movies.feature.favorite.ui.viewmodel.FavoriteViewModel
import io.mockk.every
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.receiveAsFlow
import org.junit.Rule

class DefaultFavoriteFragmentTestHelper : FavoriteFragmentTestHelper {

  @get:Rule
  val instantTaskExecutorRule = InstantTaskExecutorRule()

  override lateinit var favoriteFragment: FavoriteFragment

  override var mockUserModel = MutableLiveData<UserModel>()

  // Mocks for SharedDBViewModel
  override var mockFavoriteMoviesFromDB = MutableLiveData<List<Favorite>>()
  override var mockFavoriteTvFromDB = MutableLiveData<List<Favorite>>()
  override var mockUndoDB = MutableLiveData<Event<Favorite>>()
  override var mockDbResult = MutableLiveData<Event<DbResult<Int>>>()
  override var mockSnackBarAlready = MutableLiveData<Event<String>>()
  override var mockSnackBarChannel: Channel<SnackBarUserLoginData> = Channel()
  override var mockSnackBarAdded: Flow<SnackBarUserLoginData> =
    mockSnackBarChannel.receiveAsFlow()

  override fun setupMocks(userPreferenceViewModel: UserPreferenceViewModel) {
    mockUserModel = MutableLiveData()
    mockFavoriteMoviesFromDB = MutableLiveData()
    mockFavoriteTvFromDB = MutableLiveData()
    mockUndoDB = MutableLiveData()
    mockDbResult = MutableLiveData()

    every { userPreferenceViewModel.getUserPref() } returns mockUserModel
  }

  override fun loggedUser(favoriteViewModel: FavoriteViewModel) {
    mockUserModel.postValue(userModel)

    every { favoriteViewModel.snackBarAlready } returns mockSnackBarAlready
    every { favoriteViewModel.snackBarAdded } returns mockSnackBarAdded

    every { favoriteViewModel.getFavoriteData("movie") } returns
      flowOf(
        PagingData.from(
          listOf(
            testMediaItem,
            testMediaItem.copy(id = 1234)
          )
        )
      )

    every { favoriteViewModel.getFavoriteData("tv") } returns
      flowOf(
        PagingData.from(
          listOf(
            testMediaItem.copy(mediaType = TV_MEDIA_TYPE),
            testMediaItem.copy(mediaType = TV_MEDIA_TYPE, id = 1234)
          )
        )
      )
  }

  override fun guestUser(sharedDBViewModel: SharedDBViewModel) {
    mockFavoriteMoviesFromDB.postValue(listOf(favoriteMovie, favoriteMovie2))
    mockFavoriteTvFromDB.postValue(listOf(favoriteTv, favoriteTv.copy(id = 12345)))
    mockUserModel.postValue(userModel.copy(token = NAN))

    every { sharedDBViewModel.favoriteMoviesFromDB } returns mockFavoriteMoviesFromDB
    every { sharedDBViewModel.favoriteTvFromDB } returns mockFavoriteTvFromDB
    every { sharedDBViewModel.undoDB } returns mockUndoDB
    every { sharedDBViewModel.dbResult } returns mockDbResult
  }

  override fun launchFragment() {
    favoriteFragment = launchFragmentInHiltContainer<FavoriteFragment>()
    shortDelay()
  }

  override fun performSwipeActions() {
    rv_favorite.isDisplayed()
    onIdle()

    rv_favorite.scrollToPosition(0)
    performSwipeAction(0, swipeLeft())
    performSwipeAction(1, swipeRight())
  }

  override fun performPullToRefresh() {
    onView(withId(swipe_refresh))
      .perform(withCustomConstraints(swipeDown(), isDisplayingAtLeast(85)))
  }

  override fun performUndoAction() {
    undo.isTextVisible() // check if "undo" text is visible inside snackbar
    undo.performTextClick()
    shortDelay()
    onIdle()
  }

  override fun performSwipeAction(position: Int, viewAction: ViewAction) {
    rv_favorite.actionOnItemAt(position, viewAction)
  }

  override fun Int.assertViewPagerPosition(expected: Int) {
    onView(withId(this)).check { view, _ ->
      val vp = view as ViewPager2
      assertThat(vp.currentItem).isEqualTo(expected)
    }
  }

  override fun Int.assertViewPagerUserInputEnabled(expected: Boolean) {
    onView(withId(this)).check { view, _ ->
      val vp = view as ViewPager2
      assertThat(vp.isUserInputEnabled).isEqualTo(expected)
    }
  }

  override fun Int.assertViewPagerItemCount(expected: Int) {
    onView(withId(this)).check { view, _ ->
      val vp = view as ViewPager2
      assertThat(vp.adapter?.itemCount).isEqualTo(expected)
    }
  }
}
