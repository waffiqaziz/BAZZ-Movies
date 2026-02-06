package com.waffiq.bazz_movies.core.user.domain.usecase.authtmdbaccount

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.PostResult
import com.waffiq.bazz_movies.core.user.domain.model.account.AccountDetails
import com.waffiq.bazz_movies.core.user.domain.model.account.Authentication
import com.waffiq.bazz_movies.core.user.domain.model.account.CreateSession
import com.waffiq.bazz_movies.core.user.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthTMDbAccountInteractor @Inject constructor(
  private val authTMDbAccountRepository: IUserRepository,
) : AuthTMDbAccountUseCase {
  override fun login(
    username: String,
    pass: String,
    sessionId: String,
  ): Flow<Outcome<Authentication>> =
    authTMDbAccountRepository.login(username, pass, sessionId)

  override fun createToken(): Flow<Outcome<Authentication>> =
    authTMDbAccountRepository.createToken()

  override fun deleteSession(sessionId: String): Flow<Outcome<PostResult>> =
    authTMDbAccountRepository.deleteSession(sessionId)

  override fun createSessionLogin(requestToken: String): Flow<Outcome<CreateSession>> =
    authTMDbAccountRepository.createSessionLogin(requestToken)

  override fun getAccountDetails(sessionId: String): Flow<Outcome<AccountDetails>> =
    authTMDbAccountRepository.getAccountDetails(sessionId)
}
