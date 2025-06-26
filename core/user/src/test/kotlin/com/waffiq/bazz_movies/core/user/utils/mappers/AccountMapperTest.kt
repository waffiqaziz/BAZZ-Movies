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
import com.waffiq.bazz_movies.core.user.utils.mappers.AccountMapper.toAccountDetails
import com.waffiq.bazz_movies.core.user.utils.mappers.AccountMapper.toAuthentication
import com.waffiq.bazz_movies.core.user.utils.mappers.AccountMapper.toAvatarItem
import com.waffiq.bazz_movies.core.user.utils.mappers.AccountMapper.toAvatarTMDb
import com.waffiq.bazz_movies.core.user.utils.mappers.AccountMapper.toCountryIP
import com.waffiq.bazz_movies.core.user.utils.mappers.AccountMapper.toCreateSession
import com.waffiq.bazz_movies.core.user.utils.mappers.AccountMapper.toGravatar
import com.waffiq.bazz_movies.core.user.utils.mappers.AccountMapper.toUserModel
import com.waffiq.bazz_movies.core.user.utils.mappers.AccountMapper.toUserModelPref
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.Test

class AccountMapperTest {

  @Test
  fun toUserModel_withValidValues_returnsUserModel() {
    val userModelPref = UserModelPref(
      userId = 34567,
      name = "name",
      username = "username",
      password = "password",
      region = "region",
      token = "token",
      isLogin = false,
      gravatarHast = "gravatar_hast",
      tmdbAvatar = "tmdb_avatar",
    )

    val userModel: UserModel = userModelPref.toUserModel()
    assertEquals(34567, userModel.userId)
    assertEquals("name", userModel.name)
    assertEquals("username", userModel.username)
    assertEquals("password", userModel.password)
    assertEquals("region", userModel.region)
    assertEquals("token", userModel.token)
    assertFalse(userModel.isLogin)
    assertEquals("gravatar_hast", userModel.gravatarHast)
    assertEquals("tmdb_avatar", userModel.tmdbAvatar)
  }

  @Test
  fun toUserModelPref_withValidValues_returnsUserModelPref() {
    val userMode = UserModel(
      userId = 12345,
      name = "name_model",
      username = "username_model",
      password = "password_model",
      region = "region_model",
      token = "token_model",
      isLogin = true,
      gravatarHast = "gravatar_hast_model",
      tmdbAvatar = "tmdb_avatar_model",
    )

    val userModelPref: UserModelPref = userMode.toUserModelPref()
    assertEquals(12345, userModelPref.userId)
    assertEquals("name_model", userModelPref.name)
    assertEquals("username_model", userModelPref.username)
    assertEquals("password_model", userModelPref.password)
    assertEquals("region_model", userModelPref.region)
    assertEquals("token_model", userModelPref.token)
    assertTrue(userModelPref.isLogin)
    assertEquals("gravatar_hast_model", userModelPref.gravatarHast)
    assertEquals("tmdb_avatar_model", userModelPref.tmdbAvatar)
  }

  @Test
  fun toAuthentication_withValidValues_returnsAuthentication() {
    val response = AuthenticationResponse(
      success = true,
      expireAt = "date_expire",
      requestToken = "request_token"
    )

    val authentication: Authentication = response.toAuthentication()
    assertEquals(true, authentication.success)
    assertEquals("date_expire", authentication.expireAt)
    assertEquals("request_token", authentication.requestToken)
  }

  @Test
  fun toAuthentication_withNullValues_returnsAuthentication() {
    val response = AuthenticationResponse(
      success = true,
      expireAt = null,
      requestToken = null
    )

    val authentication: Authentication = response.toAuthentication()
    assertTrue(authentication.success)
    assertNull(authentication.expireAt)
    assertNull(authentication.requestToken)
  }

  @Test
  fun toCreateSession_withValidValues_returnCreateSession() {
    val response = CreateSessionResponse(success = true, sessionId = "session_id")

    val createSession: CreateSession = response.toCreateSession()
    assertEquals(true, createSession.success)
    assertEquals("session_id", createSession.sessionId)
  }

  @Test
  fun toAccountDetails_withValidValues_returnsAccountDetails() {
    val response = AccountDetailsResponse(
      includeAdult = false,
      iso31661 = "en",
      name = "Waffiq",
      avatarItemResponse = AvatarItemResponse(
        gravatarResponse = GravatarResponse(hash = "325987423659432"),
        avatarTMDbResponse = AvatarTMDbResponse(avatarPath = "347589074283054")
      ),
      id = 513176325,
      iso6391 = "ID",
      username = "waffiq1234",
    )

    val accountDetails: AccountDetails = response.toAccountDetails()
    assertEquals(513176325, accountDetails.id)
    assertEquals("ID", accountDetails.iso6391)
    assertEquals("en", accountDetails.iso31661)
    assertEquals("Waffiq", accountDetails.name)
    assertEquals("waffiq1234", accountDetails.username)
    assertEquals("347589074283054", accountDetails.avatarItem?.avatarTMDb?.avatarPath)
    assertEquals("325987423659432", accountDetails.avatarItem?.gravatar?.hash)
    assertFalse(accountDetails.includeAdult == true)

    assertEquals(
      response.avatarItemResponse?.avatarTMDbResponse?.avatarPath,
      accountDetails.avatarItem?.avatarTMDb?.avatarPath
    )
  }

