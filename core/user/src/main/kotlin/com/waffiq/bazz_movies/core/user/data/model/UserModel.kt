package com.waffiq.bazz_movies.core.user.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
  val userId: Int,
  val name: String,
  val username: String,
  val password: String,
  val region: String,
  val token: String,
  val isLogin: Boolean,
  val gravatarHast: String?,
  val tmdbAvatar: String?
) : Parcelable
