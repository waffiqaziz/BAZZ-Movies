package com.waffiq.bazz_movies.feature.detail.ui

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
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
import com.waffiq.bazz_movies.core.designsystem.R.string.movies
import com.waffiq.bazz_movies.core.uihelper.dialog.SingleChoiceDialog
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.justifyTextView
import com.waffiq.bazz_movies.core.uihelper.utils.InsetHelper.setupWindowInsets
import com.waffiq.bazz_movies.core.utils.FlowUtils.collectFlow
import com.waffiq.bazz_movies.feature.detail.databinding.ActivityCollectionDetailBinding
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.CollectionSortOption
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

  private var collapsedVisible = false

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
    setupScrollBehavior()
    getDataExtra()
  }

  private fun getDataExtra() {
    collectionId = intent.getIntExtra(EXTRA_COLLECTION_ID, -1)
    if (collectionId != -1) {
      viewModel.loadMovieCollection(collectionId)
    } else {
      finish()
    }
  }

  private fun setupScrollBehavior() {
    binding.scrollView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
      // position of the RecyclerView relative to the screen
      val location = IntArray(2)
      binding.rvCollectionParts.getLocationOnScreen(location)

      // Ppsition of the toolbar relative to the screen
      val toolbarLocation = IntArray(2)
      binding.toolbar.getLocationOnScreen(toolbarLocation)

      // point at which the header is fully scrolled past the toolbar
      val trigger = toolbarLocation[1] + binding.toolbar.height + binding.headerLayout.root.height
      val passedThreshold = location[1] <= trigger

      // direction of scroll (up or down)
      val scrollingDown = scrollY > oldScrollY

      // Show the collapsed header only if the scrolled past the real header AND on scrolling down
      // When scrolling up always reveals the full header again
      val shouldShowCollapsed = passedThreshold && scrollingDown

      // skip if state no change, to avoid redundant view updates
      if (shouldShowCollapsed == collapsedVisible) return@setOnScrollChangeListener
      collapsedVisible = shouldShowCollapsed

      binding.headerLayout.root.isInvisible = shouldShowCollapsed
      binding.headerLayoutCollapsed.root.isInvisible = !shouldShowCollapsed
    }

    // align the collapsed header directly below the toolbar once its height is known
    binding.toolbar.post {
      val params = binding.headerLayoutCollapsed.root.layoutParams as CoordinatorLayout.LayoutParams
      params.topMargin = binding.toolbar.height
      binding.headerLayoutCollapsed.root.layoutParams = params
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
    binding.headerLayout.root.isVisible = !isLoading
    if (isLoading) binding.headerLayoutCollapsed.root.isVisible = false
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
    binding.headerLayout.btnSort.setOnClickListener {
      showSortDialog()
    }
    binding.headerLayoutCollapsed.btnSort.setOnClickListener {
      showSortDialog()
    }
  }

  private fun showSortDialog() {
    SingleChoiceDialog.show(
      context = this,
      items = CollectionSortOption.entries,
      selected = viewModel.currentSort.value,
      onSelected = {
        viewModel.applySort(it)
      },
    )
  }

  private fun displayCollectionDetails(state: CollectionUiState) {
    val textMovie = "${state.parts.size} ${getString(movies)}"
    binding.apply {
      rvCollectionParts.isVisible = true
      headerLayout.root.isVisible = true
      headerLayout.tvMovies.text = textMovie
      headerLayoutCollapsed.tvMovies.text = textMovie

      collapsingToolbar.title = state.name
      tvCollectionOverview.text = state.overview
    }

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
