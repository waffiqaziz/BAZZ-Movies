package com.waffiq.bazz_movies.core.user.domain.usecase.getregion

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.user.domain.model.account.CountryIP
import com.waffiq.bazz_movies.core.user.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRegionInteractor @Inject constructor(
  private val getRegionRepository: IUserRepository
) : GetRegionUseCase {
  override fun getCountryCode(): Flow<Outcome<CountryIP>> =
    getRegionRepository.getCountryCode()
}
