package com.waffiq.bazz_movies.core.network.testutils

import androidx.paging.PagingSource.LoadResult
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.MediaResponseItem
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.Flow

/**
 * Utility helper object for testing paging-related functionality in unit tests.
 *
 * This class provides assertion methods for validating:
 * - Paging data structure and size
 * - Paging keys (prev/next)
 * - Item content in paginated flow results
 *
 * Designed for testing classes that implement the Paging 3 library (e.g., PagingSource).
 */
object PagingDataSourceTestHelper {

  /**
   * Asserts basic properties of a [LoadResult.Page] result from a PagingSource.
   *
   * @param page The page of data to assert.
   * @param expectedSize The expected number of items in the page.
   * @param expectedTitle Optional. The expected title or name of the first item.
   * @param expectedId Optional. The expected ID of the first item.
   */
  fun assertPageBasics(
    page: LoadResult.Page<Int, MediaResponseItem>,
    expectedSize: Int,
    expectedTitle: String? = null,
    expectedId: Int? = null,
  ) {
    assertEquals(expectedSize, page.data.size)
    expectedTitle?.let { assertEquals(it, page.data[0].title ?: page.data[0].name) }
    expectedId?.let { assertEquals(it, page.data[0].id) }
  }

  /**
   * Asserts the previous and next paging keys for a given page result.
   *
   * @param page The page of data to assert.
   * @param prevKey The expected previous key, or null.
   * @param nextKey The expected next key, or null.
   */
  fun assertPagingKeys(
    page: LoadResult.Page<Int, MediaResponseItem>,
    prevKey: Int?,
    nextKey: Int?,
  ) {
    assertEquals(prevKey, page.prevKey)
    assertEquals(nextKey, page.nextKey)
  }

  /**
   * Asserts the contents of a list of paginated items collected from a [Flow].
   *
   * @param items The list of items emitted by a paging flow.
   * @param expectedItem The expected item at the given index.
   * @param expectedId The expected ID of the item at the given index.
   * @param index Optional. The index to check. Defaults to 0 (first item).
   */
  fun assertPagingFlowBasics(
    items: List<MediaResponseItem>,
    expectedItem: MediaResponseItem,
    expectedId: Int,
    index: Int = 0,
  ) {
    assertEquals(expectedItem, items[index])
    assertTrue(items.isNotEmpty())
    assertEquals(expectedId, items[index].id)
  }
}
