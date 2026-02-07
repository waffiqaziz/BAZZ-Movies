package com.waffiq.bazz_movies.feature.watchlist.domain.usecase.composite

import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.domain.FavoriteParams
import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.movie.domain.model.post.PostFavoriteWatchlist
import com.waffiq.bazz_movies.core.movie.domain.usecase.composite.PostActionUseCase
import com.waffiq.bazz_movies.core.movie.domain.usecase.mediastate.GetMovieStateUseCase
import com.waffiq.bazz_movies.core.movie.domain.usecase.mediastate.GetTvStateUseCase
import com.waffiq.bazz_movies.core.user.domain.usecase.userpreference.UserPrefUseCase
import com.waffiq.bazz_movies.feature.watchlist.domain.model.FavoriteActionResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.take
import javax.inject.Inject

class CheckAndAddToFavoriteInteractor @Inject constructor(
  private val getMovieStateUseCase: GetMovieStateUseCase,
  private val getTvStateUseCase: GetTvStateUseCase,
  private val postActionUseCase: PostActionUseCase,
  private val userPrefUseCase: UserPrefUseCase,
) : CheckAndAddToFavoriteUseCase {

  override fun addMovieToFavorite(movieId: Int): Flow<Outcome<FavoriteActionResult>> {
    return addToFavorite(
      mediaId = movieId,
      mediaType = MOVIE_MEDIA_TYPE,
      getStateFlow = { token -> getMovieStateUseCase.getMovieState(token, movieId) }
    )
  }

  override fun addTvToFavorite(tvId: Int): Flow<Outcome<FavoriteActionResult>> {
    return addToFavorite(
      mediaId = tvId,
      mediaType = TV_MEDIA_TYPE,
      getStateFlow = { token -> getTvStateUseCase.getTvState(token, tvId) }
    )
  }

  private fun addToFavorite(
    mediaId: Int,
    mediaType: String,
    getStateFlow: (String) -> Flow<Outcome<MediaState>>,
  ): Flow<Outcome<FavoriteActionResult>> {
    return userPrefUseCase.getUserToken()
      .filterNotNull()
      .take(1)
      .flatMapConcat { token -> getStateFlow(token) }
      .flatMapConcat { stateOutcome ->
        when (stateOutcome) {
          is Outcome.Success -> {
            if (stateOutcome.data.favorite) {
              flowOf(Outcome.Success(FavoriteActionResult.AlreadyInFavorite))
            } else {
              postActionUseCase.postFavoriteWithAuth(
                FavoriteParams(mediaType, mediaId, true)
              ).map(::mapPostOutcome)
            }
          }

          is Outcome.Error -> flowOf(Outcome.Error(stateOutcome.message))
          is Outcome.Loading -> flowOf(Outcome.Loading)
        }
      }
      .onStart { emit(Outcome.Loading) }
  }

  private fun mapPostOutcome(
    postOutcome: Outcome<PostFavoriteWatchlist>,
  ): Outcome<FavoriteActionResult> {
    return when (postOutcome) {
      is Outcome.Success -> Outcome.Success(FavoriteActionResult.Added)
      is Outcome.Error -> Outcome.Error(postOutcome.message)
      is Outcome.Loading -> Outcome.Loading
    }
  }
}
