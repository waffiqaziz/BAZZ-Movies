package com.waffiq.bazz_movies.data.remote.response

import com.google.gson.annotations.SerializedName
import com.waffiq.bazz_movies.data.model.Genre

data class GenresResponse(

  @field:SerializedName("genres")
  val genres: List<Genre>
)
