package com.waffiq.bazz_movies.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.waffiq.bazz_movies.data.model.Favorite
import com.waffiq.bazz_movies.data.model.Movie

@Database(
  entities = [Favorite::class],
  version = 1,
  exportSchema = false
)
abstract class MovieDatabase : RoomDatabase() {

  companion object {
    @Volatile
    private var INSTANCE: MovieDatabase? = null

    @JvmStatic
    fun getInstance(context: Context): MovieDatabase {
      return INSTANCE ?: synchronized(this) {
        INSTANCE ?: Room.databaseBuilder(
          context.applicationContext,
          MovieDatabase::class.java, "movie.db"
        )
          .fallbackToDestructiveMigration()
          .build()
          .also { INSTANCE = it }
      }
    }
  }
}