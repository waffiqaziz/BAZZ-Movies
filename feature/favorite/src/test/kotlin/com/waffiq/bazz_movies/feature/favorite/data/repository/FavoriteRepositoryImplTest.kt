package com.waffiq.bazz_movies.feature.favorite.data.repository

import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.network.data.remote.datasource.MovieDataSource
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.SESSION_ID
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.fakeMovieResponsePagingData
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.fakeTvResponsePagingData
import com.waffiq.bazz_movies.feature.favorite.testutils.Helper.testPagingFlow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain

class FavoriteRepositoryImplTest : BehaviorSpec({

  lateinit var repository: FavoriteRepositoryImpl
  val dataSource: MovieDataSource = mockk(relaxed = true)

  beforeTest {
    Dispatchers.setMain(UnconfinedTestDispatcher())
    repository = FavoriteRepositoryImpl(dataSource)
  }

  Given("FavoriteRepositoryImpl") {

    When("getFavoriteMovies is called") {
      Then("it should return mapped MediaItem list") {
        every { dataSource.getFavoriteMovies(SESSION_ID) } returns
          flowOf(fakeMovieResponsePagingData)

        testPagingFlow<MediaItem>(repository.getFavoriteMovies(SESSION_ID)) {
          it[0].title shouldBe "Inception"
          it[1].title shouldBe "The Dark Knight"
        }
      }
    }

    When("getFavoriteTv is called") {
      Then("it should return mapped MediaItem list") {
        every { dataSource.getFavoriteTv(SESSION_ID) } returns
          flowOf(fakeTvResponsePagingData)

        testPagingFlow<MediaItem>(repository.getFavoriteTv(SESSION_ID)) {
          it[0].title shouldBe "Breaking Bad"
          it[1].title shouldBe "Game of Thrones"
        }
      }
    }
  }
})
