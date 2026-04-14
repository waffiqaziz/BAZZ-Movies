package com.waffiq.bazz_movies.feature.favorite.testutils

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.domain.UserModel
import com.waffiq.bazz_movies.core.test.PagingFlowHelperTest.testPagingFlowAwaitComplete
import com.waffiq.bazz_movies.core.user.domain.repository.IUserRepository
import com.waffiq.bazz_movies.feature.favorite.domain.repository.IFavoriteRepository
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain

abstract class BaseFavoriteInteractorTest : ShouldSpec() {

  protected var mockFavoriteRepository: IFavoriteRepository = mockk()
  protected var mockUserRepository: IUserRepository = mockk()

  init {
    beforeTest {
      Dispatchers.setMain(UnconfinedTestDispatcher())
      every { mockUserRepository.getUserPref() } returns flowOf(
        UserModel(
          userId = 1234,
          name = "name",
          username = "username",
          password = "",
          region = "id",
          token = "token",
          isLogin = true,
          gravatarHash = "url",
          tmdbAvatar = "url"
        )
      )
    }
  }

  suspend fun testPagingFlow(
    flow: Flow<PagingData<MediaItem>>,
    expectedAssertions: (List<MediaItem>) -> Unit,
  ) {
    testPagingFlowAwaitComplete(flow) {
      expectedAssertions(it)
    }
  }
}
