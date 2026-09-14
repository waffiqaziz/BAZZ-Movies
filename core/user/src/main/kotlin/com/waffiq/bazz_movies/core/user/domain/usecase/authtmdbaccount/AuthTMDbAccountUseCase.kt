package com.waffiq.bazz_movies.core.user.domain.usecase.authtmdbaccount

import com.waffiq.bazz_movies.core.common.Outcome
import com.waffiq.bazz_movies.core.model.user.PostResult
import kotlinx.coroutines.flow.Flow

interface AuthTMDbAccountUseCase {
  fun login(username: String, password: String): Flow<Outcome<Unit>>
  fun deleteSession(sessionId: String): Flow<Outcome<PostResult>>
}
