@file:Suppress("BackingPropertyNaming")

package com.waffiq.bazz_movies.feature.more.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.VisibleForTesting
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.listitem.ListItemLayout
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
import com.waffiq.bazz_movies.core.designsystem.R.string.sign_out_success
import com.waffiq.bazz_movies.core.designsystem.R.string.user_no_name
import com.waffiq.bazz_movies.core.models.UserModel
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.core.uihelper.state.UIState
import com.waffiq.bazz_movies.core.uihelper.state.isLoading
import com.waffiq.bazz_movies.core.uihelper.utils.Animation.fadeInAlpha50
import com.waffiq.bazz_movies.core.uihelper.utils.Animation.fadeOut
import com.waffiq.bazz_movies.core.uihelper.utils.SnackBarManager.toastShort
import com.waffiq.bazz_movies.core.user.ui.viewmodel.RegionViewModel
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.feature.more.databinding.FragmentMoreBinding
import com.waffiq.bazz_movies.feature.more.ui.viewmodel.MoreLocalViewModel
import com.waffiq.bazz_movies.feature.more.ui.viewmodel.MoreUserViewModel
import com.waffiq.bazz_movies.feature.more.utils.Helper.validName
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("ClickableViewAccessibility")
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

  private val backupLauncher =
    registerForActivityResult(
      ActivityResultContracts.CreateDocument("application/octet-stream"),
    ) { uri ->
      uri?.let { moreLocalViewModel.backupDatabase(it) }
    }

  private val restoreLauncher =
    registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
      uri?.let { handleRestoreUri(it) }
    }

  @VisibleForTesting
  internal fun handleRestoreUri(uri: Uri) {
    MoreDialogManager(
      context = requireContext(),
      onSignOutLoggedIn = {},
      onSignOutGuest = {},
      onRestoreConfirmed = { moreLocalViewModel.restoreDatabase(it) },
    ).showConfirmRestore(uri)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    // initialize user
    userPreferenceViewModel.getUserPref().observe(viewLifecycleOwner) { user ->
      signOutStateObserver(user.token != NAN)
      setData(user)
      btnAction(user)
      setupBackupViews(user.token != NAN)
    }
    updateListItemAppearance()

    MoreStateObserver(
      lifecycleScope = viewLifecycleOwner.lifecycleScope,
      viewModel = moreLocalViewModel,
      onProgress = { progressIsVisible(it) },
      onSuccess = { requireContext().toastShort(getString(it)) },
      onError = { mSnackbar = snackbar.showSnackbarWarning(it) },
    ).apply {
      observeBackup()
      observeRestore()
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
              moreLocalViewModel.deleteAllSearchHistory()
              requireContext().toastShort(getString(sign_out_success))
              openLoginActivity()
            }

            is UIState.Error -> errorSignOut(it.message)

            is UIState.Loading -> Unit

            else -> Unit // idle do nothing
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

            else -> Unit // idle do nothing
          }
        }
      }
    }
  }

  private fun setupBackupViews(isLogin: Boolean) {
    binding.backupLayout.isVisible = !isLogin
  }

  private fun btnAction(user: UserModel) {
    setupRegion()
    setupButtons()
    setupBackupActions()
    setupSignOut(user)
  }

  private fun setupRegion() {
    binding.apply {
      bindCardAction(btnRegion, cardRegion) { btnCountryPicker.performClick() }

      btnCountryPicker.onCountrySelectedListener = {
        userPreferenceViewModel.saveRegionPref(
          btnCountryPicker.selectedCountryCode.isoCode,
        )
      }
    }
  }

  private fun setupButtons() {
    binding.apply {
      bindCardAction(btnFaq, cardFaq) { openUrl(FAQ_LINK) }
      bindCardAction(btnSuggestion, cardSuggestion) { openUrl(FORM_HELPER) }
      bindCardAction(btnAboutUs, cardAboutUs) { navigator.openAboutActivity(requireContext()) }
      bindCardAction(btnPrivacyPolicy, cardPrivacyPolicy) { openUrl(PRIVACY_POLICY_LINK) }
      bindCardAction(btnTermsCondition, cardTermsCondition) { openUrl(TERMS_CONDITIONS_LINK) }
    }
  }

  private fun setupBackupActions() {
    binding.apply {
      bindCardAction(btnBackup, cardBackup) { backupLauncher.launch("bazz_movies_backup.json") }
      bindCardAction(btnRestore, cardRestore) { restoreLauncher.launch(arrayOf("*/*")) }
    }
  }

  private fun setupSignOut(user: UserModel) {
    binding.btnSignout.setOnClickListener {
      val dialogManager = MoreDialogManager(
        context = requireContext(),
        onSignOutLoggedIn = { sessionId ->
          fadeInAlpha50(binding.layoutBackground.bgAlpha, ANIM_DURATION)
          btnSignOutIsEnable(false)
          moreUserViewModel.deleteSession(sessionId)
        },
        onSignOutGuest = {
          fadeInAlpha50(binding.layoutBackground.bgAlpha, ANIM_DURATION)
          moreLocalViewModel.deleteAllSearchHistory()
          moreLocalViewModel.deleteAll()
        },
        onRestoreConfirmed = {},
      )

      if (user.token == NAN) {
        dialogManager.showSignOutGuestMode()
      } else {
        dialogManager.showSignOutLoggedIn(user.token)
      }
    }
  }

  private fun updateListItemAppearance() {
    binding.apply {
      itemRegion.updateAppearance(ListItemLayout.POSITION_FIRST)
      itemFaq.updateAppearance(ListItemLayout.POSITION_MIDDLE)
      itemSuggestion.updateAppearance(ListItemLayout.POSITION_MIDDLE)
      itemAboutUs.updateAppearance(ListItemLayout.POSITION_LAST)

      itemBackup.updateAppearance(ListItemLayout.POSITION_FIRST)
      itemRestore.updateAppearance(ListItemLayout.POSITION_LAST)

      itemTermsCondition.updateAppearance(ListItemLayout.POSITION_FIRST)
      itemPrivacyPolicy.updateAppearance(ListItemLayout.POSITION_LAST)
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
      tvFullName.text = user.name.validName(getString(user_no_name))
      tvUsername.text = user.username
      val link = if (!user.gravatarHash.isNullOrEmpty()) {
        "$GRAVATAR_LINK${user.gravatarHash}" + ".jpg?s=200"
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
    userPreferenceViewModel.getUserRegionPref().observe(
      viewLifecycleOwner,
    ) { userCountry ->

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

  private fun bindCardAction(
    button: View,
    card: View,
    onClick: () -> Unit,
  ) {
    button.setOnClickListener { onClick() }

    button.setOnTouchListener { _, event ->
      card.onTouchEvent(event)
      false
    }
  }

  private fun openUrl(url: String) {
    startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
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
