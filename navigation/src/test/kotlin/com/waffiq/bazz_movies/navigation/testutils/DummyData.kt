package com.waffiq.bazz_movies.navigation.testutils

import com.waffiq.bazz_movies.core.common.MediaType
import com.waffiq.bazz_movies.navigation.MediaArgs

object DummyData {

  val mediaArgs = MediaArgs(
    id = 42,
    mediaType = MediaType.MOVIE,
    name = "Test Movie",
    title = "Test Title",
    originalTitle = "Original Title",
    originalName = "Original Name",
    overview = "Overview of the movie.",
    originalLanguage = "en",
    listGenreIds = listOf(28, 12),
    posterPath = "/poster1.jpg",
    backdropPath = "/backdrop1.jpg",
    firstAirDate = "2025-01-01",
    releaseDate = "2026-01-01",
    video = true,
  )

  val mediaArgsNull = MediaArgs(mediaType = MediaType.MOVIE)
}
