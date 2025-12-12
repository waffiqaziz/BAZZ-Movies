package com.waffiq.bazz_movies.feature.detail.testutils

import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.domain.GenresItem
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.core.domain.Rated
import com.waffiq.bazz_movies.core.domain.UserModel
import com.waffiq.bazz_movies.core.network.data.remote.responses.omdb.OMDbDetailsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.omdb.RatingsItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.GenresResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.movie.BelongsToCollectionResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.movie.DetailMovieResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.releasedates.ReleaseDatesResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.releasedates.ReleaseDatesResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.releasedates.ReleaseDatesResponseItemValue
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.ContentRatingsItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.ContentRatingsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.CreatedByItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.DetailTvResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.LastEpisodeToAirResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.NetworksItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.SeasonsItemResponse
import com.waffiq.bazz_movies.core.utils.GenreHelper.transformListGenreToJoinString
import com.waffiq.bazz_movies.core.utils.GenreHelper.transformToGenreIDs
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCredits
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.PostModelState
import com.waffiq.bazz_movies.feature.detail.domain.model.ProductionCompaniesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.ProductionCountriesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.SpokenLanguagesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.Video
import com.waffiq.bazz_movies.feature.detail.domain.model.VideoItem
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.MovieDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.RatingsItem
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDates
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDatesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDatesItemValue
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.ContentRatings
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.ContentRatingsItem
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.CreatedByItem
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.LastEpisodeToAir
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.NetworksItem
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.SeasonsItem
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvExternalIds
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProviders
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProvidersItem
import com.waffiq.bazz_movies.feature.detail.utils.helpers.AgeRatingHelper.getAgeRating
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.getTransformDuration
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.getTransformTMDBScore
import com.waffiq.bazz_movies.feature.detail.utils.helpers.ReleaseDateHelper.getReleaseDateRegion

// Used as data dumb testing
object HelperTest {

  const val IMDB_ID = "tt1234567"
  const val USER_ID = 1234567890
  const val USER_REGION = "US"
  const val ERROR_MESSAGE = "Network error"
  const val SESSION_ID = "session123"

  val genresItemResponse = GenresResponseItem(
    id = 1,
    name = "Action"
  )

  val releaseDatesItemValueResponse = ReleaseDatesResponseItemValue(
    descriptors = listOf("R"),
    note = "Test note",
    type = 3,
    iso6391 = "en",
    certification = "R",
    releaseDate = "2024-01-01"
  )

  val releaseDatesItemResponse = ReleaseDatesResponseItem(
    iso31661 = "US",
    listReleaseDateResponseItemValue = listOf(releaseDatesItemValueResponse)
  )

  val releaseDatesResponse = ReleaseDatesResponse(
    listReleaseDatesResponseItem = listOf(releaseDatesItemResponse)
  )

  val belongsToCollectionResponse = BelongsToCollectionResponse(
    backdropPath = "/backdrop.jpg",
    name = "Test Collection",
    id = 1,
    posterPath = "/poster.jpg"
  )

  val detailMovieResponse = DetailMovieResponse(
    originalLanguage = "en",
    imdbId = "tt1234567",
    video = false,
    title = "Test Movie",
    backdropPath = "/backdrop.jpg",
    revenue = 1000000,
    listGenresItemResponse = listOf(genresItemResponse),
    popularity = 8.5,
    releaseDatesResponse = releaseDatesResponse,
    listProductionCountriesItemResponse = listOf(),
    id = 1,
    voteCount = 100,
    budget = 500000,
    overview = "Test overview",
    originalTitle = "Test Movie Original",
    runtime = 120,
    posterPath = "/poster.jpg",
    listSpokenLanguagesItemResponse = listOf(),
    listProductionCompaniesItemResponse = listOf(),
    releaseDate = "2024-01-01",
    voteAverage = 7.5,
    belongsToCollectionResponse = belongsToCollectionResponse,
    tagline = "Test tagline",
    adult = false,
    homepage = "https://testmovie.com",
    status = "Released"
  )

