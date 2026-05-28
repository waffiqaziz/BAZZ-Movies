package com.waffiq.bazz_movies.core.database.data.datasource

import android.net.Uri
import com.waffiq.bazz_movies.core.database.data.manager.DatabaseBackupManager
import com.waffiq.bazz_movies.core.database.utils.DbResult
import javax.inject.Inject

class DatabaseBackupDataSource @Inject constructor(
  private val backupManager: DatabaseBackupManager,
) : DatabaseBackupDataSourceInterface {

  override suspend fun backupDatabase(destinationUri: Uri): DbResult<Unit> =
    backupManager.backupToUri(destinationUri)

  override suspend fun restoreDatabase(sourceUri: Uri): DbResult<Unit> =
    backupManager.restoreFromUri(sourceUri)
}
