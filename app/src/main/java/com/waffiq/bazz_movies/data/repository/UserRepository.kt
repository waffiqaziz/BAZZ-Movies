package com.waffiq.bazz_movies.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.waffiq.bazz_movies.data.local.model.UserModel
import com.waffiq.bazz_movies.data.local.model.UserPreference
import com.waffiq.bazz_movies.data.remote.response.CountyAPIResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.AccountDetailsResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.AuthenticationResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.CreateSessionResponse
import com.waffiq.bazz_movies.data.remote.retrofit.CountryIPApiConfig
import com.waffiq.bazz_movies.data.remote.retrofit.TMDBApiConfig
import com.waffiq.bazz_movies.utils.Event
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository(
  private val pref: UserPreference
) {

  private var _token = MutableLiveData<String>()
  val token: LiveData<String> get() = _token

  private var _tokenVerified = MutableLiveData<String>()
  val tokenVerified: LiveData<String> get() = _tokenVerified

  private var _sessionId = MutableLiveData<String>()
  val sessionId: LiveData<String> get() = _sessionId

  private val _snackbarText = MutableLiveData<Event<String>>()
  val snackBarText: LiveData<Event<String>> get() = _snackbarText

  private val _user = MutableLiveData<UserModel>()
  val user: LiveData<UserModel> get() = _user

  private val _countryCode = MutableLiveData<String>()
  val countryCode: LiveData<String> = _countryCode

  private val _isLoading = MutableLiveData<Boolean>()
  val isLoading: LiveData<Boolean> = _isLoading

  fun login(username: String, pass: String, token: String) {
    _isLoading.value = true
    val client = TMDBApiConfig().getApiService().login(username, pass, token)
    client.enqueue(object : Callback<AuthenticationResponse> {
      override fun onResponse(
        call: Call<AuthenticationResponse>,
        response: Response<AuthenticationResponse>
      ) {
        _isLoading.value = false
        if (response.isSuccessful) {
          val responseBody = response.body()
          if (responseBody != null && responseBody.success) {

            _tokenVerified.value = responseBody.requestToken
          }
        } else {
          Log.e(TAG, "onFailure: ${response.message()}")

          // get message error
          val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
          val message = jsonObject.getString("status_message")
          _snackbarText.value = Event(message)
        }
      }

      override fun onFailure(call: Call<AuthenticationResponse>, t: Throwable) {
        _isLoading.value = false
        Log.e(TAG, "onFailure: ${t.message}")
      }
    })
  }

  fun createToken() {
    _isLoading.value = true
    val client = TMDBApiConfig().getApiService().createToken()
    client.enqueue(object : Callback<AuthenticationResponse> {
      override fun onResponse(
        call: Call<AuthenticationResponse>,
        response: Response<AuthenticationResponse>
      ) {
        _isLoading.value = false
        if (response.isSuccessful) {
          val responseBody = response.body()
          if (responseBody != null && responseBody.success) {
            _token.value = responseBody.requestToken
          }

        } else {
          Log.e(TAG, "onFailure: ${response.message()}")

          // get message error
          val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
          val message = jsonObject.getString("status_message")
          _snackbarText.value = Event(message)
        }
      }

      override fun onFailure(call: Call<AuthenticationResponse>, t: Throwable) {
        _isLoading.value = false
        Log.e(TAG, "onFailure: ${t.message}")
        _snackbarText.value = Event(t.message.toString())
      }
    })
  }

  suspend fun saveUser(userModel: UserModel) = pref.saveUser(userModel)

  suspend fun saveRegion(region: String) = pref.saveRegion(region)

  fun createSessionLogin(token: String) {
    _isLoading.value = true
    val client = TMDBApiConfig().getApiService().createSessionLogin(token)
    client.enqueue(object : Callback<CreateSessionResponse> {
      override fun onResponse(
        call: Call<CreateSessionResponse>,
        response: Response<CreateSessionResponse>
      ) {
        _isLoading.value = false
        if (response.isSuccessful) {
          val responseBody = response.body()
          if (responseBody != null && responseBody.success) {

            _sessionId.value = responseBody.sessionId
          }
        } else {
          Log.e(TAG, "onFailure: ${response.message()}")

          // get message error
          val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
          val message = jsonObject.getString("status_message")
          _snackbarText.value = Event(message)
        }
      }

      override fun onFailure(call: Call<CreateSessionResponse>, t: Throwable) {
        _isLoading.value = false
        Log.e(TAG, "onFailure: ${t.message}")
        _snackbarText.value = Event(t.message.toString())
      }
    })
  }

  fun getUserDetail(sessionId: String) {
    _isLoading.value = true
    val client = TMDBApiConfig().getApiService().getAccountDetails(sessionId)
    client.enqueue(object : Callback<AccountDetailsResponse> {
      override fun onResponse(
        call: Call<AccountDetailsResponse>,
        response: Response<AccountDetailsResponse>
      ) {
        _isLoading.value = false
        if (response.isSuccessful) {
          val responseBody = response.body()
          if (responseBody?.id != null) {
            _user.value = UserModel(
              userId = responseBody.id,
              name = responseBody.name.toString(),
              username = responseBody.username.toString(),
              password = "NaN",
              region = "NaN",
              token = "NaN",
              isLogin = true,
              gravatarHast = responseBody.avatar?.gravatar?.hash.toString(),
            )
          }
        } else {
          Log.e(TAG, "onFailure: ${response.message()}")

          // get message error
          val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
          val message = jsonObject.getString("status_message")
          _snackbarText.value = Event(message)
        }
      }

      override fun onFailure(call: Call<AccountDetailsResponse>, t: Throwable) {
        _isLoading.value = false
        Log.e(TAG, "onFailure: ${t.message}")
        _snackbarText.value = Event(t.message.toString())
      }
    })
  }

  fun getUser(): Flow<UserModel> {
    return pref.getUser()
  }

  fun getUserRegion(): Flow<String> {
    return pref.getRegion()
  }

  fun getCountryCode() {
    _isLoading.value = true
    val client = CountryIPApiConfig().getApiService().getIP()
    client.enqueue(object : Callback<CountyAPIResponse> {
      override fun onResponse(
        call: Call<CountyAPIResponse>,
        response: Response<CountyAPIResponse>
      ) {
        _isLoading.value = false
        if (response.isSuccessful) {
          val responseBody = response.body()
          if (responseBody != null) _countryCode.value = responseBody.country!!
        } else {
          Log.e(TAG, "onFailure: ${response.message()}")
          _countryCode.value = ""

          // get message error
          val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
          val message = jsonObject.getString("status_message")
          _snackbarText.value = Event(message)
        }
      }

      override fun onFailure(call: Call<CountyAPIResponse>, t: Throwable) {
        _isLoading.value = false
        _countryCode.value = ""
        Log.e(TAG, "onFailure: ${t.message}")
        _snackbarText.value = Event(t.message.toString())
      }
    })
  }

  suspend fun removeUserData() {
    pref.removeUserData()
  }

  companion object {
    private const val TAG = "UserRepository"
  }
}