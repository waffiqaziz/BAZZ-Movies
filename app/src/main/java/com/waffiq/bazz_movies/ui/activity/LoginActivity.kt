package com.waffiq.bazz_movies.ui.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.R
import com.waffiq.bazz_movies.data.local.model.UserModel
import com.waffiq.bazz_movies.databinding.ActivityLoginBinding
import com.waffiq.bazz_movies.ui.viewmodel.MainViewModel
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelUserFactory
import com.waffiq.bazz_movies.utils.Constants.TMDB_SIGNUP
import com.waffiq.bazz_movies.utils.Event
import com.waffiq.bazz_movies.utils.Helper

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

class LoginActivity : AppCompatActivity() {
  private lateinit var binding: ActivityLoginBinding
  private lateinit var mainViewModel: MainViewModel
  private lateinit var user: UserModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityLoginBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val factory = ViewModelUserFactory.getInstance(dataStore)
    mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

    showPassword()
    openTMDB()
    btnListener()
  }

  private fun openTMDB() {
    binding.tvJoinTMDB.setOnClickListener {
      startActivity(
        Intent(
          Intent.ACTION_VIEW,
          Uri.parse(TMDB_SIGNUP)
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
      name = "NaN",
      username = "NaN",
      password = "NaN",
      userId = "NaN",
      token = "NaN",
      isLogin = false,
      gravatarHast = "NaN"
    )

    binding.btnLogin.setOnClickListener {
      login()
    }

    binding.tvGuest.setOnClickListener {
      user.name = resources.getString(R.string.gues_user)
      user.username = resources.getString(R.string.no_data)
      mainViewModel.saveUser(user)
      goToMainActivity()
    }
  }

  private fun goToMainActivity() {
    startActivity(Intent(this, MainActivity::class.java))
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    finish()
  }

  private fun login() {
    user.username = binding.edUsername.text.toString()
    user.password = binding.edPass.text.toString()
    user.isLogin = true

    mainViewModel.createToken()
    mainViewModel.getToken().observe(this) { token ->

      mainViewModel.login(user.username, user.password, token)
      mainViewModel.getTokenVerified().observe(this) { tokenVerified ->

        mainViewModel.createSession(tokenVerified)
        mainViewModel.getSessionId().observe(this) { sessionId ->

          mainViewModel.getUserDetail(sessionId)
          mainViewModel.getDataUserDetail().observe(this) {
            user.gravatarHast = it.gravatarHast
            user.name = it.name
            user.userId = it.userId

            Helper.showToastLong(this, user.toString())
            mainViewModel.saveUser(user)
            goToMainActivity()
          }
        }
      }
    }




    mainViewModel.getSnackBarText().observe(this) {
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
}