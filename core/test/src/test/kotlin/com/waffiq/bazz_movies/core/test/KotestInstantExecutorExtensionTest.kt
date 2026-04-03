package com.waffiq.bazz_movies.core.test

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import io.kotest.core.spec.style.FunSpec
import org.junit.Assert.assertTrue

class KotestInstantExecutorExtensionTest : FunSpec({

  extensions(KotestInstantExecutorExtension)

  test("postToMainThread executes runnable synchronously") {
    var executed = false
    ArchTaskExecutor.getInstance().postToMainThread { executed = true }
    assertTrue(executed)
  }

  test("executeOnDiskIO executes runnable synchronously") {
    var executed = false
    ArchTaskExecutor.getInstance().executeOnDiskIO { executed = true }
    assertTrue(executed)
  }

  test("isMainThread returns true while delegate is set") {
    assertTrue(ArchTaskExecutor.getInstance().isMainThread)
  }

  test("postToMainThread executes synchronously (explicit runnable)") {
    var executed = false
    val runnable = Runnable { executed = true }

    ArchTaskExecutor.getInstance().postToMainThread(runnable)

    assertTrue(executed)
  }

  test("executeOnDiskIO executes synchronously (explicit runnable)") {
    var executed = false
    val runnable = Runnable { executed = true }

    ArchTaskExecutor.getInstance().executeOnDiskIO(runnable)

    assertTrue(executed)
  }
})
