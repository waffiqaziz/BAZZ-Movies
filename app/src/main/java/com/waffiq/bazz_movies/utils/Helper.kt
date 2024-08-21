package com.waffiq.bazz_movies.utils

import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getString
import androidx.core.text.HtmlCompat
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.R
import com.waffiq.bazz_movies.data.remote.responses.tmdb.detail_movie_tv.cast_crew.MovieTvCrewItemResponse
import com.waffiq.bazz_movies.domain.model.ResultItem
import com.waffiq.bazz_movies.domain.model.detail.Video
import com.waffiq.bazz_movies.domain.model.search.KnownForItem
import com.waffiq.bazz_movies.utils.common.Event
import okio.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.time.LocalDate
import java.time.Period
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.TimeZone

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

  // region LOCATION
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
  // endregion LOCATION

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

  // region PAGING
  fun pagingErrorHandling(error: Throwable): String {
    return when (error) {
      is SocketTimeoutException -> "Connection timed out. Please try again."
      is UnknownHostException -> "Unable to resolve server hostname. Please check your internet connection."
      is IOException -> "Please check your network connection"
      else -> "Something went wrong"
    }
  }

  fun combinedLoadStatesHandle2(
    loadState: CombinedLoadStates
  ): String {
    val errorState = when { // If theres an error, show a toast
      loadState.append is LoadState.Error -> loadState.append as LoadState.Error
      loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
      loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
      else -> null
    }
    errorState?.let {
      return pagingErrorHandling(it.error)
    } ?: return ""
  }
  // endregion PAGING


  // region DETAIL MOVIE PAGE
  fun convertRuntime(t: Int): String {
    val hours: Int = t / 60
    val minutes: Int = t % 60
    return "${hours}h ${minutes}m"
  }

  fun detailCrew(crew: List<MovieTvCrewItemResponse>): Pair<MutableList<String>, MutableList<String>> {
    val job: MutableList<String> = ArrayList()
    val name: MutableList<String> = ArrayList()

    val director = crew.map { it }.filter {
      it.job == "Director"
    }.map { it.name }.toString().dropLast(1).substring(1)

    val story = crew.map { it }.filter {
      it.job == "Story"
    }.map { it.name }.toString().dropLast(1).substring(1)

    val writer = crew.map { it }.filter {
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
    if (writer.isNotEmpty()) {
      job.add("Writer")
      name.add(writer)
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

  fun transformLink(video: Video): String {
    return video.results
      .filter { it.official == true && it.type.equals("Trailer", ignoreCase = true) }
      .map { it.key }.firstOrNull()?.trim()
      ?: video.results.map { it.key }.firstOrNull()?.trim()
      ?: ""
  }
  // endregion DETAIL MOVIE PAGE

  // region PERSON PAGE
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
  // endregion PERSON PAGE

  // region FAVORITE WATCHLIST PAGE
  fun titleHandler(item: ResultItem): String {
    return item.name ?: item.title ?: item.originalTitle ?: "Item"
  }

  fun snackBarWarning(
    context: Context,
    view: View,
    guideView: View,
    eventMessage: Event<String>
  ): Snackbar? {
    val message = eventMessage.getContentIfNotHandled() ?: return null
    val mSnackbar = Snackbar.make(
      view,
      message.ifEmpty { getString(context, R.string.unknown_error) },
      Snackbar.LENGTH_SHORT
    ).setAnchorView(guideView)

    val snackbarView = mSnackbar.view
    snackbarView.setBackgroundColor(ContextCompat.getColor(context, R.color.red_matte))
    if (message.isNotEmpty()) mSnackbar.show()
    return mSnackbar
  }

  fun snackBarAlreadyWatchlist(
    context: Context,
    view: View,
    viewGuide: View,
    eventMessage: Event<String>
  ): Snackbar? {
    val result = eventMessage.getContentIfNotHandled() ?: return null
    val mSnackbar = Snackbar.make(
      view,
      HtmlCompat.fromHtml(
        "<b>${result}</b> " + getString(context, R.string.already_watchlist),
        HtmlCompat.FROM_HTML_MODE_LEGACY
      ),
      Snackbar.LENGTH_SHORT
    ).setAnchorView(viewGuide)
    mSnackbar.show()
    return mSnackbar
  }

  fun snackBarAlreadyFavorite(
    context: Context,
    view: View,
    viewGuide: View,
    eventMessage: Event<String>
  ): Snackbar? {
    val result = eventMessage.getContentIfNotHandled() ?: return null
    val mSnackbar = Snackbar.make(
      view,
      HtmlCompat.fromHtml(
        "<b>${result}</b> " + getString(context, R.string.already_favorite),
        HtmlCompat.FROM_HTML_MODE_LEGACY
      ),
      Snackbar.LENGTH_SHORT
    ).setAnchorView(viewGuide)
    mSnackbar.show()
    return mSnackbar
  }

  fun getDateTwoWeeksFromToday(): String {
    val twoWeeksFromNow = LocalDate.now().plusWeeks(2)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return twoWeeksFromNow.format(formatter)
  }
  // endregion FAVORITE WATCHLIST PAGE
}