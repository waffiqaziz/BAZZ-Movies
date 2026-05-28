package com.waffiq.bazz_movies.core.database.domain.usecase

import android.net.Uri
import com.waffiq.bazz_movies.core.database.domain.repository.IDatabaseBackupRepository
import com.waffiq.bazz_movies.core.database.utils.DbResult
import javax.inject.Inject

class DatabaseBackupInteractor @Inject constructor(
  private val databaseBackupRepository: IDatabaseBackupRepository,
) : DatabaseBackupUseCase {

  override suspend fun backupDatabase(destinationUri: Uri): DbResult<Unit> =
    databaseBackupRepository.backupDatabase(destinationUri)

  override suspend fun restoreDatabase(sourceUri: Uri): DbResult<Unit> =
    databaseBackupRepository.restoreDatabase(sourceUri)
}
