package com.waffiq.bazz_movies.feature.favorite.testutils.module

import com.waffiq.bazz_movies.core.favoritewatchlist.ui.viewmodel.SharedDBViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.mockk.mockk
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MockSharedDBViewModelModule {

  @Provides
  @Singleton
  fun provideMockSharedDBViewModel(): SharedDBViewModel = mockk(relaxed = true)
}
