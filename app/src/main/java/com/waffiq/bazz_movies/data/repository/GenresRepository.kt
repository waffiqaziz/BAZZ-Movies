package com.waffiq.bazz_movies.data.repository


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.waffiq.bazz_movies.data.model.Genre
import com.waffiq.bazz_movies.data.remote.ResultResponse
import com.waffiq.bazz_movies.data.remote.response.GenresResponse
import com.waffiq.bazz_movies.data.remote.retrofit.ApiConfig
import com.waffiq.bazz_movies.data.remote.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GenresRepository (private val apiService: ApiService,) {

    private var _moviesGenres = MutableLiveData<List<Genre>>()
    val movieGenres: LiveData<List<Genre>> = _moviesGenres

    fun getMoviesGenres(){
        val client = ApiConfig
            .getApiService()
            .getMovieGenres()

        client.enqueue(object : Callback<GenresResponse> {
            override fun onResponse(
                call: Call<GenresResponse>,
                response: Response<GenresResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _moviesGenres.value = response.body()!!.genres
                    }
                } else {
                    Log.e("cekkk ", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GenresResponse>, t: Throwable) {

                Log.e("Cekkk ", "onFailure: ${t.message}")
            }
        })
    }


//     fun getMoviesGenres(): ResultResponse<GenresResponse> {
//        val response = try {
//            apiService.getMovieGenres()
//        } catch (e: Exception) {
//            return ResultResponse.Error("Unknown error occurred")
//        }
//        Log.d("Movies genres:","$response")
//        return ResultResponse.Success(response)
//    }

//    suspend fun getSeriesGenres(): Resource<GenresResponse> {
//        val response = try {
//            api.getTvSeriesGenres()
//        } catch (e: Exception) {
//            return Resource.Error("Unknown error occurred")
//        }
//        Timber.d("Series genres: $response")
//        return Resource.Success(response)
//    }
}
