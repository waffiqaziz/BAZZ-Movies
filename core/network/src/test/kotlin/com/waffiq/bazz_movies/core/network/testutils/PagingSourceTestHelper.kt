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

  /**
   * Helper function to test paging source load operations
   * @param pagingSourceFactory Factory function to create the paging source
   * @param setupMock Function to configure mocks before load operation
   * @param params Load parameters to pass to the paging source
   * @param expectedData Expected data that should be returned in the page
   * @param expectedPrevKey Expected previous key for pagination
   * @param expectedNextKey Expected next key for pagination
   */
  fun <T : Any> testLoadReturnsPage(
    pagingSourceFactory: () -> PagingSource<Int, T>,
    setupMock: suspend () -> Unit,
    params: PagingSource.LoadParams<Int>,
    expectedData: List<T>,
    expectedPrevKey: Int?,
    expectedNextKey: Int?,
  ) = runTest {
    // create paging source and setup mocks
    val pagingSource = pagingSourceFactory()
    setupMock()

    // perform load operation and verify results
    val result = pagingSource.load(params)
    val pageResult = result as PagingSource.LoadResult.Page
    assertEquals(expectedData, pageResult.data)
    assertEquals(expectedPrevKey, pageResult.prevKey)
    assertEquals(expectedNextKey, pageResult.nextKey)
  }

  /**
   * Helper function to tests paging source returns an error result when an exception occurs.
   *
   * @param pagingSourceFactory Factory function to create the paging source
   * @param setupMock Function to configure mocks before load operation
   * @param params Load parameters to pass to the paging source
   * @param expectedException Expected exception class that should be wrapped in the error result
   */
  fun <T : Any> testLoadReturnsErrorOnException(
    pagingSourceFactory: () -> PagingSource<Int, T>,
    setupMock: suspend () -> Unit,
    params: PagingSource.LoadParams<Int>,
    expectedException: Class<out Throwable>,
  ) = runTest {
    // create paging source and setup mocks
    val pagingSource = pagingSourceFactory()
    setupMock()

    // perform load operation and verify results
    val result = pagingSource.load(params)
    if (result is PagingSource.LoadResult.Error) {
      assertThat(result.throwable).isInstanceOf(expectedException)
    } else {
      fail("Expected LoadResult.Error but got $result")
    }
  }

  /**
   * Helper function to tests paging source returns an error result when an HTTP exception occurs.
   *
   * @param pagingSourceFactory Factory function to create the paging source
   * @param setupMock Function to configure mocks before load operation
   * @param params Load parameters to pass to the paging source
   * @param expectedMessage Expected error message that should be in the HTTP exception
   */
  fun <T : Any> testLoadReturnsErrorOnHttpException(
    pagingSourceFactory: () -> PagingSource<Int, T>,
    setupMock: suspend () -> Unit,
    params: PagingSource.LoadParams<Int>,
    expectedMessage: String,
  ) = runTest {
    // create paging source and setup mocks
    val pagingSource = pagingSourceFactory()
    setupMock()

    // perform load operation and verify results
    val result = pagingSource.load(params)
    assertThat(result).isInstanceOf(PagingSource.LoadResult.Error::class.java)
    val errorResult = result as PagingSource.LoadResult.Error
    assertThat(errorResult.throwable).isInstanceOf(HttpException::class.java)
    assertEquals(errorResult.throwable.message, expectedMessage)
  }

  /**
   * Helper function to tests paging source  returns a page with non-null prevKey
   * when loading a page after the initial page.
   *
   * @param pagingSourceFactory Factory function to create the paging source
   * @param setupMock Function to configure mocks for a specific page
   * @param page The page index to test
   * @param expectedData Expected data that should be returned in the page
   */
  fun <T : Any> testLoadReturnsPageWithNonNullPrevKeyOnSubsequentPage(
    pagingSourceFactory: () -> PagingSource<Int, T>,
    setupMock: suspend (Int) -> Unit,
    page: Int,
    expectedData: List<T>,
  ) = runTest {
    // create paging source and setup mocks
    val pagingSource = pagingSourceFactory()
    setupMock(page)

    // perform load operation and verify results
    val result = pagingSource.load(PagingSource.LoadParams.Refresh(page, 2, false))
    val pageResult = result as PagingSource.LoadResult.Page
    assertEquals(pageResult.data, expectedData)
    assertEquals(pageResult.prevKey, page - 1) // verify prevKey for the subsequent page
    assertEquals(pageResult.nextKey, page + 1) // verify nextKey for the subsequent page
  }

  /**
   * Helper function to tests paging source correctly returns a page with null nextKey
   * when the API response is empty.
   *
   * @param pagingSourceFactory Factory function to create the paging source
   * @param setupMock Function to configure mocks for the initial page
   * @param expectedData Expected data that should be returned in the page (typically empty)
   */
  fun <T : Any> testLoadReturnsPageWithNullNextKeyOnEmptyResponse(
    pagingSourceFactory: () -> PagingSource<Int, T>,
    setupMock: suspend (Int) -> Unit,
    expectedData: List<T>,
  ) = runTest {
    // create paging source and setup mocks
    val pagingSource = pagingSourceFactory()
    setupMock(INITIAL_PAGE_INDEX)

    // perform load operation and verify results
    val result = pagingSource.load(PagingSource.LoadParams.Refresh(INITIAL_PAGE_INDEX, 2, false))
    val pageResult = result as PagingSource.LoadResult.Page
    assertEquals(pageResult.data, expectedData)
    assertNull(pageResult.prevKey)
    assertNull(pageResult.nextKey) // null nextKey when response is empty
  }

  /**
   * Helper function to tests paging source correctly calculates the refresh key
   * when the anchor position is in a middle page.
   *
   * @param pagingSource The paging source to test
   * @param data List of page data, where each inner list represents one page's items
   * @param prevKeys List of previous keys corresponding to each page
   * @param nextKeys List of next keys corresponding to each page
   * @param anchorPosition Position in the combined data that should be used as anchor
   * @param expectedRefreshKey The refresh key that should be returned
   */
  fun <T : Any> testRefreshKeyWithAnchorInMiddlePage(
    pagingSource: PagingSource<Int, T>,
    data: List<List<T>>, // each inner list represents a page's data
    prevKeys: List<Int?>, // corresponding prevKeys for the pages
    nextKeys: List<Int?>, // corresponding nextKeys for the pages
    anchorPosition: Int,
    expectedRefreshKey: Int?,
  ) {
    // create paging state with multiple pages
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

    // verify refresh key calculation
    val refreshKey = pagingSource.getRefreshKey(state)
    assertEquals(expectedRefreshKey, refreshKey)
  }

  /**
   * Helper function to tests paging source correctly calculates the refresh key using the
   * appropriate key (prev or next) based on the anchor position.
   *
   * @param pagingSource The paging source to test
   * @param data The page data
   * @param anchorPosition Position in the data that should be used as anchor
   * @param prevKey Previous key value for the page
   * @param nextKey Next key value for the page
   * @param expectedRefreshKey The refresh key that should be returned
   */
  fun <T : Any> testRefreshKeyUsesCorrectKey(
    pagingSource: PagingSource<Int, T>,
    data: List<T>,
    anchorPosition: Int,
    prevKey: Int?,
    nextKey: Int?,
    expectedRefreshKey: Int?,
  ) {
    // create paging state with a single page
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

    // verify refresh key calculation
    val refreshKey = pagingSource.getRefreshKey(state)
    assertEquals(expectedRefreshKey, refreshKey)
  }

  /**
   * Helper function to tests paging source correctly returns null as the refresh key
   * when both prev and next keys are null.
   *
   * @param pagingSource The paging source to test
   * @param data The page data
   */
  fun <T : Any> testRefreshKeyAllKeysNull(
    pagingSource: PagingSource<Int, T>,
    data: List<T>,
  ) {
    // create paging state with a page that has null keys
    val state = PagingState(
      pages = listOf(
        PagingSource.LoadResult.Page(
          data = data,
          prevKey = null as Int?, // Explicitly set prevKey to null
          nextKey = null as Int? // Explicitly set nextKey to null
        )
      ),
      anchorPosition = 0,
      config = PagingConfig(pageSize = 1),
      leadingPlaceholderCount = 0
    )

    // verify refresh key is null
    val refreshKey = pagingSource.getRefreshKey(state)
    assertNull(refreshKey)
  }

  /**
   * Helper function to tests paging source correctly returns null as the refresh key
   * when the page list is empty.
   *
   * @param pagingSource The paging source to test
   * @param anchorPosition Position that should be used as anchor (typically null or 0 for empty list)
   */
  fun <T : Any> testRefreshKeyEmptyList(
    pagingSource: PagingSource<Int, T>,
    anchorPosition: Int? = 0,
  ) {
    val state = PagingState<Int, T>(
      pages = emptyList(),
      anchorPosition = anchorPosition,
      config = PagingConfig(pageSize = 2),
      leadingPlaceholderCount = 0
    )

    // verify refresh key is null for empty list
    assertNull(pagingSource.getRefreshKey(state))
  }
}
