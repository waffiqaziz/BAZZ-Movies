package com.waffiq.bazz_movies.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.waffiq.bazz_movies.utils.Constants.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class Favorite(
  val favorite: Boolean,
  @PrimaryKey
  val mediaId: Int,
  val mediaType: String,
  val genre: String,
  val image: String,
  val title: String,
  val releaseDate: String,
  val rating: Float
)
