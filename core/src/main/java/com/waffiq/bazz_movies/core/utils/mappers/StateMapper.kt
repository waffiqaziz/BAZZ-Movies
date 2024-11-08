package com.waffiq.bazz_movies.core.utils.mappers

import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.StatedResponse
import com.waffiq.bazz_movies.core.domain.model.Stated

object StateMapper {

  fun StatedResponse.toStated() = Stated(
    id = id,
    favorite = favorite,
    rated = rated,
    watchlist = watchlist
  )
}