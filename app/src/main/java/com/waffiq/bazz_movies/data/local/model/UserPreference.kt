package com.waffiq.bazz_movies.data.local.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference(private val dataStore: DataStore<Preferences>) {

  fun getUser(): Flow<UserModel> {
    return dataStore.data.map {
      UserModel(
        it[NAME_KEY] ?: "",
        it[USERNAME_KEY] ?: "",
        it[PASSWORD_KEY] ?: "",
        it[USERID_KEY] ?: "",
        it[TOKEN_KEY] ?: "",
        it[STATE_KEY] ?: false,
        it[GRAVATAR_KEY] ?: ""
      )
    }
  }

  suspend fun saveUser(user: UserModel) {
    dataStore.edit {
      it[NAME_KEY] = user.name
      it[USERNAME_KEY] = user.username
      it[PASSWORD_KEY] = user.password
      it[USERID_KEY] = user.userId
      it[TOKEN_KEY] = user.token
      it[STATE_KEY] = user.isLogin
      it[GRAVATAR_KEY] = user.gravatarHast
    }
  }

  suspend fun signOut() {
    dataStore.edit {
      it[STATE_KEY] = false
      it[NAME_KEY] = ""
      it[USERNAME_KEY] = ""
      it[USERID_KEY] = ""
      it[TOKEN_KEY] = ""
      it[PASSWORD_KEY] = ""
      it[GRAVATAR_KEY] = ""
    }
  }

  companion object {
    @Volatile
    private var INSTANCE: UserPreference? = null

    private val NAME_KEY = stringPreferencesKey("name")
    private val USERNAME_KEY = stringPreferencesKey("username")
    private val PASSWORD_KEY = stringPreferencesKey("password")
    private val USERID_KEY = stringPreferencesKey("userId")
    private val TOKEN_KEY = stringPreferencesKey("token")
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