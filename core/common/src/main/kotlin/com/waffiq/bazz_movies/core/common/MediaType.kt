package com.waffiq.bazz_movies.core.common

enum class MediaType {
  MOVIE,
  TV,
  PERSON,
  ;

    companion object {
    private val byValue: Map<String, MediaType> = entries.associateBy { it.value }

    /*
     * Parses a TMDB `media_type` field ("movie", "tv", "person") into a [MediaType].
     * Returns null for unknown/blank values.
     */
    fun fromValue(value: String?): MediaType? = value?.let { byValue[it.lowercase()] }
  }
}

val MediaType.value: String
  get() = name.lowercase()
