package com.waffiq.bazz_movies.feature.favorite.ui

import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.test.espresso.Espresso.onIdle
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.swipeRight
import com.google.android.material.R.id.snackbar_text
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.designsystem.R.id.btn_try_again
import com.waffiq.bazz_movies.core.designsystem.R.id.chip_sort
import com.waffiq.bazz_movies.core.designsystem.R.id.ic_general_error
import com.waffiq.bazz_movies.core.designsystem.R.string.added_to_watchlist
import com.waffiq.bazz_movies.core.designsystem.R.string.already_watchlist
import com.waffiq.bazz_movies.core.designsystem.R.string.oldest_added
import com.waffiq.bazz_movies.core.designsystem.R.string.recently_added
import com.waffiq.bazz_movies.core.designsystem.R.string.removed_from_favorite
import com.waffiq.bazz_movies.core.designsystem.R.string.tv_series
import com.waffiq.bazz_movies.core.favoritewatchlist.R.id.snackbar_anchor_test
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers.SnackBarUserLoginData
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performTextClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.doesHaveText
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.doesNotExist
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
import com.waffiq.bazz_movies.core.models.FavoriteParams
import com.waffiq.bazz_movies.core.models.WatchlistParams
import com.waffiq.bazz_movies.feature.favorite.testutils.BaseFavoriteFragmentTestHelper
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coVerify
import io.mockk.every
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
class LoggedUserDelegateFavoriteTest : BaseFavoriteFragmentTestHelper() {

  @Before
  override fun baseSetup() {
    super.baseSetup()
    every { mockNavigator.snackbarAnchor() } returns snackbar_anchor_test
    setupSnackbar()
    loggedUser(mockFavoriteViewModel)
  }

  @Test
  fun swipeAction_onTvSeriesTan_shouldPassed() {
    launchFragment()
    tv_series.performTextClick()
    performSwipeActions()
  }

  @Test
  fun pullToRefresh_onTvSeriesTab_shouldTriggerRefresh() {
    launchFragment()
    tv_series.performTextClick()
    performPullToRefresh()
  }

  @Test
  fun pullToRefresh_onMovieTab_shouldTriggerRefresh() {
    launchFragment()
    performPullToRefresh()
  }

  @Test
  fun performSort_shouldSortCorrectly() {
    launchFragment()

    chip_sort.performClick()
    shortDelay()
    oldest_added.performTextClick()
  }

  @Test
  fun performSortSameSortType_doNothing() {
    launchFragment()

    chip_sort.performClick()
    shortDelay()
    recently_added.performTextClick()
  }

  @Test
  fun swipeAction_shouldPassed() {
    launchFragment()
    performSwipeActions()
  }

  @Test
  fun swipeLeft_showAddedSnackbar() =
    runTest {
      val data = snackBarLoginData.copy(
        watchlistModel = WatchlistParams(
          mediaType = "movie",
          mediaId = 1234,
          watchlist = true,
        ),
      )
      launchFragment()

      // swipe left
      performSwipeAction(1, swipeLeft())

      mockSnackBarChannel.send(data)
      onIdle()

      val snackbarText = getString(favoriteFragment.requireActivity(), added_to_watchlist)
      snackbar_text.doesHaveText("Title $snackbarText")
      performUndoAction()
    }

  @Test
  fun swipeRight_showDeletedSnackbar() =
    runTest {
      val data = snackBarLoginData.copy(
        favoriteModel = FavoriteParams(
          mediaType = "movie",
          mediaId = 12345,
          favorite = false,
        ),
      )

      launchFragment()

      // swipe right (isWantToDelete = true)
      performSwipeAction(2, swipeRight())

      mockSnackBarChannel.send(data)
      onIdle()

      val snackbarText = getString(favoriteFragment.requireActivity(), removed_from_favorite)
      snackbar_text.doesHaveText("Title $snackbarText")
      performUndoAction()
    }

