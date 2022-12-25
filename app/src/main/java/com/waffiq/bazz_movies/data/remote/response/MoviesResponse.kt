package com.waffiq.bazz_movies.data.remote.response

import com.google.gson.annotations.SerializedName
import com.waffiq.bazz_movies.data.local.model.Movie

data class MoviesResponse(
  @SerializedName("page")
  val page: Int,

  @SerializedName("results")
  val results: List<Movie>,

  @SerializedName("total_pages")
  val totalPages: Int,

  @SerializedName("total_results")
  val totalResults: Int
)