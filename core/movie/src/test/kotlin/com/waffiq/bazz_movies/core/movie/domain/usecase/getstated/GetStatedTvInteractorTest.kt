package com.waffiq.bazz_movies.core.movie.domain.usecase.getstated

import app.cash.turbine.test
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.Rated
import com.waffiq.bazz_movies.core.domain.Stated
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

class GetStatedTvInteractorTest {
  private val mockRepository: IMoviesRepository = mockk()
  private lateinit var getStatedTvInteractor: GetStatedTvInteractor

  @Before
  fun setup() {
    getStatedTvInteractor = GetStatedTvInteractor(mockRepository)
  }

  @Test
  fun getStatedITvRepositorySuccess_returnCorrectData() = runTest {
    val stated = Stated(
      id = 6789,
      favorite = false,
      rated = Rated.Value(7.0),
      watchlist = true
    )

    coEvery { mockRepository.getStatedTv("sessionId", 6789) } returns
      flowOf(Outcome.Success(stated))

    getStatedTvInteractor.getStatedTv("sessionId", 6789).test {
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
