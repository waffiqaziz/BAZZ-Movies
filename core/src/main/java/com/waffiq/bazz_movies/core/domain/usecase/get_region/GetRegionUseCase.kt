package com.waffiq.bazz_movies.core.domain.usecase.get_region

import com.waffiq.bazz_movies.core.domain.model.account.CountryIP
import com.waffiq.bazz_movies.core.utils.result.NetworkResult
import kotlinx.coroutines.flow.Flow

interface GetRegionUseCase {
  suspend fun getCountryCode(): Flow<NetworkResult<CountryIP>>
}
