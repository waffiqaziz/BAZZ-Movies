package com.waffiq.bazz_movies.feature.favorite.ui

import androidx.core.content.ContextCompat.getString
import androidx.test.espresso.Espresso.onIdle
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.platform.app.InstrumentationRegistry
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.R.id.snackbar_text
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.designsystem.R.id.chip_sort
import com.waffiq.bazz_movies.core.designsystem.R.string.added_to_watchlist
import com.waffiq.bazz_movies.core.designsystem.R.string.already_watchlist
import com.waffiq.bazz_movies.core.designsystem.R.string.rating_asc
import com.waffiq.bazz_movies.core.designsystem.R.string.recently_added
import com.waffiq.bazz_movies.core.designsystem.R.string.removed_from_favorite
import com.waffiq.bazz_movies.core.designsystem.R.string.tv_series
import com.waffiq.bazz_movies.core.favoritewatchlist.R.id.recycler_view
import com.waffiq.bazz_movies.core.favoritewatchlist.R.id.snackbar_anchor_test
import com.waffiq.bazz_movies.core.favoritewatchlist.R.id.view_pager
import com.waffiq.bazz_movies.core.instrumentationtest.CustomRecyclerViewActions.scrollToPosition
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performTextClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.doesHaveText
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
import com.waffiq.bazz_movies.feature.favorite.testutils.BaseFavoriteFragmentTestHelper
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.favoriteMovie
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.listOfMovie
import com.waffiq.bazz_movies.feature.favorite.ui.fragment.FavoriteChildFragment
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
class GuestUserDelegateFavoriteTest : BaseFavoriteFragmentTestHelper() {

  @Before
  override fun baseSetup() {
    super.baseSetup()
    every { mockNavigator.snackbarAnchor() } returns snackbar_anchor_test
    guestUser(mockSharedDBViewModel)
  }

  @Test
  fun swipeAction_onTvSeriesTab_shouldPassed() {
    launchFragment()
    tv_series.performTextClick()
    performSwipeActions()
  }

  @Test
  fun swipeAction_onMoviesTab_shouldPassed() {
    launchFragment()
    performSwipeActions()
  }

  @Test
  fun pullToRefresh_onTvSeriesTab_shouldTriggerRefresh() {
    launchFragment()
    tv_series.performTextClick()
    performPullToRefresh()
  }

  @Test
  fun pullToRefresh_onMoviessTab_shouldTriggerRefresh() {
    launchFragment()
    performPullToRefresh()
  }

  @Test
  fun pullToRefresh_whenDataIsEmpty_doNothing() {
    // setup empty data
    mockFavoriteTvFromDB.postValue(emptyList())
    mockFavoriteMoviesFromDB.postValue(emptyList())

    launchFragment()

    tv_series.performTextClick()
    performPullToRefresh()
  }

  @Test
  fun performSort_byRatingAscending_shouldSortCorrectly() {
    launchFragment()

    chip_sort.performClick()
    shortDelay()
    rating_asc.performTextClick()
  }

  @Test
  fun performSort_whenSameSortType_doNothing() {
    launchFragment()

    chip_sort.performClick()
    shortDelay()

    // select same sort type from the initial
    recently_added.performTextClick()
  }

  @Test
  fun delete_whitUndoAction_returnsSuccess() {
    val data = favoriteMovie.copy(isWatchlist = true)

    // set first item is on watchlist, and second item not in watchlist
    mockFavoriteMoviesFromDB.postValue(listOf(data, favoriteMovie.copy(id = 4567)))
    launchFragment()

    // swipe right delete data already in watchlist
    performSwipeAction(1, swipeRight())
    mockUndoDB.postValue(Event(data))
    onIdle()

    val snackbarText = getString(favoriteFragment.requireActivity(), removed_from_favorite)
    snackbar_text.doesHaveText("${data.title} $snackbarText")
    performUndoAction()

    // swipe right delete data not in watchlist
    performSwipeAction(2, swipeRight())
    mockUndoDB.postValue(Event(favoriteMovie.copy(id = 4567)))
    onIdle()
    performUndoAction()
  }

  @Test
  fun undoDelete_successful_restoresScrollPosition() {
    launchFragment()
    shortDelay()

    // swipe right to delete
    performSwipeAction(1, swipeRight())
    mockUndoDB.postValue(Event(favoriteMovie))
    onIdle()

    // scroll down to get better animation visibility
    recycler_view.scrollToPosition(10)
    shortDelay()

    performUndoAction()

    // simulate DB re-emitt after insert/update complete
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      mockFavoriteMoviesFromDB.postValue(listOfMovie)
    }
    onIdle()

