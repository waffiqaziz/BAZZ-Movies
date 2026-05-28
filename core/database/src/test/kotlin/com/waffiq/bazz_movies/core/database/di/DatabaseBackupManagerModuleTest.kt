package com.waffiq.bazz_movies.core.database.di

import android.content.Context
import com.waffiq.bazz_movies.core.database.data.room.FavoriteDao
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertNotNull
import org.junit.Test

class DatabaseBackupManagerModuleTest {

  private val context: Context = mockk()
  private val mockFavoriteDao: FavoriteDao = mockk()
  private val testDispatcher: Dispatchers = mockk()
  private val module = DatabaseBackupManagerModule()

  @Test
  fun provideDatabaseBackupManager_whenInitialize_returnsDatabaseBackupManager() {
    val manager = module.provideDatabaseBackupManager(
      context,
      mockFavoriteDao,
      "app version",
      testDispatcher.IO,
    )
    assertNotNull(manager)
  }
}
