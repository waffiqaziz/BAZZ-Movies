package com.waffiq.bazz_movies.core.testmodule

import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.mockk.mockk
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MockUserPreferenceViewModelModule {

  @Provides
  @Singleton
  fun provideMockUserPreferenceViewModel(): UserPreferenceViewModel = mockk(relaxed = true)
}
