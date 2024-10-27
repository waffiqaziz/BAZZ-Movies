package com.waffiq.bazz_movies.core.data.local.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreference @Inject constructor(private val dataStore: DataStore<Preferences>) {

  suspend fun saveUser(user: UserModel) {
    dataStore.edit {
      it[USERID_KEY] = user.userId
      it[NAME_KEY] = user.name
      it[USERNAME_KEY] = user.username
      it[PASSWORD_KEY] = user.password
      it[REGION_KEY] = user.region
      it[TOKEN_KEY] = user.token
      it[STATE_KEY] = user.isLogin
      it[GRAVATAR_KEY] = user.gravatarHast ?: ""
      it[TMDB_AVATAR_KEY] = user.tmdbAvatar ?: ""
    }
  }

  fun getUser(): Flow<UserModel> =
    dataStore.data.map {
      UserModel(
        it[USERID_KEY] ?: 0,
        it[NAME_KEY] ?: "",
        it[USERNAME_KEY] ?: "",
        it[PASSWORD_KEY] ?: "",
        it[REGION_KEY] ?: "",
        it[TOKEN_KEY] ?: "",
        it[STATE_KEY] ?: false,
        it[GRAVATAR_KEY] ?: "",
        it[TMDB_AVATAR_KEY] ?: ""
      )
    }

  suspend fun saveRegion(region: String) {
    dataStore.edit { it[REGION_KEY] = region }
  }

  fun getToken(): Flow<String> = dataStore.data.map { it[TOKEN_KEY] ?: "" }

  fun getRegion(): Flow<String> = dataStore.data.map { it[REGION_KEY] ?: "" }

  suspend fun removeUserData() { // remove all data from datastore
    dataStore.edit {
      it[USERID_KEY] = 0
      it[NAME_KEY] = ""
      it[USERNAME_KEY] = ""
      it[REGION_KEY] = ""
      it[TOKEN_KEY] = ""
      it[PASSWORD_KEY] = ""
      it[GRAVATAR_KEY] = ""
      it[TMDB_AVATAR_KEY] = ""
      it[STATE_KEY] = false
    }
  }

  companion object {
    private val NAME_KEY = stringPreferencesKey("name")
    private val USERNAME_KEY = stringPreferencesKey("username")
    private val PASSWORD_KEY = stringPreferencesKey("password")
    private val USERID_KEY = intPreferencesKey("userId")
    private val TOKEN_KEY = stringPreferencesKey("token")
    private val REGION_KEY = stringPreferencesKey("region")
    private val STATE_KEY = booleanPreferencesKey("state")
    private val GRAVATAR_KEY = stringPreferencesKey("gravatar")
    private val TMDB_AVATAR_KEY = stringPreferencesKey("tmdb_avatar")
  }
}
