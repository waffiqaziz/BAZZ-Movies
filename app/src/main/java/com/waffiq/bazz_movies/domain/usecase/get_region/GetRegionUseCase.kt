package com.waffiq.bazz_movies.domain.usecase.get_region

import com.waffiq.bazz_movies.domain.model.account.CountryIP
import com.waffiq.bazz_movies.utils.NetworkResult
import kotlinx.coroutines.flow.Flow

interface GetRegionUseCase {
  suspend fun getCountryCode() : Flow<NetworkResult<CountryIP>>
}