package com.waffiq.bazz_movies.core.test

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class LifecycleOwnerRule : TestWatcher() {

  val lifecycleOwner: LifecycleOwner = object : LifecycleOwner {
    override val lifecycle: Lifecycle get() = lifecycleRegistry
  }

  val lifecycleRegistry by lazy { LifecycleRegistry(lifecycleOwner) }

  override fun starting(description: Description?) {
    lifecycleRegistry.currentState = Lifecycle.State.CREATED
  }

  override fun finished(description: Description?) {
    lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
  }
}
