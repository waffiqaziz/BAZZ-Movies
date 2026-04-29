package com.waffiq.bazz_movies.feature.detail.testutils

import com.waffiq.bazz_movies.core.network.data.remote.datasource.movie.MovieRemoteDataSource
import com.waffiq.bazz_movies.core.network.data.remote.datasource.omdb.OmdbRemoteDataSource
import com.waffiq.bazz_movies.core.network.data.remote.datasource.tv.TvRemoteDataSource
import com.waffiq.bazz_movies.core.test.UnconfinedDispatcherRule
import com.waffiq.bazz_movies.feature.detail.data.repository.DetailRepositoryImpl
import io.mockk.mockk
import org.junit.Rule
import kotlin.test.BeforeTest

open class BaseDetailRepositoryImplTest {

  protected lateinit var repository: DetailRepositoryImpl
  protected val mockMovieDataSource: MovieRemoteDataSource = mockk()
  protected val mockTvRemoteDataSource: TvRemoteDataSource = mockk()
  protected val mockOmdbRemoteDataSource: OmdbRemoteDataSource = mockk()

  protected val id = 1
  protected val idString = "tt12345"

  @get:Rule
  val mainDispatcherRule = UnconfinedDispatcherRule()

  @BeforeTest
  fun setUp() {
    repository = DetailRepositoryImpl(
      mockMovieDataSource,
      mockTvRemoteDataSource,
      mockOmdbRemoteDataSource,
    )
  }
}
