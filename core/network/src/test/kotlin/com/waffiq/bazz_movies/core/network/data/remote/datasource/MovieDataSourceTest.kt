package com.waffiq.bazz_movies.core.network.data.remote.datasource

import androidx.paging.PagingSource.LoadParams
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
import io.mockk.coVerify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Test

class MovieDataSourceTest : BaseMediaDataSourceTest() {

  @Test
  fun getTopRatedMovies_whenSuccessful_returnsExpectedPagingData() = runTest {
    val pagingSource = GenericPagingSource { tmdbApiService.getTopRatedMovies(1).results }
    testPagingSource(
      mockResults = defaultMediaResponse(listOf(movieDump1, movieDump2)),
      mockApiCall = { tmdbApiService.getTopRatedMovies(1) },
      loader = {
        pagingSource.load(
          LoadParams.Refresh(key = 1, loadSize = 2, placeholdersEnabled = false)
        )
      }
    ) { page ->
      assertEquals(2, page.data.size)
      assertEquals("The Shawshank Redemption", page.data[0].title)
      assertEquals("The Godfather", page.data[1].title)
      assertEquals(movieDump1, page.data[0])
      assertEquals(movieDump2, page.data[1])
      assertEquals(null, page.prevKey)
      assertEquals(2, page.nextKey)
    }

    // test using paging data
    movieDataSource.getTopRatedMovies().testPagingFlow(this) { pagingList ->
      assertEquals(movieDump1, pagingList[0])
      assertEquals(movieDump2, pagingList[1])
      assertTrue(pagingList.isNotEmpty())
      assertEquals(278, pagingList[0].id)
      assertEquals(27450, pagingList[0].voteCount)
      assertEquals("/zfbjgQE1uSd9wiPTX4VzsLi0rGG.jpg", pagingList[0].backdropPath)
      assertEquals("/9cqNxx0GxF0bflZmeSMuL5tnGzr.jpg", pagingList[0].posterPath)
      assertEquals(238, pagingList[1].id)
      assertEquals(20834, pagingList[1].voteCount)
      assertEquals("/tmU7GeKVybMWFButWEGl2M4GeiP.jpg", pagingList[1].backdropPath)
      assertEquals("/3bhkrj58Vtu7enYsRolD1fZdja1.jpg", pagingList[1].posterPath)
    }
    coVerify { tmdbApiService.getTopRatedMovies(1) }
  }

  @Test
  fun getPopularMovies_whenSuccessful_returnsExpectedPagingData() = runTest {
    val pagingSource = GenericPagingSource { tmdbApiService.getPopularMovies(1).results }
    testPagingSource(
      mockResults = defaultMediaResponse(listOf(movieDump5)),
      mockApiCall = { tmdbApiService.getPopularMovies(1) },
      loader = {
        pagingSource.load(LoadParams.Refresh(key = 1, loadSize = 2, placeholdersEnabled = false))
      }
    ) { page ->
      assertEquals(1, page.data.size)
      assertEquals("12 Angry Men", page.data[0].title)
      assertEquals(389, page.data[0].id)
      assertEquals(movieDump5, page.data[0])
      assertEquals(null, page.prevKey)
      assertEquals(2, page.nextKey)
    }

    // test using paging data
    movieDataSource.getPopularMovies().testPagingFlow(this) { pagingList ->
      assertEquals(movieDump5, pagingList[0])
      assertTrue(pagingList.isNotEmpty())
      assertEquals(389, pagingList[0].id)
      assertEquals(8775, pagingList[0].voteCount)
      assertEquals("/bxgTSUenZDHNFerQ1whRKplrMKF.jpg", pagingList[0].backdropPath)
      assertEquals("/ow3wq89wM8qd5X7hWKxiRfsFf9C.jpg", pagingList[0].posterPath)
    }
    coVerify { tmdbApiService.getPopularMovies(1) }
  }

  @Test
  fun getFavoriteMovies_whenSuccessful_returnsExpectedPagingData() = runTest {
    val pagingSource =
      GenericPagingSource { tmdbApiService.getFavoriteMovies("session_id", 1).results }
    testPagingSource(
      mockResults = defaultMediaResponse(listOf(movieDump6)),
      mockApiCall = { tmdbApiService.getFavoriteMovies("session_id", 1) },
      loader = {
        pagingSource.load(LoadParams.Refresh(key = 1, loadSize = 2, placeholdersEnabled = false))
      }
    ) { page ->
      assertEquals(1, page.data.size)
      assertEquals("Flow", page.data[0].title)
      assertEquals(823219, page.data[0].id)
      assertEquals(movieDump6, page.data[0])
      assertEquals(null, page.prevKey)
      assertEquals(2, page.nextKey)
    }

    // test using paging data
    movieDataSource.getFavoriteMovies("session_id").testPagingFlow(this) { pagingList ->
      assertEquals(movieDump6, pagingList[0])
      assertTrue(pagingList.isNotEmpty())
      assertEquals(823219, pagingList[0].id)
      assertEquals(558, pagingList[0].voteCount)
      assertNull(pagingList[0].backdropPath)
      assertEquals("/imKSymKBK7o73sajciEmndJoVkR.jpg", pagingList[0].posterPath)
    }
    coVerify { tmdbApiService.getFavoriteMovies("session_id", 1) }
  }

