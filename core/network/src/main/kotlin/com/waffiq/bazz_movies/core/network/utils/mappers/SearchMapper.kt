package com.waffiq.bazz_movies.core.network.utils.mappers

import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.search.MultiSearchResponseItem

object SearchMapper {

  fun MultiSearchResponseItem.withMediaType(mediaType: String): MultiSearchResponseItem =
    copy(mediaType = mediaType)
}
