package com.waffiq.bazz_movies.core.favoritewatchlist.testutils

import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.Constants.MOVIE_ORIGINAL_NAME
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.Constants.MOVIE_ORIGINAL_TITLE
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.Constants.MOVIE_TITLE
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.Constants.TEST_DATE
import com.waffiq.bazz_movies.core.models.Favorite
import com.waffiq.bazz_movies.core.models.MediaItem

object DummyData {

  const val INA_MOVIE_TITLE = "Indonesian Movie"
  const val INA_MOVIE_TITLE2 = "Indonesian Movie 2"
  val favorite = Favorite(
    id = 1,
    mediaId = 1,
    mediaType = "movie",
    genre = "Action",
    backDrop = "backdrop",
    poster = "poster",
    overview = "overview",
    title = INA_MOVIE_TITLE,
    releaseDate = "1979-04-04",
    popularity = 214.0,
    rating = 9.0f,
    isFavorite = true,
    isWatchlist = false,
    lastUpdated = 333,
  )

  val movieData = MediaItem(
    mediaType = "movie",
    name = "Test Movie Name",
    title = MOVIE_TITLE,
    originalTitle = MOVIE_ORIGINAL_TITLE,
    originalName = MOVIE_ORIGINAL_NAME,
    firstAirDate = TEST_DATE,
    releaseDate = TEST_DATE,
    listGenreIds = listOf(12),
    voteAverage = 10f,
    posterPath = "posterPath.jpg",
  )
}
