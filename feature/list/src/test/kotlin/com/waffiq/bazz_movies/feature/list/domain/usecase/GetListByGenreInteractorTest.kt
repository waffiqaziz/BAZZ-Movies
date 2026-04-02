package com.waffiq.bazz_movies.feature.list.domain.usecase

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.mappers.MediaItemMapper.toMediaItem
import com.waffiq.bazz_movies.core.test.PagingFlowHelperTest.testPagingFlowCancelRemaining
import com.waffiq.bazz_movies.core.user.domain.repository.IUserRepository
import com.waffiq.bazz_movies.feature.list.domain.repository.IListRepository
import com.waffiq.bazz_movies.feature.list.testutils.DummyData.fakeMovieMediaItemPagingData
import com.waffiq.bazz_movies.feature.list.testutils.DummyData.fakeTvMediaItemPagingData
import com.waffiq.bazz_movies.feature.list.testutils.DummyData.mediaMovieResponseItem
import com.waffiq.bazz_movies.feature.list.testutils.DummyData.mediaTvResponseItem
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.spec.style.scopes.BehaviorSpecWhenContainerScope
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain

class GetListByGenreInteractorTest : BehaviorSpec({

  val mockListRepository: IListRepository = mockk()
  val mockUserRepository: IUserRepository = mockk()

  lateinit var interactor: GetListInteractor

  beforeTest {
    Dispatchers.setMain(UnconfinedTestDispatcher())
    interactor = GetListInteractor(mockListRepository, mockUserRepository)
  }

  afterTest {
    clearMocks(mockListRepository, mockUserRepository)
  }

  suspend fun BehaviorSpecWhenContainerScope.thenEmitsCorrectItem(
    flowProvider: () -> Flow<PagingData<MediaItem>>,
    expected: MediaItem,
  ) {
    Then("emit paging data containing the correct media item") {
      testPagingFlowCancelRemaining(flowProvider()) {
        it[0] shouldBe expected
      }
    }
  }

  Given("a genre id and a valid user region are provided") {

    When("fetching movies by genre") {
      coEvery { mockListRepository.getMovieByGenres(any(), any()) } returns
        flowOf(fakeMovieMediaItemPagingData)
      every { mockUserRepository.getUserRegionPref() } returns flowOf("US")

      thenEmitsCorrectItem(
        flowProvider = { interactor.getMovieByGenres("1") },
        expected = mediaMovieResponseItem.toMediaItem(),
      )
    }

    When("fetching tv shows by genre") {
      coEvery { mockListRepository.getTvByGenres(any(), any()) } returns
        flowOf(fakeTvMediaItemPagingData)
      every { mockUserRepository.getUserRegionPref() } returns flowOf("ID")

      thenEmitsCorrectItem(
        flowProvider = { interactor.getTvByGenres("1") },
        expected = mediaTvResponseItem.toMediaItem(),
      )
    }
  }

  Given("a keyword id is provided") {

    When("fetching movies by keyword") {
      coEvery { mockListRepository.getMovieByKeywords(any()) } returns
        flowOf(fakeMovieMediaItemPagingData)

      thenEmitsCorrectItem(
        flowProvider = { interactor.getMovieByKeywords("1") },
        expected = mediaMovieResponseItem.toMediaItem(),
      )
    }

    When("fetching tv shows by keyword") {
      coEvery { mockListRepository.getTvByKeywords(any()) } returns
        flowOf(fakeTvMediaItemPagingData)

      thenEmitsCorrectItem(
        flowProvider = { interactor.getTvByKeywords("1") },
        expected = mediaTvResponseItem.toMediaItem(),
      )
    }
  }
})