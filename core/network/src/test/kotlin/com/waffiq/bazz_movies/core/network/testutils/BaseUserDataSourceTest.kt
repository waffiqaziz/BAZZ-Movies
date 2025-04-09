package com.waffiq.bazz_movies.core.network.testutils

import com.waffiq.bazz_movies.core.network.data.remote.datasource.UserDataSource
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostResponse
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.CountryIPApiService
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.TMDBApiService
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import io.mockk.MockKAnnotations
import io.mockk.clearMocks
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import retrofit2.Response

abstract class BaseUserDataSourceTest {

  protected val apiInvalidFormatErrorResponse: Response<PostResponse> = Response.error(
    405,
    """{"status_code": 503, "status_message": "Invalid format: This service doesn't exist in that format."}"""
      .toResponseBody("application/json".toMediaTypeOrNull())
  )

  @MockK
  protected lateinit var tmdbApiService: TMDBApiService

  @MockK
  protected lateinit var countryIPApiService: CountryIPApiService

  @MockK
  protected lateinit var testDispatcher: Dispatchers

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  protected lateinit var userDataSource: UserDataSource

  @Before
  open fun setup() {
    MockKAnnotations.init(this, relaxed = true)
    clearMocks(tmdbApiService, countryIPApiService)
    userDataSource = UserDataSource(tmdbApiService, countryIPApiService, testDispatcher.IO)
  }
}
