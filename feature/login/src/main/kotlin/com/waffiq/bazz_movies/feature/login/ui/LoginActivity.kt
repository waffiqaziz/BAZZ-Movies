package com.waffiq.bazz_movies.feature.login.ui

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.waffiq.bazz_movies.core.common.utils.Constants.ANIM_DURATION
import com.waffiq.bazz_movies.core.common.utils.Constants.NAN
import com.waffiq.bazz_movies.core.designsystem.R.font.nunito_sans_regular
import com.waffiq.bazz_movies.core.designsystem.R.string.guest_user
import com.waffiq.bazz_movies.core.designsystem.R.string.login_as_guest_successful
import com.waffiq.bazz_movies.core.designsystem.R.string.login_as_user_successful
import com.waffiq.bazz_movies.core.designsystem.R.string.please_enter_a_password
import com.waffiq.bazz_movies.core.designsystem.R.string.please_enter_a_username
import com.waffiq.bazz_movies.core.domain.UserModel
import com.waffiq.bazz_movies.core.uihelper.utils.Animation
import com.waffiq.bazz_movies.core.uihelper.utils.Animation.fadeInAlpha50
import com.waffiq.bazz_movies.core.uihelper.utils.SnackBarManager.snackBarWarning
import com.waffiq.bazz_movies.core.uihelper.utils.SnackBarManager.toastShort
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.feature.login.R.drawable.ic_eye
import com.waffiq.bazz_movies.feature.login.R.drawable.ic_eye_off
import com.waffiq.bazz_movies.feature.login.databinding.ActivityLoginBinding
import com.waffiq.bazz_movies.feature.login.utils.CustomTypefaceSpan
import com.waffiq.bazz_movies.feature.login.utils.InsetListener.applyWindowInsets
import com.waffiq.bazz_movies.feature.login.utils.common.Constants.TMDB_LINK_FORGET_PASSWORD
import com.waffiq.bazz_movies.feature.login.utils.common.Constants.TMDB_LINK_SIGNUP
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

  @Inject
  lateinit var navigator: INavigator

  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  lateinit var binding: ActivityLoginBinding

  private val authenticationViewModel: AuthenticationViewModel by viewModels()
  private val userPreferenceViewModel: UserPreferenceViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
    super.onCreate(savedInstanceState)
    binding = ActivityLoginBinding.inflate(layoutInflater)
    setContentView(binding.root)
    applyWindowInsets(binding.root)

    authenticationViewModel.errorState.observe(this) { errorMessage ->
      Animation.fadeOut(
        binding.layoutBackground.bgAlpha,
        ANIM_DURATION,
      )
      binding.btnLogin.isEnabled = true
      binding.tvGuest.isEnabled = true
      snackBarWarning(binding.activityLogin, null, errorMessage)
    }
    authenticationViewModel.loginState.observe(this) { getDetailUser(it) }
    binding.progressBar.isVisible = false

    showPassword()
    openTMDB()
    btnLoginListener()
    btnGuestListener()
  }

  private fun openTMDB() {
    binding.tvJoinTMDB.setOnClickListener {
      startActivity(Intent(Intent.ACTION_VIEW, TMDB_LINK_SIGNUP.toUri()))
    }

    binding.btnForgetPassword.setOnClickListener {
      startActivity(Intent(Intent.ACTION_VIEW, TMDB_LINK_FORGET_PASSWORD.toUri()))
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

  private fun btnLoginListener() {
    // login as user
    binding.btnLogin.setOnClickListener {
      validateFormFields()

      // listener to show button eye
      binding.edPass.addTextChangedListener {
        binding.btnEye.visibility = View.VISIBLE
        binding.edPass.error = null
      }

      // listener to remove error on username
      binding.edUsername.addTextChangedListener {
        binding.edUsername.error = null
      }

      // process login if form is valid
      if (formNotEmpty()) {
        binding.tvGuest.isEnabled = false
        binding.btnLogin.isEnabled = false
        fadeInAlpha50(binding.layoutBackground.bgAlpha, ANIM_DURATION)
        loginAsUserRegistered()
      }
    }
  }

  private fun validateFormFields() {
    // check password field
    if (binding.edPass.text.isEmpty() || binding.edPass.text.isBlank()) {
      binding.edPass.error = applyFontFamily(getString(please_enter_a_password))
      binding.btnEye.visibility = View.GONE
    } else {
      binding.edPass.error = null
    }

    // check username field
    if (binding.edUsername.text.isEmpty() || binding.edUsername.text.isBlank()) {
      binding.edUsername.error = applyFontFamily(getString(please_enter_a_username))
    } else {
      binding.edUsername.error = null
    }
  }

  private fun btnGuestListener() {
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
          tmdbAvatar = null,
        ),
      )
      goToMainActivity(isGuest = true)
    }
  }

  private fun formNotEmpty(): Boolean =
    binding.edUsername.text.isNotEmpty() &&
      binding.edUsername.text.isNotBlank() &&
      binding.edPass.text.isNotEmpty() &&
      binding.edPass.text.isNotBlank()

  private fun goToMainActivity(isGuest: Boolean) {
    navigator.openMainActivity(this)
    toastShort(
      ActivityCompat.getString(
        this,
        if (isGuest) {
          login_as_guest_successful
        } else {
          login_as_user_successful
        },
      ),
    )
    finishAffinity()
  }

  private fun loginAsUserRegistered() {
    authenticationViewModel.loadingState.observe(this) {
      binding.progressBar.isVisible = it
    }

    /*
     * login steps
     * 1. Create a new request token
     * 2. Get the user to authorize the request token
     * 3. Create a new session id with the authorized request token
     */
    authenticationViewModel.userLogin(
      binding.edUsername.text.toString(),
      binding.edPass.text.toString(),
    )
  }

  private fun applyFontFamily(text: String): SpannableStringBuilder {
    val spannableStringBuilder = SpannableStringBuilder(text)
    val typeface = ResourcesCompat.getFont(
      this,
      nunito_sans_regular,
    )
    val customTypefaceSpan = typeface?.let {
      CustomTypefaceSpan(it)
    }
    spannableStringBuilder.setSpan(
      customTypefaceSpan,
      0,
      text.length,
      SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE,
    )
    return spannableStringBuilder
  }

  override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    applyWindowInsets(binding.root)
  }

  companion object {
    const val PADDING_RIGHT = 58
  }
}
