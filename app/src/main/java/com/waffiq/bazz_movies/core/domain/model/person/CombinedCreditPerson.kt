package com.waffiq.bazz_movies.core.domain.model.person

data class CombinedCreditPerson(
  val cast: List<CastItem>? = null,
  val id: Int? = null,
  val crew: List<CrewItem>? = null
)
