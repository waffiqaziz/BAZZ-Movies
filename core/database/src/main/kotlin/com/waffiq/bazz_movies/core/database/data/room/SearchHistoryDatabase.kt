package com.waffiq.bazz_movies.core.database.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.waffiq.bazz_movies.core.database.data.model.SearchHistoryEntity

@Database(
  entities = [SearchHistoryEntity::class],
  version = 1,
  exportSchema = true,
)
abstract class SearchHistoryDatabase : RoomDatabase() {
  abstract fun searchHistoryDao(): SearchHistoryDao
}
