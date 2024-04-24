package com.waffiq.bazz_movies.data.remote.response.tmdb

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class AccountDetailsResponse(

	@Json(name="include_adult")
	val includeAdult: Boolean? = null,

	@Json(name="iso_3166_1")
	val iso31661: String? = null,

	@Json(name="name")
	val name: String? = null,

	@Json(name="avatar")
	val avatar: Avatar? = null,

	@Json(name="id")
	val id: Int? = null,

	@Json(name="iso_639_1")
	val iso6391: String? = null,

	@Json(name="username")
	val username: String? = null
)

@JsonClass(generateAdapter = false)
data class Avatar(

	@Json(name="tmdb")
	val tmdb: Tmdb? = null,

	@Json(name="gravatar")
	val gravatar: Gravatar? = null
)

@JsonClass(generateAdapter = false)
data class Gravatar(

	@Json(name="hash")
	val hash: String? = null
)

@JsonClass(generateAdapter = false)
data class Tmdb(

	@Json(name="avatar_path")
	val avatarPath: String? = null
)
