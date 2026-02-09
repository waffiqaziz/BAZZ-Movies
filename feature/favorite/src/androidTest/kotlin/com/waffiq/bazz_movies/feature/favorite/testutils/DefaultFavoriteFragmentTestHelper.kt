package com.waffiq.bazz_movies.feature.favorite.testutils

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onIdle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.waffiq.bazz_movies.core.common.utils.Constants.NAN
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.designsystem.R.string.undo
import com.waffiq.bazz_movies.core.domain.Favorite
import com.waffiq.bazz_movies.core.domain.UserModel
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.viewmodel.SharedDBViewModel
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers.SnackBarUserLoginData
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
import kotlinx.coroutines.flow.flowOf

class DefaultFavoriteFragmentTestHelper : FavoriteFragmentTestHelper {

  override lateinit var favoriteFragment: FavoriteFragment

  override var mockUserModel = MutableLiveData<UserModel>()

  // Mocks for SharedDBViewModel
  override var mockFavoriteMoviesFromDB = MutableLiveData<List<Favorite>>()
  override var mockFavoriteTvFromDB = MutableLiveData<List<Favorite>>()
  override var mockUndoDB = MutableLiveData<Event<Favorite>>()
  override var mockDbResult = MutableLiveData<Event<DbResult<Int>>>()
  override var mockSnackBarAlready = MutableLiveData<Event<String>>()
  override var mockSnackBarAdded = MutableLiveData<Event<SnackBarUserLoginData>>()

  override fun setupMocks(userPreferenceViewModel: UserPreferenceViewModel) {
    mockUserModel = MutableLiveData<UserModel>()
    mockFavoriteMoviesFromDB = MutableLiveData<List<Favorite>>()
    mockFavoriteTvFromDB = MutableLiveData<List<Favorite>>()
    mockUndoDB = MutableLiveData<Event<Favorite>>()
    mockDbResult = MutableLiveData<Event<DbResult<Int>>>()

    every { userPreferenceViewModel.getUserPref() } returns mockUserModel
  }

  override fun loggedUser(favoriteViewModel: FavoriteViewModel) {
    mockUserModel.postValue(userModel)

    every { favoriteViewModel.snackBarAlready } returns mockSnackBarAlready
    every { favoriteViewModel.snackBarAdded } returns mockSnackBarAdded

    every { favoriteViewModel.favoriteMovies(any()) } returns
      flowOf(
        PagingData.from(
          listOf(
            testMediaItem,
            testMediaItem.copy(id = 1234)
          )
        )
      )

    every { favoriteViewModel.favoriteTvSeries(any()) } returns
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
    onView(withId(rv_favorite)).check(matches(isDisplayed()))
    onIdle()

    onView(withId(rv_favorite))
      .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
    performSwipeAction(0, swipeLeft())
    performSwipeAction(1, swipeRight())
  }

  override fun performPullToRefresh() {
    onView(withId(swipe_refresh))
      .perform(withCustomConstraints(swipeDown(), isDisplayingAtLeast(85)))
  }

  override fun performUndoAction() {
    onView(withText(undo)).check(matches(isDisplayed())).perform(click())
    shortDelay()
    onIdle()
  }

  override fun performSwipeAction(position: Int, viewAction: ViewAction) {
    onView(withId(rv_favorite)).perform(
      RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(position, viewAction)
    )
  }
}
