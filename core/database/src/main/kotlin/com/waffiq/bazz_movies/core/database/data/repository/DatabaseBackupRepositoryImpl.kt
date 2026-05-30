package com.waffiq.bazz_movies.core.database.data.repository

import android.net.Uri
import com.waffiq.bazz_movies.core.database.data.datasource.DatabaseBackupDataSource
import com.waffiq.bazz_movies.core.database.domain.repository.IDatabaseBackupRepository
import com.waffiq.bazz_movies.core.database.utils.DbResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseBackupRepositoryImpl @Inject constructor(
  private val databaseBackupDataSource: DatabaseBackupDataSource,
) : IDatabaseBackupRepository {

  override suspend fun backupDatabase(destinationUri: Uri): DbResult<Unit> =
    databaseBackupDataSource.backupDatabase(destinationUri)

  override suspend fun restoreDatabase(sourceUri: Uri): DbResult<Unit> =
    databaseBackupDataSource.restoreDatabase(sourceUri)
}
