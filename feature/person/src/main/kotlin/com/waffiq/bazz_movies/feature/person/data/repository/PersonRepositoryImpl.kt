package com.waffiq.bazz_movies.feature.person.data.repository

import com.waffiq.bazz_movies.core.domain.Outcome
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
  override suspend fun getDetailPerson(id: Int): Flow<Outcome<DetailPerson>> =
    movieDataSource.getPersonDetail(id).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> Outcome.Success(networkResult.data.toDetailPerson())
        is NetworkResult.Error -> Outcome.Error(networkResult.message)
        is NetworkResult.Loading -> Outcome.Loading
      }
    }

  override suspend fun getKnownForPerson(id: Int): Flow<Outcome<CombinedCreditPerson>> =
    movieDataSource.getPersonKnownFor(id).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> Outcome.Success(networkResult.data.toCombinedCredit())
        is NetworkResult.Error -> Outcome.Error(networkResult.message)
        is NetworkResult.Loading -> Outcome.Loading
      }
    }

  override suspend fun getImagePerson(id: Int): Flow<Outcome<ImagePerson>> =
    movieDataSource.getPersonImage(id).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> Outcome.Success(networkResult.data.toImagePerson())
        is NetworkResult.Error -> Outcome.Error(networkResult.message)
        is NetworkResult.Loading -> Outcome.Loading
      }
    }

  override suspend fun getExternalIDPerson(id: Int): Flow<Outcome<ExternalIDPerson>> =
    movieDataSource.getPersonExternalID(id).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> Outcome.Success(networkResult.data.toExternalIDPerson())
        is NetworkResult.Error -> Outcome.Error(networkResult.message)
        is NetworkResult.Loading -> Outcome.Loading
      }
    }
}
