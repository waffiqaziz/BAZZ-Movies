package com.waffiq.bazz_movies.domain.usecase.get_detail_person

import com.waffiq.bazz_movies.domain.model.person.CastItem
import com.waffiq.bazz_movies.domain.model.person.CombinedCreditPerson
import com.waffiq.bazz_movies.domain.model.person.DetailPerson
import com.waffiq.bazz_movies.domain.model.person.ExternalIDPerson
import com.waffiq.bazz_movies.domain.model.person.ImagePerson
import com.waffiq.bazz_movies.domain.repository.IMoviesRepository
import com.waffiq.bazz_movies.utils.NetworkResult
import com.waffiq.bazz_movies.utils.Status
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetDetailPersonInterector(
  private val getDetailPersonRepository: IMoviesRepository
) : GetDetailPersonUseCase {
  override suspend fun getDetailPerson(id: Int): Flow<NetworkResult<DetailPerson>> =
    getDetailPersonRepository.getDetailPerson(id)

  override suspend fun getKnownForPerson(id: Int): Flow<NetworkResult<List<CastItem>>> =
    getDetailPersonRepository.getKnownForPerson(id).map { result ->
      when (result.status) {
        Status.SUCCESS -> NetworkResult.success(result.data?.cast?.sortedByDescending { it.voteCount })
        Status.ERROR -> NetworkResult.error(result.message ?: "Unknown error")
        Status.LOADING -> NetworkResult.loading()
      }
    }

  override suspend fun getImagePerson(id: Int): Flow<NetworkResult<ImagePerson>> =
    getDetailPersonRepository.getImagePerson(id)

  override suspend fun getExternalIDPerson(id: Int): Flow<NetworkResult<ExternalIDPerson>> =
    getDetailPersonRepository.getExternalIDPerson(id)
}