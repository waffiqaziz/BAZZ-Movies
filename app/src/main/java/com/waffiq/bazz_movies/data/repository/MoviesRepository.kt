package com.waffiq.bazz_movies.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.waffiq.bazz_movies.data.local.LocalDataSource
import com.waffiq.bazz_movies.data.local.model.Favorite
import com.waffiq.bazz_movies.data.local.model.Search
import com.waffiq.bazz_movies.data.paging.*
import com.waffiq.bazz_movies.data.remote.response.*
import com.waffiq.bazz_movies.data.remote.retrofit.ApiConfig
import com.waffiq.bazz_movies.data.remote.retrofit.ApiService
import com.waffiq.bazz_movies.utils.AppExecutors
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesRepository(
  private val apiService: ApiService,
  private val localDataSource: LocalDataSource,
  private val appExecutors: AppExecutors
) {
  private var _creditCastMovies = MutableLiveData<List<CastItem>>()
  val creditCastMovies: LiveData<List<CastItem>> = _creditCastMovies

  private var _creditsCrewMovies = MutableLiveData<List<CrewItem>>()
  val creditCrewMovies: LiveData<List<CrewItem>> = _creditsCrewMovies

  private var _creditCastTv = MutableLiveData<List<CastItem>>()
  val creditCastTv: LiveData<List<CastItem>> = _creditCastTv

  private var _creditsCrewTv = MutableLiveData<List<CrewItem>>()
  val creditCrewTv: LiveData<List<CrewItem>> = _creditsCrewTv

  fun getPagingTopRatedMovies(): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(
        pageSize = 5
      ),
      pagingSourceFactory = {
        TopRatedMoviePagingSource(apiService)
      }
    ).flow
  }

  fun getPagingPopularMovies(): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(
        pageSize = 5
      ),
      pagingSourceFactory = {
        PopularMoviePagingSource(apiService)
      }
    ).flow
  }

  fun getPagingPopularTv(): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(
        pageSize = 5
      ),
      pagingSourceFactory = {
        PopularTvPagingSource(apiService)
      }
    ).flow
  }

  fun getPagingOnTv(): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(
        pageSize = 5
      ),
      pagingSourceFactory = {
        OnTvPagingSource(apiService)
      }
    ).flow
  }

  fun getPagingAiringTodayTv(): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(
        pageSize = 5
      ),
      pagingSourceFactory = {
        AiringTodayTvPagingSource(apiService)
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

  fun getPagingUpcomingMovies(): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(
        pageSize = 20
      ),
      pagingSourceFactory = {
        UpcomingMoviesPagingSource(apiService)
      }
    ).flow
  }

  fun getPagingPlayingNowMovies(): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(
        pageSize = 20
      ),
      pagingSourceFactory = {
        PlayingNowMoviesPagingSource(apiService)
      }
    ).flow
  }

  fun getPagingTopRatedTv(): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(
        pageSize = 5
      ),
      pagingSourceFactory = {
        TopRatedTvPagingSource(apiService)
      }
    ).flow
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

  fun getCreditMovies(movieId: Int) {
    val client = ApiConfig
      .getApiService()
      .getCreditMovies(movieId)

    client.enqueue(object : Callback<CreditsResponse> {
      override fun onResponse(
        call: Call<CreditsResponse>,
        response: Response<CreditsResponse>
      ) {
        if (response.isSuccessful) {
          val responseBody = response.body()
          if (responseBody != null) {
            _creditsCrewMovies.value = responseBody.crew
            _creditCastMovies.value = responseBody.cast
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

  fun getCreditTv(tvId: Int) {
    val client = ApiConfig
      .getApiService()
      .getCreditTv(tvId)

    client.enqueue(object : Callback<CreditsResponse> {
      override fun onResponse(
        call: Call<CreditsResponse>,
        response: Response<CreditsResponse>
      ) {
        if (response.isSuccessful) {
          val responseBody = response.body()
          if (responseBody != null) {
            _creditsCrewTv.value = responseBody.crew
            _creditCastTv.value = responseBody.cast
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

  fun getAllFavorite(): LiveData<List<Favorite>> = localDataSource.getAllFavorite()

  fun getSpecificFavorite(name: String): LiveData<List<Favorite>> =
    localDataSource.getSpecificFavorite(name)

  fun insert(fav: Favorite) {
    appExecutors.diskIO().execute { localDataSource.insertFavorite(fav) }
  }

  fun delete(fav: Favorite) {
    appExecutors.diskIO().execute { localDataSource.deleteItemFavorite(fav) }
  }

  fun isFavorite(id: Int) = localDataSource.isFavorite(id)

  companion object {
    private const val TAG = "MoviesRepository "

    @Volatile
    private var instance: MoviesRepository? = null

    fun getInstance(
      apiService: ApiService,
      localData: LocalDataSource,
      appExecutors: AppExecutors
    ): MoviesRepository =
      instance ?: synchronized(this) {
        instance ?: MoviesRepository(apiService, localData, appExecutors)
      }
  }
}