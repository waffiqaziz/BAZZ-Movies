package com.waffiq.bazz_movies.feature.favorite.ui.fragment

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
import com.waffiq.bazz_movies.core.domain.UpdateFavoriteParams
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.FavoritePagingAdapter
import com.waffiq.bazz_movies.core.uihelper.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.feature.favorite.databinding.FragmentFavoriteChildBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow

@AndroidEntryPoint
class FavoriteChildFragment : BaseFavoriteFragment<MediaItem>() {

  private var _binding: FragmentFavoriteChildBinding? = null
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
    _binding = FragmentFavoriteChildBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    adapterPaging = FavoritePagingAdapter(navigator, mediaType)
    super.onViewCreated(view, savedInstanceState)
  }

  override fun getPagingAdapter(): PagingDataAdapter<MediaItem, *> = adapterPaging

  override fun setupPagingAdapterWithFooter() {
    binding.rvFavorite.adapter = adapterPaging.withLoadStateFooter(
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

  override fun getFavoriteData(token: String): Flow<PagingData<MediaItem>> =
    if (mediaType == MOVIE_MEDIA_TYPE) {
      favoriteViewModel.favoriteMovies(token)
    } else {
      favoriteViewModel.favoriteTvSeries(token)
    }

  override fun getDBFavoriteData(): LiveData<List<Favorite>> =
    if (mediaType == MOVIE_MEDIA_TYPE) {
      sharedDBViewModel.favoriteMoviesFromDB
    } else {
      sharedDBViewModel.favoriteTvFromDB
    }

  override fun createFavoriteModel(mediaId: Int): UpdateFavoriteParams =
    UpdateFavoriteParams(
      mediaType = mediaType,
      mediaId = mediaId,
      favorite = false
    )

  override fun postToAddWatchlist(title: String, mediaId: Int) {
    if (mediaType == MOVIE_MEDIA_TYPE) {
      favoriteViewModel.addMovieToWatchlist(mediaId, title)
    } else {
      favoriteViewModel.addTvToWatchlist(mediaId, title)
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

    fun newInstance(mediaType: String) = FavoriteChildFragment().apply {
      arguments = bundleOf(ARG_MEDIA_TYPE to mediaType)
    }
  }
}
