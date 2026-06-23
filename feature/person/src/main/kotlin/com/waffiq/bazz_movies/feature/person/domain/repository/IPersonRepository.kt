package com.waffiq.bazz_movies.feature.person.domain.repository

import com.waffiq.bazz_movies.core.models.Outcome
import com.waffiq.bazz_movies.feature.person.domain.model.DetailPerson
import com.waffiq.bazz_movies.feature.person.domain.model.ImagePerson
import kotlinx.coroutines.flow.Flow

interface IPersonRepository {
  fun getDetailPerson(id: Int): Flow<Outcome<DetailPerson>>
  fun getImagePerson(id: Int): Flow<Outcome<ImagePerson>>
}
