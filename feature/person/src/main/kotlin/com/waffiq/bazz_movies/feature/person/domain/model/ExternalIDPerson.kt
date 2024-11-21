package com.waffiq.bazz_movies.feature.person.domain.model

data class ExternalIDPerson(
  val imdbId: String? = null,
  val freebaseMid: String? = null,
  val tiktokId: String? = null,
  val wikidataId: String? = null,
  val id: Int? = null,
  val freebaseId: String? = null,
  val twitterId: String? = null,
  val youtubeId: String? = null,
  val tvrageId: String? = null,
  val facebookId: String? = null,
  val instagramId: String? = null
)
