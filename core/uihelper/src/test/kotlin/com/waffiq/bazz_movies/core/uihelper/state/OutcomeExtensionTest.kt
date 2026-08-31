package com.waffiq.bazz_movies.core.uihelper.state

import app.cash.turbine.test
import com.waffiq.bazz_movies.core.models.Outcome
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class OutcomeExtensionTest {

  @Test
  fun asUiState_whenOutcomeIsSuccess_emitsUiStateSuccessWithSameData() =
    runTest {
      val outcome = Outcome.Success("payload")

      flowOf(outcome).asUiState().test {
        val result = awaitItem()
        assertIs<UIState.Success<String>>(result)
        assertEquals("payload", result.data)
        awaitComplete()
      }
    }

  @Test
  fun asUiState_whenOutcomeIsLoading_emitsUiStateLoading() =
    runTest {
      val outcome: Outcome<String> = Outcome.Loading

      flowOf(outcome).asUiState().test {
        val result = awaitItem()
        assertTrue(result is UIState.Loading)
        awaitComplete()
      }
    }

  @Test
  fun asUiState_whenOutcomeIsError_emitsUiStateErrorWithSameMessage() =
    runTest {
      val outcome: Outcome<String> = Outcome.Error("something went wrong")

      flowOf(outcome).asUiState().test {
        val result = awaitItem()
        assertIs<UIState.Error>(result)
        assertEquals("something went wrong", result.message)
        awaitComplete()
      }
    }

  @Test
  fun asUiState_whenMultipleOutcomesEmitted_mapsEachInOriginalOrder() =
    runTest {
      val outcomes = flowOf(
        Outcome.Loading,
        Outcome.Success("data"),
        Outcome.Error("failure"),
      )

      outcomes.asUiState().test {
        assertTrue(awaitItem() is UIState.Loading)
        assertIs<UIState.Success<String>>(awaitItem())
        assertIs<UIState.Error>(awaitItem())
        awaitComplete()
      }
    }

  @Test
  fun asUiState_whenSourceFlowIsEmpty_emitsNothingAndCompletes() =
    runTest {
      val outcomes = flowOf<Outcome<String>>()

      outcomes.asUiState().test {
        awaitComplete()
      }
    }
}
