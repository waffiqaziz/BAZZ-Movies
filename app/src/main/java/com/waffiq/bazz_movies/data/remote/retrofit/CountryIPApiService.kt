package com.waffiq.bazz_movies.data.remote.retrofit

import com.waffiq.bazz_movies.data.remote.response.*
import retrofit2.Call

import retrofit2.http.*

interface CountryIPApiService {

  @GET("/")
  fun getIP(): Call<CountyAPIResponse>

}
