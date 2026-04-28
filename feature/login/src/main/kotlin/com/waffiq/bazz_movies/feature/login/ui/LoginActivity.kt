package com.waffiq.bazz_movies.feature.login.ui

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.waffiq.bazz_movies.core.common.utils.Constants.ANIM_DURATION
import com.waffiq.bazz_movies.core.designsystem.R.font.nunito_sans_regular
import com.waffiq.bazz_movies.core.designsystem.R.string.guest_user
import com.waffiq.bazz_movies.core.designsystem.R.string.login_as_guest_successful
import com.waffiq.bazz_movies.core.designsystem.R.string.login_as_user_successful
import com.waffiq.bazz_movies.core.designsystem.R.string.no_browser_installed
import com.waffiq.bazz_movies.core.designsystem.R.string.please_enter_a_password
import com.waffiq.bazz_movies.core.designsystem.R.string.please_enter_a_username
import com.waffiq.bazz_movies.core.uihelper.utils.Animation
import com.waffiq.bazz_movies.core.uihelper.utils.Animation.fadeInAlpha50
import com.waffiq.bazz_movies.core.uihelper.utils.SnackBarManager.snackBarWarning
import com.waffiq.bazz_movies.core.uihelper.utils.SnackBarManager.toastShort
import com.waffiq.bazz_movies.feature.login.R.drawable.ic_eye
import com.waffiq.bazz_movies.feature.login.R.drawable.ic_eye_off
import com.waffiq.bazz_movies.feature.login.databinding.ActivityLoginBinding
import com.waffiq.bazz_movies.feature.login.utils.CustomTypefaceSpan
import com.waffiq.bazz_movies.feature.login.utils.Helper.loadTypeface
import com.waffiq.bazz_movies.feature.login.utils.InsetListener.applyWindowInsets
import com.waffiq.bazz_movies.feature.login.utils.common.Constants.TMDB_LINK_FORGET_PASSWORD
import com.waffiq.bazz_movies.feature.login.utils.common.Constants.TMDB_LINK_SIGNUP
import com.waffiq.bazz_movies.feature.login.utils.openurl.UriLauncher
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

  @Inject
  lateinit var navigator: INavigator

  @Inject
  lateinit var uriLauncher: UriLauncher

  private lateinit var binding: ActivityLoginBinding

  private val authenticationViewModel: LoginViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
    binding = ActivityLoginBinding.inflate(layoutInflater)
    setContentView(binding.root)
    applyWindowInsets(binding.root)

    stateObserver()
    binding.progressBar.isVisible = false

    showPassword()
    buttonListener()
  }

  private fun stateObserver() {
    authenticationViewModel.errorState.observe(this) { errorMessage ->
      Animation.fadeOut(
        binding.layoutBackground.bgAlpha,
        ANIM_DURATION,
      )
      enableButton(true)
      snackBarWarning(binding.activityLogin, null, errorMessage)
    }
    authenticationViewModel.loginState.observe(this) { success ->
      if (success) goToMainActivity(isGuest = false)
    }
  }

  private fun launchUri(url: String) {
    uriLauncher.launch(url)
      .onFailure {
        Toast.makeText(this, getString(no_browser_installed), Toast.LENGTH_SHORT).show()
      }
  }

  private fun showPassword() {
    binding.apply {
      btnEye.setOnClickListener {
        // save last cursor position
        val selectionStart = etPass.selectionStart
        val selectionEnd = etPass.selectionEnd

        // if not clicked yet, then hide password
        if (etPass.transformationMethod.equals(HideReturnsTransformationMethod.getInstance())) {
          etPass.transformationMethod = PasswordTransformationMethod.getInstance()
          btnEye.setImageResource(ic_eye_off)
        } else { // show password
          etPass.transformationMethod = HideReturnsTransformationMethod.getInstance()
          btnEye.setImageResource(ic_eye)
        }
        etPass.setSelection(selectionStart, selectionEnd) // set cursor at last position
      }
    }
  }

  private fun buttonListener() {
    binding.tvJoinTMDB.setOnClickListener {
      launchUri(TMDB_LINK_SIGNUP)
    }
    binding.btnForgetPassword.setOnClickListener {
      launchUri(TMDB_LINK_FORGET_PASSWORD)
    }

    // login as guest
    binding.tvGuest.setOnClickListener {
      authenticationViewModel.saveGuestUserPref(getString(guest_user), getString(guest_user))
      goToMainActivity(isGuest = true)
    }

    // login as user
    binding.btnLogin.setOnClickListener {
      validateFormFields()

      // listener to show button eye
      binding.etPass.addTextChangedListener {
        binding.btnEye.visibility = View.VISIBLE
        binding.etPass.error = null
      }

      // listener to remove error on username
      binding.etUsername.addTextChangedListener {
        binding.etUsername.error = null
      }

      // process login if form is valid
      if (formNotEmpty()) {
        enableButton(false)
        fadeInAlpha50(binding.layoutBackground.bgAlpha, ANIM_DURATION)
        loginAsUserRegistered()
      }
    }
  }

  private fun enableButton(isEnable: Boolean) {
    binding.apply {
      tvGuest.isEnabled = isEnable
      tvJoinTMDB.isEnabled = isEnable
      btnLogin.isEnabled = isEnable
      btnForgetPassword.isEnabled = isEnable
      btnEye.isEnabled = isEnable
    }
  }

  private fun validateFormFields() {
    // check password field
    if (binding.etPass.text.isEmpty() || binding.etPass.text.isBlank()) {
      binding.etPass.error = applyFontFamily(getString(please_enter_a_password))
      binding.btnEye.visibility = View.GONE
    } else {
      binding.etPass.error = null
    }

    // check username field
    if (binding.etUsername.text.isEmpty() || binding.etUsername.text.isBlank()) {
      binding.etUsername.error = applyFontFamily(getString(please_enter_a_username))
    } else {
      binding.etUsername.error = null
    }
  }

  private fun formNotEmpty(): Boolean =
    binding.etUsername.text.isNotBlank() &&
      binding.etPass.text.isNotBlank()

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

    authenticationViewModel.userLogin(
      binding.etUsername.text.toString(),
      binding.etPass.text.toString(),
    )
  }

  private fun applyFontFamily(text: String): SpannableStringBuilder {
    val spannableStringBuilder = SpannableStringBuilder(text)
    val customTypefaceSpan = CustomTypefaceSpan(loadTypeface(nunito_sans_regular))

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
