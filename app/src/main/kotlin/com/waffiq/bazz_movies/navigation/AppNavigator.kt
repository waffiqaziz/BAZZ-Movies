package com.waffiq.bazz_movies.navigation

import android.R.anim.fade_in
import android.R.anim.fade_out
import android.content.Context
import android.content.Intent
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import com.waffiq.bazz_movies.MainActivity
import com.waffiq.bazz_movies.R.id.bottom_navigation
import com.waffiq.bazz_movies.core.domain.MediaCastItem
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.feature.about.ui.AboutActivity
import com.waffiq.bazz_movies.feature.detail.ui.MediaDetailActivity
import com.waffiq.bazz_movies.feature.login.ui.LoginActivity
import com.waffiq.bazz_movies.feature.person.ui.PersonActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppNavigator @Inject constructor() : INavigator {
  override fun openPersonDetails(context: Context, cast: MediaCastItem) {
    val intent = Intent(context, PersonActivity::class.java).apply {
      putExtra(PersonActivity.EXTRA_PERSON, cast)
    }
    val options = ActivityOptionsCompat.makeCustomAnimation(context, fade_in, fade_out)
    ActivityCompat.startActivities(context, arrayOf(intent), options.toBundle())
  }

  override fun openDetails(context: Context, mediaItem: MediaItem) {
    val intent = Intent(context, MediaDetailActivity::class.java).apply {
      putExtra(MediaDetailActivity.EXTRA_MOVIE, mediaItem)
    }
    val options = ActivityOptionsCompat.makeCustomAnimation(context, fade_in, fade_out)
    ActivityCompat.startActivities(context, arrayOf(intent), options.toBundle())
  }

  override fun openMainActivity(context: Context) {
    val options = ActivityOptionsCompat.makeCustomAnimation(context, fade_in, fade_out)
    val intent = Intent(context, MainActivity::class.java)
    ActivityCompat.startActivities(context, arrayOf(intent), options.toBundle())
  }

  override fun openLoginActivity(context: Context) {
    val intent = Intent(context, LoginActivity::class.java)
    val options = ActivityOptionsCompat.makeCustomAnimation(context, fade_in, fade_out)
    ActivityCompat.startActivities(context, arrayOf(intent), options.toBundle())
  }

  override fun openAboutActivity(context: Context) {
    val intent = Intent(context, AboutActivity::class.java)
    val options = ActivityOptionsCompat.makeCustomAnimation(context, fade_in, fade_out)
    ActivityCompat.startActivities(context, arrayOf(intent), options.toBundle())
  }

  override fun snackbarAnchor(): Int = bottom_navigation
}
