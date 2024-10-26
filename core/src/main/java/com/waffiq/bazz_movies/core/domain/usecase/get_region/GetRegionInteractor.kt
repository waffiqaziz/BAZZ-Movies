package com.waffiq.bazz_movies.core.domain.usecase.get_region

import com.waffiq.bazz_movies.core.domain.model.account.CountryIP
import com.waffiq.bazz_movies.core.domain.repository.IUserRepository
import com.waffiq.bazz_movies.core.utils.result.NetworkResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRegionInteractor @Inject constructor(
  private val getRegionRepository: IUserRepository
) : GetRegionUseCase {
  override suspend fun getCountryCode(): Flow<NetworkResult<CountryIP>> =
    getRegionRepository.getCountryCode()
}
