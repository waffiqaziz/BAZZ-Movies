package com.waffiq.bazz_movies.core.user.domain.usecase.get_region

import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import com.waffiq.bazz_movies.core.user.domain.model.account.CountryIP
import kotlinx.coroutines.flow.Flow

interface GetRegionUseCase {
  suspend fun getCountryCode(): Flow<NetworkResult<CountryIP>>
}
