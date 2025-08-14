package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.movie

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.GenresResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.ProductionCountriesResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.releasedates.ReleaseDatesResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.ProductionCompaniesResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.SpokenLanguagesResponseItem

@JsonClass(generateAdapter = false)
data class DetailMovieResponse(

  @Json(name = "original_language")
  val originalLanguage: String? = null,

  @Json(name = "imdb_id")
  val imdbId: String? = null,

  @Json(name = "video")
  val video: Boolean? = null,

  @Json(name = "title")
  val title: String? = null,

  @Json(name = "backdrop_path")
  val backdropPath: String? = null,

  @Json(name = "revenue")
  val revenue: Long? = null,

  @Json(name = "genres")
  val listGenresItemResponse: List<GenresResponseItem?>? = null,

  @Json(name = "popularity")
  val popularity: Double? = null,

  @Json(name = "release_dates")
  val releaseDatesResponse: ReleaseDatesResponse? = null,

  @Json(name = "production_countries")
  val listProductionCountriesItemResponse: List<ProductionCountriesResponseItem?>? = null,

  @Json(name = "id")
  val id: Int? = null,

  @Json(name = "vote_count")
  val voteCount: Int? = null,

  @Json(name = "budget")
  val budget: Int? = null,

  @Json(name = "overview")
  val overview: String? = null,

  @Json(name = "original_title")
  val originalTitle: String? = null,

  @Json(name = "runtime")
  val runtime: Int? = null,

  @Json(name = "poster_path")
  val posterPath: String? = null,

  @Json(name = "spoken_languages")
  val listSpokenLanguagesItemResponse: List<SpokenLanguagesResponseItem?>? = null,

  @Json(name = "production_companies")
  val listProductionCompaniesItemResponse: List<ProductionCompaniesResponseItem?>? = null,

  @Json(name = "release_date")
  val releaseDate: String? = null,

  @Json(name = "vote_average")
  val voteAverage: Double? = null,

  @Json(name = "belongs_to_collection")
  val belongsToCollectionResponse: BelongsToCollectionResponse? = null,

  @Json(name = "tagline")
  val tagline: String? = null,

  @Json(name = "adult")
  val adult: Boolean? = null,

  @Json(name = "homepage")
  val homepage: String? = null,

  @Json(name = "status")
  val status: String? = null
)
