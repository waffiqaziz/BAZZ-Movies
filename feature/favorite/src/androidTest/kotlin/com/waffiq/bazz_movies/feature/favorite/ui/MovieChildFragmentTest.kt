package com.waffiq.bazz_movies.feature.favorite.ui

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getString
import androidx.test.espresso.Espresso.onIdle
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.platform.app.InstrumentationRegistry
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.R.id.snackbar_text
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.designsystem.R.color.red_matte
import com.waffiq.bazz_movies.core.designsystem.R.id.chip_sort
import com.waffiq.bazz_movies.core.designsystem.R.string.added_to_watchlist
import com.waffiq.bazz_movies.core.designsystem.R.string.already_watchlist
import com.waffiq.bazz_movies.core.designsystem.R.string.oldest_added
import com.waffiq.bazz_movies.core.designsystem.R.string.recently_added
import com.waffiq.bazz_movies.core.designsystem.R.string.removed_from_favorite
import com.waffiq.bazz_movies.core.favoritewatchlist.R.id.snackbar_anchor_test
import com.waffiq.bazz_movies.core.favoritewatchlist.R.id.view_pager
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.viewmodel.BaseViewModel
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.viewmodel.SharedDBViewModel
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers.SnackBarUserLoginData
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performTextClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.doesHaveText
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
import com.waffiq.bazz_movies.core.models.FavoriteParams
import com.waffiq.bazz_movies.core.models.WatchlistParams
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.feature.favorite.testutils.BaseFavoriteFragmentTestHelper
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.favoriteMovie
import com.waffiq.bazz_movies.feature.favorite.ui.fragment.FavoriteChildFragment
import com.waffiq.bazz_movies.feature.favorite.ui.viewmodel.FavoriteViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MovieChildFragmentTest : BaseFavoriteFragmentTestHelper() {

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
    override fun showSnackbarWarning(message: String): Snackbar? =
      try {
        val childFragment = favoriteFragment.childFragmentManager.fragments.first()
        val rootView = childFragment.requireView().findViewById<View>(snackbar_anchor_test)

        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).apply {
          setBackgroundTint(ContextCompat.getColor(rootView.context, red_matte))
          show()
        }
      } catch (_: Exception) {
        null
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

  @BindValue
  @JvmField
  val mockBaseViewModel: BaseViewModel = mockk(relaxed = true)

  @Before
  fun setUp() {
    hiltRule.inject()
    setupMocks(mockUserPrefViewModel)
    every { mockNavigator.snackbarAnchor() } returns snackbar_anchor_test
  }

  @Test
  fun loggedUser_performSort_shouldSortCorrectly() {
    loggedUser(mockFavoriteViewModel)
    launchFragment()

    chip_sort.performClick()
    shortDelay()
    oldest_added.performTextClick()
  }

  @Test
  fun loggedUser_performSortSameSortType_doNothing() {
    loggedUser(mockFavoriteViewModel)
    launchFragment()

    chip_sort.performClick()
    shortDelay()
    recently_added.performTextClick()
  }

  @Test
  fun loggedUser_swipeAction_shouldPassed() {
    loggedUser(mockFavoriteViewModel)
    launchFragment()
    performSwipeActions()
  }

  @Test
  fun loggedUser_swipeLeft_showAddedSnackbar() =
    runTest {
      val data = snackBarLoginData.copy(
        watchlistModel = WatchlistParams(
          mediaType = "movie",
          mediaId = 1234,
          watchlist = true,
        ),
      )
      loggedUser(mockFavoriteViewModel)
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
  fun loggedUser_swipeRight_showDeletedSnackbar() =
    runTest {
      val data = snackBarLoginData.copy(
        favoriteModel = FavoriteParams(
          mediaType = "movie",
          mediaId = 12345,
          favorite = false,
        ),
      )

      loggedUser(mockFavoriteViewModel)
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
  fun loggedUser_swipeFailedResult_showFailedSnackbar() =
    runTest {
      val failedDate = snackBarLoginData.copy(
        title = "Test Error",
        isSuccess = false,
        favoriteModel = FavoriteParams(
          mediaType = "movie",
          mediaId = 12345,
          favorite = false,
        ),
      )

      loggedUser(mockFavoriteViewModel)
      launchFragment()

      // swipe right (isWantToDelete = true)
      performSwipeAction(2, swipeRight())

      mockSnackBarChannel.send(failedDate)
      snackbar_text.doesHaveText(failedDate.title)
      onIdle()
    }

  @Test
  fun loggedUser_swipeActionEmptyResult_doNothing() =
    runTest {
      val emptyData = snackBarLoginData.copy(
        title = "Test Empty Data",
        isSuccess = true,
        favoriteModel = null,
        watchlistModel = null,
      )

      loggedUser(mockFavoriteViewModel)
      launchFragment()
      mockSnackBarChannel.send(emptyData)

      // swipe right (isWantToDelete = true)
      performSwipeAction(2, swipeRight())

      mockSnackBarChannel.send(emptyData)
      onIdle()
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

      loggedUser(mockFavoriteViewModel)
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

      loggedUser(mockFavoriteViewModel)
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

    // set first item is on watchlist, and second item not in watchlist
    mockFavoriteMoviesFromDB.postValue(listOf(data, favoriteMovie.copy(id = 4567)))
    launchFragment()
    shortDelay(500)

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
  fun guestUser_swipeLeft_alreadyWatchlist() {
    val data = favoriteMovie.copy(isWatchlist = true)
    guestUser(mockSharedDBViewModel)

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
  fun guestUser_swipeLeft_addToWatchlist() {
    val data = favoriteMovie.copy(isWatchlist = false)
    guestUser(mockSharedDBViewModel)

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
  fun guestUser_failedSwipeAction_showError() {
    val data = favoriteMovie.copy(isWatchlist = false)
    guestUser(mockSharedDBViewModel)
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
  fun guestUser_successSwipeAction_showSuccess() {
    val data = favoriteMovie.copy(isWatchlist = false)
    guestUser(mockSharedDBViewModel)
    mockDbResult.postValue(Event(DbResult.Success(1)))

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
