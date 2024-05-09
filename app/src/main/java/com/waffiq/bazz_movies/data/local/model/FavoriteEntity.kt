package com.waffiq.bazz_movies.data.local.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.waffiq.bazz_movies.utils.Constants.TABLE_NAME
import kotlinx.parcelize.Parcelize

/**
 * Model table for favorite and watchlist
 */
@Parcelize
@Entity(tableName = TABLE_NAME)
@JsonClass(generateAdapter = false)
data class FavoriteEntity(

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "id")
  val id: Int = 0,

  @ColumnInfo(name = "mediaId")
  @Json(name = "mediaId")
  val mediaId: Int,

  @ColumnInfo(name = "mediaType")
  @Json(name = "mediaType")
  val mediaType: String,

  @ColumnInfo(name = "genre")
  @Json(name = "genre")
  val genre: String,

  @ColumnInfo(name = "backDrop")
  @Json(name = "backDrop")
  val backDrop: String,

  @ColumnInfo(name = "poster")
  @Json(name = "poster")
  val poster: String,

  @ColumnInfo(name = "overview")
  @Json(name = "overview")
  val overview: String,

  @ColumnInfo(name = "title")
  @Json(name = "title")
  val title: String,

  @ColumnInfo(name = "releaseDate")
  @Json(name = "releaseDate")
  val releaseDate: String,

  @ColumnInfo(name = "popularity")
  @Json(name = "popularity")
  val popularity: Double,

  @ColumnInfo(name = "rating")
  @Json(name = "rating")
  val rating: Float,

  @ColumnInfo(name = "is_favorited")
  @Json(name = "is_favorited")
  val isFavorite: Boolean,

  @ColumnInfo(name = "is_watchlist")
  @Json(name = "is_watchlist")
  val isWatchlist: Boolean

) : Parcelable
