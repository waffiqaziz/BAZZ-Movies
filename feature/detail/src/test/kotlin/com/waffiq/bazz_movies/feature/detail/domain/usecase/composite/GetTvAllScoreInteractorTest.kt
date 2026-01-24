package com.waffiq.bazz_movies.feature.detail.domain.usecase.composite

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvExternalIds
import com.waffiq.bazz_movies.feature.detail.testutils.BaseInteractorTest
import io.mockk.every
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.test.assertIs

class GetTvAllScoreInteractorTest : BaseInteractorTest() {

  private lateinit var interactor: GetTvAllScoreInteractor
  private val imdbId = "tt123456"
  private val tmdbId = 123456
  private val oMDbDetails = OMDbDetails(imdbID = imdbId, imdbRating = "99")
  private val tvExternalIds = TvExternalIds(imdbId = imdbId)

  override fun initInteractor() {
    interactor = GetTvAllScoreInteractor(
      mockGetTvDetailUseCase,
      mockGetOMDbDetailUseCase,
    )
  }

  @Before
  override fun baseSetUp() {
    super.baseSetUp()
    every { mockGetTvDetailUseCase.getTvExternalIds(tmdbId) } returns
      flowOf(Outcome.Success(tvExternalIds))
  }

  @Test
  fun getTvAllScore_whenSuccessful_emitsSuccess() = runTest {
    testSuccessScenario(
      mockCall = { mockGetOMDbDetailUseCase.getOMDbDetails(imdbId) },
      mockResponse = oMDbDetails,
      interactorCall = { interactor.getTvAllScore(tmdbId) }
    ) { emission ->
      val result = assertIs<OMDbDetails>(emission.data)
      assertEquals("99", result.imdbRating)
    }
  }

  @Test
  fun getTvAllScore_whenUnsuccessful_emitsError() = runTest {
    testErrorScenario(
      mockCall = { mockGetOMDbDetailUseCase.getOMDbDetails(imdbId) },
      interactorCall = { interactor.getTvAllScore(tmdbId) }
    )
  }

  @Test
  fun getTvAllScore_whenLoading_emitsLoading() = runTest {
    testLoadingScenario(
      mockCall = { mockGetOMDbDetailUseCase.getOMDbDetails(imdbId) },
      interactorCall = { interactor.getTvAllScore(tmdbId) }
    )
  }
}
