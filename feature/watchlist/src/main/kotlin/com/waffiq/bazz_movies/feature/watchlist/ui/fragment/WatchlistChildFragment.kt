package com.waffiq.bazz_movies.feature.watchlist.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.designsystem.R.string.binding_error
import com.waffiq.bazz_movies.core.domain.Favorite
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.domain.UpdateWatchlistParams
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.FavoritePagingAdapter
import com.waffiq.bazz_movies.core.uihelper.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.feature.watchlist.databinding.FragmentWatchlistChildBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow

@AndroidEntryPoint
class WatchlistChildFragment : BaseWatchlistFragment<MediaItem>() {

  private var _binding: FragmentWatchlistChildBinding? = null
  override val binding get() = _binding ?: error(getString(binding_error))

  private lateinit var adapterPaging: FavoritePagingAdapter

  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  val mediaType: String by lazy {
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
    adapterPaging = FavoritePagingAdapter(navigator, mediaType)
    super.onViewCreated(view, savedInstanceState)
  }

  override fun getPagingAdapter(): PagingDataAdapter<MediaItem, *> = adapterPaging

  override fun setupPagingAdapterWithFooter() {
    binding.rvWatchlist.adapter = adapterPaging.withLoadStateFooter(
      footer = LoadingStateAdapter { adapterPaging.retry() }
    )
  }

  override fun notifyPagingAdapterChanged(position: Int) {
    adapterPaging.notifyItemChanged(position)
  }

  override fun refreshPagingAdapter() {
    adapterPaging.retry()
    adapterPaging.refresh()
  }

  override fun getWatchlistData(token: String): Flow<PagingData<MediaItem>> =
    if (mediaType == MOVIE_MEDIA_TYPE) {
      watchlistViewModel.watchlistMovies(token)
    } else {
      watchlistViewModel.watchlistTvSeries(token)
    }

  override fun getDBWatchlistData(): LiveData<List<Favorite>> =
    if (mediaType == MOVIE_MEDIA_TYPE) {
      sharedDBViewModel.watchlistMoviesDB
    } else {
      sharedDBViewModel.watchlistTvSeriesDB
    }

  override fun createWatchlistModel(mediaId: Int): UpdateWatchlistParams =
    UpdateWatchlistParams(
      mediaType = mediaType,
      mediaId = mediaId,
      watchlist = false
    )

  override fun postToAddFavorite(title: String, mediaId: Int) {
    if (mediaType == MOVIE_MEDIA_TYPE) {
      watchlistViewModel.addMovieToFavorite(mediaId, title)
    } else {
      watchlistViewModel.addTvToFavorite(mediaId, title)
    }
  }

  override fun extractDataFromPagingViewHolder(viewHolder: RecyclerView.ViewHolder): MediaItem =
    (viewHolder as FavoritePagingAdapter.ViewHolder).data

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  companion object {
    private const val ARG_MEDIA_TYPE = "media_type"

    fun newInstance(mediaType: String) = WatchlistChildFragment().apply {
      arguments = bundleOf(ARG_MEDIA_TYPE to mediaType)
    }
  }
}
