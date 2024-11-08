package com.waffiq.bazz_movies.feature.person.data.repository

import com.waffiq.bazz_movies.core.network.data.remote.datasource.MovieDataSource
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import com.waffiq.bazz_movies.feature.person.domain.model.CombinedCreditPerson
import com.waffiq.bazz_movies.feature.person.domain.model.DetailPerson
import com.waffiq.bazz_movies.feature.person.domain.model.ExternalIDPerson
import com.waffiq.bazz_movies.feature.person.domain.model.ImagePerson
import com.waffiq.bazz_movies.feature.person.domain.repository.IPersonRepository
import com.waffiq.bazz_movies.feature.person.utils.mapper.PersonMapper.toCombinedCredit
import com.waffiq.bazz_movies.feature.person.utils.mapper.PersonMapper.toDetailPerson
import com.waffiq.bazz_movies.feature.person.utils.mapper.PersonMapper.toExternalIDPerson
import com.waffiq.bazz_movies.feature.person.utils.mapper.PersonMapper.toImagePerson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersonRepositoryImpl @Inject constructor(
  private val movieDataSource: MovieDataSource
) : IPersonRepository {
  override suspend fun getDetailPerson(id: Int): Flow<NetworkResult<DetailPerson>> =
    movieDataSource.getDetailPerson(id).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> NetworkResult.Success(networkResult.data.toDetailPerson())
        is NetworkResult.Error -> NetworkResult.Error(networkResult.message)
        is NetworkResult.Loading -> NetworkResult.Loading
      }
    }

  override suspend fun getKnownForPerson(id: Int): Flow<NetworkResult<CombinedCreditPerson>> =
    movieDataSource.getKnownForPerson(id).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> NetworkResult.Success(networkResult.data.toCombinedCredit())
        is NetworkResult.Error -> NetworkResult.Error(networkResult.message)
        is NetworkResult.Loading -> NetworkResult.Loading
      }
    }

  override suspend fun getImagePerson(id: Int): Flow<NetworkResult<ImagePerson>> =
    movieDataSource.getImagePerson(id).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> NetworkResult.Success(networkResult.data.toImagePerson())
        is NetworkResult.Error -> NetworkResult.Error(networkResult.message)
        is NetworkResult.Loading -> NetworkResult.Loading
      }
    }

  override suspend fun getExternalIDPerson(id: Int): Flow<NetworkResult<ExternalIDPerson>> =
    movieDataSource.getExternalIDPerson(id).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> NetworkResult.Success(networkResult.data.toExternalIDPerson())
        is NetworkResult.Error -> NetworkResult.Error(networkResult.message)
        is NetworkResult.Loading -> NetworkResult.Loading
      }
    }
}