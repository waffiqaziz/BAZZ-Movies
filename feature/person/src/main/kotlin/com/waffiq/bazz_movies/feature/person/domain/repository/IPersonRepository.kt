package com.waffiq.bazz_movies.feature.person.domain.repository

import com.waffiq.bazz_movies.core.common.Outcome
import com.waffiq.bazz_movies.feature.person.domain.model.DetailPerson
import kotlinx.coroutines.flow.Flow

fun interface IPersonRepository {
  fun getDetailPerson(id: Int): Flow<Outcome<DetailPerson>>
}
