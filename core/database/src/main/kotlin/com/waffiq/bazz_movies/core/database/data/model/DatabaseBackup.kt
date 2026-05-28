package com.waffiq.bazz_movies.core.database.data.model

data class DatabaseBackup(
  val version: Int = BACKUP_VERSION,
  val createdAt: Long = System.currentTimeMillis(),
  val appVersion: String = "",
  val favorites: List<FavoriteBackupEntry>,
) {
  companion object {
    const val BACKUP_VERSION = 1
  }
}
