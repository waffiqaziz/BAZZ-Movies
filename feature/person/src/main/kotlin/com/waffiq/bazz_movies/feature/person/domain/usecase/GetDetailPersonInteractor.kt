package com.waffiq.bazz_movies.feature.person.domain.usecase

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.feature.person.domain.model.CastItem
import com.waffiq.bazz_movies.feature.person.domain.model.DetailPerson
import com.waffiq.bazz_movies.feature.person.domain.model.ExternalIDPerson
import com.waffiq.bazz_movies.feature.person.domain.model.ImagePerson
import com.waffiq.bazz_movies.feature.person.domain.repository.IPersonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class GetDetailPersonInteractor @Inject constructor(
  private val personRepository: IPersonRepository,
) : GetDetailPersonUseCase {
  override fun getDetailPerson(id: Int): Flow<Outcome<DetailPerson>> =
    personRepository.getDetailPerson(id)

  override fun getKnownForPerson(id: Int): Flow<Outcome<List<CastItem>>> =
    personRepository.getKnownForPerson(id).mapNotNull { outcome ->
      when (outcome) {
        is Outcome.Success -> {
          outcome.data.cast?.let { castItems ->
            Outcome.Success(castItems.sortedByDescending { it.voteCount })
          }
        }

        is Outcome.Error -> Outcome.Error(outcome.message)
        is Outcome.Loading -> Outcome.Loading
      }
    }

  override fun getImagePerson(id: Int): Flow<Outcome<ImagePerson>> =
    personRepository.getImagePerson(id)

  override fun getExternalIDPerson(id: Int): Flow<Outcome<ExternalIDPerson>> =
    personRepository.getExternalIDPerson(id)
}
