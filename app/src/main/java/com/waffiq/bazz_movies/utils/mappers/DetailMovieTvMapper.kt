package com.waffiq.bazz_movies.utils.mappers

import com.waffiq.bazz_movies.data.remote.responses.omdb.OMDbDetailsResponse
import com.waffiq.bazz_movies.data.remote.responses.omdb.RatingsItemResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.person.CastItemResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.person.CombinedCreditResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.person.CrewItemResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.detail_movie_tv.movie.DetailMovieResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.detail_movie_tv.tv.DetailTvResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.detail_movie_tv.tv.ExternalIdResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.detail_movie_tv.cast_crew.MovieTvCreditsResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.StatedResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.detail_movie_tv.video_media.VideoItemResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.detail_movie_tv.video_media.VideoResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.detail_movie_tv.movie.BelongsToCollectionResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.detail_movie_tv.ProductionCountriesItemResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.detail_movie_tv.release_dates.ReleaseDatesItemResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.detail_movie_tv.release_dates.ReleaseDatesItemValueResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.detail_movie_tv.release_dates.ReleaseDatesResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.detail_movie_tv.GenresItemResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.detail_movie_tv.tv.ContentRatingsItemResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.detail_movie_tv.tv.ContentRatingsResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.detail_movie_tv.tv.CreatedByItemResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.detail_movie_tv.tv.LastEpisodeToAirResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.detail_movie_tv.tv.NetworksItemResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.detail_movie_tv.tv.ProductionCompaniesItemResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.detail_movie_tv.tv.SeasonsItemResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.detail_movie_tv.tv.SpokenLanguagesItemReponse
import com.waffiq.bazz_movies.domain.model.Stated
import com.waffiq.bazz_movies.domain.model.detail.BelongsToCollection
import com.waffiq.bazz_movies.domain.model.detail.DetailMovie
import com.waffiq.bazz_movies.domain.model.detail.GenresItem
import com.waffiq.bazz_movies.domain.model.detail.tv.DetailTv
import com.waffiq.bazz_movies.domain.model.detail.tv.ExternalTvID
import com.waffiq.bazz_movies.domain.model.detail.MovieTvCredits
import com.waffiq.bazz_movies.domain.model.detail.ProductionCompaniesItem
import com.waffiq.bazz_movies.domain.model.detail.ProductionCountriesItem
import com.waffiq.bazz_movies.domain.model.detail.ReleaseDates
import com.waffiq.bazz_movies.domain.model.detail.ReleaseDatesItem
import com.waffiq.bazz_movies.domain.model.detail.ReleaseDatesItemValue
import com.waffiq.bazz_movies.domain.model.detail.SpokenLanguagesItem
import com.waffiq.bazz_movies.domain.model.detail.Video
import com.waffiq.bazz_movies.domain.model.detail.VideoItem
import com.waffiq.bazz_movies.domain.model.detail.tv.ContentRatings
import com.waffiq.bazz_movies.domain.model.detail.tv.ContentRatingsItem
import com.waffiq.bazz_movies.domain.model.detail.tv.CreatedByItem
import com.waffiq.bazz_movies.domain.model.detail.tv.LastEpisodeToAir
import com.waffiq.bazz_movies.domain.model.detail.tv.NetworksItem
import com.waffiq.bazz_movies.domain.model.detail.tv.SeasonsItem
import com.waffiq.bazz_movies.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.domain.model.omdb.RatingsItem
import com.waffiq.bazz_movies.domain.model.person.CastItem
import com.waffiq.bazz_movies.domain.model.person.CombinedCreditPerson
import com.waffiq.bazz_movies.domain.model.person.CrewItem

object DetailMovieTvMapper {

  // region MOVIE & TV
  fun CombinedCreditResponse.toCombinedCredit() = CombinedCreditPerson(
    cast = cast?.map { it.toCastItem() },
    id = id,
    crew = crew?.map { it.toCrewItem() },
  )

