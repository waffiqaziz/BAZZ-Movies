package com.waffiq.bazz_movies.feature.favorite.domain.usecase.composite

import app.cash.turbine.test
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.Rated
import com.waffiq.bazz_movies.core.domain.WatchlistParams
import com.waffiq.bazz_movies.core.data.domain.model.post.PostFavoriteWatchlist
import com.waffiq.bazz_movies.core.data.domain.usecase.composite.MediaStateUseCase
import com.waffiq.bazz_movies.core.data.domain.usecase.composite.PostActionUseCase
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

  val mockMediaStateUseCase: MediaStateUseCase = mockk()
  val mockPostActionUseCase: PostActionUseCase = mockk()

  lateinit var checkAndAddToWatchlistInteractor: CheckAndAddToWatchlistInteractor

  val movieId = 1234
  val tvId = 5678

  beforeTest {
    Dispatchers.setMain(UnconfinedTestDispatcher())

    checkAndAddToWatchlistInteractor = CheckAndAddToWatchlistInteractor(
      mockMediaStateUseCase,
      mockPostActionUseCase,
    )

  }

  afterTest {
    clearMocks(
      mockMediaStateUseCase,
      mockPostActionUseCase,
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

        coEvery { mockMediaStateUseCase.getMovieStateWithUser(any()) } returns flowOf(stateResponse)
        coEvery { mockPostActionUseCase.postWatchlistWithAuth(any()) } returns
          flowOf(postResponse)

        Then("should emit Loading then Added") {
          checkAndAddToWatchlistInteractor.addMovieToWatchlist(movieId).test {
            awaitItem() shouldBe Outcome.Loading
            awaitItem() shouldBe Outcome.Success(WatchlistActionResult.Added)
            awaitComplete()
          }

          coVerify { mockMediaStateUseCase.getMovieStateWithUser(any()) }
          coVerify {
            mockPostActionUseCase.postWatchlistWithAuth(
              WatchlistParams(MOVIE_MEDIA_TYPE, movieId, true)
            )
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

        coEvery { mockMediaStateUseCase.getMovieStateWithUser(any()) } returns
          flowOf(stateResponse)

        Then("should emit Loading then AlreadyInWatchlist") {
          checkAndAddToWatchlistInteractor.addMovieToWatchlist(movieId).test {
            awaitItem() shouldBe Outcome.Loading
            awaitItem() shouldBe Outcome.Success(WatchlistActionResult.AlreadyInWatchlist)
            awaitComplete()
          }

          coVerify { mockMediaStateUseCase.getMovieStateWithUser(any()) }
          coVerify(exactly = 0) { mockPostActionUseCase.postWatchlistWithAuth(any()) } // this cause error
        }
      }

      And("getting movie state returns error") {
        val errorMessage = "Failed to get movie state"
        coEvery { mockMediaStateUseCase.getMovieStateWithUser(any()) } returns
          flowOf(Outcome.Error(errorMessage))

        Then("should emit Loading then Error") {
          checkAndAddToWatchlistInteractor.addMovieToWatchlist(movieId).test {
            awaitItem() shouldBe Outcome.Loading
            awaitItem() shouldBe Outcome.Error(errorMessage)
            cancelAndIgnoreRemainingEvents()
          }

          coVerify { mockMediaStateUseCase.getMovieStateWithUser(any()) }
          coVerify(exactly = 0) { mockPostActionUseCase.postWatchlistWithAuth(any()) } // this error its called
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

        coEvery { mockMediaStateUseCase.getMovieStateWithUser(any()) } returns
          flow {
            emit(Outcome.Loading)
            emit(stateResponse)
          }
        coEvery { mockPostActionUseCase.postWatchlistWithAuth(any()) } returns
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

        coEvery { mockMediaStateUseCase.getMovieStateWithUser(any()) } returns
          flowOf(stateResponse)
        coEvery { mockPostActionUseCase.postWatchlistWithAuth(any()) } returns
          flowOf(Outcome.Error(errorMessage))

        Then("should emit Loading then Error") {
          checkAndAddToWatchlistInteractor.addMovieToWatchlist(movieId).test {
            awaitItem() shouldBe Outcome.Loading
            awaitItem() shouldBe Outcome.Error(errorMessage)
            awaitComplete()
          }

          coVerify { mockMediaStateUseCase.getMovieStateWithUser(any()) }
          coVerify { mockPostActionUseCase.postWatchlistWithAuth(any()) }
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

        coEvery { mockMediaStateUseCase.getMovieStateWithUser(any()) } returns
          flowOf(stateResponse)
        coEvery { mockPostActionUseCase.postWatchlistWithAuth(any()) } returns
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

        coEvery { mockMediaStateUseCase.getMovieStateWithUser(any()) } returns
          flowOf(stateResponse)
        coEvery { mockPostActionUseCase.postWatchlistWithAuth(any()) } returns
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

        coEvery { mockMediaStateUseCase.getTvStateWithUser(any()) } returns
          flowOf(stateResponse)
        coEvery { mockPostActionUseCase.postWatchlistWithAuth(any()) } returns
          flowOf(postResponse)

        Then("should emit Loading then Added") {
          checkAndAddToWatchlistInteractor.addTvToWatchlist(tvId).test {
            awaitItem() shouldBe Outcome.Loading
            awaitItem() shouldBe Outcome.Success(WatchlistActionResult.Added)
            awaitComplete()
          }

          coVerify { mockMediaStateUseCase.getTvStateWithUser(any()) }
          coVerify {
            mockPostActionUseCase.postWatchlistWithAuth(WatchlistParams(TV_MEDIA_TYPE, tvId, true))
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

        coEvery { mockMediaStateUseCase.getTvStateWithUser(any()) } returns
          flowOf(stateResponse)

        Then("should emit Loading then AlreadyInWatchlist") {
          checkAndAddToWatchlistInteractor.addTvToWatchlist(tvId).test {
            awaitItem() shouldBe Outcome.Loading
            awaitItem() shouldBe Outcome.Success(WatchlistActionResult.AlreadyInWatchlist)
            awaitComplete()
          }

          coVerify { mockMediaStateUseCase.getTvStateWithUser(any()) }
          coVerify(exactly = 0) { mockPostActionUseCase.postWatchlistWithAuth(any()) }
        }
      }

      And("getting TV state returns error") {
        val errorMessage = "Failed to get TV state"
        coEvery { mockMediaStateUseCase.getTvStateWithUser(any()) } returns
          flowOf(Outcome.Error(errorMessage))

        Then("should emit Loading then Error") {
          checkAndAddToWatchlistInteractor.addTvToWatchlist(tvId).test {
            awaitItem() shouldBe Outcome.Loading
            awaitItem() shouldBe Outcome.Error(errorMessage)
            awaitComplete()
          }

          coVerify { mockMediaStateUseCase.getTvStateWithUser(any()) }
          coVerify(exactly = 0) { mockPostActionUseCase.postWatchlistWithAuth(any()) }
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

        coEvery { mockMediaStateUseCase.getTvStateWithUser(any()) } returns
          flow {
            emit(Outcome.Loading)
            emit(stateResponse)
          }
        coEvery { mockPostActionUseCase.postWatchlistWithAuth(any()) } returns
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

        coEvery { mockMediaStateUseCase.getTvStateWithUser(any()) } returns
          flowOf(stateResponse)
        coEvery { mockPostActionUseCase.postWatchlistWithAuth(any()) } returns
          flowOf(Outcome.Error(errorMessage))

        Then("should emit Loading then Error") {
          checkAndAddToWatchlistInteractor.addTvToWatchlist(tvId).test {
            awaitItem() shouldBe Outcome.Loading
            awaitItem() shouldBe Outcome.Error(errorMessage)
            awaitComplete()
          }

          coVerify { mockMediaStateUseCase.getTvStateWithUser(any()) }
          coVerify { mockPostActionUseCase.postWatchlistWithAuth(any()) }
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

        coEvery { mockMediaStateUseCase.getTvStateWithUser(any()) } returns
          flowOf(stateResponse)
        coEvery { mockPostActionUseCase.postWatchlistWithAuth(any()) } returns
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

        coEvery { mockMediaStateUseCase.getTvStateWithUser(any()) } returns
          flowOf(stateResponse)
        coEvery { mockPostActionUseCase.postWatchlistWithAuth(any()) } returns
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
