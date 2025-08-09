package com.waffiq.bazz_movies.core.user.di

import com.waffiq.bazz_movies.core.user.domain.usecase.getregion.GetRegionUseCase
import com.waffiq.bazz_movies.core.user.domain.usecase.userpreference.UserPrefUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.testing.TestInstallIn
import io.mockk.mockk

@TestInstallIn(
  components = [ViewModelComponent::class],
  replaces = [UserModule::class]
)
@Module
object TestUserModule {

  @Provides
  @ViewModelScoped
  fun provideUserPrefUseCase(): UserPrefUseCase = mockk(relaxed = true)

  @Provides
  @ViewModelScoped
  fun provideGetRegionUseCase(): GetRegionUseCase = mockk(relaxed = true)
}
