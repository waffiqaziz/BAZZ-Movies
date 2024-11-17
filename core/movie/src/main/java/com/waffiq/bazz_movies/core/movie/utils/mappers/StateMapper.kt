package com.waffiq.bazz_movies.core.movie.utils.mappers

import com.waffiq.bazz_movies.core.model.Stated
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.StatedResponse

object StateMapper {

  fun StatedResponse.toStated() = Stated(
    id = id,
    favorite = favorite,
    rated = rated,
    watchlist = watchlist
  )
}
