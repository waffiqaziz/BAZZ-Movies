package com.waffiq.bazz_movies.core.network.data.remote.pagingsources

import androidx.paging.PagingSource
import com.google.common.truth.Truth.assertThat
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.search.MultiSearchResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.search.ResultsItemSearchResponse
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.TMDBApiService
import com.waffiq.bazz_movies.core.network.testutils.PagingSourceTestHelper.testLoadReturnsErrorOnException
import com.waffiq.bazz_movies.core.network.testutils.PagingSourceTestHelper.testLoadReturnsErrorOnHttpException
import com.waffiq.bazz_movies.core.network.testutils.PagingSourceTestHelper.testLoadReturnsPage
import com.waffiq.bazz_movies.core.network.testutils.PagingSourceTestHelper.testLoadReturnsPageWithNonNullPrevKeyOnSubsequentPage
import com.waffiq.bazz_movies.core.network.testutils.PagingSourceTestHelper.testLoadReturnsPageWithNullNextKeyOnEmptyResponse
import com.waffiq.bazz_movies.core.network.testutils.PagingSourceTestHelper.testRefreshKeyAllKeysNull
import com.waffiq.bazz_movies.core.network.testutils.PagingSourceTestHelper.testRefreshKeyEmptyList
import com.waffiq.bazz_movies.core.network.testutils.PagingSourceTestHelper.testRefreshKeyUsesCorrectKey
import com.waffiq.bazz_movies.core.network.testutils.PagingSourceTestHelper.testRefreshKeyWithAnchorInMiddlePage
import com.waffiq.bazz_movies.core.network.utils.common.Constants.INITIAL_PAGE_INDEX
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.fail
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.HttpException
import java.io.IOException

class SearchPagingSourceTest {

  private val apiServiceMock: TMDBApiService = mockk()
  private val query = "test query"

  @Test
  fun load_ReturnsPage_OnSuccessfulApiCall() = runTest {
    val resultItems = listOf(ResultsItemSearchResponse("item1"), ResultsItemSearchResponse("item2"))
    testLoadReturnsPage(
      pagingSourceFactory = { SearchPagingSource(apiServiceMock, query) },
      setupMock = {
        coEvery {
          apiServiceMock.search(query, INITIAL_PAGE_INDEX)
        } returns MultiSearchResponse(results = resultItems)
      },
      params = PagingSource.LoadParams.Refresh(INITIAL_PAGE_INDEX, 2, false),
      expectedData = resultItems,
      expectedPrevKey = null,
      expectedNextKey = INITIAL_PAGE_INDEX + 1
    )
  }

  @Test
  fun load_ReturnsError_OnIOException() = runTest {
    testLoadReturnsErrorOnException(
      pagingSourceFactory = { SearchPagingSource(apiServiceMock, query) },
      setupMock = {
        coEvery { apiServiceMock.search(query, INITIAL_PAGE_INDEX) } throws IOException()
      },
      params = PagingSource.LoadParams.Refresh(INITIAL_PAGE_INDEX, 2, false),
      expectedException = IOException::class.java
    )
  }

  @Test
  fun load_ReturnsError_OnHttpException() = runTest {
    testLoadReturnsErrorOnHttpException(
      pagingSourceFactory = { SearchPagingSource(apiServiceMock, query) },
      setupMock = {
        val exception = mockk<HttpException> { every { message } returns "HTTP Error" }
        coEvery { apiServiceMock.search(query, INITIAL_PAGE_INDEX) } throws exception
      },
      params = PagingSource.LoadParams.Refresh(INITIAL_PAGE_INDEX, 2, false),
      expectedMessage = "HTTP Error"
    )
  }

