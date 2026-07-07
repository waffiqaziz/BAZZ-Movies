package com.waffiq.bazz_movies.feature.favorite.testutils

import com.waffiq.bazz_movies.core.favoritewatchlist.ui.viewmodel.BaseViewModel
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.viewmodel.SharedDBViewModel
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.feature.favorite.ui.viewmodel.FavoriteViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.mockk.mockk
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestModule {

  @Provides
  @Singleton
  fun provideMockNavigator(): INavigator = mockk(relaxed = true)

  @Provides
  @Singleton
  fun provideMockSnackbar(): ISnackbar = mockk(relaxed = true)

  @Provides
  @Singleton
  fun provideMockUserPrefViewModel(): UserPreferenceViewModel = mockk(relaxed = true)

  @Provides
  @Singleton
  fun provideMmckFavoriteViewModel(): FavoriteViewModel = mockk(relaxed = true)

  @Provides
  @Singleton
  fun provideMockSharedDBViewModel(): SharedDBViewModel = mockk(relaxed = true)

  @Provides
  @Singleton
  fun provideMockBaseViewModel(): BaseViewModel = mockk(relaxed = true)
}
