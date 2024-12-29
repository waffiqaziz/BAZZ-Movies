package com.waffiq.bazz_movies.core.user.domain.usecase.authtmdbaccount

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.Post
import com.waffiq.bazz_movies.core.user.domain.model.account.AccountDetails
import com.waffiq.bazz_movies.core.user.domain.model.account.Authentication
import com.waffiq.bazz_movies.core.user.domain.model.account.CreateSession
import kotlinx.coroutines.flow.Flow

interface AuthTMDbAccountUseCase {
  suspend fun login(
    username: String,
    pass: String,
    sessionId: String
  ): Flow<Outcome<Authentication>>

  suspend fun createToken(): Flow<Outcome<Authentication>>
  suspend fun deleteSession(sessionId: String): Flow<Outcome<Post>>
  suspend fun createSessionLogin(requestToken: String): Flow<Outcome<CreateSession>>
  suspend fun getUserDetail(sessionId: String): Flow<Outcome<AccountDetails>>
}