  @Test
  fun load_ReturnsError_OnNullResponseData() = runTest {
    coEvery {
      apiServiceMock.search(query, INITIAL_PAGE_INDEX)
    } returns MultiSearchResponse(results = null)
    val pagingSource = SearchPagingSource(apiServiceMock, query)

    val result = pagingSource.load(PagingSource.LoadParams.Refresh(INITIAL_PAGE_INDEX, 2, false))

    if (result is PagingSource.LoadResult.Error) {
      assertThat(result.throwable).isInstanceOf(Exception::class.java)
      assertThat(result.throwable.message).isEqualTo("Response data is null")
    } else {
      fail("Expected LoadResult.Error but got $result")
    }
  }

  @Test
  fun load_ReturnsPage_WithNonNullPrevKey_OnSubsequentPage() = runTest {
    testLoadReturnsPageWithNonNullPrevKeyOnSubsequentPage(
      pagingSourceFactory = { SearchPagingSource(apiServiceMock, query) },
      setupMock = { page ->
        val resultItems =
          listOf(ResultsItemSearchResponse("item1"), ResultsItemSearchResponse("item2"))
        coEvery {
          apiServiceMock.search(
            query,
            page
          )
        } returns MultiSearchResponse(results = resultItems)
      },
      page = 2,
      expectedData = listOf(ResultsItemSearchResponse("item1"), ResultsItemSearchResponse("item2"))
    )
  }

  @Test
  fun load_ReturnsPage_WithNullNextKey_OnEmptyResponse() = runTest {
    testLoadReturnsPageWithNullNextKeyOnEmptyResponse(
      pagingSourceFactory = { SearchPagingSource(apiServiceMock, query) },
      setupMock = { page ->
        coEvery {
          apiServiceMock.search(query, page)
        } returns MultiSearchResponse(results = emptyList<ResultsItemSearchResponse>())
      },
      expectedData = emptyList()
    )
  }

  @Test
  fun getRefreshKey_ReturnsCorrectKey_AnchorInMiddlePage() {
    testRefreshKeyWithAnchorInMiddlePage(
      pagingSource = SearchPagingSource(apiServiceMock, query),
      data = listOf(
        listOf(ResultsItemSearchResponse("item1"), ResultsItemSearchResponse("item2")),
        listOf(ResultsItemSearchResponse("item3"), ResultsItemSearchResponse("item4"))
      ),
      prevKeys = listOf(null, 1),
      nextKeys = listOf(2, 3),
      anchorPosition = 2, // Anchor in the middle of the second page
      expectedRefreshKey = 2 // Expected nextKey of the first page
    )
  }

  @Test
  fun getRefreshKey_UsesPrevKey_WhenAnchorPageHasEveryKey() {
    testRefreshKeyUsesCorrectKey(
      pagingSource = SearchPagingSource(apiServiceMock, query),
      data = listOf(ResultsItemSearchResponse("item1")),
      anchorPosition = 0,
      prevKey = 1,
      nextKey = 3,
      expectedRefreshKey = 2 // prevKey + 1
    )
  }

  @Test
  fun getRefreshKey_UsesNextKey_WhenPrefKeyIsNull() {
    testRefreshKeyUsesCorrectKey(
      pagingSource = SearchPagingSource(apiServiceMock, query),
      data = listOf(ResultsItemSearchResponse("item1")),
      anchorPosition = 0,
      prevKey = null,
      nextKey = 3,
      expectedRefreshKey = 2 // nextKey - 1
    )
  }

  @Test
  fun getRefreshKey_ReturnsNull_WhenAllKeysAreNull() {
    testRefreshKeyAllKeysNull(
      pagingSource = SearchPagingSource(apiServiceMock, query),
      data = listOf(ResultsItemSearchResponse("item1"))
    )
  }

  @Test
  fun getRefreshKey_ReturnsNull_WhenNoAnchorPositionAndEmptyPages() {
    testRefreshKeyEmptyList(
      pagingSource = SearchPagingSource(apiServiceMock, query),
      anchorPosition = null
    )
  }

  @Test
  fun getRefreshKey_ReturnsNull_WhenAnchorIsZeroAndEmptyPages() {
    testRefreshKeyEmptyList(
      pagingSource = SearchPagingSource(apiServiceMock, query),
      anchorPosition = 0
    )
  }
}
