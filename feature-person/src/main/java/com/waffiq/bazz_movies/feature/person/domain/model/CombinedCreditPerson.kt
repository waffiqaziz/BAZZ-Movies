package com.waffiq.bazz_movies.feature.person.domain.model

data class CombinedCreditPerson(
  val cast: List<CastItem>? = null,
  val id: Int? = null,
  val crew: List<CrewItem>? = null
)