  @Test
  fun getWatchlistMovies_whenSuccessful_returnsExpectedPagingData() = runTest {
    val pagingSource =
      GenericPagingSource { tmdbApiService.getWatchlistMovies("session_id", 1).results }
    testPagingSource(
      mockResults = defaultMediaResponse(listOf(movieDump2)),
      mockApiCall = { tmdbApiService.getWatchlistMovies("session_id", 1) },
      loader = {
        pagingSource.load(LoadParams.Refresh(key = 1, loadSize = 2, placeholdersEnabled = false))
      }
    ) { page ->
      assertEquals(1, page.data.size)
      assertEquals("The Godfather", page.data[0].title)
      assertEquals(238, page.data[0].id)
      assertEquals(movieDump2, page.data[0])
      assertEquals(null, page.prevKey)
      assertEquals(2, page.nextKey)
    }

    // test using paging data
    movieDataSource.getWatchlistMovies("session_id").testPagingFlow(this) { pagingList ->
      assertEquals(movieDump2, pagingList[0])
      assertTrue(pagingList.isNotEmpty())
      assertEquals(220.965, pagingList[0].popularity)
      assertEquals(20834, pagingList[0].voteCount)
      assertEquals("/tmU7GeKVybMWFButWEGl2M4GeiP.jpg", pagingList[0].backdropPath)
      assertEquals("/3bhkrj58Vtu7enYsRolD1fZdja1.jpg", pagingList[0].posterPath)
    }
    coVerify { tmdbApiService.getWatchlistMovies("session_id", 1) }
  }

  @Test
  fun getMovieRecommendation_whenSuccessful_returnsExpectedPagingData() = runTest {
    val pagingSource =
      GenericPagingSource { tmdbApiService.getMovieRecommendations(12345678, 1).results }
    testPagingSource(
      mockResults = defaultMediaResponse(listOf(movieDump3)),
      mockApiCall = { tmdbApiService.getMovieRecommendations(12345678, 1) },
      loader = {
        pagingSource.load(LoadParams.Refresh(key = 1, loadSize = 2, placeholdersEnabled = false))
      }
    ) { page ->
      assertEquals(1, page.data.size)
      assertEquals("The Godfather Part II", page.data[0].title)
      assertEquals(240, page.data[0].id)
      assertEquals(movieDump3, page.data[0])
      assertEquals(null, page.prevKey)
      assertEquals(2, page.nextKey)
    }

    // test using paging data
    movieDataSource.getMovieRecommendation(12345678).testPagingFlow(this) { pagingList ->
      assertEquals(movieDump3, pagingList[0])
      assertTrue(pagingList.isNotEmpty())
      assertEquals(240, pagingList[0].id)
      assertEquals(12577, pagingList[0].voteCount)
      assertEquals("/kGzFbGhp99zva6oZODW5atUtnqi.jpg", pagingList[0].backdropPath)
      assertEquals("/hek3koDUyRQk7FIhPXsa6mT2Zc3.jpg", pagingList[0].posterPath)
    }
    coVerify { tmdbApiService.getMovieRecommendations(12345678, 1) }
  }

  @Test
  fun getUpcomingMovies_whenSuccessful_returnsExpectedPagingData() = runTest {
    val pagingSource = GenericPagingSource { tmdbApiService.getUpcomingMovies("cn", 1).results }
    testPagingSource(
      mockResults = defaultMediaResponse(listOf(movieDump4)),
      mockApiCall = { tmdbApiService.getUpcomingMovies("cn", 1) },
      loader = {
        pagingSource.load(LoadParams.Refresh(key = 1, loadSize = 2, placeholdersEnabled = false))
      }
    ) { page ->
      assertEquals(1, page.data.size)
      assertEquals("Schindler's List", page.data[0].title)
      assertEquals(424, page.data[0].id)
      assertEquals(movieDump4, page.data[0])
      assertEquals(null, page.prevKey)
      assertEquals(2, page.nextKey)
    }

    // test using paging data
    movieDataSource.getUpcomingMovies("cn").testPagingFlow(this) { pagingList ->
      assertEquals(movieDump4, pagingList[0])
      assertTrue(pagingList.isNotEmpty())
      assertEquals(424, pagingList[0].id)
      assertEquals(16001, pagingList[0].voteCount)
      assertEquals("1993-12-15", pagingList[0].releaseDate)
      assertEquals("en", pagingList[0].originalLanguage)
    }
    coVerify { tmdbApiService.getUpcomingMovies("cn", 1) }
  }

  @Test
  fun getPlayingNowMovies_whenSuccessful_returnsExpectedPagingData() = runTest {
    val pagingSource = GenericPagingSource { tmdbApiService.getNowPlayingMovies("gb", 1).results }
    testPagingSource(
      mockResults = defaultMediaResponse(listOf(movieDump5)),
      mockApiCall = { tmdbApiService.getNowPlayingMovies("gb", 1) },
      loader = {
        pagingSource.load(LoadParams.Refresh(key = 1, loadSize = 2, placeholdersEnabled = false))
      }
    ) { page ->
      assertEquals(1, page.data.size)
      assertEquals("12 Angry Men", page.data[0].title)
      assertEquals(listOf(18), page.data[0].genreIds)
      assertEquals(movieDump5, page.data[0])
      assertEquals(null, page.prevKey)
      assertEquals(2, page.nextKey)
    }

    // test using paging data
    movieDataSource.getPlayingNowMovies("gb").testPagingFlow(this) { pagingList ->
      assertEquals(movieDump5, pagingList[0])
      assertTrue(pagingList.isNotEmpty())
      assertEquals(58.538, pagingList[0].popularity)
      assertEquals(8775, pagingList[0].voteCount)
      assertEquals("1957-04-10", pagingList[0].releaseDate)
      assertEquals("en", pagingList[0].originalLanguage)
    }
    coVerify { tmdbApiService.getNowPlayingMovies("gb", 1) }
  }
}
