package com.waffiq.bazz_movies.feature.detail.ui.state

import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.Provider
import org.junit.Assert.assertEquals
import org.junit.Test

class WatchProvidersUiStateTest {

  private val sampleProvider = Provider(
    logoPath = "path",
    providerId = 1234,
    providerName = "Provider Name",
    displayPriority = 1
  )
  private val providerList = listOf(sampleProvider)

  @Test
  fun watchProvidersUiState_withSuccess_setsCorrectValues() {
    val state = WatchProvidersUiState.Success(
      ads = providerList,
      buy = providerList,
      flatrate = providerList,
      free = providerList,
      rent = providerList,
    )

    assertEquals(providerList, state.ads)
    assertEquals(providerList, state.buy)
    assertEquals(providerList, state.flatrate)
    assertEquals(providerList, state.free)
    assertEquals(providerList, state.rent)
  }

  @Test
  fun watchProvidersUiState_withError_setsCorrectMessage() {
    val message = "Something went wrong"
    val state = WatchProvidersUiState.Error(message)

    assertEquals(message, state.message)
  }

  @Test
  fun watchProvidersUiState_whenSuccess_equalsAnotherWithSameValues() {
    val state1 = WatchProvidersUiState.Success(
      ads = providerList,
      buy = providerList,
      flatrate = providerList,
      free = providerList,
      rent = providerList,
    )

    val state2 = WatchProvidersUiState.Success(
      ads = providerList,
      buy = providerList,
      flatrate = providerList,
      free = providerList,
      rent = providerList,
    )

    assertEquals(state1, state2)
  }

  @Test
  fun watchProvidersUiState_whenUsingExhaustive_whenCoversAllStates() {
    val states: List<WatchProvidersUiState> = listOf(
      WatchProvidersUiState.Loading,
      WatchProvidersUiState.Error("Error"),
      WatchProvidersUiState.Success(
        ads = emptyList(),
        buy = emptyList(),
        flatrate = emptyList(),
        free = emptyList(),
        rent = emptyList()
      )
    )

    states.forEach { state ->
      when (state) {
        is WatchProvidersUiState.Loading -> {
          /* do nothing */
        }

        is WatchProvidersUiState.Error -> {
          /* do nothing */
        }

        is WatchProvidersUiState.Success -> {
          /* do nothing */
        }
      }
    }
  }
}
