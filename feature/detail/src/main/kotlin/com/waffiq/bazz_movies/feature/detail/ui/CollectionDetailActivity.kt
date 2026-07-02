package com.waffiq.bazz_movies.feature.detail.ui

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DefaultItemAnimator
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.designsystem.R.color.gray_900
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_backdrop_error_filled
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.justifyTextView
import com.waffiq.bazz_movies.core.uihelper.utils.InsetHelper.setupWindowInsets
import com.waffiq.bazz_movies.core.utils.FlowUtils.collectFlow
import com.waffiq.bazz_movies.feature.detail.databinding.ActivityCollectionDetailBinding
import com.waffiq.bazz_movies.feature.detail.ui.adapter.CollectionPartsAdapter
import com.waffiq.bazz_movies.feature.detail.ui.adapter.GenreAdapter
import com.waffiq.bazz_movies.feature.detail.ui.state.CollectionUiState
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.CollectionViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CollectionDetailActivity : AppCompatActivity() {

  @Inject
  lateinit var navigator: INavigator

  private lateinit var binding: ActivityCollectionDetailBinding
  private lateinit var partsAdapter: CollectionPartsAdapter
  private lateinit var adapterGenre: GenreAdapter
  private var collectionId: Int = -1

  private val viewModel: CollectionViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge(
      statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
      navigationBarStyle = SystemBarStyle.dark(ContextCompat.getColor(this, gray_900)),
    )

    binding = ActivityCollectionDetailBinding.inflate(layoutInflater)
    setContentView(binding.root)
    binding.root.setupWindowInsets()

    setupRecyclerView()
    observeViewModel()
    buttonAction()
    justifyTextView(binding.tvCollectionOverview as TextView)

    collectionId = intent.getIntExtra(EXTRA_COLLECTION_ID, -1)
    if (collectionId != -1) {
      viewModel.loadMovieCollection(collectionId)
    } else {
      finish()
    }
  }

  private fun setupRecyclerView() {
    // collections adapter
    partsAdapter = CollectionPartsAdapter(navigator)
    binding.rvCollectionParts.adapter = partsAdapter

    // genres adapter
    adapterGenre = GenreAdapter(navigator)
    binding.rvGenre.layoutManager = FlexboxLayoutManager(this).apply {
      flexDirection = FlexDirection.ROW
      flexWrap = FlexWrap.WRAP
      justifyContent = JustifyContent.FLEX_START
    }
    binding.rvGenre.itemAnimator = DefaultItemAnimator()
    binding.rvGenre.adapter = adapterGenre
    adapterGenre.setMediaType(MOVIE_MEDIA_TYPE)
  }

  private fun observeViewModel() {
    collectFlow(viewModel.uiState) { state ->
      showLoading(state.isLoading)
      showError(state.isError)

      if (state.name.isNotEmpty() && !state.isError) {
        displayCollectionDetails(state)
      }
    }
  }

  private fun showLoading(isLoading: Boolean) {
    binding.loadingIndicator.isVisible = isLoading
    binding.tvMovies.isVisible = !isLoading
  }

  private fun showError(isError: Boolean) {
    if (isError) binding.collapsingToolbar.title = ""
    binding.tvCollectionOverview.isVisible = !isError
    binding.ivCollectionBackdrop.isVisible = !isError
    binding.scrollView.isVisible = !isError
    binding.rvCollectionParts.isVisible = !isError

    binding.illustrationError.root.isVisible = isError
    binding.illustrationError.btnTryAgain.isVisible = isError
    binding.illustrationError.progressCircular.isVisible = !isError
  }

  private fun buttonAction() {
    binding.btnBack.setOnClickListener { finish() }
    binding.illustrationError.btnTryAgain.setOnClickListener {
      viewModel.loadMovieCollection(collectionId)
    }
  }

  private fun displayCollectionDetails(state: CollectionUiState) {
    binding.rvCollectionParts.isVisible = true
    binding.tvMovies.isVisible = true
    binding.collapsingToolbar.title = state.name
    binding.tvCollectionOverview.text = state.overview

    adapterGenre.setGenre(state.genreIds)
    partsAdapter.submitList(state.parts)

    Glide.with(binding.ivCollectionBackdrop)
      .load(state.backdropUrl)
      .override(binding.ivCollectionBackdrop.width, binding.ivCollectionBackdrop.height)
      .transition(withCrossFade())
      .error(ic_backdrop_error_filled)
      .into(binding.ivCollectionBackdrop)
  }

  companion object {
    const val EXTRA_COLLECTION_ID = "extra_collection_id"
  }
}
