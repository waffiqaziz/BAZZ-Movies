package com.waffiq.bazz_movies.core.test

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class MainDispatcherRuleDefaultTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule() // use default value

  @Test
  fun runTest_whenUsingMainDispatcher_shouldExecuteCoroutineSuccessfully() =
    runTest {
      var executed = false
      withContext(Dispatchers.Main) { executed = true }
      assertTrue(executed)
    }
}
