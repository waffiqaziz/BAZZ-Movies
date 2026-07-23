package com.waffiq.bazz_movies.feature.favorite.testutils.module

import com.waffiq.bazz_movies.feature.favorite.ui.viewmodel.FavoriteViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.mockk.mockk
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MockFavoriteViewModelModule {

  @Provides
  @Singleton
  fun provideMockFavoriteViewModel(): FavoriteViewModel = mockk(relaxed = true)
}
