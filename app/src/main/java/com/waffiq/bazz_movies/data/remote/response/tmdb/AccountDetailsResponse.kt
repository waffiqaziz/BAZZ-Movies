package com.waffiq.bazz_movies.data.remote.response.tmdb

import com.google.gson.annotations.SerializedName

data class AccountDetailsResponse(

	@field:SerializedName("include_adult")
	val includeAdult: Boolean? = null,

	@field:SerializedName("iso_3166_1")
	val iso31661: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("avatar")
	val avatar: Avatar? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("iso_639_1")
	val iso6391: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)

data class Avatar(

	@field:SerializedName("tmdb")
	val tmdb: Tmdb? = null,

	@field:SerializedName("gravatar")
	val gravatar: Gravatar? = null
)

data class Gravatar(

	@field:SerializedName("hash")
	val hash: String? = null
)

data class Tmdb(

	@field:SerializedName("avatar_path")
	val avatarPath: Any? = null
)
