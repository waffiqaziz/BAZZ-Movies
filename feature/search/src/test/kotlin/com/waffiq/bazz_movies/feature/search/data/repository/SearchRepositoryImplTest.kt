package com.waffiq.bazz_movies.feature.search.data.repository

import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadResult
import app.cash.turbine.test
import com.waffiq.bazz_movies.core.network.data.remote.datasource.MovieDataSource
import com.waffiq.bazz_movies.core.network.data.remote.pagingsources.SearchPagingSource
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.search.MultiSearchResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.TMDBApiService
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import com.waffiq.bazz_movies.feature.search.testutils.SearchTestVariables.QUERY
import com.waffiq.bazz_movies.feature.search.testutils.SearchTestVariables.differ
import com.waffiq.bazz_movies.feature.search.testutils.SearchTestVariables.multiSearchResponse
import com.waffiq.bazz_movies.feature.search.testutils.SearchTestVariables.multiSearchResponseItem
import com.waffiq.bazz_movies.feature.search.testutils.SearchTestVariables.multiSearchResponseItem2
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
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
  fun movieDataSource_whenSearching_returnsCorrectPageData() = runTest {
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
  fun movieDataSource_whenSearchFailsWithIOException_returnsLoadError() = runTest {
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
  fun search_whenSuccessful_returnsDataCorrectly() = runTest {
    val fakePagingData =
      PagingData.from(listOf(multiSearchResponseItem, multiSearchResponseItem2))
    every { movieDataSource.search(QUERY) } returns flowOf(fakePagingData)

    repository.search(QUERY).test {
      val pagingData = awaitItem() // collect first item
      val job = launch { differ.submitData(pagingData) }
      advanceUntilIdle()

      val listMediaItemSearch = differ.snapshot().items
      assertTrue(listMediaItemSearch.isEmpty().not())
      job.cancel()

      cancelAndIgnoreRemainingEvents()
    }

    verify { movieDataSource.search(QUERY) }
  }

  @Test
  fun search_whenSuccessful_returnsPagedData() = runTest {
    val emptyPagingData = PagingData.from(emptyList<MultiSearchResponseItem>())
    every { movieDataSource.search(QUERY) } returns flowOf(emptyPagingData)

    repository.search(QUERY).test {
      val pagingData = awaitItem()
      val job = launch { differ.submitData(pagingData) }
      advanceUntilIdle()

      assertTrue(differ.snapshot().items.isEmpty())
      job.cancel()

      cancelAndIgnoreRemainingEvents()
    }

    verify { movieDataSource.search(QUERY) }
  }

  @Test
  fun search_whenSearchItemIsNull_returnsNonEmptyPagingData() = runTest {
    val invalidItem = mockk<MultiSearchResponseItem>(relaxed = true)
    val pagingDataWithNull = PagingData.from(listOf(invalidItem))

    every { movieDataSource.search(QUERY) } returns flowOf(pagingDataWithNull)

    repository.search(QUERY).test {
      val pagingData = awaitItem()
      val job = launch { differ.submitData(pagingData) }
      advanceUntilIdle()

      assertTrue(differ.snapshot().items.isEmpty().not())
      job.cancel()

      cancelAndIgnoreRemainingEvents()
    }

    verify { movieDataSource.search(QUERY) }
  }

  @Test
  fun search_whenItemHasNulls_setsDefaultValues() = runTest {
    val responseWithNulls = MultiSearchResponseItem(
      mediaType = null, // should default to MOVIE_MEDIA_TYPE
      popularity = null, // should default to 0.0
      id = null, // should default to 0
      adult = null, // should default to false
      video = null, // should default to false
      voteAverage = null, // should default to 0.0
      voteCount = null // should default to 0.0
    )
    val pagingDataWithNulls = PagingData.from(listOf(responseWithNulls))
    every { movieDataSource.search(QUERY) } returns flowOf(pagingDataWithNulls)

    repository.search(QUERY).test {
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
    verify { movieDataSource.search(QUERY) }
  }
}
