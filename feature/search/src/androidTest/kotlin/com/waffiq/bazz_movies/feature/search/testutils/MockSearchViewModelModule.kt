package com.waffiq.bazz_movies.feature.search.testutils

import com.waffiq.bazz_movies.feature.search.ui.viewmodel.SearchViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.mockk.mockk
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MockSearchViewModelModule {

  @Provides
  @Singleton
  fun provideMockSearchViewModel(): SearchViewModel = mockk(relaxed = true)
}
