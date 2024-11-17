package com.waffiq.bazz_movies.feature.login.ui

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.waffiq.bazz_movies.core.movie.utils.common.Constants.ANIM_DURATION
import com.waffiq.bazz_movies.core.movie.utils.common.Constants.NAN
import com.waffiq.bazz_movies.core.movie.utils.helpers.uihelpers.ActionBarBehavior.transparentStatusBar
import com.waffiq.bazz_movies.core.movie.utils.helpers.uihelpers.Animation.fadeInAlpha50
import com.waffiq.bazz_movies.core.movie.utils.helpers.uihelpers.Animation.fadeOut
import com.waffiq.bazz_movies.core.movie.utils.helpers.uihelpers.CustomTypefaceSpan
import com.waffiq.bazz_movies.core.movie.utils.helpers.uihelpers.SnackBarManager.snackBarWarning
import com.waffiq.bazz_movies.core.ui.R.font.nunito_sans_regular
import com.waffiq.bazz_movies.core.ui.R.string.guest_user
import com.waffiq.bazz_movies.core.ui.R.string.please_enter_a_password
import com.waffiq.bazz_movies.core.ui.R.string.please_enter_a_username
import com.waffiq.bazz_movies.core.user.data.model.UserModel
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.feature.login.R.drawable.ic_eye
import com.waffiq.bazz_movies.feature.login.R.drawable.ic_eye_off
import com.waffiq.bazz_movies.feature.login.databinding.ActivityLoginBinding
import com.waffiq.bazz_movies.feature.login.utils.common.Constants.TMDB_LINK_FORGET_PASSWORD
import com.waffiq.bazz_movies.feature.login.utils.common.Constants.TMDB_LINK_SIGNUP
import com.waffiq.bazz_movies.navigation.Navigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

  @Inject
  lateinit var navigator: Navigator

  private lateinit var binding: ActivityLoginBinding
  private val authenticationViewModel: AuthenticationViewModel by viewModels()
  private val userPreferenceViewModel: UserPreferenceViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityLoginBinding.inflate(layoutInflater)
    setContentView(binding.root)
    applyWindowInsetsListener()
    window.transparentStatusBar()

    authenticationViewModel.errorState.observe(this) { errorMessage ->
      fadeOut(
        binding.layoutBackground.bgAlpha,
        ANIM_DURATION
      )
      binding.btnLogin.isEnabled = true
      binding.tvGuest.isEnabled = true
      snackBarWarning(
        binding.constraintLayout,
        null,
        errorMessage
      )
    }
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

    binding.btnForgetPassword.setOnClickListener {
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
      // Check if the username and password fields are filled or not
      if (binding.edPass.text.isEmpty() || binding.edPass.text.isBlank()) {
        binding.edPass.error = applyFontFamily(getString(please_enter_a_password))
        binding.btnEye.visibility = View.GONE
      }
      if (binding.edUsername.text.isEmpty() || binding.edUsername.text.isBlank()) {
        binding.edUsername.error = applyFontFamily(getString(please_enter_a_username))
      }

      // listener to show button eye
      binding.edPass.addTextChangedListener {
        binding.btnEye.visibility = View.VISIBLE
      }

      // listener for autofill
      if (binding.edUsername.text.isNotEmpty() && binding.edUsername.text.isNotBlank()) {
        binding.edUsername.error = null
      }
      if (binding.edPass.text.isNotEmpty() && binding.edPass.text.isNotBlank()) {
        binding.edPass.error = null
      }

      if (formNotEmpty()) {
        binding.tvGuest.isEnabled = false
        binding.btnLogin.isEnabled = false
        fadeInAlpha50(
          binding.layoutBackground.bgAlpha,
          ANIM_DURATION
        )
        loginAsUserRegistered()
      }
    }

    // login as guest
    binding.tvGuest.setOnClickListener {
      userPreferenceViewModel.saveUserPref(
        UserModel(
          userId = 0,
          name = resources.getString(guest_user),
          username = resources.getString(guest_user),
          password = NAN,
          region = NAN,
          token = NAN,
          isLogin = true,
          gravatarHast = null,
          tmdbAvatar = null
        )
      )
      goToMainActivity(isGuest = true)
    }
  }

  private fun formNotEmpty(): Boolean {
    return binding.edUsername.text.isNotEmpty() &&
      binding.edUsername.text.isNotBlank() &&
      binding.edPass.text.isNotEmpty() &&
      binding.edPass.text.isNotBlank()
  }

  private fun goToMainActivity(isGuest: Boolean) {
    navigator.openMainActivity(isGuest, this@LoginActivity, this)

    finishAffinity()
  }

  private fun loginAsUserRegistered() {
    authenticationViewModel.loadingState.observe(this) {
      binding.progressBar.isVisible = it
    }

    /**
     *  login steps
     * 1. Create a new request token
     * 2. Get the user to authorize the request token
     * 3. Create a new session id with the authorized request token
     */
    authenticationViewModel.userLogin(
      binding.edUsername.text.toString(),
      binding.edPass.text.toString()
    )
  }

  private fun applyFontFamily(text: String): SpannableStringBuilder {
    val spannableStringBuilder = SpannableStringBuilder(text)
    val typeface = ResourcesCompat.getFont(
      this,
      nunito_sans_regular
    )
    val customTypefaceSpan = typeface?.let {
      CustomTypefaceSpan(
        it
      )
    }
    spannableStringBuilder.setSpan(
      customTypefaceSpan,
      0,
      text.length,
      SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return spannableStringBuilder
  }

  override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    applyWindowInsetsListener()
  }

  private fun applyWindowInsetsListener() {

    val isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
      ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
        val navBarInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        val isGestureNavigation = navBarInsets.right == 0

        if (!isGestureNavigation) {
          v.setPadding(
            v.paddingLeft,
            v.paddingTop,
            58,
            v.paddingBottom
          )
        } else {
          v.setPadding(v.paddingLeft, v.paddingTop, 0, v.paddingBottom)
        }
        insets
      }
    }
  }
}
