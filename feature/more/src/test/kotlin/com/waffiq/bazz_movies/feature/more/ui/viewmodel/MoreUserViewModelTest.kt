package com.waffiq.bazz_movies.feature.more.ui.viewmodel

import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.test
import com.waffiq.bazz_movies.core.models.Outcome
import com.waffiq.bazz_movies.core.models.PostResult
import com.waffiq.bazz_movies.core.uihelper.state.UIState
import com.waffiq.bazz_movies.core.user.domain.usecase.authtmdbaccount.AuthTMDbAccountUseCase
import com.waffiq.bazz_movies.feature.more.testutils.BaseViewModelTest
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MoreUserViewModelTest : BaseViewModelTest() {

  private val sessionId = "session_id"
  private val authTMDbAccountUseCase: AuthTMDbAccountUseCase = mockk()
  private lateinit var viewModel: MoreUserViewModel

  @Before
  fun setUp() {
    viewModel = MoreUserViewModel(authTMDbAccountUseCase)
  }

  @Test
  fun deleteSession_whenSuccessful_returnsSuccess() =
    runTest {
      viewModel.state.test {
        assertDeleteSession(
          outcome = Outcome.Success(PostResult(success = true)),
          expectedState = UIState.Success(Unit),
        )
      }
    }

  @Test
  fun deleteSession_whenUnsuccessful_returnsError() =
    runTest {
      viewModel.state.test {
        assertDeleteSession(
          outcome = Outcome.Error(errorMessage),
          expectedState = UIState.Error(errorMessage),
        )
      }
    }

  @Test
  fun deleteSession_whenLoading_returnsLoading() =
    runTest {
      viewModel.state.test {
        assertDeleteSession(
          outcome = Outcome.Loading,
          expectedState = UIState.Loading,
        )
      }
    }

  @Test
  fun removeState_whenCalled_updatesSignOutStateToLoading() =
    runTest {
      viewModel.state.test {
        assertEquals(UIState.Idle, awaitItem())
        viewModel.removeState()
        expectNoEvents()
      }
    }

  private suspend fun ReceiveTurbine<UIState<Unit>>.assertDeleteSession(
    outcome: Outcome<PostResult>,
    expectedState: UIState<Unit>,
  ) {
    coEvery { authTMDbAccountUseCase.deleteSession(sessionId) } returns flowOf(outcome)

    assertEquals(UIState.Idle, awaitItem())
    viewModel.deleteSession(sessionId)

    assertEquals(expectedState, awaitItem())
    cancelAndIgnoreRemainingEvents()
  }
}
