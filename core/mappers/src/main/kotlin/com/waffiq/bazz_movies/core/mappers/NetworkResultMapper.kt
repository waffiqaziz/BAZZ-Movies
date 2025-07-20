package com.waffiq.bazz_movies.core.mappers

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


object NetworkResultMapper {

  /**
   * Maps a [Flow] of [NetworkResult] into a [Flow] of [Outcome] using the provided [mapper].
   *
   * - Converts [NetworkResult.Success] to [Outcome.Success] with mapped data.
   * - Converts [NetworkResult.Error] to [Outcome.Error] with the error message.
   * - Converts [NetworkResult.Loading] to [Outcome.Loading].
   *
   * @param mapper A function to transform the success data.
   * @return A [Flow] emitting [Outcome] states corresponding to the original [NetworkResult] states.
   */
  fun <T, R> Flow<NetworkResult<T>>.toOutcome(mapper: (T) -> R): Flow<Outcome<R>> =
    map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> Outcome.Success(mapper(networkResult.data))
        is NetworkResult.Error -> Outcome.Error(networkResult.message)
        is NetworkResult.Loading -> Outcome.Loading
      }
    }
}
