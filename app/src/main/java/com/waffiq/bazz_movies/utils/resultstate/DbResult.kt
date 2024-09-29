package com.waffiq.bazz_movies.utils.resultstate

/**
 * A sealed class that represents the possible outcomes of a database operation.
 *
 * @param T The type of the data returned when the operation is successful.
 *
 * DbResult can have one of the following states:
 * - [Success]: Indicates that the database operation was successful, with the resulting data of type [T].
 * - [Error]: Represents a failure, containing an error message that describes the issue encountered
 *            during the operation.
 *
 * This sealed class allows for structured and type-safe handling of database operation results, ensuring that
 * all potential outcomes are considered and handled appropriately.
 */
sealed class DbResult<out T> {
  data class Success<out T>(val data: T) : DbResult<T>()
  data class Error(val errorMessage: String) : DbResult<Nothing>()
}