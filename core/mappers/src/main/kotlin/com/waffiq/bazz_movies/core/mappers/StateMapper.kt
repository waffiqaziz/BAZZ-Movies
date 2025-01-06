package com.waffiq.bazz_movies.core.mappers

import com.waffiq.bazz_movies.core.domain.Rated
import com.waffiq.bazz_movies.core.domain.Stated
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.state.RatedResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.state.StatedResponse

object StateMapper {

  fun StatedResponse.toStated() = Stated(
    id = id,
    favorite = favorite,
    rated = ratedResponse.toDomainRated(),
    watchlist = watchlist
  )

  fun RatedResponse.toDomainRated(): Rated {
    return when (this) {
      is RatedResponse.Value -> Rated.Value(this.value)
      is RatedResponse.Unrated -> Rated.Unrated
    }
  }
}
