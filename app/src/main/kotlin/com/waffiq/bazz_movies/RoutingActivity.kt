package com.waffiq.bazz_movies

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.messaging.FirebaseMessaging
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_bazz_logo
import com.waffiq.bazz_movies.core.designsystem.R.string.allow
import com.waffiq.bazz_movies.core.designsystem.R.string.not_now
import com.waffiq.bazz_movies.core.designsystem.R.string.notification_description
import com.waffiq.bazz_movies.core.designsystem.R.string.notification_later
import com.waffiq.bazz_movies.core.designsystem.R.string.stay_updated
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.feature.login.ui.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoutingActivity : AppCompatActivity() {

  private val userPreferenceViewModel: UserPreferenceViewModel by viewModels()
  private lateinit var splashScreen: SplashScreen

  private var isLogin: Boolean = false

  override fun onCreate(savedInstanceState: Bundle?) {
    splashScreen = installSplashScreen()
    enableEdgeToEdge()
    super.onCreate(savedInstanceState)

    userPreferenceViewModel.getUserPref().observe(this) { user ->
      Log.d("RoutingActivity", "User is  $user")
      isLogin = user.isLogin
    }

    requestNotificationPermission()

    FirebaseMessaging.getInstance().token.addOnCompleteListener(
      OnCompleteListener { task ->
        if (!task.isSuccessful) {
          Log.w("TOKEN", "Fetching FCM registration token failed", task.exception)
          return@OnCompleteListener
        }
        val token = task.result

        Log.d("TOKEN", token)
        Toast.makeText(baseContext, "TOKEN : $token", Toast.LENGTH_SHORT).show()
      }
    )
  }

  private val requestNotificationPermissionLauncher = registerForActivityResult(
    ActivityResultContracts.RequestPermission()
  ) { isGranted: Boolean ->
    userPreferenceViewModel.savePermissionAsked()
    if (isGranted) {
      Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT).show()
    } else {
      Toast.makeText(this, "Notifications permission rejected", Toast.LENGTH_SHORT).show()
    }
    gotoMainActivity(isLogin)
  }

  private fun requestNotificationPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      userPreferenceViewModel.getPermissionAsked().observe(this) {
        if (!it) {
          if (ContextCompat.checkSelfPermission(
              this, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
          ) {
            showNotificationRationaleDialog()
          }
        } else {
          gotoMainActivity(isLogin)
        }
      }
    }
  }

  @RequiresApi(Build.VERSION_CODES.TIRAMISU)
  private fun showNotificationRationaleDialog() {
    MaterialAlertDialogBuilder(this)
      .setTitle(getString(stay_updated))
      .setMessage(getString(notification_description))
      .setIcon(ic_bazz_logo)
      .setPositiveButton(getString(allow)) { _, _ ->
        requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
      }
      .setNegativeButton(getString(not_now)) { _, _ ->
        userPreferenceViewModel.savePermissionAsked()
        Toast.makeText(this, getString(notification_later), Toast.LENGTH_SHORT).show()
        gotoMainActivity(isLogin)
      }
      .setCancelable(false) // prevent dismissing by tapping outside
      .show()
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
