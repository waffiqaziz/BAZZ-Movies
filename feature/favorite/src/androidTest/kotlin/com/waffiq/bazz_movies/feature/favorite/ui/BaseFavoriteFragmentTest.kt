package com.waffiq.bazz_movies.feature.favorite.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.waitUntilVisible
import com.waffiq.bazz_movies.core.movie.domain.usecase.composite.PostActionUseCase
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.feature.favorite.domain.usecase.composite.CheckAndAddToWatchlistUseCase
import com.waffiq.bazz_movies.feature.favorite.domain.usecase.favoritemovie.GetFavoriteMovieUseCase
import com.waffiq.bazz_movies.feature.favorite.domain.usecase.favoritetv.GetFavoriteTvUseCase
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.testMediaItem
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.userModel
import com.waffiq.bazz_movies.feature.favorite.testutils.DefaultFavoriteFragmentTestHelper
import com.waffiq.bazz_movies.feature.favorite.testutils.FavoriteFragmentTestHelper
import com.waffiq.bazz_movies.feature.favorite.ui.viewmodel.FavoriteViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class BaseFavoriteFragmentTest :
  FavoriteFragmentTestHelper by DefaultFavoriteFragmentTestHelper() {

  @get:Rule
  val instantTaskExecutorRule = InstantTaskExecutorRule()

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

  val getFavoriteMovieUseCase: GetFavoriteMovieUseCase = mockk()
  val getFavoriteTvUseCase: GetFavoriteTvUseCase = mockk()
  val postActionUseCase: PostActionUseCase = mockk()
  val checkAndAddToWatchlistUseCase: CheckAndAddToWatchlistUseCase = mockk()

  lateinit var mockFavoriteViewModel: FavoriteViewModel

  @Before
  fun setUp() {
    hiltRule.inject()
    mockFavoriteViewModel = FavoriteViewModel(
      getFavoriteMovieUseCase,
      getFavoriteTvUseCase,
      postActionUseCase,
      checkAndAddToWatchlistUseCase,
    )
    setupMocks(mockUserPrefViewModel)
  }

  @Test
  fun loggedUser_failedFavorite_showButtonTryAgain() {
    mockUserModel.postValue(userModel)
    every { getFavoriteMovieUseCase.getFavoriteMovies(any()) } returns
      flowOf(PagingData.from(listOf(testMediaItem)))
    launchFragment()

    waitUntilVisible(withText("Try Again"))
    onView(withText("Try Again")).perform(click())
  }
}
