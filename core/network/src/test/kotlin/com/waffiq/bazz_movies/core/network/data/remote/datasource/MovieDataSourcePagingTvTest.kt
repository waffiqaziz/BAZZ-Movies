package com.waffiq.bazz_movies.core.network.data.remote.datasource

import androidx.paging.PagingData
import androidx.paging.PagingSource.LoadParams
import androidx.paging.PagingSource.LoadResult
import com.waffiq.bazz_movies.core.network.data.remote.pagingsources.GenericPagingSource
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.MovieTvResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.ResultItemResponse
import com.waffiq.bazz_movies.core.network.testutils.BaseMovieDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.tvShowDump1
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.tvShowDump2
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.tvShowDump3
import com.waffiq.bazz_movies.core.network.testutils.PagingDataSourceTestHelper.assertPageBasics
import com.waffiq.bazz_movies.core.network.testutils.PagingDataSourceTestHelper.assertPagingFlowBasics
import com.waffiq.bazz_movies.core.network.testutils.PagingDataSourceTestHelper.assertPagingKeys
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.defaultMovieTvResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.differ
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testPagingFlow
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testPagingSource
import io.mockk.coVerify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import org.junit.Test

class MovieDataSourcePagingTvTest : BaseMovieDataSourceTest() {

  // test tv-series data scenarios
  private data class TvTestScenario(
    val testName: String,
    val expectedData: List<ResultItemResponse>,
    val pagingSourceFactory: () -> GenericPagingSource,
    val mockApiCall: suspend () -> MovieTvResponse,
    val dataSourceCall: () -> Flow<PagingData<ResultItemResponse>>,
    val pageValidation: (LoadResult.Page<Int, ResultItemResponse>) -> Unit,
    val pagingFlowValidation: (List<ResultItemResponse>) -> Unit,
  )

  @Test
  fun getPagingFavoriteTv_ReturnExpectedPagingData() = runTest {
    val scenario = createFavoriteTvScenario()
    testTvPagingScenario(scenario)
  }

  @Test
  fun getPagingWatchlistTv_ReturnExpectedPagingData() = runTest {
    val scenario = createWatchlistTvScenario()
    testTvPagingScenario(scenario)
  }

  @Test
  fun getPagingPopularTv_ReturnExpectedPagingData() = runTest {
    val scenario = createPopularTvScenario()
    testTvPagingScenario(scenario)
  }

  @Test
  fun getPagingAiringThisWeekTv_ReturnExpectedPagingData() = runTest {
    val scenario = createAiringThisWeekTvScenario()
    testTvPagingScenario(scenario)
  }

  @Test
  fun getPagingAiringTodayTv_ReturnExpectedPagingData() = runTest {
    val scenario = createAiringTodayTvScenario()
    testTvPagingScenario(scenario)
  }

  @Test
  fun getPagingTvRecommendation_ReturnExpectedPagingData() = runTest {
    val scenario = createTvRecommendationScenario()
    testTvPagingScenario(scenario)
  }

  @Test
  fun getPagingTopRatedTv_ReturnExpectedPagingData() = runTest {
    val scenario = createTopRatedTvScenario()
    testTvPagingScenario(scenario)
  }

  // Common test execution method
  private suspend fun testTvPagingScenario(scenario: TvTestScenario) = runTest {
    val pagingSource = scenario.pagingSourceFactory()

    testPagingSource(
      mockResults = defaultMovieTvResponse(scenario.expectedData),
      mockApiCall = scenario.mockApiCall,
      loader = {
        pagingSource.load(LoadParams.Refresh(key = 1, loadSize = 2, placeholdersEnabled = false))
      }
    ) { page ->
      scenario.pageValidation(page)
    }

    // Test using paging data
    scenario.dataSourceCall().testPagingFlow(this) { pagingList ->
      val items = differ.snapshot().items
      scenario.pagingFlowValidation(items)
    }
  }

