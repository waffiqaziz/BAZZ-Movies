package com.waffiq.bazz_movies.core.data.data.repository

import app.cash.turbine.test
import com.waffiq.bazz_movies.core.data.testutils.TestVariables.favoriteParams
import com.waffiq.bazz_movies.core.data.testutils.TestVariables.postFavoriteWatchlistResponseSuccess
import com.waffiq.bazz_movies.core.data.testutils.TestVariables.watchlistParams
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.network.data.remote.datasource.account.AccountRemoteDataSource
import com.waffiq.bazz_movies.core.network.utils.mappers.NetworkMapper.toFavoriteRequest
import com.waffiq.bazz_movies.core.network.utils.mappers.NetworkMapper.toWatchlistRequest
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AccountRepositoryTest {

  private lateinit var accountRepository: AccountRepositoryImpl
  private val mockAccountRemoteDataSource: AccountRemoteDataSource = mockk()

  @Before
  fun setup() {
    accountRepository = AccountRepositoryImpl(mockAccountRemoteDataSource)
  }

  @Test
  fun postFavorite_whenSuccessful_returnsCorrectResponse() =
    runTest {
      coEvery {
        mockAccountRemoteDataSource.postFavorite(
          "sessionId",
          favoriteParams.toFavoriteRequest(),
          12345678,
        )
      } returns flowOf(NetworkResult.Success(postFavoriteWatchlistResponseSuccess))

      accountRepository.postFavorite("sessionId", favoriteParams, 12345678).test {
        val result = awaitItem()
        assertTrue(result is Outcome.Success)
        result as Outcome.Success
        assertEquals("Success", result.data.statusMessage)
        assertEquals(201, result.data.statusCode)
        awaitComplete()
      }
    }

  @Test
  fun postWatchlist_whenSuccessful_returnsCorrectResponse() =
    runTest {
      coEvery {
        mockAccountRemoteDataSource.postWatchlist(
          "sessionId",
          watchlistParams.toWatchlistRequest(),
          666666,
        )
      } returns flowOf(NetworkResult.Success(postFavoriteWatchlistResponseSuccess))

      accountRepository.postWatchlist("sessionId", watchlistParams, 666666).test {
        val result = awaitItem()
        assertTrue(result is Outcome.Success)
        result as Outcome.Success
        assertEquals("Success", result.data.statusMessage)
        assertEquals(201, result.data.statusCode)
        awaitComplete()
      }
    }
}
