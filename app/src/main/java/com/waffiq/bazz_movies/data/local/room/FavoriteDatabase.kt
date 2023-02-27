package com.waffiq.bazz_movies.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.waffiq.bazz_movies.data.local.model.FavoriteDB

@Database(
  entities = [FavoriteDB::class],
  version = 1,
  exportSchema = false
)
abstract class FavoriteDatabase : RoomDatabase() {

  abstract fun favoriteDao(): FavoriteDao

  companion object {
    @Volatile
    private var INSTANCE: FavoriteDatabase? = null

    @JvmStatic
    fun getInstance(context: Context): FavoriteDatabase {
      return INSTANCE ?: synchronized(this) {
        INSTANCE ?: Room.databaseBuilder(
          context.applicationContext,
          FavoriteDatabase::class.java, "favorite.db"
        )
          .fallbackToDestructiveMigration()
          .build()
          .also { INSTANCE = it }
      }
    }
  }
}