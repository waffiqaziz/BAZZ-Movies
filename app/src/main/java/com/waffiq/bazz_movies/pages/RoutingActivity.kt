package com.waffiq.bazz_movies.pages

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.waffiq.bazz_movies.core.ui.R.color.gray_900
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.feature.login.ui.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoutingActivity : AppCompatActivity() {

  private val userPreferenceViewModel: UserPreferenceViewModel by viewModels()
  private lateinit var splashScreen: SplashScreen

  override fun onCreate(savedInstanceState: Bundle?) {
    splashScreen = installSplashScreen()
    super.onCreate(savedInstanceState)
    splashScreen.setKeepOnScreenCondition { true }

    // change color navigation bar
    window.navigationBarColor = ContextCompat.getColor(this, gray_900)

    userPreferenceViewModel.getUserPref().observe(this) {
      if (it.isLogin) gotoMainActivity(true) else gotoMainActivity(false)
    }
  }

  private fun gotoMainActivity(boolean: Boolean) {
    if (boolean) {
      startActivity(Intent(this, MainActivity::class.java))
    } else {
      startActivity(Intent(this, LoginActivity::class.java))
    }
    finish()
  }
}
