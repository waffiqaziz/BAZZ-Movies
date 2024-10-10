package com.waffiq.bazz_movies.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.waffiq.bazz_movies.MyApplication
import com.waffiq.bazz_movies.R.color.gray_900
import com.waffiq.bazz_movies.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.ui.viewmodelfactory.ViewModelFactory
import javax.inject.Inject

class RoutingActivity : AppCompatActivity() {

  @Inject
  lateinit var factory: ViewModelFactory

  private val userPreferenceViewModel: UserPreferenceViewModel by viewModels { factory }
  private lateinit var splashScreen: SplashScreen

  override fun onCreate(savedInstanceState: Bundle?) {
    splashScreen = installSplashScreen()
    (application as MyApplication).appComponent.inject(this)
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
