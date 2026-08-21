package com.waffiq.bazz_movies.core.network.data.remote.datasource.search

import com.waffiq.bazz_movies.core.network.data.remote.pagingsources.SearchPagingSource
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.search.MultiSearchResponse
import com.waffiq.bazz_movies.core.network.testutils.BaseMediaDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DummyData.multiMediaTypeSet
import com.waffiq.bazz_movies.core.network.testutils.DummyData.personDump1
import com.waffiq.bazz_movies.core.network.testutils.DummyData.personMediaTypeSet
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.defaultMultiSearchResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testPagingFlowSearch
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testPagingSearchSource
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SearchRemoteDataSourceTest : BaseMediaDataSourceTest() {

  @Test
  fun search_pagingSource_returnsExpectedData() =
    runTest {
      val pagingSource = SearchPagingSource(mockSearchApiService, "john", personMediaTypeSet)

      testPagingSearchSource(
        mockResults = defaultMultiSearchResponse(listOf(personDump1)),
        mockApiCall = { mockSearchApiService.searchPerson("john", 1) },
        loader = { pagingSource.toLoadResult() },
      ) { page ->
        assertEquals(1, page.data.size)
      }
    }

  @Test
  fun search_pagingFlow_returnsExpectedData() =
    runTest {
      val expected = listOf(personDump1)
      coEvery { mockSearchApiService.searchPerson("john", 1) } returns
        defaultMultiSearchResponse(expected)

      searchRemoteDataSource.search("john", personMediaTypeSet)
        .testPagingFlowSearch(this, expected)

      coVerify { mockSearchApiService.searchPerson("john", 1) }
    }

  @Test
  fun search_withoutFilters_shouldUseMultiMediaType() =
    runTest {
      val singleResponseItem = MultiSearchResponse(
        page = 1,
        results = listOf(personDump1),
        totalResults = 1,
        totalPages = 1,
      )

      coEvery { mockSearchApiService.searchMulti(any(), any()) } returns
        singleResponseItem

      searchRemoteDataSource.search("batman", multiMediaTypeSet)
        .testPagingFlowSearch(this, listOf(personDump1))

      coVerify {
        mockSearchApiService.searchMulti(any(), any())
      }

      // should not fetch others media type
      coVerify(exactly = 0) {
        mockSearchApiService.searchMovies(any(), any())
      }

      coVerify(exactly = 0) {
        mockSearchApiService.searchTv(any(), any())
      }

      coVerify(exactly = 0) {
        mockSearchApiService.searchPerson(any(), any())
      }
    }
}
