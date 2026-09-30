package com.waffiq.bazz_movies.core.database.utils

import com.waffiq.bazz_movies.core.database.data.model.GenreV1

object LegacyGenreMapper {

  // transform genre name like "Action, Romance, Horror" into [28, 10749, 27].
  fun namesToIds(raw: String): List<Int> =
    raw.split(",")
      .map { it.trim() }
      .filter { it.isNotEmpty() }
      .mapNotNull { token -> token.toIntOrNull() ?: GenreV1.fromName(token)?.id }
      .distinct()

  // transform list of genre id into string format
  fun namesToIdString(raw: String): String = namesToIds(raw).joinToString(",")
}
