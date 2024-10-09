package com.waffiq.bazz_movies.ui.activity.more

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.MyApplication
import com.waffiq.bazz_movies.R.anim.fade_in
import com.waffiq.bazz_movies.R.anim.fade_out
import com.waffiq.bazz_movies.R.drawable.ic_bazz_logo
import com.waffiq.bazz_movies.R.drawable.ic_broken_image
import com.waffiq.bazz_movies.R.font.nunito_sans_regular
import com.waffiq.bazz_movies.R.id.bottom_navigation
import com.waffiq.bazz_movies.R.string.all_data_deleted
import com.waffiq.bazz_movies.R.string.binding_error
import com.waffiq.bazz_movies.R.string.no
import com.waffiq.bazz_movies.R.string.sign_out_success
import com.waffiq.bazz_movies.R.string.warning
import com.waffiq.bazz_movies.R.string.warning_signOut_guest_mode
import com.waffiq.bazz_movies.R.string.warning_signOut_logged_user
import com.waffiq.bazz_movies.R.string.yes
import com.waffiq.bazz_movies.data.remote.post_body.SessionIDPostModel
import com.waffiq.bazz_movies.databinding.FragmentMoreBinding
import com.waffiq.bazz_movies.ui.activity.AboutActivity
import com.waffiq.bazz_movies.ui.activity.LoginActivity
import com.waffiq.bazz_movies.ui.viewmodel.RegionViewModel
import com.waffiq.bazz_movies.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.ui.viewmodelfactory.ViewModelFactory
import com.waffiq.bazz_movies.ui.viewmodelfactory.ViewModelUserFactory
import com.waffiq.bazz_movies.utils.Helper.toastShort
import com.waffiq.bazz_movies.utils.common.Constants.ANIM_DURATION
import com.waffiq.bazz_movies.utils.common.Constants.DEBOUNCE_VERY_LONG
import com.waffiq.bazz_movies.utils.common.Constants.FAQ_LINK
import com.waffiq.bazz_movies.utils.common.Constants.FORM_HELPER
import com.waffiq.bazz_movies.utils.common.Constants.GRAVATAR_LINK
import com.waffiq.bazz_movies.utils.common.Constants.NAN
import com.waffiq.bazz_movies.utils.common.Constants.PRIVACY_POLICY_LINK
import com.waffiq.bazz_movies.utils.common.Constants.TERMS_CONDITIONS_LINK
import com.waffiq.bazz_movies.utils.common.Constants.TMDB_IMG_LINK_AVATAR
import com.waffiq.bazz_movies.utils.common.Event
import com.waffiq.bazz_movies.utils.helpers.SnackBarManager.snackBarWarning
import com.waffiq.bazz_movies.utils.resultstate.DbResult
import com.waffiq.bazz_movies.utils.resultstate.NetworkResult
import com.waffiq.bazz_movies.utils.uihelpers.Animation.fadeInAlpha50
import com.waffiq.bazz_movies.utils.uihelpers.Animation.fadeOut
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject


class MoreFragment : Fragment() {

  @Inject
  lateinit var factory: ViewModelFactory

  @Inject
  lateinit var factoryUser: ViewModelUserFactory

  private var _binding: FragmentMoreBinding? = null
  private val binding get() = _binding ?: error(getString(binding_error))

  private val moreLocalViewModel: MoreLocalViewModel by viewModels { factory }
  private val moreUserViewModel: MoreUserViewModel by viewModels { factoryUser }
  private val userPreferenceViewModel: UserPreferenceViewModel by viewModels { factoryUser }
  private val regionViewModel: RegionViewModel by viewModels { factoryUser }

  private var mSnackbar: Snackbar? = null
  private var mDialog: MaterialAlertDialogBuilder? = null

  override fun onAttach(context: Context) {
    super.onAttach(context)
    (requireActivity().application as MyApplication).appComponent.inject(this)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    // initialize for guest user
    userPreferenceViewModel.getUserPref().observe(viewLifecycleOwner) { user ->
      if (user.token != NAN) {
        signOutStateObserver()
      }
    }

    setTypeface()
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

  private fun signOutStateObserver() {
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
  }

  private fun setTypeface() {
    val typeFace = ResourcesCompat.getFont(requireContext(), nunito_sans_regular) as Typeface
    binding.btnCountryPicker.setTypeFace(typeFace)
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
      userPreferenceViewModel.getUserPref().observe(viewLifecycleOwner) { user ->
        if (user.token == NAN) {
          dialogSignOutGuestMode()
        } else {
          dialogSignOutLoggedIn(user.token)
        }
      }
    }
    binding.btnRegion.setOnClickListener { binding.btnCountryPicker.performClick() }
    binding.btnCountryPicker.setOnCountryChangeListener {
      userPreferenceViewModel.saveRegionPref(binding.btnCountryPicker.selectedCountryNameCode)
    }
  }

  private fun dialogSignOutLoggedIn(token: String) {
    mDialog = MaterialAlertDialogBuilder(requireContext()).apply {
      setTitle(resources.getString(warning))
      setMessage(resources.getString(warning_signOut_logged_user))
      setNegativeButton(resources.getString(no)) { dialog, _ ->
        dialog.dismiss()
        dialog.cancel()
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
        dialog?.cancel()
      }
    }
  }

  private fun dialogSignOutGuestMode() {
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

    mDialog = MaterialAlertDialogBuilder(requireContext()).apply {
      setTitle(resources.getString(warning))
      setMessage(resources.getString(warning_signOut_guest_mode))
      setNegativeButton(resources.getString(no)) { dialog, _ ->
        dialog.dismiss()
        dialog.cancel()
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
        dialog?.cancel()
      }
    }
  }

  private fun removePrefUserData() {
    userPreferenceViewModel.removeUserDataPref()
    val intent = Intent(activity, LoginActivity::class.java)
    val options =
      ActivityOptionsCompat.makeCustomAnimation(requireContext(), fade_in, fade_out)
    ActivityCompat.startActivity(requireContext(), intent, options.toBundle())
    activity?.finishAffinity()
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
        binding.btnCountryPicker.setCountryForNameCode(userCountry)
      }
    }

    // observe country code
    regionViewModel.countryCode.observe(viewLifecycleOwner) { countryCode ->
      if (countryCode.isNotEmpty()) {
        userPreferenceViewModel.saveRegionPref(countryCode)
        binding.btnCountryPicker.setCountryForNameCode(countryCode)
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
