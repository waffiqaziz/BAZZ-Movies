package com.waffiq.bazz_movies.core.network.testutils

import com.waffiq.bazz_movies.core.network.data.remote.datasource.MovieDataSource
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostResponse
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.OMDbApiService
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

abstract class BaseMovieDataSourceTest {

  protected val apiMaintenanceErrorResponse: Response<PostResponse> = Response.error(
    503,
    """{"status_code": 503, "status_message": "The API is undergoing maintenance. Try again later."}"""
      .toResponseBody("application/json".toMediaTypeOrNull())
  )

  @MockK
  protected lateinit var tmdbApiService: TMDBApiService

  @MockK
  protected lateinit var omDbApiService: OMDbApiService

  @MockK
  protected lateinit var testDispatcher: Dispatchers

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  protected lateinit var movieDataSource: MovieDataSource

  @Before
  open fun setup() {
    // Initialize MockK annotations and relax mocking behavior
    MockKAnnotations.init(this, relaxed = true)

    // clear any previous mocks to ensure tests are isolated
    clearMocks(tmdbApiService, omDbApiService, testDispatcher)

    movieDataSource = MovieDataSource(tmdbApiService, omDbApiService, testDispatcher.IO)
  }
}
