package com.waffiq.bazz_movies.core.database.data.model.v1

import com.waffiq.bazz_movies.core.database.data.model.BackupHeader
import org.junit.Assert.assertEquals
import org.junit.Test

class BackupHeaderTest {

  @Test
  fun backupHeader_getVersion_returnsCorrectly() {
    assertEquals(12, BackupHeader(version = 12).version)
  }
}
