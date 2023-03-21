package com.waffiq.bazz_movies.ui.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.R
import com.waffiq.bazz_movies.data.local.model.UserModel
import com.waffiq.bazz_movies.databinding.ActivityLoginBinding
import com.waffiq.bazz_movies.ui.viewmodel.AuthenticationViewModel
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelUserFactory
import com.waffiq.bazz_movies.utils.Constants.TMDB_LINK_SIGNUP
import com.waffiq.bazz_movies.utils.Event
import com.waffiq.bazz_movies.utils.Helper

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

class LoginActivity : AppCompatActivity() {
  private lateinit var binding: ActivityLoginBinding
  private lateinit var authenticationViewModel: AuthenticationViewModel
  private lateinit var user: UserModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityLoginBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val factory = ViewModelUserFactory.getInstance(dataStore)
    authenticationViewModel = ViewModelProvider(this, factory)[AuthenticationViewModel::class.java]

    showPassword()
    openTMDB()
    btnListener()
  }

  private fun openTMDB() {
    binding.tvJoinTMDB.setOnClickListener {
      startActivity(
        Intent(
          Intent.ACTION_VIEW,
          Uri.parse(TMDB_LINK_SIGNUP)
        )
      )
    }
  }

  private fun showPassword() {
    binding.apply {
      btnEye.setOnClickListener {
        //if not clicked yet, then hide password
        if (edPass.transformationMethod.equals(HideReturnsTransformationMethod.getInstance())) {
          //password visible hide it
          edPass.transformationMethod = PasswordTransformationMethod.getInstance()
          btnEye.setImageResource(R.drawable.ic_eye_off)
        } else {
          edPass.transformationMethod = HideReturnsTransformationMethod.getInstance()
          btnEye.setImageResource(R.drawable.ic_eye)
        }
      }
    }
  }

  private fun btnListener() {
    user = UserModel(
      name = getString(R.string.nan),
      username = getString(R.string.nan),
      password = getString(R.string.nan),
      userId = 0,
      token = getString(R.string.nan),
      isLogin = false,
      gravatarHast = getString(R.string.nan)
    )

    binding.btnLogin.setOnClickListener {
      login()
    }

    binding.tvGuest.setOnClickListener {
      user.name = resources.getString(R.string.guest_user)
      user.username = resources.getString(R.string.no_data)
      user.isLogin = true
      authenticationViewModel.saveUser(user)
      goToMainActivity()
    }
  }

  private fun goToMainActivity() {
    startActivity(Intent(this, MainActivity::class.java))
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    Helper.showToastLong(this, "Login Successful")
    finish()
  }

  private fun login() {
    user.username = binding.edUsername.text.toString()
    user.password = binding.edPass.text.toString()
    user.isLogin = true

    authenticationViewModel.getLoading().observe(this) {
      showLoading(it)
    }

    authenticationViewModel.createToken()
    authenticationViewModel.getToken().observe(this) { token ->

      authenticationViewModel.login(user.username, user.password, token)
      authenticationViewModel.getTokenVerified().observe(this) { tokenVerified ->

        authenticationViewModel.createSession(tokenVerified)
        authenticationViewModel.getSessionId().observe(this) { sessionId ->

          user.token = sessionId
          authenticationViewModel.getUserDetail(sessionId)
          authenticationViewModel.getDataUserDetail().observe(this) {
            user.gravatarHast = it.gravatarHast
            user.name = it.name
            user.userId = it.userId

            authenticationViewModel.saveUser(user)
            goToMainActivity()
          }
        }
      }
    }

    authenticationViewModel.getSnackBarText().observe(this) {
      showSnackBar(it)
    }
  }

  private fun showSnackBar(eventMessage: Event<String>) {
    val message = eventMessage.getContentIfNotHandled() ?: return
    Snackbar.make(
      binding.constraintLayout,
      message,
      Snackbar.LENGTH_SHORT
    ).show()
  }

  private fun showLoading(isLoading: Boolean) {
    if (isLoading) {
      binding.progressBar.visibility = View.VISIBLE
    } else {
      binding.progressBar.visibility = View.GONE
    }
  }
}