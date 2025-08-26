package com.waffiq.bazz_movies.feature.favorite.ui.viewmodel

import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.domain.FavoriteModel
import com.waffiq.bazz_movies.core.domain.WatchlistModel
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers.SnackBarUserLoginData
import com.waffiq.bazz_movies.core.movie.domain.model.post.PostFavoriteWatchlist
import com.waffiq.bazz_movies.core.movie.domain.usecase.mediastate.GetMovieStateUseCase
import com.waffiq.bazz_movies.core.movie.domain.usecase.mediastate.GetTvStateUseCase
import com.waffiq.bazz_movies.core.movie.domain.usecase.postmethod.PostMethodUseCase
import com.waffiq.bazz_movies.feature.favorite.domain.usecase.GetFavoriteMovieUseCase
import com.waffiq.bazz_movies.feature.favorite.domain.usecase.GetFavoriteTvUseCase
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.ERROR_MESSAGE
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.TV_ID
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.fakeMovieMediaItemPagingData
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.fakeTvMediaItemPagingData
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.movieStateInWatchlist
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.movieStateNotWatchlist
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.outcomeError
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.outcomeLoading
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.outcomeSuccess
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.tvStateInWatchlist
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.tvStateNotWatchlist
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.user
import com.waffiq.bazz_movies.feature.favorite.testutils.Helper.testPagingFlowCustomDispatcher
import com.waffiq.bazz_movies.feature.favorite.testutils.Helper.testViewModelFlowEvent
import com.waffiq.bazz_movies.feature.favorite.testutils.InstantExecutorExtension
import com.waffiq.bazz_movies.feature.favorite.ui.MyFavoriteViewModel
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
 * Unit tests for [MyFavoriteViewModel] using Kotest BehaviorSpec.
 * This class tests the ViewModel's functional
 */
class MyFavoriteViewModelTest : BehaviorSpec({

  extensions(InstantExecutorExtension)
  val testDispatcher = UnconfinedTestDispatcher()

  val response = PostFavoriteWatchlist(
    statusCode = 200,
    statusMessage = "Success",
  )

  val getFavoriteMovieUseCase: GetFavoriteMovieUseCase = mockk()
  val getFavoriteTvUseCase: GetFavoriteTvUseCase = mockk()
  val postMethodUseCase: PostMethodUseCase = mockk()
  val getStatedMovieUseCase: GetMovieStateUseCase = mockk()
  val getStatedTvUseCase: GetTvStateUseCase = mockk()

  lateinit var viewModel: MyFavoriteViewModel

  beforeTest {
    Dispatchers.setMain(testDispatcher)
    viewModel = MyFavoriteViewModel(
      getFavoriteMovieUseCase,
      getFavoriteTvUseCase,
      postMethodUseCase,
      getStatedMovieUseCase,
      getStatedTvUseCase
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
      viewModel.checkTvStatedThenPostWatchlist(user, TV_ID, title)
    }

    fun verifyGetStatedTVCalled() {
      coVerify { getStatedTvUseCase.getTvState(user.token, TV_ID) }
    }

    When("the TV show is not in watchlist") {
      coEvery { getStatedTvUseCase.getTvState(user.token, TV_ID) } returns
        flowOf(outcomeSuccess(tvStateNotWatchlist))

      coEvery { postMethodUseCase.postWatchlist(user.token, any(), user.userId) } returns
        flowOf(outcomeSuccess(response))

      Then("it should call postWatchlist and emit success") {
        testViewModelFlowEvent(
          runBlock = { checkStateTv() },
          liveData = viewModel.snackBarAdded,
          expected = Event(
            SnackBarUserLoginData(true, title, null, WatchlistModel("tv", TV_ID, true))
          ),
          verifyBlock = {
            verifyGetStatedTVCalled()
            coVerify { postMethodUseCase.postWatchlist(user.token, any(), user.userId) }
          }
        )
      }
    }

    When("the TV show is already in watchlist") {
      coEvery { getStatedTvUseCase.getTvState(user.token, TV_ID) } returns
        flowOf(outcomeSuccess(tvStateInWatchlist))

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
      coEvery { getStatedTvUseCase.getTvState(user.token, TV_ID) } returns
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
      coEvery { getStatedTvUseCase.getTvState(user.token, TV_ID) } returns
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
      viewModel.postFavorite(user.token, user.userId, favoriteModel, title)
    }

    fun verifyPostFavoriteCalled() {
      coVerify { postMethodUseCase.postFavorite(user.token, favoriteModel, user.userId) }
    }

    When("post favorite is successful") {
      coEvery { postMethodUseCase.postFavorite(user.token, favoriteModel, user.userId) } returns
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
      coEvery { postMethodUseCase.postFavorite(user.token, favoriteModel, user.userId) } returns
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
      coEvery { postMethodUseCase.postFavorite(user.token, favoriteModel, user.userId) } returns
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
      viewModel.postWatchlist(user.token, user.userId, watchlistModel, title)
    }

    fun verifyPostWatchlistCalled() {
      coVerify { postMethodUseCase.postWatchlist(user.token, watchlistModel, user.userId) }
    }

    When("post watchlist is successful") {
      coEvery { postMethodUseCase.postWatchlist(user.token, watchlistModel, user.userId) } returns
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
      coEvery { postMethodUseCase.postWatchlist(user.token, watchlistModel, user.userId) } returns
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
      coEvery { postMethodUseCase.postWatchlist(user.token, watchlistModel, user.userId) } returns
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
      viewModel.checkMovieStatedThenPostWatchlist(user, movieId, title)
    }

    fun verifyGetStatedMovieCalled() {
      coVerify { getStatedMovieUseCase.getMovieState(user.token, movieId) }
    }

    When("the movie is not in watchlist and response successful") {
      coEvery { getStatedMovieUseCase.getMovieState(user.token, movieId) } returns
        flowOf(outcomeSuccess(movieStateNotWatchlist))

      coEvery { postMethodUseCase.postWatchlist(user.token, any(), user.userId) } returns
        flowOf(outcomeSuccess(response))

      Then("it should call postWatchlist and emit success") {
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
      coEvery { getStatedMovieUseCase.getMovieState(user.token, movieId) } returns
        flowOf(outcomeSuccess(movieStateInWatchlist))

      coEvery { postMethodUseCase.postWatchlist(user.token, any(), user.userId) } returns
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
      coEvery { getStatedMovieUseCase.getMovieState(user.token, movieId) } returns
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
      coEvery { getStatedMovieUseCase.getMovieState(user.token, movieId) } returns
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
