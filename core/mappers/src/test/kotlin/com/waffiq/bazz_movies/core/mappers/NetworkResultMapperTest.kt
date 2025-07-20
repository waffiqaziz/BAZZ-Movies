package com.waffiq.bazz_movies.core.mappers

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.mappers.NetworkResultMapper.toOutcome
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class NetworkResultMapperTest {

  data class TestData(val id: Int, val name: String)
  data class MappedData(val id: Int, val title: String)

  @Test
  fun toOutcome_withSuccessResult_returnsSuccessOutcome() = runTest {
    val testData = TestData(1, "test")
    val networkResultFlow = flowOf(NetworkResult.Success(testData))

    val outcomeFlow = networkResultFlow.toOutcome { MappedData(it.id, it.name) }
    val result = outcomeFlow.toList()

    assertEquals(1, result.size)
    assertTrue(result[0] is Outcome.Success)
    assertEquals(1, (result[0] as Outcome.Success).data.id)
    assertEquals("test", (result[0] as Outcome.Success).data.title)
  }

  @Test
  fun toOutcome_withErrorResult_returnsErrorOutcome() = runTest {
    val errorMessage = "Network error"
    val networkResultFlow = flowOf<NetworkResult<TestData>>(NetworkResult.Error(errorMessage))

    val outcomeFlow = networkResultFlow.toOutcome { MappedData(it.id, it.name) }
    val result = outcomeFlow.toList()

    assertEquals(1, result.size)
    assertTrue(result[0] is Outcome.Error)
    assertEquals(errorMessage, (result[0] as Outcome.Error).message)
  }

  @Test
  fun toOutcome_withLoadingResult_returnsLoadingOutcome() = runTest {
    val networkResultFlow = flowOf<NetworkResult<TestData>>(NetworkResult.Loading)

    val outcomeFlow = networkResultFlow.toOutcome { MappedData(it.id, it.name) }
    val result = outcomeFlow.toList()

    assertEquals(1, result.size)
    assertTrue(result[0] is Outcome.Loading)
  }

  @Test
  fun toOutcome_withMultipleResults_returnsCorrectOutcomes() = runTest {
    val testData = TestData(1, "test")
    val networkResultFlow = flowOf(
      NetworkResult.Loading,
      NetworkResult.Success(testData),
      NetworkResult.Error("Error")
    )

    val outcomeFlow = networkResultFlow.toOutcome { MappedData(it.id, it.name) }
    val results = outcomeFlow.toList()

    assertEquals(3, results.size)
    assertTrue(results[0] is Outcome.Loading)
    assertTrue(results[1] is Outcome.Success)
    assertTrue(results[2] is Outcome.Error)
  }
}
