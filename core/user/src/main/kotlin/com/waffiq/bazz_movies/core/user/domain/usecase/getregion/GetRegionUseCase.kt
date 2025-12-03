package com.waffiq.bazz_movies.core.user.domain.usecase.getregion

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.user.domain.model.account.CountryIP
import kotlinx.coroutines.flow.Flow

interface GetRegionUseCase {
  fun getCountryCode(): Flow<Outcome<CountryIP>>
}
