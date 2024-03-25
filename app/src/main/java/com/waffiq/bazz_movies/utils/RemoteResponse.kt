package com.waffiq.bazz_movies.utils

sealed class RemoteResponse<out T> {
  data class Success<out T>(val data: T) : RemoteResponse<T>()
  data class Error(val exception: Exception) : RemoteResponse<Nothing>()
  data object Loading : RemoteResponse<Nothing>()
  data object Empty : RemoteResponse<Nothing>()
}