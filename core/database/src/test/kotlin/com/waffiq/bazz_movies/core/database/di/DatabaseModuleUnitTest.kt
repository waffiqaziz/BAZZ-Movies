package com.waffiq.bazz_movies.core.database.di

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.waffiq.bazz_movies.core.database.data.room.FavoriteDatabase
import com.waffiq.bazz_movies.core.database.utils.Constants.TABLE_NAME
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DatabaseModuleUnitTest {

  private lateinit var context: Context
  private val testDatabaseName = "favorite.db"

  @Before
  fun setup() {
    context = ApplicationProvider.getApplicationContext()
  }

  /**
   * Tests that the provideDatabase method in DatabaseModule correctly creates and returns a database
   * with migration configuration.
   *
   * This test verifies:
   * 1. The database can be created successfully
   * 2. The database provides functioning DAO objects
   * 3. (Optional) The database version is at least 1, indicating initialization worked
   */
  @Test
  fun provideDatabase_withMigration_successProvided() {
    // create an instance
    val databaseModule = DatabaseModule()

    // call the method and configure it with the migration
    val database = databaseModule.provideDatabase(context)

    try {
      // verify the database was successfully created
      assertNotNull( "Database should not be null", database)

      // expect get a valid DAO object from the database
      val dao = database.favoriteDao()
      assertNotNull("DAO should not be null", dao)

      // OPTIONAL DATABASE VERSION CHECK
      // get the path where the database should be stored
      val dbPath = context.getDatabasePath("$TABLE_NAME.db")

      println("Database file exists: ${dbPath.exists()}")

      // check the version if the database file exists
      // *important because Room may create the database lazily
      if (dbPath.exists()) {
        // open the SQLite database directly to check its properties
        // "use" extension ensures the database will be closed properly
        SQLiteDatabase.openDatabase(dbPath.toString(), null, SQLiteDatabase.OPEN_READONLY).use { sqliteDb ->
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
      context.deleteDatabase("$TABLE_NAME.db")
    }
  }

  @Test
  fun provideDatabase_successProvided() {
    val databaseModule = DatabaseModule()
    val database = Room.databaseBuilder(
      context,
      FavoriteDatabase::class.java,
      testDatabaseName
    ).build()

    val dao = databaseModule.provideFavoriteDao(database)
    assertNotNull(dao)

    // clean up
    database.close()
    context.deleteDatabase(testDatabaseName)
  }

  @Test
  fun getMigrationOneToTwo_successMigrated() {
    val helper = MigrationTestHelper(
      InstrumentationRegistry.getInstrumentation(),
      FavoriteDatabase::class.java,
      emptyList(),  // no auto-migrations
      FrameworkSQLiteOpenHelperFactory()
    )

    // create v1 database and insert test data
    helper.createDatabase(testDatabaseName, 1).apply {
      // create original v1 schema
      execSQL(
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
      )

      // insert test data with NULL values
      execSQL(
        """
                INSERT INTO favorite (mediaId, mediaType, genre, is_favorited, is_watchlist)
                VALUES (101, NULL, NULL, 1, 0)
                """
      )

      close()
    }

    // get the migration
    val migration = DatabaseModule().getMigrationOneToTwo()

    // test the migration
    val db = helper.runMigrationsAndValidate(testDatabaseName, 2, true, migration)

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
}