package com.waffiq.bazz_movies.core.network.utils.result

/**
 * A sealed class that represents the possible outcomes of a network operation for data layer.
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
sealed class NetworkResult<out T> {
  data class Success<out T>(val data: T) : NetworkResult<T>()
  data class Error(val message: String) : NetworkResult<Nothing>()
  data object Loading : NetworkResult<Nothing>()
}
