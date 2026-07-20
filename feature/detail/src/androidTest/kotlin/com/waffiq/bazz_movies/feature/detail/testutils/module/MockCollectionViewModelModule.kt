package com.waffiq.bazz_movies.feature.detail.testutils.module

import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.CollectionViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.mockk.mockk
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MockCollectionViewModelModule {

  @Provides
  @Singleton
  fun provideMockCollectionViewModel(): CollectionViewModel = mockk(relaxed = true)
}