  val ratingsItem = listOf(RatingsItem())

  val oMDbDetails = OMDbDetails(
    metascore = "85",
    boxOffice = "$100M",
    website = "example.com",
    imdbRating = "8.5",
    imdbVotes = "100,000",
    ratings = ratingsItem,
    runtime = "120 min",
    language = "English",
    rated = "PG-13",
    production = "Studio",
    released = "2024-01-01",
    imdbID = "tt1234567",
    plot = "Plot summary",
    director = "Director Name",
    title = "Movie Title",
    actors = "Actor Names",
    response = "True",
    type = "movie",
    awards = "Award Info",
    dVD = "2024-06-01",
    year = "2024",
    poster = "poster.jpg",
    country = "USA",
    genre = "Action",
    writer = "Writer Name"
  )

  val networksItemResponse = NetworksItemResponse(
    logoPath = "/network_logo.jpg",
    name = "HBO",
    id = 1,
    originCountry = "US"
  )

  val seasonsItemResponse = SeasonsItemResponse(
    airDate = "2024-01-01",
    overview = "Season overview",
    episodeCount = 10,
    name = "Season 1",
    seasonNumber = 1,
    id = 1,
    posterPath = "/season_poster.jpg"
  )

  val createdByItemResponse = CreatedByItemResponse(
    gender = 1,
    creditId = "credit123",
    name = "Creator Name",
    profilePath = "/creator_profile.jpg",
    id = 1
  )

  val lastEpisodeToAirResponse = LastEpisodeToAirResponse(
    productionCode = "PROD001",
    airDate = "2024-01-15",
    overview = "Episode overview",
    episodeNumber = 1,
    episodeType = "standard",
    showId = 1,
    voteAverage = 8.5,
    name = "Episode 1",
    seasonNumber = 1,
    runtime = 60,
    id = 1,
    stillPath = "/episode_still.jpg",
    voteCount = 100
  )

  val contentRatingsItemResponse = ContentRatingsItemResponse(
    descriptors = listOf("Violence"),
    iso31661 = "US",
    rating = "TV-MA"
  )

  val contentRatingsResponse = ContentRatingsResponse(
    contentRatingsItemResponse = listOf(contentRatingsItemResponse)
  )

  val detailTvResponse = DetailTvResponse(
    originalLanguage = "en",
    numberOfEpisodes = 10,
    networksResponse = listOf(networksItemResponse),
    type = "Scripted",
    backdropPath = "/backdrop.jpg",
    genres = listOf(genresItemResponse),
    popularity = 8.5,
    productionCountriesResponse = listOf(),
    id = 1,
    numberOfSeasons = 1,
    voteCount = 100,
    firstAirDate = "2024-01-01",
    overview = "Test TV show overview",
    seasonsResponse = listOf(seasonsItemResponse),
    languages = listOf("en"),
    createdByResponse = listOf(createdByItemResponse),
    lastEpisodeToAirResponse = lastEpisodeToAirResponse,
    posterPath = "/poster.jpg",
    originCountry = listOf("US"),
    spokenLanguagesResponse = listOf(),
    productionCompaniesResponse = listOf(),
    originalName = "Test TV Show Original",
    voteAverage = 8.5,
    name = "Test TV Show",
    tagline = "Test tagline",
    episodeRunTime = listOf(60),
    contentRatingsResponse = contentRatingsResponse,
    adult = false,
    nextEpisodeToAir = null,
    inProduction = true,
    lastAirDate = "2024-01-15",
    homepage = "https://testtv.com",
    status = "Returning Series"
  )

