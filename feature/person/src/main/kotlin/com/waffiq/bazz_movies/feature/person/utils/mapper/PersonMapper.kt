package com.waffiq.bazz_movies.feature.person.utils.mapper

import androidx.annotation.VisibleForTesting
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.CastItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.CombinedCreditResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.CrewItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.DetailPersonResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.ExternalIDPersonResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.ImagePersonResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.ProfilesItemResponse
import com.waffiq.bazz_movies.feature.person.domain.model.CastItem
import com.waffiq.bazz_movies.feature.person.domain.model.CombinedCreditPerson
import com.waffiq.bazz_movies.feature.person.domain.model.CrewItem
import com.waffiq.bazz_movies.feature.person.domain.model.DetailPerson
import com.waffiq.bazz_movies.feature.person.domain.model.ExternalIDPerson
import com.waffiq.bazz_movies.feature.person.domain.model.ImagePerson
import com.waffiq.bazz_movies.feature.person.domain.model.ProfilesItem

object PersonMapper {

  fun CombinedCreditResponse.toCombinedCredit() = CombinedCreditPerson(
    cast = cast?.map { it.toCastItem() },
    id = id,
    crew = crew?.map { it.toCrewItem() },
  )

  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  fun CastItemResponse.toCastItem() = CastItem(
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
    mediaType = mediaType ?: MOVIE_MEDIA_TYPE,
    originalName = originalName,
    popularity = popularity ?: 0.0,
    voteAverage = voteAverage ?: 0f,
    name = name,
    id = id ?: 0,
    adult = adult == true,
    voteCount = voteCount ?: 0,
    originalTitle = originalTitle,
    video = video == true,
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
    profiles = profiles?.map { it.toProfilesItem() },
    id = id
  )

  private fun ProfilesItemResponse.toProfilesItem() = ProfilesItem(
    aspectRatio = aspectRatio,
    filePath = filePath,
    voteAverage = voteAverage,
    width = width,
    iso6391 = iso6391,
    voteCount = voteCount,
    height = height
  )

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
}
