package com.waffiq.bazz_movies.data.remote.response.tmdb

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = false)
data class DetailPersonResponse(

	@Json(name="also_known_as")
	val alsoKnownAs: List<String?>? = null,

	@Json(name="birthday")
	val birthday: String? = null,

	@Json(name="gender")
	val gender: Int? = null,

	@Json(name="imdb_id")
	val imdbId: String? = null,

	@Json(name="known_for_department")
	val knownForDepartment: String? = null,

	@Json(name="profile_path")
	val profilePath: String? = null,

	@Json(name="biography")
	val biography: String? = null,

	@Json(name="deathday")
	val deathday: String? = null,

	@Json(name="place_of_birth")
	val placeOfBirth: String? = null,

	@Json(name="popularity")
	val popularity: Float? = null,

	@Json(name="name")
	val name: String? = null,

	@Json(name="id")
	val id: Int? = null,

	@Json(name="adult")
	val adult: Boolean? = null,

	@Json(name="homepage")
	val homepage: String? = null
): Parcelable

@JsonClass(generateAdapter = false)
data class ImagePersonResponse(

	@Json(name="profiles")
	val profiles: List<ProfilesItemResponse>? = null,

	@Json(name="id")
	val id: Int? = null
)

@JsonClass(generateAdapter = false)
data class ProfilesItemResponse(

	@Json(name="aspect_ratio")
	val aspectRatio: Any? = null,

	@Json(name="file_path")
	val filePath: String? = null,

	@Json(name="vote_average")
	val voteAverage: Float? = null,

	@Json(name="width")
	val width: Int? = null,

	@Json(name="iso_639_1")
	val iso6391: Any? = null,

	@Json(name="vote_count")
	val voteCount: Int? = null,

	@Json(name="height")
	val height: Int? = null
)

@JsonClass(generateAdapter = false)
data class CombinedCreditResponse(

	@Json(name="cast")
	val cast: List<CastItemResponse>? = null,

	@Json(name="id")
	val id: Int? = null,

	@Json(name="crew")
	val crew: List<CrewItemResponse>? = null
)

@JsonClass(generateAdapter = false)
data class CrewItemResponse(

	@Json(name="overview")
	val overview: String? = null,

	@Json(name="original_language")
	val originalLanguage: String? = null,

	@Json(name="original_title")
	val originalTitle: String? = null,

	@Json(name="video")
	val video: Boolean? = null,

	@Json(name="title")
	val title: String? = null,

	@Json(name="genre_ids")
	val genreIds: List<Int?>? = null,

	@Json(name="poster_path")
	val posterPath: String? = null,

	@Json(name="backdrop_path")
	val backdropPath: String? = null,

	@Json(name="release_date")
	val releaseDate: String? = null,

	@Json(name="credit_id")
	val creditId: String? = null,

	@Json(name="media_type")
	val mediaType: String? = null,

	@Json(name="popularity")
	val popularity: Float? = null,

	@Json(name="vote_average")
	val voteAverage: Float? = null,

	@Json(name="id")
	val id: Int? = null,

	@Json(name="adult")
	val adult: Boolean? = null,

	@Json(name="department")
	val department: String? = null,

	@Json(name="job")
	val job: String? = null,

	@Json(name="vote_count")
	val voteCount: Int? = null
)

@JsonClass(generateAdapter = false)
data class CastItemResponse(

	@Json(name="first_air_date")
	val firstAirDate: String? = null,

	@Json(name="overview")
	val overview: String? = null,

	@Json(name="original_language")
	val originalLanguage: String? = null,

	@Json(name="episode_count")
	val episodeCount: Int? = null,

	@Json(name="genre_ids")
	val genreIds: List<Int>? = null,

	@Json(name="poster_path")
	val posterPath: String? = null,

	@Json(name="origin_country")
	val originCountry: List<String>? = null,

	@Json(name="backdrop_path")
	val backdropPath: String? = null,

	@Json(name="character")
	val character: String? = null,

	@Json(name="credit_id")
	val creditId: String? = null,

	@Json(name="media_type")
	val mediaType: String? = null,

	@Json(name="original_name")
	val originalName: String? = null,

	@Json(name="popularity")
	val popularity: Double? = null,

	@Json(name="vote_average")
	val voteAverage: Float? = null,

	@Json(name="name")
	val name: String? = null,

	@Json(name="id")
	val id: Int? = null,

	@Json(name="adult")
	val adult: Boolean? = null,

	@Json(name="vote_count")
	val voteCount: Int? = null,

	@Json(name="original_title")
	val originalTitle: String? = null,

	@Json(name="video")
	val video: Boolean? = null,

	@Json(name="title")
	val title: String? = null,

	@Json(name="release_date")
	val releaseDate: String? = null,

	@Json(name="order")
	val order: Int? = null
)

@JsonClass(generateAdapter = false)
data class ExternalIDPersonResponse(

	@Json(name="imdb_id")
	val imdbId: String? = null,

	@Json(name="freebase_mid")
	val freebaseMid: String? = null,

	@Json(name="tiktok_id")
	val tiktokId: String? = null,

	@Json(name="wikidata_id")
	val wikidataId: String? = null,

	@Json(name="id")
	val id: Int? = null,

	@Json(name="freebase_id")
	val freebaseId: String? = null,

	@Json(name="twitter_id")
	val twitterId: String? = null,

	@Json(name="youtube_id")
	val youtubeId: String? = null,

	@Json(name="tvrage_id")
	val tvrageId: String? = null,

	@Json(name="facebook_id")
	val facebookId: String? = null,

	@Json(name="instagram_id")
	val instagramId: String? = null
)
