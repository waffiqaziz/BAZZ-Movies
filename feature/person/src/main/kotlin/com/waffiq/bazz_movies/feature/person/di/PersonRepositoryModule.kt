package com.waffiq.bazz_movies.feature.person.di

import com.waffiq.bazz_movies.feature.person.data.repository.PersonRepositoryImpl
import com.waffiq.bazz_movies.feature.person.domain.repository.IPersonRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
fun interface PersonRepositoryModule {

  @Binds
  fun bindPersonRepository(personRepositoryImpl: PersonRepositoryImpl): IPersonRepository
}
