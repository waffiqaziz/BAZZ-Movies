package com.waffiq.bazz_movies.domain.model.detail

data class Video(
  val id: Int? = null,
  val results: List<VideoItem>
)