package com.waffiq.bazz_movies.core.database.di

import com.waffiq.bazz_movies.core.database.BuildConfig
import com.waffiq.bazz_movies.core.database.di.AppModule.provideAppVersion
import org.junit.Assert.assertEquals
import org.junit.Test

class AppVersionModuleTest {

  @Test
  fun provideAppVersion_initialized_returnsAppVersion() {
    assertEquals(BuildConfig.APP_VERSION, provideAppVersion())
  }
}
