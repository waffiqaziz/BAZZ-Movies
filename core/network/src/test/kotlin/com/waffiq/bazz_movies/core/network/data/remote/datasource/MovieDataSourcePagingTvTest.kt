package com.waffiq.bazz_movies.core.network.data.remote.datasource

import androidx.paging.PagingSource.LoadParams
import com.waffiq.bazz_movies.core.network.data.remote.pagingsources.GenericPagingSource
import com.waffiq.bazz_movies.core.network.testutils.BaseMovieDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.tvShowDump1
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.tvShowDump2
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.tvShowDump3
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.defaultMovieTvResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.differ
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testPagingFlow
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testPagingSource
import io.mockk.coVerify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Test

class MovieDataSourcePagingTvTest : BaseMovieDataSourceTest() {

  @Test
  fun getPagingFavoriteTv_ReturnExpectedPagingData() = runTest {
    val pagingSource = GenericPagingSource { tmdbApiService.getFavoriteTv("session_id", 1).results }
    testPagingSource(
      mockResults = defaultMovieTvResponse(listOf(tvShowDump2)),
      mockApiCall = { tmdbApiService.getFavoriteTv("session_id", 1) },
      loader = {
        pagingSource.load(LoadParams.Refresh(key = 1, loadSize = 2, placeholdersEnabled = false))
      }
    ) { page ->
      assertEquals(1, page.data.size)
      assertEquals("Eternal Love", page.data[0].title)
      assertEquals(69316, page.data[0].id)
      assertEquals(tvShowDump2, page.data[0])
      assertEquals(null, page.prevKey)
      assertEquals(2, page.nextKey)
    }

    // test using paging data
    movieDataSource.getPagingFavoriteTv("session_id").testPagingFlow(this) { pagingList ->
      val pagingList = differ.snapshot().items
      assertEquals(tvShowDump2, pagingList[0])
      assertTrue(pagingList.isNotEmpty())
      assertEquals(69316, pagingList[0].id)
      assertEquals(48, pagingList[0].voteCount)
      assertNull(pagingList[0].backdropPath)
      assertEquals("/c7Pfx7dQiRsi1rU8N9s05gHnAkI.jpg", pagingList[0].posterPath)
    }
    coVerify { tmdbApiService.getFavoriteTv("session_id", 1) }
  }

  @Test
  fun getPagingWatchlistTv_ReturnExpectedPagingData() = runTest {
    val pagingSource =
      GenericPagingSource { tmdbApiService.getWatchlistTv("session_id", 1).results }
    testPagingSource(
      mockResults = defaultMovieTvResponse(listOf(tvShowDump2)),
      mockApiCall = { tmdbApiService.getWatchlistTv("session_id", 1) },
      loader = {
        pagingSource.load(LoadParams.Refresh(key = 1, loadSize = 2, placeholdersEnabled = false))
      }
    ) { page ->
      assertEquals(1, page.data.size)
      assertEquals("Eternal Love", page.data[0].title)
      assertEquals("tv", page.data[0].mediaType)
      assertEquals(7.6f, page.data[0].voteAverage)
      assertEquals(tvShowDump2, page.data[0])
      assertEquals(null, page.prevKey)
      assertEquals(2, page.nextKey)
    }

    // test using paging data
    movieDataSource.getPagingWatchlistTv("session_id").testPagingFlow(this) { pagingList ->
      val pagingList = differ.snapshot().items
      assertEquals(tvShowDump2, pagingList[0])
      assertTrue(pagingList.isNotEmpty())
      assertEquals(listOf(18, 10765), pagingList[0].genreIds)
      assertTrue(pagingList[0].adult == false)
      assertNull(pagingList[0].backdropPath)
      assertEquals("zh", pagingList[0].originalLanguage)
    }
    coVerify { tmdbApiService.getWatchlistTv("session_id", 1) }
  }

