package com.waffiq.bazz_movies.feature.favorite.ui

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getString
import androidx.test.espresso.Espresso.onIdle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.R.id.snackbar_text
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.designsystem.R.color.red_matte
import com.waffiq.bazz_movies.core.designsystem.R.string.added_to_watchlist
import com.waffiq.bazz_movies.core.designsystem.R.string.already_watchlist
import com.waffiq.bazz_movies.core.designsystem.R.string.removed_from_favorite
import com.waffiq.bazz_movies.core.domain.FavoriteModel
import com.waffiq.bazz_movies.core.domain.WatchlistModel
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.viewmodel.SharedDBViewModel
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers.SnackBarUserLoginData
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.feature.favorite.R.id.snackbar_anchor_test
import com.waffiq.bazz_movies.feature.favorite.R.id.view_pager
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.favoriteMovie
import com.waffiq.bazz_movies.feature.favorite.testutils.DefaultFavoriteFragmentTestHelper
import com.waffiq.bazz_movies.feature.favorite.testutils.FavoriteFragmentTestHelper
import com.waffiq.bazz_movies.feature.favorite.ui.fragment.FavoriteChildFragment
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
class MovieChildFragmentTest :
  FavoriteFragmentTestHelper by DefaultFavoriteFragmentTestHelper() {

  private val snackBarLoginData = SnackBarUserLoginData(
    isSuccess = true,
    title = "Title",
    favoriteModel = null,
    watchlistModel = null,
  )

  @get:Rule
  var hiltRule = HiltAndroidRule(this)

  @BindValue
  @JvmField
  val mockNavigator: INavigator = mockk(relaxed = true)

  @BindValue
  @JvmField
  val mockSnackbar: ISnackbar = object : ISnackbar {
    override fun showSnackbarWarning(message: String): Snackbar? {
      return try {
        val childFragment = favoriteFragment.childFragmentManager.fragments.first()
        val rootView = childFragment.requireView().findViewById<View>(snackbar_anchor_test)

        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).apply {
          setBackgroundTint(ContextCompat.getColor(rootView.context, red_matte))
          show()
        }
      } catch (_: Exception) {
        null
      }
    }

    override fun showSnackbarWarning(eventMessage: Event<String>): Snackbar? =
      eventMessage.getContentIfNotHandled()?.let { showSnackbarWarning(it) }
  }

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
    performSwipeActions()
  }

  @Test
  fun loggedUser_swipeLeft_showAddedSnackbar() {
    val data = snackBarLoginData.copy(
      watchlistModel = WatchlistModel(
        mediaType = "movie",
        mediaId = 1234,
        watchlist = true
      )
    )
    loggedUser(mockFavoriteViewModel)
    launchFragment()

    // swipe left
    performSwipeAction(0, swipeLeft())

    mockSnackBarAdded.postValue(Event(data))
    onIdle()

    val snackbarText = getString(favoriteFragment.requireActivity(), added_to_watchlist)
    onView(withId(snackbar_text)).check(matches(withText("Title $snackbarText")))
    performUndoAction()
  }

  @Test
  fun loggedUser_swipeRight_showDeletedSnackbar() {
    val data = snackBarLoginData.copy(

      favoriteModel = FavoriteModel(
        mediaType = "movie",
        mediaId = 12345,
        favorite = false
      ),
    )

    loggedUser(mockFavoriteViewModel)
    launchFragment()

    // swipe right (isWantToDelete = true)
    performSwipeAction(1, swipeRight())

    mockSnackBarAdded.postValue(Event(data))
    onIdle()

    val snackbarText = getString(favoriteFragment.requireActivity(), removed_from_favorite)
    onView(withId(snackbar_text)).check(matches(withText("Title $snackbarText")))
    performUndoAction()
  }

  @Test
  fun loggedUser_swipeFailedResult_showFailedSnackbar() {
    val failedDate = snackBarLoginData.copy(
      title = "Test Error",
      isSuccess = false,
      favoriteModel = FavoriteModel(
        mediaType = "movie",
        mediaId = 12345,
        favorite = false
      ),
    )

    loggedUser(mockFavoriteViewModel)
    launchFragment()

    // swipe right (isWantToDelete = true)
    performSwipeAction(1, swipeRight())

    mockSnackBarAdded.postValue(Event(failedDate))
    onView(withId(snackbar_text)).check(matches(withText(failedDate.title)))
    onIdle()
  }

  @Test
  fun loggedUser_swipeActionEmptyResult_doNothing() {
    val emptyData = snackBarLoginData.copy(
      title = "Test Empty Data",
      isSuccess = true,
      favoriteModel = null,
      watchlistModel = null
    )

    loggedUser(mockFavoriteViewModel)
    mockSnackBarAdded.postValue(Event(emptyData))
    launchFragment()

    // swipe right (isWantToDelete = true)
    performSwipeAction(1, swipeRight())

    mockSnackBarAdded.postValue(Event(emptyData))
    onIdle()
  }

  @Test
  fun guestUser_swipeAction_shouldPassed() {
    guestUser(mockSharedDBViewModel)
    launchFragment()
    performSwipeActions()
  }

  @Test
  fun guestUser_swipeRight_removeFromFavorite() {
    val data = favoriteMovie.copy(isWatchlist = true)
    guestUser(mockSharedDBViewModel)

    // setup first item is on watchlist, and second item not in watchlist
    mockFavoriteMoviesFromDB.postValue(listOf(data, favoriteMovie.copy(id = 4567)))
    launchFragment()

    // swipe right delete data already in watchlist
    performSwipeAction(0, swipeRight())
    mockUndoDB.postValue(Event(data))
    onIdle()

    val snackbarText = getString(favoriteFragment.requireActivity(), removed_from_favorite)
    onView(withId(snackbar_text)).check(matches(withText("${data.title} $snackbarText")))
    performUndoAction()

    // swipe right delete data not in watchlist
    performSwipeAction(1, swipeRight())
    mockUndoDB.postValue(Event(favoriteMovie.copy(id = 4567)))
    onIdle()
    performUndoAction()
  }

  @Test
  fun guestUser_swipeLeft_alreadyWatchlist() {
    val data = favoriteMovie.copy(isWatchlist = true)
    guestUser(mockSharedDBViewModel)

    // setup item is on watchlist
    mockFavoriteMoviesFromDB.postValue(listOf(data))
    launchFragment()

    // swipe left (isWantToDelete = false)
    performSwipeAction(0, swipeLeft())

    mockUndoDB.postValue(Event(data))
    onIdle()

    val snackbarText = getString(favoriteFragment.requireActivity(), already_watchlist)
    onView(withId(snackbar_text)).check(matches(withText("${data.title} $snackbarText")))
    onIdle()
  }

  @Test
  fun guestUser_swipeLeft_addToWatchlist() {
    val data = favoriteMovie.copy(isWatchlist = false)
    guestUser(mockSharedDBViewModel)

    // setup item is on watchlist
    mockFavoriteMoviesFromDB.postValue(listOf(data, data.copy(id = 4567)))
    launchFragment()

    // swipe left (isWantToDelete = false)
    performSwipeAction(1, swipeLeft())
    mockUndoDB.postValue(Event(data))
    onIdle()

    val snackbarText = getString(favoriteFragment.requireActivity(), added_to_watchlist)
    onView(withId(snackbar_text)).check(matches(withText("${data.title} $snackbarText")))
    performUndoAction()
  }

  @Test
  fun guestUser_failedSwipeAction_showError() {
    val data = favoriteMovie.copy(isWatchlist = false)
    guestUser(mockSharedDBViewModel)
    mockDbResult.postValue(Event(DbResult.Error("Error Action")))

    // setup item is on watchlist
    mockFavoriteMoviesFromDB.postValue(listOf(data))
    launchFragment()

    // swipe left (isWantToDelete = false)
    performSwipeAction(0, swipeLeft())
    mockUndoDB.postValue(Event(data))
    onIdle()

    // cant handle toast need manual
  }

  @Test
  fun guestUser_successSwipeAction_showSuccess() {
    val data = favoriteMovie.copy(isWatchlist = false)
    guestUser(mockSharedDBViewModel)
    mockDbResult.postValue(Event(DbResult.Success(1)))

    // setup item is on watchlist
    mockFavoriteMoviesFromDB.postValue(listOf(data))
    launchFragment()

    // swipe left (isWantToDelete = false)
    performSwipeAction(0, swipeLeft())
    mockUndoDB.postValue(Event(data))
    onIdle()

    // cant handle toast need manual
  }

  @Test
  fun loggedUser_pullToRefresh_shouldTriggerRefresh() {
    loggedUser(mockFavoriteViewModel)
    launchFragment()
    performPullToRefresh()
  }

  @Test
  fun guestUser_pullToRefresh_shouldTriggerRefresh() {
    guestUser(mockSharedDBViewModel)
    launchFragment()
    performPullToRefresh()
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
