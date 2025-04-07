package com.waffiq.bazz_movies.core.network.data.remote.pagingsources

import androidx.paging.PagingSource
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.ResultItemResponse
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
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.fail
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.HttpException
import java.io.IOException

class GenericPagingSourceTest {

  private val apiCallMock: suspend (Int) -> List<ResultItemResponse> = mockk()

  @Test
  fun load_ReturnsPage_OnSuccessfulApiCall() = runTest {
    val resultItems = listOf(ResultItemResponse("item1"), ResultItemResponse("item2"))
    testLoadReturnsPage(
      pagingSourceFactory = { GenericPagingSource(apiCallMock) },
      setupMock = {
        coEvery { apiCallMock(INITIAL_PAGE_INDEX) } returns resultItems
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
      pagingSourceFactory = { GenericPagingSource(apiCallMock) },
      setupMock = {
        coEvery { apiCallMock(INITIAL_PAGE_INDEX) } throws IOException()
      },
      params = PagingSource.LoadParams.Refresh(INITIAL_PAGE_INDEX, 2, false),
      expectedException = IOException::class.java
    )
  }

  @Test
  fun load_ReturnsError_OnHttpException() = runTest {
    testLoadReturnsErrorOnHttpException(
      pagingSourceFactory = { GenericPagingSource(apiCallMock) },
      setupMock = {
        val exception = mockk<HttpException> { every { message } returns "HTTP Error" }
        coEvery { apiCallMock(INITIAL_PAGE_INDEX) } throws exception
      },
      params = PagingSource.LoadParams.Refresh(INITIAL_PAGE_INDEX, 2, false),
      expectedMessage = "HTTP Error"
    )
  }

  @Test
  fun load_ReturnsNullData_OnNullResponseData() = runTest {
    coEvery { apiCallMock(INITIAL_PAGE_INDEX) } returns listOf(ResultItemResponse())
    val pagingSource = GenericPagingSource(apiCallMock)

    val result = pagingSource.load(PagingSource.LoadParams.Refresh(INITIAL_PAGE_INDEX, 2, false))
    if (result is PagingSource.LoadResult.Page) {
      assertNull(result.data[0].name)
      assertNull(result.data[0].title)
    } else {
      fail("Expected LoadResult.Error but got $result")
    }
  }

  @Test
  fun load_ReturnsPage_WithNonNullPrevKey_OnSubsequentPage() = runTest {
    testLoadReturnsPageWithNonNullPrevKeyOnSubsequentPage(
      pagingSourceFactory = { GenericPagingSource(apiCallMock) },
      setupMock = { page ->
        val resultItems = listOf(ResultItemResponse("item1"), ResultItemResponse("item2"))
        coEvery { apiCallMock(page) } returns resultItems
      },
      page = 2,
      expectedData = listOf(ResultItemResponse("item1"), ResultItemResponse("item2"))
    )
  }

  @Test
  fun load_ReturnsPage_WithNullNextKey_OnEmptyResponse() = runTest {
    testLoadReturnsPageWithNullNextKeyOnEmptyResponse(
      pagingSourceFactory = { GenericPagingSource(apiCallMock) },
      setupMock = { page ->
        coEvery { apiCallMock(page) } returns emptyList<ResultItemResponse>()
      },
      expectedData = emptyList()
    )
  }

  @Test
  fun getRefreshKey_ReturnsCorrectKey_AnchorInMiddlePage() {
    testRefreshKeyWithAnchorInMiddlePage(
      pagingSource = GenericPagingSource(apiCallMock),
      data = listOf(
        listOf(ResultItemResponse("item1"), ResultItemResponse("item2")),
        listOf(ResultItemResponse("item3"), ResultItemResponse("item4"))
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
      pagingSource = GenericPagingSource(apiCallMock),
      data = listOf(ResultItemResponse("item1")),
      anchorPosition = 0,
      prevKey = 1,
      nextKey = 3,
      expectedRefreshKey = 2 // prevKey + 1
    )
  }

  @Test
  fun getRefreshKey_UsesNextKey_WhenPrefKeyIsNull() {
    testRefreshKeyUsesCorrectKey(
      pagingSource = GenericPagingSource(apiCallMock),
      data = listOf(ResultItemResponse("item1")),
      anchorPosition = 0,
      prevKey = null,
      nextKey = 3,
      expectedRefreshKey = 2 // nextKey - 1
    )
  }

  @Test
  fun getRefreshKey_ReturnsNull_WhenAllKeysAreNull() {
    testRefreshKeyAllKeysNull(
      pagingSource = GenericPagingSource(apiCallMock),
      data = listOf(ResultItemResponse("item1"))
    )
  }

  @Test
  fun getRefreshKey_ReturnsNull_WhenNoAnchorPositionAndEmptyPages() {
    testRefreshKeyEmptyList(
      pagingSource = GenericPagingSource(apiCallMock),
      anchorPosition = null
    )
  }

  @Test
  fun getRefreshKey_ReturnsNull_WhenAnchorIsZeroAndEmptyPages() {
    testRefreshKeyEmptyList(
      pagingSource = GenericPagingSource(apiCallMock),
      anchorPosition = 0
    )
  }
}
