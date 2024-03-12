package com.waffiq.bazz_movies.data.local.model

data class FilterSearch (
  /**
   * filter type
   * 1 -> multi
   * 2 -> movie
   * 3 -> tv series
   * 4 -> person
   */
  val filterType : Byte? = null,

  /**
   * filter genre
   * https://developer.themoviedb.org/reference/genre-tv-list
   * https://developer.themoviedb.org/reference/genre-movie-list
   */
  val genre: String? = null,

  /**
   * sort by
   * 1 -> popularity
   * 2 -> A-Z
   * 2 -> Z-A
   */
  val sortBy: Byte? = null
)