  // Scenario factory methods
  private fun createFavoriteTvScenario() = TvTestScenario(
    testName = "FavoriteTv",
    expectedData = listOf(tvShowDump2),
    pagingSourceFactory = {
      GenericPagingSource { tmdbApiService.getFavoriteTv("session_id", 1).results }
    },
    mockApiCall = { tmdbApiService.getFavoriteTv("session_id", 1) },
    dataSourceCall = { movieDataSource.getPagingFavoriteTv("session_id") },
    pageValidation = { page ->
      assertPageBasics(page, expectedSize = 1, expectedTitle = "Eternal Love", expectedId = 69316)
      assertEquals(tvShowDump2, page.data[0])
      assertPagingKeys(page, prevKey = null, nextKey = 2)
    },
    pagingFlowValidation = { items ->
      assertPagingFlowBasics(items, expectedItem = tvShowDump2, expectedId = 69316)
      assertEquals(48, items[0].voteCount)
      assertNull(items[0].backdropPath)
      assertEquals("/c7Pfx7dQiRsi1rU8N9s05gHnAkI.jpg", items[0].posterPath)
      coVerify { tmdbApiService.getFavoriteTv("session_id", 1) }
    }
  )

  private fun createWatchlistTvScenario() = TvTestScenario(
    testName = "WatchlistTv",
    expectedData = listOf(tvShowDump2),
    pagingSourceFactory = {
      GenericPagingSource { tmdbApiService.getWatchlistTv("session_id", 1).results }
    },
    mockApiCall = { tmdbApiService.getWatchlistTv("session_id", 1) },
    dataSourceCall = { movieDataSource.getPagingWatchlistTv("session_id") },
    pageValidation = { page ->
      assertPageBasics(page, expectedSize = 1, expectedTitle = "Eternal Love", expectedId = 69316)
      assertEquals("tv", page.data[0].mediaType)
      assertEquals(7.6f, page.data[0].voteAverage)
      assertEquals(tvShowDump2, page.data[0])
      assertPagingKeys(page, prevKey = null, nextKey = 2)
    },
    pagingFlowValidation = { items ->
      assertPagingFlowBasics(items, expectedItem = tvShowDump2, expectedId = 69316)
      assertEquals(listOf(18, 10765), items[0].genreIds)
      assertFalse(items[0].adult == true)
      assertNull(items[0].backdropPath)
      assertEquals("zh", items[0].originalLanguage)
      coVerify { tmdbApiService.getWatchlistTv("session_id", 1) }
    }
  )

  private fun createPopularTvScenario() = TvTestScenario(
    testName = "PopularTv",
    expectedData = listOf(tvShowDump3),
    pagingSourceFactory = {
      GenericPagingSource { tmdbApiService.getPopularTv(1, "id", "2023-11-06").results }
    },
    mockApiCall = { tmdbApiService.getPopularTv(1, "id", "2023-11-06") },
    dataSourceCall = { movieDataSource.getPagingPopularTv("id", "2023-11-06") },
    pageValidation = { page ->
      assertPageBasics(page, expectedSize = 1, expectedTitle = "Kingdom", expectedId = 46437)
      assertEquals(tvShowDump3, page.data[0])
      assertPagingKeys(page, prevKey = null, nextKey = 2)
    },
    pagingFlowValidation = { items ->
      assertPagingFlowBasics(items, expectedItem = tvShowDump3, expectedId = 46437)
      assertEquals("Kingdom", items[0].title)
      assertEquals(38, items[0].voteCount)
      assertNull(items[0].backdropPath)
      assertEquals("/dehuJJkKo50nYvCYppigrWejqLe.jpg", items[0].posterPath)
      coVerify { tmdbApiService.getPopularTv(1, "id", "2023-11-06") }
    }
  )

  private fun createAiringThisWeekTvScenario() = TvTestScenario(
    testName = "AiringThisWeekTv",
    expectedData = listOf(tvShowDump1, tvShowDump2, tvShowDump3),
    pagingSourceFactory = {
      GenericPagingSource {
        tmdbApiService.getTvAiring("id", "2023-11-14", "2023-11-06", 1).results
      }
    },
    mockApiCall = { tmdbApiService.getTvAiring("id", "2023-11-14", "2023-11-06", 1) },
    dataSourceCall = {
      movieDataSource.getPagingAiringThisWeekTv(
        "id",
        "2023-11-14",
        "2023-11-06"
      )
    },
    pageValidation = { page ->
      assertEquals(3, page.data.size)
      assertEquals("Squid Game", page.data[0].name)
      assertEquals(93405, page.data[0].id)
      assertEquals(tvShowDump1, page.data[0])
      assertPagingKeys(page, prevKey = null, nextKey = 2)
    },
    pagingFlowValidation = { items ->
      assertTrue(items.isNotEmpty())
      assertEquals(tvShowDump2, items[1])
      assertEquals("Eternal Love", items[1].title)
      assertEquals(48, items[1].voteCount)
      assertNull(items[1].backdropPath)
      assertEquals("/c7Pfx7dQiRsi1rU8N9s05gHnAkI.jpg", items[1].posterPath)
      coVerify { tmdbApiService.getTvAiring("id", "2023-11-14", "2023-11-06", 1) }
    }
  )

