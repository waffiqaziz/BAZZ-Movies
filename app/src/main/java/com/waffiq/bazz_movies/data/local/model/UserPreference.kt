package com.waffiq.bazz_movies.data.local.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference(private val dataStore: DataStore<Preferences>) {

  fun getUser(): Flow<UserModel> {
    return dataStore.data.map {
      UserModel(
        it[USERID_KEY] ?: 0,
        it[NAME_KEY] ?: "",
        it[USERNAME_KEY] ?: "",
        it[PASSWORD_KEY] ?: "",
        it[REGION_KEY] ?: "",
        it[TOKEN_KEY] ?: "",
        it[STATE_KEY] ?: false,
        it[GRAVATAR_KEY] ?: ""
      )
    }
  }

  fun getRegion(): Flow<String> {
    return dataStore.data.map {
      it[REGION_KEY] ?: ""
    }
  }

  suspend fun saveUser(user: UserModel) {
    dataStore.edit {
      it[USERID_KEY] = user.userId
      it[NAME_KEY] = user.name
      it[USERNAME_KEY] = user.username
      it[PASSWORD_KEY] = user.password
      it[REGION_KEY] = ""
      it[TOKEN_KEY] = user.token
      it[STATE_KEY] = user.isLogin
      it[GRAVATAR_KEY] = user.gravatarHast
    }
  }

  suspend fun saveRegion(region: String) {
    dataStore.edit {
      it[REGION_KEY] = region
    }
  }

  suspend fun signOut() {
    dataStore.edit {
      it[USERID_KEY] = 0
      it[NAME_KEY] = ""
      it[USERNAME_KEY] = ""
      it[REGION_KEY] = ""
      it[SETTING_REGION_KEY] = ""
      it[TOKEN_KEY] = ""
      it[PASSWORD_KEY] = ""
      it[GRAVATAR_KEY] = ""
      it[STATE_KEY] = false
    }
  }

  companion object {
    @Volatile
    private var INSTANCE: UserPreference? = null

    private val NAME_KEY = stringPreferencesKey("name")
    private val USERNAME_KEY = stringPreferencesKey("username")
    private val PASSWORD_KEY = stringPreferencesKey("password")
    private val USERID_KEY = intPreferencesKey("userId")
    private val TOKEN_KEY = stringPreferencesKey("token")
    private val REGION_KEY = stringPreferencesKey("region")
    private val SETTING_REGION_KEY = stringPreferencesKey("set_region")
    private val STATE_KEY = booleanPreferencesKey("state")
    private val GRAVATAR_KEY = stringPreferencesKey("gravatar")

    fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
      return INSTANCE ?: synchronized(this) {
        val instance = UserPreference(dataStore)
        INSTANCE = instance
        instance
      }
    }
  }
}