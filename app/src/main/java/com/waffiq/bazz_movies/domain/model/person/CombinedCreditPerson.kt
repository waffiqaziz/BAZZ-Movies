package com.waffiq.bazz_movies.domain.model.person

data class CombinedCreditPerson(
  val cast: List<CastItem>? = null,
  val id: Int? = null,
  val crew: List<CrewItem>? = null
)
