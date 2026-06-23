package com.waffiq.bazz_movies.core.network.data.remote.datasource.movie

import com.waffiq.bazz_movies.core.network.data.remote.pagingsources.GenericPagingSource
import com.waffiq.bazz_movies.core.network.testutils.BaseMediaDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.movieDump1
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.movieDump2
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.movieDump3
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.movieDump4
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.movieDump5
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.defaultMediaResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testPagingFlow
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testPagingSource
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class MovieRemoteDataSourceTest : BaseMediaDataSourceTest() {

  @Test
  fun getTopRatedMovies_pagingSource_returnsExpectedData() =
    runTest {
      val pagingSource = GenericPagingSource { mockMovieApiService.getTopRatedMovies(1).results }

      testPagingSource(
        mockResults = defaultMediaResponse(listOf(movieDump1, movieDump2)),
        mockApiCall = { mockMovieApiService.getTopRatedMovies(1) },
        loader = { pagingSource.toLoadResult() },
      ) { page ->
        assertEquals(2, page.data.size)
      }
    }

  @Test
  fun getTopRatedMovies_pagingFlow_returnsExpectedData() =
    runTest {
      val expected = listOf(movieDump1, movieDump2)
      coEvery { mockMovieApiService.getTopRatedMovies(1) } returns
        defaultMediaResponse(expected)

      movieRemoteDataSource.getTopRatedMovies().testPagingFlow(this, expected)

      coVerify { mockMovieApiService.getTopRatedMovies(1) }
    }

  @Test
  fun getPopularMovies_pagingSource_returnsExpectedData() =
    runTest {
      val pagingSource = GenericPagingSource { mockMovieApiService.getPopularMovies(1).results }

      testPagingSource(
        mockResults = defaultMediaResponse(listOf(movieDump5)),
        mockApiCall = { mockMovieApiService.getPopularMovies(1) },
        loader = { pagingSource.toLoadResult() },
      ) { page ->
        assertEquals(1, page.data.size)
      }
    }

  @Test
  fun getPopularMovies_pagingFlow_returnsExpectedData() =
    runTest {
      val expected = listOf(movieDump5)
      coEvery { mockMovieApiService.getPopularMovies(1) } returns
        defaultMediaResponse(expected)

      movieRemoteDataSource.getPopularMovies().testPagingFlow(this, expected)

      coVerify { mockMovieApiService.getPopularMovies(1) }
    }

  @Test
  fun getMovieRecommendations_pagingSource_returnsExpectedData() =
    runTest {
      val pagingSource =
        GenericPagingSource { mockMovieApiService.getMovieRecommendations(12345678, 1).results }

      testPagingSource(
        mockResults = defaultMediaResponse(listOf(movieDump3)),
        mockApiCall = { mockMovieApiService.getMovieRecommendations(12345678, 1) },
        loader = { pagingSource.toLoadResult() },
      ) { page ->
        assertEquals(1, page.data.size)
      }
    }

  @Test
  fun getMovieRecommendations_pagingFlow_returnsExpectedData() =
    runTest {
      val expected = listOf(movieDump3)
      coEvery { mockMovieApiService.getMovieRecommendations(12345678, 1) } returns
        defaultMediaResponse(expected)

      movieRemoteDataSource.getMovieRecommendation(12345678).testPagingFlow(this, expected)

      coVerify { mockMovieApiService.getMovieRecommendations(12345678, 1) }
    }

  @Test
  fun getUpcomingMovies_pagingSource_returnsExpectedData() =
    runTest {
      val pagingSource =
        GenericPagingSource { mockMovieApiService.getUpcomingMovies("cn", 1).results }

      testPagingSource(
        mockResults = defaultMediaResponse(listOf(movieDump4)),
        mockApiCall = { mockMovieApiService.getUpcomingMovies("cn", 1) },
        loader = { pagingSource.toLoadResult() },
      ) { page ->
        assertEquals(1, page.data.size)
      }
    }

  @Test
  fun getUpcomingMovies_pagingFlow_returnsExpectedData() =
    runTest {
      val expected = listOf(movieDump4)
      coEvery { mockMovieApiService.getUpcomingMovies("cn", 1) } returns
        defaultMediaResponse(expected)

      movieRemoteDataSource.getUpcomingMovies("cn").testPagingFlow(this, expected)

      coVerify { mockMovieApiService.getUpcomingMovies("cn", 1) }
    }

  @Test
  fun getPlayingNowMovies_pagingSource_returnsExpectedData() =
    runTest {
      val pagingSource =
        GenericPagingSource { mockMovieApiService.getNowPlayingMovies("gb", 1).results }

      testPagingSource(
        mockResults = defaultMediaResponse(listOf(movieDump5)),
        mockApiCall = { mockMovieApiService.getNowPlayingMovies("gb", 1) },
        loader = { pagingSource.toLoadResult() },
      ) { page ->
        assertEquals(1, page.data.size)
      }
    }

  @Test
  fun getPlayingNowMovies_pagingFlow_returnsExpectedData() =
    runTest {
      val expected = listOf(movieDump5)
      coEvery { mockMovieApiService.getNowPlayingMovies("gb", 1) } returns
        defaultMediaResponse(expected)

      movieRemoteDataSource.getPlayingNowMovies("gb").testPagingFlow(this, expected)

      coVerify { mockMovieApiService.getNowPlayingMovies("gb", 1) }
    }
}
