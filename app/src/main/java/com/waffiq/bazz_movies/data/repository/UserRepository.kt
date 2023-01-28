package com.waffiq.bazz_movies.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.waffiq.bazz_movies.data.local.model.UserModel
import com.waffiq.bazz_movies.data.local.model.UserPreference
import com.waffiq.bazz_movies.data.remote.response.AccountDetailsResponse
import com.waffiq.bazz_movies.data.remote.response.AuthenticationResponse
import com.waffiq.bazz_movies.data.remote.response.CreateSessionResponse
import com.waffiq.bazz_movies.data.remote.retrofit.ApiConfig
import com.waffiq.bazz_movies.data.remote.retrofit.ApiService
import com.waffiq.bazz_movies.utils.Event
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository(private val apiService: ApiService, private val pref: UserPreference) {

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

  fun login(username: String, pass: String, token: String){
    val service = ApiConfig().getApiService().login(username, pass, token)
    service.enqueue(object : Callback<AuthenticationResponse> {
      override fun onResponse(
        call: Call<AuthenticationResponse>,
        response: Response<AuthenticationResponse>
      ) {
        if (response.isSuccessful) {
          val responseBody = response.body()
          if (responseBody != null && responseBody.success) {

            _tokenVerified.value = responseBody.request_token
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
        Log.e(TAG, "onFailure: ${t.message}")
        _snackbarText.value = Event(t.message.toString())
      }
    })
  }

  fun createToken(){
    val service = ApiConfig().getApiService().createToken()
    service.enqueue(object : Callback<AuthenticationResponse> {
      override fun onResponse(
        call: Call<AuthenticationResponse>,
        response: Response<AuthenticationResponse>
      ) {
        if (response.isSuccessful) {
          val responseBody = response.body()
          if (responseBody != null && responseBody.success) {
            _token.value = responseBody.request_token
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
        Log.e(TAG, "onFailure: ${t.message}")
        _snackbarText.value = Event(t.message.toString())
      }
    })
  }

  suspend fun saveUser(userModel: UserModel) = pref.saveUser(userModel)

  fun createSessionLogin(token: String){
    val service = ApiConfig().getApiService().createSessionLogin(token)
    service.enqueue(object : Callback<CreateSessionResponse> {
      override fun onResponse(
        call: Call<CreateSessionResponse>,
        response: Response<CreateSessionResponse>
      ) {
        if (response.isSuccessful) {
          val responseBody = response.body()
          if (responseBody != null && responseBody.success) {

            _sessionId.value = responseBody.session_id
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
        Log.e(TAG, "onFailure: ${t.message}")
        _snackbarText.value = Event(t.message.toString())
      }
    })
  }

  fun getUserDetail(sessionId: String){
    val service = ApiConfig().getApiService().getAccountDetails(sessionId)
    service.enqueue(object : Callback<AccountDetailsResponse> {
      override fun onResponse(
        call: Call<AccountDetailsResponse>,
        response: Response<AccountDetailsResponse>
      ) {
        if (response.isSuccessful) {
          val responseBody = response.body()
          if (responseBody?.id != null) {
            _user.value = UserModel(
              name = responseBody.name.toString(),
              username = responseBody.username.toString(),
              password = "NaN",
              userId = responseBody.id.toString(),
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
        Log.e(TAG, "onFailure: ${t.message}")
        _snackbarText.value = Event(t.message.toString())
      }
    })
  }

  fun getUser(): Flow<UserModel> {
    return pref.getUser()
  }

  suspend fun logout() {
    pref.signOut()
  }

  companion object {
    private const val TAG = "UserRepository"
    private const val SUCCESS = "success"
  }
}