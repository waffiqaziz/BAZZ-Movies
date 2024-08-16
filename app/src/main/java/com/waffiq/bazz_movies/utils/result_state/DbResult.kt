package com.waffiq.bazz_movies.utils.result_state

sealed class DbResult<out T> {
  data class Success<out T>(val data: T) : DbResult<T>()
  data class Error(val errorMessage: String) : DbResult<Nothing>()
}