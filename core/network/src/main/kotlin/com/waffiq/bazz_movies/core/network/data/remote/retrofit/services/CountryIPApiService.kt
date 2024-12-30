package com.waffiq.bazz_movies.core.network.data.remote.retrofit.services

import com.waffiq.bazz_movies.core.network.data.remote.responses.countryip.CountryIPResponse
import retrofit2.Response
import retrofit2.http.GET

interface CountryIPApiService {

  @GET("/")
  suspend fun getIP(): Response<CountryIPResponse>
}
