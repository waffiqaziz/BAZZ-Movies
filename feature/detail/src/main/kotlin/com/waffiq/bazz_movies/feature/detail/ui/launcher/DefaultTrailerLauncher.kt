package com.waffiq.bazz_movies.feature.detail.ui.launcher

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.waffiq.bazz_movies.core.common.utils.Constants.YOUTUBE_LINK_VIDEO

/**
 * Default implementation of the [TrailerLauncher] interface.
 * This implementation launches a YouTube video link in the default web browser.
 *
 * @property YOUTUBE_LINK_VIDEO The base URL for YouTube video links.
 */
class DefaultTrailerLauncher : TrailerLauncher {
  override fun launch(activity: AppCompatActivity, link: String) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
      data = "$YOUTUBE_LINK_VIDEO$link".toUri()
    }
    activity.startActivity(intent)
  }
}
