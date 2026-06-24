package com.waffiq.bazz_movies.feature.person.domain.repository

import com.waffiq.bazz_movies.core.models.Outcome
import com.waffiq.bazz_movies.feature.person.domain.model.DetailPerson
import kotlinx.coroutines.flow.Flow

fun interface IPersonRepository {
  fun getDetailPerson(id: Int): Flow<Outcome<DetailPerson>>
}
