package com.waffiq.bazz_movies.feature.detail.domain.usecase.getOmdbDetail

import app.cash.turbine.test
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.feature.detail.testutils.BaseInteractorTest
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.ERROR_MESSAGE
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.IMDB_ID
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.omdbDetails
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetDetailOMDbInteractorTest : BaseInteractorTest() {

  private lateinit var interactor: GetOMDbDetailInteractor

  override fun initInteractor() {
    interactor = GetOMDbDetailInteractor(mockRepository)
  }

  @Test
  fun getDetailOMDb_whenSuccessful_emitsSuccess() = runTest {
    val flow = flowOf(Outcome.Success(omdbDetails))
    coEvery { mockRepository.getOMDbDetails(IMDB_ID) } returns flow

    interactor.getOMDbDetails(IMDB_ID).test {
      val emission = awaitItem()
      assertTrue(emission is Outcome.Success)
      emission as Outcome.Success

      assertEquals("Some Movie", emission.data.title)
      assertEquals(IMDB_ID, emission.data.imdbID)
      assertEquals("This is the plot.", emission.data.plot)
      assertEquals("Action, Adventure", emission.data.genre)
      assertEquals("Jane Doe", emission.data.director)
      assertEquals("John Smith", emission.data.writer)
      assertEquals("Actor A, Actor B", emission.data.actors)
      assertEquals("2023-01-01", emission.data.released)
      assertEquals("English", emission.data.language)
      assertEquals("USA", emission.data.country)
      assertEquals("3 wins", emission.data.awards)
      assertEquals("https://poster.url", emission.data.poster)
      assertEquals("65", emission.data.metascore)
      assertEquals("7.8", emission.data.imdbRating)
      assertEquals("120,000", emission.data.imdbVotes)
      assertEquals("$100,000,000", emission.data.boxOffice)
      assertEquals("https://example.com", emission.data.website)

      awaitComplete()
    }

    coVerify { mockRepository.getOMDbDetails(IMDB_ID) }
  }

  @Test
  fun getDetailOMDb_whenUnsuccessful_emitsError() = runTest {
    val flow = flowOf(Outcome.Error(ERROR_MESSAGE))
    coEvery { mockRepository.getOMDbDetails(IMDB_ID) } returns flow

    interactor.getOMDbDetails(IMDB_ID).test {
      val emission = awaitItem()
      assertTrue(emission is Outcome.Error)
      emission as Outcome.Error
      assertEquals(ERROR_MESSAGE, emission.message)
      awaitComplete()
    }

    coVerify { mockRepository.getOMDbDetails(IMDB_ID) }
  }

  @Test
  fun getOMDbDetails_whenLoading_emitsLoading() = runTest {
    val flow = flowOf(Outcome.Loading)
    coEvery { mockRepository.getOMDbDetails(IMDB_ID) } returns flow

    interactor.getOMDbDetails(IMDB_ID).test {
      assertTrue(awaitItem() is Outcome.Loading)
      awaitComplete()
    }

    coVerify { mockRepository.getOMDbDetails(IMDB_ID) }
  }
}
