package com.waffiq.bazz_movies.feature.detail.utils.mappers

import com.waffiq.bazz_movies.core.domain.MediaCastItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.castcrew.MediaCastResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.castcrew.MediaCrewResponseItem
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCrewItem

object MediaCreditsMapper {

  fun MediaCastResponseItem.toMediaCastItem() =
    MediaCastItem(
      castId = castId,
      character = character,
      gender = gender,
      creditId = creditId,
      knownForDepartment = knownForDepartment,
      originalName = originalName,
      popularity = popularity,
      name = name,
      profilePath = profilePath,
      id = id,
      adult = adult,
      order = order,
    )

  fun MediaCrewResponseItem.toMediaCrewItem() =
    MediaCrewItem(
      gender = gender,
      creditId = creditId,
      knownForDepartment = knownForDepartment,
      originalName = originalName,
      popularity = popularity,
      name = name,
      profilePath = profilePath,
      id = id,
      adult = adult,
      department = department,
      job = job,
    )
}
