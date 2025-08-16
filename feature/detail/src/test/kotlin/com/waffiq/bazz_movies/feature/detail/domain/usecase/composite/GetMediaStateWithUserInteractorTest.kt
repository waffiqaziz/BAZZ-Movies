package com.waffiq.bazz_movies.feature.detail.domain.usecase.composite

import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.feature.detail.testutils.BaseInteractorTest
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.MOVIE_ID
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.SESSION_ID
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.TV_ID
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.movieMediaState
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.tvMediaState
import io.mockk.every
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.test.assertIs

class GetMediaStateWithUserInteractorTest : BaseInteractorTest() {

  private lateinit var interactor: GetMediaStateWithUserInteractor

  override fun initInteractor() {
    interactor = GetMediaStateWithUserInteractor(
      mockGetMovieStateUseCase,
      mockGetTvStateUseCase,
      mockUserPrefUseCase
    )
  }

  @Before
  override fun baseSetUp() {
    super.baseSetUp()
    every { mockUserPrefUseCase.getUserToken() } returns flowOf(SESSION_ID)
  }

  @Test
  fun getMovieStateWithUser_whenSuccessful_emitsSuccess() = runTest {
    testSuccessScenario(
      mockCall = { mockGetMovieStateUseCase.getMovieState(SESSION_ID, MOVIE_ID) },
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
      mockCall = { mockGetMovieStateUseCase.getMovieState(SESSION_ID, MOVIE_ID) },
      interactorCall = { interactor.getMovieStateWithUser(MOVIE_ID) }
    )
  }

  @Test
  fun getMovieStateWithUser_whenLoading_emitsLoading() = runTest {
    testLoadingScenario(
      mockCall = { mockGetMovieStateUseCase.getMovieState(SESSION_ID, MOVIE_ID) },
      interactorCall = { interactor.getMovieStateWithUser(MOVIE_ID) }
    )
  }

  @Test
  fun getTvStateWithUser_whenSuccessful_emitsSuccess() = runTest {
    testSuccessScenario(
      mockCall = { mockGetTvStateUseCase.getTvState(SESSION_ID, TV_ID) },
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
      mockCall = { mockGetTvStateUseCase.getTvState(SESSION_ID, TV_ID) },
      interactorCall = { interactor.getTvStateWithUser(TV_ID) }
    )
  }

  @Test
  fun getTvStateWithUser_whenLoading_emitsLoading() = runTest {
    testLoadingScenario(
      mockCall = { mockGetTvStateUseCase.getTvState(SESSION_ID, TV_ID) },
      interactorCall = { interactor.getTvStateWithUser(TV_ID) }
    )
  }
}
