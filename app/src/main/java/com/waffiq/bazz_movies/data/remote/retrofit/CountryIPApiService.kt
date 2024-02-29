package com.waffiq.bazz_movies.data.remote.retrofit

import com.waffiq.bazz_movies.data.remote.response.CountyAPIResponse
import retrofit2.Call
import retrofit2.http.GET

interface CountryIPApiService {

  @GET("/")
  fun getIP(): Call<CountyAPIResponse>

}
