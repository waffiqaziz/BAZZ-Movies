package com.waffiq.bazz_movies.feature.about.ui

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.waffiq.bazz_movies.core.common.utils.Constants.BAZZ_MOVIES_LINK
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_LINK_MAIN
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.justifyTextView
import com.waffiq.bazz_movies.core.uihelper.utils.InsetHelper.setupWindowInsets
import com.waffiq.bazz_movies.core.utils.openurl.UriLauncher
import com.waffiq.bazz_movies.feature.about.databinding.ActivityAboutBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class AboutActivity : AppCompatActivity() {

  private lateinit var binding: ActivityAboutBinding

  @Inject
  lateinit var uriLauncher: UriLauncher

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge(
      statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
      navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
    )
    binding = ActivityAboutBinding.inflate(layoutInflater)
    setContentView(binding.root)

    binding.root.setupWindowInsets()

    justifyTextView(binding.tvTmdbAttribute)
    justifyTextView(binding.tvAboutText)
    buttonAction()
  }

  private fun buttonAction() {
    binding.btnBack.setOnClickListener { finish() }

    // setup tmdb logo
    binding.ivTmdbLogo.setOnClickListener {
      uriLauncher.launch(TMDB_LINK_MAIN)
    }

    binding.btnAboutUs.setOnClickListener {
      uriLauncher.launch(BAZZ_MOVIES_LINK)
    }
  }
}