  private fun CastItemResponse.toCastItem() = CastItem(
    firstAirDate = firstAirDate,
    overview = overview,
    originalLanguage = originalLanguage,
    episodeCount = episodeCount ?: 0,
    listGenreIds = genreIds,
    posterPath = posterPath,
    listOriginCountry = originCountry,
    backdropPath = backdropPath,
    character = character,
    creditId = creditId,
    mediaType = mediaType ?: "movie",
    originalName = originalName,
    popularity = popularity ?: 0.0,
    voteAverage = voteAverage ?: 0f,
    name = name,
    id = id ?: 0,
    adult = adult ?: false,
    voteCount = voteCount ?: 0,
    originalTitle = originalTitle,
    video = video ?: false,
    title = title,
    releaseDate = releaseDate,
    order = order ?: 0,
  )

  private fun CrewItemResponse.toCrewItem() = CrewItem(
    overview = overview,
    originalLanguage = originalLanguage,
    originalTitle = originalTitle,
    video = video,
    title = title,
    genreIds = genreIds,
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = releaseDate,
    creditId = creditId,
    mediaType = mediaType,
    popularity = popularity,
    voteAverage = voteAverage,
    id = id,
    adult = adult,
    department = department,
    job = job,
    voteCount = voteCount,
  )

  fun VideoResponse.toVideo() = Video(
    id = id,
    results = results.map { it.toVideoItem() }
  )

  private fun VideoItemResponse.toVideoItem() = VideoItem(
    site = site,
    size = size,
    iso31661 = iso31661,
    name = name,
    official = official,
    id = id,
    publishedAt = publishedAt,
    type = type,
    iso6391 = iso6391,
    key = key
  )

  fun MovieTvCreditsResponse.toMovieTvCredits() = MovieTvCredits(
    cast = cast,
    id = id,
    crew = crew
  )

  fun StatedResponse.toStated() = Stated(
    id = id,
    favorite = favorite,
    rated = rated,
    watchlist = watchlist
  )

