package com.waffiq.bazz_movies.ui.activity.more

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
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelUserFactory
import com.waffiq.bazz_movies.utils.Helper.toastStillOnDevelopment
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

class MoreFragment : Fragment() {

  private var _binding: FragmentMoreBinding? = null
  private val binding get() = _binding!!

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
    this.moreViewModel = ViewModelProvider(this, factory)[MoreViewModel::class.java]

    setData()
    btnAction()
    return root
  }

  private fun btnAction(){
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
      viewLifecycleOwner.lifecycleScope.launch{
        startActivity(Intent(activity, LoginActivity::class.java))
        moreViewModel.signOut()
        activity?.finish()
      }
    }
  }

  private fun setData(){
    moreViewModel.getUser().observe(requireActivity()){
      binding.apply {
        tvFullName.text = it.name
        tvUsername.text = it.username

        Glide.with(binding.imgAvatar)
          .load("https://secure.gravatar.com/avatar/${it.gravatarHast}" + ".jpg?s=200" )
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