package com.waffiq.bazz_movies.core.network.data.remote.retrofit.adapter

import android.util.Log
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class RatedResponseAdapterLogErrorTest {

  private lateinit var adapter: RatedResponseAdapter

  @Before
  fun setUp() {
    adapter = RatedResponseAdapter()
  }

  @Test
  fun logError_LogsCorrectMessage() {
    mockkStatic(Log::class)
    every { Log.e(any(), any()) } returns 0

    val errorMessage = "Test error message"
    adapter.logError(errorMessage)

    verify { Log.e("RatedResponseAdapter", errorMessage) }
    unmockkStatic(Log::class)
  }
}
