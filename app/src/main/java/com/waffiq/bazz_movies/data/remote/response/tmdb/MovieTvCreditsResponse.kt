package com.waffiq.bazz_movies.data.remote.response.tmdb

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class MovieTvCreditsResponse(

	@field:SerializedName("cast")
	val cast: List<CastItem>,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("crew")
	val crew: List<CrewItem>
)

@Parcelize
data class CastItem(

	@field:SerializedName("cast_id")
	val castId: Int? = null,

	@field:SerializedName("character")
	val character: String? = null,

	@field:SerializedName("gender")
	val gender: Int? = null,

	@field:SerializedName("credit_id")
	val creditId: String? = null,

	@field:SerializedName("known_for_department")
	val knownForDepartment: String? = null,

	@field:SerializedName("original_name")
	val originalName: String? = null,

	@field:SerializedName("popularity")
	val popularity: Double? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("profile_path")
	val profilePath: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("adult")
	val adult: Boolean? = null,

	@field:SerializedName("order")
	val order: Int? = null
): Parcelable

data class CrewItem(

	@field:SerializedName("gender")
	val gender: Int? = null,

	@field:SerializedName("credit_id")
	val creditId: String? = null,

	@field:SerializedName("known_for_department")
	val knownForDepartment: String? = null,

	@field:SerializedName("original_name")
	val originalName: String? = null,

	@field:SerializedName("popularity")
	val popularity: Double? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("profile_path")
	val profilePath: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("adult")
	val adult: Boolean? = null,

	@field:SerializedName("department")
	val department: String? = null,

	@field:SerializedName("job")
	val job: String? = null
)
