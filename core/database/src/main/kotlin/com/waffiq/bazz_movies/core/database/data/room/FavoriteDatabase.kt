package com.waffiq.bazz_movies.core.database.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.waffiq.bazz_movies.core.database.data.model.FavoriteEntity

@Database(
  entities = [FavoriteEntity::class],
  version = 4,
  exportSchema = true,
)
@TypeConverters(GenreIdsConverter::class)
abstract class FavoriteDatabase : RoomDatabase() {
  abstract fun favoriteDao(): FavoriteDao
}
