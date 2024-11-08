package com.waffiq.bazz_movies.feature.person.domain.usecase

import com.waffiq.bazz_movies.feature.person.domain.model.CastItem
import com.waffiq.bazz_movies.feature.person.domain.model.DetailPerson
import com.waffiq.bazz_movies.feature.person.domain.model.ExternalIDPerson
import com.waffiq.bazz_movies.feature.person.domain.model.ImagePerson
import kotlinx.coroutines.flow.Flow

interface GetDetailPersonUseCase {
  suspend fun getDetailPerson(id: Int): Flow<com.waffiq.bazz_movies.core.network.utils.result.NetworkResult<DetailPerson>>
  suspend fun getKnownForPerson(id: Int): Flow<com.waffiq.bazz_movies.core.network.utils.result.NetworkResult<List<CastItem>>>
  suspend fun getImagePerson(id: Int): Flow<com.waffiq.bazz_movies.core.network.utils.result.NetworkResult<ImagePerson>>
  suspend fun getExternalIDPerson(id: Int): Flow<com.waffiq.bazz_movies.core.network.utils.result.NetworkResult<ExternalIDPerson>>
}
