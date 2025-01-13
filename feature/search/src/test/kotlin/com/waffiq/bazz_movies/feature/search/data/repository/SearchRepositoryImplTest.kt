package com.waffiq.bazz_movies.feature.search.data.repository

import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadResult
import app.cash.turbine.test
import com.waffiq.bazz_movies.core.network.data.remote.datasource.MovieDataSource
import com.waffiq.bazz_movies.core.network.data.remote.pagingsources.SearchPagingSource
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.TMDBApiService
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
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

    val resultFlow = repository.getPagingSearch(QUERY)
    // act & assert: Collect the flow and check the results
    resultFlow.test {
      val pagingData = awaitItem() // Collect first item
      val job = launch { differ.submitData(pagingData) }
      advanceUntilIdle()

      // assert on the actual items
      val pagingList = differ.snapshot().items // this is the data you wanted
      assertTrue(pagingList.isEmpty().not())
      job.cancel()

      cancelAndIgnoreRemainingEvents()
    }

    verify { movieDataSource.getPagingSearch(QUERY) }
  }
}
