package com.waffiq.bazz_movies.feature.favorite.ui.viewmodel

import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.domain.FavoriteModel
import com.waffiq.bazz_movies.core.domain.WatchlistModel
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers.SnackBarUserLoginData
import com.waffiq.bazz_movies.core.movie.domain.model.post.PostFavoriteWatchlist
import com.waffiq.bazz_movies.core.movie.domain.usecase.composite.PostActionUseCase
import com.waffiq.bazz_movies.feature.favorite.domain.model.WatchlistActionResult
import com.waffiq.bazz_movies.feature.favorite.domain.usecase.composite.CheckAndAddToWatchlistUseCase
import com.waffiq.bazz_movies.feature.favorite.domain.usecase.favoritemovie.GetFavoriteMovieUseCase
import com.waffiq.bazz_movies.feature.favorite.domain.usecase.favoritetv.GetFavoriteTvUseCase
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.ERROR_MESSAGE
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.TV_ID
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.fakeMovieMediaItemPagingData
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.fakeTvMediaItemPagingData
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.outcomeError
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.outcomeLoading
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.outcomeSuccess
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.user
import com.waffiq.bazz_movies.feature.favorite.testutils.Helper.testPagingFlowCustomDispatcher
import com.waffiq.bazz_movies.feature.favorite.testutils.Helper.testViewModelFlowEvent
import com.waffiq.bazz_movies.feature.favorite.testutils.InstantExecutorExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

/**
 * Unit tests for [FavoriteViewModel] using Kotest BehaviorSpec.
 * This class tests the ViewModel's functional
 */
