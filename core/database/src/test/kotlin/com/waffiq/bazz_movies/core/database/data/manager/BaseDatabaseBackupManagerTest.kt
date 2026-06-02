package com.waffiq.bazz_movies.core.database.data.manager

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.waffiq.bazz_movies.core.database.data.model.DatabaseBackup
import com.waffiq.bazz_movies.core.database.data.model.FavoriteBackupEntry
import com.waffiq.bazz_movies.core.database.data.model.FavoriteEntity
import com.waffiq.bazz_movies.core.database.utils.FavoriteMapper.toBackupEntry
import java.io.File

abstract class BaseDatabaseBackupManagerTest {

  protected val gson: Gson = GsonBuilder().setPrettyPrinting().create()

  protected fun Context.writeBackupFile(
    version: Int = 1,
    favorites: List<FavoriteBackupEntry> = listOf(fakeBackupEntry()),
  ): File {
    val backup = DatabaseBackup(
      version = version,
      appVersion = "1.0.0",
      favorites = favorites,
    )
    val json = GsonBuilder().setPrettyPrinting().create().toJson(backup)
    return tempFile().also { it.writeText(json) }
  }

  protected fun Context.tempFile(): File = File.createTempFile("backup_test", ".json", cacheDir)

  protected fun fakeBackupEntry(
    mediaId: Int = 1,
    mediaType: String = "movie",
    title: String = "Test Movie",
  ) = fakeEntity(mediaId = mediaId, mediaType = mediaType, title = title).toBackupEntry()

  protected fun fakeEntity(
    id: Int = 1,
    mediaId: Int = 1,
    mediaType: String = "movie",
    title: String = "Test Movie",
  ) = FavoriteEntity(
    mediaId = mediaId,
    mediaType = mediaType,
    title = "$title $id",
    id = id,
    genre = "",
    backDrop = "",
    poster = "",
    overview = "",
    releaseDate = "",
    popularity = 222.0,
    rating = 22f,
    isFavorite = true,
    isWatchlist = true,
    lastUpdated = 2312,
  )
}
