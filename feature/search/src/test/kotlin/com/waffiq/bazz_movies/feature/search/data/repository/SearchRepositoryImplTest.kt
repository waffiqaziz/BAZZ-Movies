package com.waffiq.bazz_movies.feature.search.data.repository

import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadResult
import app.cash.turbine.test
import com.waffiq.bazz_movies.core.network.data.remote.datasource.MovieDataSource
import com.waffiq.bazz_movies.core.network.data.remote.pagingsources.SearchPagingSource
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.search.ResultsItemSearchResponse
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.TMDBApiService
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import com.waffiq.bazz_movies.feature.search.domain.model.ResultsItemSearch
import com.waffiq.bazz_movies.feature.search.testutils.SearchTestVariables.QUERY
import com.waffiq.bazz_movies.feature.search.testutils.SearchTestVariables.differ
import com.waffiq.bazz_movies.feature.search.testutils.SearchTestVariables.multiSearchResponse
import com.waffiq.bazz_movies.feature.search.testutils.SearchTestVariables.resultsItemSearchResponse
import com.waffiq.bazz_movies.feature.search.testutils.SearchTestVariables.resultsItemSearchResponse2
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class SearchRepositoryImplTest {

  private lateinit var repository: SearchRepositoryImpl
  private val movieDataSource: MovieDataSource = mockk()
  private val mockTMDBApiService = mockk<TMDBApiService>()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Before
  fun setUp() {
    repository = SearchRepositoryImpl(movieDataSource)
  }

  @Test
  fun movieDataSource_getPagingSearchReturnsDataCorrectly() = runTest {
    coEvery { mockTMDBApiService.search(QUERY, 1) } returns multiSearchResponse
    val pagingSource = SearchPagingSource(mockTMDBApiService, QUERY)

    val result = pagingSource.load(
      PagingSource.LoadParams.Refresh(key = 1, loadSize = 2, placeholdersEnabled = false)
    )

    advanceUntilIdle()

    assert(result is LoadResult.Page)
    val page = result as LoadResult.Page
    assertEquals(2, page.data.size)
    assertEquals("Transformers TV-series", page.data[0].title)
    assertEquals("Transformers 2", page.data[1].title)
    assertEquals(null, page.prevKey)
    assertEquals(2, page.nextKey) // expect nextKey to be the next page index
    coVerify { mockTMDBApiService.search(query = QUERY, page = 1) }
  }

  @Test
  fun movieDataSource_getPagingSearchThrowsIOException() = runTest {
    coEvery { mockTMDBApiService.search(QUERY, 1) } throws IOException("Network Error")
    val pagingSource = SearchPagingSource(mockTMDBApiService, QUERY)

    val result = pagingSource.load(
      PagingSource.LoadParams.Refresh(key = 1, loadSize = 2, placeholdersEnabled = false)
    )

    advanceUntilIdle()

    assert(result is LoadResult.Error)
    val error = result as LoadResult.Error
    assertTrue(error.throwable is IOException)
    assertEquals("Network Error", error.throwable.message)
    coVerify { mockTMDBApiService.search(query = QUERY, page = 1) }
  }

  @Test
  fun repository_getPagingSearchReturnsDataCorrectly() = runTest {
    val fakePagingData =
      PagingData.from(listOf(resultsItemSearchResponse, resultsItemSearchResponse2))
    every { movieDataSource.getPagingSearch(QUERY) } returns flowOf(fakePagingData)

    repository.getPagingSearch(QUERY).test {
      val pagingData = awaitItem() // Collect first item
      val job = launch { differ.submitData(pagingData) }
      advanceUntilIdle()

      val listResultItemSearch = differ.snapshot().items
      assertTrue(listResultItemSearch.isEmpty().not())
      job.cancel()

      cancelAndIgnoreRemainingEvents()
    }

    verify { movieDataSource.getPagingSearch(QUERY) }
  }

  @Test
  fun repository_getPagingSearchWithEmptyData() = runTest {
    val emptyPagingData = PagingData.from(emptyList<ResultsItemSearchResponse>())
    every { movieDataSource.getPagingSearch(QUERY) } returns flowOf(emptyPagingData)

    repository.getPagingSearch(QUERY).test {
      val pagingData = awaitItem()
      val job = launch { differ.submitData(pagingData) }
      advanceUntilIdle()

      assertTrue(differ.snapshot().items.isEmpty())
      job.cancel()

      cancelAndIgnoreRemainingEvents()
    }

    verify { movieDataSource.getPagingSearch(QUERY) }
  }

  @Test
  fun repository_getPagingSearchWithNullData() = runTest {
    val invalidItem = mockk<ResultsItemSearchResponse>(relaxed = true)
    val pagingDataWithNull = PagingData.from(listOf(invalidItem))

    every { movieDataSource.getPagingSearch(QUERY) } returns flowOf(pagingDataWithNull)

    repository.getPagingSearch(QUERY).test {
      val pagingData = awaitItem()
      val job = launch { differ.submitData(pagingData) }
      advanceUntilIdle()

      assertTrue(differ.snapshot().items.isEmpty().not())
      job.cancel()

      cancelAndIgnoreRemainingEvents()
    }

    verify { movieDataSource.getPagingSearch(QUERY) }
  }

  @Test
  fun repository_getPagingSearchWithNullValues() = runTest {
    val responseWithNulls = ResultsItemSearchResponse(
      mediaType = null, // Should default to MOVIE_MEDIA_TYPE
      popularity = null, // Should default to 0.0
      id = null, // Should default to 0
      adult = null, // Should default to false
      video = null, // Should default to false
      voteAverage = null, // Should default to 0.0
      voteCount = null // Should default to 0.0
    )

    val pagingDataWithNulls = PagingData.from(listOf(responseWithNulls))

    every { movieDataSource.getPagingSearch(QUERY) } returns flowOf(pagingDataWithNulls)

    repository.getPagingSearch(QUERY).test {
      val pagingData = awaitItem()
      val job = launch { differ.submitData(pagingData) }
      advanceUntilIdle()

      val result = differ.snapshot().items.first()
      assertEquals("movie", result.mediaType)
      assertEquals(0.0, result.popularity)
      assertEquals(0, result.id)
      assertEquals(false, result.adult)
      assertEquals(false, result.video)
      assertEquals(0.0, result.voteAverage)
      assertEquals(0.0, result.voteCount)

      job.cancel()
      cancelAndIgnoreRemainingEvents()
    }

    verify { movieDataSource.getPagingSearch(QUERY) }
  }
}
