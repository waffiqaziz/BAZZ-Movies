package com.waffiq.bazz_movies.core.database.testutils

import android.content.Context
import androidx.room.migration.Migration
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.waffiq.bazz_movies.core.database.data.room.FavoriteDao
import com.waffiq.bazz_movies.core.database.data.room.FavoriteDatabase
import com.waffiq.bazz_movies.core.database.di.FavoriteDatabaseModule
import com.waffiq.bazz_movies.core.database.utils.Constants.FAVORITE_TABLE_NAME
import org.junit.After
import org.junit.Before

abstract class BaseFavoriteDatabaseModuleTest {

  protected val testDatabaseName = "favorite.db"
  protected lateinit var context: Context
  protected lateinit var dbPath: String
  protected lateinit var helper: MigrationTestHelper
  protected lateinit var databaseModule: FavoriteDatabaseModule
  protected lateinit var database: FavoriteDatabase
  protected lateinit var dao: FavoriteDao

  protected lateinit var migrationOneToTwo: Migration
  protected lateinit var migrationTwoToThree: Migration
  protected lateinit var migrationThreeToFour: Migration

  protected val sqlCreateTableVersionOne =
    """
      CREATE TABLE IF NOT EXISTS favorite (
        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
        mediaId INTEGER NOT NULL,
        mediaType TEXT,
        genre TEXT,
        backDrop TEXT,
        poster TEXT,
        overview TEXT,
        title TEXT,
        releaseDate TEXT,
        popularity REAL,
        rating REAL,
        is_favorited INTEGER,
        is_watchlist INTEGER
      )
    """.trimIndent()

  protected val sqlCreateTableVersionTwo =
    """
      CREATE TABLE IF NOT EXISTS favorite (
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

  protected val sqlCreateTableVersionThree =
    """
      CREATE TABLE IF NOT EXISTS $FAVORITE_TABLE_NAME (
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
        is_watchlist INTEGER NOT NULL,
        last_updated INTEGER NOT NULL DEFAULT 0
      )
    """.trimIndent()

  protected val sqlInsertMovieData =
    """
      INSERT INTO favorite (mediaId, mediaType, genre, backDrop, poster, overview, title, 
      releaseDate, popularity, rating, is_favorited, is_watchlist)
      VALUES (101, 'movie', 'Action', '', '', '', 'Movie A', '2024-01-01', 7.5, 8.0, 1, 0)
    """.trimIndent()

  protected val sqlInsertTvData =
    """
      INSERT INTO favorite (mediaId, mediaType, genre, backDrop, poster, overview, title, 
      releaseDate, popularity, rating, is_favorited, is_watchlist)
      VALUES (102, 'tv', 'Drama', '', '', '', 'TV Show A', '2024-02-01', 6.5, 7.0, 0, 1)
    """.trimIndent()

  protected val sqlInsertDuplicateMovieDataWithHigherId =
    """
        INSERT INTO $FAVORITE_TABLE_NAME 
        (mediaId, mediaType, genre, backDrop, poster, overview, title, releaseDate, popularity, rating, is_favorited, is_watchlist)
        VALUES (101, 'movie', 'Action', '', '', '', 'Duplicate Movie', '2024-01-01', 7.5, 8.0, 1, 0)
    """.trimIndent()

  protected fun insertMovieWithGenre(mediaId: Int, genre: String) =
    """
      INSERT INTO $FAVORITE_TABLE_NAME 
      (mediaId, mediaType, genre, backDrop, poster, overview, title, releaseDate, popularity, rating, is_favorited, is_watchlist, last_updated)
      VALUES ($mediaId, 'movie', '$genre', '', '', '', 'Movie $mediaId', '2024-01-01', 7.5, 8.0, 1, 0, ${System.currentTimeMillis()})
    """.trimIndent()

  protected fun SupportSQLiteDatabase.genreOf(mediaId: Int): String =
    query("SELECT genre FROM $FAVORITE_TABLE_NAME WHERE mediaId = $mediaId").use { c ->
      c.moveToFirst()
      c.getString(c.getColumnIndexOrThrow("genre"))
    }

  @Before
  fun setup() {
    context = ApplicationProvider.getApplicationContext()

    // get the path where the database should be stored
    dbPath = context.getDatabasePath(testDatabaseName).path

    helper = MigrationTestHelper(
      InstrumentationRegistry.getInstrumentation(),
      FavoriteDatabase::class.java,
      emptyList(), // no auto-migrations
      FrameworkSQLiteOpenHelperFactory(),
    )

    databaseModule = FavoriteDatabaseModule()
    database = databaseModule.provideDatabase(context)
    dao = database.favoriteDao()

    migrationOneToTwo = databaseModule.getMigrationOneToTwo()
    migrationTwoToThree = databaseModule.getMigrationTwoToThree()
    migrationThreeToFour = databaseModule.getMigrationThreeToFour()
  }

  @After
  fun tearDown() {
    if (::database.isInitialized) database.close()
  }
}
