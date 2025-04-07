package com.waffiq.bazz_movies.core.network.testutils

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.common.truth.Truth.assertThat
import com.waffiq.bazz_movies.core.network.utils.common.Constants.INITIAL_PAGE_INDEX
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.fail
import kotlinx.coroutines.test.runTest
import retrofit2.HttpException

object PagingSourceTestHelper {

  fun <T : Any> testLoadReturnsPage(
    pagingSourceFactory: () -> PagingSource<Int, T>,
    setupMock: suspend () -> Unit,
    params: PagingSource.LoadParams<Int>,
    expectedData: List<T>,
    expectedPrevKey: Int?,
    expectedNextKey: Int?
  ) = runTest {
    val pagingSource = pagingSourceFactory()
    setupMock()

    val result = pagingSource.load(params)
    val pageResult = result as PagingSource.LoadResult.Page
    assertEquals(expectedData, pageResult.data)
    assertEquals(expectedPrevKey, pageResult.prevKey)
    assertEquals(expectedNextKey, pageResult.nextKey)
  }

  fun <T : Any> testLoadReturnsErrorOnException(
    pagingSourceFactory: () -> PagingSource<Int, T>,
    setupMock: suspend () -> Unit,
    params: PagingSource.LoadParams<Int>,
    expectedException: Class<out Throwable>
  ) = runTest {
    val pagingSource = pagingSourceFactory()
    setupMock()

    val result = pagingSource.load(params)
    if (result is PagingSource.LoadResult.Error) {
      assertThat(result.throwable).isInstanceOf(expectedException)
    } else {
      fail("Expected LoadResult.Error but got $result")
    }
  }

  fun <T : Any> testLoadReturnsErrorOnHttpException(
    pagingSourceFactory: () -> PagingSource<Int, T>,
    setupMock: suspend () -> Unit,
    params: PagingSource.LoadParams<Int>,
    expectedMessage: String
  ) = runTest {
    val pagingSource = pagingSourceFactory()
    setupMock()

    val result = pagingSource.load(params)
    assertThat(result).isInstanceOf(PagingSource.LoadResult.Error::class.java)
    val errorResult = result as PagingSource.LoadResult.Error
    assertThat(errorResult.throwable).isInstanceOf(HttpException::class.java)
    assertEquals(errorResult.throwable.message, expectedMessage)
  }

  fun <T : Any> testLoadReturnsPageWithNonNullPrevKeyOnSubsequentPage(
    pagingSourceFactory: () -> PagingSource<Int, T>,
    setupMock: suspend (Int) -> Unit,
    page: Int,
    expectedData: List<T>
  ) = runTest {
    val pagingSource = pagingSourceFactory()
    setupMock(page)

    val result = pagingSource.load(PagingSource.LoadParams.Refresh(page, 2, false))
    val pageResult = result as PagingSource.LoadResult.Page
    assertEquals(pageResult.data, expectedData)
    assertEquals(pageResult.prevKey, page - 1) // verify prevKey for the subsequent page
    assertEquals(pageResult.nextKey, page + 1) // verify nextKey for the subsequent page
  }

  fun <T : Any> testLoadReturnsPageWithNullNextKeyOnEmptyResponse(
    pagingSourceFactory: () -> PagingSource<Int, T>,
    setupMock: suspend (Int) -> Unit,
    expectedData: List<T>
  ) = runTest {
    val pagingSource = pagingSourceFactory()
    setupMock(INITIAL_PAGE_INDEX)

    val result = pagingSource.load(PagingSource.LoadParams.Refresh(INITIAL_PAGE_INDEX, 2, false))
    val pageResult = result as PagingSource.LoadResult.Page
    assertEquals(pageResult.data, expectedData)
    assertNull(pageResult.prevKey)
    assertNull(pageResult.nextKey) // null nextKey when response is empty
  }

  fun <T : Any> testRefreshKeyWithAnchorInMiddlePage(
    pagingSource: PagingSource<Int, T>,
    data: List<List<T>>, // each inner list represents a page's data
    prevKeys: List<Int?>, // corresponding prevKeys for the pages
    nextKeys: List<Int?>, // corresponding nextKeys for the pages
    anchorPosition: Int,
    expectedRefreshKey: Int?
  ) {
    val pages = data.zip(prevKeys.zip(nextKeys)) { pageData, keys ->
      PagingSource.LoadResult.Page(
        data = pageData,
        prevKey = keys.first,
        nextKey = keys.second
      )
    }
    val state = PagingState(
      pages = pages,
      anchorPosition = anchorPosition,
      config = PagingConfig(pageSize = data.firstOrNull()?.size ?: 1),
      leadingPlaceholderCount = 0
    )
    val refreshKey = pagingSource.getRefreshKey(state)
    assertEquals(expectedRefreshKey, refreshKey)
  }

  fun <T : Any> testRefreshKeyUsesCorrectKey(
    pagingSource: PagingSource<Int, T>,
    data: List<T>,
    anchorPosition: Int,
    prevKey: Int?,
    nextKey: Int?,
    expectedRefreshKey: Int?
  ) {
    val state = PagingState(
      pages = listOf(
        PagingSource.LoadResult.Page(
          data = data,
          prevKey = prevKey,
          nextKey = nextKey
        )
      ),
      anchorPosition = anchorPosition,
      config = PagingConfig(pageSize = 1),
      leadingPlaceholderCount = 0
    )
    val refreshKey = pagingSource.getRefreshKey(state)
    assertEquals(expectedRefreshKey, refreshKey)
  }

  fun <T : Any> testRefreshKeyAllKeysNull(
    pagingSource: PagingSource<Int, T>,
    data: List<T>
  ) {
    val state = PagingState(
      pages = listOf(
        PagingSource.LoadResult.Page(
          data = data,
          prevKey = null as Int?, // Explicitly set prevKey to null
          nextKey = null as Int?  // Explicitly set nextKey to null
        )
      ),
      anchorPosition = 0,
      config = PagingConfig(pageSize = 1),
      leadingPlaceholderCount = 0
    )
    val refreshKey = pagingSource.getRefreshKey(state)
    assertNull(refreshKey)
  }

  fun <T : Any> testRefreshKeyEmptyList(
    pagingSource: PagingSource<Int, T>,
    anchorPosition: Int? = 0
  ) {
    val state = PagingState<Int, T>(
      pages = emptyList(),
      anchorPosition = anchorPosition,
      config = PagingConfig(pageSize = 2),
      leadingPlaceholderCount = 0
    )
    assertNull(pagingSource.getRefreshKey(state))
  }
}
