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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.R.color.red_matte
import com.waffiq.bazz_movies.R.drawable.ic_broken_image
import com.waffiq.bazz_movies.R.font.nunito_sans_regular
import com.waffiq.bazz_movies.R.mipmap.ic_launcher
import com.waffiq.bazz_movies.R.string.all_data_deleted
import com.waffiq.bazz_movies.R.string.binding_error
import com.waffiq.bazz_movies.R.string.no
import com.waffiq.bazz_movies.R.string.sign_out_success
import com.waffiq.bazz_movies.R.string.warning
import com.waffiq.bazz_movies.R.string.warning_signOut_guest_mode
import com.waffiq.bazz_movies.R.string.yes
import com.waffiq.bazz_movies.data.remote.post_body.SessionIDPostModel
import com.waffiq.bazz_movies.databinding.FragmentMoreBinding
import com.waffiq.bazz_movies.ui.activity.AboutActivity
import com.waffiq.bazz_movies.ui.activity.RoutingActivity
import com.waffiq.bazz_movies.ui.viewmodel.AuthenticationViewModel
import com.waffiq.bazz_movies.ui.viewmodel.RegionViewModel
import com.waffiq.bazz_movies.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.ui.viewmodelfactory.ViewModelFactory
import com.waffiq.bazz_movies.ui.viewmodelfactory.ViewModelUserFactory
import com.waffiq.bazz_movies.utils.Helper.showToastShort
import com.waffiq.bazz_movies.utils.Status
import com.waffiq.bazz_movies.utils.common.Constants.FAQ_LINK
import com.waffiq.bazz_movies.utils.common.Constants.FORM_HELPER
import com.waffiq.bazz_movies.utils.common.Constants.GRAVATAR_LINK
import com.waffiq.bazz_movies.utils.common.Constants.PRIVACY_POLICY_LINK
import com.waffiq.bazz_movies.utils.common.Constants.TERMS_CONDITIONS_LINK
import com.waffiq.bazz_movies.utils.common.Constants.TMDB_IMG_LINK_AVATAR
import com.waffiq.bazz_movies.utils.common.Event
import com.waffiq.bazz_movies.utils.result_state.DbResult

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

class MoreFragment : Fragment() {

  private var _binding: FragmentMoreBinding? = null
  private val binding get() = _binding ?: error(getString(binding_error))

  private lateinit var authViewModel: AuthenticationViewModel
  private lateinit var moreViewModelLocal: MoreViewModelLocal
  private lateinit var moreViewModelUser: MoreViewModelUser
  private lateinit var userPreferenceViewModel: UserPreferenceViewModel
  private lateinit var regionViewModel: RegionViewModel

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentMoreBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val pref = requireContext().dataStore
    val factory = ViewModelUserFactory.getInstance(pref)
    authViewModel = ViewModelProvider(this, factory)[AuthenticationViewModel::class.java]
    moreViewModelUser = ViewModelProvider(this, factory)[MoreViewModelUser::class.java]
    userPreferenceViewModel = ViewModelProvider(this, factory)[UserPreferenceViewModel::class.java]
    regionViewModel = ViewModelProvider(this, factory)[RegionViewModel::class.java]


    moreViewModelUser.errorState.observe(viewLifecycleOwner) { showSnackBar(it) }

    val factory2 = ViewModelFactory.getInstance(requireContext())
    moreViewModelLocal = ViewModelProvider(this, factory2)[MoreViewModelLocal::class.java]

    // hide action bar
    (activity as AppCompatActivity).supportActionBar?.hide()

    setTypeface()
    setData()
    btnAction()
    signOutStateObserver()
    return root
  }

  private fun signOutStateObserver() {
    moreViewModelUser.signOutState.observe(viewLifecycleOwner) { result ->
      when (result.status) {
        Status.SUCCESS -> {
          if (result.data?.success == true) {
            showToastShort(requireContext(), getString(sign_out_success))
            removePrefUserData() // remove preference user data
          }
        }

        Status.LOADING -> {}
        Status.ERROR -> {
          showSnackBar(Event(result.message.toString()))
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
        // sign out for guest account
        if (user.token == "NaN" || user.token.isEmpty()) {
          dialogSignOutGuestMode()
        } else { // sign out for login account
          moreViewModelUser.deleteSession(SessionIDPostModel(user.token)) // revoke session for login user
        }
      }
    }
    binding.btnRegion.setOnClickListener { binding.btnCountryPicker.performClick() }
    binding.btnCountryPicker.setOnCountryChangeListener {
      userPreferenceViewModel.saveRegionPref(binding.btnCountryPicker.selectedCountryNameCode)
    }
  }

  private fun dialogSignOutGuestMode() {
    moreViewModelLocal.dbResult.observe(viewLifecycleOwner) { eventResult ->
      eventResult.getContentIfNotHandled().let {
        when (it) {
          is DbResult.Success -> showToastShort(
            requireActivity(),
            getString(all_data_deleted)
          )

          is DbResult.Error -> showToastShort(requireActivity(), it.errorMessage)
          else -> {}
        }
      }
    }

    val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
    builder
      .setMessage(getString(warning_signOut_guest_mode))
      .setTitle(getString(warning))
      .setPositiveButton(getString(yes)) { dialog, _ ->
        moreViewModelLocal.deleteAll() // delete all user data  (watchlistPostModel and favoritePostModel)
        dialog.dismiss()
        removePrefUserData() // remove preference user data
      }
      .setNegativeButton(getString(no)) { dialog, _ ->
        dialog.dismiss()
        dialog.cancel()
      }

    val dialog: AlertDialog = builder.create()
    if (!requireActivity().isFinishing) dialog.show()
    else dialog.cancel()
  }

  private fun removePrefUserData() {
    userPreferenceViewModel.removeUserDataPref()
    startActivity(Intent(activity, RoutingActivity::class.java))
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
        } else GRAVATAR_LINK

        Glide.with(binding.imgAvatar)
          .load(link)
          .transform(CenterCrop())
          .transition(withCrossFade())
          .placeholder(ic_launcher)
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
      } else binding.btnCountryPicker.setCountryForNameCode(userCountry)
    }
  }

  private fun showSnackBar(eventMessage: Event<String>) {
    val message = eventMessage.getContentIfNotHandled() ?: return
    val snackBar = Snackbar.make(
      binding.constraintLayout,
      message,
      Snackbar.LENGTH_SHORT
    ).setAnchorView(binding.guideSnackbar)

    val snackbarView = snackBar.view
    snackbarView.setBackgroundColor(ContextCompat.getColor(requireActivity(), red_matte))
    snackBar.show()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}