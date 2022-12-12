package com.waffiq.bazz_movies.data.remote.response


import com.google.gson.annotations.SerializedName
import com.waffiq.bazz_movies.data.model.Search

data class MultiSearchResponse(
    @SerializedName("page")
    val page: Int,

    @SerializedName("results")
    val searches: List<Search>,

    @SerializedName("total_pages")
    val totalPages: Int,

    @SerializedName("total_results")
    val totalResults: Int
)