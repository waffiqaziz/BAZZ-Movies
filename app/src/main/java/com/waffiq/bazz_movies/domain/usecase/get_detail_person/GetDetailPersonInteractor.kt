package com.waffiq.bazz_movies.domain.usecase.get_detail_person

import com.waffiq.bazz_movies.domain.model.person.CastItem
import com.waffiq.bazz_movies.domain.model.person.DetailPerson
import com.waffiq.bazz_movies.domain.model.person.ExternalIDPerson
import com.waffiq.bazz_movies.domain.model.person.ImagePerson
import com.waffiq.bazz_movies.domain.repository.IMoviesRepository
import com.waffiq.bazz_movies.utils.resultstate.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class GetDetailPersonInteractor @Inject constructor(
  private val getDetailPersonRepository: IMoviesRepository
) : GetDetailPersonUseCase {
  override suspend fun getDetailPerson(id: Int): Flow<NetworkResult<DetailPerson>> =
    getDetailPersonRepository.getDetailPerson(id)

  override suspend fun getKnownForPerson(id: Int): Flow<NetworkResult<List<CastItem>>> =
    getDetailPersonRepository.getKnownForPerson(id).mapNotNull { networkResult ->
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
    getDetailPersonRepository.getImagePerson(id)

  override suspend fun getExternalIDPerson(id: Int): Flow<NetworkResult<ExternalIDPerson>> =
    getDetailPersonRepository.getExternalIDPerson(id)
}
