package com.waffiq.bazz_movies.core.network.data.remote.query

import com.waffiq.bazz_movies.core.network.data.remote.constants.Genre
import com.waffiq.bazz_movies.core.network.data.remote.constants.Keyword
import com.waffiq.bazz_movies.core.network.data.remote.constants.SortBy

data class DiscoverMovieParams(
  val page: Int = 1,
  val genres: List<Genre>? = null,
  val genre: String? = null,
  val keywords: List<Keyword>? = null,
  val keyword: String? = null,
  val releaseDateGte: String? = null,
  val releaseDateLte: String? = null,
  val watchRegion: String? = null,
  val sortBy: String = SortBy.POPULARITY_DESC,
  val includeAdult: Boolean = false,
  val language: String = "en-US",
)
