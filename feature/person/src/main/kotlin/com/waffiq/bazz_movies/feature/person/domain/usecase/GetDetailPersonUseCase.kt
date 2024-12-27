package com.waffiq.bazz_movies.feature.person.domain.usecase

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.feature.person.domain.model.CastItem
import com.waffiq.bazz_movies.feature.person.domain.model.DetailPerson
import com.waffiq.bazz_movies.feature.person.domain.model.ExternalIDPerson
import com.waffiq.bazz_movies.feature.person.domain.model.ImagePerson
import kotlinx.coroutines.flow.Flow

interface GetDetailPersonUseCase {
  suspend fun getDetailPerson(id: Int): Flow<Outcome<DetailPerson>>
  suspend fun getKnownForPerson(id: Int): Flow<Outcome<List<CastItem>>>
  suspend fun getImagePerson(id: Int): Flow<Outcome<ImagePerson>>
  suspend fun getExternalIDPerson(id: Int): Flow<Outcome<ExternalIDPerson>>
}
