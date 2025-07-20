package com.waffiq.bazz_movies.feature.person.data.repository

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.mappers.NetworkResultMapper.toOutcome
import com.waffiq.bazz_movies.core.network.data.remote.datasource.MovieDataSource
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
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersonRepositoryImpl @Inject constructor(
  private val movieDataSource: MovieDataSource,
) : IPersonRepository {
  override suspend fun getDetailPerson(id: Int): Flow<Outcome<DetailPerson>> =
    movieDataSource.getPersonDetail(id).toOutcome { it.toDetailPerson() }

  override suspend fun getKnownForPerson(id: Int): Flow<Outcome<CombinedCreditPerson>> =
    movieDataSource.getPersonKnownFor(id).toOutcome { it.toCombinedCredit() }

  override suspend fun getImagePerson(id: Int): Flow<Outcome<ImagePerson>> =
    movieDataSource.getPersonImage(id).toOutcome { it.toImagePerson() }

  override suspend fun getExternalIDPerson(id: Int): Flow<Outcome<ExternalIDPerson>> =
    movieDataSource.getPersonExternalID(id).toOutcome { it.toExternalIDPerson() }
}
