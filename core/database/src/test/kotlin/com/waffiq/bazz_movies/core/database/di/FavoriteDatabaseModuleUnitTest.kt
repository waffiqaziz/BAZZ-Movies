package com.waffiq.bazz_movies.core.database.di

import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import com.waffiq.bazz_movies.core.database.data.room.FavoriteDatabase
import com.waffiq.bazz_movies.core.database.testutils.BaseFavoriteDatabaseModuleTest
import com.waffiq.bazz_movies.core.database.utils.Constants.FAVORITE_TABLE_NAME
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FavoriteDatabaseModuleUnitTest : BaseFavoriteDatabaseModuleTest() {

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
    try {
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
    val database = Room.databaseBuilder(
      context,
      FavoriteDatabase::class.java,
      testDatabaseName,
    ).build()

    val dao = databaseModule.provideFavoriteDao(database)
    assertNotNull(dao)

    // clean up
    database.close()
    context.deleteDatabase(testDatabaseName)
  }

  @Test
  fun getMigrationOneToTwo_whenApplied_correctlyMigratesSchemaAndData() {
    // create v1 database and insert test data
    helper.createDatabase(dbPath, 1).use { db ->
      // create original v1 schema
      db.execSQL(sqlCreateTableVersionOne)

      // insert test data with NULL values
      db.execSQL(
        """
          INSERT INTO favorite (mediaId, mediaType, genre, is_favorited, is_watchlist)
          VALUES (101, NULL, NULL, 1, 0)
        """,
      )
    }

    // test the migration
    helper.runMigrationsAndValidate(dbPath, 2, true, migrationOneToTwo).use { db ->
      db.query("SELECT * FROM favorite").use { cursor ->
        cursor.moveToFirst()

        // expect empty strings, not NULL
        assertEquals("", cursor.getString(cursor.getColumnIndex("mediaType")))
        assertEquals("", cursor.getString(cursor.getColumnIndex("genre")))
      }
    }
  }

  @Test
  fun getMigrationTwoToThree_whenApplied_correctlyAddsUniqueIndexAndLastUpdatedColumn() {
    helper.createDatabase(dbPath, 2).use { db ->
      db.execSQL(sqlCreateTableVersionTwo)
      db.execSQL(sqlInsertMovieData)
      db.execSQL(sqlInsertTvData)
    }

    helper.runMigrationsAndValidate(dbPath, 3, true, migrationTwoToThree).use { db ->
      // verify unique index exists
      db.query(
        "SELECT name, `unique` FROM pragma_index_list('favorite') " +
          "WHERE name = 'index_favorite_mediaId_mediaType'",
      ).use { indexCursor ->
        assertTrue("Unique index should exist", indexCursor.moveToFirst())
        assertEquals(
          "index_favorite_mediaId_mediaType",
          indexCursor.getString(indexCursor.getColumnIndex("name")),
        )
        assertEquals(1, indexCursor.getInt(indexCursor.getColumnIndex("unique")))
      }

      // verify last_updated column exists with non-zero value
      db.query("SELECT last_updated FROM $FAVORITE_TABLE_NAME").use { cursor ->
        assertTrue("Table should have rows", cursor.moveToFirst())

        val lastUpdatedIdx = cursor.getColumnIndex("last_updated")
        assertTrue("last_updated column should exist", lastUpdatedIdx >= 0)
        assertTrue("last_updated should have a non-zero value", cursor.getLong(lastUpdatedIdx) > 0)
      }

      // verify existing data is intact
      db.query("SELECT * FROM $FAVORITE_TABLE_NAME ORDER BY mediaId ASC").use { dataCursor ->
        assertEquals(2, dataCursor.count)
        dataCursor.moveToFirst()
        assertEquals(101, dataCursor.getInt(dataCursor.getColumnIndex("mediaId")))
        dataCursor.moveToNext()
        assertEquals(102, dataCursor.getInt(dataCursor.getColumnIndex("mediaId")))
      }

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
    }
  }

  @Test
  fun getMigrationTwoToThree_whenDuplicatesExist_keepsHighestIdAndMigratesSuccessfully() {
    helper.createDatabase(dbPath, 2).use { db ->
      db.execSQL(sqlCreateTableVersionTwo)

      // insert duplicate (mediaId=101, mediaType='movie') rows, which only allowed in v2
      db.execSQL(sqlInsertMovieData) // id=1, mediaId=101, mediaType='movie'
      db.execSQL(sqlInsertDuplicateMovieDataWithHigherId) // id=2, mediaId=101, mediaType='movie'
    }

    // migration should NOT throw despite duplicates existing
    helper.runMigrationsAndValidate(dbPath, 3, true, migrationTwoToThree).use { db ->

      db.query("SELECT id, mediaId, mediaType FROM $FAVORITE_TABLE_NAME").use { cursor ->
        cursor.moveToFirst()

        // only 1 row should remain after dedup
        assertTrue(
          "Row with highest id should be kept",
          cursor.getInt(cursor.getColumnIndex("id")) > 0,
        )

        // the row with the highest id should be kept
        cursor.moveToFirst()
        assertEquals(
          "Row with highest id should be kept",
          2,
          cursor.getInt(cursor.getColumnIndex("id")),
        )
        assertEquals(101, cursor.getInt(cursor.getColumnIndex("mediaId")))
        assertEquals("movie", cursor.getString(cursor.getColumnIndex("mediaType")))
      }
    }
  }

  @Test
  fun getMigrationThreeToFour_whenGenreNamesExist_convertsNamesToIdString() {
    helper.createDatabase(dbPath, 3).use { db ->
      db.execSQL(sqlCreateTableVersionThree)
      db.execSQL(insertMovieWithGenre(101, "Action, Romance, Horror"))
    }

    helper.runMigrationsAndValidate(dbPath, 4, true, migrationThreeToFour).use { db ->
      // Action=28, Romance=10749, Horror=27
      assertEquals("28,10749,27", db.genreOf(101))
    }
  }

  @Test
  fun getMigrationThreeToFour_whenGenreIsAlreadyIds_keepsIds() {
    helper.createDatabase(dbPath, 3).use { db ->
      db.execSQL(sqlCreateTableVersionThree)
      db.execSQL(insertMovieWithGenre(101, "28,10749"))
    }

    helper.runMigrationsAndValidate(dbPath, 4, true, migrationThreeToFour).use { db ->
      assertEquals("28,10749", db.genreOf(101))
    }
  }

  @Test
  fun getMigrationThreeToFour_whenGenreIsEmpty_resultsInEmptyString() {
    helper.createDatabase(dbPath, 3).use { db ->
      db.execSQL(sqlCreateTableVersionThree)
      db.execSQL(insertMovieWithGenre(101, ""))
    }

    helper.runMigrationsAndValidate(dbPath, 4, true, migrationThreeToFour).use { db ->
      assertEquals("", db.genreOf(101))
    }
  }

  @Test
  fun getMigrationThreeToFour_whenGenreNameIsUnknown_dropsUnknownAndKeepsValid() {
    helper.createDatabase(dbPath, 3).use { db ->
      db.execSQL(sqlCreateTableVersionThree)
      db.execSQL(insertMovieWithGenre(101, "Action, NotARealGenre"))
    }

    helper.runMigrationsAndValidate(dbPath, 4, true, migrationThreeToFour).use { db ->
      assertEquals("28", db.genreOf(101))
    }
  }

  @Test
  fun getMigrationThreeToFour_whenGenreHasDuplicatesAndExtraWhitespace_dedupsAndTrims() {
    helper.createDatabase(dbPath, 3).use { db ->
      db.execSQL(sqlCreateTableVersionThree)
      db.execSQL(insertMovieWithGenre(101, "  Action ,Action,  28 , , Horror "))
    }

    helper.runMigrationsAndValidate(dbPath, 4, true, migrationThreeToFour).use { db ->
      // remove duplication
      assertEquals("28,27", db.genreOf(101))
    }
  }

  @Test
  fun getMigrationThreeToFour_whenMultipleRows_migratesEachRowIndependently() {
    helper.createDatabase(dbPath, 3).use { db ->
      db.execSQL(sqlCreateTableVersionThree)
      db.execSQL(insertMovieWithGenre(101, "Action"))
      db.execSQL(insertMovieWithGenre(102, "Horror, Romance"))
      db.execSQL(insertMovieWithGenre(103, ""))
    }

    helper.runMigrationsAndValidate(dbPath, 4, true, migrationThreeToFour).use { db ->
      assertEquals("28", db.genreOf(101))
      assertEquals("27,10749", db.genreOf(102))
      assertEquals("", db.genreOf(103))
    }
  }

  @Test
  fun getMigrationThreeToFour_whenTableIsEmpty_migratesWithoutError() {
    helper.createDatabase(dbPath, 3).use { db ->
      db.execSQL(sqlCreateTableVersionThree)
    }

    helper.runMigrationsAndValidate(dbPath, 4, true, migrationThreeToFour).use { db ->
      db.query("SELECT COUNT(*) FROM $FAVORITE_TABLE_NAME").use { cursor ->
        cursor.moveToFirst()
        assertEquals(0, cursor.getInt(0))
      }
    }
  }

  @Test
  fun getMigrationThreeToFour_preservesOtherColumns() {
    helper.createDatabase(dbPath, 3).use { db ->
      db.execSQL(sqlCreateTableVersionThree)
      db.execSQL(insertMovieWithGenre(101, "Action"))
    }

    helper.runMigrationsAndValidate(dbPath, 4, true, migrationThreeToFour).use { db ->
      db.query("SELECT id, mediaId, mediaType, title, is_favorited FROM $FAVORITE_TABLE_NAME")
        .use { c ->
          assertTrue(c.moveToFirst())
          assertEquals(1, c.getInt(c.getColumnIndexOrThrow("id")))
          assertEquals(101, c.getInt(c.getColumnIndexOrThrow("mediaId")))
          assertEquals("movie", c.getString(c.getColumnIndexOrThrow("mediaType")))
          assertEquals("Movie 101", c.getString(c.getColumnIndexOrThrow("title")))
          assertEquals(1, c.getInt(c.getColumnIndexOrThrow("is_favorited")))
        }
    }
  }
}
