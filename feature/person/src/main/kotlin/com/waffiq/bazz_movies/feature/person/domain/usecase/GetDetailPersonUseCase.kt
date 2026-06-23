package com.waffiq.bazz_movies.feature.person.domain.usecase

import com.waffiq.bazz_movies.core.models.Outcome
import com.waffiq.bazz_movies.feature.person.domain.model.DetailPerson
import kotlinx.coroutines.flow.Flow

fun interface GetDetailPersonUseCase {
  fun getDetailPerson(id: Int): Flow<Outcome<DetailPerson>>
}
