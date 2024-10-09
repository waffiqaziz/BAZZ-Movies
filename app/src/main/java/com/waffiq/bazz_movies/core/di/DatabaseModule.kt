package com.waffiq.bazz_movies.core.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.waffiq.bazz_movies.data.local.room.FavoriteDao
import com.waffiq.bazz_movies.data.local.room.FavoriteDatabase
import com.waffiq.bazz_movies.utils.common.Constants.TABLE_NAME
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

  @Singleton
  @Provides
  fun provideDatabase(context: Context): FavoriteDatabase = Room.databaseBuilder(
    context,
    FavoriteDatabase::class.java, "$TABLE_NAME.db"
  ).addMigrations(migrationOneTwo)
    .build()

  // Define the migration from version 1 to version 2
  // update app version from v1.0.7 to v1.0.8 and newer
  private val migrationOneTwo = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
      // Step 1: Create a new table with the correct schema
      db.execSQL(
        """
            CREATE TABLE favorite_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                mediaId INTEGER NOT NULL,
                mediaType TEXT NOT NULL,
                genre TEXT NOT NULL,
                backDrop TEXT NOT NULL,
                poster TEXT NOT NULL,
                overview TEXT NOT NULL,
                title TEXT NOT NULL,
                releaseDate TEXT NOT NULL,
                popularity REAL NOT NULL,
                rating REAL NOT NULL,
                is_favorited INTEGER NOT NULL,
                is_watchlist INTEGER NOT NULL
            )
          """.trimIndent()
      )

      // Step 2: Copy data from the old table to the new table
      db.execSQL(
        """
            INSERT INTO favorite_new (id, mediaId, mediaType, genre, backDrop, poster, overview, title, releaseDate, popularity, rating, is_favorited, is_watchlist)
            SELECT id, 
                   mediaId, 
                   COALESCE(mediaType, '') AS mediaType, 
                   COALESCE(genre, '') AS genre, 
                   COALESCE(backDrop, '') AS backDrop, 
                   COALESCE(poster, '') AS poster, 
                   COALESCE(overview, '') AS overview, 
                   COALESCE(title, '') AS title, 
                   COALESCE(releaseDate, '') AS releaseDate, 
                   COALESCE(popularity, 0.0) AS popularity, 
                   COALESCE(rating, 0.0) AS rating, 
                   COALESCE(is_favorited, 0) AS is_favorited, 
                   COALESCE(is_watchlist, 0) AS is_watchlist
            FROM $TABLE_NAME
          """.trimIndent()
      )

      // Step 3: Drop the old table
      db.execSQL("DROP TABLE $TABLE_NAME")

      // Step 4: Rename the new table to the original table name
      db.execSQL("ALTER TABLE favorite_new RENAME TO $TABLE_NAME")
    }
  }

  @Provides
  fun provideFavoriteDao(database: FavoriteDatabase): FavoriteDao = database.favoriteDao()
}