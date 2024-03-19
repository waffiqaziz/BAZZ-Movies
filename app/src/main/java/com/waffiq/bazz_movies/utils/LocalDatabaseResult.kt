package com.waffiq.bazz_movies.utils

sealed class LocalDatabaseResult {
  data object Success : LocalDatabaseResult()
  data class Error(val message: String) : LocalDatabaseResult()
}