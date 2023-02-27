package com.waffiq.bazz_movies.data.local.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
  var name: String,
  var username: String,
  var password: String,
  var userId: Int,
  var token: String,
  var isLogin: Boolean,
  var gravatarHast: String
): Parcelable