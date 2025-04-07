package com.waffiq.bazz_movies.core.network.data.remote.datasource

import androidx.paging.PagingSource.LoadParams
import com.waffiq.bazz_movies.core.network.data.remote.pagingsources.GenericPagingSource
import com.waffiq.bazz_movies.core.network.data.remote.pagingsources.SearchPagingSource
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.OMDbApiService
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.TMDBApiService
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.movieDump1
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.movieDump2
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.movieDump3
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.movieDump4
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.movieDump5
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.movieDump6
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.movieDump7
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.personDump1
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.tvShowDump1
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.tvShowDump2
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.tvShowDump3
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.defaultMovieTvResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.defaultMultiSearchResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.differ
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.differSearch
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testPagingFlow
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testPagingFlowSearch
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testPagingSearchSource
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testPagingSource
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import io.mockk.MockKAnnotations
import io.mockk.clearMocks
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MovieDataSourcePagingTest {

  @MockK
  private lateinit var tmdbApiService: TMDBApiService

  @MockK
  private lateinit var omDbApiService: OMDbApiService

  @MockK
  private lateinit var testDispatcher: Dispatchers

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private lateinit var movieDataSource: MovieDataSource

  @Before
  fun setup() {
    // Initialize MockK annotations and relax mocking behavior
    // `relaxed = true` allows MockK to automatically provide
    // default behavior for any un-mocked method calls.
    MockKAnnotations.init(this, relaxed = true)

    // clear any previous mocks to ensure tests are isolated
    clearMocks(tmdbApiService, omDbApiService)

    movieDataSource = MovieDataSource(tmdbApiService, omDbApiService, testDispatcher.IO)
  }

  @Test
  fun getPagingTopRatedMovies_ReturnExpectedPagingData() = runTest {
    val pagingSource = GenericPagingSource { tmdbApiService.getTopRatedMovies(1).results }
    testPagingSource(
      mockResults = defaultMovieTvResponse(listOf(movieDump1, movieDump2)),
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
    movieDataSource.getPagingTopRatedMovies().testPagingFlow(this) { pagingList ->
      val pagingList = differ.snapshot().items
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
  fun getPagingTrendingWeek_ReturnExpectedPagingData() = runTest {
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
  fun getPagingTrendingDay_ReturnExpectedPagingData() = runTest {
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
  fun getPagingPopularMovies_ReturnExpectedPagingData() = runTest {
    val pagingSource = GenericPagingSource { tmdbApiService.getPopularMovies(1).results }
    testPagingSource(
      mockResults = defaultMovieTvResponse(listOf(movieDump5)),
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
    movieDataSource.getPagingPopularMovies().testPagingFlow(this) { pagingList ->
      val pagingList = differ.snapshot().items
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
  fun getPagingFavoriteMovies_ReturnExpectedPagingData() = runTest {
    val pagingSource =
      GenericPagingSource { tmdbApiService.getFavoriteMovies("session_id", 1).results }
    testPagingSource(
      mockResults = defaultMovieTvResponse(listOf(movieDump6)),
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
    movieDataSource.getPagingFavoriteMovies("session_id").testPagingFlow(this) { pagingList ->
      val pagingList = differ.snapshot().items
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
  fun getPagingWatchlistMovies_ReturnExpectedPagingData() = runTest {
    val pagingSource =
      GenericPagingSource { tmdbApiService.getWatchlistMovies("session_id", 1).results }
    testPagingSource(
      mockResults = defaultMovieTvResponse(listOf(movieDump2)),
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
    movieDataSource.getPagingWatchlistMovies("session_id").testPagingFlow(this) { pagingList ->
      val pagingList = differ.snapshot().items
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
  fun getPagingMovieRecommendation_ReturnExpectedPagingData() = runTest {
    val pagingSource =
      GenericPagingSource { tmdbApiService.getRecommendedMovie(12345678, 1).results }
    testPagingSource(
      mockResults = defaultMovieTvResponse(listOf(movieDump3)),
      mockApiCall = { tmdbApiService.getRecommendedMovie(12345678, 1) },
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
    movieDataSource.getPagingMovieRecommendation(12345678).testPagingFlow(this) { pagingList ->
      val pagingList = differ.snapshot().items
      assertEquals(movieDump3, pagingList[0])
      assertTrue(pagingList.isNotEmpty())
      assertEquals(240, pagingList[0].id)
      assertEquals(12577, pagingList[0].voteCount)
      assertEquals("/kGzFbGhp99zva6oZODW5atUtnqi.jpg", pagingList[0].backdropPath)
      assertEquals("/hek3koDUyRQk7FIhPXsa6mT2Zc3.jpg", pagingList[0].posterPath)
    }
    coVerify { tmdbApiService.getRecommendedMovie(12345678, 1) }
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
  fun getPagingUpcomingMovies_ReturnExpectedPagingData() = runTest {
    val pagingSource = GenericPagingSource { tmdbApiService.getUpcomingMovies("cn", 1).results }
    testPagingSource(
      mockResults = defaultMovieTvResponse(listOf(movieDump4)),
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
    movieDataSource.getPagingUpcomingMovies("cn").testPagingFlow(this) { pagingList ->
      val pagingList = differ.snapshot().items
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
  fun getPagingPlayingNowMovies_ReturnExpectedPagingData() = runTest {
    val pagingSource = GenericPagingSource { tmdbApiService.getPlayingNowMovies("gb", 1).results }
    testPagingSource(
      mockResults = defaultMovieTvResponse(listOf(movieDump5)),
      mockApiCall = { tmdbApiService.getPlayingNowMovies("gb", 1) },
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
    movieDataSource.getPagingPlayingNowMovies("gb").testPagingFlow(this) { pagingList ->
      val pagingList = differ.snapshot().items
      assertEquals(movieDump5, pagingList[0])
      assertTrue(pagingList.isNotEmpty())
      assertEquals(58.538, pagingList[0].popularity)
      assertEquals(8775, pagingList[0].voteCount)
      assertEquals("1957-04-10", pagingList[0].releaseDate)
      assertEquals("en", pagingList[0].originalLanguage)
    }
    coVerify { tmdbApiService.getPlayingNowMovies("gb", 1) }
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

  @Test
  fun getPagingSearch_ReturnExpectedPagingData() = runTest {
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
