package com.waffiq.bazz_movies.feature.detail.testutils

import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.models.Favorite
import com.waffiq.bazz_movies.core.models.GenresItem
import com.waffiq.bazz_movies.core.models.MediaItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.omdb.OMDbDetailsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.omdb.RatingsResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.GenresResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.castcrew.MediaCastResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.castcrew.MediaCreditsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.castcrew.MediaCrewResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.keywords.MediaKeywordsResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.keywords.MovieKeywordsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.keywords.TvKeywordsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.movie.BelongsToCollectionResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.movie.DetailMovieResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.releasedates.ReleaseDatesResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.releasedates.ReleaseDatesResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.releasedates.ReleaseDatesResponseItemValue
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.AggregateCreditsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.ContentRatingsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.ContentRatingsResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.CreatedByResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.DetailTvResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.ExternalIdResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.JobsResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.LastEpisodeToAirResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.NetworksResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.NextEpisodeToAirResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.RolesResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.SeasonsResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.TvCastResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.TvCrewResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.videomedia.VideoResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.videomedia.VideoResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.watchproviders.ProviderResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.watchproviders.WatchProvidersResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.watchproviders.WatchProvidersResponseItem
import com.waffiq.bazz_movies.core.utils.GenreHelper.transformListGenreToJoinString
import com.waffiq.bazz_movies.core.utils.GenreHelper.transformToGenreIDs
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCastItem
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCrewItem
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.ProductionCompaniesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.ProductionCountriesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.SpokenLanguagesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.UpdateMediaStateResult
import com.waffiq.bazz_movies.feature.detail.domain.model.keywords.MediaKeywords
import com.waffiq.bazz_movies.feature.detail.domain.model.keywords.MediaKeywordsItem
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.BelongsToCollection
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.DetailCollections
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.MovieDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.PartsItem
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.RatingsItem
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDateRegion
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDates
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDatesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDatesItemValue
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.AggregateCredits
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.CastItem
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.ContentRatings
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.ContentRatingsItem
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.CreatedByItem
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.CrewItem
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.JobsItem
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.LastEpisodeToAir
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.NetworksItem
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.RolesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.SeasonsItem
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.Provider
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProviders
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProvidersItem
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.rolesItem
import com.waffiq.bazz_movies.feature.detail.utils.helpers.AgeRatingHelper.getAgeRating
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.getTransformDuration
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.getTransformTMDBScore
import com.waffiq.bazz_movies.feature.detail.utils.helpers.ReleaseDateHelper.getReleaseDateRegion
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaDetailMapper.toMediaCredits
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaDetailMapper.toVideo
import com.waffiq.bazz_movies.feature.detail.utils.mappers.TvMapper.toCredits
import com.waffiq.bazz_movies.feature.detail.utils.mappers.TvMapper.toExternalTvID
import com.waffiq.bazz_movies.feature.detail.utils.mappers.TvMapper.toNextEpisodeToAir

@Suppress("LargeClass")
object DummyData {

  const val IMDB_ID = "tt1234567"
  const val USER_REGION = "US"
  const val ERROR_MESSAGE = "Network error"
  const val SESSION_ID = "session123"
  const val COLLECTION_ID = 100

  val belongsToCollection = BelongsToCollection(
    id = COLLECTION_ID,
    name = "name collection",
    backdropPath = "backdrop path collection",
    posterPath = "poster path collection",
  )

  val partsItem = PartsItem(
    id = 12344,
    title = "parts item title",
    originalTitle = "parts item original title",
    posterPath = "parts item poster path",
    backdropPath = "parts item backdrop path",
    overview = "parts item overview",
    originalLanguage = "parts item original language",
    video = false,
    genreIds = listOf(33, 44, 55),
    mediaType = "parts item media type",
    releaseDate = "parts item release date",
    popularity = 444.0f,
    voteAverage = 555.0f,
    adult = false,
    voteCount = 666,
  )

  val detailCollection = DetailCollections(
    id = COLLECTION_ID,
    name = "Test Collection",
    originalName = "Test original name",
    overview = "test overview",
    backdropPath = "backdrop path",
    posterPath = "poster path",
    originalLanguage = "en",
    parts = listOf(partsItem),
  )

