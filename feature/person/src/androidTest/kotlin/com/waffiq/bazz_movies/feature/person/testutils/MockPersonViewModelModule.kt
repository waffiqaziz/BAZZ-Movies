package com.waffiq.bazz_movies.feature.person.testutils

import com.waffiq.bazz_movies.feature.person.ui.PersonViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.mockk.mockk
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MockPersonViewModelModule {

  @Provides
  @Singleton
  fun provideMockPersonViewModel(): PersonViewModel = mockk(relaxed = true)
}
