package com.waffiq.bazz_movies.feature.detail.domain.usecase.getOmdbDetail

import com.waffiq.bazz_movies.feature.detail.testutils.BaseInteractorTest
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.IMDB_ID
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.omdbDetails
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetDetailOMDbInteractorTest : BaseInteractorTest() {

  private lateinit var interactor: GetOMDbDetailInteractor

  override fun initInteractor() {
    interactor = GetOMDbDetailInteractor(mockRepository)
  }

  @Test
  fun getDetailOMDb_whenSuccessful_emitsSuccess() = runTest {
    testSuccessScenario(
      mockCall = { mockRepository.getOMDbDetails(IMDB_ID) },
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
      mockCall = { mockRepository.getOMDbDetails(IMDB_ID) },
      interactorCall = { interactor.getOMDbDetails(IMDB_ID) }
    )
  }

  @Test
  fun getOMDbDetails_whenLoading_emitsLoading() = runTest {
    testLoadingScenario(
      mockCall = { mockRepository.getOMDbDetails(IMDB_ID) },
      interactorCall = { interactor.getOMDbDetails(IMDB_ID) }
    )
  }
}
