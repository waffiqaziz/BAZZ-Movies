package com.waffiq.bazz_movies.core.network.data.remote.retrofit.services

import com.google.common.truth.Truth.assertThat
import com.waffiq.bazz_movies.core.network.testutils.BaseApiServiceTest
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class TvApiServiceTest : BaseApiServiceTest() {

  // This test only to make sure moshi can convert json to data class
  // We test only several api service

  @Test
  fun getTopRatedTv_useDefaultLanguage_returnsEnglish() = runTest {
    mockMediaResponse()

    tvApiService.getTopRatedTv(page = 1)

    val request = mockWebServer.takeRequest()
    assertEquals("en-US", request.requestUrl?.queryParameter("language"))
  }

  @Test
  fun getTopRatedTv_overrideDefaultLanguage_returnsCorrectLanguage() = runTest {
    mockMediaResponse()

    tvApiService.getTopRatedTv(page = 1, language = "id-ID")

    val request = mockWebServer.takeRequest()

    assertEquals("id-ID", request.requestUrl?.queryParameter("language"))
  }

  @Test
  fun getPopularTv_useDefaultLanguage_returnsEnglish() = runTest {
    mockMediaResponse()

    tvApiService.getPopularTv(
      region = "US",
      dateTime = "2024-01-01",
      page = 1
    )

    val request = mockWebServer.takeRequest()
    assertThat(request.path).contains("language=en-US")
  }
}
