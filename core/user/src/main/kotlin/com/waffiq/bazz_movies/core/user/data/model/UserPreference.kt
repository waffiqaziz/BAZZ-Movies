package com.waffiq.bazz_movies.core.user.data.model

import androidx.annotation.VisibleForTesting
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

  suspend fun saveUser(user: UserModelPref) {
    dataStore.edit {
      it[USERID_KEY] = user.userId
      it[NAME_KEY] = user.name
      it[USERNAME_KEY] = user.username
      it[PASSWORD_KEY] = user.password
      it[REGION_KEY] = user.region
      it[TOKEN_KEY] = user.token
      it[STATE_KEY] = user.isLogin
      it[GRAVATAR_KEY] = user.gravatarHast.orEmpty()
      it[TMDB_AVATAR_KEY] = user.tmdbAvatar.orEmpty()
    }
  }

  fun getUser(): Flow<UserModelPref> =
    dataStore.data.map {
      UserModelPref(
        it[USERID_KEY] ?: 0,
        it[NAME_KEY].orEmpty(),
        it[USERNAME_KEY].orEmpty(),
        it[PASSWORD_KEY].orEmpty(),
        it[REGION_KEY].orEmpty(),
        it[TOKEN_KEY].orEmpty(),
        it[STATE_KEY] == true,
        it[GRAVATAR_KEY].orEmpty(),
        it[TMDB_AVATAR_KEY].orEmpty()
      )
    }

  suspend fun saveRegion(region: String) {
    dataStore.edit { it[REGION_KEY] = region }
  }

  fun getToken(): Flow<String> = dataStore.data.map { it[TOKEN_KEY].orEmpty() }

  fun getRegion(): Flow<String> = dataStore.data.map { it[REGION_KEY].orEmpty() }

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

  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  internal companion object TestingKeys {
    val NAME_KEY = stringPreferencesKey("name")
    val USERNAME_KEY = stringPreferencesKey("username")
    val PASSWORD_KEY = stringPreferencesKey("password")
    val USERID_KEY = intPreferencesKey("userId")
    val TOKEN_KEY = stringPreferencesKey("token")
    val REGION_KEY = stringPreferencesKey("region")
    val STATE_KEY = booleanPreferencesKey("state")
    val GRAVATAR_KEY = stringPreferencesKey("gravatar")
    val TMDB_AVATAR_KEY = stringPreferencesKey("tmdb_avatar")
  }
}
