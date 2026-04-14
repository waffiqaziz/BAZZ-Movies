package com.waffiq.bazz_movies.feature.detail.domain.usecase.composite

import com.waffiq.bazz_movies.core.domain.PostResult
import com.waffiq.bazz_movies.feature.detail.testutils.BaseInteractorTest
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.MOVIE_ID
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.SESSION_ID
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.TV_ID
import io.mockk.every
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.test.assertIs

class PostRateInteractorTest : BaseInteractorTest() {

  private val rate = 9.0f
  private lateinit var interactor: PostRateInteractor

  val post = PostResult(
    success = true,
    statusCode = 201,
    statusMessage = "Success"
  )

  override fun initInteractor() {
    interactor = PostRateInteractor(mockMoviesRepository, mockTvRepository, mockUserRepository)
  }

  @Before
  override fun baseSetUp() {
    super.baseSetUp()
    every { mockUserRepository.getUserToken() } returns flowOf(SESSION_ID)
  }

  @Test
  fun postMovieRate_whenSuccessful_emitsSuccess() = runTest {
    testSuccessScenario(
      mockCall = { mockMoviesRepository.postMovieRate(SESSION_ID, rate, MOVIE_ID) },
      mockResponse = post,
      interactorCall = { interactor.postMovieRate(rate, MOVIE_ID) }
    ) { emission ->
      val result = assertIs<PostResult>(emission.data)
      assertEquals(201, result.statusCode)
    }
  }

  @Test
  fun postMovieRate_whenUnsuccessful_emitsError() = runTest {
    testErrorScenario(
      mockCall = { mockMoviesRepository.postMovieRate(SESSION_ID, rate, MOVIE_ID) },
      interactorCall = { interactor.postMovieRate(rate, MOVIE_ID) }
    )
  }

  @Test
  fun postMovieRate_whenLoading_emitsLoading() = runTest {
    testLoadingScenario(
      mockCall = { mockMoviesRepository.postMovieRate(SESSION_ID, rate, MOVIE_ID) },
      interactorCall = { interactor.postMovieRate(rate, MOVIE_ID) }
    )
  }

  @Test
  fun postTvRate_whenSuccessful_emitsSuccess() = runTest {
    testSuccessScenario(
      mockCall = { mockTvRepository.postTvRate(SESSION_ID, rate, TV_ID) },
      mockResponse = post,
      interactorCall = { interactor.postTvRate(rate, TV_ID) }
    ) { emission ->
      val result = assertIs<PostResult>(emission.data)
      assertEquals(201, result.statusCode)
    }
  }

  @Test
  fun postTvRate_whenUnsuccessful_emitsError() = runTest {
    testErrorScenario(
      mockCall = { mockTvRepository.postTvRate(SESSION_ID, rate, TV_ID) },
      interactorCall = { interactor.postTvRate(rate, TV_ID) }
    )
  }

  @Test
  fun postTvRate_whenLoading_emitsLoading() = runTest {
    testLoadingScenario(
      mockCall = { mockTvRepository.postTvRate(SESSION_ID, rate, TV_ID) },
      interactorCall = { interactor.postTvRate(rate, TV_ID) }
    )
  }
}
