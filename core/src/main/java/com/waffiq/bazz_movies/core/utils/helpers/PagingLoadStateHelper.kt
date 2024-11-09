package com.waffiq.bazz_movies.core.utils.helpers

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Used to handle error on PagingData
 */
object PagingLoadStateHelper {
  fun pagingErrorHandling(error: Throwable): String =
    when (error) {
      is SocketTimeoutException -> "Connection timed out. Please try again."
      is UnknownHostException -> "Unable to resolve server hostname. Please check your internet connection."
      is IOException -> "Please check your network connection"
      else -> "Something went wrong"
    }

  fun pagingErrorState(loadState: CombinedLoadStates): LoadState.Error? =
    when {
      loadState.append is LoadState.Error -> loadState.append as LoadState.Error
      loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
      loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
      else -> null
    }
}
