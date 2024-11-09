package com.waffiq.bazz_movies.feature.person.domain.usecase

import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import com.waffiq.bazz_movies.feature.person.domain.model.CastItem
import com.waffiq.bazz_movies.feature.person.domain.model.DetailPerson
import com.waffiq.bazz_movies.feature.person.domain.model.ExternalIDPerson
import com.waffiq.bazz_movies.feature.person.domain.model.ImagePerson
import com.waffiq.bazz_movies.feature.person.domain.repository.IPersonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class GetDetailPersonInteractor @Inject constructor(
  private val personRepository: IPersonRepository
) : GetDetailPersonUseCase {
  override suspend fun getDetailPerson(id: Int): Flow<NetworkResult<DetailPerson>> =
    personRepository.getDetailPerson(id)

  override suspend fun getKnownForPerson(id: Int): Flow<NetworkResult<List<CastItem>>> =
    personRepository.getKnownForPerson(id).mapNotNull { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> {
          networkResult.data.cast?.let { castItems ->
            NetworkResult.Success(castItems.sortedByDescending { it.voteCount })
          }
        }

        is NetworkResult.Error -> NetworkResult.Error(networkResult.message)
        is NetworkResult.Loading -> NetworkResult.Loading
      }
    }

  override suspend fun getImagePerson(id: Int): Flow<NetworkResult<ImagePerson>> =
    personRepository.getImagePerson(id)

  override suspend fun getExternalIDPerson(id: Int): Flow<NetworkResult<ExternalIDPerson>> =
    personRepository.getExternalIDPerson(id)
}
