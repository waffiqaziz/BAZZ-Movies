package com.waffiq.bazz_movies.core.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.waffiq.bazz_movies.core.data.local.model.FavoriteEntity

@Database(
  entities = [FavoriteEntity::class],
  version = 2,
  exportSchema = true
)
abstract class FavoriteDatabase : RoomDatabase() {
  abstract fun favoriteDao(): FavoriteDao
}
