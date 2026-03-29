package com.waffiq.bazz_movies.feature.list.data.repository

import com.waffiq.bazz_movies.core.mappers.MediaItemMapper.toMediaItem
import com.waffiq.bazz_movies.core.network.data.remote.datasource.MovieDataSource
import com.waffiq.bazz_movies.core.test.PagingFlowHelperTest.testPagingFlowAwaitComplete
import com.waffiq.bazz_movies.feature.list.testutils.DummyData.fakeMovieResponsePagingData
import com.waffiq.bazz_movies.feature.list.testutils.DummyData.fakeTvResponsePagingData
import com.waffiq.bazz_movies.feature.list.testutils.DummyData.mediaMovieResponseItem
import com.waffiq.bazz_movies.feature.list.testutils.DummyData.mediaMovieResponseItem2
import com.waffiq.bazz_movies.feature.list.testutils.DummyData.mediaTvResponseItem
import com.waffiq.bazz_movies.feature.list.testutils.DummyData.mediaTvResponseItem2
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain

class ListRepositoryImplTest : BehaviorSpec({

  lateinit var repository: ListRepositoryImpl

  val dataSource: MovieDataSource = mockk(relaxed = true)

  beforeTest {
    Dispatchers.setMain(UnconfinedTestDispatcher())
    repository = ListRepositoryImpl(dataSource)
  }

  Given("fetching movies by genre") {
    When("the response is successful") {
      every { dataSource.getMovieByGenres(any(), any()) } returns
        flowOf(fakeMovieResponsePagingData)
      Then("should return mapped MediaItem") {
        testPagingFlowAwaitComplete(repository.getMovieByGenres("1", "id")) {
          it[0] shouldBe mediaMovieResponseItem.toMediaItem()
          it[1] shouldBe mediaMovieResponseItem2.toMediaItem()
        }
      }
    }
  }

  Given("fetching tv by genre") {
    When("the response is successful") {
      every { dataSource.getTvByGenres(any(), any()) } returns
        flowOf(fakeTvResponsePagingData)
      Then("should return mapped MediaItem") {
        testPagingFlowAwaitComplete(repository.getTvByGenres("1", "id")) {
          it[0] shouldBe mediaTvResponseItem.toMediaItem()
          it[1] shouldBe mediaTvResponseItem2.toMediaItem()
        }
      }
    }
  }

  Given("fetching movie by keywords") {
    When("the response is successful") {
      every { dataSource.getMovieByKeywords(any()) } returns
        flowOf(fakeMovieResponsePagingData)
      Then("should return mapped MediaItem") {
        testPagingFlowAwaitComplete(repository.getMovieByKeywords("1")) {
          it[0] shouldBe mediaMovieResponseItem.toMediaItem()
          it[1] shouldBe mediaMovieResponseItem2.toMediaItem()
        }
      }
    }
  }

  Given("fetching tv by keywords") {
    When("the response is successful") {
      every { dataSource.getTvByKeywords(any()) } returns
        flowOf(fakeTvResponsePagingData)
      Then("should return mapped MediaItem") {
        testPagingFlowAwaitComplete(repository.getTvByKeywords("1")) {
          it[0] shouldBe mediaTvResponseItem.toMediaItem()
          it[1] shouldBe mediaTvResponseItem2.toMediaItem()
        }
      }
    }
  }
})
