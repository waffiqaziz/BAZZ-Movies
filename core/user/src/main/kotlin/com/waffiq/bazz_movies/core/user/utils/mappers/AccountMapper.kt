package com.waffiq.bazz_movies.core.user.utils.mappers

import com.waffiq.bazz_movies.core.domain.UserModel
import com.waffiq.bazz_movies.core.network.data.remote.responses.countryip.CountryIPResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account.AccountDetailsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account.AuthenticationResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account.AvatarItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account.AvatarTMDbResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account.CreateSessionResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account.GravatarResponse
import com.waffiq.bazz_movies.core.user.data.model.UserModelPref
import com.waffiq.bazz_movies.core.user.domain.model.account.AccountDetails
import com.waffiq.bazz_movies.core.user.domain.model.account.Authentication
import com.waffiq.bazz_movies.core.user.domain.model.account.AvatarItem
import com.waffiq.bazz_movies.core.user.domain.model.account.AvatarTMDb
import com.waffiq.bazz_movies.core.user.domain.model.account.CountryIP
import com.waffiq.bazz_movies.core.user.domain.model.account.CreateSession
import com.waffiq.bazz_movies.core.user.domain.model.account.Gravatar

object AccountMapper {

  fun UserModelPref.toUserModel() = UserModel(
    userId = userId,
    name = name,
    username = username,
    password = password,
    region = region,
    token = token,
    isLogin = isLogin,
    gravatarHast = gravatarHast,
    tmdbAvatar = tmdbAvatar,
  )

  fun UserModel.toUserModelPref() = UserModelPref(
    userId = userId,
    name = name,
    username = username,
    password = password,
    region = region,
    token = token,
    isLogin = isLogin,
    gravatarHast = gravatarHast,
    tmdbAvatar = tmdbAvatar,
  )

  fun AuthenticationResponse.toAuthentication() = Authentication(
    success = success,
    expireAt = expireAt,
    requestToken = requestToken
  )

  fun CreateSessionResponse.toCreateSession() = CreateSession(
    success = success,
    sessionId = sessionId
  )

  fun AccountDetailsResponse.toAccountDetails() = AccountDetails(
    includeAdult = includeAdult,
    iso31661 = iso31661,
    name = name,
    avatarItem = avatarItemResponse?.toAvatarItem(),
    id = id,
    iso6391 = iso6391,
    username = username
  )

  private fun AvatarItemResponse.toAvatarItem() = AvatarItem(
    avatarTMDb = avatarTMDbResponse?.toAvatarTMDb(),
    gravatar = gravatarResponse?.toGravatar()
  )

  private fun AvatarTMDbResponse.toAvatarTMDb() = AvatarTMDb(
    avatarPath = avatarPath
  )

  private fun GravatarResponse.toGravatar() = Gravatar(
    hash = hash
  )

  fun CountryIPResponse.toCountryIP() = CountryIP(
    country = country,
    ip = ip
  )
}
