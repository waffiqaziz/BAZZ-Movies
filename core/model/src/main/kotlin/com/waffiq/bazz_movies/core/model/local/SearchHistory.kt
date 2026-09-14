package com.waffiq.bazz_movies.core.model.local

data class SearchHistory(
  val id: Int = 1,
  val query: String,
  val createdAt: Long,
)
