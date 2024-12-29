package com.waffiq.bazz_movies.feature.person.domain.repository

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.feature.person.domain.model.CombinedCreditPerson
import com.waffiq.bazz_movies.feature.person.domain.model.DetailPerson
import com.waffiq.bazz_movies.feature.person.domain.model.ExternalIDPerson
import com.waffiq.bazz_movies.feature.person.domain.model.ImagePerson
import kotlinx.coroutines.flow.Flow

interface IPersonRepository {
  suspend fun getDetailPerson(id: Int): Flow<Outcome<DetailPerson>>
  suspend fun getKnownForPerson(id: Int): Flow<Outcome<CombinedCreditPerson>>
  suspend fun getImagePerson(id: Int): Flow<Outcome<ImagePerson>>
  suspend fun getExternalIDPerson(id: Int): Flow<Outcome<ExternalIDPerson>>
}
