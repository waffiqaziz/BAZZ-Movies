package com.waffiq.bazz_movies.feature.detail.domain.model.tv

import com.waffiq.bazz_movies.core.domain.GenresItem
import com.waffiq.bazz_movies.feature.detail.domain.model.ProductionCompaniesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.ProductionCountriesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.SpokenLanguagesItem

data class TvDetail(
  val originalLanguage: String? = null,
  val numberOfEpisodes: Int? = null,
  val listNetworksItem: List<NetworksItem?>? = null,
  val type: String? = null,
  val backdropPath: String? = null,
  val listGenres: List<GenresItem?>? = null,
  val popularity: Double? = null,
  val listProductionCountriesItem: List<ProductionCountriesItem?>? = null,
  val id: Int? = null,
  val numberOfSeasons: Int? = null,
  val voteCount: Int? = null,
  val firstAirDate: String? = null,
  val overview: String? = null,
  val listSeasonsItem: List<SeasonsItem?>? = null,
  val listLanguages: List<String?>? = null,
  val listCreatedByItem: List<CreatedByItem?>? = null,
  val lastEpisodeToAir: LastEpisodeToAir? = null,
  val posterPath: String? = null,
  val listOriginCountry: List<String?>? = null,
  val listSpokenLanguagesItem: List<SpokenLanguagesItem?>? = null,
  val listProductionCompaniesItem: List<ProductionCompaniesItem?>? = null,
  val originalName: String? = null,
  val voteAverage: Double? = null,
  val name: String? = null,
  val tagline: String? = null,
  val listEpisodeRunTime: List<Int?>? = null,
  val contentRatings: ContentRatings? = null,
  val adult: Boolean? = null,
  val nextEpisodeToAir: Any? = null,
  val inProduction: Boolean? = null,
  val lastAirDate: String? = null,
  val homepage: String? = null,
  val status: String? = null
)
