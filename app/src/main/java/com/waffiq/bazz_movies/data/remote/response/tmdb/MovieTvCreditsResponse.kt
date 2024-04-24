package com.waffiq.bazz_movies.data.remote.response.tmdb

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = false)
data class MovieTvCreditsResponse(

	@Json(name="cast")
	val cast: List<CastItem>,

	@Json(name="id")
	val id: Int? = null,

	@Json(name="crew")
	val crew: List<CrewItem>
)

@Parcelize
@JsonClass(generateAdapter = false)
data class CastItem(

	@Json(name="cast_id")
	val castId: Int? = null,

	@Json(name="character")
	val character: String? = null,

	@Json(name="gender")
	val gender: Int? = null,

	@Json(name="credit_id")
	val creditId: String? = null,

	@Json(name="known_for_department")
	val knownForDepartment: String? = null,

	@Json(name="original_name")
	val originalName: String? = null,

	@Json(name="popularity")
	val popularity: Double? = null,

	@Json(name="name")
	val name: String? = null,

	@Json(name="profile_path")
	val profilePath: String? = null,

	@Json(name="id")
	val id: Int? = null,

	@Json(name="adult")
	val adult: Boolean? = null,

	@Json(name="order")
	val order: Int? = null
): Parcelable

@JsonClass(generateAdapter = false)
data class CrewItem(

	@Json(name="gender")
	val gender: Int? = null,

	@Json(name="credit_id")
	val creditId: String? = null,

	@Json(name="known_for_department")
	val knownForDepartment: String? = null,

	@Json(name="original_name")
	val originalName: String? = null,

	@Json(name="popularity")
	val popularity: Double? = null,

	@Json(name="name")
	val name: String? = null,

	@Json(name="profile_path")
	val profilePath: String? = null,

	@Json(name="id")
	val id: Int? = null,

	@Json(name="adult")
	val adult: Boolean? = null,

	@Json(name="department")
	val department: String? = null,

	@Json(name="job")
	val job: String? = null
)
