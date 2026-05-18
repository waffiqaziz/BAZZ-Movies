package com.waffiq.bazz_movies.feature.favorite.ui.viewmodel

import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.data.domain.model.post.PostFavoriteWatchlist
import com.waffiq.bazz_movies.core.data.domain.usecase.composite.MediaStateUseCase
import com.waffiq.bazz_movies.core.data.domain.usecase.composite.PostActionUseCase
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers.SnackBarUserLoginData
import com.waffiq.bazz_movies.core.models.MediaState
import com.waffiq.bazz_movies.core.models.Rated
import com.waffiq.bazz_movies.core.models.WatchlistParams
import com.waffiq.bazz_movies.core.test.KotestInstantExecutorExtension
import com.waffiq.bazz_movies.feature.favorite.domain.usecase.composite.CheckAndAddToWatchlistInteractor
import com.waffiq.bazz_movies.feature.favorite.domain.usecase.favoritemovie.GetFavoriteMovieUseCase
import com.waffiq.bazz_movies.feature.favorite.domain.usecase.favoritetv.GetFavoriteTvUseCase
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.ERROR_MESSAGE
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.outcomeError
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.outcomeLoading
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.outcomeSuccess
import com.waffiq.bazz_movies.feature.favorite.testutils.Helper.testViewModelFlow
import com.waffiq.bazz_movies.feature.favorite.testutils.Helper.testViewModelLiveDataEvent
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

class FavoriteViewModelEdgeTest :
  BehaviorSpec({

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
    val mockMediaStateUseCase: MediaStateUseCase = mockk()

    val checkAndAddToWatchlistUseCase = CheckAndAddToWatchlistInteractor(
      mockMediaStateUseCase,
      mockPostActionUseCase,
    )

    lateinit var viewModel: FavoriteViewModel

    beforeTest {
      Dispatchers.setMain(testDispatcher)

      viewModel = FavoriteViewModel(
        getFavoriteMovieUseCase,
        getFavoriteTvUseCase,
        mockPostActionUseCase,
        checkAndAddToWatchlistUseCase,
      )
    }

    afterTest {
      Dispatchers.resetMain()
    }

    fun mockMovieState(watchlist: Boolean) {
      coEvery { mockMediaStateUseCase.getMovieStateWithUser(movieId) } returns
        flowOf(
          outcomeSuccess(
            MediaState(
              id = 102,
              favorite = false,
              rated = Rated.Unrated,
              watchlist = watchlist,
            ),
          ),
        )
    }

    fun mockPostWatchlistSuccess() {
      coEvery { mockPostActionUseCase.postWatchlistWithAuth(any()) } returns
        flowOf(outcomeSuccess(response))
    }

    fun verifySnackBarAdded(expected: SnackBarUserLoginData?) {
      testViewModelFlow(
        runBlock = { viewModel.addMovieToWatchlist(movieId, title) },
        flow = viewModel.snackBarAdded,
        expected = expected,
        verifyBlock = { },
      )
    }

    fun verifyErrorSnackBar() {
      verifySnackBarAdded(
        SnackBarUserLoginData(false, ERROR_MESSAGE, null, null),
      )
    }

    Given("checks movie state before posting to watchlist") {

      When("the movie is not in watchlist") {

        beforeTest {
          mockMovieState(watchlist = false)
          mockPostWatchlistSuccess()
        }

        Then("it should add to watchlist") {
          verifySnackBarAdded(
            expected = SnackBarUserLoginData(
              true,
              title,
              null,
              WatchlistParams("movie", movieId, true),
            ),
          )
        }
      }

      When("the movie is already in watchlist") {

        beforeTest {
          mockMovieState(watchlist = true)
        }

        Then("it should show already in watchlist message") {
          testViewModelLiveDataEvent(
            runBlock = { viewModel.addMovieToWatchlist(movieId, title) },
            liveData = viewModel.snackBarAlready,
            expected = Event(title),
            verifyBlock = { },
          )
        }
      }

      When("getting movie state fails") {

        beforeTest {
          coEvery {
            mockMediaStateUseCase.getMovieStateWithUser(movieId)
          } returns flowOf(outcomeError)
        }

        Then("it should show error message") {
          verifyErrorSnackBar()
        }
      }

      When("getting movie state is loading") {

        beforeTest {
          coEvery {
            mockMediaStateUseCase.getMovieStateWithUser(movieId)
          } returns flowOf(outcomeLoading)
        }

        Then("it should do nothing") {
          verifySnackBarAdded(expected = null)
        }
      }

      When("posting to watchlist fails after state check") {

        beforeTest {
          mockMovieState(watchlist = false)

          coEvery {
            mockPostActionUseCase.postWatchlistWithAuth(any())
          } returns flowOf(outcomeError)
        }

        Then("it should show error message") {
          verifyErrorSnackBar()
        }
      }
    }
  })