  @Test
  fun getPagingPopularTv_ReturnExpectedPagingData() = runTest {
    val pagingSource =
      GenericPagingSource { tmdbApiService.getPopularTv(1, "id", "2023-11-06").results }
    testPagingSource(
      mockResults = defaultMovieTvResponse(listOf(tvShowDump3)),
      mockApiCall = { tmdbApiService.getPopularTv(1, "id", "2023-11-06") },
      loader = {
        pagingSource.load(LoadParams.Refresh(key = 1, loadSize = 2, placeholdersEnabled = false))
      }
    ) { page ->
      assertEquals(1, page.data.size)
      assertEquals("Kingdom", page.data[0].title)
      assertEquals(46437, page.data[0].id)
      assertEquals(tvShowDump3, page.data[0])
      assertEquals(null, page.prevKey)
      assertEquals(2, page.nextKey)
    }

    // test using paging data
    movieDataSource.getPagingPopularTv("id", "2023-11-06").testPagingFlow(this) { pagingList ->
      val pagingList = differ.snapshot().items
      assertEquals(tvShowDump3, pagingList[0])
      assertTrue(pagingList.isNotEmpty())
      assertEquals("Kingdom", pagingList[0].title)
      assertEquals(38, pagingList[0].voteCount)
      assertNull(pagingList[0].backdropPath)
      assertEquals("/dehuJJkKo50nYvCYppigrWejqLe.jpg", pagingList[0].posterPath)
    }
    coVerify { tmdbApiService.getPopularTv(1, "id", "2023-11-06") }
  }

  @Test
  fun getPagingAiringThisWeekTv_ReturnExpectedPagingData() = runTest {
    val pagingSource = GenericPagingSource {
      tmdbApiService.getTvAiring(
        "id",
        "2023-11-14",
        "2023-11-06",
        1
      ).results
    }
    testPagingSource(
      mockResults = defaultMovieTvResponse(listOf(tvShowDump1, tvShowDump2, tvShowDump3)),
      mockApiCall = { tmdbApiService.getTvAiring("id", "2023-11-14", "2023-11-06", 1) },
      loader = {
        pagingSource.load(LoadParams.Refresh(key = 1, loadSize = 2, placeholdersEnabled = false))
      }
    ) { page ->
      assertEquals(3, page.data.size)
      assertEquals("Squid Game", page.data[0].name)
      assertEquals(93405, page.data[0].id)
      assertEquals(tvShowDump1, page.data[0])
      assertEquals(null, page.prevKey)
      assertEquals(2, page.nextKey)
    }

    // test using paging data
    movieDataSource.getPagingAiringThisWeekTv("id", "2023-11-14", "2023-11-06")
      .testPagingFlow(this) { pagingList ->
        val pagingList = differ.snapshot().items
        assertEquals(tvShowDump2, pagingList[1])
        assertTrue(pagingList.isNotEmpty())
        assertEquals("Eternal Love", pagingList[1].title)
        assertEquals(48, pagingList[1].voteCount)
        assertNull(pagingList[1].backdropPath)
        assertEquals("/c7Pfx7dQiRsi1rU8N9s05gHnAkI.jpg", pagingList[1].posterPath)
      }
    coVerify { tmdbApiService.getTvAiring("id", "2023-11-14", "2023-11-06", 1) }
  }

  @Test
  fun getPagingAiringTodayTv_ReturnExpectedPagingData() = runTest {
    val pagingSource = GenericPagingSource {
      tmdbApiService.getTvAiring(
        "id",
        "2023-11-14",
        "2023-11-14",
        1
      ).results
    }
    testPagingSource(
      mockResults = defaultMovieTvResponse(listOf(tvShowDump1, tvShowDump3)),
      mockApiCall = { tmdbApiService.getTvAiring("id", "2023-11-14", "2023-11-14", 1) },
      loader = {
        pagingSource.load(LoadParams.Refresh(key = 1, loadSize = 2, placeholdersEnabled = false))
      }
    ) { page ->
      assertEquals(2, page.data.size)
      assertEquals("/dDlEmu3EZ0Pgg93K2SVNLCjCSvE.jpg", page.data[0].posterPath)
      assertEquals(93405, page.data[0].id)
      assertEquals(tvShowDump1, page.data[0])
      assertEquals(null, page.prevKey)
      assertEquals(2, page.nextKey)
    }

    // test using paging data
    movieDataSource.getPagingAiringTodayTv("id", "2023-11-14", "2023-11-14")
      .testPagingFlow(this) { pagingList ->
        val pagingList = differ.snapshot().items
        assertEquals(tvShowDump3, pagingList[1])
        assertTrue(pagingList.isNotEmpty())
        assertEquals("Kingdom", pagingList[1].title)
        assertEquals(38, pagingList[1].voteCount)
        assertNull(pagingList[1].backdropPath)
        assertEquals("/dehuJJkKo50nYvCYppigrWejqLe.jpg", pagingList[1].posterPath)
      }
    coVerify { tmdbApiService.getTvAiring("id", "2023-11-14", "2023-11-14", 1) }
  }