  val tvDetailFull = TvDetail(
    originalLanguage = "en",
    numberOfEpisodes = 62,
    listNetworksItem = listOf(NetworksItem(name = "AMC")),
    type = "Scripted",
    backdropPath = "/backdrop.jpg",
    listGenres = listOf(GenresItem()),
    popularity = 100.5,
    listProductionCountriesItem = listOf(ProductionCountriesItem()),
    id = 1396,
    numberOfSeasons = 5,
    voteCount = 5000,
    firstAirDate = "2008-01-20",
    overview = "Show overview",
    listSeasonsItem = listOf(SeasonsItem()),
    listLanguages = listOf("en"),
    listCreatedByItem = listOf(CreatedByItem()),
    lastEpisodeToAir = LastEpisodeToAir(),
    posterPath = "/poster.jpg",
    listOriginCountry = listOf("US"),
    listSpokenLanguagesItem = listOf(SpokenLanguagesItem()),
    listProductionCompaniesItem = listOf(ProductionCompaniesItem()),
    originalName = "Breaking Bad",
    voteAverage = 9.3,
    name = "Breaking Bad",
    tagline = "All Bad Things Must Come to an End",
    listEpisodeRunTime = listOf(47),
    contentRatings = ContentRatings(),
    adult = false,
    nextEpisodeToAir = "Episode data",
    inProduction = false,
    lastAirDate = "2013-09-29",
    homepage = "http://www.amc.com/shows/breaking-bad",
    status = "Ended"
  )

  private val ratingsItemResponse = RatingsItemResponse(
    value = "8.5/10",
    source = "Internet Movie Database"
  )

  val omdbDetailsResponse = OMDbDetailsResponse(
    metascore = "85",
    boxOffice = "$100,000,000",
    website = "https://movie.com",
    imdbRating = "8.5",
    imdbVotes = "500,000",
    ratings = listOf(ratingsItemResponse),
    runtime = "148 min",
    language = "English",
    rated = "PG-13",
    production = "Warner Bros",
    released = "15 Jul 2008",
    imdbID = "tt0468569",
    plot = "When the menace known as the Joker wreaks havoc...",
    director = "Christopher Nolan",
    title = "The Dark Knight",
    actors = "Christian Bale, Heath Ledger, Aaron Eckhart",
    response = "True",
    type = "movie",
    awards = "Won 2 Oscars. Another 146 wins & 142 nominations.",
    dVD = "09 Dec 2008",
    year = "2008",
    poster = "https://poster.jpg",
    country = "United States",
    genre = "Action, Crime, Drama",
    writer = "Jonathan Nolan, Christopher Nolan"
  )

  // region MOVIE
  const val MOVIE_ID = 1001
  val detailMovie = MovieDetail(
    id = MOVIE_ID,
    runtime = 120,
    imdbId = IMDB_ID,
    voteAverage = 7.5,
    listGenres = listOf(GenresItem(id = 1, name = "Action")),
    releaseDates = ReleaseDates(
      listReleaseDatesItem = listOf(
        ReleaseDatesItem(
          iso31661 = "US",
          listReleaseDatesItemValue = listOf(
            ReleaseDatesItemValue(
              releaseDate = "2023-11-20T00:00:00.000Z",
              certification = "PG-13"
            )
          )
        )
      )
    )
  )

  val movieMediaDetail = MediaDetail(
    id = MOVIE_ID,
    genre = transformListGenreToJoinString(detailMovie.listGenres),
    genreId = transformToGenreIDs(detailMovie.listGenres),
    duration = getTransformDuration(detailMovie.runtime),
    imdbId = detailMovie.imdbId,
    ageRating = getAgeRating(
      detailMovie,
      getReleaseDateRegion(detailMovie, USER_REGION).regionRelease
    ),
    tmdbScore = getTransformTMDBScore(detailMovie.voteAverage),
    releaseDateRegion = getReleaseDateRegion(detailMovie, USER_REGION)
  )

  val movieCredits = MediaCredits(
    cast = listOf(),
    crew = listOf(),
    id = MOVIE_ID
  )

  val movieMediaItem = MediaItem(
    mediaType = MOVIE_MEDIA_TYPE,
    title = "Transformers",
    id = 99999,
    adult = false,
    voteCount = 8000
  )
  // endregion MOVIE

