package com.waffiq.bazz_movies.core.data.domain.usecase.composite

import app.cash.turbine.test
import com.waffiq.bazz_movies.core.data.testutils.BaseInteractorTest
import com.waffiq.bazz_movies.core.data.testutils.TestVariables.favoriteParams
import com.waffiq.bazz_movies.core.data.testutils.TestVariables.postFavoriteWatchlistSuccess
import com.waffiq.bazz_movies.core.data.testutils.TestVariables.watchlistParams
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.UserModel
import io.mockk.coEvery
import io.mockk.coVerify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class PostActionInteractorTest : BaseInteractorTest() {

  private lateinit var postActionInteractor: PostActionInteractor

  @Before
  fun setUp() {
    postActionInteractor = PostActionInteractor(mockAccountRepository, mockUserRepository)
    coEvery { mockUserRepository.getUserPref() } returns
      flowOf(
        UserModel(
          userId = 1234,
          name = "name",
          username = "username",
          password = "",
          region = "id",
          token = "token",
          isLogin = true,
          gravatarHash = "hash",
          tmdbAvatar = "tmdb",
        ),
      )
  }

  @Test
  fun postFavorite_whenSuccessful_emitsSuccess() =
    runTest {
      coEvery { mockAccountRepository.postFavorite(any(), favoriteParams, any()) } returns
        flowOf(Outcome.Success(postFavoriteWatchlistSuccess))

      postActionInteractor.postFavoriteWithAuth(favoriteParams).test {
        val result = awaitItem()
        assertTrue(result is Outcome.Success)
        result as Outcome.Success
        assertEquals(postFavoriteWatchlistSuccess, result.data)
        awaitComplete()
      }
      coVerify { mockAccountRepository.postFavorite(any(), favoriteParams, any()) }
    }

  @Test
  fun postWatchlist_whenSuccessful_emitsSuccess() =
    runTest {
      coEvery { mockAccountRepository.postWatchlist(any(), watchlistParams, any()) } returns
        flowOf(Outcome.Success(postFavoriteWatchlistSuccess))

      postActionInteractor.postWatchlistWithAuth(watchlistParams).test {
        val result = awaitItem()
        assertTrue(result is Outcome.Success)
        result as Outcome.Success
        assertEquals(postFavoriteWatchlistSuccess, result.data)
        awaitComplete()
      }
      coVerify { mockAccountRepository.postWatchlist(any(), watchlistParams, any()) }
    }
}
