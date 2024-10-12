package com.waffiq.bazz_movies.core.utils.helpers

import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.Color
import android.graphics.text.LineBreaker
import android.os.Build
import android.text.Layout
import android.view.View
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.waffiq.bazz_movies.core_ui.R.color.gray_1000
import com.waffiq.bazz_movies.core.domain.model.search.KnownForItem
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * Used as general helper
 */
object GeneralHelper {

  fun Context.toastShort(text: String) {
    Toast.makeText(
      applicationContext,
      HtmlCompat.fromHtml(
        text,
        HtmlCompat.FROM_HTML_MODE_LEGACY
      ),
      Toast.LENGTH_SHORT
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

  fun initLinearLayoutManager(context: Context) =
    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

  @Suppress("DEPRECATION")
  fun transparentStatusBar(window: Window) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
      window.setDecorFitsSystemWindows(false)
    } else {
      // This flag is deprecated in API 30 (Android R), but necessary for older versions
      window.decorView.systemUiVisibility =
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
    window.statusBarColor = Color.TRANSPARENT
  }

  fun justifyTextView(textView: TextView) {
    @Suppress("WrongConstant")
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      textView.justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      textView.justificationMode = Layout.JUSTIFICATION_MODE_INTER_WORD
    }
  }

  // region NESTED SCROLL VIEW BEHAVIOR
  fun Context.scrollActionBarBehavior(
    appBarLayout: AppBarLayout,
    nestedScrollView: NestedScrollView
  ) {
    val fromColor = ContextCompat.getColor(this, android.R.color.transparent)
    val toColor = ContextCompat.getColor(this, gray_1000)

    nestedScrollView.setOnScrollChangeListener(
      NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
        val maxScroll = nestedScrollView.getChildAt(0).height - nestedScrollView.height
        val percentage = if (maxScroll > 0) scrollY.toFloat() / maxScroll.toFloat() else 0f
        animateColorChange(appBarLayout, fromColor, toColor, percentage)
      }
    )
  }

  private fun animateColorChange(
    appBarLayout: AppBarLayout,
    fromColor: Int,
    toColor: Int,
    percentage: Float
  ) {
    // Calculate the adjusted progress based on the percentage scrolled
    val adjustedProgress = percentage.coerceIn(0f, 1f) // Ensure the progress is between 0 and 1

    // Calculate the interpolated color based on the adjusted progress
    val interpolatedColor = ArgbEvaluator().evaluate(adjustedProgress, fromColor, toColor) as Int

    // Set the interpolated color as the background color of the AppBarLayout
    appBarLayout.setBackgroundColor(interpolatedColor)
  }
  // endregion NESTED SCROLL VIEW BEHAVIOR
}
