package com.waffiq.bazz_movies.feature.search.utils

import androidx.annotation.StringRes
import com.waffiq.bazz_movies.core.common.MediaType
import com.waffiq.bazz_movies.core.designsystem.R.string.movie
import com.waffiq.bazz_movies.core.designsystem.R.string.person
import com.waffiq.bazz_movies.core.designsystem.R.string.tv_series

@StringRes
fun MediaType.labelRes(): Int =
  when (this) {
    MediaType.PERSON -> person
    MediaType.MOVIE -> movie
    MediaType.TV -> tv_series
    MediaType.MULTI -> error("MULTI is not a selectable chip")
  }

fun List<String>?.toMediaTypeSet(): Set<MediaType> =
  this
    ?.mapNotNull { runCatching { MediaType.valueOf(it) }.getOrNull() }
    ?.toSet()
    ?.takeIf { it.isNotEmpty() }
    ?: setOf(MediaType.MULTI)
