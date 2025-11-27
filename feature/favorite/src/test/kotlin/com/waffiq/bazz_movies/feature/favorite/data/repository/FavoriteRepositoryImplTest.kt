package com.waffiq.bazz_movies.feature.favorite.data.repository

import androidx.paging.PagingData
import app.cash.turbine.test
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

        testPagingFlow(repository.getFavoriteMovies(SESSION_ID)) {
          it[0].title shouldBe "Inception"
          it[1].title shouldBe "The Dark Knight"
        }
      }
    }

    When("getFavoriteTv is called") {
      Then("it should return mapped MediaItem list") {
        every { dataSource.getFavoriteTv(SESSION_ID) } returns
          flowOf(fakeTvResponsePagingData)

        testPagingFlow(repository.getFavoriteTv(SESSION_ID)) {
          it[0].title shouldBe "Breaking Bad"
          it[1].title shouldBe "Game of Thrones"
        }
      }
    }

    When("getFavoriteMovies is called with empty data") {
      Then("it should return empty MediaItem list") {
        every { dataSource.getFavoriteMovies(SESSION_ID) } returns
          flowOf(PagingData.empty())

        testPagingFlow(repository.getFavoriteMovies(SESSION_ID)) {
          it shouldBe emptyList()
        }
      }
    }

    When("getFavoriteTv is called with empty data") {
      Then("it should return empty MediaItem list") {
        every { dataSource.getFavoriteTv(SESSION_ID) } returns
          flowOf(PagingData.empty())

        testPagingFlow(repository.getFavoriteTv(SESSION_ID)) {
          it shouldBe emptyList()
        }
      }
    }
  }

  Given("multiple paging data emissions") {
    every { dataSource.getFavoriteMovies(SESSION_ID) } returns
      flowOf(
        fakeMovieResponsePagingData,
        PagingData.empty(),
        fakeMovieResponsePagingData
      )

    When("getFavoriteMovies is called") {
      // collect multiple emissions
      val emissions = mutableListOf<PagingData<MediaItem>>()
      repository.getFavoriteMovies(SESSION_ID).test {
        repeat(3) {
          emissions.add(awaitItem())
        }
        awaitComplete()
      }

      Then("it should emit exactly 3 instances") {
        emissions.size shouldBe 3
      }

      And("first emission should contain movie data") {
        testPagingFlow(flowOf(emissions[0])) { items ->
          items.size shouldBe 2
          items[0].title shouldBe "Inception"
          items[1].title shouldBe "The Dark Knight"
        }
      }

      And("second emission should be empty") {
        testPagingFlow(flowOf(emissions[1])) { items ->
          items shouldBe emptyList()
        }
      }

      And("third emission should contain movie data again") {
        testPagingFlow(flowOf(emissions[2])) { items ->
          items.size shouldBe 2
          items[0].title shouldBe "Inception"
          items[1].title shouldBe "The Dark Knight"
        }
      }
    }
  }
})
