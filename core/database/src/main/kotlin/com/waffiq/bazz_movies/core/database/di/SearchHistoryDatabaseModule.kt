package com.waffiq.bazz_movies.core.database.di

import android.content.Context
import androidx.room.Room
import com.waffiq.bazz_movies.core.database.data.room.SearchHistoryDao
import com.waffiq.bazz_movies.core.database.data.room.SearchHistoryDatabase
import com.waffiq.bazz_movies.core.database.utils.Constants.SEARCH_HISTORY_TABLE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SearchHistoryDatabaseModule {

  @Provides
  @Singleton
  fun provideDatabase(@ApplicationContext context: Context): SearchHistoryDatabase =
    Room.databaseBuilder(
      context,
      SearchHistoryDatabase::class.java,
      "$SEARCH_HISTORY_TABLE_NAME.db",
    ).build()

  @Provides
  fun provideSearchHistoryDao(database: SearchHistoryDatabase): SearchHistoryDao =
    database.searchHistoryDao()
}
