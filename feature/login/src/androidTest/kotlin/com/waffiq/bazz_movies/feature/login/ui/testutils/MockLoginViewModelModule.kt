package com.waffiq.bazz_movies.feature.login.ui.testutils

import com.waffiq.bazz_movies.feature.login.ui.LoginViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.mockk.mockk
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MockLoginViewModelModule {

  @Provides
  @Singleton
  fun provideMockloginViewModel(): LoginViewModel = mockk(relaxed = true)
}
