package com.waffiq.bazz_movies.core.testmodule

import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.mockk.every
import io.mockk.mockk
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MockSnackbarModule {

  @Provides
  @Singleton
  fun provideMockSnackbar(): ISnackbar =
    mockk<ISnackbar>(relaxed = true).apply {
      every { showSnackbarWarning(any<Event<String>>()) } returns mockk(relaxed = true)
      every { showSnackbarWarning(any<String>()) } returns mockk(relaxed = true)
    }
}
