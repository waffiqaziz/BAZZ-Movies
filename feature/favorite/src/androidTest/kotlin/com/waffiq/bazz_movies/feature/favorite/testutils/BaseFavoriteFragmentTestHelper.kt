package com.waffiq.bazz_movies.feature.favorite.testutils

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.test.espresso.Espresso.onIdle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.common.utils.Constants.NAN
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.designsystem.R.color.red_matte
import com.waffiq.bazz_movies.core.designsystem.R.string.undo
import com.waffiq.bazz_movies.core.favoritewatchlist.R.id.recycler_view
import com.waffiq.bazz_movies.core.favoritewatchlist.R.id.snackbar_anchor_test
import com.waffiq.bazz_movies.core.favoritewatchlist.R.id.swipe_refresh
import com.waffiq.bazz_movies.core.favoritewatchlist.domain.sort.GuestFavoriteSortOption
import com.waffiq.bazz_movies.core.favoritewatchlist.domain.sort.LoggedFavoriteSortOption
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.viewmodel.BaseViewModel
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.viewmodel.SharedDBViewModel
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers.SnackBarUserLoginData
import com.waffiq.bazz_movies.core.instrumentationtest.CustomRecyclerViewActions.actionOnItemAt
import com.waffiq.bazz_movies.core.instrumentationtest.CustomRecyclerViewActions.scrollToPosition
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performTextClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.CustomVisibilityMatchers.isTextVisible
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
import com.waffiq.bazz_movies.core.instrumentationtest.launchFragmentInHiltContainer
import com.waffiq.bazz_movies.core.models.Favorite
import com.waffiq.bazz_movies.core.models.MediaItem
import com.waffiq.bazz_movies.core.models.UserModel
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.favoriteTv
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.listOfMovie
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.testMediaItem
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.userModel
import com.waffiq.bazz_movies.feature.favorite.testutils.Helper.withCustomConstraints
import com.waffiq.bazz_movies.feature.favorite.ui.fragment.FavoriteFragment
import com.waffiq.bazz_movies.feature.favorite.ui.viewmodel.FavoriteViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.testing.HiltAndroidRule
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.receiveAsFlow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

abstract class BaseFavoriteFragmentTestHelper {

  protected val snackBarLoginData = SnackBarUserLoginData(
    isSuccess = true,
    title = "Title",
    favoriteModel = null,
    watchlistModel = null,
  )

  @get:Rule(order = 0)
  var hiltRule = HiltAndroidRule(this)

  @get:Rule(order = 1)
  val instantTaskExecutorRule = InstantTaskExecutorRule()

  @Inject
  lateinit var mockNavigator: INavigator

  @Inject
  lateinit var mockSnackbar: ISnackbar

  @Inject
  lateinit var mockUserPrefViewModel: UserPreferenceViewModel

  @Inject
  lateinit var mockFavoriteViewModel: FavoriteViewModel

  @Inject
  lateinit var mockSharedDBViewModel: SharedDBViewModel

  @Inject
  lateinit var mockBaseViewModel: BaseViewModel

  @Before
  open fun baseSetup() {
    hiltRule.inject()
    setupMocks()
  }

  protected lateinit var favoriteFragment: FavoriteFragment

  protected var mockUserModel = MutableLiveData<UserModel>()

  // Mocks for SharedDBViewModel
  protected var mockFavoriteMoviesFromDB = MutableLiveData<List<Favorite>>()
  protected var mockFavoriteTvFromDB = MutableLiveData<List<Favorite>>()
  protected var mockUndoDB = MutableLiveData<Event<Favorite>>()
  protected var mockDbResult = MutableLiveData<Event<DbResult<*>>>()
  protected var mockSnackBarAlready = MutableLiveData<Event<String>>()
  protected var mockSnackBarChannel: Channel<SnackBarUserLoginData> = Channel()
  protected var mockSnackBarAdded: Flow<SnackBarUserLoginData> =
    mockSnackBarChannel.receiveAsFlow()

  private fun setupMocks() {
    mockUserModel = MutableLiveData()
    mockFavoriteMoviesFromDB = MutableLiveData()
    mockFavoriteTvFromDB = MutableLiveData()
    mockUndoDB = MutableLiveData()
    mockDbResult = MutableLiveData()

    every { mockNavigator.snackbarAnchor() } returns snackbar_anchor_test
    every { mockUserPrefViewModel.getUserPref() } returns mockUserModel

    setupSnackbar()
  }

