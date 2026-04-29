package com.waffiq.bazz_movies.core.network.testutils

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.TvApiService
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

abstract class BaseApiServiceTest {

  protected lateinit var mockWebServer: MockWebServer
  protected lateinit var tvApiService: TvApiService

  @Before
  fun setup() {
    mockWebServer = MockWebServer()

    val moshi = Moshi.Builder()
      .add(KotlinJsonAdapterFactory())
      .build()

    val retrofit = Retrofit.Builder()
      .baseUrl(mockWebServer.url("/"))
      .addConverterFactory(MoshiConverterFactory.create(moshi))
      .build()

    tvApiService = retrofit.create(TvApiService::class.java)
  }

  @After
  fun tearDown() {
    mockWebServer.shutdown()
  }

  protected fun mockMediaResponse() {
    mockWebServer.enqueue(
      MockResponse().setBody(
        """{ "page":1, "results": [], "total_pages" :1, "total_results": 1 }""",
      ),
    )
  }
}
