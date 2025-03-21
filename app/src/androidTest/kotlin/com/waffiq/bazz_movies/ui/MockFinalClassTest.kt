package com.waffiq.bazz_movies.ui

import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * This class is a helper to ensure mockk can run properly
 */
@RunWith(AndroidJUnit4::class)
class MockFinalClassTest {

  internal class FinalClass {
    val method: String = "original"
  }

  @Test
  fun testMockingFinalClass() {
    val mockFinal = mockk<FinalClass>()
    every { mockFinal.method } returns "mocked"
    assertEquals("mocked", mockFinal.method)
  }
}
