package com.waffiq.bazz_movies.core.domain

/**
 * A sealed class that represents the possible outcomes of a network operation for domain layer.
 *
 * @param T The type of the data returned when the operation is successful.
 *
 * NetworkResult can have one of the following states:
 * - [Success]: Indicates a successful network operation with the result data of type [T].
 * - [Error]: Represents a failure with an error message describing the issue.
 * - [Loading]: Represents an ongoing network operation, typically used to show a loading state.
 *
 * This sealed class helps in handling network responses in a type-safe and exhaustive manner,
 * ensuring that all possible states are handled in a structured way.
 */
sealed class Outcome<out T> {
  data class Success<out T>(val data: T) : Outcome<T>()
  data class Error(val message: String) : Outcome<Nothing>()
  data object Loading : Outcome<Nothing>()
}