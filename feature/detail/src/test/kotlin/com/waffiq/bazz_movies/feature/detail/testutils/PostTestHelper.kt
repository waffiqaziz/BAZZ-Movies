package com.waffiq.bazz_movies.feature.detail.testutils

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.PostResult
import com.waffiq.bazz_movies.core.movie.domain.model.post.PostFavoriteWatchlist
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.ERROR_MESSAGE
import io.mockk.mockk
import kotlinx.coroutines.flow.flow

interface PostTestHelper {

  val mockPost get() = mockk<PostResult>()
  val mockPostFavoriteWatchlist get() = mockk<PostFavoriteWatchlist>()

  fun <T : Any> flowSuccessWithLoading(data: T) = flow {
    emit(Outcome.Loading)
    emit(Outcome.Success(data))
  }

  val flowFailedWithLoading
    get() = flow {
      emit(Outcome.Loading)
      emit(Outcome.Error(ERROR_MESSAGE))
    }
}
