package com.waffiq.bazz_movies.feature.list.ui

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_IMG_LINK_BACKDROP_W780
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_bazz_logo
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_broken_image
import com.waffiq.bazz_movies.core.uihelper.mappers.UIStateMapper.toUiState
import com.waffiq.bazz_movies.core.uihelper.state.UIState
import com.waffiq.bazz_movies.core.uihelper.state.isLoading
import com.waffiq.bazz_movies.core.utils.FlowUtils.collectAndSubmitData
import com.waffiq.bazz_movies.core.utils.GenreHelper.getGenreName
import com.waffiq.bazz_movies.feature.list.databinding.ActivityListBinding
import com.waffiq.bazz_movies.feature.list.ui.adapter.ListAdapter
import com.waffiq.bazz_movies.feature.list.ui.viewmodel.ListViewModel
import com.waffiq.bazz_movies.feature.list.utils.BackdropHelper.getBackdropMovieGenre
import com.waffiq.bazz_movies.feature.list.utils.BackdropHelper.getBackdropTvGenre
import com.waffiq.bazz_movies.feature.list.utils.Helper.capitaliseEachWord
import com.waffiq.bazz_movies.feature.list.utils.ParcelableHelper.extractArgsItemFromIntent
import com.waffiq.bazz_movies.navigation.INavigator
import com.waffiq.bazz_movies.navigation.ListArgs
import com.waffiq.bazz_movies.navigation.ListType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ListActivity : AppCompatActivity() {

  @Inject
  lateinit var navigator: INavigator

  private lateinit var binding: ActivityListBinding

  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  lateinit var adapter: ListAdapter

  private val viewModel: ListViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge(
      statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
      navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
    )
    binding = ActivityListBinding.inflate(layoutInflater)
    setContentView(binding.root)

    // get data extra
    val args = extractDataFromIntent() ?: run {
      finish()
      return
    }

    buttonAction()
    setupRecyclerView()
    setupList(args)
    lifecycleScope.launch {
      adapter.loadStateFlow
        .distinctUntilChangedBy { it.refresh }
        .map { it.toUiState() }
        .collect(::handleRefreshState)
    }
  }

  private fun extractDataFromIntent(): ListArgs? = extractArgsItemFromIntent(intent)

  private fun setupList(args: ListArgs) {
    adapter.setMediaType(args.mediaType)
    binding.toolbar.subtitle = args.mediaType.uppercase()

    when (args.listType) {
      ListType.BY_GENRE -> showListBasedGenre(args)

      ListType.BY_KEYWORD -> showListBasedKeywords(args)

      else -> {
        binding.toolbar.title = args.title
      }
    }
  }

  private fun showListBasedGenre(args: ListArgs) {
    val backdrop = if (args.mediaType == MOVIE_MEDIA_TYPE) {
      getBackdropMovieGenre(args.genreId)
    } else {
      getBackdropTvGenre(args.genreId)
    }
    showBackdrop(backdrop)
    binding.toolbar.title = getGenreName(args.genreId)
    collectAndSubmitData(
      this,
      {
        if (args.mediaType == MOVIE_MEDIA_TYPE) {
          viewModel.getMovieByGenres(args.genreId.toString())
        } else {
          viewModel.getTvByGenres(args.genreId.toString())
        }
      },
      adapter,
    )
  }

  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  internal fun handleRefreshState(state: UIState<Unit>) {
    binding.loadingIndicator.isVisible = state.isLoading
    binding.rvList.isVisible = state is UIState.Success
    binding.illustrationError.root.isVisible = state is UIState.Error
    binding.illustrationError.progressCircular.isVisible = false
    binding.illustrationError.btnTryAgain.isVisible = state is UIState.Error
  }

  private fun showListBasedKeywords(args: ListArgs) {
    binding.toolbar.title = args.title.capitaliseEachWord()
    collectAndSubmitData(
      this,
      {
        if (args.mediaType == MOVIE_MEDIA_TYPE) {
          viewModel.getMovieByKeywords(args.keywordId.toString())
        } else {
          viewModel.getTvByKeywords(args.keywordId.toString())
        }
      },
      adapter,
    )

    lifecycleScope.launch {
      adapter.loadStateFlow.collect { onKeywordsLoadStateChanged() }
    }
  }

  @VisibleForTesting
  internal fun onKeywordsLoadStateChanged() {
    if (adapter.itemCount >= 1) {
      showBackdrop(adapter.snapshot().items.first().backdropPath)
    }
  }

  private fun showBackdrop(backdrop: String?) {
    Glide.with(binding.ivPicture)
      .load(TMDB_IMG_LINK_BACKDROP_W780 + backdrop)
      .placeholder(ic_bazz_logo)
      .transition(withCrossFade())
      .error(ic_broken_image)
      .into(binding.ivPicture)
  }

  private fun setupRecyclerView() {
    adapter = ListAdapter(navigator)
    binding.rvList.layoutManager = GridLayoutManager(
      this,
      calculateSpanCount(),
      GridLayoutManager.VERTICAL,
      false,
    )
    binding.rvList.adapter = adapter
  }

  private fun buttonAction() {
    binding.btnClose.setOnClickListener { finish() }
    binding.swipeRefresh.setOnRefreshListener {
      adapter.refresh()
      binding.swipeRefresh.isRefreshing = false
    }
  }

  private fun calculateSpanCount(): Int {
    val displayMetrics = resources.displayMetrics
    val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
    val columnWidthDp = COLUMN_WIDTH
    return (screenWidthDp / columnWidthDp).toInt().coerceAtLeast(2)
  }

  companion object {
    const val COLUMN_WIDTH = 120f
    const val EXTRA_LIST = "LIST"
  }
}
