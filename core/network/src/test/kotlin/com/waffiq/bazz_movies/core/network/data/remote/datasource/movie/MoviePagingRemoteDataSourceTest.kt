package com.waffiq.bazz_movies.core.network.data.remote.datasource.movie

import com.waffiq.bazz_movies.core.network.data.remote.pagingsources.GenericPagingSource
import com.waffiq.bazz_movies.core.network.testutils.BaseMediaDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager
import com.waffiq.bazz_movies.core.network.testutils.TestHelper
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testPagingFlow
import io.mockk.coEvery
import io.mockk.coVerify
import junit.framework.TestCase
import kotlinx.coroutines.test.runTest
import org.junit.Test

class MoviePagingRemoteDataSourceTest : BaseMediaDataSourceTest() {

  @Test
  fun getTopRatedMovies_pagingSource_returnsExpectedData() =
    runTest {
      val pagingSource = GenericPagingSource { mockMovieApiService.getTopRatedMovies(1).results }
      TestHelper.testPagingSource(
        mockResults = TestHelper.defaultMediaResponse(
          listOf(
            DataDumpManager.movieDump1,
            DataDumpManager.movieDump2,
          ),
        ),
        mockApiCall = { mockMovieApiService.getTopRatedMovies(1) },
        loader = { pagingSource.toLoadResult() },
      ) { page ->
        TestCase.assertEquals(2, page.data.size)
      }
    }

  @Test
  fun getTopRatedMovies_pagingFlow_returnsExpectedData() =
    runTest {
      val expected = listOf(DataDumpManager.movieDump1, DataDumpManager.movieDump2)
      coEvery { mockMovieApiService.getTopRatedMovies(1) } returns TestHelper.defaultMediaResponse(
        expected,
      )
      movieRemoteDataSource.getTopRatedMovies().testPagingFlow(this, expected)
      coVerify { mockMovieApiService.getTopRatedMovies(1) }
    }

  @Test
  fun getPopularMovies_pagingSource_returnsExpectedData() =
    runTest {
      val pagingSource = GenericPagingSource { mockMovieApiService.getPopularMovies(1).results }
      TestHelper.testPagingSource(
        mockResults = TestHelper.defaultMediaResponse(listOf(DataDumpManager.movieDump5)),
        mockApiCall = { mockMovieApiService.getPopularMovies(1) },
        loader = { pagingSource.toLoadResult() },
      ) { page ->
        TestCase.assertEquals(1, page.data.size)
      }
    }

  @Test
  fun getPopularMovies_pagingFlow_returnsExpectedData() =
    runTest {
      val expected = listOf(DataDumpManager.movieDump5)
      coEvery { mockMovieApiService.getPopularMovies(1) } returns TestHelper.defaultMediaResponse(
        expected,
      )
      movieRemoteDataSource.getPopularMovies().testPagingFlow(this, expected)
      coVerify { mockMovieApiService.getPopularMovies(1) }
    }

  @Test
  fun getFavoriteMovies_pagingSource_returnsExpectedData() =
    runTest {
      val pagingSource = GenericPagingSource {
        mockAccountApiService.getFavoriteMovies(userId, sessionId, 1).results
      }
      TestHelper.testPagingSource(
        mockResults = TestHelper.defaultMediaResponse(listOf(DataDumpManager.movieDump6)),
        mockApiCall = { mockAccountApiService.getFavoriteMovies(userId, sessionId, 1) },
        loader = { pagingSource.toLoadResult() },
      ) { page ->
        TestCase.assertEquals(1, page.data.size)
      }
    }

  @Test
  fun getWatchlistMovies_pagingSource_returnsExpectedData() =
    runTest {
      val pagingSource = GenericPagingSource {
        mockAccountApiService.getWatchlistMovies(userId, sessionId, 1).results
      }
      TestHelper.testPagingSource(
        mockResults = TestHelper.defaultMediaResponse(listOf(DataDumpManager.movieDump2)),
        mockApiCall = { mockAccountApiService.getWatchlistMovies(userId, sessionId, 1) },
        loader = { pagingSource.toLoadResult() },
      ) { page ->
        TestCase.assertEquals(1, page.data.size)
      }
    }

  @Test
  fun getMovieRecommendations_pagingSource_returnsExpectedData() =
    runTest {
      val pagingSource =
        GenericPagingSource { mockMovieApiService.getMovieRecommendations(12345678, 1).results }
      TestHelper.testPagingSource(
        mockResults = TestHelper.defaultMediaResponse(listOf(DataDumpManager.movieDump3)),
        mockApiCall = { mockMovieApiService.getMovieRecommendations(12345678, 1) },
        loader = { pagingSource.toLoadResult() },
      ) { page ->
        TestCase.assertEquals(1, page.data.size)
      }
    }

  @Test
  fun getMovieRecommendations_pagingFlow_returnsExpectedData() =
    runTest {
      val expected = listOf(DataDumpManager.movieDump3)
      coEvery { mockMovieApiService.getMovieRecommendations(12345678, 1) } returns
        TestHelper.defaultMediaResponse(expected)
      movieRemoteDataSource.getMovieRecommendation(12345678).testPagingFlow(this, expected)
      coVerify { mockMovieApiService.getMovieRecommendations(12345678, 1) }
    }

  @Test
  fun getUpcomingMovies_pagingSource_returnsExpectedData() =
    runTest {
      val pagingSource =
        GenericPagingSource { mockMovieApiService.getUpcomingMovies("cn", 1).results }
      TestHelper.testPagingSource(
        mockResults = TestHelper.defaultMediaResponse(listOf(DataDumpManager.movieDump4)),
        mockApiCall = { mockMovieApiService.getUpcomingMovies("cn", 1) },
        loader = { pagingSource.toLoadResult() },
      ) { page ->
        TestCase.assertEquals(1, page.data.size)
      }
    }

  @Test
  fun getUpcomingMovies_pagingFlow_returnsExpectedData() =
    runTest {
      val expected = listOf(DataDumpManager.movieDump4)
      coEvery {
        mockMovieApiService.getUpcomingMovies(
          "cn",
          1,
        )
      } returns TestHelper.defaultMediaResponse(expected)
      movieRemoteDataSource.getUpcomingMovies("cn").testPagingFlow(this, expected)
      coVerify { mockMovieApiService.getUpcomingMovies("cn", 1) }
    }

  @Test
  fun getPlayingNowMovies_pagingSource_returnsExpectedData() =
    runTest {
      val pagingSource =
        GenericPagingSource { mockMovieApiService.getNowPlayingMovies("gb", 1).results }
      TestHelper.testPagingSource(
        mockResults = TestHelper.defaultMediaResponse(listOf(DataDumpManager.movieDump5)),
        mockApiCall = { mockMovieApiService.getNowPlayingMovies("gb", 1) },
        loader = { pagingSource.toLoadResult() },
      ) { page ->
        TestCase.assertEquals(1, page.data.size)
      }
    }

  @Test
  fun getPlayingNowMovies_pagingFlow_returnsExpectedData() =
    runTest {
      val expected = listOf(DataDumpManager.movieDump5)
      coEvery {
        mockMovieApiService.getNowPlayingMovies(
          "gb",
          1,
        )
      } returns TestHelper.defaultMediaResponse(expected)
      movieRemoteDataSource.getPlayingNowMovies("gb").testPagingFlow(this, expected)
      coVerify { mockMovieApiService.getNowPlayingMovies("gb", 1) }
    }
}
