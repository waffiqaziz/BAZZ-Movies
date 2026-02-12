package com.waffiq.bazz_movies.feature.watchlist.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.core.os.bundleOf
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.designsystem.R.string.binding_error
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.feature.watchlist.databinding.FragmentWatchlistChildBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WatchlistChildFragment : BaseWatchlistFragment<MediaItem>() {

  private var _binding: FragmentWatchlistChildBinding? = null
  override val binding get() = _binding ?: error(getString(binding_error))

  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  val internalMediaType: String by lazy {
    requireArguments().getString(ARG_MEDIA_TYPE) ?: MOVIE_MEDIA_TYPE
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    _binding = FragmentWatchlistChildBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
  }

  override fun getMediaType(): String = internalMediaType

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  companion object {
    private const val ARG_MEDIA_TYPE = "media_type"

    fun newInstance(mediaType: String) =
      WatchlistChildFragment().apply {
        arguments = bundleOf(ARG_MEDIA_TYPE to mediaType)
      }
  }
}
