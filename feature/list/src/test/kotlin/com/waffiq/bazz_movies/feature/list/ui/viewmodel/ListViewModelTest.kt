package com.waffiq.bazz_movies.feature.list.ui.viewmodel

import com.waffiq.bazz_movies.core.test.KotestInstantExecutorExtension
import com.waffiq.bazz_movies.core.test.PagingFlowHelperTest.testPagingFlowCancelRemaining
import com.waffiq.bazz_movies.feature.list.domain.usecase.GetListUseCase
import com.waffiq.bazz_movies.feature.list.testutils.DummyData.fakeMovieMediaItemPagingData
import com.waffiq.bazz_movies.feature.list.testutils.DummyData.fakeTvMediaItemPagingData
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

class ListViewModelTest : BehaviorSpec({

  extensions(KotestInstantExecutorExtension)
  val testDispatcher = UnconfinedTestDispatcher()
  val getListUseCase: GetListUseCase = mockk()

  lateinit var viewModel: ListViewModel

  beforeTest {
    Dispatchers.setMain(testDispatcher)
    viewModel = ListViewModel(getListUseCase)
  }

  afterTest {
    Dispatchers.resetMain()
  }

  Given("fetching movies by genre") {
    When("the response is successful") {
      coEvery { getListUseCase.getMovieByGenres(any()) } returns
        flowOf(fakeMovieMediaItemPagingData)

      Then("return correct movie") {
        testPagingFlowCancelRemaining(viewModel.getMovieByGenres("1")) {
          it[0].id shouldBe 1
          it[0].title shouldBe "Inception"
          it[0].overview shouldBe "A mind-bending thriller"
        }
      }
    }
  }

  Given("fetching tv by genre") {
    When("the response is successful") {
      coEvery { getListUseCase.getTvByGenres(any()) } returns
        flowOf(fakeTvMediaItemPagingData)

      Then("return correct movie") {
        testPagingFlowCancelRemaining(viewModel.getTvByGenres("1")) {
          it[0].id shouldBe 1
          it[0].title shouldBe "Breaking Bad"
          it[0].overview shouldBe "A high school chemistry teacher turned methamphetamine producer"
        }
      }
    }
  }

  Given("fetching movies by keywords") {
    When("the response is successful") {
      coEvery { getListUseCase.getMovieByKeywords(any()) } returns
        flowOf(fakeMovieMediaItemPagingData)

      Then("return correct movie") {
        testPagingFlowCancelRemaining(viewModel.getMovieByKeywords("1")) {
          it[0].id shouldBe 1
          it[0].title shouldBe "Inception"
          it[0].overview shouldBe "A mind-bending thriller"
        }
      }
    }
  }

  Given("fetching tv by keywords") {
    When("the response is successful") {
      coEvery { getListUseCase.getTvByKeywords(any()) } returns
        flowOf(fakeTvMediaItemPagingData)

      Then("return correct movie") {
        testPagingFlowCancelRemaining(viewModel.getTvByKeywords("1")) {
          it[0].id shouldBe 1
          it[0].title shouldBe "Breaking Bad"
          it[0].overview shouldBe "A high school chemistry teacher turned methamphetamine producer"
        }
      }
    }
  }
})