  @Test
  fun oAccountDetails_withSomeNullValues_returnsAccountDetails() {
    val response = AccountDetailsResponse(
      includeAdult = false,
      iso31661 = "id",
      name = "people",
      avatarItemResponse = null,
      id = 5637195,
      iso6391 = "MY",
      username = "someone",
    )

    val accountDetails: AccountDetails = response.toAccountDetails()
    assertEquals(5637195, accountDetails.id)
    assertEquals("MY", accountDetails.iso6391)
    assertEquals("id", accountDetails.iso31661)
    assertEquals("people", accountDetails.name)
    assertEquals("someone", accountDetails.username)
    assertNull(accountDetails.avatarItem)
    assertFalse(accountDetails.includeAdult == true)
  }

  @Test
  fun toAccountDetails_withAllNullValues_returnsAccountDetails() {
    val response = AccountDetailsResponse(
      includeAdult = null,
      iso31661 = null,
      name = null,
      avatarItemResponse = null,
      id = null,
      iso6391 = null,
      username = null,
    )

    val accountDetails: AccountDetails = response.toAccountDetails()
    assertNull(accountDetails.id)
    assertNull(accountDetails.iso6391)
    assertNull(accountDetails.iso31661)
    assertNull(accountDetails.name)
    assertNull(accountDetails.username)
    assertNull(accountDetails.avatarItem?.avatarTMDb?.avatarPath)
    assertNull(accountDetails.avatarItem?.gravatar?.hash)
    assertNull(accountDetails.includeAdult)
  }

  @Test
  fun toAvatarItem_withValidValues_returnsAvatarItem() {
    val response = AvatarItemResponse(
      gravatarResponse = GravatarResponse(hash = "/617956749353.jpg"),
      avatarTMDbResponse = AvatarTMDbResponse(avatarPath = "/27359679153253.jpg")
    )

    val avatarItem: AvatarItem = response.toAvatarItem()
    assertEquals("/617956749353.jpg", avatarItem.gravatar?.hash)
    assertEquals("/27359679153253.jpg", avatarItem.avatarTMDb?.avatarPath)
  }

  @Test
  fun toAvatarItem_withGravatarNull_returnsAvatarItem() {
    val response = AvatarItemResponse(
      gravatarResponse = null,
      avatarTMDbResponse = AvatarTMDbResponse(avatarPath = "/27359679153253.jpg")
    )

    val avatarItem: AvatarItem = response.toAvatarItem()

    assertNull(avatarItem.gravatar)
    assertEquals("/27359679153253.jpg", avatarItem.avatarTMDb?.avatarPath)
  }

  @Test
  fun toAvatarItem_withAvatarNull_returnsAvatarItem() {
    val response = AvatarItemResponse(
      gravatarResponse = GravatarResponse(hash = "/617956749353.jpg"),
      avatarTMDbResponse = null
    )

    val avatarItem: AvatarItem = response.toAvatarItem()

    assertEquals("/617956749353.jpg", avatarItem.gravatar?.hash)
    assertNull(avatarItem.avatarTMDb)
  }

  @Test
  fun toAvatarItem_withNullValues_returnsAvatarItem() {
    val response = AvatarItemResponse(gravatarResponse = null, avatarTMDbResponse = null)

    val avatarItem: AvatarItem = response.toAvatarItem()

    assertNull(avatarItem.gravatar)
    assertNull(avatarItem.avatarTMDb)
  }

  @Test
  fun toAvatarTMDb_withValidValues_returnsAvatarTMDb() {
    val response = AvatarTMDbResponse(avatarPath = "/32154543587943.jpg")

    val avatarTmdb: AvatarTMDb = response.toAvatarTMDb()
    assertEquals("/32154543587943.jpg", avatarTmdb.avatarPath)
  }

  @Test
  fun toGravatar_withValidValues_returnsGravatar() {
    val response = GravatarResponse(hash = "/325987423659432.jpg")

    val gravatar: Gravatar = response.toGravatar()
    assertEquals("/325987423659432.jpg", gravatar.hash)
  }

  @Test
  fun toCountryIP_withValidValues_returnsCountryIP() {
    val response = CountryIPResponse(
      country = "ID",
      ip = "ip_country"
    )

    val countryIp: CountryIP = response.toCountryIP()
    assertEquals("ID", countryIp.country)
    assertEquals("ip_country", countryIp.ip)
  }
}
