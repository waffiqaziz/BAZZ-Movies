package com.waffiq.bazz_movies.core.domain.usecase.auth_tmdb_account

import com.waffiq.bazz_movies.core.data.remote.post_body.SessionIDPostModel
import com.waffiq.bazz_movies.core.domain.model.account.AccountDetails
import com.waffiq.bazz_movies.core.domain.model.account.Authentication
import com.waffiq.bazz_movies.core.domain.model.account.CreateSession
import com.waffiq.bazz_movies.core.domain.model.post.Post
import com.waffiq.bazz_movies.core.utils.result.NetworkResult
import kotlinx.coroutines.flow.Flow

interface AuthTMDbAccountUseCase {
  suspend fun login(
    username: String,
    pass: String,
    token: String
  ): Flow<NetworkResult<Authentication>>

  suspend fun createToken(): Flow<NetworkResult<Authentication>>
  suspend fun deleteSession(data: SessionIDPostModel): Flow<NetworkResult<Post>>
  suspend fun createSessionLogin(token: String): Flow<NetworkResult<CreateSession>>
  suspend fun getUserDetail(sessionId: String): Flow<NetworkResult<AccountDetails>>
}