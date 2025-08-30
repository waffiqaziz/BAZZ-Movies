package com.waffiq.bazz_movies.feature.favorite.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.designsystem.R.string.binding_error
import com.waffiq.bazz_movies.core.domain.Favorite
import com.waffiq.bazz_movies.core.domain.FavoriteModel
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.FavoriteMovieAdapter
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.FavoriteTvAdapter
import com.waffiq.bazz_movies.core.uihelper.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.feature.favorite.databinding.FragmentFavoriteChildBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow

@AndroidEntryPoint
class FavoriteTvSeriesFragment : BaseFavoriteFragment<MediaItem>() {

  private var _binding: FragmentFavoriteChildBinding? = null
  override val binding get() = _binding ?: error(getString(binding_error))

  private lateinit var adapterPaging: FavoriteTvAdapter

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    _binding = FragmentFavoriteChildBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    adapterPaging = FavoriteTvAdapter(navigator)
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

  override fun retryPagingAdapter() {
    adapterPaging.retry()
  }

  override fun getFavoriteData(token: String): Flow<PagingData<MediaItem>> =
    favoriteViewModel.favoriteTvSeries(token)

  override fun getDBFavoriteData(): LiveData<List<Favorite>> =
    sharedDBViewModel.favoriteTvFromDB

  override fun createFavoriteModel(mediaId: Int): FavoriteModel =
    FavoriteModel(
      mediaType = TV_MEDIA_TYPE,
      mediaId = mediaId,
      favorite = false
    )

  override fun postToAddWatchlist(title: String, mediaId: Int) {
    userPreferenceViewModel.getUserPref().observe(viewLifecycleOwner) { user ->
      favoriteViewModel.checkTvStatedThenPostWatchlist(user, mediaId, title)
    }
  }

  override fun extractDataFromPagingViewHolder(viewHolder: RecyclerView.ViewHolder): MediaItem =
    (viewHolder as FavoriteTvAdapter.ViewHolder).data

  override fun getMediaType(): String = MOVIE_MEDIA_TYPE

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}
