package com.waffiq.bazz_movies.core.data.domain.usecase.asian

import com.waffiq.bazz_movies.core.data.testutils.BaseInteractorTest
import com.waffiq.bazz_movies.core.data.testutils.TestVariables.tvMediaItem
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
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
        pagingData = fakeTvPagingData,
        interactorCall = { discoverInteractor.getAnimeAllTime() },
      ) { pagingList ->
        assertEquals(tvMediaItem, pagingList[0])
      }
    }

  @Test
  fun getAnimeThisSeason_whenValueIsValid_returnsDataCorrectly() =
    runTest {
      testPagingData(
        mockCall = { mockAsianRepository.getAnimeThisSeason() },
        pagingData = fakeTvPagingData,
        interactorCall = { discoverInteractor.getAnimeThisSeason() },
      ) { pagingList ->
        assertEquals(tvMediaItem, pagingList[0])
      }
    }

  @Test
  fun getDonghua_whenValueIsValid_returnsDataCorrectly() =
    runTest {
      testPagingData(
        mockCall = { mockAsianRepository.getDonghua() },
        pagingData = fakeTvPagingData,
        interactorCall = { discoverInteractor.getDonghua() },
      ) { pagingList ->
        assertEquals(tvMediaItem, pagingList[0])
      }
    }

  @Test
  fun getAsianRomance_whenValueIsValid_returnsDataCorrectly() =
    runTest {
      testPagingData(
        mockCall = { mockAsianRepository.getAsianRomance() },
        pagingData = fakeTvPagingData,
        interactorCall = { discoverInteractor.getAsianRomance() },
      ) { pagingList ->
        assertEquals(tvMediaItem, pagingList[0])
      }
    }

  @Test
  fun getCostumeDrama_whenValueIsValid_returnsDataCorrectly() =
    runTest {
      testPagingData(
        mockCall = { mockAsianRepository.getCostumeDrama() },
        pagingData = fakeTvPagingData,
        interactorCall = { discoverInteractor.getCostumeDrama() },
      ) { pagingList ->
        assertEquals(tvMediaItem, pagingList[0])
      }
    }

  @Test
  fun getRealityShow_whenValueIsValid_returnsDataCorrectly() =
    runTest {
      testPagingData(
        mockCall = { mockAsianRepository.getRealityShow() },
        pagingData = fakeTvPagingData,
        interactorCall = { discoverInteractor.getRealityShow() },
      ) { pagingList ->
        assertEquals(tvMediaItem, pagingList[0])
      }
    }
}
