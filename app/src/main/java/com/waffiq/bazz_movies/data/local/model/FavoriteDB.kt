package com.waffiq.bazz_movies.data.local.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.waffiq.bazz_movies.utils.Constants.TABLE_NAME
import kotlinx.parcelize.Parcelize

/**
 * Model table for favorite and watchlist
 */
@Parcelize
@Entity(tableName = TABLE_NAME)
data class FavoriteDB(

  @PrimaryKey
  @ColumnInfo(name = "mediaId")
  @field:SerializedName("mediaId")
  val mediaId: Int,

  @ColumnInfo(name = "mediaType")
  @field:SerializedName("mediaType")
  val mediaType: String? = null,

  @ColumnInfo(name = "genre")
  @field:SerializedName("genre")
  val genre: String? = null,

  @ColumnInfo(name = "backDrop")
  @field:SerializedName("backDrop")
  val backDrop: String? = null,

  @ColumnInfo(name = "poster")
  @field:SerializedName("poster")
  val poster: String? = null,

  @ColumnInfo(name = "overview")
  @field:SerializedName("overview")
  val overview: String? = null,

  @ColumnInfo(name = "title")
  @field:SerializedName("title")
  val title: String? = null,

  @ColumnInfo(name = "releaseDate")
  @field:SerializedName("releaseDate")
  val releaseDate: String? = null,

  @ColumnInfo(name = "popularity")
  @field:SerializedName("popularity")
  val popularity: Double? = null,

  @ColumnInfo(name = "rating")
  @field:SerializedName("rating")
  val rating: Float? = null,

  @ColumnInfo(name = "is_favorited")
  @field:SerializedName("is_favorited")
  val isFavorite: Boolean? = null,

  @ColumnInfo(name = "is_watchlist")
  @field:SerializedName("is_watchlist")
  val isWatchlist: Boolean? = null

): Parcelable
