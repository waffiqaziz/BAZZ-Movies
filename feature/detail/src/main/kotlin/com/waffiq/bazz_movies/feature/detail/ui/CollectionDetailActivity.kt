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
import com.waffiq.bazz_movies.core.uihelper.state.UIState
import com.waffiq.bazz_movies.core.uihelper.state.dataOrNull
import com.waffiq.bazz_movies.core.uihelper.state.isLoading
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.justifyTextView
import com.waffiq.bazz_movies.core.uihelper.utils.InsetHelper.setupWindowInsets
import com.waffiq.bazz_movies.core.utils.FlowUtils.collectFlow
import com.waffiq.bazz_movies.feature.detail.databinding.ActivityCollectionDetailBinding
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.DetailCollections
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.genreIds
import com.waffiq.bazz_movies.feature.detail.ui.adapter.CollectionPartsAdapter
import com.waffiq.bazz_movies.feature.detail.ui.adapter.GenreAdapter
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.CollectionViewModel
import com.waffiq.bazz_movies.feature.detail.utils.helpers.ImageHelper.backdropOriginalSource
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
    binding.apply {
      partsAdapter = CollectionPartsAdapter(navigator)
      rvCollectionParts.adapter = partsAdapter

      adapterGenre = GenreAdapter(navigator)
      rvGenre.apply {
        layoutManager = FlexboxLayoutManager(context).apply {
          flexDirection = FlexDirection.ROW
          flexWrap = FlexWrap.WRAP
          justifyContent = JustifyContent.FLEX_START
        }
        itemAnimator = DefaultItemAnimator()
        adapter = adapterGenre
      }
      adapterGenre.setMediaType(MOVIE_MEDIA_TYPE)
    }
  }

  private fun observeViewModel() {
    collectFlow(viewModel.uiState) {
      binding.apply {
        // loading
        loadingIndicator.isVisible = it.isLoading
        tvMovies.isVisible = !it.isLoading

        // error
        showError(it is UIState.Error)

        // success
        displayCollectionDetails(it.dataOrNull ?: return@collectFlow)
      }
    }
  }

  private fun showError(isError: Boolean) {
    binding.apply {
      if (isError) collapsingToolbar.title = ""
      tvCollectionOverview.isVisible = !isError
      ivCollectionBackdrop.isVisible = !isError
      scrollView.isVisible = !isError
      rvCollectionParts.isVisible = !isError

      illustrationError.root.isVisible = isError
      illustrationError.btnTryAgain.isVisible = isError
      illustrationError.progressCircular.isVisible = !isError
    }
  }

  private fun buttonAction() {
    binding.btnBack.setOnClickListener { finish() }
    binding.illustrationError.btnTryAgain.setOnClickListener {
      viewModel.loadMovieCollection(collectionId)
    }
  }

  private fun displayCollectionDetails(collection: DetailCollections) {
    binding.apply {
      rvCollectionParts.isVisible = true
      tvMovies.isVisible = true
      collapsingToolbar.title = collection.name
      tvCollectionOverview.text = collection.overview
      adapterGenre.setGenre(collection.genreIds)

      Glide.with(ivCollectionBackdrop)
        .load(collection.backdropOriginalSource)
        .override(ivCollectionBackdrop.width, ivCollectionBackdrop.height)
        .transition(withCrossFade())
        .error(ic_backdrop_error_filled)
        .into(ivCollectionBackdrop)
    }

    val cleanPartsList = collection.parts?.filterNotNull() ?: emptyList()
    partsAdapter.submitList(cleanPartsList)
  }

  companion object {
    const val EXTRA_COLLECTION_ID = "extra_collection_id"
  }
}
