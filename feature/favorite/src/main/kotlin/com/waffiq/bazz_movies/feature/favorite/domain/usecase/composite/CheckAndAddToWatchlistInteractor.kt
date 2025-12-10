package com.waffiq.bazz_movies.feature.favorite.domain.usecase.composite

import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.WatchlistModel
import com.waffiq.bazz_movies.core.movie.domain.model.post.PostFavoriteWatchlist
import com.waffiq.bazz_movies.core.movie.domain.usecase.composite.PostActionUseCase
import com.waffiq.bazz_movies.core.movie.domain.usecase.mediastate.GetMovieStateUseCase
import com.waffiq.bazz_movies.core.movie.domain.usecase.mediastate.GetTvStateUseCase
import com.waffiq.bazz_movies.core.user.domain.usecase.userpreference.UserPrefUseCase
import com.waffiq.bazz_movies.feature.favorite.domain.model.WatchlistActionResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CheckAndAddToWatchlistInteractor @Inject constructor(
  private val getMovieStateUseCase: GetMovieStateUseCase,
  private val getTvStateUseCase: GetTvStateUseCase,
  private val postActionUseCase: PostActionUseCase,
  private val userPrefUseCase: UserPrefUseCase,
) : CheckAndAddToWatchlistUseCase {

  override fun addMovieToWatchlist(movieId: Int): Flow<Outcome<WatchlistActionResult>> {
    return addToWatchlist(
      mediaId = movieId,
      mediaType = MOVIE_MEDIA_TYPE,
      getStateFlow = { token -> getMovieStateUseCase.getMovieState(token, movieId) }
    )
  }

  override fun addTvToWatchlist(tvId: Int): Flow<Outcome<WatchlistActionResult>> {
    return addToWatchlist(
      mediaId = tvId,
      mediaType = TV_MEDIA_TYPE,
      getStateFlow = { token -> getTvStateUseCase.getTvState(token, tvId) }
    )
  }

  private fun addToWatchlist(
    mediaId: Int,
    mediaType: String,
    getStateFlow: (String) -> Flow<Outcome<MediaState>>
  ): Flow<Outcome<WatchlistActionResult>> = flow {
    emit(Outcome.Loading)
    val token = userPrefUseCase.getUserToken().first()

    getStateFlow(token).collect { outcome ->
      when (outcome) {
        is Outcome.Success -> {
          if (outcome.data.watchlist) {
            emit(Outcome.Success(WatchlistActionResult.AlreadyInWatchlist))
          } else {
            postActionUseCase.postWatchlistWithAuth(
              WatchlistModel(mediaType, mediaId, true)
            ).collect { postOutcome ->
              emit(mapPostOutcome(postOutcome))
            }
          }
        }
        is Outcome.Error -> emit(Outcome.Error(outcome.message))
        is Outcome.Loading -> emit(Outcome.Loading)
      }
    }
  }

  private fun mapPostOutcome(
    postOutcome: Outcome<PostFavoriteWatchlist>
  ): Outcome<WatchlistActionResult> {
    return when (postOutcome) {
      is Outcome.Success -> Outcome.Success(WatchlistActionResult.Added)
      is Outcome.Error -> Outcome.Error(postOutcome.message)
      is Outcome.Loading -> Outcome.Loading
    }
  }
}
