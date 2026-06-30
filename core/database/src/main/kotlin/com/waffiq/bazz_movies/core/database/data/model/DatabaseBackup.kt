package com.waffiq.bazz_movies.core.database.data.model

import kotlinx.serialization.Serializable

/**
 * Represents the data written to a backup file.
 *
 * [checksum] is nullable to permits manually edited backup files,
 * where the original checksum is no longer valid.
 */
@Serializable
data class DatabaseBackup(
  val version: Int,
  val createdAt: Long,
  val appVersion: String,
  val favorites: List<FavoriteBackupEntry>,
  val checksum: String? = null,
) {
  companion object {
    const val BACKUP_VERSION = 1
  }
}
