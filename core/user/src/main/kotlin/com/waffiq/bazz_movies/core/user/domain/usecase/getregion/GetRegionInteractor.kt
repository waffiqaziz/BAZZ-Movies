package com.waffiq.bazz_movies.core.user.domain.usecase.getregion

import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import com.waffiq.bazz_movies.core.user.domain.model.account.CountryIP
import com.waffiq.bazz_movies.core.user.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRegionInteractor @Inject constructor(
  private val getRegionRepository: IUserRepository
) : GetRegionUseCase {
  override suspend fun getCountryCode(): Flow<NetworkResult<CountryIP>> =
    getRegionRepository.getCountryCode()
}
