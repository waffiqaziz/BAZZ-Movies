package com.waffiq.bazz_movies.data.remote.retrofit

import com.waffiq.bazz_movies.data.remote.response.countryip.CountryIPResponse
import retrofit2.Response
import retrofit2.http.GET

interface CountryIPApiService {

  @GET("/")
  suspend fun getIP(): Response<CountryIPResponse>

}
