package com.waffiq.bazz_movies.feature.favorite.domain.usecase.composite

import app.cash.turbine.test
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.Rated
import com.waffiq.bazz_movies.core.domain.UpdateWatchlistParams
import com.waffiq.bazz_movies.core.movie.domain.model.post.PostFavoriteWatchlist
import com.waffiq.bazz_movies.core.movie.domain.usecase.composite.PostActionUseCase
import com.waffiq.bazz_movies.core.movie.domain.usecase.mediastate.GetMovieStateUseCase
import com.waffiq.bazz_movies.core.movie.domain.usecase.mediastate.GetTvStateUseCase
import com.waffiq.bazz_movies.core.user.domain.usecase.userpreference.UserPrefUseCase
import com.waffiq.bazz_movies.feature.favorite.domain.model.WatchlistActionResult
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain

class CheckAndAddToWatchlistInteractorTest : BehaviorSpec({

  val getMovieStateUseCase: GetMovieStateUseCase = mockk(relaxed = true)
  val getTvStateUseCase: GetTvStateUseCase = mockk(relaxed = true)
  val postActionUseCase: PostActionUseCase = mockk(relaxed = true)
  val userPrefUseCase: UserPrefUseCase = mockk(relaxed = true)

  lateinit var checkAndAddToWatchlistInteractor: CheckAndAddToWatchlistInteractor

  val movieId = 1234
  val tvId = 5678
  val token = "token"

  beforeTest {
    Dispatchers.setMain(UnconfinedTestDispatcher())

    checkAndAddToWatchlistInteractor = CheckAndAddToWatchlistInteractor(
      getMovieStateUseCase,
      getTvStateUseCase,
      postActionUseCase,
      userPrefUseCase,
    )
    coEvery { userPrefUseCase.getUserToken() } returns flowOf(token)
  }

  afterTest {
    clearMocks(
      getMovieStateUseCase,
      getTvStateUseCase,
      postActionUseCase,
      userPrefUseCase
    )
  }

  Given("a user with valid token") {

    When("adding a movie to watchlist") {

      And("movie is not in watchlist") {
        val stateResponse = Outcome.Success(
          MediaState(
            id = movieId,
            favorite = false,
            rated = Rated.Unrated,
            watchlist = false
          )
        )
        val postResponse = Outcome.Success(
          PostFavoriteWatchlist(
            statusCode = 201,
            statusMessage = "Success"
          )
        )

        coEvery { getMovieStateUseCase.getMovieState(any(), any()) } returns
          flowOf(stateResponse)
        coEvery { postActionUseCase.postWatchlistWithAuth(any()) } returns
          flowOf(postResponse)

        Then("should emit Loading then Added") {
          checkAndAddToWatchlistInteractor.addMovieToWatchlist(movieId).test {
            awaitItem() shouldBe Outcome.Loading
            awaitItem() shouldBe Outcome.Success(WatchlistActionResult.Added)
            awaitComplete()
          }

          coVerify { getMovieStateUseCase.getMovieState(any(), any()) }
          coVerify {
            postActionUseCase.postWatchlistWithAuth(UpdateWatchlistParams(MOVIE_MEDIA_TYPE, movieId, true))
          }
        }
      }

      And("movie is already in watchlist") {
        val stateResponse = Outcome.Success(
          MediaState(
            id = movieId,
            favorite = false,
            rated = Rated.Unrated,
            watchlist = true
          )
        )

        coEvery { getMovieStateUseCase.getMovieState(any(), any()) } returns
          flowOf(stateResponse)

        Then("should emit Loading then AlreadyInWatchlist") {
          checkAndAddToWatchlistInteractor.addMovieToWatchlist(movieId).test {
            awaitItem() shouldBe Outcome.Loading
            awaitItem() shouldBe Outcome.Success(WatchlistActionResult.AlreadyInWatchlist)
            awaitComplete()
          }

          coVerify { getMovieStateUseCase.getMovieState(any(), any()) }
          coVerify(exactly = 0) { postActionUseCase.postWatchlistWithAuth(any()) } // this cause error
        }
      }

      And("getting movie state returns error") {
        val errorMessage = "Failed to get movie state"
        coEvery { getMovieStateUseCase.getMovieState(any(), any()) } returns
          flowOf(Outcome.Error(errorMessage))

        Then("should emit Loading then Error") {
          checkAndAddToWatchlistInteractor.addMovieToWatchlist(movieId).test {
            awaitItem() shouldBe Outcome.Loading
            awaitItem() shouldBe Outcome.Error(errorMessage)
            cancelAndIgnoreRemainingEvents()
          }

          coVerify { getMovieStateUseCase.getMovieState(any(), any()) }
          coVerify(exactly = 0) { postActionUseCase.postWatchlistWithAuth(any()) } // this error its called
        }
      }

      And("getting movie state emits loading state") {
        val stateResponse = Outcome.Success(
          MediaState(
            id = movieId,
            favorite = false,
            rated = Rated.Unrated,
            watchlist = false
          )
        )

        coEvery { getMovieStateUseCase.getMovieState(any(), any()) } returns
          flow {
            emit(Outcome.Loading)
            emit(stateResponse)
          }
        coEvery { postActionUseCase.postWatchlistWithAuth(any()) } returns
          flowOf(Outcome.Success(PostFavoriteWatchlist(201, "Success")))

        Then("should emit all loading states and final result") {
          checkAndAddToWatchlistInteractor.addMovieToWatchlist(movieId).test {
            awaitItem() shouldBe Outcome.Loading // Initial
            awaitItem() shouldBe Outcome.Loading // From getMovieState
            awaitItem() shouldBe Outcome.Success(WatchlistActionResult.Added)
            awaitComplete()
          }
        }
      }

      And("posting to watchlist fails") {
        val stateResponse = Outcome.Success(
          MediaState(
            id = movieId,
            favorite = false,
            rated = Rated.Unrated,
            watchlist = false
          )
        )
        val errorMessage = "Failed to add to watchlist"

        coEvery { getMovieStateUseCase.getMovieState(any(), any()) } returns
          flowOf(stateResponse)
        coEvery { postActionUseCase.postWatchlistWithAuth(any()) } returns
          flowOf(Outcome.Error(errorMessage))

        Then("should emit Loading then Error") {
          checkAndAddToWatchlistInteractor.addMovieToWatchlist(movieId).test {
            awaitItem() shouldBe Outcome.Loading
            awaitItem() shouldBe Outcome.Error(errorMessage)
            awaitComplete()
          }

          coVerify { getMovieStateUseCase.getMovieState(any(), any()) }
          coVerify { postActionUseCase.postWatchlistWithAuth(any()) }
        }
      }

      And("posting to watchlist emits loading state") {
        val stateResponse = Outcome.Success(
          MediaState(
            id = movieId,
            favorite = false,
            rated = Rated.Unrated,
            watchlist = false
          )
        )

        coEvery { getMovieStateUseCase.getMovieState(any(), any()) } returns
          flowOf(stateResponse)
        coEvery { postActionUseCase.postWatchlistWithAuth(any()) } returns
          flow {
            emit(Outcome.Loading)
            emit(Outcome.Success(PostFavoriteWatchlist(201, "Success")))
          }

        Then("should emit all loading states and final result") {
          checkAndAddToWatchlistInteractor.addMovieToWatchlist(movieId).test {
            awaitItem() shouldBe Outcome.Loading // Initial
            awaitItem() shouldBe Outcome.Loading // From postWatchlist
            awaitItem() shouldBe Outcome.Success(WatchlistActionResult.Added)
            awaitComplete()
          }
        }
      }

      And("multiple emissions from post action") {
        val stateResponse = Outcome.Success(
          MediaState(
            id = movieId,
            favorite = false,
            rated = Rated.Unrated,
            watchlist = false
          )
        )

        coEvery { getMovieStateUseCase.getMovieState(any(), any()) } returns
          flowOf(stateResponse)
        coEvery { postActionUseCase.postWatchlistWithAuth(any()) } returns
          flow {
            emit(Outcome.Loading)
            emit(Outcome.Loading)
            emit(Outcome.Success(PostFavoriteWatchlist(201, "Success")))
          }

        Then("should emit all states in order") {
          checkAndAddToWatchlistInteractor.addMovieToWatchlist(movieId).test {
            awaitItem() shouldBe Outcome.Loading
            awaitItem() shouldBe Outcome.Loading
            awaitItem() shouldBe Outcome.Loading
            awaitItem() shouldBe Outcome.Success(WatchlistActionResult.Added)
            awaitComplete()
          }
        }
      }
    }

    When("adding a TV show to watchlist") {

      And("TV show is not in watchlist") {
        val stateResponse = Outcome.Success(
          MediaState(
            id = tvId,
            favorite = false,
            rated = Rated.Unrated,
            watchlist = false
          )
        )
        val postResponse = Outcome.Success(
          PostFavoriteWatchlist(
            statusCode = 201,
            statusMessage = "Success"
          )
        )

        coEvery { getTvStateUseCase.getTvState(any(), any()) } returns
          flowOf(stateResponse)
        coEvery { postActionUseCase.postWatchlistWithAuth(any()) } returns
          flowOf(postResponse)

        Then("should emit Loading then Added") {
          checkAndAddToWatchlistInteractor.addTvToWatchlist(tvId).test {
            awaitItem() shouldBe Outcome.Loading
            awaitItem() shouldBe Outcome.Success(WatchlistActionResult.Added)
            awaitComplete()
          }

          coVerify { getTvStateUseCase.getTvState(any(), any()) }
          coVerify {
            postActionUseCase.postWatchlistWithAuth(UpdateWatchlistParams(TV_MEDIA_TYPE, tvId, true))
          }
        }
      }

      And("TV show is already in watchlist") {
        val stateResponse = Outcome.Success(
          MediaState(
            id = tvId,
            favorite = false,
            rated = Rated.Unrated,
            watchlist = true
          )
        )

        coEvery { getTvStateUseCase.getTvState(any(), any()) } returns
          flowOf(stateResponse)

        Then("should emit Loading then AlreadyInWatchlist") {
          checkAndAddToWatchlistInteractor.addTvToWatchlist(tvId).test {
            awaitItem() shouldBe Outcome.Loading
            awaitItem() shouldBe Outcome.Success(WatchlistActionResult.AlreadyInWatchlist)
            awaitComplete()
          }

          coVerify { getTvStateUseCase.getTvState(any(), any()) }
          coVerify(exactly = 0) { postActionUseCase.postWatchlistWithAuth(any()) }
        }
      }

      And("getting TV state returns error") {
        val errorMessage = "Failed to get TV state"
        coEvery { getTvStateUseCase.getTvState(any(), any()) } returns
          flowOf(Outcome.Error(errorMessage))

        Then("should emit Loading then Error") {
          checkAndAddToWatchlistInteractor.addTvToWatchlist(tvId).test {
            awaitItem() shouldBe Outcome.Loading
            awaitItem() shouldBe Outcome.Error(errorMessage)
            awaitComplete()
          }

          coVerify { getTvStateUseCase.getTvState(any(), any()) }
          coVerify(exactly = 0) { postActionUseCase.postWatchlistWithAuth(any()) }
        }
      }

      And("getting TV state emits loading state") {
        val stateResponse = Outcome.Success(
          MediaState(
            id = tvId,
            favorite = false,
            rated = Rated.Unrated,
            watchlist = false
          )
        )

        coEvery { getTvStateUseCase.getTvState(any(), any()) } returns
          flow {
            emit(Outcome.Loading)
            emit(stateResponse)
          }
        coEvery { postActionUseCase.postWatchlistWithAuth(any()) } returns
          flowOf(Outcome.Success(PostFavoriteWatchlist(201, "Success")))

        Then("should emit all loading states and final result") {
          checkAndAddToWatchlistInteractor.addTvToWatchlist(tvId).test {
            awaitItem() shouldBe Outcome.Loading // Initial
            awaitItem() shouldBe Outcome.Loading // From getTvState
            awaitItem() shouldBe Outcome.Success(WatchlistActionResult.Added)
            awaitComplete()
          }
        }
      }

      And("posting to watchlist fails") {
        val stateResponse = Outcome.Success(
          MediaState(
            id = tvId,
            favorite = false,
            rated = Rated.Unrated,
            watchlist = false
          )
        )
        val errorMessage = "Failed to add to watchlist"

        coEvery { getTvStateUseCase.getTvState(any(), any()) } returns
          flowOf(stateResponse)
        coEvery { postActionUseCase.postWatchlistWithAuth(any()) } returns
          flowOf(Outcome.Error(errorMessage))

        Then("should emit Loading then Error") {
          checkAndAddToWatchlistInteractor.addTvToWatchlist(tvId).test {
            awaitItem() shouldBe Outcome.Loading
            awaitItem() shouldBe Outcome.Error(errorMessage)
            awaitComplete()
          }

          coVerify { getTvStateUseCase.getTvState(any(), any()) }
          coVerify { postActionUseCase.postWatchlistWithAuth(any()) }
        }
      }

      And("posting to watchlist emits loading state") {
        val stateResponse = Outcome.Success(
          MediaState(
            id = tvId,
            favorite = false,
            rated = Rated.Unrated,
            watchlist = false
          )
        )

        coEvery { getTvStateUseCase.getTvState(any(), any()) } returns
          flowOf(stateResponse)
        coEvery { postActionUseCase.postWatchlistWithAuth(any()) } returns
          flow {
            emit(Outcome.Loading)
            emit(Outcome.Success(PostFavoriteWatchlist(201, "Success")))
          }

        Then("should emit all loading states and final result") {
          checkAndAddToWatchlistInteractor.addTvToWatchlist(tvId).test {
            awaitItem() shouldBe Outcome.Loading // Initial
            awaitItem() shouldBe Outcome.Loading // From postWatchlist
            awaitItem() shouldBe Outcome.Success(WatchlistActionResult.Added)
            awaitComplete()
          }
        }
      }

      And("multiple emissions from post action") {
        val stateResponse = Outcome.Success(
          MediaState(
            id = tvId,
            favorite = false,
            rated = Rated.Unrated,
            watchlist = false
          )
        )

        coEvery { getTvStateUseCase.getTvState(any(), any()) } returns
          flowOf(stateResponse)
        coEvery { postActionUseCase.postWatchlistWithAuth(any()) } returns
          flow {
            emit(Outcome.Loading)
            emit(Outcome.Loading)
            emit(Outcome.Success(PostFavoriteWatchlist(201, "Success")))
          }

        Then("should emit all states in order") {
          checkAndAddToWatchlistInteractor.addTvToWatchlist(tvId).test {
            awaitItem() shouldBe Outcome.Loading
            awaitItem() shouldBe Outcome.Loading
            awaitItem() shouldBe Outcome.Loading
            awaitItem() shouldBe Outcome.Success(WatchlistActionResult.Added)
            awaitComplete()
          }
        }
      }
    }
  }
})
