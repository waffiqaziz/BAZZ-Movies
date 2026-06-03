package com.waffiq.bazz_movies.core.database.data.room

import android.content.Context
import android.database.sqlite.SQLiteException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.database.testdummy.DummyData.favoriteMovieEntity
import com.waffiq.bazz_movies.core.database.testdummy.DummyData.favoriteTvEntity
import com.waffiq.bazz_movies.core.database.testdummy.DummyData.watchlistMovieEntity
import com.waffiq.bazz_movies.core.database.testdummy.DummyData.watchlistTvEntity
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

// https://developer.android.com/training/data-storage/room/testing-db
@RunWith(RobolectricTestRunner::class)
class FavoriteDaoTest {

  private lateinit var database: FavoriteDatabase
  private lateinit var favoriteDao: FavoriteDao

  @Before
  fun setup() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    // use in-memory database for testing
    database = Room.inMemoryDatabaseBuilder(
      context,
      FavoriteDatabase::class.java,
    ).allowMainThreadQueries() // allow for testing on main thread
      .build()

    favoriteDao = database.favoriteDao()
  }

  @After
  fun tearDown() {
    database.close()
  }

  @Test
  fun insertAndGetFavoriteTv_whenSuccessful_returnTvDataCorrectly() =
    runTest {
      favoriteDao.insert(favoriteTvEntity)

      val favorites = favoriteDao.getFavoriteTv().first()
      assertEquals(1, favorites.size)
      assertEquals(favoriteTvEntity.mediaId, favorites[0].mediaId)
    }

  @Test
  fun insertAndGetFavoriteMovies_whenSuccessful_returnMovieDataCorrectly() =
    runTest {
      favoriteDao.insert(favoriteMovieEntity)

      val favorites = favoriteDao.getFavoriteMovies().first()
      assertEquals(1, favorites.size)
      assertEquals(favoriteMovieEntity.mediaId, favorites[0].mediaId)
    }

  @Test
  fun getAllFavorite_whenSuccessful_returnAllDataCorrectly() =
    runTest {
      // use insert all
      favoriteDao.insertAll(listOf(favoriteTvEntity, favoriteMovieEntity))

      val favorites = favoriteDao.getAllFavorites()
      assertEquals(2, favorites.size)
      assertEquals(favoriteTvEntity.copy(id = 1), favorites[0]) // test with generated id
      assertEquals(favoriteMovieEntity.copy(id = 2), favorites[1])
    }

  @Test
  fun clearAndInsert_whenSuccessful_deleteAllAndInsertCorrectly() =
    runTest {
      val data = listOf(favoriteTvEntity, favoriteMovieEntity)
      favoriteDao.insertAll(data)
      favoriteDao.clearAndInsert(data)

      val favorites = favoriteDao.getAllFavorites()
      assertEquals(2, favorites.size)
      assertEquals(favoriteTvEntity.copy(id = 3), favorites[0]) // the id should continue
      assertEquals(favoriteMovieEntity.copy(id = 4), favorites[1])
    }

  @Test
  fun getWatchlistMovies_whenSuccessful_returnDataCorrectly() =
    runTest(UnconfinedTestDispatcher()) {
      favoriteDao.insert(watchlistMovieEntity)

      favoriteDao.getWatchlistMovies().test {
        val firstEmission = awaitItem()
        assertEquals(1, firstEmission.size)
        assertEquals(watchlistMovieEntity.mediaId, firstEmission[0].mediaId)

        // check multiple insert
        favoriteDao.insert(
          watchlistMovieEntity.copy(
            mediaId = 2345,
            isWatchlist = true,
            isFavorite = false,
          ),
        )

        val secondEmission = awaitItem()
        assertEquals(2, secondEmission.size)
        assertEquals(2345, secondEmission[1].mediaId)
      }
    }

  @Test
  fun getWatchlistTv_whenSuccessful_returnDataCorrectly() =
    runTest(UnconfinedTestDispatcher()) {
      favoriteDao.insert(watchlistTvEntity)

      favoriteDao.getWatchlistTv().test {
        val firstEmission = awaitItem()
        assertEquals(1, firstEmission.size)
        assertEquals(watchlistTvEntity.mediaId, firstEmission[0].mediaId)

        favoriteDao.insert(
          favoriteTvEntity.copy(
            mediaId = 444,
            isFavorite = false,
            isWatchlist = true,
          ),
        )

        val secondEmission = awaitItem()
        assertEquals(2, secondEmission.size)
        assertEquals(444, secondEmission[1].mediaId)
      }
    }

  @Test
  fun insertOrUpdate_whenInsertSucceeds_insertsWithoutUpdating() =
    runTest {
      favoriteDao.insertOrUpdate(favoriteTvEntity)

      val favorites = favoriteDao.getAllFavorites()
      assertEquals(1, favorites.size)
      assertEquals(favoriteTvEntity.copy(id = 1), favorites[0])
    }

  @Test
  fun insertOrUpdate_whenInsertSuccessThenExistingFound_updatesCorrectly() =
    runTest {
      // only insert
      favoriteDao.insertOrUpdate(favoriteTvEntity)

      // perform updated
      val updated = favoriteTvEntity.copy(title = "Updated Title")
      favoriteDao.insertOrUpdate(updated)

      val favorites = favoriteDao.getAllFavorites()
      assertEquals(1, favorites.size)
      assertEquals(updated.copy(id = 1), favorites[0]) // should same id which mean same item
    }

  // race condition,item deleted between insert conflict and getByMedia
  @Test
  fun insertOrUpdate_whenRaceCondition_existingDeletedAfterConflict_doesNothing() =
    runTest {
      val mockDao = mockk<FavoriteDao>()

      coEvery { mockDao.insert(any()) } returns -1L // conflict detected
      coEvery { mockDao.getByMedia(any(), any()) } returns null // but now it's gone
      coEvery { mockDao.insertOrUpdate(any()) } coAnswers { callOriginal() }

      // should silently do nothing, not throw
      mockDao.insertOrUpdate(favoriteTvEntity)
    }

  // DB corrupti, item exists but update affects 0 rows
  @Test
  fun insertOrUpdate_whenDbCorruption_updateAffectsNoRows_throwsSQLiteException() =
    runTest {
      val mockDao = mockk<FavoriteDao>()

      coEvery { mockDao.insert(any()) } returns -1L // conflict detected
      coEvery { mockDao.getByMedia(any(), any()) } returns favoriteTvEntity.copy(id = 99)
      coEvery { mockDao.update(any()) } returns 0 // but update silently failed
      coEvery { mockDao.insertOrUpdate(any()) } coAnswers { callOriginal() }

      assertThrows(SQLiteException::class.java) {
        runBlocking { mockDao.insertOrUpdate(favoriteTvEntity) }
      }
    }

  @Test
  fun getByMedia_whenSuccessful_returnsFavorite() =
    runTest {
      favoriteDao.insert(favoriteTvEntity)

      val result = favoriteDao.getByMedia(103, TV_MEDIA_TYPE)
      assertTrue(result?.isFavorite == true)
    }

  @Test
  fun deleteItem_whenSuccessful_removesFromDatabase() =
    runTest {
      favoriteDao.insert(favoriteTvEntity)

      val deleteCount = favoriteDao.deleteItem(103, TV_MEDIA_TYPE)
      assertEquals(1, deleteCount)

      val favorites = favoriteDao.getFavoriteTv().first()
      assertEquals(0, favorites.size)
    }

  @Test
  fun deleteAll_whenSuccessful_removesAllItems() =
    runTest {
      favoriteDao.insert(favoriteTvEntity)
      favoriteDao.insert(favoriteMovieEntity)

      // check if the item is inserted
      val favoritesTv = favoriteDao.getFavoriteTv().first()
      assertEquals(1, favoritesTv.size)

      val favoritesMovie = favoriteDao.getFavoriteMovies().first()
      assertEquals(1, favoritesMovie.size)

      val deleteCount = favoriteDao.deleteAll()

      assertEquals(2, deleteCount)
      assertEquals(0, favoriteDao.getFavoriteTv().first().size)
      assertEquals(0, favoriteDao.getFavoriteMovies().first().size)
    }

  @Test
  fun update_whenSuccessful_changesValues() =
    runTest {
      val result = favoriteDao.insert(favoriteTvEntity)

      val updateCount = favoriteDao.update(
        favoriteTvEntity.copy(id = result.toInt(), isFavorite = true, isWatchlist = true),
      )

      assertEquals(1, updateCount)
      val media = favoriteDao.getByMedia(103, TV_MEDIA_TYPE)
      assertTrue(media?.isFavorite == true)
      assertTrue(media?.isWatchlist == true)
    }

  @Test
  fun update_differentValue_changesValues() =
    runTest {
      val result = favoriteDao.insert(
        favoriteTvEntity.copy(isFavorite = false, isWatchlist = true),
      )

      val updateCount = favoriteDao.update(
        favoriteTvEntity.copy(id = result.toInt(), isFavorite = false, isWatchlist = false),
      )

      assertEquals(1, updateCount)
      val media = favoriteDao.getByMedia(favoriteTvEntity.mediaId, TV_MEDIA_TYPE)
      assertFalse(media?.isFavorite == true)
      assertFalse(media?.isWatchlist == true)
    }

  @Test
  fun insert_whenSuccessful_ignoresOnConflict() =
    runTest {
      val original = favoriteMovieEntity.copy(id = 1, title = "Original Title")
      val duplicate = favoriteMovieEntity.copy(id = 1, title = "Updated Title")

      val firstInsert = favoriteDao.insert(original)
      val secondInsert = favoriteDao.insert(duplicate) // Should be ignored

      assertEquals(1, firstInsert)
      assertEquals(-1, secondInsert) // -1 indicates insert was ignored

      val movies = favoriteDao.getFavoriteMovies().first()
      assertEquals(1, movies.size)
      assertEquals("Original Title", movies[0].title)
      assertEquals(true, movies[0].isFavorite)
    }
}
