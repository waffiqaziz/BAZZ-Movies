package com.waffiq.bazz_movies.core.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.waffiq.bazz_movies.core.utils.common.Constants.TABLE_NAME

/**
 * Model table for favorite and watchlist
 */
@Entity(tableName = TABLE_NAME)
data class FavoriteEntity(

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "id")
  val id: Int = 0,

  @ColumnInfo(name = "mediaId")
  val mediaId: Int,

  @ColumnInfo(name = "mediaType")
  val mediaType: String,

  @ColumnInfo(name = "genre")
  val genre: String,

  @ColumnInfo(name = "backDrop")
  val backDrop: String,

  @ColumnInfo(name = "poster")
  val poster: String,

  @ColumnInfo(name = "overview")
  val overview: String,

  @ColumnInfo(name = "title")
  val title: String,

  @ColumnInfo(name = "releaseDate")
  val releaseDate: String,

  @ColumnInfo(name = "popularity")
  val popularity: Double,

  @ColumnInfo(name = "rating")
  val rating: Float,

  @ColumnInfo(name = "is_favorited")
  val isFavorite: Boolean,

  @ColumnInfo(name = "is_watchlist")
  val isWatchlist: Boolean
)
