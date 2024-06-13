package com.waffiq.bazz_movies.domain.usecase.auth_tmdb_account

import com.waffiq.bazz_movies.data.local.model.UserModel
import com.waffiq.bazz_movies.data.remote.post_body.SessionIDPostModel
import com.waffiq.bazz_movies.domain.model.account.AccountDetails
import com.waffiq.bazz_movies.domain.model.account.Authentication
import com.waffiq.bazz_movies.domain.model.account.CreateSession
import com.waffiq.bazz_movies.domain.model.post.Post
import com.waffiq.bazz_movies.domain.repository.IUserRepository
import com.waffiq.bazz_movies.utils.NetworkResult
import kotlinx.coroutines.flow.Flow

class AuthTMDbAccountInteractor(
  private val authTMDbAccountRepository : IUserRepository
) : AuthTMDbAccountUseCase {
  override suspend fun login(
    username: String,
    pass: String,
    token: String
  ): Flow<NetworkResult<Authentication>> =
    authTMDbAccountRepository.login(username,pass,token)

  override suspend fun createToken(): Flow<NetworkResult<Authentication>> =
    authTMDbAccountRepository.createToken()

  override suspend fun deleteSession(data: SessionIDPostModel): Flow<NetworkResult<Post>> =
    authTMDbAccountRepository.deleteSession(data)

  override suspend fun createSessionLogin(token: String): Flow<NetworkResult<CreateSession>> =
    authTMDbAccountRepository.createSessionLogin(token)

  override suspend fun getUserDetail(sessionId: String): Flow<NetworkResult<AccountDetails>> =
    authTMDbAccountRepository.getUserDetail(sessionId)
}