package com.waffiq.bazz_movies.ui.activity.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.waffiq.bazz_movies.databinding.FragmentMoreBinding
import com.waffiq.bazz_movies.utils.Helper.toastStillOnDevelopment

class MoreFragment : Fragment() {

  private var _binding: FragmentMoreBinding? = null

  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val moreViewModel =
      ViewModelProvider(this)[MoreViewModel::class.java]

    _binding = FragmentMoreBinding.inflate(inflater, container, false)
    val root: View = binding.root

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
    binding.btnSignout.setOnClickListener { toastStillOnDevelopment(requireContext()) }
    binding.tvPrivacyPolicy.setOnClickListener { toastStillOnDevelopment(requireContext()) }
    binding.tvTermsConditon.setOnClickListener { toastStillOnDevelopment(requireContext()) }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}