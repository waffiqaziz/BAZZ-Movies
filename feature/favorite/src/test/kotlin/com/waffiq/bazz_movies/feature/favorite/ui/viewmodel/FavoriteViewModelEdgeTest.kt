package com.waffiq.bazz_movies.feature.favorite.ui.viewmodel

import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.core.domain.Rated
import com.waffiq.bazz_movies.core.domain.WatchlistParams
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers.SnackBarUserLoginData
import com.waffiq.bazz_movies.core.movie.domain.model.post.PostFavoriteWatchlist
import com.waffiq.bazz_movies.core.movie.domain.repository.IMoviesRepository
import com.waffiq.bazz_movies.core.movie.domain.usecase.composite.PostActionUseCase
import com.waffiq.bazz_movies.core.test.KotestInstantExecutorExtension
import com.waffiq.bazz_movies.core.user.domain.repository.IUserRepository
import com.waffiq.bazz_movies.feature.favorite.domain.usecase.composite.CheckAndAddToWatchlistInteractor
import com.waffiq.bazz_movies.feature.favorite.domain.usecase.favoritemovie.GetFavoriteMovieUseCase
import com.waffiq.bazz_movies.feature.favorite.domain.usecase.favoritetv.GetFavoriteTvUseCase
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.ERROR_MESSAGE
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.MOVIE_ID
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.TV_ID
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.outcomeError
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.outcomeLoading
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.outcomeSuccess
import com.waffiq.bazz_movies.feature.favorite.testutils.Helper.testViewModelFlow
import com.waffiq.bazz_movies.feature.favorite.testutils.Helper.testViewModelLiveDataEvent
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

class FavoriteViewModelEdgeTest : BehaviorSpec({

  extensions(KotestInstantExecutorExtension)
  val testDispatcher = UnconfinedTestDispatcher()

  val movieId = 12345
  val title = "title"

  val response = PostFavoriteWatchlist(
    statusCode = 200,
    statusMessage = "Success",
  )

  val getFavoriteMovieUseCase: GetFavoriteMovieUseCase = mockk()
  val getFavoriteTvUseCase: GetFavoriteTvUseCase = mockk()
  val mockPostActionUseCase: PostActionUseCase = mockk()

  val mockMoviesRepository: IMoviesRepository = mockk()
  val mockUserRepository: IUserRepository = mockk()

  val checkAndAddToWatchlistUseCase = CheckAndAddToWatchlistInteractor(
    mockMoviesRepository,
    mockPostActionUseCase,
    mockUserRepository
  )

  lateinit var viewModel: FavoriteViewModel

  beforeTest {
    Dispatchers.setMain(testDispatcher)
    every { mockUserRepository.getUserToken() } returns flowOf("session")
    viewModel = FavoriteViewModel(
      getFavoriteMovieUseCase,
      getFavoriteTvUseCase,
      mockPostActionUseCase,
      checkAndAddToWatchlistUseCase // Real implementation!
    )
  }

  afterTest {
    Dispatchers.resetMain()
  }

  Given("checks movie state before posting to watchlist") {
    When("the movie is not in watchlist") {
      coEvery { mockMoviesRepository.getMovieState("session", MOVIE_ID) } returns
        flowOf(
          outcomeSuccess(
            MediaState(
              id = 102,
              favorite = false,
              rated = Rated.Unrated,
              watchlist = false
            )
          )
        )

      coEvery { mockPostActionUseCase.postWatchlistWithAuth(any()) } returns
        flowOf(outcomeSuccess(response))

      Then("it should add to watchlist") {
        testViewModelFlow(
          runBlock = { viewModel.addMovieToWatchlist(MOVIE_ID, title) },
          flow = viewModel.snackBarAdded,
          expected = SnackBarUserLoginData(
            true,
            title,
            null,
            WatchlistParams("movie", MOVIE_ID, true)
          ),
          verifyBlock = { coVerify { checkAndAddToWatchlistUseCase.addTvToWatchlist(TV_ID) } }
        )
      }
    }


    Given("checks movie state before posting to watchlist") {
      When("the movie is not in watchlist") {
        coEvery { mockMoviesRepository.getMovieState("session", movieId) } returns
          flowOf(
            outcomeSuccess(
              MediaState(
                id = 102,
                favorite = false,
                rated = Rated.Unrated,
                watchlist = false
              )
            )
          )

        coEvery { mockPostActionUseCase.postWatchlistWithAuth(any()) } returns
          flowOf(outcomeSuccess(response))

        Then("it should add to watchlist") {
          testViewModelFlow(
            runBlock = { viewModel.addMovieToWatchlist(movieId, title) },
            flow = viewModel.snackBarAdded,
            expected = SnackBarUserLoginData(
              true,
              title,
              null,
              WatchlistParams("movie", movieId, true)
            ),
            verifyBlock = { }
          )
        }
      }

      When("the movie is already in watchlist") {
        coEvery { mockMoviesRepository.getMovieState("session", movieId) } returns
          flowOf(
            outcomeSuccess(
              MediaState(
                id = 102,
                favorite = false,
                rated = Rated.Unrated,
                watchlist = true // ALREADY IN WATCHLIST
              )
            )
          )

        Then("it should show already in watchlist message") {
          testViewModelLiveDataEvent(
            runBlock = { viewModel.addMovieToWatchlist(movieId, title) },
            liveData = viewModel.snackBarAlready,
            expected = Event(title),
            verifyBlock = { }
          )
        }
      }

      When("getting movie state fails") {
        coEvery { mockMoviesRepository.getMovieState("session", movieId) } returns
          flowOf(outcomeError)

        Then("it should show error message") {
          testViewModelFlow(
            runBlock = { viewModel.addMovieToWatchlist(movieId, title) },
            flow = viewModel.snackBarAdded,
            expected = SnackBarUserLoginData(false, ERROR_MESSAGE, null, null),
            verifyBlock = { }
          )
        }
      }

      When("getting movie state is loading") {
        coEvery { mockMoviesRepository.getMovieState("session", movieId) } returns
          flowOf(outcomeLoading)

        Then("it should do nothing") {
          testViewModelFlow(
            runBlock = { viewModel.addMovieToWatchlist(movieId, title) },
            flow = viewModel.snackBarAdded,
            expected = null,
            verifyBlock = { }
          )
        }
      }

      When("posting to watchlist fails after state check") {
        coEvery { mockMoviesRepository.getMovieState("session", movieId) } returns
          flowOf(
            outcomeSuccess(
              MediaState(
                id = 102,
                favorite = false,
                rated = Rated.Unrated,
                watchlist = false
              )
            )
          )

        coEvery { mockPostActionUseCase.postWatchlistWithAuth(any()) } returns
          flowOf(outcomeError)

        Then("it should show error message") {
          testViewModelFlow(
            runBlock = { viewModel.addMovieToWatchlist(movieId, title) },
            flow = viewModel.snackBarAdded,
            expected = SnackBarUserLoginData(false, ERROR_MESSAGE, null, null),
            verifyBlock = { }
          )
        }
      }
    }
  }
})
