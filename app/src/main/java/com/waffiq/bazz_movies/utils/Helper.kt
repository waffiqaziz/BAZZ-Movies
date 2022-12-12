package com.waffiq.bazz_movies.utils

import android.content.Context
import android.widget.Toast
import com.waffiq.bazz_movies.R
import com.waffiq.bazz_movies.data.model.Search

object Helper {

  fun toastStillOnDevelopment(context: Context) {
    Toast.makeText(context, context.getString(R.string.feature_not_ready), Toast.LENGTH_SHORT)
      .show()
  }

  fun getGenreName(int: Int): String {
    return when (int) {
      28 -> "Action"
      12 -> "Adventure"
      16 -> "Animation"
      35 -> "Comedy"
      80 -> "Crime"
      99 -> "Documentary"
      18 -> "Drama"
      10751 -> "Family"
      14 -> "Fantasy"
      36 -> "History"
      27 -> "Horror"
      10402 -> "Music"
      9648 -> "Mystery"
      10749 -> "Romance"
      878 -> "Science Fiction"
      10770 -> "TV Movie"
      53 -> "Thriller"
      10752 -> "War"
      37 -> "Western"
      10759 -> "Action & Adventure"
      10762 -> "Kids"
      10763 -> "News"
      10764 -> "Reality"
      10765 -> "Sci-Fi & Fantasy"
      10766 -> "Soap"
      10767 -> "Talk"
      10768 -> "War & Politics"
      else -> "NaN"
    }
  }

  fun iterateGenre(data: Search): String {
    var temp = ""
    var size = 0
    if (data.genreIds != null) size = data.genreIds.size

    for (i in 0..size - 1) {
      if (i == size - 1) temp = temp + getGenreName(data.genreIds[i])
      else temp = temp + getGenreName(data.genreIds[i]) + ", "
    }
    return temp
  }
}