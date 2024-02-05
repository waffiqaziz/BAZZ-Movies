package com.waffiq.bazz_movies.data.remote.response.tmdb

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailPersonResponse(

	@field:SerializedName("also_known_as")
	val alsoKnownAs: List<String?>? = null,

	@field:SerializedName("birthday")
	val birthday: String? = null,

	@field:SerializedName("gender")
	val gender: Int? = null,

	@field:SerializedName("imdb_id")
	val imdbId: String? = null,

	@field:SerializedName("known_for_department")
	val knownForDepartment: String? = null,

	@field:SerializedName("profile_path")
	val profilePath: String? = null,

	@field:SerializedName("biography")
	val biography: String? = null,

	@field:SerializedName("deathday")
	val deathday: String? = null,

	@field:SerializedName("place_of_birth")
	val placeOfBirth: String? = null,

	@field:SerializedName("popularity")
	val popularity: Float? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("adult")
	val adult: Boolean? = null,

	@field:SerializedName("homepage")
	val homepage: String? = null
): Parcelable

data class ImagePersonResponse(

	@field:SerializedName("profiles")
	val profiles: List<ProfilesItem>? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

data class ProfilesItem(

	@field:SerializedName("aspect_ratio")
	val aspectRatio: Any? = null,

	@field:SerializedName("file_path")
	val filePath: String? = null,

	@field:SerializedName("vote_average")
	val voteAverage: Float? = null,

	@field:SerializedName("width")
	val width: Int? = null,

	@field:SerializedName("iso_639_1")
	val iso6391: Any? = null,

	@field:SerializedName("vote_count")
	val voteCount: Int? = null,

	@field:SerializedName("height")
	val height: Int? = null
)

data class CombinedCreditResponse(

	@field:SerializedName("cast")
	val cast: List<CastCombinedItem>? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("crew")
	val crew: List<CrewCombinedItem>? = null
)

data class CrewCombinedItem(

	@field:SerializedName("overview")
	val overview: String? = null,

	@field:SerializedName("original_language")
	val originalLanguage: String? = null,

	@field:SerializedName("original_title")
	val originalTitle: String? = null,

	@field:SerializedName("video")
	val video: Boolean? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("genre_ids")
	val genreIds: List<Int?>? = null,

	@field:SerializedName("poster_path")
	val posterPath: String? = null,

	@field:SerializedName("backdrop_path")
	val backdropPath: String? = null,

	@field:SerializedName("release_date")
	val releaseDate: String? = null,

	@field:SerializedName("credit_id")
	val creditId: String? = null,

	@field:SerializedName("media_type")
	val mediaType: String? = null,

	@field:SerializedName("popularity")
	val popularity: Float? = null,

	@field:SerializedName("vote_average")
	val voteAverage: Float? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("adult")
	val adult: Boolean? = null,

	@field:SerializedName("department")
	val department: String? = null,

	@field:SerializedName("job")
	val job: String? = null,

	@field:SerializedName("vote_count")
	val voteCount: Int? = null
)

data class CastCombinedItem(

	@field:SerializedName("first_air_date")
	val firstAirDate: String? = null,

	@field:SerializedName("overview")
	val overview: String? = null,

	@field:SerializedName("original_language")
	val originalLanguage: String? = null,

	@field:SerializedName("episode_count")
	val episodeCount: Int? = null,

	@field:SerializedName("genre_ids")
	val genreIds: List<Int>? = null,

	@field:SerializedName("poster_path")
	val posterPath: String? = null,

	@field:SerializedName("origin_country")
	val originCountry: List<String>? = null,

	@field:SerializedName("backdrop_path")
	val backdropPath: String? = null,

	@field:SerializedName("character")
	val character: String? = null,

	@field:SerializedName("credit_id")
	val creditId: String? = null,

	@field:SerializedName("media_type")
	val mediaType: String? = null,

	@field:SerializedName("original_name")
	val originalName: String? = null,

	@field:SerializedName("popularity")
	val popularity: Double? = null,

	@field:SerializedName("vote_average")
	val voteAverage: Float? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("adult")
	val adult: Boolean? = null,

	@field:SerializedName("vote_count")
	val voteCount: Int? = null,

	@field:SerializedName("original_title")
	val originalTitle: String? = null,

	@field:SerializedName("video")
	val video: Boolean? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("release_date")
	val releaseDate: String? = null,

	@field:SerializedName("order")
	val order: Int? = null
)
