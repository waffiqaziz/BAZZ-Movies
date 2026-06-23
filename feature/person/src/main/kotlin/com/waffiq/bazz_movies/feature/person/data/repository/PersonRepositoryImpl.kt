package com.waffiq.bazz_movies.feature.person.data.repository

import com.waffiq.bazz_movies.core.mappers.NetworkResultMapper.toOutcome
import com.waffiq.bazz_movies.core.models.Outcome
import com.waffiq.bazz_movies.core.network.data.remote.datasource.person.PersonRemoteDataSource
import com.waffiq.bazz_movies.feature.person.domain.model.DetailPerson
import com.waffiq.bazz_movies.feature.person.domain.model.ImagePerson
import com.waffiq.bazz_movies.feature.person.domain.repository.IPersonRepository
import com.waffiq.bazz_movies.feature.person.utils.mapper.PersonMapper.toDetailPerson
import com.waffiq.bazz_movies.feature.person.utils.mapper.PersonMapper.toImagePerson
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersonRepositoryImpl @Inject constructor(
  private val personRemoteDataSource: PersonRemoteDataSource,
) : IPersonRepository {

  override fun getDetailPerson(id: Int): Flow<Outcome<DetailPerson>> =
    personRemoteDataSource.getPersonDetails(id).toOutcome { it.toDetailPerson() }

  override fun getImagePerson(id: Int): Flow<Outcome<ImagePerson>> =
    personRemoteDataSource.getPersonImages(id).toOutcome { it.toImagePerson() }
}
