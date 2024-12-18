package com.waffiq.bazz_movies.core.user.utils.mappers

import com.waffiq.bazz_movies.core.network.data.remote.responses.countryip.CountryIPResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account.AccountDetailsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account.AuthenticationResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account.AvatarItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account.AvatarTMDbResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account.CreateSessionResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account.GravatarResponse
import com.waffiq.bazz_movies.core.user.domain.model.account.AccountDetails
import com.waffiq.bazz_movies.core.user.domain.model.account.Authentication
import com.waffiq.bazz_movies.core.user.domain.model.account.CountryIP
import com.waffiq.bazz_movies.core.user.domain.model.account.CreateSession
import com.waffiq.bazz_movies.core.user.utils.mappers.AccountMapper.toAccountDetails
import com.waffiq.bazz_movies.core.user.utils.mappers.AccountMapper.toAuthentication
import com.waffiq.bazz_movies.core.user.utils.mappers.AccountMapper.toCountryIP
import com.waffiq.bazz_movies.core.user.utils.mappers.AccountMapper.toCreateSession
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import org.junit.Test

class AccountMapperTest {

  @Test
  fun `map AuthenticationResponse to Authentication`() {
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
  fun `map CreateSessionResponse to CreateSession`() {
    val response = CreateSessionResponse(
      success = true,
      sessionId = "session_id"
    )

    val authentication: CreateSession = response.toCreateSession()
    assertEquals(true, authentication.success)
    assertEquals("session_id", authentication.sessionId)
  }

  @Test
  fun `map AccountDetailsResponse to AccountDetails`() {
    val response = AccountDetailsResponse(
      includeAdult = false,
      iso31661 = "en",
      name = "Waffiq",
      avatarItemResponse = AvatarItemResponse(
        gravatarResponse = GravatarResponse(
          hash = "325987423659432"
        ),
        avatarTMDbResponse = AvatarTMDbResponse(
          avatarPath = "347589074283054"
        )
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
  fun `map CountryIPResponse to CountryIP`() {
    val response = CountryIPResponse(
      country = "ID",
      ip = "ip_country"
    )

    val countryIp: CountryIP = response.toCountryIP()
    assertEquals("ID", countryIp.country)
    assertEquals("ip_country", countryIp.ip)
  }
}