    // scrollToPosition ran, then reset to null.
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      mockFavoriteMoviesFromDB.postValue(listOfMovie)
    }
    onIdle()

    shortDelay(1000)
    // manual checking the scroll, the position should move to top of recyclerview
    // which is the position where the item is deleted and undo
  }

  @Test
  fun undoAction_undoDBNull_doesNothing() {
    val data = favoriteMovie.copy(isWatchlist = false)
    mockFavoriteMoviesFromDB.postValue(listOf(data))
    launchFragment()

    performSwipeAction(1, swipeLeft())
    // no postValue to mockUndoDB, so undoDB.value is null
    onIdle()

    performUndoAction()
  }

  @Test
  fun undoAction_whenUndoEventAlreadyConsumed_doesNothing() {
    val data = favoriteMovie.copy(isWatchlist = false)
    mockFavoriteMoviesFromDB.postValue(listOf(data, favoriteMovie.copy(id = 4567)))
    launchFragment()

    // perform undo
    performSwipeAction(1, swipeLeft())
    val event = Event(data)
    mockUndoDB.postValue(event)
    onIdle()
    performUndoAction()

    // delete another item without postValue a new undo event.
    performSwipeAction(2, swipeLeft())
    onIdle()

    // the previous Event has already been consumed, so no undo action is performed.
    // getContentIfNotHandled() returns null and handleUndo() does nothing.
    performUndoAction()
  }

  @Test
  fun dbResult_whenEventAlreadyHandled_doesNothingOnSecondEmission() {
    mockFavoriteMoviesFromDB.postValue(listOf(favoriteMovie.copy(isWatchlist = false)))

    val event = Event(DbResult.Error("Error Action") as DbResult<*>)
    mockDbResult.postValue(event)
    launchFragment()
    onIdle()

    // re-post same already handled event object, triggers observer again,
    // but getContentIfNotHandled() now returns null
    mockDbResult.postValue(event)
    onIdle()
  }

  @Test
  fun addToWatchlist_alreadyWatchlist_showAlreadySnackbar() {
    val data = favoriteMovie.copy(isWatchlist = true)

    // setup item is on watchlist
    mockFavoriteMoviesFromDB.postValue(listOf(data))
    launchFragment()

    // swipe left (isWantToDelete = false)
    performSwipeAction(1, swipeLeft())

    mockUndoDB.postValue(Event(data))
    onIdle()

    val snackbarText = getString(favoriteFragment.requireActivity(), already_watchlist)
    snackbar_text.doesHaveText("${data.title} $snackbarText")
    onIdle()
  }

  @Test
  fun addToWatchlist_successful_showsSuccessSnackbar() {
    val data = favoriteMovie.copy(isWatchlist = false)

    // setup item is on watchlist
    mockFavoriteMoviesFromDB.postValue(listOf(data, data.copy(id = 4567)))
    launchFragment()

    // swipe left (isWantToDelete = false)
    performSwipeAction(2, swipeLeft())
    mockUndoDB.postValue(Event(data))
    onIdle()

    val snackbarText = getString(favoriteFragment.requireActivity(), added_to_watchlist)
    snackbar_text.doesHaveText("${data.title} $snackbarText")
    performUndoAction()
  }

  @Test
  fun addToWatclist_failed_showsErrorToast() {
    val data = favoriteMovie.copy(isWatchlist = false)
    mockDbResult.postValue(Event(DbResult.Error("Error Action")))

    // setup item is on watchlist
    mockFavoriteMoviesFromDB.postValue(listOf(data))
    launchFragment()

    // swipe left (isWantToDelete = false)
    performSwipeAction(1, swipeLeft())
    mockUndoDB.postValue(Event(data))
    onIdle()

    // cant handle toast need manual
  }

  @Test
  fun addToWatchlist_successful_showsAddedSnackbar() {
    val data = favoriteMovie.copy(isWatchlist = false)
    mockDbResult.postValue(Event(DbResult.Success(1)))

    // setup item is on watchlist
    mockFavoriteMoviesFromDB.postValue(listOf(data))
    launchFragment()

    // swipe left (isWantToDelete = false)
    performSwipeAction(1, swipeLeft())
    mockUndoDB.postValue(Event(data))
    onIdle()

    snackbar_text.isDisplayed()
  }

  @Test
  fun openFavorite_onInitial_shouldShowTabMovies() {
    launchFragment()
    val viewPager = favoriteFragment.requireView().findViewById<ViewPager2>(view_pager)
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      viewPager.setCurrentItem(0, false)
    }

    val moviesFragment = favoriteFragment
      .childFragmentManager
      .fragments
      .filterIsInstance<FavoriteChildFragment>()
      .first()

    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      moviesFragment.onDestroyView()
    }
  }
}
