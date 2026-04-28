package com.waffiq.bazz_movies.core.user.domain.usecase.authtmdbaccount

import com.waffiq.bazz_movies.core.common.utils.Constants.NAN
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.PostResult
import com.waffiq.bazz_movies.core.domain.UserModel
import com.waffiq.bazz_movies.core.user.domain.model.account.AccountDetails
import com.waffiq.bazz_movies.core.user.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthTMDbAccountInteractor @Inject constructor(
  private val authTMDbAccountRepository: IUserRepository,
) : AuthTMDbAccountUseCase {

  // used only as helper the flow chain
  private class StepFailureException(val outcomeError: Outcome.Error) : Exception()

  override fun login(username: String, password: String): Flow<Outcome<Unit>> =
    flow {
      emit(Outcome.Loading)

      // Official TMDB authentication can be seen here:
      // https://developer.themoviedb.org/reference/authentication-how-do-i-generate-a-session-id
      try {
        // Step 1: Create token
        val requestToken = authTMDbAccountRepository.createToken()
          .unwrapStep()
          .data.requestToken
          ?: throw StepFailureException(Outcome.Error("Request token was null"))

        // Step 2: Authorize token
        val authorizedToken = authTMDbAccountRepository.login(username, password, requestToken)
          .unwrapStep()
          .data.requestToken
          ?: throw StepFailureException(Outcome.Error("Authorized token was null"))

        // Step 3: Create session
        val session = authTMDbAccountRepository.createSessionLogin(authorizedToken)
          .unwrapStep()
          .data
        if (!session.success) throw StepFailureException(Outcome.Error("Session creation failed"))
        val sessionId = session.sessionId

        // Step 4: Get account details
        val accountDetails = authTMDbAccountRepository.getAccountDetails(sessionId)
          .unwrapStep()
          .data

        // Step 5: Save user
        authTMDbAccountRepository.saveUserPref(accountDetails.buildUserModel(sessionId))
        emit(Outcome.Success(Unit))
      } catch (e: StepFailureException) {
        emit(e.outcomeError)
      }
    }

  /**
   * Filters loading states, takes the first resolved outcome, and either returns
   * [Outcome.Success] or throws [StepFailureException]
   */
  private suspend fun <T> Flow<Outcome<T>>.unwrapStep(): Outcome.Success<T> {
    val outcome = filterNot { it is Outcome.Loading }.firstOrNull()
    return when (outcome) {
      is Outcome.Success -> outcome
      is Outcome.Error -> throw StepFailureException(outcome)
      else -> throw StepFailureException(Outcome.Error("No outcome emitted from step"))
    }
  }

  private fun AccountDetails.buildUserModel(sessionId: String) =
    UserModel(
      userId = id ?: 0,
      name = name.toString(),
      username = username.toString(),
      password = NAN,
      region = NAN,
      token = sessionId,
      isLogin = true,
      gravatarHash = avatarItem?.gravatar?.hash,
      tmdbAvatar = avatarItem?.avatarTMDb?.avatarPath,
    )

  override fun deleteSession(sessionId: String): Flow<Outcome<PostResult>> =
    authTMDbAccountRepository.deleteSession(sessionId)
}
