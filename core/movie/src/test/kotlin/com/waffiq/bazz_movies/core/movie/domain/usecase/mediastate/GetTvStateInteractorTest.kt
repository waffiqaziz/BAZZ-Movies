package com.waffiq.bazz_movies.core.movie.domain.usecase.mediastate

import app.cash.turbine.test
import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.Rated
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

class GetTvStateInteractorTest {
  private val mockRepository: IMoviesRepository = mockk()
  private lateinit var getTvStateInteractor: GetTvStateInteractor

  @Before
  fun setup() {
    getTvStateInteractor = GetTvStateInteractor(mockRepository)
  }

  @Test
  fun getTvState_whenSuccessful_returnsCorrectData() = runTest {
    val stated = MediaState(
      id = 6789,
      favorite = false,
      rated = Rated.Value(7.0),
      watchlist = true
    )

    coEvery { mockRepository.getTvState("sessionId", 6789) } returns
      flowOf(Outcome.Success(stated))

    getTvStateInteractor.getTvState("sessionId", 6789).test {
      val result = awaitItem()
      assertTrue(result is Outcome.Success)
      result as Outcome.Success
      assertEquals(6789, result.data.id)
      assertFalse(result.data.favorite)
      assertEquals(Rated.Value(7.0), result.data.rated)
      assertTrue(result.data.watchlist)
      awaitComplete()
    }
  }
}
