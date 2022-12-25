package com.waffiq.bazz_movies.data.local.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.waffiq.bazz_movies.utils.Constants.TABLE_NAME
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = TABLE_NAME)
data class Favorite(

  @PrimaryKey
  @ColumnInfo(name = "mediaId")
  @field:SerializedName("mediaId")
  val mediaId: Int? = null,

  @ColumnInfo(name = "mediaType")
  @field:SerializedName("mediaType")
  val mediaType: String? = null,

  @ColumnInfo(name = "genre")
  @field:SerializedName("genre")
  val genre: String? = null,

  @ColumnInfo(name = "image")
  @field:SerializedName("image")
  val imagePath: String? = null,

  @ColumnInfo(name = "title")
  @field:SerializedName("title")
  val title: String? = null,

  @ColumnInfo(name = "releaseDate")
  @field:SerializedName("releaseDate")
  val releaseDate: String? = null,

  @ColumnInfo(name = "rating")
  @field:SerializedName("rating")
  val rating: Double? = null

): Parcelable
