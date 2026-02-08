package com.waffiq.bazz_movies.core.network.data.remote.retrofit.adapter

import android.util.Log
import androidx.annotation.VisibleForTesting
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.state.RatedResponse

/**
 * Adapter to handle custom serialization and deserialization of the `RatedResponse` sealed class
 * for use with Moshi. This adapter maps JSON responses to their respective `RatedResponse` types
 * and ensures safe handling of malformed or unexpected data.
 *
 * Response JSON References:
 * - [movie state](https://developer.themoviedb.org/reference/movie-account-states)
 * - [tv state](https://developer.themoviedb.org/reference/tv-series-account-states)
 *
 * Type of response:
 * - [RatedResponse.Value]: Represents a rating value when the JSON contains a valid numeric
 * - [RatedResponse.Unrated]: Represents an unrated state when the JSON contains a boolean `false`
 *                            or when the data cannot be interpreted.
 *
 * This adapter avoids throwing exceptions on unexpected data by logging errors and returning a
 * fallback value. It ensures the app does not crash due to invalid API responses.
 */
class RatedResponseAdapter {

  /**
   * Deserializes the JSON field `rated` into a `RatedResponse`.
   *
   * @param rated The raw JSON value for the `rated` field. Can be a map, boolean, or unknown type.
   * @return A [RatedResponse] object (`Value` or `Unrated`) depending on the input data.
   */
  @FromJson
  @Suppress("Unused")
  fun fromJson(rated: Any?): RatedResponse =
    when (rated) {
      is Map<*, *> -> {
        // handle cases where `rated` is a map containing a numeric `value`
        val value = rated["value"] as? Double
        if (value != null) {
          RatedResponse.Value(value)
        } else {
          // log and return fallback for invalid or missing `value`
          logError("Invalid 'value' in rated field: $rated")
          RatedResponse.Unrated
        }
      }

      is Boolean -> {
        // handle cases where `rated` is a boolean (e.g., false)
        RatedResponse.Unrated
      }

      else -> {
        // log and return fallback for unknown or unexpected data types
        logError("Unknown type for rated field: ${rated ?: "Null value"}")
        RatedResponse.Unrated
      }
    }

  /**
   * Serializes a [RatedResponse] object into its JSON representation.
   *
   * @param rated The [RatedResponse] object to serialize.
   * @return A JSON-compatible object (`Map` for `Value` or `false` for `Unrated`).
   */
  @ToJson
  @Suppress("Unused")
  fun toJson(rated: RatedResponse): Any =
    when (rated) {
      is RatedResponse.Value -> mapOf("value" to rated.value)
      is RatedResponse.Unrated -> false
    }

  /**
   * Logs an error message for unexpected or invalid data during deserialization.
   *
   * @param message The error message to log.
   */
  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  internal fun logError(message: String) {
    Log.e("RatedResponseAdapter", message)
  }
}
