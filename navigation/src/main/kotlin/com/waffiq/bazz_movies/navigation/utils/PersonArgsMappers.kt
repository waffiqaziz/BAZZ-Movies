package com.waffiq.bazz_movies.navigation.utils

import com.waffiq.bazz_movies.core.models.MediaCastItem
import com.waffiq.bazz_movies.navigation.PersonArgs

fun MediaCastItem.toPersonArgs() =
  PersonArgs(
    id = id ?: 0,
    profilePath = profilePath,
    name = name,
    originalName = originalName,
  )
