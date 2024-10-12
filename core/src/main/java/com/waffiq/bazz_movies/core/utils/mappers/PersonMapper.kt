package com.waffiq.bazz_movies.core.utils.mappers

import com.waffiq.bazz_movies.core.data.remote.responses.tmdb.person.DetailPersonResponse
import com.waffiq.bazz_movies.core.data.remote.responses.tmdb.person.ExternalIDPersonResponse
import com.waffiq.bazz_movies.core.data.remote.responses.tmdb.person.ImagePersonResponse
import com.waffiq.bazz_movies.core.data.remote.responses.tmdb.person.ProfilesItemResponse
import com.waffiq.bazz_movies.core.domain.model.person.DetailPerson
import com.waffiq.bazz_movies.core.domain.model.person.ExternalIDPerson
import com.waffiq.bazz_movies.core.domain.model.person.ImagePerson
import com.waffiq.bazz_movies.core.domain.model.person.ProfilesItem

object PersonMapper {
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
