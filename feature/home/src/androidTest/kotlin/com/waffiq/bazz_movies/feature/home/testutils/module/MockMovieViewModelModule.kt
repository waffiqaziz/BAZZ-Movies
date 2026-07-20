package com.waffiq.bazz_movies.feature.home.testutils.module

import com.waffiq.bazz_movies.feature.home.ui.viewmodel.MovieViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.mockk.mockk
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MockMovieViewModelModule {

  @Provides
  @Singleton
  fun provideMockMovieViewModel(): MovieViewModel = mockk(relaxed = true)
}
