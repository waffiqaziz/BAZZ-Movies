package com.waffiq.bazz_movies.ui.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.R.color.red_matte
import com.waffiq.bazz_movies.R.drawable.ic_eye
import com.waffiq.bazz_movies.R.drawable.ic_eye_off
import com.waffiq.bazz_movies.R.font.nunito_sans_regular
import com.waffiq.bazz_movies.R.string.guest_user
import com.waffiq.bazz_movies.R.string.login_as_guest_successful
import com.waffiq.bazz_movies.R.string.login_successful
import com.waffiq.bazz_movies.R.string.nan
import com.waffiq.bazz_movies.R.string.please_enter_a_password
import com.waffiq.bazz_movies.R.string.please_enter_a_username
import com.waffiq.bazz_movies.data.local.model.UserModel
import com.waffiq.bazz_movies.databinding.ActivityLoginBinding
import com.waffiq.bazz_movies.ui.viewmodel.AuthenticationViewModel
import com.waffiq.bazz_movies.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.ui.viewmodelfactory.ViewModelUserFactory
import com.waffiq.bazz_movies.utils.common.Constants.TMDB_LINK_FORGET_PASSWORD
import com.waffiq.bazz_movies.utils.common.Constants.TMDB_LINK_SIGNUP
import com.waffiq.bazz_movies.utils.CustomTypefaceSpan
import com.waffiq.bazz_movies.utils.common.Event
import com.waffiq.bazz_movies.utils.Helper.showToastShort

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

class LoginActivity : AppCompatActivity() {
  private lateinit var binding: ActivityLoginBinding
  private lateinit var authenticationViewModel: AuthenticationViewModel
  private lateinit var userPreferenceViewModel: UserPreferenceViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityLoginBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val factory = ViewModelUserFactory.getInstance(dataStore)
    authenticationViewModel = ViewModelProvider(this, factory)[AuthenticationViewModel::class.java]
    userPreferenceViewModel = ViewModelProvider(this, factory)[UserPreferenceViewModel::class.java]

    authenticationViewModel.errorState.observe(this) { showSnackBar(it) }
    authenticationViewModel.loginState.observe(this) { getDetailUser(it) }
    binding.progressBar.isVisible = false

    showPassword()
    openTMDB()
    btnListener()
  }

  private fun openTMDB() {
    binding.tvJoinTMDB.setOnClickListener {
      startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(TMDB_LINK_SIGNUP)))
    }

    binding.tvForgetPassword.setOnClickListener {
      startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(TMDB_LINK_FORGET_PASSWORD)))
    }
  }

  private fun getDetailUser(loginState: Boolean) {
    if (loginState) {
      authenticationViewModel.userModel.observe(this) { dataUser ->
        userPreferenceViewModel.saveUserPref(dataUser)
        goToMainActivity(isGuest = false)
      }
    }
  }

  private fun showPassword() {
    binding.apply {
      btnEye.setOnClickListener {
        // save last cursor position
        val selectionStart = edPass.selectionStart
        val selectionEnd = edPass.selectionEnd

        // if not clicked yet, then hide password
        if (edPass.transformationMethod.equals(HideReturnsTransformationMethod.getInstance())) {
          edPass.transformationMethod = PasswordTransformationMethod.getInstance()
          btnEye.setImageResource(ic_eye_off)
        } else { // show password
          edPass.transformationMethod = HideReturnsTransformationMethod.getInstance()
          btnEye.setImageResource(ic_eye)
        }
        edPass.setSelection(selectionStart, selectionEnd) // set cursor at last position
      }
    }
  }

  private fun btnListener() {

    // login as user
    binding.btnLogin.setOnClickListener {

      // check if username and password form is filled or not
      if (binding.edPass.text.isEmpty() || binding.edPass.text.isBlank()) {
        binding.edPass.error = applyFontFamily(getString(please_enter_a_password))
        binding.btnEye.visibility = View.GONE
      }
      if (binding.edUsername.text.isEmpty() || binding.edUsername.text.isBlank())
        binding.edUsername.error = applyFontFamily(getString(please_enter_a_username))

      // add listener to shop again btn eye
      binding.edPass.addTextChangedListener {
        binding.btnEye.visibility = View.VISIBLE
      }

      // add listener for auto fill in
      if (binding.edUsername.text.isNotEmpty() && binding.edUsername.text.isNotBlank())
        binding.edUsername.error = null
      if (binding.edPass.text.isNotEmpty() && binding.edPass.text.isNotBlank())
        binding.edPass.error = null

      if (binding.edUsername.text.isNotEmpty()
        && binding.edUsername.text.isNotBlank()
        && binding.edPass.text.isNotEmpty()
        && binding.edPass.text.isNotBlank()
      ) loginAsUserRegistered()
    }

    // login as guest
    binding.tvGuest.setOnClickListener {
      val guestUser = UserModel(
        userId = 0,
        name = resources.getString(guest_user),
        username = resources.getString(guest_user),
        password = getString(nan),
        region = getString(nan),
        token = getString(nan),
        isLogin = true,
        gravatarHast = null,
        tmdbAvatar = null
      )

      userPreferenceViewModel.saveUserPref(guestUser)
      goToMainActivity(isGuest = true)
    }
  }


  private fun goToMainActivity(isGuest: Boolean) {
    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
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
    if (isGuest) showToastShort(this, getString(login_as_guest_successful))
    else showToastShort(this, getString(login_successful))

    finishAffinity()
  }

  private fun loginAsUserRegistered() {
    val username = binding.edUsername.text.toString()
    val password = binding.edPass.text.toString()

    authenticationViewModel.loadingState.observe(this) { showLoading(it) }

    /**
     *  login steps
     * 1. Create a new request token
     * 2. Get the user to authorize the request token
     * 3. Create a new session id with the authorized request token
     */
    authenticationViewModel.userLogin(username, password)
  }

  private fun showSnackBar(eventMessage: Event<String>) {
    val message = eventMessage.getContentIfNotHandled() ?: return
    if (message.isNotEmpty()) {
      val snackBar = Snackbar.make(
        binding.constraintLayout,
        message,
        Snackbar.LENGTH_SHORT
      )

      val snackbarView = snackBar.view
      snackbarView.setBackgroundColor(
        ContextCompat.getColor(
          this,
          red_matte
        )
      )
      snackBar.show()
    }
  }

  private fun showLoading(isLoading: Boolean) {
    if (isLoading) binding.progressBar.visibility = View.VISIBLE
    else binding.progressBar.visibility = View.GONE
  }

  private fun applyFontFamily(text: String): SpannableStringBuilder {
    val spannableStringBuilder = SpannableStringBuilder(text)
    val typeface = ResourcesCompat.getFont(this, nunito_sans_regular)
    val customTypefaceSpan = typeface?.let { CustomTypefaceSpan(it) }
    spannableStringBuilder.setSpan(
      customTypefaceSpan,
      0,
      text.length,
      SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return spannableStringBuilder
  }

}