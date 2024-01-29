package com.waffiq.bazz_movies.data.remote.response

import com.google.gson.annotations.SerializedName

data class CountyAPIResponse(

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("ip")
	val ip: String? = null
)
