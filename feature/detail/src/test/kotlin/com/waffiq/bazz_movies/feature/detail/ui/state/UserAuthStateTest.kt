package com.waffiq.bazz_movies.feature.detail.ui.state

import org.junit.Assert.assertSame
import org.junit.Test

class UserAuthStateTest {

  @Test
  fun notInitialized_withMultipleReferences_shouldReturnSameInstance() {
    val state1 = UserAuthState.NotInitialized
    val state2 = UserAuthState.NotInitialized

    assertSame(state1, state2)
  }

  @Test
  fun guest_withMultipleReferences_shouldReturnSameInstance() {
    val state1 = UserAuthState.Guest
    val state2 = UserAuthState.Guest

    assertSame(state1, state2)
  }

  @Test
  fun loggedIn_withMultipleReferences_shouldReturnSameInstance() {
    val state1 = UserAuthState.LoggedIn
    val state2 = UserAuthState.LoggedIn

    assertSame(state1, state2)
  }
}
