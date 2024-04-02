package com.waffiq.bazz_movies.data.repository

import com.waffiq.bazz_movies.data.local.model.UserModel
import com.waffiq.bazz_movies.data.local.model.UserPreference
import com.waffiq.bazz_movies.data.remote.SessionID
import com.waffiq.bazz_movies.data.remote.datasource.UserDataSource
import kotlinx.coroutines.flow.Flow

class UserRepository(
  private val pref: UserPreference,
  private val userDataSource: UserDataSource
) {
  suspend fun login(username: String, pass: String, token: String) =
    userDataSource.login(username, pass, token)

  suspend fun createToken() = userDataSource.createToken()

  suspend fun deleteSession(data: SessionID) = userDataSource.deleteSession(data)

  suspend fun saveUser(userModel: UserModel) = pref.saveUser(userModel)

  suspend fun saveRegion(region: String) = pref.saveRegion(region)

  suspend fun createSessionLogin(token: String) = userDataSource.createSessionLogin(token)

  suspend fun getUserDetail(sessionId: String) = userDataSource.getUserDetail(sessionId)

  fun getUser(): Flow<UserModel> = pref.getUser()

  fun getUserRegion(): Flow<String> = pref.getRegion()

  suspend fun getCountryCode() = userDataSource.getCountryCode()

  suspend fun removeUserData() = pref.removeUserData()

  companion object {
    private const val TAG = "UserRepository"
  }
}