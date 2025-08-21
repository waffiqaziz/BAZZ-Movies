package com.waffiq.bazz_movies.feature.favorite.testutils

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import io.kotest.core.listeners.AfterSpecListener
import io.kotest.core.listeners.BeforeSpecListener
import io.kotest.core.spec.Spec

object InstantExecutorExtension : BeforeSpecListener, AfterSpecListener {

  override suspend fun beforeSpec(spec: Spec) {
    ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
      override fun executeOnDiskIO(runnable: Runnable) = runnable.run()
      override fun postToMainThread(runnable: Runnable) = runnable.run()
      override fun isMainThread(): Boolean = true
    })
  }

  override suspend fun afterSpec(spec: Spec) {
    ArchTaskExecutor.getInstance().setDelegate(null)
  }
}
