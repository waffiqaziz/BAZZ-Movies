package com.waffiq.bazz_movies.core.network.di

import com.waffiq.bazz_movies.core.network.data.remote.retrofit.interceptors.ApiKeyInterceptorOMDb
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.interceptors.ApiKeyInterceptorTMDB
import com.waffiq.bazz_movies.core.network.domain.IDebugConfig
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Before
import org.junit.Test

class NetworkModuleTest {

  private lateinit var debugConfig: IDebugConfig

  @Before
  fun setup() {
    debugConfig = mockk()
  }

  @Test
  fun provideLoggingInterceptor_debugEnabled_setsLevelBody() {
    every { debugConfig.isDebug() } returns true

    val loggingInterceptor = NetworkModule().provideLoggingInterceptor(debugConfig)
    assertEquals(HttpLoggingInterceptor.Level.BODY, loggingInterceptor.level)
  }

  @Test
  fun provideLoggingInterceptor_debugDisabled_setsLevelNone() {
    every { debugConfig.isDebug() } returns false

    val loggingInterceptor = NetworkModule().provideLoggingInterceptor(debugConfig)
    assertEquals(HttpLoggingInterceptor.Level.NONE, loggingInterceptor.level)
  }

  @Test
  fun provideMoshi_returnsValidMoshiInstance() {
    val moshi = NetworkModule().provideMoshi()
    assertNotNull(moshi.adapter(Any::class.java))
  }

  @Test
  fun provideOkHttpClient_configuresCorrectTimeouts() {
    val loggingInterceptor = mockk<HttpLoggingInterceptor>(relaxed = true)

    val client = NetworkModule().provideOkHttpClient(loggingInterceptor)
    assertEquals(30L, client.connectTimeoutMillis.toLong() / 1000)
    assertEquals(30L, client.readTimeoutMillis.toLong() / 1000)
    assertEquals(30L, client.writeTimeoutMillis.toLong() / 1000)
  }

  @Test
  fun provideCountryIPApiService_createsValidService() {
    val client = mockk<OkHttpClient>(relaxed = true)
    val service = NetworkModule().provideCountryIPApiService(client)
    assertNotNull(service)
  }

  @Test
  fun provideOMDBApiService_addsCorrectInterceptor() {
    val clientBuilder = mockk<OkHttpClient.Builder>(relaxed = true)
    val client = mockk<OkHttpClient> {
      every { newBuilder() } returns clientBuilder
    }
    val interceptorSlot = slot<ApiKeyInterceptorOMDb>() // Use a slot to capture the interceptor

    every { clientBuilder.addInterceptor(capture(interceptorSlot)) } returns clientBuilder
    every { clientBuilder.build() } returns mockk()

    val networkModule = NetworkModule()
    val service = networkModule.provideOMDBApiService(client)

    assertNotNull(service)

    // verify that the correct interceptor was added and capture it
    verify { clientBuilder.addInterceptor(ofType(ApiKeyInterceptorOMDb::class)) }
  }

  @Test
  fun provideTMDBApiService_addsCorrectInterceptor() {
    val clientBuilder = mockk<OkHttpClient.Builder>(relaxed = true)
    val client = mockk<OkHttpClient> {
      every { newBuilder() } returns clientBuilder
    }
    val interceptorSlot = slot<ApiKeyInterceptorTMDB>() // Use a slot to capture the interceptor

    every { clientBuilder.addInterceptor(capture(interceptorSlot)) } returns clientBuilder
    every { clientBuilder.build() } returns mockk()

    val networkModule = NetworkModule()
    val service = networkModule.provideTMDBApiService(client)

    assertNotNull(service)

    // verify that the correct interceptor was added and capture it
    verify { clientBuilder.addInterceptor(ofType(ApiKeyInterceptorTMDB::class)) }
  }
}
