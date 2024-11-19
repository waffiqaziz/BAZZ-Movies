package com.waffiq.bazz_movies.core.database.data.room

import androidx.room.RoomDatabase
import com.waffiq.bazz_movies.core.database.data.model.FavoriteEntity

@androidx.room.Database(
  entities = [FavoriteEntity::class],
  version = 2,
  exportSchema = true
)
abstract class FavoriteDatabase : RoomDatabase() {
  abstract fun favoriteDao(): FavoriteDao
}
