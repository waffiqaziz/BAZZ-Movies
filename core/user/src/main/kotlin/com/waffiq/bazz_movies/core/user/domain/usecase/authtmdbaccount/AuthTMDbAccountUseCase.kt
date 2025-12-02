package com.waffiq.bazz_movies.core.user.domain.usecase.authtmdbaccount

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.Post
import com.waffiq.bazz_movies.core.user.domain.model.account.AccountDetails
import com.waffiq.bazz_movies.core.user.domain.model.account.Authentication
import com.waffiq.bazz_movies.core.user.domain.model.account.CreateSession
import kotlinx.coroutines.flow.Flow

interface AuthTMDbAccountUseCase {
  fun login(
    username: String,
    pass: String,
    sessionId: String,
  ): Flow<Outcome<Authentication>>

  fun createToken(): Flow<Outcome<Authentication>>
  fun deleteSession(sessionId: String): Flow<Outcome<Post>>
  fun createSessionLogin(requestToken: String): Flow<Outcome<CreateSession>>
  fun getUserDetail(sessionId: String): Flow<Outcome<AccountDetails>>
}
