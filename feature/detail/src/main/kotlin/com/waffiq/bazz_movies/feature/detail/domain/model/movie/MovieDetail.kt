package com.waffiq.bazz_movies.feature.detail.domain.model.movie

import com.waffiq.bazz_movies.core.domain.GenresItem
import com.waffiq.bazz_movies.feature.detail.domain.model.BelongsToCollection
import com.waffiq.bazz_movies.feature.detail.domain.model.ProductionCompaniesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.ProductionCountriesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.SpokenLanguagesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDates

data class MovieDetail(
  val originalLanguage: String? = null,
  val imdbId: String? = null,
  val video: Boolean? = null,
  val title: String? = null,
  val backdropPath: String? = null,
  val revenue: Long? = null,
  val listGenres: List<GenresItem?>? = null,
  val popularity: Double? = null,
  val releaseDates: ReleaseDates? = null,
  val listProductionCountriesItem: List<ProductionCountriesItem?>? = null,
  val id: Int? = null,
  val voteCount: Int? = null,
  val budget: Int? = null,
  val overview: String? = null,
  val originalTitle: String? = null,
  val runtime: Int? = null,
  val posterPath: String? = null,
  val listSpokenLanguagesItem: List<SpokenLanguagesItem?>? = null,
  val listProductionCompaniesItem: List<ProductionCompaniesItem?>? = null,
  val releaseDate: String? = null,
  val voteAverage: Double? = null,
  val belongsToCollection: BelongsToCollection? = null,
  val tagline: String? = null,
  val adult: Boolean? = null,
  val homepage: String? = null,
  val status: String? = null
)
