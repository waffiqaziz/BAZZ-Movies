package com.waffiq.bazz_movies.core.database.testutils

import android.content.Context
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.waffiq.bazz_movies.core.database.data.room.FavoriteDao
import com.waffiq.bazz_movies.core.database.data.room.FavoriteDatabase
import com.waffiq.bazz_movies.core.database.di.FavoriteDatabaseModule
import org.junit.Before

abstract class BaseFavoriteDatabaseModuleTest {

  protected lateinit var context: Context
  protected lateinit var dbPath: String
  protected lateinit var helper: MigrationTestHelper
  protected lateinit var databaseModule: FavoriteDatabaseModule
  protected lateinit var database: FavoriteDatabase
  protected lateinit var dao: FavoriteDao
  protected val testDatabaseName = "favorite.db"

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
  }
}