  protected fun loggedUser(favoriteViewModel: FavoriteViewModel) {
    mockUserModel.postValue(userModel)

    every { favoriteViewModel.snackBarAlready } returns mockSnackBarAlready
    every { favoriteViewModel.snackBarAdded } returns mockSnackBarAdded
    every { favoriteViewModel.currentSort } returns
      MutableStateFlow(LoggedFavoriteSortOption.RECENTLY_ADDED)
    every { mockBaseViewModel.markSnackbarShown() } just Runs
    every { mockBaseViewModel.resetSnackbarShown() } just Runs

    every { favoriteViewModel.getFavoriteData("movie") } returns
      flowOf(
        PagingData.from(
          listOf(
            testMediaItem,
            testMediaItem.copy(id = 1234),
          ),
        ),
      )

    every { favoriteViewModel.getFavoriteData("tv") } returns
      flowOf(
        PagingData.from(
          listOf(
            testMediaItem.copy(mediaType = TV_MEDIA_TYPE),
            testMediaItem.copy(mediaType = TV_MEDIA_TYPE, id = 1234),
          ),
        ),
      )
    every { favoriteViewModel.updateSort(any()) } just Runs
  }

  protected fun guestUser(sharedDBViewModel: SharedDBViewModel) {
    mockFavoriteMoviesFromDB.postValue(listOfMovie)
    mockFavoriteTvFromDB.postValue(listOf(favoriteTv, favoriteTv.copy(id = 12345)))
    mockUserModel.postValue(userModel.copy(token = NAN))

    every { sharedDBViewModel.favoriteMoviesFromDB } returns mockFavoriteMoviesFromDB
    every { sharedDBViewModel.favoriteTvFromDB } returns mockFavoriteTvFromDB
    every { sharedDBViewModel.undoDB } returns mockUndoDB
    every { sharedDBViewModel.dbResult } returns mockDbResult
    every { sharedDBViewModel.currentSort } returns
      MutableStateFlow(GuestFavoriteSortOption.RECENTLY_ADDED)
    every { sharedDBViewModel.updateSort(any()) } just Runs
  }

  private fun setupSnackbar() {
    every { mockSnackbar.showSnackbarWarning(any<String>()) } answers {
      val message = firstArg<String>()
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
    }

    every { mockSnackbar.showSnackbarWarning(any<Event<String>>()) } answers {
      firstArg<Event<String>>().getContentIfNotHandled()?.let { errorMessage ->
        mockSnackbar.showSnackbarWarning(errorMessage)
      }
    }
  }

  protected fun launchFragment() {
    favoriteFragment = launchFragmentInHiltContainer<FavoriteFragment>().fragment
    shortDelay()
  }

  protected fun performSwipeActions() {
    recycler_view.isDisplayed()
    onIdle()

    recycler_view.scrollToPosition(0)
    performSwipeAction(1, swipeLeft())
    performSwipeAction(2, swipeRight())
  }

  protected fun performPullToRefresh() {
    onView(withId(swipe_refresh))
      .perform(withCustomConstraints(swipeDown(), isDisplayingAtLeast(85)))
  }

  protected fun performUndoAction() {
    undo.isTextVisible() // check if "undo" text is visible inside snackbar
    undo.performTextClick()
    shortDelay()
    onIdle()
  }

  protected fun performSwipeAction(position: Int, viewAction: ViewAction) {
    recycler_view.actionOnItemAt(position, viewAction)
  }

  protected fun Int.assertViewPagerPosition(expected: Int) {
    onView(withId(this)).check { view, _ ->
      val vp = view as ViewPager2
      assertEquals(expected, vp.currentItem)
    }
  }

  protected fun Int.assertViewPagerUserInputEnabled(expected: Boolean) {
    onView(withId(this)).check { view, _ ->
      val vp = view as ViewPager2
      assertEquals(expected, vp.isUserInputEnabled)
    }
  }

  protected fun Int.assertViewPagerItemCount(expected: Int) {
    onView(withId(this)).check { view, _ ->
      val vp = view as ViewPager2
      assertEquals(expected, vp.adapter?.itemCount)
    }
  }

  class ErrorPagingSource : PagingSource<Int, MediaItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MediaItem> =
      LoadResult.Error(Throwable("Network error"))

    override fun getRefreshKey(state: PagingState<Int, MediaItem>): Int? = null
  }
}
