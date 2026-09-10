package com.waffiq.bazz_movies.feature.detail.utils.mapper

import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCastItem
import com.waffiq.bazz_movies.navigation.PersonArgs

object PersonArgsMappers {

  fun MediaCastItem.toPersonArgs() =
    PersonArgs(
      id = id ?: 0,
      profilePath = profilePath,
      name = name,
      originalName = originalName,
    )
}