  val mediaCastResponseItem = MediaCastResponseItem(
    castId = 1111,
    character = "character",
    gender = 1,
    creditId = "1234",
    knownForDepartment = "actor 2",
    originalName = "original name",
    popularity = 431.0,
    name = "full name",
    profilePath = "profile_path_cast.jpg",
    id = 123456,
    adult = false,
    order = 12,
  )

  val mediaCrewResponseItem = MediaCrewResponseItem(
    gender = 2,
    creditId = "122333",
    knownForDepartment = "director",
    originalName = "original name crew",
    popularity = 3333.0,
    name = "name crew",
    profilePath = "profile_path_crew.jpg",
    id = 3333,
    adult = false,
    department = "director",
    job = "director",
  )

  val rolesItem = RolesItem(
    character = "Joel Miller",
    episodeCount = 9,
    creditId = "5e4b8a8f0c3a36847",
  )

  val mediaCastItem = MediaCastItem(
    name = "Pedro Pascal",
    originalName = "José Pedro Balmaceda Pascal",
    profilePath = "/profile.jpg",
    id = 125336,
    castId = 123,
    character = "Joel Miller",
    roles = listOf(rolesItem),
    totalEpisodeCount = 9,
    gender = 2,
    creditId = "5e4b8a8f0c3a36847",
    knownForDepartment = "Acting",
    popularity = 87.42,
    adult = false,
    order = 0,
  )

  val mediaCrewItem = MediaCrewItem(
    name = "David Fincher",
    originalName = "David Fincher",
    profilePath = "/profile.jpg",
    id = 7467,
    creditId = "52fe425ec3a36847",
    totalEpisodeCount = 8,
    gender = 2,
    knownForDepartment = "Directing",
    jobs = emptyList(),
    popularity = 25.18,
    adult = false,
    department = "Directing",
    job = "Director",
  )

  val mediaCreditsResponse = MediaCreditsResponse(
    cast = listOf(mediaCastResponseItem, mediaCastResponseItem.copy(id = 89, name = "actor 3")),
    crew = listOf(mediaCrewResponseItem),
  )

  val rolesResponseItem =
    RolesResponseItem(character = "Character 1", episodeCount = 12, creditId = "creditId")

  val castResponseItem =
    TvCastResponseItem(id = 334, name = "Cast 1", roles = listOf(rolesResponseItem))

  val jobsResponseItem =
    JobsResponseItem(job = "Writter", episodeCount = 12, creditId = "creditId")

  val crewResponseItem =
    TvCrewResponseItem(id = 224, name = "name crew", jobs = listOf(jobsResponseItem))

  val aggregateCreditsResponse = AggregateCreditsResponse(
    cast = listOf(castResponseItem),
    crew = listOf(crewResponseItem),
  )

  val castItem = CastItem(
    id = 334,
    name = "Cast 1",
    originalName = "Cast One",
    roles = listOf(rolesItem),
    knownForDepartment = "acting",
    gender = 1,
    profilePath = "path1.jpg",
    adult = false,
    order = 1,
  )

  val jobsItem =
    JobsItem(job = "Writter", episodeCount = 12, creditId = "creditId")

  val crewItem = CrewItem(
    id = 224,
    name = "name crew",
    jobs = listOf(jobsItem),
    totalEpisodeCount = 12,
    gender = 0,
    knownForDepartment = "writter",
    originalName = "name crew original",
    popularity = 3232,
    profilePath = "path.jpg",
    adult = false,
    department = "writter",
  )

  val aggregateCredits = AggregateCredits(
    cast = listOf(castItem),
    crew = listOf(crewItem),
  )

  val mediaCredits = mediaCreditsResponse.toMediaCredits()

  val mediaKeywordsResponseItem = MediaKeywordsResponseItem(id = 333, name = "crime")

  val tvKeywordsResponse = TvKeywordsResponse(keywords = listOf(mediaKeywordsResponseItem))

  val movieKeywordsResponse = MovieKeywordsResponse(keywords = listOf(mediaKeywordsResponseItem))

  val mediaKeywordsItem1 = MediaKeywordsItem(id = 10, name = "superhero")

  val mediaKeywordsItem2 = MediaKeywordsItem(id = 20, name = "adventure")

  val mediaKeywordsItems = listOf(mediaKeywordsItem1, mediaKeywordsItem2)

  val mediaKeywords = MediaKeywords(keywords = mediaKeywordsItems)

