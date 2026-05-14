package com.waffiq.bazz_movies.core.database.utils

import com.waffiq.bazz_movies.core.database.data.model.SearchHistoryEntity
import com.waffiq.bazz_movies.core.models.SearchHistory

object SearchHistoryMapper {

  fun SearchHistory.toSearchHistoryEntity() =
    SearchHistoryEntity(
      id = id,
      query = query,
      createdAt = createdAt,
    )

  fun SearchHistoryEntity.toSearchHistory() =
    SearchHistory(
      id = id,
      query = query,
      createdAt = createdAt,
    )
}
