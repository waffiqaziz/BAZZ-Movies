package com.waffiq.bazz_movies.feature.detail.domain.model.movie

import com.waffiq.bazz_movies.core.models.Imageble

data class BelongsToCollection(
  val id: Int? = null,
  val name: String? = null,
  override val backdropPath: String? = null,
  override val posterPath: String? = null,
) : Imageble
