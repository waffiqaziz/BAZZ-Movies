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
import androidx.core.content.res.ResourcesCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.R
import com.waffiq.bazz_movies.databinding.FragmentMoreBinding
import com.waffiq.bazz_movies.ui.activity.AboutActivity
import com.waffiq.bazz_movies.ui.activity.SplashScreenActivity
import com.waffiq.bazz_movies.ui.viewmodel.AuthenticationViewModel
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelFactory
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelUserFactory
import com.waffiq.bazz_movies.utils.Constants
import com.waffiq.bazz_movies.utils.Constants.GRAVATAR_LINK
import com.waffiq.bazz_movies.utils.Event
import com.waffiq.bazz_movies.utils.Helper.showToastShort
import com.waffiq.bazz_movies.utils.Helper.toastStillOnDevelopment

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

class MoreFragment : Fragment() {

  private var _binding: FragmentMoreBinding? = null
  private val binding get() = _binding!!

  private lateinit var authViewModel: AuthenticationViewModel
  private lateinit var moreViewModel: MoreViewModel
  private lateinit var moreViewModelUser: MoreViewModelUser

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
    moreViewModelUser.getSnackBarText().observe(viewLifecycleOwner) { showSnackBar(it) }

    val factory2 = ViewModelFactory.getInstance(requireContext())
    moreViewModel = ViewModelProvider(this, factory2)[MoreViewModel::class.java]

    // hide action bar
    (activity as AppCompatActivity).supportActionBar?.hide()

    setTypeface()
    setData()
    btnAction()
    return root
  }

  private fun setTypeface() {
    val typeFace = ResourcesCompat.getFont(requireContext(), R.font.gothic) as Typeface
    binding.btnCountryPicker.setTypeFace(typeFace)
  }

  private fun btnAction() {
    binding.btnRate.setOnClickListener { toastStillOnDevelopment(requireContext()) }
    binding.btnLanguage.setOnClickListener { toastStillOnDevelopment(requireContext()) }
    binding.btnDarkMode.setOnClickListener {
      binding.cbDarkMode.isChecked = !binding.cbDarkMode.isChecked
      toastStillOnDevelopment(requireContext())
    }
    binding.btnFaq.setOnClickListener {
      startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Constants.FAQ_LINK)))
    }
    binding.tvPrivacyPolicy.setOnClickListener {
      startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Constants.PRIVACY_POLICY_LINK)))
    }
    binding.tvTermsConditon.setOnClickListener {
      startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Constants.TERMS_CONDITIONS_LINK)))
    }
    binding.btnSuggestion.setOnClickListener {
      startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Constants.FORM_HELPER)))
    }
    binding.btnAboutUs.setOnClickListener {
      activity?.let {
        it.startActivity(Intent(it, AboutActivity::class.java))
      }
    }

    binding.btnSignout.setOnClickListener {
      authViewModel.getUser().observe(viewLifecycleOwner) { user ->
        // sign out for guest account
        if (user.token == "NaN" || user.token.isEmpty()) dialogSignOutGuestMode()
        else { // sign out for login account
          removeAllUserData()
          showToastShort(requireContext(), getString(R.string.sign_out_success))
        }
      }
    }
    binding.btnRegion.setOnClickListener { binding.btnCountryPicker.performClick() }
    binding.btnCountryPicker.setOnCountryChangeListener {
      moreViewModelUser.saveUserRegion(binding.btnCountryPicker.selectedCountryNameCode)
    }
  }

  private fun dialogSignOutGuestMode() {
    val builder: AlertDialog.Builder = AlertDialog.Builder(context)
    builder
      .setMessage(getString(R.string.warning_signOut_guest_mode))
      .setTitle(getString(R.string.warning))
      .setPositiveButton(getString(R.string.yes)) { _, _ ->
        removeAllUserData()
        showToastShort(requireContext(), getString(R.string.all_data_deleted))
        moreViewModel.deleteAll() // delete all data
      }
      .setNegativeButton(getString(R.string.no)) { dialog, _ ->
        dialog.dismiss()
      }

    val dialog: AlertDialog = builder.create()
    dialog.show()
  }

  private fun removeAllUserData() {
    authViewModel.removeUserData()
    activity?.finish()
    startActivity(Intent(activity, SplashScreenActivity::class.java))
  }

  private fun setData() {

    authViewModel.getUser().observe(viewLifecycleOwner) {
      binding.apply {
        tvFullName.text = it.name
        tvUsername.text = it.username

        Glide.with(binding.imgAvatar)
          .load("$GRAVATAR_LINK${it.gravatarHast}" + ".jpg?s=200")
          .placeholder(R.mipmap.ic_launcher)
          .error(R.drawable.ic_broken_image)
          .into(binding.imgAvatar)
      }


    }

    // check if user already have countryCode
    moreViewModelUser.getUserRegion().observe(viewLifecycleOwner) { userCountry ->

      if (userCountry.equals("NaN")) { // if not yet, then set country
        moreViewModelUser.getCountryCode()
        moreViewModelUser.countryCode().observe(viewLifecycleOwner) { countryCode ->

          if (countryCode.isNotEmpty()) {
            moreViewModelUser.saveUserRegion(countryCode)
            binding.btnCountryPicker.setCountryForNameCode(countryCode)
          }
        }
      } else binding.btnCountryPicker.setCountryForNameCode(userCountry)
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

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}