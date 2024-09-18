package com.waffiq.bazz_movies.ui.activity.more

import android.app.AlertDialog
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
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.R.anim.fade_in
import com.waffiq.bazz_movies.R.anim.fade_out
import com.waffiq.bazz_movies.R.drawable.ic_bazz_logo
import com.waffiq.bazz_movies.R.drawable.ic_broken_image
import com.waffiq.bazz_movies.R.font.nunito_sans_regular
import com.waffiq.bazz_movies.R.id.nav_view
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
import com.waffiq.bazz_movies.ui.viewmodel.AuthenticationViewModel
import com.waffiq.bazz_movies.ui.viewmodel.RegionViewModel
import com.waffiq.bazz_movies.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.ui.viewmodelfactory.ViewModelFactory
import com.waffiq.bazz_movies.ui.viewmodelfactory.ViewModelUserFactory
import com.waffiq.bazz_movies.utils.Helper.showToastShort
import com.waffiq.bazz_movies.utils.common.Constants.ANIM_DURATION
import com.waffiq.bazz_movies.utils.common.Constants.FAQ_LINK
import com.waffiq.bazz_movies.utils.common.Constants.FORM_HELPER
import com.waffiq.bazz_movies.utils.common.Constants.GRAVATAR_LINK
import com.waffiq.bazz_movies.utils.common.Constants.PRIVACY_POLICY_LINK
import com.waffiq.bazz_movies.utils.common.Constants.TERMS_CONDITIONS_LINK
import com.waffiq.bazz_movies.utils.common.Constants.TMDB_IMG_LINK_AVATAR
import com.waffiq.bazz_movies.utils.common.Event
import com.waffiq.bazz_movies.utils.helpers.SnackBarManager.snackBarWarning
import com.waffiq.bazz_movies.utils.resultstate.DbResult
import com.waffiq.bazz_movies.utils.resultstate.Status
import com.waffiq.bazz_movies.utils.uihelpers.Animation.fadeInAlpha50
import com.waffiq.bazz_movies.utils.uihelpers.Animation.fadeOut

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

class MoreFragment : Fragment() {

  private var _binding: FragmentMoreBinding? = null
  private val binding get() = _binding ?: error(getString(binding_error))

  private lateinit var authViewModel: AuthenticationViewModel // logged-in user
  private lateinit var moreViewModelLocal: MoreViewModelLocal // guest user
  private lateinit var moreViewModelUser: MoreViewModelUser
  private lateinit var userPreferenceViewModel: UserPreferenceViewModel
  private lateinit var regionViewModel: RegionViewModel

  private var mSnackbar: Snackbar? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val pref = requireContext().dataStore
    val factory = ViewModelUserFactory.getInstance(pref)
    authViewModel = ViewModelProvider(this, factory)[AuthenticationViewModel::class.java]
    moreViewModelUser = ViewModelProvider(this, factory)[MoreViewModelUser::class.java]
    userPreferenceViewModel = ViewModelProvider(this, factory)[UserPreferenceViewModel::class.java]
    regionViewModel = ViewModelProvider(this, factory)[RegionViewModel::class.java]
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    // initialize for guest user
    userPreferenceViewModel.getUserPref().observe(viewLifecycleOwner) { user ->
      if (user.token == "NaN" || user.token.isEmpty()) {
        val factory = ViewModelFactory.getInstance(requireContext())
        moreViewModelLocal = ViewModelProvider(this, factory)[MoreViewModelLocal::class.java]
      } else {
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
    moreViewModelUser.signOutState.observe(viewLifecycleOwner) { result ->
      when (result.status) {
        Status.SUCCESS -> {
          progressIsVisible(false)
          if (result.data?.success == true) {
            showToastShort(requireContext(), getString(sign_out_success))
            removePrefUserData() // remove preference user data
          }
        }

        Status.LOADING -> btnSignOutIsEnable(false)
        Status.ERROR -> {
          fadeOut(binding.layoutBackground.bgAlpha, ANIM_DURATION)
          btnSignOutIsEnable(true)
          progressIsVisible(false)
          mSnackbar = snackBarWarning(
            requireContext(),
            binding.constraintLayout,
            requireActivity().findViewById(nav_view),
            Event(result.message.toString())
          )
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
        if (user.token == "NaN" || user.token.isEmpty()) {
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
    val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
    builder
      .setMessage(getString(warning_signOut_logged_user))
      .setTitle(getString(warning))
      .setPositiveButton(getString(yes)) { dialog, _ ->
        fadeInAlpha50(binding.layoutBackground.bgAlpha, ANIM_DURATION)
        btnSignOutIsEnable(false)
        progressIsVisible(true)
        moreViewModelUser.deleteSession(SessionIDPostModel(token)) // revoke session for login user
        dialog.dismiss()
      }
      .setNegativeButton(getString(no)) { dialog, _ ->
        dialog.dismiss()
        dialog.cancel()
      }

    val dialog: AlertDialog = builder.create()
    if (!requireActivity().isFinishing) {
      dialog.show()
    } else {
      dialog.cancel()
    }
  }

  private fun dialogSignOutGuestMode() {
    moreViewModelLocal.dbResult.observe(viewLifecycleOwner) { eventResult ->
      eventResult.getContentIfNotHandled().let {
        when (it) {
          is DbResult.Success -> {
            progressIsVisible(false)
            showToastShort(
              requireActivity(),
              getString(all_data_deleted)
            )
          }

          is DbResult.Error -> {
            progressIsVisible(false)
            mSnackbar = snackBarWarning(
              requireContext(),
              binding.constraintLayout,
              requireActivity().findViewById(nav_view),
              Event(it.errorMessage)
            )
          }

          else -> {}
        }
      }
    }

    val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
    builder
      .setMessage(getString(warning_signOut_guest_mode))
      .setTitle(getString(warning))
      .setPositiveButton(getString(yes)) { dialog, _ ->
        fadeInAlpha50(binding.layoutBackground.bgAlpha, ANIM_DURATION)
        moreViewModelLocal.deleteAll() // delete all user data  (watchlistPostModel and favoritePostModel)
        dialog.dismiss()
        removePrefUserData() // remove preference user data
      }
      .setNegativeButton(getString(no)) { dialog, _ ->
        dialog.dismiss()
        dialog.cancel()
      }

    val dialog: AlertDialog = builder.create()
    if (!requireActivity().isFinishing) {
      dialog.show()
    } else {
      dialog.cancel()
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

      if (userCountry.equals("NaN")) { // if not yet, then set country
        regionViewModel.getCountryCode()
        regionViewModel.countryCode.observe(viewLifecycleOwner) { countryCode ->

          if (countryCode.isNotEmpty()) {
            userPreferenceViewModel.saveRegionPref(countryCode)
            binding.btnCountryPicker.setCountryForNameCode(countryCode)
          }
        }
      } else {
        binding.btnCountryPicker.setCountryForNameCode(userCountry)
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

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
    mSnackbar?.dismiss()
    moreViewModelUser.removeState()
    Glide.get(requireContext()).clearMemory()
  }
}
