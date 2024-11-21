package com.waffiq.bazz_movies.navigation

import android.R.anim.fade_in
import android.R.anim.fade_out
import android.app.Activity
import android.app.Activity.OVERRIDE_TRANSITION_OPEN
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import com.waffiq.bazz_movies.MainActivity
import com.waffiq.bazz_movies.R.id.bottom_navigation
import com.waffiq.bazz_movies.core.data.MovieTvCastItem
import com.waffiq.bazz_movies.core.data.ResultItem
import com.waffiq.bazz_movies.core.designsystem.R.string.login_as_guest_successful
import com.waffiq.bazz_movies.core.designsystem.R.string.login_successful
import com.waffiq.bazz_movies.core.uihelper.utils.SnackBarManager.toastShort
import com.waffiq.bazz_movies.feature.about.ui.AboutActivity
import com.waffiq.bazz_movies.feature.detail.ui.DetailMovieActivity
import com.waffiq.bazz_movies.feature.login.ui.LoginActivity
import com.waffiq.bazz_movies.feature.person.ui.PersonActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppNavigator @Inject constructor() : Navigator {
  override fun openPersonDetails(context: Context, cast: MovieTvCastItem) {
    val intent = Intent(context, PersonActivity::class.java).apply {
      putExtra(PersonActivity.EXTRA_PERSON, cast)
    }
    val options = ActivityOptionsCompat.makeCustomAnimation(context, fade_in, fade_out)
    ActivityCompat.startActivity(context, intent, options.toBundle())
  }

  override fun openDetails(context: Context, resultItem: ResultItem) {
    val intent = Intent(context, DetailMovieActivity::class.java).apply {
      putExtra(DetailMovieActivity.EXTRA_MOVIE, resultItem)
    }
    val options = ActivityOptionsCompat.makeCustomAnimation(context, fade_in, fade_out)
    ActivityCompat.startActivity(context, intent, options.toBundle())
  }

  override fun openMainActivity(isGuest: Boolean, activity: Activity, context: Context) {
    activity.startActivity(Intent(context, MainActivity::class.java))
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
      activity.overrideActivityTransition(
        OVERRIDE_TRANSITION_OPEN,
        fade_in,
        fade_out
      )
    } else {
      @Suppress("DEPRECATION")
      activity.overridePendingTransition(fade_in, fade_out)
    }
    if (isGuest) {
      context.toastShort(ActivityCompat.getString(context, login_as_guest_successful))
    } else {
      context.toastShort(ActivityCompat.getString(context, login_successful))
    }
  }

  override fun openLoginActivity(context: Context) {
    val intent = Intent(context, LoginActivity::class.java)
    val options = ActivityOptionsCompat.makeCustomAnimation(context, fade_in, fade_out)
    ActivityCompat.startActivity(context, intent, options.toBundle())
  }

  override fun openAboutActivity(context: Context) {
    val intent = Intent(context, AboutActivity::class.java)
    val options = ActivityOptionsCompat.makeCustomAnimation(context, fade_in, fade_out)
    ActivityCompat.startActivity(context, intent, options.toBundle())
  }

  override fun snackbarAnchor(): Int = bottom_navigation
}
