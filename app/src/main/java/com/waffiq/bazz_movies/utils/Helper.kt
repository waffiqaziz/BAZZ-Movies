package com.waffiq.bazz_movies.utils

import android.content.Context
import android.widget.Toast
import com.waffiq.bazz_movies.R
import com.waffiq.bazz_movies.data.local.model.FavoriteDB
import com.waffiq.bazz_movies.data.remote.response.tmdb.KnownForItem
import com.waffiq.bazz_movies.data.remote.response.tmdb.ResultItem
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

object Helper {

  fun toastStillOnDevelopment(context: Context) {
    Toast.makeText(context, context.getString(R.string.feature_not_ready), Toast.LENGTH_SHORT)
      .show()
  }

  fun showToastLong(context: Context, text: String) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT)
      .show()
  }

  private fun getGenreName(int: Int): String {
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
      10770 -> "TV MovieAndTvModel"
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

  fun iterateGenre(data: List<Int>): String {
    var temp = ""
    data.forEach {
      temp = temp + getGenreName(it) + ", "
    }
    temp = temp.dropLast(2)
    return temp
  }

  fun getKnownFor(knownForItem: List<KnownForItem>): String {
    var temp = ""
    knownForItem.forEach {
      temp = temp + it.title + ", "
    }
    temp = temp.dropLast(2)
    return temp
  }

  fun mapResponsesToEntitiesFavorite(input: ResultItem): FavoriteDB {
    return FavoriteDB(
      mediaId = input.id,
      mediaType = input.mediaType,
      title = input.name ?: input.originalName ?: input.title ?: input.originalTitle,
      releaseDate = input.releaseDate ?: input.firstAirDate,
      rating = input.voteAverage,
      backDrop = input.backdropPath,
      poster = input.posterPath,
      genre = iterateGenre(input.genreIds ?: listOf()),
      popularity = input.popularity,
      overview = input.overview,
      is_favorited = true,
      is_watchlist = false
    )
  }

  fun mapResponsesToEntitiesWatchlist(input: ResultItem): FavoriteDB {
    return FavoriteDB(
      mediaId = input.id,
      mediaType = input.mediaType,
      title = input.name ?: input.originalName ?: input.title ?: input.originalTitle,
      releaseDate = input.releaseDate ?: input.firstAirDate,
      rating = input.voteAverage,
      backDrop = input.backdropPath,
      poster = input.posterPath,
      genre = iterateGenre(input.genreIds ?: listOf()),
      popularity = input.popularity,
      overview = input.overview,
      is_favorited = false,
      is_watchlist = true
    )
  }

  fun dateFormater(date: String): String? {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val newDate = formatter.parse(date)
    return DateTimeFormatter.ofPattern("MMM dd, yyyy").format(newDate)
  }

  fun getAgeBirth(date: String): Int {
    val dateParts = date.split("-").toTypedArray()
    val year = dateParts[0].toInt()
    val month = dateParts[1].toInt()
    val day = dateParts[2].toInt()

    return Period.between(
      LocalDate.of(year, month, day),
      LocalDate.now()
    ).years
  }

  fun getAgeDeath(dateBirth: String, dateDeath: String): Int {
    var dateParts = dateBirth.split("-").toTypedArray()
    val yearBirth = dateParts[0].toInt()
    val monthBirth = dateParts[1].toInt()
    val dayBirth = dateParts[2].toInt()

    dateParts = dateDeath.split("-").toTypedArray()
    val yearDeath = dateParts[0].toInt()
    val monthDeath = dateParts[1].toInt()
    val dayDeath = dateParts[2].toInt()

    return Period.between(
      LocalDate.of(yearBirth, monthBirth, dayBirth),
      LocalDate.of(yearDeath, monthDeath, dayDeath)
    ).years
  }

  fun convertRuntime(t: Int): String{
    val hours: Int = t / 60 // since both are ints, you get an int
    val minutes: Int = t % 60
    return "${hours}h ${minutes}m"
  }
}