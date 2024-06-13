package com.waffiq.bazz_movies.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.waffiq.bazz_movies.R.color.gray_900
import com.waffiq.bazz_movies.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.ui.viewmodelfactory.ViewModelUserFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

class RoutingActivity : AppCompatActivity() {
  private lateinit var userPreferenceViewModel: UserPreferenceViewModel
  private lateinit var splashScreen: SplashScreen

  override fun onCreate(savedInstanceState: Bundle?) {
    splashScreen = installSplashScreen()
    super.onCreate(savedInstanceState)
    splashScreen.setKeepOnScreenCondition { true }

    // change color navigation bar
    window.navigationBarColor = ContextCompat.getColor(this, gray_900)

    val factory = ViewModelUserFactory.getInstance(dataStore)
    userPreferenceViewModel = ViewModelProvider(this, factory)[UserPreferenceViewModel::class.java]
    userPreferenceViewModel.getUserPref().observe(this) {
      if (it.isLogin) gotoMainActivity(true)
      else gotoMainActivity(false)
    }
  }

  private fun gotoMainActivity(boolean: Boolean) {
    if (boolean) {
      startActivity(Intent(this, MainActivity::class.java))
      finish()
    } else {
      startActivity(Intent(this, LoginActivity::class.java))
      finish()
    }
  }
}