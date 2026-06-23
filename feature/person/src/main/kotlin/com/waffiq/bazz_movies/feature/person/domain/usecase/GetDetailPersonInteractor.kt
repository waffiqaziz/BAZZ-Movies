package com.waffiq.bazz_movies.feature.person.domain.usecase

import com.waffiq.bazz_movies.core.models.Outcome
import com.waffiq.bazz_movies.feature.person.domain.model.DetailPerson
import com.waffiq.bazz_movies.feature.person.domain.model.ImagePerson
import com.waffiq.bazz_movies.feature.person.domain.repository.IPersonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDetailPersonInteractor @Inject constructor(
  private val personRepository: IPersonRepository,
) : GetDetailPersonUseCase {
  override fun getDetailPerson(id: Int): Flow<Outcome<DetailPerson>> =
    personRepository.getDetailPerson(id)

  override fun getImagePerson(id: Int): Flow<Outcome<ImagePerson>> =
    personRepository.getImagePerson(id)
}
