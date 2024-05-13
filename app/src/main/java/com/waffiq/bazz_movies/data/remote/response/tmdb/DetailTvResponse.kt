package com.waffiq.bazz_movies.data.remote.response.tmdb

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class DetailTvResponse(

	@Json(name="original_language")
	val originalLanguage: String? = null,

	@Json(name="number_of_episodes")
	val numberOfEpisodes: Int? = null,

	@Json(name="networks")
	val networks: List<NetworksItem?>? = null,

	@Json(name="type")
	val type: String? = null,

	@Json(name="backdrop_path")
	val backdropPath: String? = null,

	@Json(name="genres")
	val genres: List<GenresItem?>? = null,

	@Json(name="popularity")
	val popularity: Double? = null,

	@Json(name="production_countries")
	val productionCountries: List<ProductionCountriesItem?>? = null,

	@Json(name="id")
	val id: Int? = null,

	@Json(name="number_of_seasons")
	val numberOfSeasons: Int? = null,

	@Json(name="vote_count")
	val voteCount: Int? = null,

	@Json(name="first_air_date")
	val firstAirDate: String? = null,

	@Json(name="overview")
	val overview: String? = null,

	@Json(name="seasons")
	val seasons: List<SeasonsItem?>? = null,

	@Json(name="languages")
	val languages: List<String?>? = null,

	@Json(name="created_by")
	val createdBy: List<CreatedByItem?>? = null,

	@Json(name="last_episode_to_air")
	val lastEpisodeToAir: LastEpisodeToAir? = null,

	@Json(name="poster_path")
	val posterPath: String? = null,

	@Json(name="origin_country")
	val originCountry: List<String?>? = null,

	@Json(name="spoken_languages")
	val spokenLanguages: List<SpokenLanguagesItem?>? = null,

	@Json(name="production_companies")
	val productionCompanies: List<ProductionCompaniesItem?>? = null,

	@Json(name="original_name")
	val originalName: String? = null,

	@Json(name="vote_average")
	val voteAverage: Double? = null,

	@Json(name="name")
	val name: String? = null,

	@Json(name="tagline")
	val tagline: String? = null,

	@Json(name="episode_run_time")
	val episodeRunTime: List<Int?>? = null,

	@Json(name="content_ratings")
	val contentRatings: ContentRatings? = null,

	@Json(name="adult")
	val adult: Boolean? = null,

	@Json(name="next_episode_to_air")
	val nextEpisodeToAir: Any? = null,

	@Json(name="in_production")
	val inProduction: Boolean? = null,

	@Json(name="last_air_date")
	val lastAirDate: String? = null,

	@Json(name="homepage")
	val homepage: String? = null,

	@Json(name="status")
	val status: String? = null
)

@JsonClass(generateAdapter = false)
data class ContentRatings(

	@Json(name="results")
	val results: List<ContentRatingsItem?>? = null
)

@JsonClass(generateAdapter = false)
data class ContentRatingsItem(

	@Json(name="descriptors")
	val descriptors: List<Any?>? = null,

	@Json(name="iso_3166_1")
	val iso31661: String? = null,

	@Json(name="rating")
	val rating: String? = null
)

@JsonClass(generateAdapter = false)
data class SeasonsItem(

	@Json(name="air_date")
	val airDate: String? = null,

	@Json(name="overview")
	val overview: String? = null,

	@Json(name="episode_count")
	val episodeCount: Int? = null,

	@Json(name="name")
	val name: String? = null,

	@Json(name="season_number")
	val seasonNumber: Int? = null,

	@Json(name="id")
	val id: Int? = null,

	@Json(name="poster_path")
	val posterPath: String? = null
)

@JsonClass(generateAdapter = false)
data class CreatedByItem(

	@Json(name="gender")
	val gender: Int? = null,

	@Json(name="credit_id")
	val creditId: String? = null,

	@Json(name="name")
	val name: String? = null,

	@Json(name="profile_path")
	val profilePath: String? = null,

	@Json(name="id")
	val id: Int? = null
)

@JsonClass(generateAdapter = false)
data class GenresItem(

	@Json(name="name")
	val name: String? = null,

	@Json(name="id")
	val id: Int? = null
)

@JsonClass(generateAdapter = false)
data class ProductionCompaniesItem(

	@Json(name="logo_path")
	val logoPath: Any? = null,

	@Json(name="name")
	val name: String? = null,

	@Json(name="id")
	val id: Int? = null,

	@Json(name="origin_country")
	val originCountry: String? = null
)

@JsonClass(generateAdapter = false)
data class SpokenLanguagesItem(

	@Json(name="name")
	val name: String? = null,

	@Json(name="iso_639_1")
	val iso6391: String? = null,

	@Json(name="english_name")
	val englishName: String? = null
)

@JsonClass(generateAdapter = false)
data class LastEpisodeToAir(

	@Json(name="production_code")
	val productionCode: String? = null,

	@Json(name="air_date")
	val airDate: String? = null,

	@Json(name="overview")
	val overview: String? = null,

	@Json(name="episode_number")
	val episodeNumber: Int? = null,

	@Json(name="show_id")
	val showId: Int? = null,

	@Json(name="vote_average")
	val voteAverage: Double? = null,

	@Json(name="name")
	val name: String? = null,

	@Json(name="season_number")
	val seasonNumber: Int? = null,

	@Json(name="runtime")
	val runtime: Int? = null,

	@Json(name="id")
	val id: Int? = null,

	@Json(name="still_path")
	val stillPath: String? = null,

	@Json(name="vote_count")
	val voteCount: Int? = null
)

@JsonClass(generateAdapter = false)
data class NetworksItem(

	@Json(name="logo_path")
	val logoPath: String? = null,

	@Json(name="name")
	val name: String? = null,

	@Json(name="id")
	val id: Int? = null,

	@Json(name="origin_country")
	val originCountry: String? = null
)

@JsonClass(generateAdapter = false)
data class ExternalIdResponse(

	@Json(name="imdb_id")
	val imdbId: String? = null,

	@Json(name="freebase_mid")
	val freebaseMid: String? = null,

	@Json(name="tvdb_id")
	val tvdbId: Int? = null,

	@Json(name="freebase_id")
	val freebaseId: String? = null,

	@Json(name="id")
	val id: Int? = null,

	@Json(name="twitter_id")
	val twitterId: String? = null,

	@Json(name="tvrage_id")
	val tvrageId: Int? = null,

	@Json(name="facebook_id")
	val facebookId: String? = null,

	@Json(name="instagram_id")
	val instagramId: String? = null
)