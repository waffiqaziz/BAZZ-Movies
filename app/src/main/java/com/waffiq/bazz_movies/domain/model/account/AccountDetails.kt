package com.waffiq.bazz_movies.domain.model.account

data class AccountDetails(
  val includeAdult: Boolean? = null,
  val iso31661: String? = null,
  val name: String? = null,
  val avatarItem: AvatarItem? = null,
  val id: Int? = null,
  val iso6391: String? = null,
  val username: String? = null
)