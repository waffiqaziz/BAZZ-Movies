package com.waffiq.bazz_movies.ui.activity.myfolder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.waffiq.bazz_movies.databinding.FragmentMyFolderBinding

class MyFolderFragment : Fragment() {

  private var _binding: FragmentMyFolderBinding? = null

  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val viewModel =
      ViewModelProvider(this)[MyFolderViewModel::class.java]

    _binding = FragmentMyFolderBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val textView: TextView = binding.textMyFolder
    viewModel.text.observe(viewLifecycleOwner) {
      textView.text = it
    }
    return root
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}