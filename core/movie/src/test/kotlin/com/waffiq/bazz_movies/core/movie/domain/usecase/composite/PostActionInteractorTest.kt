package com.waffiq.bazz_movies.core.movie.domain.usecase.composite

import app.cash.turbine.test
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.domain.FavoriteModel
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.UserModel
import com.waffiq.bazz_movies.core.domain.WatchlistModel
import com.waffiq.bazz_movies.core.movie.domain.model.post.PostFavoriteWatchlist
import com.waffiq.bazz_movies.core.movie.domain.repository.IMoviesRepository
import com.waffiq.bazz_movies.core.user.domain.repository.IUserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class PostActionInteractorTest {

  private val moviesRepository: IMoviesRepository = mockk()
  private val userRepository: IUserRepository = mockk()
  private lateinit var postActionInteractor: PostActionInteractor

  val response = PostFavoriteWatchlist(201, "success")

  @Before
  fun setUp() {
    postActionInteractor = PostActionInteractor(moviesRepository, userRepository)
    coEvery { userRepository.getUserPref() } returns
      flowOf(
        UserModel(
          userId = 1234,
          name = "name",
          username = "username",
          password = "",
          region = "id",
          token = "token",
          isLogin = true,
          gravatarHast = "hash",
          tmdbAvatar = "tmdb"
        )
      )
  }

  @Test
  fun postFavorite_whenSuccessful_emitsSuccess() = runTest {
    val data = FavoriteModel(
      mediaType = MOVIE_MEDIA_TYPE,
      mediaId = 1234,
      favorite = true
    )
    val response = PostFavoriteWatchlist(200, "success")

    coEvery { moviesRepository.postFavorite(any(), data, any()) } returns
      flowOf(Outcome.Success(response))

    postActionInteractor.postFavoriteWithAuth(data).test {
      val result = awaitItem()
      assertTrue(result is Outcome.Success)
      result as Outcome.Success
      assertEquals(response, result.data)
      awaitComplete()
    }
    coVerify { moviesRepository.postFavorite(any(), data, any()) }
  }


  @Test
  fun postWatchlist_whenSuccessful_emitsSuccess() = runTest {
    val data = WatchlistModel(
      mediaType = MOVIE_MEDIA_TYPE,
      mediaId = 23456,
      watchlist = true
    )

    coEvery { moviesRepository.postWatchlist(any(), data, any()) } returns
      flowOf(Outcome.Success(response))

    postActionInteractor.postWatchlistWithAuth(data).test {
      val result = awaitItem()
      assertTrue(result is Outcome.Success)
      result as Outcome.Success
      assertEquals(response, result.data)
      awaitComplete()
    }
    coVerify { moviesRepository.postWatchlist(any(), data, any()) }
  }
}
