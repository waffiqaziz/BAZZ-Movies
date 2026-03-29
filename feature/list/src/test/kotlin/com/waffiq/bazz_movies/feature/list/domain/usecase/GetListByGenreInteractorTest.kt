package com.waffiq.bazz_movies.feature.list.domain.usecase

import com.waffiq.bazz_movies.core.mappers.MediaItemMapper.toMediaItem
import com.waffiq.bazz_movies.core.test.PagingFlowHelperTest.testPagingFlowCancelRemaining
import com.waffiq.bazz_movies.core.user.domain.usecase.userpreference.UserPrefUseCase
import com.waffiq.bazz_movies.feature.list.domain.repository.IListRepository
import com.waffiq.bazz_movies.feature.list.testutils.DummyData.fakeMovieMediaItemPagingData
import com.waffiq.bazz_movies.feature.list.testutils.DummyData.fakeTvMediaItemPagingData
import com.waffiq.bazz_movies.feature.list.testutils.DummyData.mediaMovieResponseItem
import com.waffiq.bazz_movies.feature.list.testutils.DummyData.mediaTvResponseItem
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain

class GetListByGenreInteractorTest : BehaviorSpec({

  val repository: IListRepository = mockk(relaxed = true)
  val userPref: UserPrefUseCase = mockk(relaxed = true)

  lateinit var interactor: GetListInteractor

  beforeTest {
    Dispatchers.setMain(UnconfinedTestDispatcher())
    interactor = GetListInteractor(repository, userPref)
  }

  afterTest {
    clearMocks(repository, userPref)
  }

  Given("fetching movies by genre") {
    When("the response is successful") {
      coEvery { repository.getMovieByGenres(any(), any()) } returns
        flowOf(fakeMovieMediaItemPagingData)

      And("valid user region") {
        every { userPref.getUserRegionPref() } returns flowOf("US")

        Then("return correct movie") {
          testPagingFlowCancelRemaining(interactor.getMovieByGenres("1")) {
            it[0] shouldBe mediaMovieResponseItem.toMediaItem()
          }
        }
      }
    }
  }

  Given("fetching tv by genre") {
    When("the response is successful") {
      coEvery { repository.getTvByGenres(any(), any()) } returns
        flowOf(fakeTvMediaItemPagingData)

      And("valid user region") {
        every { userPref.getUserRegionPref() } returns flowOf("ID")

        Then("return correct tv series") {
          testPagingFlowCancelRemaining(interactor.getTvByGenres("1")) {
            it[0] shouldBe mediaTvResponseItem.toMediaItem()
          }
        }
      }
    }
  }

  Given("fetching movies by keywords") {
    When("the response is successful") {
      coEvery { repository.getMovieByKeywords(any()) } returns
        flowOf(fakeMovieMediaItemPagingData)

      Then("return correct movie") {
        testPagingFlowCancelRemaining(interactor.getMovieByKeywords("1")) {
          it[0] shouldBe mediaMovieResponseItem.toMediaItem()
        }
      }
    }
  }

  Given("fetching tv by keywords") {
    When("the response is successful") {
      coEvery { repository.getTvByKeywords(any()) } returns
        flowOf(fakeTvMediaItemPagingData)

      Then("return correct tv series") {
        testPagingFlowCancelRemaining(interactor.getTvByKeywords("1")) {
          it[0] shouldBe mediaTvResponseItem.toMediaItem()
        }
      }
    }
  }
})
