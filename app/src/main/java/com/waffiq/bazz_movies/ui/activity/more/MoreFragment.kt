package com.waffiq.bazz_movies.ui.activity.more

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.waffiq.bazz_movies.R
import com.waffiq.bazz_movies.databinding.FragmentMoreBinding
import com.waffiq.bazz_movies.ui.activity.LoginActivity
import com.waffiq.bazz_movies.ui.viewmodel.AuthenticationViewModel
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelFactory
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelUserFactory
import com.waffiq.bazz_movies.utils.Constants.GRAVATAR_LINK
import com.waffiq.bazz_movies.utils.Helper.showToastLong
import com.waffiq.bazz_movies.utils.Helper.toastStillOnDevelopment
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

class MoreFragment : Fragment() {

  private var _binding: FragmentMoreBinding? = null
  private val binding get() = _binding!!

  private lateinit var authViewModel: AuthenticationViewModel
  private lateinit var moreViewModel: MoreViewModel

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentMoreBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val pref = requireContext().dataStore
    val factory = ViewModelUserFactory.getInstance(pref)
    this.authViewModel = ViewModelProvider(this, factory)[AuthenticationViewModel::class.java]

    val factory2 = ViewModelFactory.getInstance(requireContext())
    moreViewModel = ViewModelProvider(this, factory2)[MoreViewModel::class.java]

    setData()
    btnAction()
    return root
  }

  private fun btnAction() {
    binding.btnSetting.setOnClickListener { toastStillOnDevelopment(requireContext()) }
    binding.btnRate.setOnClickListener { toastStillOnDevelopment(requireContext()) }
    binding.btnLanguage.setOnClickListener { toastStillOnDevelopment(requireContext()) }
    binding.ivBtnEdit.setOnClickListener { toastStillOnDevelopment(requireContext()) }
    binding.btnDarkMode.setOnClickListener {
      binding.cbDarkMode.isChecked = !binding.cbDarkMode.isChecked
      toastStillOnDevelopment(requireContext())
    }
    binding.btnHelp.setOnClickListener { toastStillOnDevelopment(requireContext()) }
    binding.tvPrivacyPolicy.setOnClickListener { toastStillOnDevelopment(requireContext()) }
    binding.tvTermsConditon.setOnClickListener { toastStillOnDevelopment(requireContext()) }

    binding.btnSignout.setOnClickListener {
      authViewModel.getUser().observe(viewLifecycleOwner) { user ->
        if (user.token == "NaN") dialogSignOutGuestMode() // sign out for guest account
        else signOut(true) // sign out for login account
      }
    }
  }

  private fun dialogSignOutGuestMode() {
    val builder: AlertDialog.Builder = AlertDialog.Builder(context)
    builder
      .setMessage(getString(R.string.warning_signOut_guest_mode))
      .setTitle(getString(R.string.warning))
      .setPositiveButton(getString(R.string.yes)) { dialog, which ->
        signOut(false)
        showToastLong(requireContext(), getString(R.string.all_data_deleted))
        moreViewModel.deleteAll() // delete all data
      }
      .setNegativeButton(getString(R.string.no)) { dialog, which ->
        dialog.dismiss()
      }

    val dialog: AlertDialog = builder.create()
    dialog.show()
  }

  private fun signOut(showToast : Boolean){
    viewLifecycleOwner.lifecycleScope.launch {
      authViewModel.signOut()
      activity?.finish()
      startActivity(Intent(activity, LoginActivity::class.java))
    }
    if(showToast) showToastLong(requireContext(), getString(R.string.sign_out_success))
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
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}