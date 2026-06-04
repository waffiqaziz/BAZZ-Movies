package com.waffiq.bazz_movies.feature.detail.utils.mappers

import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.keywords.MediaKeywordsResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.keywords.MovieKeywordsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.keywords.TvKeywordsResponse
import com.waffiq.bazz_movies.feature.detail.domain.model.keywords.MediaKeywords
import com.waffiq.bazz_movies.feature.detail.domain.model.keywords.MediaKeywordsItem
import com.waffiq.bazz_movies.feature.detail.domain.model.keywords.ValidKeywordItem

object MediaKeywordsMapper {

  fun TvKeywordsResponse.toMediaKeywords(): MediaKeywords =
    MediaKeywords(
      id = id,
      keywords = keywords?.map { it?.toMediaKeywordsItem() },
    )

  fun MovieKeywordsResponse.toMediaKeywords(): MediaKeywords =
    MediaKeywords(
      id = id,
      keywords = keywords?.map { it?.toMediaKeywordsItem() },
    )

  private fun MediaKeywordsResponseItem.toMediaKeywordsItem(): MediaKeywordsItem =
    MediaKeywordsItem(
      id = id,
      name = name,
    )

  fun MediaKeywordsItem.toValidKeywordOrNull(): ValidKeywordItem? {
    val validId = id
    val validName = name.takeUnless { it.isNullOrEmpty() }

    return if (validId != null && validName != null) {
      ValidKeywordItem(id = validId, name = validName)
    } else {
      null
    }
  }
}
