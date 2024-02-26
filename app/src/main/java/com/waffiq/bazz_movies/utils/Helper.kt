package com.waffiq.bazz_movies.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.telephony.TelephonyManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.text.HtmlCompat
import com.waffiq.bazz_movies.R
import com.waffiq.bazz_movies.data.local.model.FavoriteDB
import com.waffiq.bazz_movies.data.remote.response.tmdb.CrewItem
import com.waffiq.bazz_movies.data.remote.response.tmdb.KnownForItem
import com.waffiq.bazz_movies.data.remote.response.tmdb.ResultItem
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.TimeZone

object Helper {

  fun toastStillOnDevelopment(context: Context) {
    Toast.makeText(context, context.getString(R.string.feature_not_ready), Toast.LENGTH_SHORT)
      .show()
  }

  fun showToastShort(context: Context, text: String) {
    Toast.makeText(
      context, HtmlCompat.fromHtml(
        text, HtmlCompat.FROM_HTML_MODE_LEGACY
      ), Toast.LENGTH_SHORT
    ).show()
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
    data.forEach { temp = temp + getGenreName(it) + ", " }
    temp = temp.dropLast(2)
    return temp
  }

  fun getKnownFor(knownForItem: List<KnownForItem>): String {
    var temp = ""
    knownForItem.forEach { temp = temp + it.title + ", " }
    temp = temp.dropLast(2)
    return temp
  }

  private fun mapResponsesToEntitiesFavoriteDB(
    isFavorite: Boolean,
    isWatchlist: Boolean,
    input: ResultItem
  ): FavoriteDB {
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
      isFavorite = isFavorite,
      isWatchlist = isWatchlist
    )
  }

  fun favTrueWatchlistTrue(data: ResultItem): FavoriteDB {
    return mapResponsesToEntitiesFavoriteDB(isFavorite = true, isWatchlist = true, input = data)
  }

  fun favTrueWatchlistFalse(data: ResultItem): FavoriteDB {
    return mapResponsesToEntitiesFavoriteDB(isFavorite = true, isWatchlist = false, input = data)
  }

  fun favFalseWatchlistTrue(data: ResultItem): FavoriteDB {
    return mapResponsesToEntitiesFavoriteDB(isFavorite = false, isWatchlist = true, input = data)
  }

  fun favFalseWatchlistFalse(data: ResultItem): FavoriteDB {
    return mapResponsesToEntitiesFavoriteDB(isFavorite = false, isWatchlist = false, input = data)
  }

  fun dateFormatter(date: String): String? {
    return if (date.isNotEmpty()) {
      val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
      val newDate = formatter.parse(date)
      DateTimeFormatter.ofPattern("MMM dd, yyyy").format(newDate) // Feb 23, 2021
    } else date
  }

  fun getAgeBirth(date: String): Int {
    val dateParts = date.split("-").toTypedArray()
    val year = dateParts[0].toInt()
    val month = dateParts[1].toInt()
    val day = dateParts[2].toInt()

    return Period.between(LocalDate.of(year, month, day), LocalDate.now()).years
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

  fun convertRuntime(t: Int): String {
    val hours: Int = t / 60
    val minutes: Int = t % 60
    return "${hours}h ${minutes}m"
  }

  fun animFadeOutLong(context: Context): Animation {
    val animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_out)
    animation.duration = 700
    return animation
  }

  private fun getNetworkLocation(context: Context): String {
    val telMgr: TelephonyManager =
      context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    return when (telMgr.simState) {
      TelephonyManager.SIM_STATE_ABSENT ->
        TimeZone.getDefault().id.lowercase()

      TelephonyManager.SIM_STATE_READY ->
        telMgr.networkCountryIso.lowercase()

      TelephonyManager.SIM_STATE_CARD_IO_ERROR -> ""

      TelephonyManager.SIM_STATE_CARD_RESTRICTED -> ""

      TelephonyManager.SIM_STATE_NETWORK_LOCKED -> ""

      TelephonyManager.SIM_STATE_NOT_READY -> ""

      TelephonyManager.SIM_STATE_PERM_DISABLED -> ""

      TelephonyManager.SIM_STATE_PIN_REQUIRED -> ""

      TelephonyManager.SIM_STATE_PUK_REQUIRED -> ""

      TelephonyManager.SIM_STATE_UNKNOWN -> ""

      else -> ""
    }
  }

  @Suppress("DEPRECATION")
  fun getLocation(context: Context): String {

    return getNetworkLocation(context).ifEmpty {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        context.resources.configuration.locales.get(0).country.toString().lowercase()
      } else context.resources.configuration.locale.country.toString().lowercase()
    }
  }

  fun detailCrew(crew: List<CrewItem>): Pair<MutableList<String>, MutableList<String>> {
    val job: MutableList<String> = ArrayList()
    val name: MutableList<String> = ArrayList()

    val director = crew.map { it }.filter {
      it.job == "Director"
    }.map { it.name }.toString().dropLast(1).substring(1)

    val story = crew.map { it }.filter {
      it.job == "Story"
    }.map { it.name }.toString().dropLast(1).substring(1)

    val writing = crew.map { it }.filter {
      it.job == "Writer"
    }.map { it.name }.toString().dropLast(1).substring(1)

    val characters = crew.map { it }.filter {
      it.job == "Characters"
    }.map { it.name }.toString().dropLast(1).substring(1)

    val creator = crew.map { it }.filter {
      it.job == "Executive Producer"
    }.map { it.name }.toString().dropLast(1).substring(1)

    val author = crew.map { it }.filter {
      it.job == "Author"
    }.map { it.name }.toString().dropLast(1).substring(1)

    val screenplay = crew.map { it }.filter {
      it.job == "Screenplay"
    }.map { it.name }.toString().dropLast(1).substring(1)

    val novel = crew.map { it }.filter {
      it.job == "Novel"
    }.map { it.name }.toString().dropLast(1).substring(1)

    if (director.isNotEmpty()) {
      job.add("Director")
      name.add(director)
    }
    if (story.isNotEmpty()) {
      job.add("Story")
      name.add(story)
    }
    if (creator.isNotEmpty()) {
      job.add("Creator")
      name.add(creator)
    }
    if (characters.isNotEmpty()) {
      job.add("Characters")
      name.add(characters)
    }
    if (writing.isNotEmpty()) {
      job.add("Writing")
      name.add(writing)
    }
    if (author.isNotEmpty()) {
      job.add("Author")
      name.add(author)
    }
    if (screenplay.isNotEmpty()) {
      job.add("Screenplay")
      name.add(screenplay)
    }
    if (novel.isNotEmpty()) {
      job.add("Novel")
      name.add(novel)
    }

    return job to name
  }

  private fun isInternetConnectionAvailable(context: Context): Boolean {
    val result: Boolean
    val connectivityManager =
      context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities = connectivityManager.activeNetwork ?: return false
    val actNw =
      connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
    result = when {
      actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
      actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
      actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
      else -> false
    }
    return result
  }

  fun checkInternet(context: Context): String {
    return if (!isInternetConnectionAvailable(context)) context.getString(R.string.no_connection) else ""
  }
}