package com.waffiq.bazz_movies.feature.more.testutils.module

import com.waffiq.bazz_movies.feature.more.ui.viewmodel.MoreLocalViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.mockk.mockk
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MockMoreLocalViewModelModule {

  @Provides
  @Singleton
  fun provideMockMoreLocalViewModel(): MoreLocalViewModel = mockk(relaxed = true)
}
