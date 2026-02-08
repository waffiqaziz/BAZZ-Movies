package com.waffiq.bazz_movies.core.utils

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Utility object that handles error states related to PagingData.
 * It provides methods for error handling and extracting error information
 * from `PagingData` load states.
 */
object PagingLoadStateHelper {

  /**
   * Handles errors that occur during PagingData loading.
   * It provides specific error messages based on the type of exception.
   *
   * @param error The Throwable error encountered during data loading.
   * @return A user-friendly error message based on the type of exception.
   */
  fun pagingErrorHandling(error: Throwable): String =
    when (error) {
      is SocketTimeoutException -> "Connection timed out. Please try again."

      is UnknownHostException -> {
        "Unable to resolve server hostname. Please check your internet connection."
      }

      is IOException -> "Please check your network connection"

      else -> "Something went wrong"
    }

  /**
   * Checks the current `LoadState` for any errors that might have occurred
   * during the paging data load process (either refresh, append, or prepend).
   *
   * @param loadState The combined load states for the paging operation.
   * @return The error load state (LoadState.Error) if one exists, or null if there is no error.
   */
  fun pagingErrorState(loadState: CombinedLoadStates): LoadState.Error? =
    when {
      loadState.append is LoadState.Error -> loadState.append as LoadState.Error
      loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
      loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
      else -> null
    }
}
