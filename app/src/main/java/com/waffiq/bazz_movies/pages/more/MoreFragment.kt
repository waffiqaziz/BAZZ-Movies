package com.waffiq.bazz_movies.pages.more

import android.R.anim.fade_in
import android.R.anim.fade_out
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core_ui.R.drawable.ic_bazz_logo
import com.waffiq.bazz_movies.core_ui.R.drawable.ic_broken_image
import com.waffiq.bazz_movies.R.id.bottom_navigation
import com.waffiq.bazz_movies.core.data.remote.post_body.SessionIDPostModel
import com.waffiq.bazz_movies.core.utils.common.Constants.ANIM_DURATION
import com.waffiq.bazz_movies.core.utils.common.Constants.DEBOUNCE_VERY_LONG
import com.waffiq.bazz_movies.core.utils.common.Constants.FAQ_LINK
import com.waffiq.bazz_movies.core.utils.common.Constants.FORM_HELPER
import com.waffiq.bazz_movies.core.utils.common.Constants.GRAVATAR_LINK
import com.waffiq.bazz_movies.core.utils.common.Constants.NAN
import com.waffiq.bazz_movies.core.utils.common.Constants.PRIVACY_POLICY_LINK
import com.waffiq.bazz_movies.core.utils.common.Constants.TERMS_CONDITIONS_LINK
import com.waffiq.bazz_movies.core.utils.common.Constants.TMDB_IMG_LINK_AVATAR
import com.waffiq.bazz_movies.core.utils.common.Event
import com.waffiq.bazz_movies.core.utils.helpers.GeneralHelper.toastShort
import com.waffiq.bazz_movies.core.utils.helpers.uihelpers.Animation.fadeInAlpha50
import com.waffiq.bazz_movies.core.utils.helpers.uihelpers.Animation.fadeOut
import com.waffiq.bazz_movies.core.utils.helpers.uihelpers.SnackBarManager.snackBarWarning
import com.waffiq.bazz_movies.core.utils.result.DbResult
import com.waffiq.bazz_movies.core.utils.result.NetworkResult
import com.waffiq.bazz_movies.core_ui.R.string.all_data_deleted
import com.waffiq.bazz_movies.core_ui.R.string.binding_error
import com.waffiq.bazz_movies.core_ui.R.string.no
import com.waffiq.bazz_movies.core_ui.R.string.sign_out_success
import com.waffiq.bazz_movies.core_ui.R.string.warning
import com.waffiq.bazz_movies.core_ui.R.string.warning_signOut_guest_mode
import com.waffiq.bazz_movies.core_ui.R.string.warning_signOut_logged_user
import com.waffiq.bazz_movies.core_ui.R.string.yes
import com.waffiq.bazz_movies.core_ui.R.style.CustomAlertDialogTheme
import com.waffiq.bazz_movies.databinding.FragmentMoreBinding
import com.waffiq.bazz_movies.pages.AboutActivity
import com.waffiq.bazz_movies.pages.LoginActivity
import com.waffiq.bazz_movies.viewmodel.RegionViewModel
import com.waffiq.bazz_movies.viewmodel.UserPreferenceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoreFragment : Fragment() {

  private var _binding: FragmentMoreBinding? = null
  private val binding get() = _binding ?: error(getString(binding_error))

  private val moreLocalViewModel: MoreLocalViewModel by viewModels()
  private val moreUserViewModel: MoreUserViewModel by viewModels()
  private val userPreferenceViewModel: UserPreferenceViewModel by viewModels()
  private val regionViewModel: RegionViewModel by viewModels()

  private var mSnackbar: Snackbar? = null
  private var mDialog: MaterialAlertDialogBuilder? = null

  private var isCancelSignout = false

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    // initialize for guest user
    userPreferenceViewModel.getUserPref().observe(viewLifecycleOwner) { user ->
      if (user.token != NAN) signOutStateObserver(true) else signOutStateObserver(false)
    }

    setData()
    btnAction()
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentMoreBinding.inflate(inflater, container, false)
    return binding.root
  }

  private fun signOutStateObserver(isLogin: Boolean) {
    if (isLogin) {
      viewLifecycleOwner.lifecycleScope.launch {
        @OptIn(FlowPreview::class)
        moreUserViewModel.signOutState.debounce(DEBOUNCE_VERY_LONG).collectLatest { networkResult ->
          when (networkResult) {
            is NetworkResult.Success -> {
              progressIsVisible(false)
              requireContext().toastShort(getString(sign_out_success))
              removePrefUserData() // remove preference user data
            }

            is NetworkResult.Loading -> {}

            is NetworkResult.Error -> {
              fadeOut(binding.layoutBackground.bgAlpha, ANIM_DURATION)
              btnSignOutIsEnable(true)
              progressIsVisible(false)
              mSnackbar = snackBarWarning(
                binding.constraintLayout,
                requireActivity().findViewById(bottom_navigation),
                Event(networkResult.message)
              )
            }

            else -> {}
          }
        }
      }
    } else {
      moreLocalViewModel.dbResult.observe(viewLifecycleOwner) { eventResult ->
        eventResult.getContentIfNotHandled().let {
          when (it) {
            is DbResult.Success -> {
              progressIsVisible(false)
              requireContext().toastShort(getString(all_data_deleted))
            }

            is DbResult.Error -> {
              progressIsVisible(false)
              mSnackbar = snackBarWarning(
                binding.constraintLayout,
                requireActivity().findViewById(bottom_navigation),
                Event(it.errorMessage)
              )
            }

            else -> {}
          }
        }
      }
    }
  }

  private fun btnAction() {
    binding.btnFaq.setOnClickListener {
      startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(FAQ_LINK)))
    }
    binding.tvPrivacyPolicy.setOnClickListener {
      startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(PRIVACY_POLICY_LINK)))
    }
    binding.tvTermsConditon.setOnClickListener {
      startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(TERMS_CONDITIONS_LINK)))
    }
    binding.btnSuggestion.setOnClickListener {
      startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(FORM_HELPER)))
    }
    binding.btnAboutUs.setOnClickListener {
      activity?.let {
        it.startActivity(Intent(it, AboutActivity::class.java))
      }
    }

    binding.btnSignout.setOnClickListener {
      isCancelSignout = false
      userPreferenceViewModel.getUserPref().observe(viewLifecycleOwner) { user ->
        if (!isCancelSignout) {
          if (user.token == NAN) {
            dialogSignOutGuestMode()
          } else {
            dialogSignOutLoggedIn(user.token)
          }
        }
      }
    }
    binding.btnRegion.setOnClickListener { binding.btnCountryPicker.performClick() }
    binding.btnCountryPicker.onCountrySelectedListener = {
      userPreferenceViewModel.saveRegionPref(binding.btnCountryPicker.selectedCountryCode.isoCode)
    }
  }

  private fun dialogSignOutLoggedIn(token: String) {

    mDialog = MaterialAlertDialogBuilder(requireContext(), CustomAlertDialogTheme).apply {
      setTitle(resources.getString(warning))
      setMessage(resources.getString(warning_signOut_logged_user))
      setNegativeButton(
        HtmlCompat.fromHtml(
          "<b>${resources.getString(no)}</b>",
          HtmlCompat.FROM_HTML_MODE_LEGACY
        )
      ) { dialog, _ ->
        isCancelSignout = !isCancelSignout
        dialog.dismiss()
      }
      setPositiveButton(resources.getString(yes)) { dialog, _ ->
        fadeInAlpha50(binding.layoutBackground.bgAlpha, ANIM_DURATION)
        btnSignOutIsEnable(false)
        progressIsVisible(true)
        moreUserViewModel.deleteSession(SessionIDPostModel(token)) // revoke session for login user
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
      setNegativeButton(
        HtmlCompat.fromHtml(
          "<b>${resources.getString(no)}</b>",
          HtmlCompat.FROM_HTML_MODE_LEGACY
        )
      ) { dialog, _ ->
        isCancelSignout = !isCancelSignout
        dialog.dismiss()
      }
      setPositiveButton(resources.getString(yes)) { dialog, _ ->
        fadeInAlpha50(binding.layoutBackground.bgAlpha, ANIM_DURATION)
        moreLocalViewModel.deleteAll() // delete all user data (watchlistPostModel and favoritePostModel)
        dialog.dismiss()
        removePrefUserData() // remove preference user data
      }
    }

    mDialog?.show().also { dialog ->
      // Ensure dialog is shown if the activity is not finishing
      if (requireActivity().isFinishing) {
        dialog?.dismiss()
      }
    }
  }

  private fun removePrefUserData() {
    userPreferenceViewModel.removeUserDataPref()
    ActivityCompat.startActivity(
      requireContext(),
      Intent(activity, LoginActivity::class.java),
      ActivityOptionsCompat.makeCustomAnimation(requireContext(), fade_in, fade_out).toBundle()
    )
    activity?.finishAfterTransition()
  }

  private fun setData() {
    userPreferenceViewModel.getUserPref().observe(viewLifecycleOwner) {
      binding.apply {
        tvFullName.text = it.name
        tvUsername.text = it.username
        val link = if (!it.gravatarHast.isNullOrEmpty()) {
          "$GRAVATAR_LINK${it.gravatarHast}" + ".jpg?s=200"
        } else if (!it.tmdbAvatar.isNullOrEmpty()) {
          "$TMDB_IMG_LINK_AVATAR${it.tmdbAvatar}" + ".png"
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
