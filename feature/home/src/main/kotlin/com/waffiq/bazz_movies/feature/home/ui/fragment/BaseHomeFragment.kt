package com.waffiq.bazz_movies.feature.home.ui.fragment

import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.paging.CombinedLoadStates
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.designsystem.databinding.IllustrationErrorBinding
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.observeLoadState
import com.waffiq.bazz_movies.navigation.INavigator
import com.waffiq.bazz_movies.navigation.ListArgs
import com.waffiq.bazz_movies.navigation.ListType
import com.waffiq.bazz_movies.navigation.MediaSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

abstract class BaseHomeFragment : Fragment() {

  @Inject
  lateinit var navigator: INavigator

  @Inject
  lateinit var snackbar: ISnackbar

  protected var mSnackbar: Snackbar? = null

  abstract fun showShimmer(isVisible: Boolean)
  abstract fun showView(isVisible: Boolean)
  abstract fun onClearBinding()

  protected fun openList(listType: ListType, mediaType: String) {
    navigator.openList(
      requireContext(),
      ListArgs(
        listType = listType,
        mediaType = MediaSource.Typed(mediaType),
        title = "",
      ),
    )
  }

  protected fun observePrimaryLoadState(
    loadStateFlow: Flow<CombinedLoadStates>,
    itemCount: () -> Int,
    illustrationError: IllustrationErrorBinding,
  ) {
    viewLifecycleOwner.observeLoadState(
      loadStateFlow = loadStateFlow,
      onLoading = { showShimmer(true) },
      onSuccess = {
        illustrationError.progressCircular.isVisible = false
        illustrationError.btnTryAgain.isVisible = true
        showShimmer(false)
        showView(true)
      },
      onError = { error ->
        illustrationError.progressCircular.isVisible = false
        illustrationError.btnTryAgain.isVisible = true
        showShimmer(false)
        showView(itemCount() > 0)
        mSnackbar = snackbar.showSnackbarWarning(error)
      },
    )
  }

  override fun onPause() {
    super.onPause()
    mSnackbar?.dismiss()
  }

  override fun onStop() {
    super.onStop()
    mSnackbar?.dismiss()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    mSnackbar = null
    onClearBinding()
  }
}
