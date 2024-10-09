package com.waffiq.bazz_movies.core.di

import android.content.Context
import com.waffiq.bazz_movies.domain.repository.IMoviesRepository
import com.waffiq.bazz_movies.domain.repository.IUserRepository
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
  modules = [RepositoryModule::class]
)
interface CoreComponent {

  @Component.Factory
  interface Factory {
    fun create(@BindsInstance context: Context): CoreComponent
  }

  fun provideMovieRepository(): IMoviesRepository

  fun provideUserRepository(): IUserRepository
}