  val genresResponseItem = GenresResponseItem(id = 1, name = "Action")

  val releaseDatesItemValueResponse = ReleaseDatesResponseItemValue(
    descriptors = listOf("R"),
    note = "Test note",
    type = 3,
    iso6391 = "en",
    certification = "R",
    releaseDate = "2024-01-01",
  )

  val releaseDatesResponseItem = ReleaseDatesResponseItem(
    iso31661 = "US",
    listReleaseDateResponseItemValue = listOf(releaseDatesItemValueResponse),
  )

  val releaseDatesResponse = ReleaseDatesResponse(
    listReleaseDatesResponseItem = listOf(releaseDatesResponseItem),
  )

  val belongsToCollectionResponse = BelongsToCollectionResponse(
    backdropPath = "/backdrop.jpg",
    name = "Test Collection",
    id = 1,
    posterPath = "/poster.jpg",
  )

  val externalIdResponse = ExternalIdResponse(
    imdbId = "tt1234567",
    id = 999,
    freebaseMid = "freebase mid",
    tvdbId = 4312,
    freebaseId = "freebase id",
    twitterId = "twitter id",
    tvrageId = 431342,
    facebookId = "facebook id",
    instagramId = "instagram id",
  )

  val tvExternalIds = externalIdResponse.toExternalTvID()

  val videoResponse = VideoResponse(
    results = listOf(VideoResponseItem(name = "Trailer", type = "Trailer", key = "Link Trailer")),
  )

  val video = videoResponse.toVideo()

  val detailMovieResponse = DetailMovieResponse(
    originalLanguage = "en",
    imdbId = "tt1234567",
    video = false,
    title = "Test Movie",
    backdropPath = "/backdrop.jpg",
    credits = mediaCreditsResponse,
    revenue = 1000000,
    genres = listOf(genresResponseItem),
    keywords = movieKeywordsResponse,
    popularity = 8.5,
    releaseDates = releaseDatesResponse,
    productionCountries = listOf(),
    id = 1,
    voteCount = 100,
    budget = 500000,
    overview = "Test overview",
    originalTitle = "Test Movie Original",
    runtime = 120,
    posterPath = "/poster.jpg",
    spokenLanguages = listOf(),
    productionCompanies = listOf(),
    releaseDate = "2024-01-01",
    voteAverage = 7.5,
    belongsToCollection = belongsToCollectionResponse,
    tagline = "Test tagline",
    adult = false,
    homepage = "https://testmovie.com",
    status = "Released",
    videos = videoResponse,
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
    writer = "Writer Name",
  )

  val networksResponseItem = NetworksResponseItem(
    logoPath = "/network_logo.jpg",
    name = "HBO",
    id = 1,
    originCountry = "US",
  )

  val seasonsResponseItem = SeasonsResponseItem(
    airDate = "2024-01-01",
    overview = "Season overview",
    episodeCount = 10,
    name = "Season 1",
    seasonNumber = 1,
    id = 1,
    posterPath = "/season_poster.jpg",
  )

  val createdByResponseItem = CreatedByResponseItem(
    gender = 1,
    creditId = "credit123",
    name = "Creator Name",
    profilePath = "/creator_profile.jpg",
    id = 1,
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
    voteCount = 100,
  )

  val contentRatingsResponseItem = ContentRatingsResponseItem(
    descriptors = listOf("Violence"),
    iso31661 = "US",
    rating = "TV-MA",
  )

  val contentRatingsResponse = ContentRatingsResponse(
    contentRatings = listOf(contentRatingsResponseItem),
  )

  val nextEpisodeToAirResponse = NextEpisodeToAirResponse(
    id = 674,
    name = "name",
    airDate = "2026-06-19",
  )

  val nextEpisodeToAir = nextEpisodeToAirResponse.toNextEpisodeToAir()

  val netflix = ProviderResponse(
    logoPath = "/9A1JSVmSgCK7b0e2h6f0s6s7L6M.jpg",
    providerId = 8,
    providerName = "Netflix",
    displayPriority = 1,
  )

  val amazonPrimeVideo = ProviderResponse(
    logoPath = "/emthp39XA2YScoYL1p0sdbAH2m3.jpg",
    providerId = 119,
    providerName = "Amazon Prime Video",
    displayPriority = 2,
  )

