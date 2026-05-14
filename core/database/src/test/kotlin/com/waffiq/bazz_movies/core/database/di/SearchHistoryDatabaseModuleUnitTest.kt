package com.waffiq.bazz_movies.core.database.di

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.database.data.room.SearchHistoryDatabase
import com.waffiq.bazz_movies.core.database.utils.Constants.SEARCH_HISTORY_TABLE_NAME
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SearchHistoryDatabaseModuleUnitTest {

  private lateinit var context: Context

  private val databaseModule = SearchHistoryDatabaseModule()
  private val testDatabaseName = "$SEARCH_HISTORY_TABLE_NAME.db"

  @Before
  fun setup() {
    context = ApplicationProvider.getApplicationContext()
  }

  @Test
  fun provideDatabase_withMigration_returnsValidDatabaseAndDao() {
    val database = databaseModule.provideDatabase(context)
    assertNotNull("Database should not be null", database)

    val dao = database.searchHistoryDao()
    assertNotNull("DAO should not be null", dao)

    database.openHelper.writableDatabase

    val dbFile = context.getDatabasePath("$testDatabaseName.db")
    println("Database file exists: ${dbFile.exists()}")
  }

  @Test
  fun provideSearchHistoryDao_whenSuccessful_returnsValidDao() {
    val database = Room.databaseBuilder(
      context,
      SearchHistoryDatabase::class.java,
      testDatabaseName,
    ).build()

    val dao = databaseModule.provideSearchHistoryDao(database)
    assertNotNull(dao)

    // clean up
    database.close()
    context.deleteDatabase(testDatabaseName)
  }
}