  @Test
  fun pagingData_returnsError_showsErrorViews() {
    every { mockFavoriteViewModel.getFavoriteData("movie") } returns
      Pager(PagingConfig(pageSize = 20)) { ErrorPagingSource() }.flow
    every { mockBaseViewModel.isSnackbarShown.value } returns false

    launchFragment()
    shortDelay()

    btn_try_again.isDisplayed()
    ic_general_error.isDisplayed()

    // also shows snackbar
    snackbar_text.doesHaveText("Something went wrong")

    // press the button
    btn_try_again.performClick()
  }

  @Test
  fun pagingDataError_snackbarIsAlreadyShows_doesnShowsTwice() {
    every { mockFavoriteViewModel.getFavoriteData("movie") } returns
      Pager(PagingConfig(pageSize = 20)) { ErrorPagingSource() }.flow
    every { mockBaseViewModel.isSnackbarShown.value } returns true

    launchFragment()
    shortDelay()

    snackbar_text.doesNotExist()
  }

  @Test
  fun verifyMock() {
    val liveData = MutableLiveData(true)

    every { mockBaseViewModel.isSnackbarShown } returns liveData

    assertTrue(mockBaseViewModel.isSnackbarShown.value == true)
  }

  @Test
  fun swipeFailedResult_showFailedSnackbar() =
    runTest {
      val failedDate = SnackBarUserLoginData(
        title = "Test Error",
        isSuccess = false,
        favoriteModel = FavoriteParams(
          mediaType = "movie",
          mediaId = 23,
          favorite = false,
        ),
        watchlistModel = null,
      )

      launchFragment()

      // swipe right (isWantToDelete = true)
      mockSnackBarChannel.send(failedDate)
      performSwipeAction(2, swipeRight())

      snackbar_text.doesHaveText(failedDate.title)
      onIdle()
    }

  @Test
  fun swipeActionEmptyResult_doNothing() =
    runTest {
      val emptyData = snackBarLoginData.copy(
        title = "Test Empty Data",
        isSuccess = true,
        favoriteModel = null,
        watchlistModel = null,
      )

      launchFragment()
      mockSnackBarChannel.send(emptyData)

      // swipe right (isWantToDelete = true)
      performSwipeAction(2, swipeRight())

      mockSnackBarChannel.send(emptyData)
      onIdle()
    }

  @Test
  fun addToWatchlist_butDuplicate_showsSnackbarDuplicate() =
    runTest {
      loggedUser(mockFavoriteViewModel)
      launchFragment()

      performSwipeAction(2, swipeRight())

      mockSnackBarAlready.postValue(Event("This Movie"))
      onIdle()

      val snackbarText = getString(favoriteFragment.requireActivity(), already_watchlist)
      snackbar_text.doesHaveText("This Movie $snackbarText")
    }

  @Test
  fun undoDelete_withSuccessResponse_invokesPostFavoriteTwice() =
    runTest {
      val deleteData = snackBarLoginData.copy(
        favoriteModel = FavoriteParams(
          mediaType = "movie",
          mediaId = 12345,
          favorite = false,
        ),
      )

      launchFragment()

      performSwipeAction(2, swipeRight())
      mockSnackBarChannel.send(deleteData)
      onIdle()

      performUndoAction()
      val undoSuccessData = snackBarLoginData.copy(
        isSuccess = true,
        favoriteModel = null,
        watchlistModel = null,
      )
      mockSnackBarChannel.send(undoSuccessData)
      onIdle()

      coVerify(atLeast = 2) { mockFavoriteViewModel.postFavorite(any(), any()) }
    }

  @Test
  fun undoDelete_withFailedResponse_invokesPostFavoriteOnce() =
    runTest {
      val deleteData = snackBarLoginData.copy(
        favoriteModel = FavoriteParams(
          mediaType = "movie",
          mediaId = 12345,
          favorite = false,
        ),
      )

      launchFragment()

      performSwipeAction(2, swipeRight())
      mockSnackBarChannel.send(deleteData)
      onIdle()

      performUndoAction()
      val undoFailedData = snackBarLoginData.copy(
        isSuccess = false,
        favoriteModel = null,
        watchlistModel = null,
      )
      mockSnackBarChannel.send(undoFailedData)
      onIdle()

      coVerify(atLeast = 1) { mockFavoriteViewModel.postFavorite(any(), any()) }
    }
}
