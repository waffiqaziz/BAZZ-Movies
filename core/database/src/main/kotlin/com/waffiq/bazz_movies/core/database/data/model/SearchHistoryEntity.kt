package com.waffiq.bazz_movies.core.database.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.waffiq.bazz_movies.core.database.utils.Constants.SEARCH_HISTORY_TABLE_NAME

@Entity(
  tableName = SEARCH_HISTORY_TABLE_NAME,
  indices = [Index(value = ["query"], unique = true)],
)
data class SearchHistoryEntity(

  @PrimaryKey(autoGenerate = true)
  val id: Int = 0,

  val query: String,

  val createdAt: Long = System.currentTimeMillis(),
)