  val appleTv = ProviderResponse(
    logoPath = "/peURlLlr8jggOwK53fJ5wdQl05y.jpg",
    providerId = 350,
    providerName = "Apple TV",
    displayPriority = 3,
  )

  val watchProvidersResponse = WatchProvidersResponse(
    results = mapOf(
      "US" to WatchProvidersResponseItem(
        link = "https://www.themoviedb.org/movie/123/watch",
        ads = null,
        buy = listOf(appleTv),
        flatrate = listOf(netflix),
        free = null,
        rent = listOf(amazonPrimeVideo),
      ),
    ),
  )

  val detailTvResponse = DetailTvResponse(
    originalLanguage = "en",
    numberOfEpisodes = 10,
    networks = listOf(networksResponseItem),
    type = "Scripted",
    backdropPath = "/backdrop.jpg",
    aggregateCredits = aggregateCreditsResponse,
    genres = listOf(genresResponseItem),
    keywords = tvKeywordsResponse,
    popularity = 8.5,
    productionCountries = listOf(),
    id = 1,
    numberOfSeasons = 1,
    voteCount = 100,
    firstAirDate = "2024-01-01",
    overview = "Test TV show overview",
    seasons = listOf(seasonsResponseItem),
    languages = listOf("en"),
    createdBy = listOf(createdByResponseItem),
    lastEpisodeToAir = lastEpisodeToAirResponse,
    posterPath = "/poster.jpg",
    originCountry = listOf("US"),
    spokenLanguages = listOf(),
    productionCompanies = listOf(),
    originalName = "Test TV Show Original",
    voteAverage = 8.5,
    name = "Test TV Show",
    tagline = "Test tagline",
    episodeRunTime = listOf(60),
    contentRatings = contentRatingsResponse,
    adult = false,
    nextEpisodeToAir = nextEpisodeToAirResponse,
    inProduction = true,
    lastAirDate = "2024-01-15",
    homepage = "https://testtv.com",
    status = "Returning Series",
    externalIds = externalIdResponse,
    videos = videoResponse,
    watchProviders = watchProvidersResponse,
  )

  val tvDetailFull = TvDetail(
    originalLanguage = "en",
    numberOfEpisodes = 62,
    listNetworksItem = listOf(NetworksItem(name = "AMC")),
    type = "Scripted",
    backdropPath = "/backdrop.jpg",
    credits = aggregateCreditsResponse.toCredits(),
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
    nextEpisodeToAir = nextEpisodeToAir,
    inProduction = false,
    lastAirDate = "2013-09-29",
    homepage = "http://www.amc.com/shows/breaking-bad",
    status = "Ended",
    externalIds = tvExternalIds,
    videos = video,
  )

  private val ratingsResponseItem = RatingsResponseItem(
    value = "8.5/10",
    source = "Internet Movie Database",
  )

  val omdbDetailsResponse = OMDbDetailsResponse(
    metascore = "85",
    boxOffice = "$100,000,000",
    website = "https://movie.com",
    imdbRating = "8.5",
    imdbVotes = "500,000",
    ratings = listOf(ratingsResponseItem),
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
    writer = "Jonathan Nolan, Christopher Nolan",
  )

  val genresItems = listOf(
    GenresItem(id = 1, name = "Action"),
    GenresItem(id = 2, name = "Comedy"),
  )

  val ads = listOf(
    Provider(
      logoPath = "/ads.png",
      providerId = 1,
      providerName = "Ads Provider",
      displayPriority = 1,
    ),
  )

  val buy = listOf(
    Provider(
      logoPath = "/buy.png",
      providerId = 2,
      providerName = "Buy Provider",
      displayPriority = 2,
    ),
  )

  val flatrate = listOf(
    Provider(
      logoPath = "/stream.png",
      providerId = 3,
      providerName = "Streaming Provider",
      displayPriority = 3,
    ),
  )

  val free = listOf(
    Provider(
      logoPath = "/free.png",
      providerId = 4,
      providerName = "Free Provider",
      displayPriority = 4,
    ),
  )

  val rent = listOf(
    Provider(
      logoPath = "/rent.png",
      providerId = 5,
      providerName = "Rent Provider",
      displayPriority = 5,
    ),
  )

  val watchProvidersItem = WatchProvidersItem(
    link = "https://some-provider.com",
    ads = ads,
    buy = buy,
    flatrate = flatrate,
    free = free,
    rent = rent,
  )

