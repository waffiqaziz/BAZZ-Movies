package com.waffiq.bazz_movies.core.database.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.waffiq.bazz_movies.core.database.data.model.SearchHistoryEntity
import com.waffiq.bazz_movies.core.database.utils.Constants.SEARCH_HISTORY_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {

  @Query(
    """
        SELECT * FROM $SEARCH_HISTORY_TABLE_NAME
        ORDER BY createdAt DESC
    """,
  )
  fun getSearchHistory(): Flow<List<SearchHistoryEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(item: SearchHistoryEntity)

  @Query("DELETE FROM $SEARCH_HISTORY_TABLE_NAME WHERE `query` = :query")
  suspend fun deleteByQuery(query: String): Int

  @Delete
  suspend fun delete(item: SearchHistoryEntity): Int

  @Query("DELETE FROM $SEARCH_HISTORY_TABLE_NAME")
  suspend fun deleteAll(): Int

  @Query(
    """
        DELETE FROM $SEARCH_HISTORY_TABLE_NAME
        WHERE id NOT IN (
            SELECT id FROM $SEARCH_HISTORY_TABLE_NAME
            ORDER BY createdAt DESC
            LIMIT 20
        )
    """,
  )
  suspend fun trimHistory(): Int
}
