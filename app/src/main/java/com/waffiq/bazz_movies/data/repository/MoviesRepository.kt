package com.waffiq.bazz_movies.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.waffiq.bazz_movies.data.model.Genre
import com.waffiq.bazz_movies.data.model.Movie
import com.waffiq.bazz_movies.data.model.Search
import com.waffiq.bazz_movies.data.paging.SearchPagingSource
import com.waffiq.bazz_movies.data.paging.TopRatedMoviePagingSource
import com.waffiq.bazz_movies.data.paging.TrendingPagingSource
import com.waffiq.bazz_movies.data.paging.UpcomingMoviesPagingSource
import com.waffiq.bazz_movies.data.remote.ResultResponse
import com.waffiq.bazz_movies.data.remote.response.GenresResponse
import com.waffiq.bazz_movies.data.remote.response.MoviesResponse
import com.waffiq.bazz_movies.data.remote.response.ResultItem
import com.waffiq.bazz_movies.data.remote.retrofit.ApiConfig
import com.waffiq.bazz_movies.data.remote.retrofit.ApiService
import com.waffiq.bazz_movies.data.room.MovieDatabase
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesRepository(
  private val storyDatabase: MovieDatabase,
  private val apiService: ApiService,
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

  init {
    getMoviesGenres()
  }

  private var _moviesGenres = MutableLiveData<List<Genre>>()
  val movieGenres: LiveData<List<Genre>> = _moviesGenres

  private fun getMoviesGenres(){
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
    val _itemMovie = MutableLiveData<List<Movie>>()

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
              _itemMovie.value = response.body()?.results
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
    return _itemMovie
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

  companion object {
    private const val TAG = "MoviesRepository"
  }
}