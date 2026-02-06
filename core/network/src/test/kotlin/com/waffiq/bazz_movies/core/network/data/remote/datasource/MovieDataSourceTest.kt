package com.waffiq.bazz_movies.core.network.data.remote.datasource

import com.waffiq.bazz_movies.core.network.data.remote.pagingsources.GenericPagingSource
import com.waffiq.bazz_movies.core.network.testutils.BaseMediaDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.movieDump1
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.movieDump2
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.movieDump3
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.movieDump4
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.movieDump5
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.movieDump6
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.defaultMediaResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testPagingFlow
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testPagingSource
import io.mockk.coEvery
import io.mockk.coVerify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test

class MovieDataSourceTest : BaseMediaDataSourceTest() {

  @Test
  fun getTopRatedMovies_pagingSource_returnsExpectedData() = runTest {
    val pagingSource = GenericPagingSource { tmdbApiService.getTopRatedMovies(1).results }
    testPagingSource(
      mockResults = defaultMediaResponse(listOf(movieDump1, movieDump2)),
      mockApiCall = { tmdbApiService.getTopRatedMovies(1) },
      loader = { pagingSource.toLoadResult() }
    ) { page ->
      assertEquals(2, page.data.size)
    }
  }

  @Test
  fun getTopRatedMovies_pagingFlow_returnsExpectedData() = runTest {
    val expected = listOf(movieDump1, movieDump2)
    coEvery { tmdbApiService.getTopRatedMovies(1) } returns defaultMediaResponse(expected)
    movieDataSource.getTopRatedMovies().testPagingFlow(this, expected)
    coVerify { tmdbApiService.getTopRatedMovies(1) }
  }

  @Test
  fun getPopularMovies_pagingSource_returnsExpectedData() = runTest {
    val pagingSource = GenericPagingSource { tmdbApiService.getPopularMovies(1).results }
    testPagingSource(
      mockResults = defaultMediaResponse(listOf(movieDump5)),
      mockApiCall = { tmdbApiService.getPopularMovies(1) },
      loader = { pagingSource.toLoadResult() }
    ) { page ->
      assertEquals(1, page.data.size)
    }
  }

  @Test
  fun getPopularMovies_pagingFlow_returnsExpectedData() = runTest {
    val expected = listOf(movieDump5)
    coEvery { tmdbApiService.getPopularMovies(1) } returns defaultMediaResponse(expected)
    movieDataSource.getPopularMovies().testPagingFlow(this, expected)
    coVerify { tmdbApiService.getPopularMovies(1) }
  }

  @Test
  fun getFavoriteMovies_pagingSource_returnsExpectedData() = runTest {
    val pagingSource =
      GenericPagingSource { tmdbApiService.getFavoriteMovies("session_id", 1).results }
    testPagingSource(
      mockResults = defaultMediaResponse(listOf(movieDump6)),
      mockApiCall = { tmdbApiService.getFavoriteMovies("session_id", 1) },
      loader = { pagingSource.toLoadResult() }
    ) { page ->
      assertEquals(1, page.data.size)
    }
  }

  @Test
  fun getFavoriteMovies_pagingFlow_returnsExpectedData() = runTest {
    val expected = listOf(movieDump6)
    coEvery { tmdbApiService.getFavoriteMovies("session_id", 1) } returns
      defaultMediaResponse(expected)
    movieDataSource.getFavoriteMovies("session_id").testPagingFlow(this, expected)
    coVerify { tmdbApiService.getFavoriteMovies("session_id", 1) }
  }

  @Test
  fun getWatchlistMovies_pagingSource_returnsExpectedData() = runTest {
    val pagingSource =
      GenericPagingSource { tmdbApiService.getWatchlistMovies("session_id", 1).results }
    testPagingSource(
      mockResults = defaultMediaResponse(listOf(movieDump2)),
      mockApiCall = { tmdbApiService.getWatchlistMovies("session_id", 1) },
      loader = { pagingSource.toLoadResult() }
    ) { page ->
      assertEquals(1, page.data.size)
    }
  }

  @Test
  fun getWatchlistMovies_pagingFlow_returnsExpectedData() = runTest {
    val expected = listOf(movieDump2)
    coEvery { tmdbApiService.getWatchlistMovies("session_id", 1) } returns
      defaultMediaResponse(expected)
    movieDataSource.getWatchlistMovies("session_id").testPagingFlow(this, expected)
    coVerify { tmdbApiService.getWatchlistMovies("session_id", 1) }
  }

  @Test
  fun getMovieRecommendations_pagingSource_returnsExpectedData() = runTest {
    val pagingSource =
      GenericPagingSource { tmdbApiService.getMovieRecommendations(12345678, 1).results }
    testPagingSource(
      mockResults = defaultMediaResponse(listOf(movieDump3)),
      mockApiCall = { tmdbApiService.getMovieRecommendations(12345678, 1) },
      loader = { pagingSource.toLoadResult() }
    ) { page ->
      assertEquals(1, page.data.size)
    }
  }

  @Test
  fun getMovieRecommendations_pagingFlow_returnsExpectedData() = runTest {
    val expected = listOf(movieDump3)
    coEvery { tmdbApiService.getMovieRecommendations(12345678, 1) } returns
      defaultMediaResponse(expected)
    movieDataSource.getMovieRecommendation(12345678).testPagingFlow(this, expected)
    coVerify { tmdbApiService.getMovieRecommendations(12345678, 1) }
  }

  @Test
  fun getUpcomingMovies_pagingSource_returnsExpectedData() = runTest {
    val pagingSource = GenericPagingSource { tmdbApiService.getUpcomingMovies("cn", 1).results }
    testPagingSource(
      mockResults = defaultMediaResponse(listOf(movieDump4)),
      mockApiCall = { tmdbApiService.getUpcomingMovies("cn", 1) },
      loader = { pagingSource.toLoadResult() }
    ) { page ->
      assertEquals(1, page.data.size)
    }
  }

  @Test
  fun getUpcomingMovies_pagingFlow_returnsExpectedData() = runTest {
    val expected = listOf(movieDump4)
    coEvery { tmdbApiService.getUpcomingMovies("cn", 1) } returns defaultMediaResponse(expected)
    movieDataSource.getUpcomingMovies("cn").testPagingFlow(this, expected)
    coVerify { tmdbApiService.getUpcomingMovies("cn", 1) }
  }

  @Test
  fun getPlayingNowMovies_pagingSource_returnsExpectedData() = runTest {
    val pagingSource = GenericPagingSource { tmdbApiService.getNowPlayingMovies("gb", 1).results }
    testPagingSource(
      mockResults = defaultMediaResponse(listOf(movieDump5)),
      mockApiCall = { tmdbApiService.getNowPlayingMovies("gb", 1) },
      loader = { pagingSource.toLoadResult() }
    ) { page ->
      assertEquals(1, page.data.size)
    }
  }

  @Test
  fun getPlayingNowMovies_pagingFlow_returnsExpectedData() = runTest {
    val expected = listOf(movieDump5)
    coEvery { tmdbApiService.getNowPlayingMovies("gb", 1) } returns defaultMediaResponse(expected)
    movieDataSource.getPlayingNowMovies("gb").testPagingFlow(this, expected)
    coVerify { tmdbApiService.getNowPlayingMovies("gb", 1) }
  }
}
