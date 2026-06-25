package com.waffiq.bazz_movies.core.database.data.model

import kotlinx.serialization.Serializable

/**
 * The data that gets hashed to produce [DatabaseBackup.checksum].
 *
 * This is identical to [DatabaseBackup] but without the [checksum] field, because
 * u can't include a hash of a file inside the file you're hashing. We serialize
 * this object to JSON, hash it, and store the result in [DatabaseBackup.checksum].
 *
 * Important: any change to these fields (rename, reorder, add, remove) will break
 * checksum verification for existing backups.
 */
@Serializable
data class BackupPayload(
  val version: Int,
  val createdAt: Long,
  val appVersion: String,
  val favorites: List<FavoriteBackupEntry>,
)
