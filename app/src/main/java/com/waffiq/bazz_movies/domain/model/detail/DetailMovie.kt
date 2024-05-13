package com.waffiq.bazz_movies.domain.model.detail

import com.waffiq.bazz_movies.data.remote.response.tmdb.BelongsToCollection
import com.waffiq.bazz_movies.data.remote.response.tmdb.GenresItem
import com.waffiq.bazz_movies.data.remote.response.tmdb.ProductionCompaniesItem
import com.waffiq.bazz_movies.data.remote.response.tmdb.ProductionCountriesItem
import com.waffiq.bazz_movies.data.remote.response.tmdb.ReleaseDates
import com.waffiq.bazz_movies.data.remote.response.tmdb.SpokenLanguagesItem

data class DetailMovie(
  val originalLanguage: String? = null,
  val imdbId: String? = null,
  val video: Boolean? = null,
  val title: String? = null,
  val backdropPath: String? = null,
  val revenue: Long? = null,
  val genres: List<GenresItem?>? = null,
  val popularity: Double? = null,
  val releaseDates: ReleaseDates? = null,
  val productionCountries: List<ProductionCountriesItem?>? = null,
  val id: Int? = null,
  val voteCount: Int? = null,
  val budget: Int? = null,
  val overview: String? = null,
  val originalTitle: String? = null,
  val runtime: Int? = null,
  val posterPath: String? = null,
  val spokenLanguages: List<SpokenLanguagesItem?>? = null,
  val productionCompanies: List<ProductionCompaniesItem?>? = null,
  val releaseDate: String? = null,
  val voteAverage: Double? = null,
  val belongsToCollection: BelongsToCollection? = null,
  val tagline: String? = null,
  val adult: Boolean? = null,
  val homepage: String? = null,
  val status: String? = null
)
