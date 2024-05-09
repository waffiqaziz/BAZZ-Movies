package com.waffiq.bazz_movies.data.repository

import com.waffiq.bazz_movies.data.local.model.UserModel
import com.waffiq.bazz_movies.data.local.model.UserPreference
import com.waffiq.bazz_movies.data.remote.SessionIDPostModel
import com.waffiq.bazz_movies.data.remote.datasource.UserDataSource

class UserRepository(
  private val pref: UserPreference,
  private val userDataSource: UserDataSource
) {
  suspend fun login(username: String, pass: String, token: String) =
    userDataSource.login(username, pass, token)

  suspend fun createToken() = userDataSource.createToken()

  suspend fun deleteSession(data: SessionIDPostModel) = userDataSource.deleteSession(data)

  suspend fun saveUser(userModel: UserModel) = pref.saveUser(userModel)

  suspend fun saveRegion(region: String) = pref.saveRegion(region)

  suspend fun createSessionLogin(token: String) = userDataSource.createSessionLogin(token)

  suspend fun getUserDetail(sessionId: String) = userDataSource.getUserDetail(sessionId)

  fun getUser() = pref.getUser()

  fun getUserRegion() = pref.getRegion()

  suspend fun getCountryCode() = userDataSource.getCountryCode()

  suspend fun removeUserData() = pref.removeUserData()

  companion object {
    private const val TAG = "UserRepository"
  }
}