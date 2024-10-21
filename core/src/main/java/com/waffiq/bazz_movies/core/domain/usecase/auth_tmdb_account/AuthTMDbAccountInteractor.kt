package com.waffiq.bazz_movies.core.domain.usecase.auth_tmdb_account

import com.waffiq.bazz_movies.core.data.remote.post_body.SessionIDPostModel
import com.waffiq.bazz_movies.core.domain.model.account.AccountDetails
import com.waffiq.bazz_movies.core.domain.model.account.Authentication
import com.waffiq.bazz_movies.core.domain.model.account.CreateSession
import com.waffiq.bazz_movies.core.domain.model.post.Post
import com.waffiq.bazz_movies.core.domain.repository.IUserRepository
import com.waffiq.bazz_movies.core.utils.result.NetworkResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthTMDbAccountInteractor @Inject constructor(
  private val authTMDbAccountRepository: IUserRepository
) : AuthTMDbAccountUseCase {
  override suspend fun login(
    username: String,
    pass: String,
    token: String
  ): Flow<NetworkResult<Authentication>> =
    authTMDbAccountRepository.login(username, pass, token)

  override suspend fun createToken(): Flow<NetworkResult<Authentication>> =
    authTMDbAccountRepository.createToken()

  override suspend fun deleteSession(data: SessionIDPostModel): Flow<NetworkResult<Post>> =
    authTMDbAccountRepository.deleteSession(data)

  override suspend fun createSessionLogin(token: String): Flow<NetworkResult<CreateSession>> =
    authTMDbAccountRepository.createSessionLogin(token)

  override suspend fun getUserDetail(sessionId: String): Flow<NetworkResult<AccountDetails>> =
    authTMDbAccountRepository.getUserDetail(sessionId)
}