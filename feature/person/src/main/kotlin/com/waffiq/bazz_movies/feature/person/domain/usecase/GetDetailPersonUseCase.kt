package com.waffiq.bazz_movies.feature.person.domain.usecase

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.feature.person.domain.model.CastItem
import com.waffiq.bazz_movies.feature.person.domain.model.DetailPerson
import com.waffiq.bazz_movies.feature.person.domain.model.ExternalIDPerson
import com.waffiq.bazz_movies.feature.person.domain.model.ImagePerson
import kotlinx.coroutines.flow.Flow

interface GetDetailPersonUseCase {
  fun getDetailPerson(id: Int): Flow<Outcome<DetailPerson>>
  fun getKnownForPerson(id: Int): Flow<Outcome<List<CastItem>>>
  fun getImagePerson(id: Int): Flow<Outcome<ImagePerson>>
  fun getExternalIDPerson(id: Int): Flow<Outcome<ExternalIDPerson>>
}
