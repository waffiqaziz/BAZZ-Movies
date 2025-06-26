package com.waffiq.bazz_movies.core.database.data.datasource

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDiskIOException
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteFullException
import com.waffiq.bazz_movies.core.database.data.model.FavoriteEntity
import com.waffiq.bazz_movies.core.database.data.room.FavoriteDao
import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LocalDataSourceMockkTest {

  private val testDispatcher = Dispatchers
  private lateinit var localDataSource: LocalDataSource

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @MockK
  private lateinit var favoriteDao: FavoriteDao

  @Before
  fun setUp() {
    MockKAnnotations.init(this, relaxed = true)
    localDataSource = LocalDataSource(favoriteDao, testDispatcher.IO)
  }

  @Test
  fun insert_withValidValue_returnSuccess() = runTest {
    val favorite = FavoriteEntity(
      mediaId = 101,
      mediaType = "movie",
      genre = "Action",
      backDrop = "",
      poster = "",
      overview = "",
      title = "Movie1",
      releaseDate = "",
      popularity = 0.0,
      rating = 0f,
      isFavorite = true,
      isWatchlist = false
    )

    coEvery { favoriteDao.insert(favorite) } returns 1L

    val result = localDataSource.insert(favorite)

    assertTrue(result is DbResult.Success)
    assertEquals(1, (result as DbResult.Success).data)
  }

  @Test
  fun deleteItemFromDB_whenSuccessful_returnSuccess() = runTest {
    coEvery { favoriteDao.deleteItem(101, "movie") } returns 1

    val result = localDataSource.deleteItemFromDB(101, "movie")

    assertTrue(result is DbResult.Success)
    assertEquals(1, (result as DbResult.Success).data)
  }

  @Test
  fun deleteAll_whenSuccessful_returnSuccess() = runTest {
    coEvery { favoriteDao.deleteALl() } returns 1

    val result = localDataSource.deleteAll()

    assertTrue(result is DbResult.Success)
    assertEquals(1, (result as DbResult.Success).data)
  }

  @Test
  fun isFavorite_whenFavoriteExists_returnsTrue() = runTest {
    coEvery { favoriteDao.isFavorite(101, "movie") } returns true

    val result = localDataSource.isFavorite(101, "movie")

    assertTrue(result is DbResult.Success)
    assertEquals(true, (result as DbResult.Success).data)
  }

  @Test
  fun update_whenSuccessful_returnSuccess() = runTest {
    coEvery {
      favoriteDao.update(
        isFavorite = true,
        isWatchlist = false,
        id = 101,
        mediaType = "movie"
      )
    } returns 1

    val result =
      localDataSource.update(isFavorite = true, isWatchlist = false, id = 101, mediaType = "movie")

    assertTrue(result is DbResult.Success)
    assertEquals(1, (result as DbResult.Success).data)
  }

  private fun <T> runDbErrorTest(
    expectedMessage: String,
    mockDaoCall: () -> Unit,
    functionUnderTest: suspend () -> DbResult<T>
  ) = runTest {
    mockDaoCall()

    val result = functionUnderTest()
    assertTrue(result is DbResult.Error)
    assertEquals(expectedMessage, (result as DbResult.Error).errorMessage)
  }

  @Test
  fun insert_whenConstraintViolation_throwsSQLiteConstraintException() = runDbErrorTest(
    "Unique constraint violation",
    { coEvery { favoriteDao.insert(any()) } throws SQLiteConstraintException("Unique constraint failed") }
  ) {
    localDataSource.insert(createFavoriteEntity())
  }

  @Test
  fun deleteItemFromDB_whenDatabaseFull_throwsSQLiteFullException() = runDbErrorTest(
    "Database is full",
    {
      coEvery {
        favoriteDao.deleteItem(any(), any())
      } throws SQLiteFullException("Database is full")
    }
  ) {
    localDataSource.deleteItemFromDB(101, "movie")
  }

  @Test
  fun isFavorite_whenDiskIOIssue_throwsSQLiteDiskIOException() = runDbErrorTest(
    "Disk IO issue",
    {
      coEvery {
        favoriteDao.isFavorite(any(), any())
      } throws SQLiteDiskIOException("Disk IO issue")
    }
  ) {
    localDataSource.isFavorite(101, "movie")
  }

  @Test
  fun update_whenGenericError_throwsSQLiteException() = runDbErrorTest(
    "SQLite exception",
    {
      coEvery {
        favoriteDao.update(any(), any(), any(), any())
      } throws SQLiteException("General SQLite error")
    }
  ) {
    localDataSource.update(true, false, 101, "movie")
  }

  @Test
  fun insert_whenUnknownError_throwsException() = runDbErrorTest(
    "Unknown error",
    { coEvery { favoriteDao.insert(any()) } throws Exception("Unknown error") }
  ) {
    localDataSource.insert(createFavoriteEntity())
  }

  private fun createFavoriteEntity() = FavoriteEntity(
    mediaId = 101,
    mediaType = "movie",
    genre = "Action",
    backDrop = "",
    poster = "",
    overview = "",
    title = "Movie1",
    releaseDate = "",
    popularity = 0.0,
    rating = 0f,
    isFavorite = true,
    isWatchlist = false
  )
}
