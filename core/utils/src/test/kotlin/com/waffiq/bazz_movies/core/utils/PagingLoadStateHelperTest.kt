package com.waffiq.bazz_movies.core.utils

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class PagingLoadStateHelperTest {

  private val loadState = mockk<CombinedLoadStates>()

  @Test
  fun testPagingErrorHandling_socketTimeoutException() {
    val exception = SocketTimeoutException("Timeout")
    val result = PagingLoadStateHelper.pagingErrorHandling(exception)
    assertEquals("Connection timed out. Please try again.", result)
  }

  @Test
  fun testPagingErrorHandling_unknownHostException() {
    val exception = UnknownHostException("Unknown Host")
    val result = PagingLoadStateHelper.pagingErrorHandling(exception)
    assertEquals(
      "Unable to resolve server hostname. Please check your internet connection.",
      result
    )
  }

  @Test
  fun testPagingErrorHandling_ioException() {
    val exception = IOException("IO error")
    val result = PagingLoadStateHelper.pagingErrorHandling(exception)
    assertEquals("Please check your network connection", result)
  }

  @Test
  fun testPagingErrorHandling_genericException() {
    val exception = Exception("Generic error")
    val result = PagingLoadStateHelper.pagingErrorHandling(exception)
    assertEquals("Something went wrong", result)
  }

  @Test
  fun testPagingErrorState_appendError() {
    val appendError = mockk<LoadState.Error>()
    every { loadState.append } returns appendError
    every { appendError.error } returns IOException("Append error")

    val result = PagingLoadStateHelper.pagingErrorState(loadState)
    assertEquals(appendError, result)
  }

  @Test
  fun testPagingErrorState_prependError() {
    val prependError = LoadState.Error(SocketTimeoutException("Prepend error"))

    // mock that prepend returns a LoadState.Error, while others are not loading
    every { loadState.prepend } returns prependError
    every { loadState.append } returns LoadState.NotLoading(false)
    every { loadState.refresh } returns LoadState.NotLoading(false)

    val result = PagingLoadStateHelper.pagingErrorState(loadState)
    assertEquals(prependError, result)
  }

  @Test
  fun testPagingErrorState_refreshError() {
    val refreshError = LoadState.Error(UnknownHostException("Refresh error"))

    every { loadState.refresh } returns refreshError
    every { loadState.append } returns LoadState.NotLoading(false) // Not loading for append
    every { loadState.prepend } returns LoadState.NotLoading(false) // Not loading for prepend

    val result = PagingLoadStateHelper.pagingErrorState(loadState)
    assertEquals(refreshError, result)
  }

  @Test
  fun testPagingErrorState_noError() {
    val notLoadingState = mockk<LoadState.NotLoading>()

    every { loadState.append } returns notLoadingState
    every { loadState.prepend } returns notLoadingState
    every { loadState.refresh } returns notLoadingState

    val result = PagingLoadStateHelper.pagingErrorState(loadState)
    assertEquals(null, result)
  }
}
