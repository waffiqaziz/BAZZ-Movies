package com.waffiq.bazz_movies.core.data.domain.usecase.composite

import com.waffiq.bazz_movies.core.data.testutils.BaseInteractorTest
import com.waffiq.bazz_movies.core.data.testutils.TestVariables.MOVIE_ID
import com.waffiq.bazz_movies.core.data.testutils.TestVariables.SESSION_ID
import com.waffiq.bazz_movies.core.data.testutils.TestVariables.TV_ID
import com.waffiq.bazz_movies.core.data.testutils.TestVariables.movieMediaState
import com.waffiq.bazz_movies.core.data.testutils.TestVariables.tvMediaState
import com.waffiq.bazz_movies.core.domain.MediaState
import io.mockk.every
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.test.assertIs

class MediaStateInteractorTest : BaseInteractorTest() {

  private lateinit var interactor: MediaStateInteractor

  @Before
  override fun setup() {
    interactor = MediaStateInteractor(mockMoviesRepository, mockTvRepository, mockUserRepository)
    every { mockUserRepository.getUserToken() } returns flowOf(SESSION_ID)
  }

  @Test
  fun getMovieStateWithUser_whenSuccessful_emitsSuccess() = runTest {
    testSuccessScenario(
      mockCall = { mockMoviesRepository.getMovieState(SESSION_ID, MOVIE_ID) },
      mockResponse = movieMediaState,
      interactorCall = { interactor.getMovieStateWithUser(MOVIE_ID) }
    ) { emission ->
      val mediaState = assertIs<MediaState>(emission.data)
      assertEquals(MOVIE_ID, mediaState.id)
    }
  }

  @Test
  fun getMovieStateWithUser_whenUnsuccessful_emitsError() = runTest {
    testErrorScenario(
      mockCall = { mockMoviesRepository.getMovieState(SESSION_ID, MOVIE_ID) },
      interactorCall = { interactor.getMovieStateWithUser(MOVIE_ID) }
    )
  }

  @Test
  fun getMovieStateWithUser_whenLoading_emitsLoading() = runTest {
    testLoadingScenario(
      mockCall = { mockMoviesRepository.getMovieState(SESSION_ID, MOVIE_ID) },
      interactorCall = { interactor.getMovieStateWithUser(MOVIE_ID) }
    )
  }

  @Test
  fun getTvStateWithUser_whenSuccessful_emitsSuccess() = runTest {
    testSuccessScenario(
      mockCall = { mockTvRepository.getTvState(SESSION_ID, TV_ID) },
      mockResponse = tvMediaState,
      interactorCall = { interactor.getTvStateWithUser(TV_ID) }
    ) { emission ->
      val mediaState = assertIs<MediaState>(emission.data)
      assertEquals(TV_ID, mediaState.id)
    }
  }

  @Test
  fun getTvStateWithUser_whenUnsuccessful_emitsError() = runTest {
    testErrorScenario(
      mockCall = { mockTvRepository.getTvState(SESSION_ID, TV_ID) },
      interactorCall = { interactor.getTvStateWithUser(TV_ID) }
    )
  }

  @Test
  fun getTvStateWithUser_whenLoading_emitsLoading() = runTest {
    testLoadingScenario(
      mockCall = { mockTvRepository.getTvState(SESSION_ID, TV_ID) },
      interactorCall = { interactor.getTvStateWithUser(TV_ID) }
    )
  }
}
