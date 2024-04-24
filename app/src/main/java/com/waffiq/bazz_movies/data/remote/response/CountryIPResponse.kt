package com.waffiq.bazz_movies.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class CountryIPResponse(

	@Json(name="country")
	val country: String? = null,

	@Json(name="ip")
	val ip: String? = null
)
