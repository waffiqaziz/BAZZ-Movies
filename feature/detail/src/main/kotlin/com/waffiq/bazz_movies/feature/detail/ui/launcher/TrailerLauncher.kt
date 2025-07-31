package com.waffiq.bazz_movies.feature.detail.ui.launcher

import androidx.appcompat.app.AppCompatActivity

/**
 * Interface for launching trailers in the Media Detail Activity.
 *
 * This interface defines a method to launch a trailer link from the Media Detail Activity.
 * Implementations should handle the actual launching of the trailer, typically by opening
 * a web browser or a youtube app with the provided link.
 */
interface TrailerLauncher {
  fun launch(activity: AppCompatActivity, link: String)
}
