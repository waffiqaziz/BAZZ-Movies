package com.waffiq.bazz_movies.ui.activity.more

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waffiq.bazz_movies.data.remote.post_body.SessionIDPostModel
import com.waffiq.bazz_movies.domain.model.post.Post
import com.waffiq.bazz_movies.domain.usecase.auth_tmdb_account.AuthTMDbAccountUseCase
import com.waffiq.bazz_movies.utils.NetworkResult
import com.waffiq.bazz_movies.utils.common.Event
import kotlinx.coroutines.launch

class MoreViewModelUser(
  private val authTMDbAccountUseCase: AuthTMDbAccountUseCase
) : ViewModel() {

  private val _signOutState = MutableLiveData<NetworkResult<Post>>()
  val signOutState: LiveData<NetworkResult<Post>> get() = _signOutState

  private val _errorState = MutableLiveData<Event<String>>()
  val errorState: LiveData<Event<String>> get() = _errorState

  fun deleteSession(data: SessionIDPostModel) =
    viewModelScope.launch {
      authTMDbAccountUseCase.deleteSession(data).collect { networkResult ->
        _signOutState.value = networkResult
      }
    }
}