  fun OMDbDetailsResponse.toOMDbDetails() = OMDbDetails(
    metascore = metascore,
    boxOffice = boxOffice,
    website = website,
    imdbRating = imdbRating,
    imdbVotes = imdbVotes,
    ratings = ratings?.map { it.toRatingsItem() },
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

  private fun RatingsItemResponse.toRatingsItem() = RatingsItem(
    value = value,
    source = source
  )

  private fun GenresItemResponse.toGenresItem() = GenresItem(
    name = name,
    id = id
  )

  private fun SpokenLanguagesItemReponse.toSpokenLanguagesItem() = SpokenLanguagesItem(
    name = name,
    iso6391 = iso6391,
    englishName = englishName
  )


  private fun ProductionCountriesItemResponse.toProductionCountriesItem() = ProductionCountriesItem(
    iso31661 = iso31661,
    name = name,
    type = type,
    iso6391 = iso6391,
    certification = certification
  )

  private fun ProductionCompaniesItemResponse.toProductionCompaniesItem() = ProductionCompaniesItem(
    logoPath = logoPath,
    name = name,
    id = id,
    originCountry = originCountry
  )
  // endregion MOVIE & TV


  // region MOVIE
  fun DetailMovieResponse.toDetailMovie() = DetailMovie(
    originalLanguage = originalLanguage,
    imdbId = imdbId,
    video = video,
    title = title,
    backdropPath = backdropPath,
    revenue = revenue,
    listGenres = listGenresItemResponse?.map { it?.toGenresItem() ?: GenresItem() },
    popularity = popularity,
    releaseDates = releaseDatesResponse?.toReleaseDates(),
    listProductionCountriesItem = listProductionCountriesItemResponse?.map { it?.toProductionCountriesItem() },
    id = id,
    voteCount = voteCount,
    budget = budget,
    overview = overview,
    originalTitle = originalTitle,
    runtime = runtime,
    posterPath = posterPath,
    listSpokenLanguagesItem = listSpokenLanguagesItemResponse?.map { it?.toSpokenLanguagesItem() },
    listProductionCompaniesItem = listProductionCompaniesItemResponse?.map { it?.toProductionCompaniesItem() },
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    belongsToCollection = belongsToCollectionResponse?.toBelongsToCollection(),
    tagline = tagline,
    adult = adult,
    homepage = homepage,
    status = status
  )

  private fun ReleaseDatesResponse.toReleaseDates() = ReleaseDates(
    listReleaseDatesItem = listReleaseDatesItemResponse?.map { it?.toReleaseDatesItem() }
  )

  private fun ReleaseDatesItemResponse.toReleaseDatesItem() = ReleaseDatesItem(
    iso31661 = iso31661,
    listReleaseDatesitemValue = listReleaseDateItemValueResponse?.map { it.toReleaseDatesItemValue() }
  )

  private fun ReleaseDatesItemValueResponse.toReleaseDatesItemValue() = ReleaseDatesItemValue(
    descriptors = descriptors,
    note = note,
    type = type,
    iso6391 = iso6391,
    certification = certification,
    releaseDate = releaseDate
  )

  private fun BelongsToCollectionResponse.toBelongsToCollection() = BelongsToCollection(
    backdropPath = backdropPath,
    name = name,
    id = id,
    posterPath = posterPath
  )
  // endregion MOVIE


  // region TV
  fun DetailTvResponse.toDetailTv() = DetailTv(
    originalLanguage = originalLanguage,
    numberOfEpisodes = numberOfEpisodes,
    listNetworksItem = networksResponse?.map { it?.toNetworksItem() },
    type = type,
    backdropPath = backdropPath,
    listGenres = genres?.map { it?.toGenresItem() ?: GenresItem() },
    popularity = popularity,
    listProductionCountriesItem = productionCountriesResponse?.map { it?.toProductionCountriesItem() },
    id = id,
    numberOfSeasons = numberOfSeasons,
    voteCount = voteCount,
    firstAirDate = firstAirDate,
    overview = overview,
    listSeasonsItem = seasonsResponse?.map { it?.toSeasonsItem() },
    listLanguages = languages,
    listCreatedByItem = createdByResponse?.map { it?.toCreatedByItem() },
    lastEpisodeToAir = lastEpisodeToAirResponse?.toLastEpisodeToAir(),
    posterPath = posterPath,
    listOriginCountry = originCountry,
    listSpokenLanguagesItem = spokenLanguagesResponse?.map { it?.toSpokenLanguagesItem() },
    listProductionCompaniesItem = productionCompaniesResponse?.map { it?.toProductionCompaniesItem() },
    originalName = originalName,
    voteAverage = voteAverage,
    name = name,
    tagline = tagline,
    listEpisodeRunTime = episodeRunTime,
    contentRatings = contentRatingsResponse?.toContentRatings(),
    adult = adult,
    nextEpisodeToAir = nextEpisodeToAir,
    inProduction = inProduction,
    lastAirDate = lastAirDate,
    homepage = homepage,
    status = status,
  )

  private fun ContentRatingsResponse.toContentRatings() = ContentRatings(
    contentRatingsItem = contentRatingsItemResponse?.map { it?.toContentRatingsItem() }
  )

  private fun ContentRatingsItemResponse.toContentRatingsItem() = ContentRatingsItem(
    descriptors = descriptors,
    iso31661 = iso31661,
    rating = rating,
  )

  private fun NetworksItemResponse.toNetworksItem() = NetworksItem(
    logoPath = logoPath,
    name = name,
    id = id,
    originCountry = originCountry
  )

  private fun SeasonsItemResponse.toSeasonsItem() = SeasonsItem(
    airDate = airDate,
    overview = overview,
    episodeCount = episodeCount,
    name = name,
    seasonNumber = seasonNumber,
    id = id,
    posterPath = posterPath
  )

  private fun CreatedByItemResponse.toCreatedByItem() = CreatedByItem(
    gender = gender,
    creditId = creditId,
    name = name,
    profilePath = profilePath,
    id = id
  )

  private fun LastEpisodeToAirResponse.toLastEpisodeToAir() = LastEpisodeToAir(
    productionCode = productionCode,
    airDate = airDate,
    overview = overview,
    episodeNumber = episodeNumber,
    showId = showId,
    voteAverage = voteAverage,
    name = name,
    seasonNumber = seasonNumber,
    runtime = runtime,
    id = id,
    stillPath = stillPath,
    voteCount = voteCount
  )

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
  // endregion TV
}