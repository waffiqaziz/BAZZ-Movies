package com.waffiq.bazz_movies.utils.mappers

import com.waffiq.bazz_movies.data.remote.responses.countryip.CountryIPResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.account.AccountDetailsResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.account.AuthenticationResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.account.AvatarItemResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.account.CreateSessionResponse
import com.waffiq.bazz_movies.domain.model.account.CountryIP
import com.waffiq.bazz_movies.domain.model.account.AccountDetails
import com.waffiq.bazz_movies.domain.model.account.Authentication
import com.waffiq.bazz_movies.domain.model.account.AvatarItem
import com.waffiq.bazz_movies.domain.model.account.CreateSession

object AccountMapper {

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
    avatarTMDb = avatarTMDbResponse,
    gravatar = gravatarResponse
  )

  fun CountryIPResponse.toCountryIP() = CountryIP(
    country = country,
    ip = ip
  )
}