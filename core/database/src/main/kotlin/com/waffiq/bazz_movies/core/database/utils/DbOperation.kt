package com.waffiq.bazz_movies.core.database.utils

import android.database.sqlite.SQLiteException
import android.util.Log

@Suppress("TooGenericExceptionCaught")
suspend fun <T> executeDbOperation(operation: suspend () -> T): DbResult<T> =
  try {
    DbResult.Success(operation())
  } catch (e: SQLiteException) {
    Log.e("DatabaseError", "SQLite error: ${e.message}")
    DbResult.Error("Database error")
  } catch (e: Exception) {
    Log.e("DatabaseError", "Unexpected error: ${e.message}")
    DbResult.Error("Unknown error")
  }
