package com.waffiq.bazz_movies.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.waffiq.bazz_movies.data.local.LocalDataSource
import com.waffiq.bazz_movies.data.local.model.Favorite
import com.waffiq.bazz_movies.data.local.model.Movie
import com.waffiq.bazz_movies.data.local.model.Search
import com.waffiq.bazz_movies.data.paging.SearchPagingSource
import com.waffiq.bazz_movies.data.paging.TopRatedMoviePagingSource
import com.waffiq.bazz_movies.data.paging.TrendingPagingSource
import com.waffiq.bazz_movies.data.paging.UpcomingMoviesPagingSource
import com.waffiq.bazz_movies.data.remote.response.*
import com.waffiq.bazz_movies.data.remote.retrofit.ApiConfig
import com.waffiq.bazz_movies.data.remote.retrofit.ApiService
import com.waffiq.bazz_movies.data.local.room.FavoriteDatabase
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesRepository(
  private val apiService: ApiService,
  private val localDataSource: LocalDataSource
) {

//  fun login(email: String, pass: String): LiveData<ResultResponse<LoginResult>> =
//    liveData {
//      emit(ResultResponse.Loading)
//      try {
//        val response = apiService.login(email, pass)
//        if (!response.error) {
//          emit(ResultResponse.Success(response.loginResult))
//        } else {
//          Log.e(TAG, "Login Fail: ${response.message}")
//          emit(ResultResponse.Error(response.message))
//        }
//      } catch (e: Exception) {
//        Log.e(TAG, "Login Exception: ${e.message.toString()} ")
//        emit(ResultResponse.Error(e.message.toString()))
//      }
//    }


  private var _creditsCast = MutableLiveData<List<CastItem>>()
  val creditCast: LiveData<List<CastItem>> = _creditsCast

  private var _creditsCrew = MutableLiveData<List<CrewItem>>()
  val creditCrew: LiveData<List<CrewItem>> = _creditsCrew

  fun getPagingTopRatedMovies(): Flow<PagingData<Movie>> {
    return Pager(
      config = PagingConfig(
        pageSize = 5
      ),
      pagingSourceFactory = {
        TopRatedMoviePagingSource(apiService)
      }
    ).flow
  }

  fun getPagingTrending(): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(
        pageSize = 5
      ),
      pagingSourceFactory = {
        TrendingPagingSource(apiService)
      }
    ).flow
  }

  fun getPagingUpComingMovies(): Flow<PagingData<Movie>> {
    return Pager(
      config = PagingConfig(
        pageSize = 20
      ),
      pagingSourceFactory = {
        UpcomingMoviesPagingSource(apiService)
      }
    ).flow
  }

  fun getMovieNowPlaying(): LiveData<List<Movie>> {
    val itemMovie = MutableLiveData<List<Movie>>()

    val client = ApiConfig
      .getApiService()
      .getMovieNowPlaying()

    client.enqueue(object : Callback<MoviesResponse> {
      override fun onResponse(
        call: Call<MoviesResponse>,
        response: Response<MoviesResponse>
      ) {

        if (response.isSuccessful) {
          val responseBody = response.body()
          if (responseBody != null) {
            if (responseBody.results.isEmpty()) {
              itemMovie.value = response.body()?.results
            }
          }
        } else {
          Log.e(TAG, "onFailure: ${response.message()}")
        }
      }

      override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
        Log.e(TAG, "onFailure: ${t.message}")
      }
    })
    return itemMovie
  }

  fun search(query: String): Flow<PagingData<Search>> {
    return Pager(
      config = PagingConfig(
        pageSize = 20
      ),
      pagingSourceFactory = {
        SearchPagingSource(apiService, query)
      }
    ).flow
  }

  fun getCredits(movieId : Int) {
    val client = ApiConfig
      .getApiService()
      .getCredits(movieId)

    client.enqueue(object : Callback<CreditsResponse> {
      override fun onResponse(
        call: Call<CreditsResponse>,
        response: Response<CreditsResponse>
      ) {
        if (response.isSuccessful) {
          val responseBody = response.body()
          if (responseBody != null) {
            _creditsCrew.value = response.body()!!.crew
            _creditsCast.value = response.body()!!.cast
          }
        } else {
          Log.e(TAG, "onFailure: ${response.message()}")
        }
      }

      override fun onFailure(call: Call<CreditsResponse>, t: Throwable) {
        Log.e(TAG, "onFailure: ${t.message}")
      }
    })
  }

  fun getFavorite(): PagingSource<Int, Favorite> = localDataSource.getAllFavorite()

  fun insert(fav: Favorite) {
    localDataSource.insertFavorite(fav)
  }

  fun delete(fav: Favorite) {
    localDataSource.deleteItemFavorite(fav)
  }

  fun isFavorite(id: Int) = localDataSource.isFavorite(id)

  companion object {
    private const val TAG = "MoviesRepository "

    @Volatile
    private var instance: MoviesRepository? = null

    fun getInstance(
      apiService: ApiService,
      localData: LocalDataSource,
    ): MoviesRepository =
      instance ?: synchronized(this) {
        instance ?: MoviesRepository(apiService,localData)
      }
  }
}