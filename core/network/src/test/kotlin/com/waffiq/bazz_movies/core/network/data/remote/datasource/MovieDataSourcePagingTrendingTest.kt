package com.waffiq.bazz_movies.core.network.data.remote.datasource

import androidx.paging.PagingSource.LoadParams
import com.waffiq.bazz_movies.core.network.data.remote.pagingsources.GenericPagingSource
import com.waffiq.bazz_movies.core.network.data.remote.pagingsources.SearchPagingSource
import com.waffiq.bazz_movies.core.network.testutils.BaseMovieDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.movieDump2
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.movieDump3
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.movieDump4
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.movieDump7
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.personDump1
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.tvShowDump1
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.defaultMovieTvResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.defaultMultiSearchResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.differ
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.differSearch
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testPagingFlow
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testPagingFlowSearch
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testPagingSearchSource
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testPagingSource
import io.mockk.coVerify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Test

class MovieDataSourcePagingTrendingTest : BaseMovieDataSourceTest() {

  @Test
  fun getPagingTrendingWeek_returnExpectedPagingData() = runTest {
    val pagingSource = GenericPagingSource { tmdbApiService.getTrendingWeek("id", 1).results }
    testPagingSource(
      mockResults = defaultMovieTvResponse(listOf(movieDump4, movieDump2, movieDump3)),
      mockApiCall = { tmdbApiService.getTrendingWeek("id", 1) },
      loader = {
        pagingSource.load(LoadParams.Refresh(key = 1, loadSize = 2, placeholdersEnabled = false))
      }
    ) { page ->
      assertEquals(3, page.data.size)
      assertEquals("Schindler's List", page.data[0].title)
      assertEquals("The Godfather", page.data[1].title)
      assertEquals("The Godfather Part II", page.data[2].title)
      assertEquals(movieDump4, page.data[0])
      assertEquals(movieDump2, page.data[1])
      assertEquals(movieDump3, page.data[2])
      assertEquals(null, page.prevKey)
      assertEquals(2, page.nextKey)
    }

    // test using paging data
    movieDataSource.getPagingTrendingWeek("id").testPagingFlow(this) { pagingList ->
      val pagingList = differ.snapshot().items
      assertEquals(movieDump4, pagingList[0])
      assertEquals(movieDump2, pagingList[1])
      assertEquals(movieDump3, pagingList[2])
      assertTrue(pagingList.isNotEmpty())
      assertEquals(424, pagingList[0].id)
      assertEquals(16001, pagingList[0].voteCount)
      assertEquals("/zb6fM1CX41D9rF9hdgclu0peUmy.jpg", pagingList[0].backdropPath)
      assertEquals("/sF1U4EUQS8YHUYjNl3pMGNIQyr0.jpg", pagingList[0].posterPath)
      assertEquals(238, pagingList[1].id)
      assertEquals(20834, pagingList[1].voteCount)
      assertEquals("/tmU7GeKVybMWFButWEGl2M4GeiP.jpg", pagingList[1].backdropPath)
      assertEquals("/3bhkrj58Vtu7enYsRolD1fZdja1.jpg", pagingList[1].posterPath)
      assertEquals(240, pagingList[2].id)
      assertEquals(12577, pagingList[2].voteCount)
      assertEquals("/kGzFbGhp99zva6oZODW5atUtnqi.jpg", pagingList[2].backdropPath)
      assertEquals("/hek3koDUyRQk7FIhPXsa6mT2Zc3.jpg", pagingList[2].posterPath)
    }
    coVerify { tmdbApiService.getTrendingWeek("id", 1) }
  }

  @Test
  fun getPagingTrendingDay_returnExpectedPagingData() = runTest {
    val pagingSource = GenericPagingSource { tmdbApiService.getTrendingDay("ca", 1).results }
    testPagingSource(
      mockResults = defaultMovieTvResponse(listOf(tvShowDump1, movieDump7)),
      mockApiCall = { tmdbApiService.getTrendingDay("ca", 1) },
      loader = {
        pagingSource.load(LoadParams.Refresh(key = 1, loadSize = 2, placeholdersEnabled = false))
      }
    ) { page ->
      assertEquals(2, page.data.size)
      assertEquals("Squid Game", page.data[0].name)
      assertEquals("Wicked", page.data[1].title)
      assertEquals(tvShowDump1, page.data[0])
      assertEquals(movieDump7, page.data[1])
      assertEquals(null, page.prevKey)
      assertEquals(2, page.nextKey)
    }

    // test using paging data
    movieDataSource.getPagingTrendingDay("ca").testPagingFlow(this) { pagingList ->
      val pagingList = differ.snapshot().items
      assertEquals(tvShowDump1, pagingList[0])
      assertEquals(movieDump7, pagingList[1])
      assertTrue(pagingList.isNotEmpty())
      assertEquals(93405, pagingList[0].id)
      assertEquals(14755, pagingList[0].voteCount)
      assertNull(pagingList[0].backdropPath)
      assertEquals("/dDlEmu3EZ0Pgg93K2SVNLCjCSvE.jpg", pagingList[0].posterPath)
      assertEquals(402431, pagingList[1].id)
      assertEquals(1071, pagingList[1].voteCount)
      assertNull(pagingList[1].backdropPath)
      assertEquals("/2E1x1qcHqGZcYuYi4PzVZjzg8IV.jpg", pagingList[1].posterPath)
    }
    coVerify { tmdbApiService.getTrendingDay("ca", 1) }
  }

  @Test
  fun getPagingSearch_returnExpectedPagingData() = runTest {
    val pagingSource = SearchPagingSource(tmdbApiService, "john")
    testPagingSearchSource(
      mockResults = defaultMultiSearchResponse(listOf(personDump1)),
      mockApiCall = { tmdbApiService.search("john", 1) },
      loader = {
        pagingSource.load(LoadParams.Refresh(key = 1, loadSize = 2, placeholdersEnabled = false))
      }
    ) { page ->
      assertEquals(1, page.data.size)
      assertEquals(18918, page.data[0].id)
      assertEquals("Dwayne Johnson", page.data[0].name)
      assertEquals("Acting", page.data[0].knownForDepartment)
      assertEquals("Jumanji: Welcome to the Jungle", page.data[0].listKnownFor?.get(0)?.title)
      assertEquals("Jumanji: The Next Level", page.data[0].listKnownFor?.get(1)?.title)
      assertEquals("San Andreas", page.data[0].listKnownFor?.get(2)?.title)
      assertEquals(null, page.prevKey)
      assertEquals(2, page.nextKey)
    }

    // test using paging data
    movieDataSource.getPagingSearch("john").testPagingFlowSearch(this) { pagingList ->
      val pagingList = differSearch.snapshot().items
      assertEquals(personDump1, pagingList[0])
      assertTrue(pagingList.isNotEmpty())
      assertEquals(102.851, pagingList[0].popularity)
      assertEquals("/kuqFzlYMc2IrsOyPznMd1FroeGq.jpg", pagingList[0].profilePath)
      assertTrue(pagingList[0].adult == false)
    }
    coVerify { tmdbApiService.search("john", 1) }
  }
}
