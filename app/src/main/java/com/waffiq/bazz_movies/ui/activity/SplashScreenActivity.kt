package com.waffiq.bazz_movies.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.waffiq.bazz_movies.R.color.gray_900
import com.waffiq.bazz_movies.databinding.ActivitySplashScreenBinding
import com.waffiq.bazz_movies.ui.viewmodel.AuthenticationViewModel
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelUserFactory
import com.waffiq.bazz_movies.utils.Constants.DELAY_TIME_SPLASH_SCREEN

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
  private lateinit var binding: ActivitySplashScreenBinding
  private lateinit var authenticationViewModel: AuthenticationViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivitySplashScreenBinding.inflate(layoutInflater)
    setContentView(binding.root)

    // change color navigation bar
    window.navigationBarColor = ContextCompat.getColor(this, gray_900)

    val factory = ViewModelUserFactory.getInstance(dataStore)
    authenticationViewModel = ViewModelProvider(this, factory)[AuthenticationViewModel::class.java]

    //add splash screen with fade transition 2 second
    binding.imgLogo.animate().setDuration(DELAY_TIME_SPLASH_SCREEN).alpha(1f).withEndAction {

      authenticationViewModel.getUser().observe(this) {
        if (it.isLogin) gotoMainActivity(true)
        else gotoMainActivity(false)
      }
    }
  }

  private fun gotoMainActivity(boolean: Boolean) {
    if (boolean) {
      startActivity(Intent(this, MainActivity::class.java))
      activityTransition()
      finish()
    } else {
      startActivity(Intent(this, LoginActivity::class.java))
      activityTransition()
      finish()
    }
  }

  private fun activityTransition(){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
      overrideActivityTransition(
        OVERRIDE_TRANSITION_OPEN,
        android.R.anim.fade_in,
        android.R.anim.fade_out
      )
    } else {
      @Suppress("DEPRECATION")
      overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
  }
}