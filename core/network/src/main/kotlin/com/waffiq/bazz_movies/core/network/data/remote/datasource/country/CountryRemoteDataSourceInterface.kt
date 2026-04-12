package com.waffiq.bazz_movies.core.network.data.remote.datasource.country

import com.waffiq.bazz_movies.core.network.data.remote.responses.countryip.CountryIPResponse
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import kotlinx.coroutines.flow.Flow

fun interface CountryRemoteDataSourceInterface {
  fun getCountryCode(): Flow<NetworkResult<CountryIPResponse>>
}
