package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.tv

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.GenresItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.ProductionCountriesItemResponse

@JsonClass(generateAdapter = false)
data class DetailTvResponse(

  @Json(name = "original_language")
  val originalLanguage: String? = null,

  @Json(name = "number_of_episodes")
  val numberOfEpisodes: Int? = null,

  @Json(name = "networks")
  val networksResponse: List<NetworksItemResponse?>? = null,

  @Json(name = "type")
  val type: String? = null,

  @Json(name = "backdrop_path")
  val backdropPath: String? = null,

  @Json(name = "genres")
  val genres: List<GenresItemResponse?>? = null,

  @Json(name = "popularity")
  val popularity: Double? = null,

  @Json(name = "production_countries")
  val productionCountriesResponse: List<ProductionCountriesItemResponse?>? = null,

  @Json(name = "id")
  val id: Int? = null,

  @Json(name = "number_of_seasons")
  val numberOfSeasons: Int? = null,

  @Json(name = "vote_count")
  val voteCount: Int? = null,

  @Json(name = "first_air_date")
  val firstAirDate: String? = null,

  @Json(name = "overview")
  val overview: String? = null,

  @Json(name = "seasons")
  val seasonsResponse: List<SeasonsItemResponse?>? = null,

  @Json(name = "languages")
  val languages: List<String?>? = null,

  @Json(name = "created_by")
  val createdByResponse: List<CreatedByItemResponse?>? = null,

  @Json(name = "last_episode_to_air")
  val lastEpisodeToAirResponse: LastEpisodeToAirResponse? = null,

  @Json(name = "poster_path")
  val posterPath: String? = null,

  @Json(name = "origin_country")
  val originCountry: List<String?>? = null,

  @Json(name = "spoken_languages")
  val spokenLanguagesResponse: List<SpokenLanguagesItemResponse?>? = null,

  @Json(name = "production_companies")
  val productionCompaniesResponse: List<ProductionCompaniesItemResponse?>? = null,

  @Json(name = "original_name")
  val originalName: String? = null,

  @Json(name = "vote_average")
  val voteAverage: Double? = null,

  @Json(name = "name")
  val name: String? = null,

  @Json(name = "tagline")
  val tagline: String? = null,

  @Json(name = "episode_run_time")
  val episodeRunTime: List<Int?>? = null,

  @Json(name = "content_ratings")
  val contentRatingsResponse: ContentRatingsResponse? = null,

  @Json(name = "adult")
  val adult: Boolean? = null,

  @Json(name = "next_episode_to_air")
  val nextEpisodeToAir: Any? = null,

  @Json(name = "in_production")
  val inProduction: Boolean? = null,

  @Json(name = "last_air_date")
  val lastAirDate: String? = null,

  @Json(name = "homepage")
  val homepage: String? = null,

  @Json(name = "status")
  val status: String? = null
)
