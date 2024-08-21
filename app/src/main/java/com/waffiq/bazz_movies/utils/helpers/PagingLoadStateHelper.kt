package com.waffiq.bazz_movies.utils.helpers

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.waffiq.bazz_movies.utils.Helper
import okio.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object PagingLoadStateHelper {
  fun pagingErrorHandling(error: Throwable): String {
    return when (error) {
      is SocketTimeoutException -> "Connection timed out. Please try again."
      is UnknownHostException -> "Unable to resolve server hostname. Please check your internet connection."
      is IOException -> "Please check your network connection"
      else -> "Something went wrong"
    }
  }

  fun combinedLoadStatesHandle2(
    loadState: CombinedLoadStates
  ): String {
    val errorState = when { // If theres an error, show a toast
      loadState.append is LoadState.Error -> loadState.append as LoadState.Error
      loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
      loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
      else -> null
    }
    errorState?.let {
      return pagingErrorHandling(it.error)
    } ?: return ""
  }
}