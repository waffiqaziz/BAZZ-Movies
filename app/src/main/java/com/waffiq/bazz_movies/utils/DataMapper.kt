package com.waffiq.bazz_movies.utils

import com.waffiq.bazz_movies.data.local.model.FavoriteEntity
import com.waffiq.bazz_movies.data.remote.response.countryip.CountryIPResponse
import com.waffiq.bazz_movies.data.remote.response.omdb.OMDbDetailsResponse
import com.waffiq.bazz_movies.data.remote.response.omdb.RatingsItemResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.CastItemResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.CombinedCreditResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.CrewItemResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.DetailMovieResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.DetailPersonResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.DetailTvResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.ExternalIDPersonResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.ExternalIdResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.ImagePersonResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.MovieTvCreditsResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.ProfilesItemResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.ResultItemResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.VideoItemResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.VideoResponse
import com.waffiq.bazz_movies.domain.model.CountryIP
import com.waffiq.bazz_movies.domain.model.person.DetailPerson
import com.waffiq.bazz_movies.domain.model.Favorite
import com.waffiq.bazz_movies.domain.model.Stated
import com.waffiq.bazz_movies.domain.model.detail.DetailMovie
import com.waffiq.bazz_movies.domain.model.detail.DetailTv
import com.waffiq.bazz_movies.domain.model.detail.ExternalTvID
import com.waffiq.bazz_movies.domain.model.detail.MovieTvCredits
import com.waffiq.bazz_movies.domain.model.detail.Video
import com.waffiq.bazz_movies.domain.model.detail.VideoItem
import com.waffiq.bazz_movies.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.domain.model.omdb.RatingsItem
import com.waffiq.bazz_movies.domain.model.person.CastItem
import com.waffiq.bazz_movies.domain.model.person.CombinedCreditPerson
import com.waffiq.bazz_movies.domain.model.person.CrewItem
import com.waffiq.bazz_movies.domain.model.person.ExternalIDPerson
import com.waffiq.bazz_movies.domain.model.person.ImagePerson
import com.waffiq.bazz_movies.domain.model.person.ProfilesItem

object DataMapper {

  private fun mapResponsesToDomainFavorite(
    isFavorite: Boolean,
    isWatchlist: Boolean,
    input: ResultItemResponse
  ): Favorite {
    return Favorite(
      id = 0,
      mediaId = input.id ?: error("No ID for FavoriteEntity"),
      mediaType = input.mediaType ?: error("No Media Type for FavoriteEntity"),
      title = input.name ?: input.originalName ?: input.title ?: input.originalTitle ?: "N/A",
      releaseDate = input.releaseDate ?: input.firstAirDate ?: "N/A",
      rating = input.voteAverage ?: 0.0f,
      backDrop = input.backdropPath ?: "N/A",
      poster = input.posterPath ?: "N/A",
      genre = Helper.iterateGenre(input.genreIds ?: listOf()),
      popularity = input.popularity ?: 0.0,
      overview = input.overview ?: "N/A",
      isFavorite = isFavorite,
      isWatchlist = isWatchlist
    )
  }

  fun favTrueWatchlistTrue(data: ResultItemResponse): Favorite {
    return mapResponsesToDomainFavorite(isFavorite = true, isWatchlist = true, input = data)
  }

  fun favTrueWatchlistFalse(data: ResultItemResponse): Favorite {
    return mapResponsesToDomainFavorite(isFavorite = true, isWatchlist = false, input = data)
  }

  fun favFalseWatchlistTrue(data: ResultItemResponse): Favorite {
    return mapResponsesToDomainFavorite(isFavorite = false, isWatchlist = true, input = data)
  }

  fun favFalseWatchlistFalse(data: ResultItemResponse): Favorite {
    return mapResponsesToDomainFavorite(isFavorite = false, isWatchlist = false, input = data)
  }