  // region TV
  const val TV_ID = 2002
  val detailTv = TvDetail(
    id = TV_ID,
    status = "Returning Series",
    voteAverage = 8.2,
    listGenres = listOf(GenresItem(id = 5, name = "Drama")),
    contentRatings = ContentRatings(
      contentRatingsItem = listOf(
        ContentRatingsItem(iso31661 = "US", rating = "TV-MA")
      )
    ),
    firstAirDate = "2023-12-20T00:00:00.000Z"
  )
  val externalTvID = TvExternalIds(imdbId = "tt87654321")
  val tvCredits = MediaCredits(cast = listOf(), crew = listOf(), id = TV_ID)
  // endregion TV

  val video = Video(
    1234,
    results = listOf(VideoItem(name = "Trailer", type = "Trailer", key = "Link Trailer"))
  )

  val watchProviders = WatchProviders(
    results = mapOf(
      "US" to WatchProvidersItem(
        link = "https://some-provider.com",
        ads = null,
        buy = null,
        flatrate = null,
        free = null,
        rent = null
      )
    ),
    id = 1234
  )

  val omdbDetails = OMDbDetails(
    imdbID = IMDB_ID,
    title = "Some Movie",
    plot = "This is the plot.",
    genre = "Action, Adventure",
    director = "Jane Doe",
    writer = "John Smith",
    actors = "Actor A, Actor B",
    released = "2023-01-01",
    language = "English",
    country = "USA",
    awards = "3 wins",
    poster = "https://poster.url",
    metascore = "65",
    imdbRating = "7.8",
    imdbVotes = "120,000",
    boxOffice = "$100,000,000",
    website = "https://example.com"
  )

  val dataMediaItem = MediaItem(
    firstAirDate = "",
    overview = "Overview",
    originalLanguage = "",
    listGenreIds = listOf(),
    posterPath = "Poster",
    backdropPath = "Backdrop",
    mediaType = "movie",
    originalName = "original name",
    popularity = 12345.0,
    voteAverage = 9.0f,
    name = "name",
    id = 1234,
    adult = false,
    voteCount = 999,
    originalTitle = "original title",
    video = false,
    title = "title",
    releaseDate = "release date",
    originCountry = listOf()
  )

  val tvMediaDetail = MediaDetail(
    id = TV_ID,
    genre = transformListGenreToJoinString(tvDetailFull.listGenres),
    genreId = transformToGenreIDs(tvDetailFull.listGenres),
    duration = tvDetailFull.status,
    imdbId = "",
    ageRating = getAgeRating(
      tvDetailFull,
      getReleaseDateRegion(tvDetailFull).regionRelease
    ),
    tmdbScore = getTransformTMDBScore(tvDetailFull.voteAverage),
    releaseDateRegion = getReleaseDateRegion(tvDetailFull)
  )

  val postModelAddFavoriteStateSuccess =
    PostModelState(
      isSuccess = true,
      isDelete = false,
      isFavorite = true
    )

  val postModelDeleteFavoriteStateSuccess =
    PostModelState(
      isSuccess = true,
      isDelete = true,
      isFavorite = true
    )

  val postModelAddWatchlistStateSuccess =
    PostModelState(
      isSuccess = true,
      isDelete = false,
      isFavorite = false
    )

  val postModelDeleteWatchlistStateSuccess =
    PostModelState(
      isSuccess = true,
      isDelete = true,
      isFavorite = false
    )

  val movieMediaState = MediaState(
    id = MOVIE_ID,
    favorite = true,
    rated = Rated.Unrated,
    watchlist = false
  )

  val tvMediaState = movieMediaState.copy(id = TV_ID)

  val userModel = UserModel(
    userId = USER_ID,
    name = "Jane Doe",
    username = "janedoe",
    password = "",
    region = "US",
    token = SESSION_ID,
    isLogin = true,
    gravatarHast = null,
    tmdbAvatar = null
  )
}
