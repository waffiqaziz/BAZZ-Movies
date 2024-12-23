package com.waffiq.bazz_movies.core.test

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

// Updated rule for new coroutine testing API
class MainCoroutineRule(
  val dispatcher: TestDispatcher = StandardTestDispatcher(),
  val scope: TestScope = TestScope(dispatcher)
) : TestRule {
  override fun apply(base: Statement, description: Description?): Statement {
    return object : Statement() {
      override fun evaluate() {
        Dispatchers.setMain(dispatcher)
        try {
          base.evaluate()
        } finally {
          Dispatchers.resetMain()
        }
      }
    }
  }
}

