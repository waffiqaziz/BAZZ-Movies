package com.waffiq.bazz_movies.feature.detail.domain.usecase.getOmdbDetail

import app.cash.turbine.test
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvExternalIds
import com.waffiq.bazz_movies.feature.detail.testutils.BaseInteractorTest
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.IMDB_ID
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.omdbDetails
import io.mockk.every
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.test.assertIs

class GetDetailOMDbInteractorTest : BaseInteractorTest() {

  private val imdbId = "tt123456"
  private val tmdbId = 123456
  private val oMDbDetails = OMDbDetails(imdbID = imdbId, imdbRating = "99")
  private val tvExternalIds = TvExternalIds(imdbId = imdbId)

  private lateinit var interactor: GetOMDbDetailInteractor

  override fun initInteractor() {
    interactor = GetOMDbDetailInteractor(mockDetailRepository)
    every { mockDetailRepository.getTvExternalIds(tmdbId) } returns
      flowOf(Outcome.Success(tvExternalIds))
  }

  @Test
  fun getDetailOMDb_whenSuccessful_emitsSuccess() = runTest {
    testSuccessScenario(
      mockCall = { mockDetailRepository.getOMDbDetails(IMDB_ID) },
      mockResponse = omdbDetails,
      interactorCall = { interactor.getOMDbDetails(IMDB_ID) }
    ) { emission ->
      val details = emission.data
      assertEquals("Some Movie", details.title)
      assertEquals(IMDB_ID, details.imdbID)
      assertEquals("This is the plot.", details.plot)
      assertEquals("Action, Adventure", details.genre)
      assertEquals("Jane Doe", details.director)
      assertEquals("John Smith", details.writer)
      assertEquals("Actor A, Actor B", details.actors)
      assertEquals("2023-01-01", details.released)
      assertEquals("English", details.language)
      assertEquals("USA", details.country)
      assertEquals("3 wins", details.awards)
      assertEquals("https://poster.url", details.poster)
      assertEquals("65", details.metascore)
      assertEquals("7.8", details.imdbRating)
      assertEquals("120,000", details.imdbVotes)
      assertEquals("$100,000,000", details.boxOffice)
      assertEquals("https://example.com", details.website)
    }
  }

  @Test
  fun getDetailOMDb_whenUnsuccessful_emitsError() = runTest {
    testErrorScenario(
      mockCall = { mockDetailRepository.getOMDbDetails(IMDB_ID) },
      interactorCall = { interactor.getOMDbDetails(IMDB_ID) }
    )
  }

  @Test
  fun getOMDbDetails_whenLoading_emitsLoading() = runTest {
    testLoadingScenario(
      mockCall = { mockDetailRepository.getOMDbDetails(IMDB_ID) },
      interactorCall = { interactor.getOMDbDetails(IMDB_ID) }
    )
  }

  @Test
  fun getTvAllScore_whenSuccessful_emitsSuccess() = runTest {
    testSuccessScenario(
      mockCall = { mockDetailRepository.getOMDbDetails(imdbId) },
      mockResponse = oMDbDetails,
      interactorCall = { interactor.getTvAllScore(tmdbId) }
    ) { emission ->
      val result = assertIs<OMDbDetails>(emission.data)
      assertEquals("99", result.imdbRating)
    }
  }

  @Test
  fun getTvAllScore_whenImdbEmpty_emitsError() = runTest {
    every { mockDetailRepository.getTvExternalIds(tmdbId) } returns
      flowOf(Outcome.Success(tvExternalIds.copy(imdbId = "")))

    interactor.getTvAllScore(tmdbId).test {
      assertTrue(awaitItem() is Outcome.Error)
      awaitComplete()
    }
  }

  @Test
  fun getTvAllScore_whenImdbNull_emitsError() = runTest {
    every { mockDetailRepository.getTvExternalIds(tmdbId) } returns
      flowOf(Outcome.Success(tvExternalIds.copy(imdbId = null)))

    interactor.getTvAllScore(tmdbId).test {
      assertTrue(awaitItem() is Outcome.Error)
      awaitComplete()
    }
  }

  @Test
  fun getTvAllScore_whenUnsuccessful_emitsError() = runTest {
    testErrorScenario(
      mockCall = { mockDetailRepository.getOMDbDetails(imdbId) },
      interactorCall = { interactor.getTvAllScore(tmdbId) }
    )
  }

  @Test
  fun getTvAllScore_whenLoading_emitsLoading() = runTest {
    testLoadingScenario(
      mockCall = { mockDetailRepository.getOMDbDetails(imdbId) },
      interactorCall = { interactor.getTvAllScore(tmdbId) }
    )
  }
}
