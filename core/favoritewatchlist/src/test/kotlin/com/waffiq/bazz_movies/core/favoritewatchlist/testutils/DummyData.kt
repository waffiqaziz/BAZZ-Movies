package com.waffiq.bazz_movies.core.favoritewatchlist.testutils

import com.waffiq.bazz_movies.core.domain.Favorite

object DummyData {

  val indonesianMovie = "Indonesian Movie"
  val indonesianMovie2 = "Indonesian Movie 2"
  val favorite = Favorite(
    id = 1,
    mediaId = 1,
    mediaType = "movie",
    genre = "Action",
    backDrop = "backdrop",
    poster = "poster",
    overview = "overview",
    title = indonesianMovie,
    releaseDate = "1979-04-04",
    popularity = 214.0,
    rating = 9.0f,
    isFavorite = true,
    isWatchlist = false
  )
}