class FavoriteViewModelTest : BehaviorSpec({

  extensions(InstantExecutorExtension)
  val testDispatcher = UnconfinedTestDispatcher()

  val response = PostFavoriteWatchlist(
    statusCode = 200,
    statusMessage = "Success",
  )

  val getFavoriteMovieUseCase: GetFavoriteMovieUseCase = mockk()
  val getFavoriteTvUseCase: GetFavoriteTvUseCase = mockk()
  val postActionUseCase: PostActionUseCase = mockk()
  val checkAndAddToWatchlistUseCase: CheckAndAddToWatchlistUseCase = mockk()

  lateinit var viewModel: FavoriteViewModel

  beforeTest {
    Dispatchers.setMain(testDispatcher)
    viewModel = FavoriteViewModel(
      getFavoriteMovieUseCase,
      getFavoriteTvUseCase,
      postActionUseCase,
      checkAndAddToWatchlistUseCase
    )
  }

  afterTest {
    Dispatchers.resetMain()
  }

  Given("fetching favorite movies") {
    When("the response is successful") {
      coEvery { getFavoriteMovieUseCase.getFavoriteMovies(user.token) } returns
        flowOf(fakeMovieMediaItemPagingData)

      Then("it should emit the favorite movies list") {
        testPagingFlowCustomDispatcher(viewModel.favoriteMovies(user.token)) {
          it[0].id shouldBe 1
          it[0].title shouldBe "Inception"
          it[0].overview shouldBe "A mind-bending thriller"
        }
      }
    }
  }

  Given("fetching favorite TV shows") {
    When("the response is successful") {
      coEvery { getFavoriteTvUseCase.getFavoriteTv(user.token) } returns
        flowOf(fakeTvMediaItemPagingData)

      Then("it should emit the favorite TV shows list") {
        testPagingFlowCustomDispatcher(viewModel.favoriteTvSeries(user.token)) {
          it[0].id shouldBe 1
          it[0].title shouldBe "Breaking Bad"
          it[0].overview shouldBe "A high school chemistry teacher turned methamphetamine producer"
        }
      }
    }
  }

  Given("checks TV state before posting to watchlist") {
    val title = "Breaking Bad"

    fun checkStateTv() {
      viewModel.addTvToWatchlist(TV_ID, title)
    }

    fun verifyGetStatedTVCalled() {
      coVerify { checkAndAddToWatchlistUseCase.addTvToWatchlist(TV_ID) }
    }

    When("the TV show is not in watchlist") {
      coEvery { checkAndAddToWatchlistUseCase.addTvToWatchlist(TV_ID) } returns
        flowOf(outcomeSuccess(WatchlistActionResult.Added))

      coEvery { postActionUseCase.postWatchlistWithAuth(any()) } returns
        flowOf(outcomeSuccess(response))

      Then("it should call postWatchlistWithAuth and emit success") {
        testViewModelFlowEvent(
          runBlock = { checkStateTv() },
          liveData = viewModel.snackBarAdded,
          expected = Event(
            SnackBarUserLoginData(
              isSuccess = true,
              title = title,
              favoriteModel = null,
              watchlistModel = WatchlistModel("tv", TV_ID, true)
            )
          ),
          verifyBlock = { verifyGetStatedTVCalled() }
        )
      }
    }

    When("the TV show is already in watchlist") {
      coEvery { checkAndAddToWatchlistUseCase.addTvToWatchlist(TV_ID) } returns
        flowOf(outcomeSuccess(WatchlistActionResult.AlreadyInWatchlist))

      Then("it should emit snackBarAlready with title") {
        testViewModelFlowEvent(
          runBlock = { checkStateTv() },
          liveData = viewModel.snackBarAlready,
          expected = Event(title),
          verifyBlock = { verifyGetStatedTVCalled() }
        )
      }
    }

    When("the response is unsuccessful") {
      coEvery { checkAndAddToWatchlistUseCase.addTvToWatchlist(TV_ID) } returns
        flowOf(outcomeError)

      Then("it should show snackBar error message") {
        testViewModelFlowEvent(
          runBlock = { checkStateTv() },
          liveData = viewModel.snackBarAdded,
          expected = Event(SnackBarUserLoginData(false, ERROR_MESSAGE, null, null)),
          verifyBlock = { verifyGetStatedTVCalled() }
        )
      }
    }

    When("the response is loading") {
      coEvery { checkAndAddToWatchlistUseCase.addTvToWatchlist(TV_ID) } returns
        flowOf(outcomeLoading)

      Then("do nothing") {
        testViewModelFlowEvent(
          runBlock = { checkStateTv() },
          liveData = viewModel.snackBarAdded,
          verifyBlock = { verifyGetStatedTVCalled() }
        )
      }
    }
  }

  Given("posting favorite") {
    val title = "Avatar"
    val favoriteModel = FavoriteModel("movie", 999, true)

    fun runPostFavorite() {
      viewModel.postFavorite(favoriteModel, title)
    }

    fun verifyPostFavoriteCalled() {
      coVerify { postActionUseCase.postFavoriteWithAuth(favoriteModel) }
    }

    When("post favorite is successful") {
      coEvery { postActionUseCase.postFavoriteWithAuth(favoriteModel) } returns
        flowOf(outcomeSuccess(response))

      Then("it should emit success snackbar") {
        testViewModelFlowEvent(
          runBlock = { runPostFavorite() },
          liveData = viewModel.snackBarAdded,
          expected = Event(SnackBarUserLoginData(true, title, favoriteModel, null)),
          verifyBlock = { verifyPostFavoriteCalled() }
        )
      }
    }

    When("post favorite is unsuccessful") {
      coEvery { postActionUseCase.postFavoriteWithAuth(favoriteModel) } returns
        flowOf(outcomeError)

      Then("it should emit error snackbar") {
        testViewModelFlowEvent(
          runBlock = { runPostFavorite() },
          liveData = viewModel.snackBarAdded,
          expected = Event(SnackBarUserLoginData(false, ERROR_MESSAGE, null, null)),
          verifyBlock = { verifyPostFavoriteCalled() }
        )
      }
    }

    When("post favorite is loading") {
      coEvery { postActionUseCase.postFavoriteWithAuth(favoriteModel) } returns
        flowOf(outcomeLoading)

      Then("it should emit nothing") {
        testViewModelFlowEvent(
          runBlock = { runPostFavorite() },
          liveData = viewModel.snackBarAdded,
          verifyBlock = { verifyPostFavoriteCalled() }
        )
      }
    }
  }

  Given("posting watchlist") {
    val title = "The Matrix"
    val watchlistModel = WatchlistModel("movie", 333, true)

    fun runPostWatchlist() {
      viewModel.postWatchlist(watchlistModel, title)
    }

    fun verifyPostWatchlistCalled() {
      coVerify { postActionUseCase.postWatchlistWithAuth(watchlistModel) }
    }

    When("post watchlist is successful") {
      coEvery { postActionUseCase.postWatchlistWithAuth(watchlistModel) } returns
        flowOf(outcomeSuccess(response))

      Then("it should emit success snackbar") {
        testViewModelFlowEvent(
          runBlock = { runPostWatchlist() },
          liveData = viewModel.snackBarAdded,
          expected = Event(SnackBarUserLoginData(true, title, null, watchlistModel)),
          verifyBlock = { verifyPostWatchlistCalled() }
        )
      }
    }

    When("post watchlist is unsuccessful") {
      coEvery { postActionUseCase.postWatchlistWithAuth(watchlistModel) } returns
        flowOf(outcomeError)

      Then("it should emit error snackbar") {
        testViewModelFlowEvent(
          runBlock = { runPostWatchlist() },
          liveData = viewModel.snackBarAdded,
          expected = Event(SnackBarUserLoginData(false, ERROR_MESSAGE, null, null)),
          verifyBlock = { verifyPostWatchlistCalled() }
        )
      }
    }

    When("post watchlist is loading") {
      coEvery { postActionUseCase.postWatchlistWithAuth(watchlistModel) } returns
        flowOf(outcomeLoading)

      Then("it should emit nothing") {
        testViewModelFlowEvent(
          runBlock = { runPostWatchlist() },
          liveData = viewModel.snackBarAdded,
          verifyBlock = { verifyPostWatchlistCalled() }
        )
      }
    }
  }

  Given("checks movie state before posting to watchlist") {
    val movieId = 111
    val title = "Inception"

    fun checkStateMovie() {
      viewModel.addMovieToWatchlist(movieId, title)
    }

    fun verifyGetStatedMovieCalled() {
      coVerify { checkAndAddToWatchlistUseCase.addMovieToWatchlist(movieId) }
    }

    When("the movie is not in watchlist and response successful") {
      coEvery { checkAndAddToWatchlistUseCase.addMovieToWatchlist(movieId) } returns
        flowOf(outcomeSuccess(WatchlistActionResult.Added))

      coEvery { postActionUseCase.postWatchlistWithAuth(any()) } returns
        flowOf(outcomeSuccess(response))

      Then("it should call postWatchlistWithAuth and emit success") {
        testViewModelFlowEvent(
          runBlock = { checkStateMovie() },
          liveData = viewModel.snackBarAdded,
          expected = Event(
            SnackBarUserLoginData(
              isSuccess = true,
              title = title,
              favoriteModel = null,
              watchlistModel = WatchlistModel("movie", movieId, true)
            )
          ),
          verifyBlock = { verifyGetStatedMovieCalled() }
        )
      }
    }

    When("the movie is in watchlist and response successful") {
      coEvery { checkAndAddToWatchlistUseCase.addMovieToWatchlist(movieId) } returns
        flowOf(outcomeSuccess(WatchlistActionResult.AlreadyInWatchlist))

      coEvery { postActionUseCase.postWatchlistWithAuth(any()) } returns
        flowOf(outcomeSuccess(response))

      Then("it should show snackBarAlready with title") {
        testViewModelFlowEvent(
          runBlock = { checkStateMovie() },
          liveData = viewModel.snackBarAlready,
          expected = Event(title),
          verifyBlock = { verifyGetStatedMovieCalled() }
        )
      }
    }

    When("the response is unsuccessful") {
      coEvery { checkAndAddToWatchlistUseCase.addMovieToWatchlist(movieId) } returns
        flowOf(outcomeError)

      Then("it should show snackBar error message") {
        testViewModelFlowEvent(
          runBlock = { checkStateMovie() },
          liveData = viewModel.snackBarAdded,
          expected = Event(SnackBarUserLoginData(false, ERROR_MESSAGE, null, null)),
          verifyBlock = { verifyGetStatedMovieCalled() }
        )
      }
    }

    When("the response is loading") {
      coEvery { checkAndAddToWatchlistUseCase.addMovieToWatchlist(movieId) } returns
        flowOf(outcomeLoading)

      Then("do nothing") {
        testViewModelFlowEvent(
          runBlock = { checkStateMovie() },
          liveData = viewModel.snackBarAdded,
          verifyBlock = { verifyGetStatedMovieCalled() }
        )
      }
    }
  }
})
