package com.waffiq.bazz_movies.feature.more.ui

import com.waffiq.bazz_movies.core.designsystem.R.string.backup_success
import com.waffiq.bazz_movies.core.designsystem.R.string.restore_success
import com.waffiq.bazz_movies.core.uihelper.state.UIState
import com.waffiq.bazz_movies.core.uihelper.state.isLoading
import com.waffiq.bazz_movies.feature.more.ui.viewmodel.MoreLocalViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MoreStateObserver(
  private val lifecycleScope: CoroutineScope,
  private val viewModel: MoreLocalViewModel,
  private val onProgress: (Boolean) -> Unit,
  private val onSuccess: (Int) -> Unit,
  private val onError: (String) -> Unit,
) {
  fun observeBackup() {
    lifecycleScope.launch {
      viewModel.backupState.collect { state ->
        onProgress(state.isLoading)
        when (state) {
          is UIState.Success -> {
            viewModel.resetBackupState()
            onSuccess(backup_success)
          }

          is UIState.Error -> {
            viewModel.resetBackupState()
            onError(state.message)
          }

          else -> Unit
        }
      }
    }
  }

  fun observeRestore() {
    lifecycleScope.launch {
      viewModel.restoreState.collect { state ->
        onProgress(state.isLoading)
        when (state) {
          is UIState.Success -> {
            viewModel.resetRestoreState()
            onSuccess(restore_success)
          }

          is UIState.Error -> {
            viewModel.resetRestoreState()
            onError(state.message)
          }

          else -> Unit
        }
      }
    }
  }
}
