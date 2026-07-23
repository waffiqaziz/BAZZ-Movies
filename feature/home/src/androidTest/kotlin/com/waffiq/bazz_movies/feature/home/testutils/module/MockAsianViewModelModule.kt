package com.waffiq.bazz_movies.feature.home.testutils.module

import com.waffiq.bazz_movies.feature.home.ui.viewmodel.AsianViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.mockk.mockk
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MockAsianViewModelModule {

  @Provides
  @Singleton
  fun provideMockAsianViewModel(): AsianViewModel = mockk(relaxed = true)
}
