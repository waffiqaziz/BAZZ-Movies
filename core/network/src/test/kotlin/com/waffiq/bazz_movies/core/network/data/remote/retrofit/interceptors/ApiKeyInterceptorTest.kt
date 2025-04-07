package com.waffiq.bazz_movies.core.network.data.remote.retrofit.interceptors

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test

class ApiKeyInterceptorTest {

  private lateinit var mockWebServer: MockWebServer

  @Before
  fun setUp() {
    mockWebServer = MockWebServer()
    mockWebServer.start()
  }

  @After
  fun tearDown() {
    mockWebServer.shutdown()
  }

  private fun makeRequest(client: OkHttpClient): Response {
    val request = Request.Builder()
      .url(mockWebServer.url("/test"))
      .build()
    return client.newCall(request).execute()
  }

  private fun testAddsApiKeyQueryParameter(
    interceptor: Interceptor,
    apiKeyParamName: String,
    apiKey: String
  ) {
    val client = OkHttpClient.Builder()
      .addInterceptor(interceptor)
      .build()

    mockWebServer.enqueue(MockResponse().setResponseCode(200))

    val response = makeRequest(client)
    val requestUrl = mockWebServer.takeRequest().requestUrl
    assertEquals(apiKey, requestUrl?.queryParameter(apiKeyParamName))
    assertEquals(200, response.code)
  }

  private fun testRetainsExistingQueryParameters(
    interceptor: Interceptor,
    apiKeyParamName: String,
    apiKey: String
  ) {
    val client = OkHttpClient.Builder()
      .addInterceptor(interceptor)
      .build()

    mockWebServer.enqueue(MockResponse().setResponseCode(200))

    val request = Request.Builder()
      .url(mockWebServer.url("/test?existingParam=value"))
      .build()
    client.newCall(request).execute()

    val requestUrl = mockWebServer.takeRequest().requestUrl
    assertEquals("value", requestUrl?.queryParameter("existingParam"))
    assertEquals(apiKey, requestUrl?.queryParameter(apiKeyParamName))
  }

  private fun testHandlesEmptyOriginalUrl(
    interceptor: Interceptor
  ) {
    val client = OkHttpClient.Builder()
      .addInterceptor(interceptor)
      .build()

    mockWebServer.enqueue(MockResponse().setResponseCode(200))

    val response = makeRequest(client)
    assertTrue(response.isSuccessful)
  }

  private fun testThrowsExceptionOnInvalidUrl(
    interceptor: Interceptor
  ) {
    val client = OkHttpClient.Builder()
      .addInterceptor(interceptor)
      .build()

    val exception = runCatching {
      client.newCall(Request.Builder().url("invalid-url").build()).execute()
    }.exceptionOrNull()
    assertTrue(exception is IllegalArgumentException)
  }

  @Test
  fun oMDbInterceptor_AllTestPassed() {
    val interceptor = ApiKeyInterceptorOMDb("omdbKey")
    val apiKeyParamName = "apikey"
    val apiKey = "omdbKey"

    testAddsApiKeyQueryParameter(interceptor, apiKeyParamName, apiKey)
    testRetainsExistingQueryParameters(interceptor, apiKeyParamName, apiKey)
    testHandlesEmptyOriginalUrl(interceptor)
    testThrowsExceptionOnInvalidUrl(interceptor)
  }

  @Test
  fun tMDbInterceptor_AllTestPassed() {
    val interceptor = ApiKeyInterceptorTMDB("tmdbKey")
    val apiKeyParamName = "api_key"
    val apiKey = "tmdbKey"

    testAddsApiKeyQueryParameter(interceptor, apiKeyParamName, apiKey)
    testRetainsExistingQueryParameters(interceptor, apiKeyParamName, apiKey)
    testHandlesEmptyOriginalUrl(interceptor)
    testThrowsExceptionOnInvalidUrl(interceptor)
  }
}
