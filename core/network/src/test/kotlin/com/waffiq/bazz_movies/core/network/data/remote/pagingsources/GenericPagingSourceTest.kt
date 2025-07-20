package com.waffiq.bazz_movies.core.network.data.remote.pagingsources

import androidx.paging.PagingSource
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.MediaResponseItem
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

  private val apiCallMock: suspend (Int) -> List<MediaResponseItem> = mockk()

  @Test
  fun loadInitialPage_whenApiCallSucceeds_returnsCorrectPage() = runTest {
    val mediaItems = listOf(MediaResponseItem("item1"), MediaResponseItem("item2"))
    testLoadReturnsPage(
      pagingSourceFactory = { GenericPagingSource(apiCallMock) },
      setupMock = {
        coEvery { apiCallMock(INITIAL_PAGE_INDEX) } returns mediaItems
      },
      params = PagingSource.LoadParams.Refresh(INITIAL_PAGE_INDEX, 2, false),
      expectedData = mediaItems,
      expectedPrevKey = null,
      expectedNextKey = INITIAL_PAGE_INDEX + 1
    )
  }

  @Test
  fun loadPage_whenIOExceptionOccurs_returnsError() = runTest {
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
  fun loadPage_whenHttpExceptionOccurs_returnsErrorWithMessage() = runTest {
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
  fun loadPage_whenResponseDataIsNull_returnsNullFields() = runTest {
    coEvery { apiCallMock(INITIAL_PAGE_INDEX) } returns listOf(MediaResponseItem())
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
  fun loadSubsequentPage_returnsPageWithNonNullPrevKey() = runTest {
    testLoadReturnsPageWithNonNullPrevKeyOnSubsequentPage(
      pagingSourceFactory = { GenericPagingSource(apiCallMock) },
      setupMock = { page ->
        val mediaItems = listOf(MediaResponseItem("item1"), MediaResponseItem("item2"))
        coEvery { apiCallMock(page) } returns mediaItems
      },
      page = 2,
      expectedData = listOf(MediaResponseItem("item1"), MediaResponseItem("item2"))
    )
  }

  @Test
  fun loadPage_withEmptyResponse_returnsPageWithNullNextKey() = runTest {
    testLoadReturnsPageWithNullNextKeyOnEmptyResponse(
      pagingSourceFactory = { GenericPagingSource(apiCallMock) },
      setupMock = { page ->
        coEvery { apiCallMock(page) } returns emptyList<MediaResponseItem>()
      },
      expectedData = emptyList()
    )
  }

  @Test
  fun getRefreshKey_whenAnchorInMiddlePage_returnsCorrectKey() {
    testRefreshKeyWithAnchorInMiddlePage(
      pagingSource = GenericPagingSource(apiCallMock),
      data = listOf(
        listOf(MediaResponseItem("item1"), MediaResponseItem("item2")),
        listOf(MediaResponseItem("item3"), MediaResponseItem("item4"))
      ),
      prevKeys = listOf(null, 1),
      nextKeys = listOf(2, 3),
      anchorPosition = 2, // Anchor in the middle of the second page
      expectedRefreshKey = 2 // Expected nextKey of the first page
    )
  }

  @Test
  fun getRefreshKey_whenBothKeysPresent_shouldUsePrevKeyPlusOne() {
    testRefreshKeyUsesCorrectKey(
      pagingSource = GenericPagingSource(apiCallMock),
      data = listOf(MediaResponseItem("item1")),
      anchorPosition = 0,
      prevKey = 1,
      nextKey = 3,
      expectedRefreshKey = 2 // prevKey + 1
    )
  }

  @Test
  fun getRefreshKey_whenPrevKeyIsNull_shouldUseNextKeyMinusOne() {
    testRefreshKeyUsesCorrectKey(
      pagingSource = GenericPagingSource(apiCallMock),
      data = listOf(MediaResponseItem("item1")),
      anchorPosition = 0,
      prevKey = null,
      nextKey = 3,
      expectedRefreshKey = 2 // nextKey - 1
    )
  }

  @Test
  fun getRefreshKey_whenAllKeysAreNull_returnsNull() {
    testRefreshKeyAllKeysNull(
      pagingSource = GenericPagingSource(apiCallMock),
      data = listOf(MediaResponseItem("item1"))
    )
  }

  @Test
  fun getRefreshKey_whenNoAnchorPositionAndEmptyPages_returnsNull() {
    testRefreshKeyEmptyList(
      pagingSource = GenericPagingSource(apiCallMock),
      anchorPosition = null
    )
  }

  @Test
  fun getRefreshKey_whenAnchorIsZeroAndEmptyPages_returnsNull() {
    testRefreshKeyEmptyList(
      pagingSource = GenericPagingSource(apiCallMock),
      anchorPosition = 0
    )
  }
}
