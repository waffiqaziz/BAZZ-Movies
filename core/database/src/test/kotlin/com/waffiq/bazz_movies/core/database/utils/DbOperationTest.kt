package com.waffiq.bazz_movies.core.database.utils

import android.database.sqlite.SQLiteException
import android.util.Log
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class DbOperationTest {

  @Before
  fun setup() {
    mockkStatic(Log::class)
    every { Log.e(any(), any()) } returns 0
  }

  @After
  fun tearDown() {
    unmockkStatic(Log::class)
  }

  @Test
  fun executeDbOperation_operationSucceeds_returnsSuccess() =
    runTest {
      val expectedData = "Query Result"
      val operation = suspend { expectedData }

      val result = executeDbOperation(operation)

      assertTrue(result is DbResult.Success)
      assertEquals(expectedData, (result as DbResult.Success).data)
    }

  @Test
  fun executeDbOperation_sqliteExceptionCaught_returnsDatabaseError() =
    runTest {
      val operation = suspend {
        throw SQLiteException("Table not found")
      }

      val result = executeDbOperation(operation)

      assertTrue(result is DbResult.Error)
      assertEquals("Database error", (result as DbResult.Error).errorMessage)
    }

  @Test
  fun executeDbOperation_genericExceptionCaught_returnsUnknownError() =
    runTest {
      val operation = suspend {
        throw IOException("Network timeout or disk full")
      }

      val result = executeDbOperation(operation)

      assertTrue(result is DbResult.Error)
      assertEquals("Unknown error", (result as DbResult.Error).errorMessage)
    }
}
