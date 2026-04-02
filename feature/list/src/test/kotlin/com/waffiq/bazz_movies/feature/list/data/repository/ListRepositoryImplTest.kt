package com.waffiq.bazz_movies.feature.list.data.repository

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaItem
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
import io.kotest.core.spec.style.scopes.BehaviorSpecWhenContainerScope
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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

  suspend fun BehaviorSpecWhenContainerScope.thenEmitsMappedMediaItems(
    flowProvider: () -> Flow<PagingData<MediaItem>>,
    expected: List<MediaItem>,
  ) {
    Then("emit paging data with correctly mapped MediaItems") {
      testPagingFlowAwaitComplete(flowProvider()) { items ->
        expected.forEachIndexed { index, mediaItem ->
          items[index] shouldBe mediaItem
        }
      }
    }
  }

  Given("the data source returns paging data successfully") {

    When("fetching movies by genre") {
      every { dataSource.getMovieByGenres(any(), any()) } returns
        flowOf(fakeMovieResponsePagingData)

      thenEmitsMappedMediaItems(
        flowProvider = { repository.getMovieByGenres("1", "id") },
        expected = listOf(
          mediaMovieResponseItem.toMediaItem(),
          mediaMovieResponseItem2.toMediaItem(),
        ),
      )
    }

    When("fetching tv shows by genre") {
      every { dataSource.getTvByGenres(any(), any()) } returns
        flowOf(fakeTvResponsePagingData)

      thenEmitsMappedMediaItems(
        flowProvider = { repository.getTvByGenres("1", "id") },
        expected = listOf(
          mediaTvResponseItem.toMediaItem(),
          mediaTvResponseItem2.toMediaItem(),
        ),
      )
    }

    When("fetching movies by keyword") {
      every { dataSource.getMovieByKeywords(any()) } returns
        flowOf(fakeMovieResponsePagingData)

      thenEmitsMappedMediaItems(
        flowProvider = { repository.getMovieByKeywords("1") },
        expected = listOf(
          mediaMovieResponseItem.toMediaItem(),
          mediaMovieResponseItem2.toMediaItem(),
        ),
      )
    }

    When("fetching tv shows by keyword") {
      every { dataSource.getTvByKeywords(any()) } returns
        flowOf(fakeTvResponsePagingData)

      thenEmitsMappedMediaItems(
        flowProvider = { repository.getTvByKeywords("1") },
        expected = listOf(
          mediaTvResponseItem.toMediaItem(),
          mediaTvResponseItem2.toMediaItem(),
        ),
      )
    }
  }
})