  val watchProviders = WatchProviders(
    results = mapOf("US" to watchProvidersItem),
  )

  // region MOVIE
  const val MOVIE_ID = 1001
  val detailMovie = MovieDetail(
    id = MOVIE_ID,
    runtime = 120,
    imdbId = IMDB_ID,
    voteAverage = 7.5,
    listGenres = listOf(GenresItem(id = 28, name = "Action")),
    keywords = mediaKeywords,
    releaseDates = ReleaseDates(
      listReleaseDatesItem = listOf(
        ReleaseDatesItem(
          iso31661 = "US",
          listReleaseDatesItemValue = listOf(
            ReleaseDatesItemValue(
              releaseDate = "2023-11-20T00:00:00.000Z",
              certification = "PG-13",
            ),
          ),
        ),
      ),
    ),
    belongsToCollection = belongsToCollection,
  )

  val movieMediaDetail = MediaDetail(
    id = MOVIE_ID,
    genre = transformListGenreToJoinString(detailMovie.listGenres),
    genreId = transformToGenreIDs(detailMovie.listGenres),
    duration = getTransformDuration(detailMovie.runtime),
    imdbId = IMDB_ID,
    ageRating = getAgeRating(
      detailMovie,
      getReleaseDateRegion(detailMovie, USER_REGION).regionRelease,
    ),
    tmdbScore = getTransformTMDBScore(detailMovie.voteAverage),
    releaseDateRegion = getReleaseDateRegion(detailMovie, USER_REGION),
  )

  val fullMovieDetail = MovieDetail(
    id = 1,
    originalLanguage = "en",
    listGenres = genresItems,
    keywords = mediaKeywords,
    voteAverage = 8.0,
    status = "Released",
    runtime = 120,
    imdbId = "tt9999999",
    budget = 1000000,
    revenue = 5000000L,
    popularity = 4444.0,
    videos = video,
    watchProviders = watchProviders,
    releaseDate = "2025-07-01",
    posterPath = "posterPath",
    backdropPath = "backdropPath",
    overview = "overview",
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
        ContentRatingsItem(iso31661 = "US", rating = "TV-MA"),
      ),
    ),
    firstAirDate = "2023-12-20T00:00:00.000Z",
  )

  val fullTvDetail = TvDetail(
    id = 1,
    originalLanguage = "en",
    listGenres = genresItems,
    keywords = mediaKeywords,
    voteAverage = 7.5,
    status = "Returning Series",
    listOriginCountry = listOf("US"),
    firstAirDate = "2020-01-01",
    lastAirDate = "2023-06-01",
    numberOfSeasons = 4,
    numberOfEpisodes = 96,
    popularity = 9.0,
    externalIds = tvExternalIds,
    videos = video,
    watchProviders = watchProviders,
    posterPath = "posterPath",
    backdropPath = "backdropPath",
    overview = "overview",
  )

  // endregion TV

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
    website = "https://example.com",
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
    originCountry = listOf(),
  )

  val postModelAddFavoriteStateSuccess =
    UpdateMediaStateResult(
      isSuccess = true,
      isDelete = false,
      isFavorite = true,
    )

  val postModelDeleteFavoriteStateSuccess =
    UpdateMediaStateResult(
      isSuccess = true,
      isDelete = true,
      isFavorite = true,
    )

  val postModelAddWatchlistStateSuccess =
    UpdateMediaStateResult(
      isSuccess = true,
      isDelete = false,
      isFavorite = false,
    )

  val postModelDeleteWatchlistStateSuccess =
    UpdateMediaStateResult(
      isSuccess = true,
      isDelete = true,
      isFavorite = false,
    )

  val releaseDateRegion = ReleaseDateRegion(
    regionRelease = "US",
    releaseDate = "2023",
  )

  val favoriteMovie = Favorite(
    id = 4545,
    mediaId = 654324,
    mediaType = MOVIE_MEDIA_TYPE,
    genre = "Adventure",
    backDrop = "",
    poster = "",
    overview = "",
    title = "Movie Adventure",
    releaseDate = "",
    popularity = 890342.0,
    rating = 7f,
    isFavorite = false,
    isWatchlist = false,
    lastUpdated = System.currentTimeMillis(),
  )
}
