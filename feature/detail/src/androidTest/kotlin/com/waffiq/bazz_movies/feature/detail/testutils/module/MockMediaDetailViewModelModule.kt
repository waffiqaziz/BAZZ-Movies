package com.waffiq.bazz_movies.feature.detail.testutils.module

import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.MediaDetailViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.mockk.mockk
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MockMediaDetailViewModelModule {

  @Provides
  @Singleton
  fun provideMockMediaDetailViewModel(): MediaDetailViewModel = mockk(relaxed = true)
}
