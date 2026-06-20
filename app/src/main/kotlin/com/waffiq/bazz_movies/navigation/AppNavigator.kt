package com.waffiq.bazz_movies.navigation

import android.R.anim.fade_in
import android.R.anim.fade_out
import android.content.Context
import android.content.Intent
import androidx.core.app.ActivityOptionsCompat
import com.waffiq.bazz_movies.MainActivity
import com.waffiq.bazz_movies.R.id.bottom_navigation
import com.waffiq.bazz_movies.core.models.MediaCastItem
import com.waffiq.bazz_movies.core.models.MediaItem
import com.waffiq.bazz_movies.feature.about.ui.AboutActivity
import com.waffiq.bazz_movies.feature.detail.ui.MediaDetailActivity
import com.waffiq.bazz_movies.feature.detail.ui.MediaDetailActivity.Companion.EXTRA_MOVIE
import com.waffiq.bazz_movies.feature.list.ui.ListActivity
import com.waffiq.bazz_movies.feature.list.ui.ListActivity.Companion.EXTRA_LIST
import com.waffiq.bazz_movies.feature.login.ui.LoginActivity
import com.waffiq.bazz_movies.feature.person.ui.PersonActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppNavigator @Inject constructor() : INavigator {

  private fun Context.openActivity(intent: Intent) {
    val options = ActivityOptionsCompat.makeCustomAnimation(this, fade_in, fade_out)
    startActivity(intent, options.toBundle())
  }

  override fun openPersonDetails(context: Context, cast: MediaCastItem) {
    val intent = Intent(context, PersonActivity::class.java).apply {
      putExtra(PersonActivity.EXTRA_PERSON, cast)
    }
    context.openActivity(intent)
  }

  override fun openDetails(context: Context, mediaItem: MediaItem) {
    val intent = Intent(context, MediaDetailActivity::class.java).apply {
      putExtra(EXTRA_MOVIE, mediaItem)
    }
    context.openActivity(intent)
  }

  override fun openMainActivity(context: Context) {
    val intent = Intent(context, MainActivity::class.java)
    context.openActivity(intent)
  }

  override fun openLoginActivity(context: Context) {
    val intent = Intent(context, LoginActivity::class.java)
    context.openActivity(intent)
  }

  override fun openAboutActivity(context: Context) {
    val intent = Intent(context, AboutActivity::class.java)
    context.openActivity(intent)
  }

  override fun snackbarAnchor(): Int = bottom_navigation

  override fun openList(context: Context, args: ListArgs) {
    val intent = Intent(context, ListActivity::class.java).apply {
      putExtra(EXTRA_LIST, args)
    }
    context.openActivity(intent)
  }
}
