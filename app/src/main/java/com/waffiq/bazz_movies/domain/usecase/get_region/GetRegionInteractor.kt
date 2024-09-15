package com.waffiq.bazz_movies.domain.usecase.get_region

import com.waffiq.bazz_movies.domain.model.account.CountryIP
import com.waffiq.bazz_movies.domain.repository.IUserRepository
import com.waffiq.bazz_movies.utils.resultstate.NetworkResult
import kotlinx.coroutines.flow.Flow

class GetRegionInteractor(
  private val getRegionRepository: IUserRepository
) : GetRegionUseCase {
  override suspend fun getCountryCode(): Flow<NetworkResult<CountryIP>> =
    getRegionRepository.getCountryCode()
}
