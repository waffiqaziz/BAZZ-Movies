package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.keywords

interface KeywordsResponse {
  val id: Int?
  val keywords: List<MediaKeywordsResponseItem?>?
}
