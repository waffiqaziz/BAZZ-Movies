package com.waffiq.bazz_movies.domain.model.detail

import com.waffiq.bazz_movies.data.remote.response.tmdb.ContentRatings
import com.waffiq.bazz_movies.data.remote.response.tmdb.CreatedByItem
import com.waffiq.bazz_movies.data.remote.response.tmdb.GenresItem
import com.waffiq.bazz_movies.data.remote.response.tmdb.LastEpisodeToAir
import com.waffiq.bazz_movies.data.remote.response.tmdb.NetworksItem
import com.waffiq.bazz_movies.data.remote.response.tmdb.ProductionCompaniesItem
import com.waffiq.bazz_movies.data.remote.response.tmdb.ProductionCountriesItem
import com.waffiq.bazz_movies.data.remote.response.tmdb.SeasonsItem
import com.waffiq.bazz_movies.data.remote.response.tmdb.SpokenLanguagesItem

data class DetailTv(
  val originalLanguage: String? = null,
  val numberOfEpisodes: Int? = null,
  val networks: List<NetworksItem?>? = null,
  val type: String? = null,
  val backdropPath: String? = null,
  val genres: List<GenresItem?>? = null,
  val popularity: Double? = null,
  val productionCountries: List<ProductionCountriesItem?>? = null,
  val id: Int? = null,
  val numberOfSeasons: Int? = null,
  val voteCount: Int? = null,
  val firstAirDate: String? = null,
  val overview: String? = null,
  val seasons: List<SeasonsItem?>? = null,
  val languages: List<String?>? = null,
  val createdBy: List<CreatedByItem?>? = null,
  val lastEpisodeToAir: LastEpisodeToAir? = null,
  val posterPath: String? = null,
  val originCountry: List<String?>? = null,
  val spokenLanguages: List<SpokenLanguagesItem?>? = null,
  val productionCompanies: List<ProductionCompaniesItem?>? = null,
  val originalName: String? = null,
  val voteAverage: Double? = null,
  val name: String? = null,
  val tagline: String? = null,
  val episodeRunTime: List<Int?>? = null,
  val contentRatings: ContentRatings? = null,
  val adult: Boolean? = null,
  val nextEpisodeToAir: Any? = null,
  val inProduction: Boolean? = null,
  val lastAirDate: String? = null,
  val homepage: String? = null,
  val status: String? = null
)
