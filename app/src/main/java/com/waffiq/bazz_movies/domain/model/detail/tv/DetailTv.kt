package com.waffiq.bazz_movies.domain.model.detail.tv

import com.waffiq.bazz_movies.data.remote.responses.tmdb.detail_movie_tv.tv.ContentRatingsResponse
import com.waffiq.bazz_movies.domain.model.detail.GenresItem
import com.waffiq.bazz_movies.domain.model.detail.ProductionCompaniesItem
import com.waffiq.bazz_movies.domain.model.detail.ProductionCountriesItem
import com.waffiq.bazz_movies.domain.model.detail.SpokenLanguagesItem

data class DetailTv(
  val originalLanguage: String? = null,
  val numberOfEpisodes: Int? = null,
  val listNetworksItem: List<NetworksItem?>? = null,
  val type: String? = null,
  val backdropPath: String? = null,
  val listGenres: List<GenresItem>? = null,
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
  val contentRatingsResponse: ContentRatingsResponse? = null,
  val adult: Boolean? = null,
  val nextEpisodeToAir: Any? = null,
  val inProduction: Boolean? = null,
  val lastAirDate: String? = null,
  val homepage: String? = null,
  val status: String? = null
)