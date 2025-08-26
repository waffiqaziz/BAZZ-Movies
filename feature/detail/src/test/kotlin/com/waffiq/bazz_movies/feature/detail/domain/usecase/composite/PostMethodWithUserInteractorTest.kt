package com.waffiq.bazz_movies.feature.detail.domain.usecase.composite

import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.domain.FavoriteModel
import com.waffiq.bazz_movies.core.domain.Post
import com.waffiq.bazz_movies.core.domain.WatchlistModel
import com.waffiq.bazz_movies.core.movie.domain.model.post.PostFavoriteWatchlist
import com.waffiq.bazz_movies.feature.detail.testutils.BaseInteractorTest
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.MOVIE_ID
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.SESSION_ID
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.TV_ID
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.USER_ID
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.userModel
import io.mockk.every
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.test.assertIs

class PostMethodWithUserInteractorTest : BaseInteractorTest() {

  private val rate = 9.0f
  private lateinit var interactor: PostMethodWithUserInteractor

  val post = Post(
    success = true,
    statusCode = 201,
    statusMessage = "Success"
  )
  val postFavoriteWatchlist = PostFavoriteWatchlist(
    statusCode = 201,
    statusMessage = "Success"
  )
  val favoritePostModel = FavoriteModel(
    mediaType = MOVIE_MEDIA_TYPE,
    mediaId = MOVIE_ID,
    favorite = false
  )
  val watchlistPostModel = WatchlistModel(
    mediaType = MOVIE_MEDIA_TYPE,
    mediaId = MOVIE_ID,
    watchlist = false
  )

  override fun initInteractor() {
    interactor = PostMethodWithUserInteractor(
      mockPostMethodUseCase,
      mockUserPrefUseCase,
    )
  }

  @Before
  override fun baseSetUp() {
    super.baseSetUp()
    every { mockUserPrefUseCase.getUser() } returns flowOf(userModel)
  }

  @Test
  fun postFavorite_whenSuccessful_emitsSuccess() = runTest {
    testSuccessScenario(
      mockCall = { mockPostMethodUseCase.postFavorite(SESSION_ID, favoritePostModel, USER_ID) },
      mockResponse = postFavoriteWatchlist,
      interactorCall = { interactor.postFavorite(favoritePostModel) }
    ) { emission ->
      val result = assertIs<PostFavoriteWatchlist>(emission.data)
      assertEquals(201, result.statusCode)
    }
  }

  @Test
  fun postFavorite_whenUnsuccessful_emitsError() = runTest {
    testErrorScenario(
      mockCall = { mockPostMethodUseCase.postFavorite(SESSION_ID, favoritePostModel, USER_ID) },
      interactorCall = { interactor.postFavorite(favoritePostModel) }
    )
  }

  @Test
  fun postFavorite_whenLoading_emitsLoading() = runTest {
    testLoadingScenario(
      mockCall = { mockPostMethodUseCase.postFavorite(SESSION_ID, favoritePostModel, USER_ID) },
      interactorCall = { interactor.postFavorite(favoritePostModel) }
    )
  }

  @Test
  fun postWatchlist_whenSuccessful_emitsSuccess() = runTest {
    testSuccessScenario(
      mockCall = { mockPostMethodUseCase.postWatchlist(SESSION_ID, watchlistPostModel, USER_ID) },
      mockResponse = postFavoriteWatchlist,
      interactorCall = { interactor.postWatchlist(watchlistPostModel) }
    ) { emission ->
      val result = assertIs<PostFavoriteWatchlist>(emission.data)
      assertEquals(201, result.statusCode)
    }
  }

  @Test
  fun postWatchlist_whenUnsuccessful_emitsError() = runTest {
    testErrorScenario(
      mockCall = { mockPostMethodUseCase.postWatchlist(SESSION_ID, watchlistPostModel, USER_ID) },
      interactorCall = { interactor.postWatchlist(watchlistPostModel) }
    )
  }

  @Test
  fun postWatchlist_whenLoading_emitsLoading() = runTest {
    testLoadingScenario(
      mockCall = { mockPostMethodUseCase.postWatchlist(SESSION_ID, watchlistPostModel, USER_ID) },
      interactorCall = { interactor.postWatchlist(watchlistPostModel) }
    )
  }

  @Test
  fun postMovieRate_whenSuccessful_emitsSuccess() = runTest {
    testSuccessScenario(
      mockCall = { mockPostMethodUseCase.postMovieRate(SESSION_ID, rate, MOVIE_ID) },
      mockResponse = post,
      interactorCall = { interactor.postMovieRate(rate, MOVIE_ID) }
    ) { emission ->
      val result = assertIs<Post>(emission.data)
      assertEquals(201, result.statusCode)
    }
  }

  @Test
  fun postMovieRate_whenUnsuccessful_emitsError() = runTest {
    testErrorScenario(
      mockCall = { mockPostMethodUseCase.postMovieRate(SESSION_ID, rate, MOVIE_ID) },
      interactorCall = { interactor.postMovieRate(rate, MOVIE_ID) }
    )
  }

  @Test
  fun postMovieRate_whenLoading_emitsLoading() = runTest {
    testLoadingScenario(
      mockCall = { mockPostMethodUseCase.postMovieRate(SESSION_ID, rate, MOVIE_ID) },
      interactorCall = { interactor.postMovieRate(rate, MOVIE_ID) }
    )
  }

  @Test
  fun postTvRate_whenSuccessful_emitsSuccess() = runTest {
    testSuccessScenario(
      mockCall = { mockPostMethodUseCase.postTvRate(SESSION_ID, rate, TV_ID) },
      mockResponse = post,
      interactorCall = { interactor.postTvRate(rate, TV_ID) }
    ) { emission ->
      val result = assertIs<Post>(emission.data)
      assertEquals(201, result.statusCode)
    }
  }

  @Test
  fun postTvRate_whenUnsuccessful_emitsError() = runTest {
    testErrorScenario(
      mockCall = { mockPostMethodUseCase.postTvRate(SESSION_ID, rate, TV_ID) },
      interactorCall = { interactor.postTvRate(rate, TV_ID) }
    )
  }

  @Test
  fun postTvRate_whenLoading_emitsLoading() = runTest {
    testLoadingScenario(
      mockCall = { mockPostMethodUseCase.postTvRate(SESSION_ID, rate, TV_ID) },
      interactorCall = { interactor.postTvRate(rate, TV_ID) }
    )
  }
}
