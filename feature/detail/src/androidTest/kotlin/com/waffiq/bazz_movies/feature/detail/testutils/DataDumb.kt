package com.waffiq.bazz_movies.feature.detail.testutils

import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.domain.MediaCastItem
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.core.domain.Rated
import com.waffiq.bazz_movies.core.domain.UserModel
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCredits
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCrewItem
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.PostModelState
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.RatingsItem
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDateRegion
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvExternalIds
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.Provider
import com.waffiq.bazz_movies.feature.detail.ui.state.WatchProvidersUiState

object DataDumb {

  val testUserModel = UserModel(
    username = "testUser",
    userId = 12345,
    name = "user name",
    password = "",
    region = "id",
    token = "NAN",
    isLogin = true,
    gravatarHast = "",
    tmdbAvatar = ""
  )

  val testMediaState = MediaState(
    id = 12345,
    favorite = false,
    rated = Rated.Unrated,
    watchlist = false
  )

  val testMediaStateRated = MediaState(
    id = 12345,
    favorite = true,
    rated = Rated.Value(7.0),
    watchlist = true
  )

  val testMediaCastItem = MediaCastItem(
    castId = 1111,
    character = "character",
    gender = 1,
    creditId = "22222",
    knownForDepartment = "actor",
    originalName = "original name",
    popularity = 12345.0,
    name = "name",
    profilePath = "profile_path.jpg",
    id = 123456,
    adult = false,
    order = 12
  )

  val testMediaCrewItem = MediaCrewItem(
    gender = 2,
    creditId = "122333",
    knownForDepartment = "director",
    originalName = "original name crew",
    popularity = 3333.0,
    name = "name crew",
    profilePath = "profile path crew",
    id = 3333,
    adult = false,
    department = "director",
    job = "director"
  )

  val testMediaCredits = MediaCredits(
    cast = listOf(testMediaCastItem),
    id = 1234,
    crew = listOf(testMediaCrewItem)
  )

  private val testRatingsItem = RatingsItem(
    value = "8.5/10",
    source = "Internet Movie Database"
  )

  val testOMDbDetails = OMDbDetails(
    metascore = "90",
    imdbRating = "75",
    imdbVotes = "1234",
    ratings = listOf(testRatingsItem),
    rated = "67",
  )

  val testPostModelState = PostModelState(
    isSuccess = true,
    isDelete = false,
    isFavorite = true
  )

  val testMediaDetail = MediaDetail(
    id = 12345678,
    genre = "Action",
    genreId = listOf(28),
    duration = "1h 1m",
    imdbId = "tt12345678",
    ageRating = "G",
    tmdbScore = "100",
    status = "released",
    releaseDateRegion = ReleaseDateRegion(
      regionRelease = "ID",
      releaseDate = "Jul 23, 2025",
    )
  )

  val testTvExternalIds = TvExternalIds(
    imdbId = "tt12345678",
    id = 12345678,
  )

  val testMediaItem = MediaItem(
    firstAirDate = "2025-07-09",
    overview = "Overview",
    originalLanguage = "en",
    listGenreIds = listOf(28),
    posterPath = "poster.jpg",
    backdropPath = "backdrop.jpg",
    mediaType = MOVIE_MEDIA_TYPE,
    popularity = 354.5509,
    voteAverage = 7.453f,
    name = "name",
    title = "title",
    originalTitle = "original title",
    originalName = "original name",
    id = 1061474,
    adult = false,
    voteCount = 1102,
    video = false,
    releaseDate = "2025-07-09",
    originCountry = null
  )

  val testProvider = Provider(
    logoPath = "logo path",
    providerId = 123,
    providerName = "provider name",
    displayPriority = 1
  )

  val testWatchProvidersUiState = WatchProvidersUiState.Success(
    ads = listOf(testProvider),
    buy = listOf(testProvider),
    flatrate = listOf(testProvider),
    free = listOf(testProvider),
    rent = listOf(testProvider),
  )
}