  fun mapEntitiesToDomainFavorite(data: List<FavoriteEntity>): List<Favorite> =
    data.map {
      Favorite(
        id = it.id,
        mediaId = it.mediaId,
        mediaType = it.mediaType,
        genre = it.genre,
        backDrop = it.backDrop,
        poster = it.poster,
        overview = it.overview,
        title = it.title,
        releaseDate = it.releaseDate,
        popularity = it.popularity,
        rating = it.rating,
        isFavorite = it.isFavorite,
        isWatchlist = it.isWatchlist,
      )
    }

  fun Favorite.toFavoriteEntity() = FavoriteEntity(
    id = id,
    mediaId = mediaId,
    mediaType = mediaType,
    genre = genre,
    backDrop = backDrop,
    poster = poster,
    overview = overview,
    title = title,
    releaseDate = releaseDate,
    popularity = popularity,
    rating = rating,
    isFavorite = isFavorite,
    isWatchlist = isWatchlist,
  )

  // endregion PERSON
  fun DetailPersonResponse.toDetailPerson() = DetailPerson(
    alsoKnownAs = alsoKnownAs,
    birthday = birthday,
    gender = gender,
    imdbId = imdbId,
    knownForDepartment = knownForDepartment,
    profilePath = profilePath,
    biography = biography,
    deathday = deathday,
    placeOfBirth = placeOfBirth,
    popularity = popularity,
    name = name,
    id = id,
    adult = adult,
    homepage = homepage,
  )


  fun ImagePersonResponse.toImagePerson() = ImagePerson(
    profiles = mapResponseToDomainProfilesItem(profiles),
    id = id
  )

  private fun mapResponseToDomainProfilesItem(data: List<ProfilesItemResponse>?) =
    data?.map {
      ProfilesItem(
        aspectRatio = it.aspectRatio,
        filePath = it.filePath,
        voteAverage = it.voteAverage,
        width = it.width,
        iso6391 = it.iso6391,
        voteCount = it.voteCount,
        height = it.height
      )

    }

  fun ExternalIDPersonResponse.toExternalIDPerson() = ExternalIDPerson(
    imdbId = imdbId,
    freebaseMid = freebaseMid,
    tiktokId = tiktokId,
    wikidataId = wikidataId,
    id = id,
    freebaseId = freebaseId,
    twitterId = twitterId,
    youtubeId = youtubeId,
    tvrageId = tvrageId,
    facebookId = facebookId,
    instagramId = instagramId
  )
  // region PERSON

  // region DETAIL
  fun CombinedCreditResponse.toCombinedCredit() = CombinedCreditPerson(
    cast = mapResponseToDomainCastItem(cast),
    id = id,
    crew = mapResponseToDomainCrewItem(crew),
  )

  private fun mapResponseToDomainCastItem(data: List<CastItemResponse>?) =
    data?.map {
      CastItem(
        firstAirDate = it.firstAirDate,
        overview = it.overview,
        originalLanguage = it.originalLanguage,
        episodeCount = it.episodeCount,
        genreIds = it.genreIds,
        posterPath = it.posterPath,
        originCountry = it.originCountry,
        backdropPath = it.backdropPath,
        character = it.character,
        creditId = it.creditId,
        mediaType = it.mediaType,
        originalName = it.originalName,
        popularity = it.popularity,
        voteAverage = it.voteAverage,
        name = it.name,
        id = it.id,
        adult = it.adult,
        voteCount = it.voteCount,
        originalTitle = it.originalTitle,
        video = it.video,
        title = it.title,
        releaseDate = it.releaseDate,
        order = it.order,
      )
    }

  private fun mapResponseToDomainCrewItem(data: List<CrewItemResponse>?) =
    data?.map {
      CrewItem(
        overview = it.overview,
        originalLanguage = it.originalLanguage,
        originalTitle = it.originalTitle,
        video = it.video,
        title = it.title,
        genreIds = it.genreIds,
        posterPath = it.posterPath,
        backdropPath = it.backdropPath,
        releaseDate = it.releaseDate,
        creditId = it.creditId,
        mediaType = it.mediaType,
        popularity = it.popularity,
        voteAverage = it.voteAverage,
        id = it.id,
        adult = it.adult,
        department = it.department,
        job = it.job,
        voteCount = it.voteCount,
      )
    }
  // endregion DETAIL

