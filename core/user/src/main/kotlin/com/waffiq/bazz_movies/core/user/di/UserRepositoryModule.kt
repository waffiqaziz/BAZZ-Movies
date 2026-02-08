package com.waffiq.bazz_movies.core.user.di

import com.waffiq.bazz_movies.core.user.data.repository.UserRepositoryImpl
import com.waffiq.bazz_movies.core.user.domain.repository.IUserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("Unused")
@Module
@InstallIn(SingletonComponent::class)
fun interface UserRepositoryModule {

  @Binds
  fun bindUserRepository(userRepository: UserRepositoryImpl): IUserRepository
}
