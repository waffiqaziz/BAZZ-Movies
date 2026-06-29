package com.waffiq.bazz_movies.core.database.testutils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import com.waffiq.bazz_movies.core.database.data.manager.DatabaseBackupManager
import com.waffiq.bazz_movies.core.database.data.model.BackupPayload
import com.waffiq.bazz_movies.core.database.data.model.DatabaseBackup
import com.waffiq.bazz_movies.core.database.data.model.FavoriteBackupEntry
import com.waffiq.bazz_movies.core.database.data.room.FavoriteDao
import com.waffiq.bazz_movies.core.database.testutils.DummyData.favoriteTvEntity
import com.waffiq.bazz_movies.core.database.utils.sha256
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before
import java.io.ByteArrayInputStream

abstract class BaseDatabaseBackupManagerTest {

  companion object {
    protected const val CREATED_AT = 1710000000000L
    protected const val APP_VERSION = "1.0.0"
  }

  protected val context: Context = mockk(relaxed = true)
  protected val contentResolver: ContentResolver = mockk()
  protected val favoriteDao: FavoriteDao = mockk(relaxed = true)
  protected val testDispatcher = StandardTestDispatcher()
  protected val mockUri = mockk<Uri>()

  protected lateinit var manager: DatabaseBackupManager

  protected val jsonCompact = Json { ignoreUnknownKeys = true }

  protected val jsonPretty = Json {
    ignoreUnknownKeys = true
    prettyPrint = true
  }

  protected fun createMockEntity(id: Int) = favoriteTvEntity.copy(id = id)

  protected val validBackupEntry = FavoriteBackupEntry(
    mediaId = 101,
    mediaType = "tv",
    genre = "Drama",
    backDrop = "/backdrop1.jpg",
    poster = "/poster1.jpg",
    overview = "Overview 1",
    title = "Breaking Test",
    releaseDate = "2024-01-01",
    popularity = 98.5,
    rating = 8.7f,
    isFavorite = true,
    isWatchlist = false,
    lastUpdated = CREATED_AT,
  )

  protected val secondBackupEntry = validBackupEntry.copy(
    mediaId = 202,
    mediaType = "movie",
    genre = "Action",
    backDrop = "/backdrop2.jpg",
    poster = "/poster2.jpg",
    overview = "Overview 2",
    title = "Movie Test",
    releaseDate = "2024-02-01",
    popularity = 88.2,
    rating = 7.9f,
    isWatchlist = true,
  )

  protected val invalidBackupEntry = validBackupEntry.copy(
    mediaId = -1,
    mediaType = "",
    title = "",
  )

  @Before
  fun setUp() {
    Dispatchers.setMain(testDispatcher)

    mockkStatic(Log::class)
    every { Log.e(any(), any(), any()) } returns 0

    every { context.contentResolver } returns contentResolver

    manager = DatabaseBackupManager(
      context = context,
      favoriteDao = favoriteDao,
      ioDispatcher = testDispatcher,
      appVersion = APP_VERSION,
    )
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
    unmockkAll()
  }

  protected fun loadBackup(name: String): ByteArrayInputStream =
    ByteArrayInputStream(
      loadResource("backup/$name").toByteArray(Charsets.UTF_8),
    )

  private fun loadResource(name: String): String =
    checkNotNull(javaClass.classLoader?.getResource(name)) {
      "Resource $name not found"
    }.readText()

  protected fun createPayload(
    favorites: List<FavoriteBackupEntry>,
    version: Int = DatabaseBackup.BACKUP_VERSION,
    appVersion: String = APP_VERSION,
  ) = BackupPayload(
    version = version,
    createdAt = CREATED_AT,
    appVersion = appVersion,
    favorites = favorites,
  )

  protected fun generateValidBackupJson() {
    generateBackupJson(
      createPayload(
        favorites = listOf(
          validBackupEntry,
          secondBackupEntry,
        ),
      ),
    )
  }

  protected fun generateInvalidEntriesBackupJson() {
    generateBackupJson(
      createPayload(
        favorites = listOf(invalidBackupEntry),
      ),
    )
  }

  protected fun generateEmptyFavoritesBackupJson() {
    generateBackupJson(
      createPayload(
        favorites = emptyList(),
      ),
    )
  }

  protected fun generateInvalidVersionBackupJson() {
    generateBackupJson(
      createPayload(
        favorites = emptyList(),
        version = 0,
      ),
    )
  }

  protected fun generateNewerVersionBackupJson() {
    generateBackupJson(
      createPayload(
        favorites = emptyList(),
        version = DatabaseBackup.BACKUP_VERSION + 1,
        appVersion = "2.0.0",
      ),
    )
  }

  protected fun generateBackupJson(
    payload: BackupPayload,
    checksum: String? = jsonCompact.encodeToString(payload).sha256(),
  ): DatabaseBackup {
    val backup = DatabaseBackup(
      version = payload.version,
      createdAt = payload.createdAt,
      appVersion = payload.appVersion,
      favorites = payload.favorites,
      checksum = checksum,
    )

    // uncomment to get the checsum output
    // println(jsonPretty.encodeToString(backup))

    return backup
  }
}
