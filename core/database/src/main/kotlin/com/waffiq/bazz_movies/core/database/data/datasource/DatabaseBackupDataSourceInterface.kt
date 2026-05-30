package com.waffiq.bazz_movies.core.database.data.datasource

import android.net.Uri
import com.waffiq.bazz_movies.core.database.utils.DbResult

interface DatabaseBackupDataSourceInterface {
  suspend fun backupDatabase(destinationUri: Uri): DbResult<Unit>
  suspend fun restoreDatabase(sourceUri: Uri): DbResult<Unit>
}
