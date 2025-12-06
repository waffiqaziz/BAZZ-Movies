package com.waffiq.bazz_movies.feature.person.testutils

import com.waffiq.bazz_movies.core.network.data.remote.datasource.MovieDataSource
import com.waffiq.bazz_movies.core.test.UnconfinedDispatcherRule
import com.waffiq.bazz_movies.feature.person.data.repository.PersonRepositoryImpl
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule

abstract class BasePersonRepositoryImplTest {

  protected val id = 1

  protected lateinit var repository: PersonRepositoryImpl
  protected val movieDataSource: MovieDataSource = mockk()

  @get:Rule
  val mainDispatcherRule = UnconfinedDispatcherRule()

  @Before
  fun setUp() {
    repository = PersonRepositoryImpl(movieDataSource)
  }
}
