package com.waffiq.bazz_movies.feature.person.testutils

import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.CastItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.CombinedCreditResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.CrewItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.ExternalIDPersonResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.ProfilesItemResponse

object DummyData {

  val listOfCastItemResponse = listOf(
    CastItemResponse(id = 1, name = "John", voteCount = 12345),
    CastItemResponse(id = 2, name = "Rex", voteCount = 2345),
  )

  val listOfCrewItemResponse = listOf(
    CrewItemResponse(id = 1, job = "cameraman", title = "what"),
    CrewItemResponse(id = 2, job = "director", title = "why"),
  )

  val combinedCreditResponse = CombinedCreditResponse(
    cast = listOfCastItemResponse,
    crew = listOfCrewItemResponse,
  )

  val castItemResponse = CastItemResponse(
    firstAirDate = "firstAirDate",
    overview = "overview",
    originalLanguage = "originalLanguage",
    episodeCount = 12,
    genreIds = emptyList(),
    posterPath = "posterPath",
    originCountry = emptyList(),
    backdropPath = "backdropPath",
    character = "character",
    creditId = "creditId",
    mediaType = "tv",
    originalName = "originalName",
    popularity = 1234.0,
    voteAverage = 4123f,
    name = "name",
    id = null,
    adult = false,
    voteCount = null,
    originalTitle = "originalTitle",
    video = false,
    title = "title",
    releaseDate = "releaseDate",
    order = 3,
  )

  val listOfProfilesItemResponse = listOf(
    ProfilesItemResponse(
      width = 300,
      height = 450,
      filePath = "/file_path.jpg",
      voteCount = 98765,
    ),
    ProfilesItemResponse(
      width = 300,
      height = 450,
      filePath = "/file_path2.jpg",
      voteCount = 9999,
    ),
  )

  val externalIDPersonResponse = ExternalIDPersonResponse(
    imdbId = "nm12345",
    instagramId = "instagram_id",
    twitterId = "twitter_id",
  )
}