  private fun createAiringTodayTvScenario() = TvTestScenario(
    testName = "AiringTodayTv",
    expectedData = listOf(tvShowDump1, tvShowDump3),
    pagingSourceFactory = {
      GenericPagingSource {
        tmdbApiService.getTvAiring("id", "2023-11-14", "2023-11-14", 1).results
      }
    },
    mockApiCall = { tmdbApiService.getTvAiring("id", "2023-11-14", "2023-11-14", 1) },
    dataSourceCall = { movieDataSource.getPagingAiringTodayTv("id", "2023-11-14", "2023-11-14") },
    pageValidation = { page ->
      assertEquals(2, page.data.size)
      assertEquals("/dDlEmu3EZ0Pgg93K2SVNLCjCSvE.jpg", page.data[0].posterPath)
      assertEquals(93405, page.data[0].id)
      assertEquals(tvShowDump1, page.data[0])
      assertPagingKeys(page, prevKey = null, nextKey = 2)
    },
    pagingFlowValidation = { items ->
      assertTrue(items.isNotEmpty())
      assertEquals(tvShowDump3, items[1])
      assertEquals("Kingdom", items[1].title)
      assertEquals(38, items[1].voteCount)
      assertNull(items[1].backdropPath)
      assertEquals("/dehuJJkKo50nYvCYppigrWejqLe.jpg", items[1].posterPath)
      coVerify { tmdbApiService.getTvAiring("id", "2023-11-14", "2023-11-14", 1) }
    }
  )

  private fun createTvRecommendationScenario() = TvTestScenario(
    testName = "TvRecommendation",
    expectedData = listOf(tvShowDump1),
    pagingSourceFactory = {
      GenericPagingSource { tmdbApiService.getRecommendedTv(98765, 1).results }
    },
    mockApiCall = { tmdbApiService.getRecommendedTv(98765, 1) },
    dataSourceCall = { movieDataSource.getPagingTvRecommendation(98765) },
    pageValidation = { page ->
      assertPageBasics(page, expectedSize = 1, expectedId = 93405)
      assertEquals(10243.452, page.data[0].popularity)
      assertEquals("2021-09-17", page.data[0].firstAirDate)
      assertEquals(tvShowDump1, page.data[0])
      assertPagingKeys(page, prevKey = null, nextKey = 2)
    },
    pagingFlowValidation = { items ->
      assertPagingFlowBasics(items, expectedItem = tvShowDump1, expectedId = 93405)
      assertEquals("Squid Game", items[0].name)
      assertEquals(14755, items[0].voteCount)
      assertNull(items[0].backdropPath)
      assertNull(items[0].originalTitle)
      coVerify { tmdbApiService.getRecommendedTv(98765, 1) }
    }
  )

  private fun createTopRatedTvScenario() = TvTestScenario(
    testName = "TopRatedTv",
    expectedData = listOf(tvShowDump3, tvShowDump2, tvShowDump1),
    pagingSourceFactory = {
      GenericPagingSource { tmdbApiService.getTopRatedTv(1).results }
    },
    mockApiCall = { tmdbApiService.getTopRatedTv(1) },
    dataSourceCall = { movieDataSource.getPagingTopRatedTv() },
    pageValidation = { page ->
      assertEquals(3, page.data.size)
      assertEquals("/dehuJJkKo50nYvCYppigrWejqLe.jpg", page.data[0].posterPath)
      assertEquals(46437, page.data[0].id)
      assertEquals(tvShowDump3, page.data[0])
      assertPagingKeys(page, prevKey = null, nextKey = 2)
    },
    pagingFlowValidation = { items ->
      assertTrue(items.isNotEmpty())
      assertEquals(tvShowDump2, items[1])
      assertEquals("Eternal Love", items[1].title)
      assertEquals(48, items[1].voteCount)
      assertNull(items[1].backdropPath)
      assertFalse(items[1].adult == true)
      assertEquals("zh", items[1].originalLanguage)
      assertEquals("/c7Pfx7dQiRsi1rU8N9s05gHnAkI.jpg", items[1].posterPath)
      coVerify { tmdbApiService.getTopRatedTv(1) }
    }
  )
}
