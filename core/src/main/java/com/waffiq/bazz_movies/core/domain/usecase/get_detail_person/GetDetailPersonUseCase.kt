package com.waffiq.bazz_movies.core.domain.usecase.get_detail_person

import com.waffiq.bazz_movies.core.domain.model.person.CastItem
import com.waffiq.bazz_movies.core.domain.model.person.DetailPerson
import com.waffiq.bazz_movies.core.domain.model.person.ExternalIDPerson
import com.waffiq.bazz_movies.core.domain.model.person.ImagePerson
import com.waffiq.bazz_movies.core.utils.result.NetworkResult
import kotlinx.coroutines.flow.Flow

interface GetDetailPersonUseCase {
  suspend fun getDetailPerson(id: Int): Flow<NetworkResult<DetailPerson>>
  suspend fun getKnownForPerson(id: Int): Flow<NetworkResult<List<CastItem>>>
  suspend fun getImagePerson(id: Int): Flow<NetworkResult<ImagePerson>>
  suspend fun getExternalIDPerson(id: Int): Flow<NetworkResult<ExternalIDPerson>>
}