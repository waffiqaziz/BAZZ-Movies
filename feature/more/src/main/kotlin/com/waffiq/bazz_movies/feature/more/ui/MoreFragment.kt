package com.waffiq.bazz_movies.feature.more.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.common.utils.Constants.ANIM_DURATION
import com.waffiq.bazz_movies.core.common.utils.Constants.FAQ_LINK
import com.waffiq.bazz_movies.core.common.utils.Constants.FORM_HELPER
import com.waffiq.bazz_movies.core.common.utils.Constants.GRAVATAR_LINK
import com.waffiq.bazz_movies.core.common.utils.Constants.NAN
import com.waffiq.bazz_movies.core.common.utils.Constants.PRIVACY_POLICY_LINK
import com.waffiq.bazz_movies.core.common.utils.Constants.TERMS_CONDITIONS_LINK
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_IMG_LINK_AVATAR
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_bazz_logo
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_broken_image
import com.waffiq.bazz_movies.core.designsystem.R.string.all_data_deleted
import com.waffiq.bazz_movies.core.designsystem.R.string.binding_error
import com.waffiq.bazz_movies.core.designsystem.R.string.no
import com.waffiq.bazz_movies.core.designsystem.R.string.sign_out_success
import com.waffiq.bazz_movies.core.designsystem.R.string.warning
import com.waffiq.bazz_movies.core.designsystem.R.string.warning_signOut_guest_mode
import com.waffiq.bazz_movies.core.designsystem.R.string.warning_signOut_logged_user
import com.waffiq.bazz_movies.core.designsystem.R.string.yes
import com.waffiq.bazz_movies.core.designsystem.R.style.CustomAlertDialogTheme
import com.waffiq.bazz_movies.core.domain.UserModel
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.core.uihelper.state.UIState
import com.waffiq.bazz_movies.core.uihelper.state.isLoading
import com.waffiq.bazz_movies.core.uihelper.utils.Animation.fadeInAlpha50
import com.waffiq.bazz_movies.core.uihelper.utils.Animation.fadeOut
import com.waffiq.bazz_movies.core.uihelper.utils.SnackBarManager.toastShort
import com.waffiq.bazz_movies.core.user.ui.viewmodel.RegionViewModel
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.feature.more.databinding.FragmentMoreBinding
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MoreFragment : Fragment() {

  @Inject
  lateinit var navigator: INavigator

  @Inject
  lateinit var snackbar: ISnackbar

  private var _binding: FragmentMoreBinding? = null
  private val binding get() = _binding ?: error(getString(binding_error))

  private val moreLocalViewModel: MoreLocalViewModel by viewModels()
  private val moreUserViewModel: MoreUserViewModel by viewModels()
  private val userPreferenceViewModel: UserPreferenceViewModel by viewModels()
  private val regionViewModel: RegionViewModel by viewModels()

  private var mSnackbar: Snackbar? = null
  private var mDialog: MaterialAlertDialogBuilder? = null

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    // initialize for guest user
    userPreferenceViewModel.getUserPref().observe(viewLifecycleOwner) { user ->
      if (user.token != NAN) signOutStateObserver(true) else signOutStateObserver(false)
      setData(user)
      btnAction(user)
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    _binding = FragmentMoreBinding.inflate(inflater, container, false)
    return binding.root
  }

  private fun signOutStateObserver(isLogin: Boolean) {
    if (isLogin) {
      viewLifecycleOwner.lifecycleScope.launch {
        moreUserViewModel.state.collect {
          progressIsVisible(it.isLoading)

          when (it) {
            is UIState.Success -> {
              requireContext().toastShort(getString(sign_out_success))
              openLoginActivity()
            }

            is UIState.Error -> errorSignOut(it.message)
            is UIState.Loading -> Unit
            is UIState.Idle -> Unit
          }
        }
      }
    } else {
      viewLifecycleOwner.lifecycleScope.launch {
        moreLocalViewModel.state.collect { state ->
          progressIsVisible(state.isLoading)

          when (state) {
            is UIState.Success -> {
              requireContext().toastShort(getString(all_data_deleted))
              openLoginActivity()
            }

            is UIState.Error -> errorSignOut(state.message)
            is UIState.Idle -> Unit
            is UIState.Loading -> Unit
          }
        }
      }
    }
  }

  private fun btnAction(user: UserModel) {
    binding.btnFaq.setOnClickListener {
      startActivity(Intent(Intent.ACTION_VIEW, FAQ_LINK.toUri()))
    }
    binding.tvPrivacyPolicy.setOnClickListener {
      startActivity(Intent(Intent.ACTION_VIEW, PRIVACY_POLICY_LINK.toUri()))
    }
    binding.tvTermsConditon.setOnClickListener {
      startActivity(Intent(Intent.ACTION_VIEW, TERMS_CONDITIONS_LINK.toUri()))
    }
    binding.btnSuggestion.setOnClickListener {
      startActivity(Intent(Intent.ACTION_VIEW, FORM_HELPER.toUri()))
    }
    binding.btnAboutUs.setOnClickListener {
      navigator.openAboutActivity(requireContext())
    }

    binding.btnSignout.setOnClickListener {
      if (user.token == NAN) {
        dialogSignOutGuestMode()
      } else {
        dialogSignOutLoggedIn(user.token)
      }
    }
    binding.btnRegion.setOnClickListener { binding.btnCountryPicker.performClick() }
    binding.btnCountryPicker.onCountrySelectedListener = {
      userPreferenceViewModel.saveRegionPref(binding.btnCountryPicker.selectedCountryCode.isoCode)
    }
  }

  private fun dialogSignOutLoggedIn(sessionId: String) {
    mDialog = MaterialAlertDialogBuilder(requireContext(), CustomAlertDialogTheme).apply {
      setTitle(resources.getString(warning))
      setMessage(resources.getString(warning_signOut_logged_user))
      setNegativeButton(resources.getString(no)) { dialog, _ ->
        dialog.dismiss()
      }
      setPositiveButton(resources.getString(yes)) { dialog, _ ->
        fadeInAlpha50(binding.layoutBackground.bgAlpha, ANIM_DURATION)
        btnSignOutIsEnable(false)
        moreUserViewModel.deleteSession(sessionId) // revoke session for login user
        dialog.dismiss()
      }
    }

    mDialog?.show().also { dialog ->
      // Ensure dialog is shown if the activity is not finishing
      if (requireActivity().isFinishing) {
        dialog?.dismiss()
      }
    }
  }

  private fun dialogSignOutGuestMode() {
    mDialog = MaterialAlertDialogBuilder(requireContext(), CustomAlertDialogTheme).apply {
      setTitle(resources.getString(warning))
      setMessage(resources.getString(warning_signOut_guest_mode))
      setNegativeButton(resources.getString(no)) { dialog, _ ->
        dialog.dismiss()
      }
      setPositiveButton(resources.getString(yes)) { dialog, _ ->
        fadeInAlpha50(binding.layoutBackground.bgAlpha, ANIM_DURATION)
        moreLocalViewModel.deleteAll() // delete all user data (watchlistPostModel and favoritePostModel)
        dialog.dismiss()
      }
    }

    mDialog?.show().also { dialog ->
      // Ensure dialog is shown if the activity is not finishing
      if (requireActivity().isFinishing) {
        dialog?.dismiss()
      }
    }
  }

  private fun errorSignOut(message: String) {
    btnSignOutIsEnable(true)
    fadeOut(binding.layoutBackground.bgAlpha, ANIM_DURATION)
    mSnackbar = snackbar.showSnackbarWarning(message)
  }

  private fun openLoginActivity() {
    userPreferenceViewModel.removeUserDataPref()
    navigator.openLoginActivity(requireContext())
    activity?.finishAfterTransition()
  }

  private fun setData(user: UserModel) {
    binding.apply {
      tvFullName.text = user.name
      tvUsername.text = user.username
      val link = if (!user.gravatarHast.isNullOrEmpty()) {
        "$GRAVATAR_LINK${user.gravatarHast}" + ".jpg?s=200"
      } else if (!user.tmdbAvatar.isNullOrEmpty()) {
        "$TMDB_IMG_LINK_AVATAR${user.tmdbAvatar}" + ".png"
      } else {
        GRAVATAR_LINK
      }

      Glide.with(binding.imgAvatar)
        .load(link)
        .transform(CenterCrop())
        .transition(withCrossFade())
        .placeholder(ic_bazz_logo)
        .error(ic_broken_image)
        .into(binding.imgAvatar)
    }

    // check if user already have countryCode
    userPreferenceViewModel.getUserRegionPref().observe(viewLifecycleOwner) { userCountry ->

      if (userCountry == NAN) { // if country not yet initialize, set country
        regionViewModel.getCountryCode()
      } else {
        binding.btnCountryPicker.setCountry(userCountry.uppercase())
      }
    }

    // observe country code
    regionViewModel.countryCode.observe(viewLifecycleOwner) { countryCode ->
      if (countryCode.isNotEmpty()) {
        userPreferenceViewModel.saveRegionPref(countryCode)
        binding.btnCountryPicker.setCountry(countryCode.uppercase())
      }
    }
  }

  private fun btnSignOutIsEnable(isEnable: Boolean) {
    binding.btnSignout.isEnabled = isEnable
  }

  private fun progressIsVisible(isVisible: Boolean) {
    binding.progressBar.isVisible = isVisible
  }

  override fun onResume() {
    super.onResume()
    btnSignOutIsEnable(true)
    progressIsVisible(false)
  }

  override fun onStop() {
    super.onStop()
    mSnackbar?.dismiss()
    mSnackbar = null
    mDialog = null
  }

  override fun onDestroyView() {
    super.onDestroyView()
    mSnackbar?.dismiss()
    mSnackbar = null
    mDialog = null
    moreUserViewModel.removeState()
    Glide.get(requireContext()).clearMemory()
    _binding = null
  }
}
