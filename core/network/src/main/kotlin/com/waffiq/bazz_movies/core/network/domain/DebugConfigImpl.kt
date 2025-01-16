package com.waffiq.bazz_movies.core.network.domain

import com.waffiq.bazz_movies.core.network.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DebugConfigImpl @Inject constructor(private val isDebug: Boolean = BuildConfig.DEBUG) : IDebugConfig {

  override fun isDebug(): Boolean = isDebug
}
