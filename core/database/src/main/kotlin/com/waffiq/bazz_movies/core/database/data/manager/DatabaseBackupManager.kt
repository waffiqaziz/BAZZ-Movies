package com.waffiq.bazz_movies.core.database.data.manager

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.gson.GsonBuilder
import com.waffiq.bazz_movies.core.coroutines.IoDispatcher
import com.waffiq.bazz_movies.core.database.data.model.DatabaseBackup
import com.waffiq.bazz_movies.core.database.data.room.FavoriteDao
import com.waffiq.bazz_movies.core.database.di.AppVersion
import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.database.utils.FavoriteMapper.toBackupEntry
import com.waffiq.bazz_movies.core.database.utils.FavoriteMapper.toEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DatabaseBackupManager @Inject constructor(
  @ApplicationContext private val context: Context,
  private val favoriteDao: FavoriteDao,
  @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
  @AppVersion private val appVersion: String,
) {
  private val gson = GsonBuilder().setPrettyPrinting().create()

  companion object {
    private const val CURRENT_BACKUP_VERSION = 1
  }

  suspend fun backupToUri(destinationUri: Uri): DbResult<Unit> =
    withContext(ioDispatcher) {
      runCatching {
        val entities = favoriteDao.getAllFavorites()

        val backup = DatabaseBackup(
          version = CURRENT_BACKUP_VERSION,
          appVersion = appVersion,
          favorites = entities.map { it.toBackupEntry() },
        )

        val json = gson.toJson(backup)

        context.contentResolver.openOutputStream(destinationUri)
          ?.use { output -> output.write(json.toByteArray(Charsets.UTF_8)) }
          ?: return@runCatching DbResult.Error("Cannot open output stream")

        DbResult.Success(Unit)
      }.getOrElse {
        DbResult.Error(it.message ?: "Backup failed")
      }
    }

  suspend fun restoreFromUri(sourceUri: Uri): DbResult<Unit> =
    withContext(ioDispatcher) {
      val result = runCatching {
        val json = readJson(sourceUri)
        val backup = parseBackup(json)

        validateBackup(backup)

        val validEntries = backup.favorites.filter { it.isValid() }
        if (validEntries.isEmpty()) {
          return@runCatching DbResult.Error("Backup contains no valid entries")
        }

        favoriteDao.clearAndInsert(validEntries.map { it.toEntity() })

        DbResult.Success(Unit)
      }

      result.getOrElse {
        DbResult.Error(it.message ?: "Restore failed")
      }
    }

  private fun readJson(sourceUri: Uri): String {
    val inputStream = context.contentResolver.openInputStream(sourceUri)
      ?: throw IllegalStateException("Cannot open input stream")

    return inputStream.use { it.bufferedReader(Charsets.UTF_8).readText() }
  }

  private fun parseBackup(json: String): DatabaseBackup =
    runCatching {
      gson.fromJson(json, DatabaseBackup::class.java)
    }.getOrElse {
      throw IllegalArgumentException("Invalid backup file: not valid JSON")
    }

  private fun validateBackup(backup: DatabaseBackup) {
    when {
      backup.version <= 0 -> {
        throw IllegalArgumentException("Invalid backup version")
      }

      backup.version > CURRENT_BACKUP_VERSION -> {
        Log.e("DatabaseBackupManager", "This backup was created with a newer backup format")
        throw IllegalStateException(
          "Backup is from a newer app version. Please update the app.",
        )
      }
    }
  }
}
