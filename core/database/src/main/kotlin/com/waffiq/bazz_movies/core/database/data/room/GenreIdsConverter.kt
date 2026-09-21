package com.waffiq.bazz_movies.core.database.data.room

import androidx.room.TypeConverter

class GenreIdsConverter {
  @TypeConverter
  fun fromGenreIds(value: List<Int>): String = value.joinToString(",")

  @TypeConverter
  fun toGenreIds(value: String): List<Int> = value.split(",").mapNotNull { it.trim().toIntOrNull() }
}
