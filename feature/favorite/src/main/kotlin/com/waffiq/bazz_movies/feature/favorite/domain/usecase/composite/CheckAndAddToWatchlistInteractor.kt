package com.waffiq.bazz_movies.feature.favorite.domain.usecase.composite

import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.data.domain.model.post.PostFavoriteWatchlist
import com.waffiq.bazz_movies.core.data.domain.usecase.composite.MediaStateUseCase
import com.waffiq.bazz_movies.core.data.domain.usecase.composite.PostActionUseCase
import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.WatchlistParams
import com.waffiq.bazz_movies.feature.favorite.domain.model.WatchlistActionResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class CheckAndAddToWatchlistInteractor @Inject constructor(
  private val mediaStateUseCase: MediaStateUseCase,
  private val postActionUseCase: PostActionUseCase,
) : CheckAndAddToWatchlistUseCase {

  override fun addMovieToWatchlist(movieId: Int): Flow<Outcome<WatchlistActionResult>> =
    addToWatchlist(
      mediaId = movieId,
      mediaType = MOVIE_MEDIA_TYPE,
      getStateFlow = { mediaStateUseCase.getMovieStateWithUser(movieId) },
    )

  override fun addTvToWatchlist(tvId: Int): Flow<Outcome<WatchlistActionResult>> =
    addToWatchlist(
      mediaId = tvId,
      mediaType = TV_MEDIA_TYPE,
      getStateFlow = { mediaStateUseCase.getTvStateWithUser(tvId) },
    )

  private fun addToWatchlist(
    mediaId: Int,
    mediaType: String,
    getStateFlow: () -> Flow<Outcome<MediaState>>,
  ): Flow<Outcome<WatchlistActionResult>> =
    getStateFlow()
      .flatMapConcat { stateOutcome ->
        when (stateOutcome) {
          is Outcome.Success -> {
            if (stateOutcome.data.watchlist) {
              flowOf(Outcome.Success(WatchlistActionResult.AlreadyInWatchlist))
            } else {
              postActionUseCase.postWatchlistWithAuth(
                WatchlistParams(mediaType, mediaId, true),
              ).map(::mapPostOutcome)
            }
          }

          is Outcome.Error -> flowOf(Outcome.Error(stateOutcome.message))

          is Outcome.Loading -> flowOf(Outcome.Loading)
        }
      }
      .onStart { emit(Outcome.Loading) }

  private fun mapPostOutcome(
    postOutcome: Outcome<PostFavoriteWatchlist>,
  ): Outcome<WatchlistActionResult> =
    when (postOutcome) {
      is Outcome.Success -> Outcome.Success(WatchlistActionResult.Added)
      is Outcome.Error -> Outcome.Error(postOutcome.message)
      is Outcome.Loading -> Outcome.Loading
    }
}
