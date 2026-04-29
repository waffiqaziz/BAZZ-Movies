@file:Suppress("UnnecessaryAbstractClass")

package com.waffiq.bazz_movies.core.network.testutils

import androidx.paging.PagingSource.LoadParams
import com.waffiq.bazz_movies.core.network.data.remote.datasource.account.AccountRemoteDataSource
import com.waffiq.bazz_movies.core.network.data.remote.datasource.country.CountryRemoteDataSource
import com.waffiq.bazz_movies.core.network.data.remote.datasource.discover.DiscoverRemoteDataSource
import com.waffiq.bazz_movies.core.network.data.remote.datasource.movie.MovieRemoteDataSource
import com.waffiq.bazz_movies.core.network.data.remote.datasource.omdb.OmdbRemoteDataSource
import com.waffiq.bazz_movies.core.network.data.remote.datasource.person.PersonRemoteDataSource
import com.waffiq.bazz_movies.core.network.data.remote.datasource.search.SearchRemoteDataSource
import com.waffiq.bazz_movies.core.network.data.remote.datasource.trending.TrendingRemoteDataSource
import com.waffiq.bazz_movies.core.network.data.remote.datasource.tv.TvRemoteDataSource
import com.waffiq.bazz_movies.core.network.data.remote.models.FavoriteRequest
import com.waffiq.bazz_movies.core.network.data.remote.models.WatchlistRequest
import com.waffiq.bazz_movies.core.network.data.remote.pagingsources.GenericPagingSource
import com.waffiq.bazz_movies.core.network.data.remote.pagingsources.SearchPagingSource
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostResponse
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.AccountApiService
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.AuthApiService
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.CountryIPApiService
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.DiscoverApiService
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.MovieApiService
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.OMDbApiService
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.PersonApiService
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.SearchApiService
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.TrendingApiService
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.TvApiService
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import io.mockk.MockKAnnotations
import io.mockk.clearMocks
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import retrofit2.Response

/**
 * Base test class for verifying behavior of [MovieRemoteDataSource].
 *
 * Provides common mock dependencies, error response stubs, and a shared setup routine
 * to be reused across concrete test implementations.
 */
open class BaseMediaDataSourceTest {

  protected val apiMaintenanceErrorResponse: Response<PostResponse> = Response.error(
    503,
    """{"status_code": 503, "status_message": "The API is undergoing maintenance. Try again later."}"""
      .toResponseBody("application/json".toMediaTypeOrNull()),
  )

  protected val apiMaintenanceErrorMessage = "The API is undergoing maintenance. Try again later."

  protected val backendErrorResponse: Response<PostResponse> = Response.error(
    502,
    """{"status_code": 502, "status_message": "Couldn't connect to the backend server."}"""
      .toResponseBody("application/json".toMediaTypeOrNull()),
  )

  protected val notAuthorizedErrorResponse: Response<PostResponse> = Response.error(
    401,
    """{"status_code": 401, "status_message": "Not Authorized"}"""
      .toResponseBody("application/json".toMediaTypeOrNull()),
  )
  protected val invalidServiceErrorResponse: Response<PostResponse> = Response.error(
    501,
    """{"status_code": 501, "status_message": "Invalid service: this service does not exist."}"""
      .toResponseBody("application/json".toMediaTypeOrNull()),
  )
  protected val invalidParameterErrorResponse: Response<PostResponse> = Response.error(
    422,
    """{"status_code": 422, "status_message": "Invalid parameters: Your request parameters are incorrect."}"""
      .toResponseBody("application/json".toMediaTypeOrNull()),
  )

  protected val userId = 123123
  protected val sessionId = "session_id"
  protected val tvId = 45678
  protected val movieId = 121212
  protected val rating = 9f
  protected val fav = FavoriteRequest("tv", 35462654, false)
  protected val wtc = WatchlistRequest("movie", 345345, false)

  protected val backendErrorMessage = "Couldn't connect to the backend server."

  @MockK
  protected lateinit var mockAccountApiService: AccountApiService

  @MockK
  protected lateinit var mockAuthApiService: AuthApiService

  @MockK
  protected lateinit var mockCountryIPApiService: CountryIPApiService

  @MockK
  protected lateinit var mockDiscoverApiService: DiscoverApiService

  @MockK
  protected lateinit var mockMovieApiService: MovieApiService

  @MockK
  protected lateinit var mockPersonApiService: PersonApiService

  @MockK
  protected lateinit var mockSearchApiService: SearchApiService

  @MockK
  protected lateinit var mockTrendingApiService: TrendingApiService

  @MockK
  protected lateinit var mockTvApiService: TvApiService

  @MockK
  protected lateinit var mockOmdbApiService: OMDbApiService

  @MockK
  protected lateinit var testDispatcher: Dispatchers

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  protected lateinit var accountRemoteDataSource: AccountRemoteDataSource
  protected lateinit var countryRemoteDataSource: CountryRemoteDataSource
  protected lateinit var discoverRemoteDataSource: DiscoverRemoteDataSource
  protected lateinit var movieRemoteDataSource: MovieRemoteDataSource
  protected lateinit var omdbRemoteDataSource: OmdbRemoteDataSource
  protected lateinit var personRemoteDataSource: PersonRemoteDataSource
  protected lateinit var searchRemoteDataSource: SearchRemoteDataSource
  protected lateinit var trendingRemoteDataSource: TrendingRemoteDataSource
  protected lateinit var tvRemoteDataSource: TvRemoteDataSource

  @Before
  open fun setup() {
    // initialize MockK annotations and relax mocking behavior
    MockKAnnotations.init(this, relaxed = true)

    accountRemoteDataSource = AccountRemoteDataSource(mockAccountApiService, testDispatcher.IO)
    countryRemoteDataSource = CountryRemoteDataSource(mockCountryIPApiService, testDispatcher.IO)
    discoverRemoteDataSource = DiscoverRemoteDataSource(mockDiscoverApiService, testDispatcher.IO)
    movieRemoteDataSource = MovieRemoteDataSource(mockMovieApiService, testDispatcher.IO)
    omdbRemoteDataSource = OmdbRemoteDataSource(mockOmdbApiService, testDispatcher.IO)
    personRemoteDataSource = PersonRemoteDataSource(mockPersonApiService, testDispatcher.IO)
    searchRemoteDataSource = SearchRemoteDataSource(mockSearchApiService, testDispatcher.IO)
    trendingRemoteDataSource = TrendingRemoteDataSource(mockTrendingApiService, testDispatcher.IO)
    tvRemoteDataSource = TvRemoteDataSource(mockTvApiService, testDispatcher.IO)
  }

  @After
  fun clear() {
    // clear any previous mocks to ensure tests are isolated
    clearMocks(
      mockAccountApiService,
      mockAuthApiService,
      mockCountryIPApiService,
      mockDiscoverApiService,
      mockMovieApiService,
      mockPersonApiService,
      mockSearchApiService,
      mockTrendingApiService,
      mockTvApiService,
      mockOmdbApiService,
    )
  }

  protected suspend fun GenericPagingSource.toLoadResult() =
    this.load(LoadParams.Refresh(key = 1, loadSize = 2, placeholdersEnabled = false))

  protected suspend fun SearchPagingSource.toLoadResult() =
    this.load(LoadParams.Refresh(key = 1, loadSize = 2, placeholdersEnabled = false))
}
