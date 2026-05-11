package com.waffiq.bazz_movies.core.data.domain.usecase.asian

import com.waffiq.bazz_movies.core.data.testutils.BaseInteractorTest
import com.waffiq.bazz_movies.core.data.testutils.TestVariables.movieMediaItem
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetAsianMediaInteractorTest : BaseInteractorTest() {

  private lateinit var discoverInteractor: GetAsianMediaInteractor

  @Before
  override fun setup() {
    super.setup()
    discoverInteractor = GetAsianMediaInteractor(mockAsianRepository)
  }

  @Test
  fun getAnimeAllTime_whenValueIsValid_returnsDataCorrectly() =
    runTest {
      testPagingData(
        mockCall = { mockAsianRepository.getAnimeAllTime() },
        pagingData = fakeMoviePagingData,
        interactorCall = { discoverInteractor.getAnimeAllTime() },
      ) { pagingList ->
        Assert.assertEquals(movieMediaItem, pagingList[0])
      }
    }

  @Test
  fun getAnimeThisSeason_whenValueIsValid_returnsDataCorrectly() =
    runTest {
      testPagingData(
        mockCall = { mockAsianRepository.getAnimeThisSeason() },
        pagingData = fakeMoviePagingData,
        interactorCall = { discoverInteractor.getAnimeThisSeason() },
      ) { pagingList ->
        Assert.assertEquals(movieMediaItem, pagingList[0])
      }
    }

  @Test
  fun getDonghua_whenValueIsValid_returnsDataCorrectly() =
    runTest {
      testPagingData(
        mockCall = { mockAsianRepository.getDonghua() },
        pagingData = fakeMoviePagingData,
        interactorCall = { discoverInteractor.getDonghua() },
      ) { pagingList ->
        Assert.assertEquals(movieMediaItem, pagingList[0])
      }
    }

  @Test
  fun getAsianRomance_whenValueIsValid_returnsDataCorrectly() =
    runTest {
      testPagingData(
        mockCall = { mockAsianRepository.getAsianRomance() },
        pagingData = fakeMoviePagingData,
        interactorCall = { discoverInteractor.getAsianRomance() },
      ) { pagingList ->
        Assert.assertEquals(movieMediaItem, pagingList[0])
      }
    }

  @Test
  fun getCostumeDrama_whenValueIsValid_returnsDataCorrectly() =
    runTest {
      testPagingData(
        mockCall = { mockAsianRepository.getCostumeDrama() },
        pagingData = fakeMoviePagingData,
        interactorCall = { discoverInteractor.getCostumeDrama() },
      ) { pagingList ->
        Assert.assertEquals(movieMediaItem, pagingList[0])
      }
    }
}
