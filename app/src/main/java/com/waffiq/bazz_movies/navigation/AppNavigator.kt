package com.waffiq.bazz_movies.navigation

import android.R.anim.fade_in
import android.R.anim.fade_out
import android.content.Context
import android.content.Intent
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import com.waffiq.bazz_movies.core.domain.model.ResultItem
import com.waffiq.bazz_movies.core.domain.model.person.MovieTvCastItem
import com.waffiq.bazz_movies.feature_detail.ui.DetailMovieActivity
import com.waffiq.bazz_movies.feature_person.ui.PersonActivity
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
}