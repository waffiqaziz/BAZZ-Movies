package com.waffiq.bazz_movies.core.movie.domain.usecase.getstated

import app.cash.turbine.test
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.Rated
import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.core.movie.domain.repository.IMoviesRepository
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetStatedMovieInteractorTest {
  private val mockRepository: IMoviesRepository = mockk()
  private lateinit var getStatedMovieInteractor: GetStatedMovieInteractor

  @Before
  fun setup() {
    getStatedMovieInteractor = GetStatedMovieInteractor(mockRepository)
  }

  @Test
  fun getStatedIMovieRepository_whenSuccessful_returnsCorrectData() = runTest {
    val stated = MediaState(
      id = 1234,
      favorite = true,
      rated = Rated.Unrated,
      watchlist = false
    )

    coEvery { mockRepository.getStatedMovie("sessionId", 1234) } returns
      flowOf(Outcome.Success(stated))

    getStatedMovieInteractor.getMovieState("sessionId", 1234).test {
      val result = awaitItem()
      assertTrue(result is Outcome.Success)
      result as Outcome.Success
      assertEquals(1234, result.data.id)
      assertTrue(result.data.favorite)
      assertEquals(Rated.Unrated, result.data.rated)
      assertFalse(result.data.watchlist)
      awaitComplete()
    }
  }
}
