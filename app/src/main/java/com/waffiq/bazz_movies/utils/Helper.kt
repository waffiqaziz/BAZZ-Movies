package com.waffiq.bazz_movies.utils

import android.content.Context
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.text.HtmlCompat
import com.waffiq.bazz_movies.domain.model.detail.Video
import com.waffiq.bazz_movies.domain.model.search.KnownForItem
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

object Helper {

  fun showToastShort(context: Context, text: String) {
    Toast.makeText(
      context, HtmlCompat.fromHtml(
        text, HtmlCompat.FROM_HTML_MODE_LEGACY
      ), Toast.LENGTH_SHORT
    ).show()
  }

  fun getKnownFor(knownForItemResponse: List<KnownForItem>): String {
    var temp = ""
    knownForItemResponse.forEach { temp = temp + it.title + ", " }
    temp = temp.dropLast(2)
    return temp
  }

  fun dateFormatterStandard(date: String?): String {
    return try {
      val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
      val newDate = formatter.parse(date)
      DateTimeFormatter.ofPattern("MMM dd, yyyy").format(newDate) // Feb 23, 2021
    } catch (e: DateTimeParseException) {
      ""
    }
  }

  fun dateFormatterISO8601(date: String?): String {
    return try {
      val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
      val newDate = ZonedDateTime.parse(date, formatter)
      DateTimeFormatter.ofPattern("MMM dd, yyyy").format(newDate) // Feb 23, 2021
    } catch (e: DateTimeParseException) {
      ""
    }
  }

  fun animFadeOutLong(context: Context): Animation {
    val animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_out)
    animation.duration = 750
    return animation
  }

  fun transformLink(video: Video): String {
    return video.results
      .filter { it.official == true && it.type.equals("Trailer", ignoreCase = true) }
      .map { it.key }.firstOrNull()?.trim()
      ?: video.results.map { it.key }.firstOrNull()?.trim()
      ?: ""
  }

  // region GENRE
  private fun getGenreName(int: Int): String {
    return when (int) {

      // movies
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

      // for TV
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
    data.forEach { temp = temp + getGenreName(it) + ", " }
    temp = temp.dropLast(2)
    return temp
  }

  private fun genreToInt(genre: String): Int {
    return when (genre) {
      // movies
      "action" -> 28
      "adventure" -> 12
      "animation" -> 16
      "comedy" -> 35
      "crime" -> 80
      "documentary" -> 99
      "drama" -> 18
      "family" -> 10751
      "fantasy" -> 14
      "history" -> 36
      "horror" -> 27
      "music" -> 10402
      "mystery" -> 9648
      "romance" -> 10749
      "science fiction" -> 878
      "tv movie" -> 10770
      "thriller" -> 53
      "war" -> 10752
      "western" -> 37

      // for TV
      "action & adventure" -> 10759
      "kids" -> 10762
      "news" -> 10763
      "reality" -> 10764
      "sci-fi & fantasy" -> 10765
      "soap" -> 10766
      "talk" -> 10767
      "war & politics" -> 10768
      else -> 0
    }
  }

  @Suppress("UNUSED")
  fun iterateGenreToInt(data: List<String>): String {
    var temp = ""

    /**
     *  "," Comma's are treated like an AND and query while "|"Pipe's are an OR.
     *  https://www.themoviedb.org/talk/635968b34a4bf6007c5997f3
     *
     *  used to get id genre
     */
    data.forEach { temp = temp + genreToInt(it) + "|" } // using OR
    temp = temp.dropLast(2)
    return temp
  }
  // endregion GENRE
}