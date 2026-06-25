package com.waffiq.bazz_movies.core.database.data.manager

import android.content.Context
import android.net.Uri
import android.util.Log
import com.waffiq.bazz_movies.core.coroutines.IoDispatcher
import com.waffiq.bazz_movies.core.database.data.model.BackupPayload
import com.waffiq.bazz_movies.core.database.data.model.DatabaseBackup
import com.waffiq.bazz_movies.core.database.data.room.FavoriteDao
import com.waffiq.bazz_movies.core.database.di.AppVersion
import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.database.utils.FavoriteMapper.toBackupEntry
import com.waffiq.bazz_movies.core.database.utils.FavoriteMapper.toEntity
import com.waffiq.bazz_movies.core.database.utils.sha256
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

/**
 * Handles backup and restore favorites/watchlist database
 *
 * Backup flow:
 * 1. Read the database
 * 2. Build [BackupPayload] (everything except the checksum)
 * 3. Hash the payload JSON, then store as checksum
 * 4. Write the full [DatabaseBackup] (payload + checksum) to the file as JSON
 *
 * Restore flow:
 * 1. Read and parse the backup JSON file into a [DatabaseBackup]
 * 2. Check the backup format version is one we support
 * 3. Verify the checksum. If it doesn't match, the backup marks as corrupted or edited
 * 4. Filter out any invalid entries, then replace the database contents
 */
class DatabaseBackupManager @Inject constructor(
  @ApplicationContext private val context: Context,
  private val favoriteDao: FavoriteDao,
  @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
  @AppVersion private val appVersion: String,
) {
  // Used for writing the final backup file and reading it back.
  // prettyPrint = true so the file is human-readable if someone opens it manually.
  // ignoreUnknownKeys = true so older app versions don't crash when reading a backup
  // that was created by a newer app version with extra fields.
  private val json = Json {
    ignoreUnknownKeys = true
    prettyPrint = true
  }

  // Used only for checksum computation, must produce compact, deterministic JSON
  // (no extra whitespace) so the hash is consistent across write and read.
  private val jsonCompact = Json { ignoreUnknownKeys = true }

  companion object {
    private const val TAG = "DatabaseBackupManager"
  }

  suspend fun backupToUri(destinationUri: Uri): DbResult<Unit> =
    withContext(ioDispatcher) {
      runCatching {
        val entities = favoriteDao.getAllFavorites()

        // build payload (all data fields, no checksum)
        val payload = BackupPayload(
          version = DatabaseBackup.BACKUP_VERSION,
          createdAt = System.currentTimeMillis(),
          appVersion = appVersion,
          favorites = entities.map { it.toBackupEntry() },
        )

        // hash compact JSON of the payload, then attach it to the final backup
        val backup = DatabaseBackup(
          version = payload.version,
          createdAt = payload.createdAt,
          appVersion = payload.appVersion,
          favorites = payload.favorites,
          checksum = jsonCompact.encodeToString(payload).sha256(),
        )

        // write backup (payload + checksum) to the file
        context.contentResolver.openOutputStream(destinationUri)
          ?.use { output -> output.write(json.encodeToString(backup).toByteArray(Charsets.UTF_8)) }
          ?: return@runCatching DbResult.Error("Cannot open output stream")

        DbResult.Success(Unit)
      }.getOrElse {
        Log.e(TAG, "Backup failed", it)
        DbResult.Error(it.message ?: "Backup failed")
      }
    }

  suspend fun restoreFromUri(sourceUri: Uri): DbResult<Unit> =
    withContext(ioDispatcher) {
      runCatching {
        // read data from json backup file
        val backup = parseBackup(readJson(sourceUri))

        // validate
        validateVersion(backup.version)
        validateChecksum(backup)

        val validEntries = backup.favorites.filter { it.isValid() }
        if (validEntries.isEmpty()) {
          return@runCatching DbResult.Error("Backup contains no valid entries")
        }

        // wipe the current database and replace it with the backup contents
        favoriteDao.clearAndInsert(validEntries.map { it.toEntity() })

        DbResult.Success(Unit)
      }.getOrElse {
        Log.e(TAG, "Restore failed", it)
        DbResult.Error(it.message ?: "Restore failed")
      }
    }

  private fun readJson(sourceUri: Uri): String {
    val inputStream = context.contentResolver.openInputStream(sourceUri)
      ?: throw IllegalStateException("Cannot open input stream")

    try {
      return BufferedReader(InputStreamReader(inputStream, Charsets.UTF_8)).readText()
    } finally {
      inputStream.close()
    }
  }

  private fun parseBackup(rawJson: String): DatabaseBackup =
    try {
      json.decodeFromString<DatabaseBackup>(rawJson)
    } catch (e: SerializationException) {
      throw IllegalArgumentException("Invalid backup file: not valid JSON", e)
    }

  private fun validateVersion(version: Int) {
    when {
      version <= 0 ->
        throw IllegalArgumentException("Invalid backup version")

      // If user try restore a backup made by a newer app version,
      // don't restore, it may use a format the app don't understand.
      version > DatabaseBackup.BACKUP_VERSION -> {
        Log.e(TAG, "This backup was created with a newer backup format")
        throw IllegalStateException("Backup is from a newer app version. Please update the app.")
      }
    }
  }

  /**
   * Verifies the backup file hasn't been corrupted or modified.
   *
   * Rebuilds the original [BackupPayload], computes its checksum, and compares
   * it with the stored checksum.
   *
   * If [DatabaseBackup.checksum] is `null`, verification is skipped. This
   * permits manually edited backup files, where the original checksum is no
   * longer valid.
   */
  private fun validateChecksum(backup: DatabaseBackup) {
    val expectedChecksum = backup.checksum ?: return

    val payload = BackupPayload(
      version = backup.version,
      createdAt = backup.createdAt,
      appVersion = backup.appVersion,
      favorites = backup.favorites,
    )

    if (jsonCompact.encodeToString(payload).sha256() != expectedChecksum) {
      throw IllegalArgumentException("Backup file is corrupted or has been modified")
    }
  }
}
