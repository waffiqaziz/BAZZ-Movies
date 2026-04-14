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
import retrofit2.Retrofit

class NetworkModuleTest {

  private val debugConfig: IDebugConfig = mockk()
  private val moshi = NetworkModule().provideMoshi()

  private lateinit var loggingInterceptor: HttpLoggingInterceptor
  private lateinit var client: OkHttpClient
  private lateinit var tmdbRetrofit: Retrofit
  private lateinit var omdbRetrofit: Retrofit
  private lateinit var countryRetrofit: Retrofit

  @Before
  fun setup() {
    every { debugConfig.isDebug() } returns true

    loggingInterceptor = NetworkModule().provideLoggingInterceptor(debugConfig)
    client = NetworkModule().provideOkHttpClient(loggingInterceptor)
    countryRetrofit = NetworkModule().provideCountryRetrofit(client, moshi)
    tmdbRetrofit = NetworkModule().provideTMDBRetrofit(client, moshi)
    omdbRetrofit = NetworkModule().provideOMDbRetrofit(client, moshi)
  }

  @Test
  fun provideLoggingInterceptor_whenDebugEnabled_setsLevelBody() {
    every { debugConfig.isDebug() } returns true

    val loggingInterceptor = NetworkModule().provideLoggingInterceptor(debugConfig)
    assertEquals(HttpLoggingInterceptor.Level.BODY, loggingInterceptor.level)
  }

  @Test
  fun provideLoggingInterceptor_whenDebugDisabled_setsLevelNone() {
    every { debugConfig.isDebug() } returns false

    val loggingInterceptor = NetworkModule().provideLoggingInterceptor(debugConfig)
    assertEquals(HttpLoggingInterceptor.Level.NONE, loggingInterceptor.level)
  }

  @Test
  fun provideMoshi_whenAlreadyInitialized_returnsValidMoshiInstance() {
    assertNotNull(moshi.adapter(Any::class.java))
  }

  @Test
  fun provideOkHttpClient_whenProvided_configuresCorrectTimeouts() {
    val loggingInterceptor = mockk<HttpLoggingInterceptor>(relaxed = true)

    val client = NetworkModule().provideOkHttpClient(loggingInterceptor)
    assertEquals(30L, client.connectTimeoutMillis.toLong() / 1000)
    assertEquals(30L, client.readTimeoutMillis.toLong() / 1000)
    assertEquals(30L, client.writeTimeoutMillis.toLong() / 1000)
  }

  @Test
  fun provideApi_whenAlreadyInitialized_createsValidService() {
    assertNotNull(NetworkModule().provideCountryApi(countryRetrofit))
    assertNotNull(NetworkModule().provideOMDbApi(omdbRetrofit))
    assertNotNull(NetworkModule().provideAccountApi(tmdbRetrofit))
    assertNotNull(NetworkModule().provideAuthApi(tmdbRetrofit))
    assertNotNull(NetworkModule().provideDiscoverApi(tmdbRetrofit))
    assertNotNull(NetworkModule().provideMovieApi(tmdbRetrofit))
    assertNotNull(NetworkModule().providePersonApi(tmdbRetrofit))
    assertNotNull(NetworkModule().provideSearchApi(tmdbRetrofit))
    assertNotNull(NetworkModule().provideTrendingApi(tmdbRetrofit))
    assertNotNull(NetworkModule().provideTvApi(tmdbRetrofit))
  }

  @Test
  fun provideOMDBApiService_whenAlreadyInitialized_addsCorrectInterceptor() {
    val clientBuilder = mockk<OkHttpClient.Builder>(relaxed = true)
    val client = mockk<OkHttpClient> {
      every { newBuilder() } returns clientBuilder
    }
    val interceptorSlot = slot<ApiKeyInterceptorOMDb>() // Use a slot to capture the interceptor

    every { clientBuilder.addInterceptor(capture(interceptorSlot)) } returns clientBuilder
    every { clientBuilder.build() } returns mockk()

    val networkModule = NetworkModule()
    val service = networkModule.provideOMDbRetrofit(client, moshi)

    assertNotNull(service)

    // verify that the correct interceptor was added and capture it
    verify { clientBuilder.addInterceptor(ofType(ApiKeyInterceptorOMDb::class)) }
  }

  @Test
  fun provideTMDBApiService_whenAlreadyInitialized_addsCorrectInterceptor() {
    val clientBuilder = mockk<OkHttpClient.Builder>(relaxed = true)
    val client = mockk<OkHttpClient> {
      every { newBuilder() } returns clientBuilder
    }
    val interceptorSlot = slot<ApiKeyInterceptorTMDB>() // Use a slot to capture the interceptor

    every { clientBuilder.addInterceptor(capture(interceptorSlot)) } returns clientBuilder
    every { clientBuilder.build() } returns mockk()

    val networkModule = NetworkModule()
    val service = networkModule.provideTMDBRetrofit(client, moshi)

    assertNotNull(service)

    // verify that the correct interceptor was added and capture it
    verify { clientBuilder.addInterceptor(ofType(ApiKeyInterceptorTMDB::class)) }
  }
}
