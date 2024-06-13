package com.waffiq.bazz_movies.domain.model.account

import com.waffiq.bazz_movies.data.remote.responses.tmdb.account.AvatarTMDbResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.account.GravatarResponse

data class AvatarItem(
  val avatarTMDb: AvatarTMDbResponse? = null,
  val gravatar: GravatarResponse? = null
)