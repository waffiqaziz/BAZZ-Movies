package com.waffiq.bazz_movies.feature.detail.ui.state

/**
 * Represents the authentication state of the user in the application.
 * This sealed class defines three possible states:
 * - [NotInitialized]: The user authentication state has not been initialized.
 * - [Guest]: The user as a guest without logging in.
 * - [LoggedIn]: The user is logged in with their TMDB account.
 */
sealed class UserAuthState {
  object NotInitialized : UserAuthState()
  object Guest : UserAuthState()
  object LoggedIn : UserAuthState()
}
