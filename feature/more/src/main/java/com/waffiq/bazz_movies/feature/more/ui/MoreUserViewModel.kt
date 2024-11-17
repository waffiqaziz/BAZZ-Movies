package com.waffiq.bazz_movies.feature.more.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waffiq.bazz_movies.core.model.Post
import com.waffiq.bazz_movies.core.network.data.remote.post_body.SessionIDPostModel
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import com.waffiq.bazz_movies.core.user.domain.usecase.auth_tmdb_account.AuthTMDbAccountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoreUserViewModel @Inject constructor(
  private val authTMDbAccountUseCase: AuthTMDbAccountUseCase
) : ViewModel() {

  private val _signOutState = MutableStateFlow<NetworkResult<Post>?>(null)
  val signOutState: Flow<NetworkResult<Post>?> get() = _signOutState

  fun deleteSession(data: SessionIDPostModel) {
    viewModelScope.launch {
      authTMDbAccountUseCase.deleteSession(data).collect { networkResult ->
        _signOutState.value = networkResult
      }
    }
  }

  fun removeState() {
    _signOutState.value = NetworkResult.Loading
  }
}
