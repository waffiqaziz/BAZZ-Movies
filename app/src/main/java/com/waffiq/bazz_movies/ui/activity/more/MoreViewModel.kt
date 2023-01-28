package com.waffiq.bazz_movies.ui.activity.more


import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.waffiq.bazz_movies.data.repository.UserRepository

class MoreViewModel(private val userRepository: UserRepository) : ViewModel() {

  fun getUser() = userRepository.getUser().asLiveData()

  suspend fun signOut() = userRepository.logout()
}