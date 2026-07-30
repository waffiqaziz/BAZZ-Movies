package com.waffiq.bazz_movies.feature.favorite.testutils.module

import com.waffiq.bazz_movies.core.favoritewatchlist.ui.viewmodel.BaseViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MockBaseViewModelModule {

  @Provides
  @Singleton
  fun provideMockBaseViewModel(): BaseViewModel =
    mockk<BaseViewModel>(relaxed = true).apply {
      every { markSnackbarShown() } just Runs
      every { resetSnackbarShown() } just Runs
    }
}
