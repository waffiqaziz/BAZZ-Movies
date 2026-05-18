package com.waffiq.bazz_movies

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.feature.login.ui.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoutingActivity : AppCompatActivity() {

  private val userPreferenceViewModel: UserPreferenceViewModel by viewModels()
  private lateinit var splashScreen: SplashScreen

  override fun onCreate(savedInstanceState: Bundle?) {
    splashScreen = installSplashScreen()
    enableEdgeToEdge()
    super.onCreate(savedInstanceState)

    Log.d("RoutingActivity", "Running without Firebase Messaging")
    userPreferenceViewModel.getUserPref().observe(this) { user ->
      Log.d("RoutingActivity", "User is $user")
      gotoMainActivity(user.isLogin)
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
