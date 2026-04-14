package com.waffiq.bazz_movies.core.network.data.remote.datasource.search

import com.waffiq.bazz_movies.core.network.data.remote.pagingsources.SearchPagingSource
import com.waffiq.bazz_movies.core.network.testutils.BaseMediaDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.personDump1
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.defaultMultiSearchResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testPagingFlowSearch
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testPagingSearchSource
import io.mockk.coEvery
import io.mockk.coVerify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SearchRemoteDataSourceTest : BaseMediaDataSourceTest() {

  @Test
  fun search_pagingSource_returnsExpectedData() = runTest {
    val pagingSource = SearchPagingSource(mockSearchApiService, "john")
    testPagingSearchSource(
      mockResults = defaultMultiSearchResponse(listOf(personDump1)),
      mockApiCall = { mockSearchApiService.search("john", 1) },
      loader = { pagingSource.toLoadResult() }
    ) { page ->
      assertEquals(1, page.data.size)
    }
  }

  @Test
  fun search_pagingFlow_returnsExpectedData() = runTest {
    val expected = listOf(personDump1)
    coEvery { mockSearchApiService.search("john", 1) } returns defaultMultiSearchResponse(expected)
    searchRemoteDataSource.search("john").testPagingFlowSearch(this, expected)
    coVerify { mockSearchApiService.search("john", 1) }
  }
}
