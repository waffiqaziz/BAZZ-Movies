package com.waffiq.bazz_movies.feature.detail.testutils

import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.models.MediaCastItem
import com.waffiq.bazz_movies.core.models.MediaItem
import com.waffiq.bazz_movies.core.models.MediaState
import com.waffiq.bazz_movies.core.models.Rated
import com.waffiq.bazz_movies.core.models.UserModel
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCredits
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCrewItem
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.UpdateMediaStateResult
import com.waffiq.bazz_movies.feature.detail.domain.model.keywords.MediaKeywordsItem
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.BelongsToCollection
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.DetailCollections
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.PartsItem
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.RatingsItem
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDateRegion
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvExternalIds
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.Provider
import com.waffiq.bazz_movies.feature.detail.ui.state.MediaDetailUiState
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
    gravatarHash = "",
    tmdbAvatar = "",
  )

  val testMediaState = MediaState(
    id = 12345,
    favorite = false,
    rated = Rated.Unrated,
    watchlist = false,
  )

  val testMediaStateRated = MediaState(
    id = 12345,
    favorite = true,
    rated = Rated.Value(7.0),
    watchlist = true,
  )

  val testMediaCastItem = MediaCastItem(
    castId = 1111,
    character = "character",
    gender = 1,
    creditId = "22222",
    knownForDepartment = "actor 1",
    originalName = "original name",
    popularity = 12345.0,
    name = "name",
    profilePath = "profile_path.jpg",
    id = 123456,
    adult = false,
    order = 12,
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
    job = "director",
  )

  val testMediaCrewItems = listOf(
    testMediaCrewItem.copy(
      id = 1,
      name = "Director 1",
      job = "Director",
      department = "Directing",
    ),
    testMediaCrewItem.copy(
      id = 2,
      name = "Director 2",
      job = "Director",
      department = "Directing",
    ),
    testMediaCrewItem.copy(
      id = 3,
      name = "Story Author",
      job = "Story",
      department = "Writing",
    ),
    testMediaCrewItem.copy(
      id = 4,
      name = "Character Creator",
      job = "Characters",
      department = "Writing",
    ),
    testMediaCrewItem.copy(
      id = 5,
      name = "Executive Producer",
      job = "Executive Producer",
      department = "Production",
    ),
    testMediaCrewItem.copy(
      id = 6,
      name = "Main Writer",
      job = "Writer",
      department = "Writing",
    ),
    testMediaCrewItem.copy(
      id = 7,
      name = "Original Author",
      job = "Author",
      department = "Writing",
    ),
    testMediaCrewItem.copy(
      id = 8,
      name = "Screenplay Writer",
      job = "Screenplay",
      department = "Writing",
    ),
    testMediaCrewItem.copy(
      id = 9,
      name = "Novel Author",
      job = "Novel",
      department = "Writing",
    ),
  )

  val testMediaCredits = MediaCredits(
    cast = listOf(
      testMediaCastItem,
      testMediaCastItem.copy(id = 89, name = "Actor 2"),
    ),
    crew = testMediaCrewItems,
  )

  private val testRatingsItem = RatingsItem(
    value = "8.5/10",
    source = "Internet Movie Database",
  )

  val testOMDbDetails = OMDbDetails(
    metascore = "90",
    imdbRating = "75",
    imdbVotes = "1234",
    ratings = listOf(testRatingsItem),
    rated = "67",
  )

  val testPostModelState = UpdateMediaStateResult(
    isSuccess = true,
    isDelete = false,
    isFavorite = true,
  )

  val testProvider = Provider(
    logoPath = "logo path",
    providerId = 123,
    providerName = "provider name",
    displayPriority = 1,
  )

  val testWatchProvidersUiState = WatchProvidersUiState.Success(
    ads = listOf(testProvider),
    buy = listOf(testProvider),
    flatrate = listOf(testProvider),
    free = listOf(testProvider),
    rent = listOf(testProvider),
  )

  val belongsToCollection = BelongsToCollection(
    id = 413,
    name = "Avatar Collection",
    backdropPath = "collection backdrop path",
    posterPath = "colleciton poster path ",
  )

  val testMediaDetail = MediaDetail(
    id = 12345678,
    credits = testMediaCredits,
    genre = "Action",
    genreId = listOf(28),
    duration = "1h 1m",
    imdbId = "tt12345678",
    ageRating = "G",
    tmdbScore = "100",
    status = "released",
    trailer = "video trailer",
    watchProviders = testWatchProvidersUiState,
    releaseDateRegion = ReleaseDateRegion(
      regionRelease = "ID",
      releaseDate = "Jul 23, 2025",
    ),
    keywords = listOf(
      MediaKeywordsItem("animation", 123),
      MediaKeywordsItem("music", 4344),
      MediaKeywordsItem("action", null),
      MediaKeywordsItem(null, 334),
      MediaKeywordsItem("", 335),
    ),
    belongsToCollection = belongsToCollection,
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
    originCountry = null,
  )

  val mediaDetailUiState = MediaDetailUiState(
    detail = testMediaDetail,
    omdbDetails = OMDbDetails(),
    itemState = MediaState(
      id = 90,
      favorite = false,
      rated = Rated.Value(90.0),
      watchlist = false,
    ),
    isFavorite = false,
    isWatchlist = false,
    mediaStateResult = null, // it should only initiate when add to watchlist/favorite
    isLoading = false,
  )

  val partsItem = PartsItem(
    id = 23,
    genreIds = listOf(16, 35, 80, 99),
    title = "movie 1",
    overview =
    """
        Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt 
        ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation 
        ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in 
        reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur 
        sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est 
        laborum.",
    """.trimIndent(),
  )

  val detailCollections = DetailCollections(
    id = 22,
    name = "Lorem Ipsum Collections",
    originalName = "Lorem Ipsum Original",
    overview = "Overview Lorem Ipsum",
    parts = listOf(
      partsItem,
      partsItem.copy(id = 24, title = "movie 2"),
      partsItem.copy(id = 25, title = "movie 3"),
      partsItem.copy(id = 26, title = "movie 4"),
      partsItem.copy(id = 27, title = "movie 5"),
      partsItem.copy(id = 28, title = "movie 6"),
      partsItem.copy(id = 29, title = "movie 7"),
      partsItem.copy(id = 30, title = "movie 8"),
      partsItem.copy(id = 31, title = "movie 9"),
      partsItem.copy(id = 32, title = "movie 10"),
    ),
  )
}