  @Test
  fun getPagingTvRecommendation_ReturnExpectedPagingData() = runTest {
    val pagingSource = GenericPagingSource { tmdbApiService.getRecommendedTv(98765, 1).results }
    testPagingSource(
      mockResults = defaultMovieTvResponse(listOf(tvShowDump1)),
      mockApiCall = { tmdbApiService.getRecommendedTv(98765, 1) },
      loader = {
        pagingSource.load(LoadParams.Refresh(key = 1, loadSize = 2, placeholdersEnabled = false))
      }
    ) { page ->
      assertEquals(1, page.data.size)
      assertEquals(10243.452, page.data[0].popularity)
      assertEquals("2021-09-17", page.data[0].firstAirDate)
      assertEquals(tvShowDump1, page.data[0])
      assertEquals(null, page.prevKey)
      assertEquals(2, page.nextKey)
    }

    // test using paging data
    movieDataSource.getPagingTvRecommendation(98765).testPagingFlow(this) { pagingList ->
      val pagingList = differ.snapshot().items
      assertEquals(tvShowDump1, pagingList[0])
      assertTrue(pagingList.isNotEmpty())
      assertEquals("Squid Game", pagingList[0].name)
      assertEquals(14755, pagingList[0].voteCount)
      assertNull(pagingList[0].backdropPath)
      assertNull(pagingList[0].originalTitle)
    }
    coVerify { tmdbApiService.getRecommendedTv(98765, 1) }
  }

  @Test
  fun getPagingTopRatedTv_ReturnExpectedPagingData() = runTest {
    val pagingSource = GenericPagingSource { tmdbApiService.getTopRatedTv(1).results }
    testPagingSource(
      mockResults = defaultMovieTvResponse(listOf(tvShowDump3, tvShowDump2, tvShowDump1)),
      mockApiCall = { tmdbApiService.getTopRatedTv(1) },
      loader = {
        pagingSource.load(LoadParams.Refresh(key = 1, loadSize = 2, placeholdersEnabled = false))
      }
    ) { page ->
      assertEquals(3, page.data.size)
      assertEquals("/dehuJJkKo50nYvCYppigrWejqLe.jpg", page.data[0].posterPath)
      assertEquals(46437, page.data[0].id)
      assertEquals(tvShowDump3, page.data[0])
      assertEquals(null, page.prevKey)
      assertEquals(2, page.nextKey)
    }

    // test using paging data
    movieDataSource.getPagingTopRatedTv().testPagingFlow(this) { pagingList ->
      val pagingList = differ.snapshot().items
      assertEquals(tvShowDump2, pagingList[1])
      assertTrue(pagingList.isNotEmpty())
      assertEquals("Eternal Love", pagingList[1].title)
      assertEquals(48, pagingList[1].voteCount)
      assertNull(pagingList[1].backdropPath)
      assertTrue(pagingList[1].adult == false)
      assertEquals("zh", pagingList[1].originalLanguage)
      assertEquals("/c7Pfx7dQiRsi1rU8N9s05gHnAkI.jpg", pagingList[1].posterPath)
    }
    coVerify { tmdbApiService.getTopRatedTv(1) }
  }
}