  fun OMDbDetailsResponse.toOMDbDetails() = OMDbDetails(
    metascore = metascore,
    boxOffice = boxOffice,
    website = website,
    imdbRating = imdbRating,
    imdbVotes = imdbVotes,
    ratings = mapResponseToDomainRatingsItem(ratings),
    runtime = runtime,
    language = language,
    rated = rated,
    production = production,
    released = released,
    imdbID = imdbID,
    plot = plot,
    director = director,
    title = title,
    actors = actors,
    response = response,
    type = type,
    awards = awards,
    dVD = dVD,
    year = year,
    poster = poster,
    country = country,
    genre = genre,
    writer = writer
  )

  private fun mapResponseToDomainRatingsItem(data: List<RatingsItemResponse>?) =
    data?.map {
      RatingsItem(
        value = it.value,
        source = it.source,
      )
    }

  fun ExternalIdResponse.toExternalTvID() = ExternalTvID(
    imdbId = imdbId,
    freebaseMid = freebaseMid,
    tvdbId = tvdbId,
    freebaseId = freebaseId,
    id = id,
    twitterId = twitterId,
    tvrageId = tvrageId,
    facebookId = facebookId,
    instagramId = instagramId
  )


  fun CountryIPResponse.toCountryIP() = CountryIP(
    country = country,
    ip = ip
  )

  fun VideoResponse.toVideo() = Video(
    id = id,
    results = mapResponseToDomainVideoItem(results)
  )

  private fun mapResponseToDomainVideoItem(data: List<VideoItemResponse>) = data.map {
    VideoItem(
      site = it.site,
      size = it.size,
      iso31661 = it.iso31661,
      name = it.name,
      official = it.official,
      id = it.id,
      publishedAt = it.publishedAt,
      type = it.type,
      iso6391 = it.iso6391,
      key = it.key
    )
  }

  fun DetailMovieResponse.toDetailMovie() = DetailMovie(
    originalLanguage = originalLanguage,
    imdbId = imdbId,
    video = video,
    title = title,
    backdropPath = backdropPath,
    revenue = revenue,
    genres = genres,
    popularity = popularity,
    releaseDates = releaseDates,
    productionCountries = productionCountries,
    id = id,
    voteCount = voteCount,
    budget = budget,
    overview = overview,
    originalTitle = originalTitle,
    runtime = runtime,
    posterPath = posterPath,
    spokenLanguages = spokenLanguages,
    productionCompanies = productionCompanies,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    belongsToCollection = belongsToCollection,
    tagline = tagline,
    adult = adult,
    homepage = homepage,
    status = status
  )

  fun DetailTvResponse.toDetailTv() = DetailTv(
    originalLanguage = originalLanguage,
    numberOfEpisodes = numberOfEpisodes,
    networks = networks,
    type = type,
    backdropPath = backdropPath,
    genres = genres,
    popularity = popularity,
    productionCountries = productionCountries,
    id = id,
    numberOfSeasons = numberOfSeasons,
    voteCount = voteCount,
    firstAirDate = firstAirDate,
    overview = overview,
    seasons = seasons,
    languages = languages,
    createdBy = createdBy,
    lastEpisodeToAir = lastEpisodeToAir,
    posterPath = posterPath,
    originCountry = originCountry,
    spokenLanguages = spokenLanguages,
    productionCompanies = productionCompanies,
    originalName = originalName,
    voteAverage = voteAverage,
    name = name,
    tagline = tagline,
    episodeRunTime = episodeRunTime,
    contentRatings = contentRatings,
    adult = adult,
    nextEpisodeToAir = nextEpisodeToAir,
    inProduction = inProduction,
    lastAirDate = lastAirDate,
    homepage = homepage,
    status = status,
  )

  fun MovieTvCreditsResponse.toMovieTvCredits() = MovieTvCredits(
    cast = cast,
    id = id,
    crew = crew
  )

  fun com.waffiq.bazz_movies.data.remote.response.tmdb.StatedResponse.toStated() = Stated(
    id = id,
    favorite = favorite,
    rated = rated,
    watchlist = watchlist
  )
}