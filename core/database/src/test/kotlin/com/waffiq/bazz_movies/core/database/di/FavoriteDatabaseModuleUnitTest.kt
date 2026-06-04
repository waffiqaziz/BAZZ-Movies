package com.waffiq.bazz_movies.core.database.di

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.waffiq.bazz_movies.core.database.data.room.FavoriteDatabase
import com.waffiq.bazz_movies.core.database.utils.Constants.FAVORITE_TABLE_NAME
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FavoriteDatabaseModuleUnitTest {

  private lateinit var context: Context
  private lateinit var dbPath: String
  private lateinit var helper: MigrationTestHelper
  private val testDatabaseName = "favorite.db"

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
  }

  /**
   * Tests the provideDatabase method in DatabaseModule correctly creates and returns a database
   * with migration configuration.
   *
   * This test verifies:
   * 1. The database can be created successfully
   * 2. The database provides functioning DAO objects
   * 3. The database version is at least 1, indicating initialization worked
   */
  @Test
  fun provideDatabase_withMigration_returnsValidDatabaseAndDao() {
    // create an instance
    val favoriteDatabaseModule = FavoriteDatabaseModule()

    // call the method and configure it with the migration
    val database = favoriteDatabaseModule.provideDatabase(context)

    try {
      // verify the database was successfully created
      assertNotNull("Database should not be null", database)

      // expect valid DAO
      val dao = database.favoriteDao()
      assertNotNull("DAO should not be null", dao)

      // perform open database
      database.openHelper.writableDatabase

      // get the path where the database should be stored
      val dbPath = context.getDatabasePath("$testDatabaseName.db")

      println("Database file exists: ${dbPath.exists()}")

      // check the version if the database file exists
      if (dbPath.exists()) {
        // open the SQLite database directly to check its properties
        // "use" extension ensures the database will be closed properly
        SQLiteDatabase.openDatabase(dbPath.toString(), null, SQLiteDatabase.OPEN_READONLY)
          .use { sqliteDb ->
            // verify the database has at least version 1
            // the version might not be 2 yet if no operations triggered the migration
            assertTrue(sqliteDb.version >= 1)
          }
      }
    } finally {
      // cleanup
      database.close()

      // delete the database file to clean up after the test  ensures tests are isolated
      // and don't affect each other
      context.deleteDatabase("$testDatabaseName.db")
    }
  }

  @Test
  fun provideFavoriteDao_whenSuccessful_returnsValidDao() {
    val favoriteDatabaseModule = FavoriteDatabaseModule()
    val database = Room.databaseBuilder(
      context,
      FavoriteDatabase::class.java,
      testDatabaseName,
    ).build()

    val dao = favoriteDatabaseModule.provideFavoriteDao(database)
    assertNotNull(dao)

    // clean up
    database.close()
    context.deleteDatabase(testDatabaseName)
  }

  private val sqlCreateTableVersionOne =
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

  @Test
  fun getMigrationOneToTwo_whenApplied_correctlyMigratesSchemaAndData() {
    // create v1 database and insert test data
    helper.createDatabase(dbPath, 1).apply {
      // create original v1 schema
      execSQL(sqlCreateTableVersionOne)

      // insert test data with NULL values
      execSQL(
        """
          INSERT INTO favorite (mediaId, mediaType, genre, is_favorited, is_watchlist)
          VALUES (101, NULL, NULL, 1, 0)
        """,
      )

      close()
    }

    // get the migration
    val migration = FavoriteDatabaseModule().getMigrationOneToTwo()

    // test the migration
    val db = helper.runMigrationsAndValidate(dbPath, 2, true, migration)

    // query the database to verify migration worked correctly
    val cursor = db.query("SELECT * FROM favorite")
    cursor.moveToFirst()

    // verify columns have correct values (non-null as expected)
    val mediaTypeIdx = cursor.getColumnIndex("mediaType")
    val genreIdx = cursor.getColumnIndex("genre")

    // expect empty strings, not NULL
    assertEquals("", cursor.getString(mediaTypeIdx))
    assertEquals("", cursor.getString(genreIdx))

    cursor.close()
    db.close()
  }

  private val sqlCreateTableVersionTwo =
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

  private val sqlInsertMovieData =
    """
      INSERT INTO favorite (mediaId, mediaType, genre, backDrop, poster, overview, title, 
      releaseDate, popularity, rating, is_favorited, is_watchlist)
      VALUES (101, 'movie', 'Action', '', '', '', 'Movie A', '2024-01-01', 7.5, 8.0, 1, 0)
    """.trimIndent()

  private val sqlInsertTvData =
    """
      INSERT INTO favorite (mediaId, mediaType, genre, backDrop, poster, overview, title, 
      releaseDate, popularity, rating, is_favorited, is_watchlist)
      VALUES (102, 'tv', 'Drama', '', '', '', 'TV Show A', '2024-02-01', 6.5, 7.0, 0, 1)
    """.trimIndent()

  @Test
  fun getMigrationTwoToThree_whenApplied_correctlyAddsUniqueIndexAndLastUpdatedColumn() {
    helper.createDatabase(dbPath, 2).apply {
      execSQL(sqlCreateTableVersionTwo)

      // insert test data
      execSQL(sqlInsertMovieData)
      execSQL(sqlInsertTvData)

      close()
    }

    val migration = FavoriteDatabaseModule().getMigrationTwoToThree()
    val db = helper.runMigrationsAndValidate(dbPath, 3, true, migration)

    // verify unique index exists
    val indexCursor = db.query(
      "SELECT name, `unique` FROM pragma_index_list('favorite') " +
        "WHERE name = 'index_favorite_mediaId_mediaType'",
    )
    assertTrue("Unique index should exist", indexCursor.moveToFirst())
    assertEquals(
      "index_favorite_mediaId_mediaType",
      indexCursor.getString(indexCursor.getColumnIndex("name")),
    )
    assertEquals(1, indexCursor.getInt(indexCursor.getColumnIndex("unique")))
    indexCursor.close()

    // verify last_updated column exists with non-zero value
    val cursor = db.query("SELECT last_updated FROM $FAVORITE_TABLE_NAME")
    assertTrue("Table should have rows", cursor.moveToFirst())
    val lastUpdatedIdx = cursor.getColumnIndex("last_updated")
    assertTrue("last_updated column should exist", lastUpdatedIdx >= 0)
    assertTrue("last_updated should have a non-zero value", cursor.getLong(lastUpdatedIdx) > 0)
    cursor.close()

    // verify existing data is intact
    val dataCursor = db.query("SELECT * FROM $FAVORITE_TABLE_NAME ORDER BY mediaId ASC")
    assertEquals(2, dataCursor.count)
    dataCursor.moveToFirst()
    assertEquals(101, dataCursor.getInt(dataCursor.getColumnIndex("mediaId")))
    dataCursor.moveToNext()
    assertEquals(102, dataCursor.getInt(dataCursor.getColumnIndex("mediaId")))
    dataCursor.close()

    // verify unique constraint is enforced after migration
    try {
      db.execSQL(
        """
          INSERT INTO favorite (mediaId, mediaType, genre, backDrop, poster, overview, title, 
          releaseDate, popularity, rating, is_favorited, is_watchlist, last_updated)
          VALUES (101, 'movie', 'Action', '', '', '', 'Duplicate', '2024-01-01', 7.5, 8.0, 1, 0, 
          ${System.currentTimeMillis()})
        """.trimIndent(),
      )
      fail("Expected unique constraint violation but no exception was thrown")
    } catch (_: android.database.sqlite.SQLiteConstraintException) {
      // expected
    }

    db.close()
  }

  private val sqlInsertDuplicateMovieDataWithHigherId =
    """
        INSERT INTO $FAVORITE_TABLE_NAME 
        (mediaId, mediaType, genre, backDrop, poster, overview, title, releaseDate, popularity, rating, is_favorited, is_watchlist)
        VALUES (101, 'movie', 'Action', '', '', '', 'Duplicate Movie', '2024-01-01', 7.5, 8.0, 1, 0)
    """.trimIndent()

  @Test
  fun getMigrationTwoToThree_whenDuplicatesExist_keepsHighestIdAndMigratesSuccessfully() {
    helper.createDatabase(dbPath, 2).apply {
      execSQL(sqlCreateTableVersionTwo)

      // insert duplicate (mediaId=101, mediaType='movie') rows, which only allowed in v2
      execSQL(sqlInsertMovieData) // id=1, mediaId=101, mediaType='movie'
      execSQL(sqlInsertDuplicateMovieDataWithHigherId) // id=2, mediaId=101, mediaType='movie'

      close()
    }

    val migration = FavoriteDatabaseModule().getMigrationTwoToThree()

    // migration should NOT throw despite duplicates existing
    val db = helper.runMigrationsAndValidate(dbPath, 3, true, migration)

    // only 1 row should remain after dedup
    val cursor = db.query("SELECT id, mediaId, mediaType FROM $FAVORITE_TABLE_NAME")
    cursor.moveToFirst()

    val keptId = cursor.getInt(cursor.getColumnIndex("id"))
    assertTrue("Row with highest id should be kept", keptId > 0)

    // the row with the highest id should be kept
    cursor.moveToFirst()
    assertEquals(
      "Row with highest id should be kept",
      2,
      cursor.getInt(cursor.getColumnIndex("id")),
    )
    assertEquals(101, cursor.getInt(cursor.getColumnIndex("mediaId")))
    assertEquals("movie", cursor.getString(cursor.getColumnIndex("mediaType")))
    cursor.close()

    db.close()
  }
}
