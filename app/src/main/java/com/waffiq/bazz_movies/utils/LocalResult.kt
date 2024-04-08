package com.waffiq.bazz_movies.utils

sealed class LocalResult {
  data object Success : LocalResult()
  data class Error(val message: String) : LocalResult()
}