package com.waffiq.bazz_movies.core.mappers

import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.core.domain.Rated
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.state.MediaStateResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.state.RatedResponse

object MediaStateMapper {

  fun MediaStateResponse.toMediaState() =
    MediaState(
      id = id,
      favorite = favorite,
      rated = ratedResponse.toRated(),
      watchlist = watchlist,
    )

  fun RatedResponse.toRated(): Rated =
    when (this) {
      is RatedResponse.Value -> Rated.Value(this.value)
      is RatedResponse.Unrated -> Rated.Unrated
    }
}
