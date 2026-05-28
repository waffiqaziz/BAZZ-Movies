package com.waffiq.bazz_movies.core.database.domain.usecase

import android.net.Uri
import com.waffiq.bazz_movies.core.database.utils.DbResult

interface DatabaseBackupUseCase {
  suspend fun backupDatabase(destinationUri: Uri): DbResult<Unit>
  suspend fun restoreDatabase(sourceUri: Uri): DbResult<Unit>
}
