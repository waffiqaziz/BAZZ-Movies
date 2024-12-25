package com.waffiq.bazz_movies.core.user.domain.usecase.authtmdbaccount

import com.waffiq.bazz_movies.core.data.Post
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import com.waffiq.bazz_movies.core.user.domain.model.account.AccountDetails
import com.waffiq.bazz_movies.core.user.domain.model.account.Authentication
import com.waffiq.bazz_movies.core.user.domain.model.account.CreateSession
import com.waffiq.bazz_movies.core.user.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthTMDbAccountInteractor @Inject constructor(
  private val authTMDbAccountRepository: IUserRepository
) : AuthTMDbAccountUseCase {
  override suspend fun login(
    username: String,
    pass: String,
    sessionId: String
  ): Flow<NetworkResult<Authentication>> =
    authTMDbAccountRepository.login(username, pass, sessionId)

  override suspend fun createToken(): Flow<NetworkResult<Authentication>> =
    authTMDbAccountRepository.createToken()

  override suspend fun deleteSession(sessionId: String): Flow<NetworkResult<Post>> =
    authTMDbAccountRepository.deleteSession(sessionId)

  override suspend fun createSessionLogin(requestToken: String): Flow<NetworkResult<CreateSession>> =
    authTMDbAccountRepository.createSessionLogin(requestToken)

  override suspend fun getUserDetail(sessionId: String): Flow<NetworkResult<AccountDetails>> =
    authTMDbAccountRepository.getUserDetail(sessionId)
}
