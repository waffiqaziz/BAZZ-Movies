package com.waffiq.bazz_movies.feature.detail.domain.model.movie

import com.waffiq.bazz_movies.core.models.Imageble

data class DetailCollections(
  val id: Int? = null,
  val name: String? = null,
  val originalName: String? = null,
  val overview: String? = null,
  override val backdropPath: String? = null,
  override val posterPath: String? = null,
  val originalLanguage: String? = null,
  val parts: List<PartsItem?>? = null,
) : Imageble

/**
 * Returns all unique genre IDs from the collection parts, sorted in descending order.
 */
val DetailCollections.genreIds: List<Int>
  get() = parts
    .orEmpty()
    .flatMap { it?.genreIds.orEmpty() }
    .toSet()
    .sorted()
