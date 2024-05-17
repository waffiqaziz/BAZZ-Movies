package com.waffiq.bazz_movies.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.waffiq.bazz_movies.data.local.datasource.LocalDataSource
import com.waffiq.bazz_movies.data.remote.FavoritePostModel
import com.waffiq.bazz_movies.data.remote.RatePostModel
import com.waffiq.bazz_movies.data.remote.WatchlistPostModel
import com.waffiq.bazz_movies.data.remote.datasource.MovieDataSource
import com.waffiq.bazz_movies.data.remote.response.tmdb.PostRateResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.PostResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.ResultsItemSearchResponse
import com.waffiq.bazz_movies.domain.model.Favorite
import com.waffiq.bazz_movies.domain.model.ResultItem
import com.waffiq.bazz_movies.domain.model.Stated
import com.waffiq.bazz_movies.domain.model.detail.DetailMovie
import com.waffiq.bazz_movies.domain.model.detail.DetailTv
import com.waffiq.bazz_movies.domain.model.detail.ExternalTvID
import com.waffiq.bazz_movies.domain.model.detail.MovieTvCredits
import com.waffiq.bazz_movies.domain.model.detail.Video
import com.waffiq.bazz_movies.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.domain.model.person.CombinedCreditPerson
import com.waffiq.bazz_movies.domain.model.person.DetailPerson
import com.waffiq.bazz_movies.domain.model.person.ExternalIDPerson
import com.waffiq.bazz_movies.domain.model.person.ImagePerson
import com.waffiq.bazz_movies.utils.DataMapper.mapEntitiesToDomainFavorite
import com.waffiq.bazz_movies.utils.DataMapper.mapResultItemResponseToResultItem
import com.waffiq.bazz_movies.utils.DataMapper.toCombinedCredit
import com.waffiq.bazz_movies.utils.DataMapper.toDetailMovie
import com.waffiq.bazz_movies.utils.DataMapper.toDetailPerson
import com.waffiq.bazz_movies.utils.DataMapper.toDetailTv
import com.waffiq.bazz_movies.utils.DataMapper.toExternalTvID
import com.waffiq.bazz_movies.utils.DataMapper.toExternalIDPerson
import com.waffiq.bazz_movies.utils.DataMapper.toFavoriteEntity
import com.waffiq.bazz_movies.utils.DataMapper.toImagePerson
import com.waffiq.bazz_movies.utils.DataMapper.toMovieTvCredits
import com.waffiq.bazz_movies.utils.DataMapper.toOMDbDetails
import com.waffiq.bazz_movies.utils.DataMapper.toStated
import com.waffiq.bazz_movies.utils.DataMapper.toVideo
import com.waffiq.bazz_movies.utils.NetworkResult
import com.waffiq.bazz_movies.utils.Status
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MoviesRepository(
  private val localDataSource: LocalDataSource,
  private val movieDataSource: MovieDataSource
) {
  // region PAGING FUNCTION
  fun getPagingTopRatedMovies(): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingTopRatedMovies().map { pagingData ->
      pagingData.map { response ->
        mapResultItemResponseToResultItem(response)
      }
    }


  fun getPagingPopularMovies(): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingPopularMovies().map { pagingData ->
      pagingData.map { response ->
        mapResultItemResponseToResultItem(response)
      }
    }

  fun getPagingFavoriteMovies(sessionId: String): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingFavoriteMovies(sessionId).map { pagingData ->
      pagingData.map { response ->
        mapResultItemResponseToResultItem(response)
      }
    }

  fun getPagingFavoriteTv(sessionId: String): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingFavoriteTv(sessionId).map { pagingData ->
      pagingData.map { response ->
        mapResultItemResponseToResultItem(response)
      }
    }

  fun getPagingWatchlistMovies(sessionId: String): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingWatchlistMovies(sessionId).map { pagingData ->
      pagingData.map { response ->
        mapResultItemResponseToResultItem(response)
      }
    }

  fun getPagingWatchlistTv(sessionId: String): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingWatchlistTv(sessionId).map { pagingData ->
      pagingData.map { response ->
        mapResultItemResponseToResultItem(response)
      }
    }

  fun getPagingPopularTv(): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingPopularTv().map { pagingData ->
      pagingData.map { response ->
        mapResultItemResponseToResultItem(response)
      }
    }

  fun getPagingOnTv(): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingOnTv().map { pagingData ->
      pagingData.map { response ->
        mapResultItemResponseToResultItem(response)
      }
    }

  fun getPagingAiringTodayTv(): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingAiringTodayTv().map { pagingData ->
      pagingData.map { response ->
        mapResultItemResponseToResultItem(response)
      }
    }

  fun getPagingTrendingWeek(region: String): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingTrendingWeek(region).map { pagingData ->
      pagingData.map { response ->
        mapResultItemResponseToResultItem(response)
      }
    }

  fun getPagingTrendingDay(region: String): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingTrendingDay(region).map { pagingData ->
      pagingData.map { response ->
        mapResultItemResponseToResultItem(response)
      }
    }

  fun getPagingMovieRecommendation(movieId: Int): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingMovieRecommendation(movieId).map { pagingData ->
      pagingData.map { response ->
        mapResultItemResponseToResultItem(response)
      }
    }

  fun getPagingTvRecommendation(tvId: Int): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingTvRecommendation(tvId).map { pagingData ->
      pagingData.map { response ->
        mapResultItemResponseToResultItem(response)
      }
    }

  fun getPagingUpcomingMovies(region: String): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingUpcomingMovies(region).map { pagingData ->
      pagingData.map { response ->
        mapResultItemResponseToResultItem(response)
      }
    }

  fun getPagingPlayingNowMovies(region: String): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingPlayingNowMovies(region).map { pagingData ->
      pagingData.map { response ->
        mapResultItemResponseToResultItem(response)
      }
    }

  fun getPagingTopRatedTv(): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingTopRatedTv().map { pagingData ->
      pagingData.map { response ->
        mapResultItemResponseToResultItem(response)
      }
    }

  fun getPagingSearch(query: String): Flow<PagingData<ResultsItemSearchResponse>> =
    movieDataSource.getPagingSearch(query)
  // endregion PAGING FUNCTION

  // region DETAIL
  suspend fun getDetailOMDb(imdbId: String): Flow<NetworkResult<OMDbDetails>> =
    movieDataSource.getDetailOMDb(imdbId).map { networkResult ->
      when (networkResult.status) {
        Status.SUCCESS -> NetworkResult.success(networkResult.data?.toOMDbDetails())
        Status.ERROR -> NetworkResult.error(networkResult.message ?: "Unknown error")
        Status.LOADING -> NetworkResult.loading()
      }
    }

  suspend fun getDetailMovie(id: Int): Flow<NetworkResult<DetailMovie>> =
    movieDataSource.getDetailMovie(id).map { networkResult ->
      when (networkResult.status) {
        Status.SUCCESS -> NetworkResult.success(networkResult.data?.toDetailMovie())
        Status.ERROR -> NetworkResult.error(networkResult.message ?: "Unknown error")
        Status.LOADING -> NetworkResult.loading()
      }
    }

  suspend fun getDetailTv(tvId: Int): Flow<NetworkResult<DetailTv>> =
    movieDataSource.getDetailTv(tvId).map { networkResult ->
      when (networkResult.status) {
        Status.SUCCESS -> NetworkResult.success(networkResult.data?.toDetailTv())
        Status.ERROR -> NetworkResult.error(networkResult.message ?: "Unknown error")
        Status.LOADING -> NetworkResult.loading()
      }
    }

  suspend fun getExternalTvId(tvId: Int): Flow<NetworkResult<ExternalTvID>> =
    movieDataSource.getExternalTvId(tvId).map { networkResult ->
      when (networkResult.status) {
        Status.SUCCESS -> NetworkResult.success(networkResult.data?.toExternalTvID())
        Status.ERROR -> NetworkResult.error(networkResult.message ?: "Unknown error")
        Status.LOADING -> NetworkResult.loading()
      }
    }

  suspend fun getVideoMovies(movieId: Int): Flow<NetworkResult<Video>> =
    movieDataSource.getVideoMovies(movieId).map { networkResult ->
      when (networkResult.status) {
        Status.SUCCESS -> NetworkResult.success(networkResult.data?.toVideo())
        Status.ERROR -> NetworkResult.error(networkResult.message ?: "Unknown error")
        Status.LOADING -> NetworkResult.loading()
      }
    }

  suspend fun getVideoTv(tvId: Int): Flow<NetworkResult<Video>> =
    movieDataSource.getVideoTv(tvId).map { networkResult ->
      when (networkResult.status) {
        Status.SUCCESS -> NetworkResult.success(networkResult.data?.toVideo())
        Status.ERROR -> NetworkResult.error(networkResult.message ?: "Unknown error")
        Status.LOADING -> NetworkResult.loading()
      }
    }

  suspend fun getCreditMovies(movieId: Int): Flow<NetworkResult<MovieTvCredits>> =
    movieDataSource.getCreditMovies(movieId).map { networkResult ->
      when (networkResult.status) {
        Status.SUCCESS -> NetworkResult.success(networkResult.data?.toMovieTvCredits())
        Status.ERROR -> NetworkResult.error(networkResult.message ?: "Unknown error")
        Status.LOADING -> NetworkResult.loading()
      }
    }

  suspend fun getCreditTv(tvId: Int): Flow<NetworkResult<MovieTvCredits>> =
    movieDataSource.getCreditTv(tvId).map { networkResult ->
      when (networkResult.status) {
        Status.SUCCESS -> NetworkResult.success(networkResult.data?.toMovieTvCredits())
        Status.ERROR -> NetworkResult.error(networkResult.message ?: "Unknown error")
        Status.LOADING -> NetworkResult.loading()
      }
    }

  suspend fun getStatedMovie(sessionId: String, id: Int): Flow<NetworkResult<Stated>> =
    movieDataSource.getStatedMovie(sessionId, id).map { networkResult ->
      when (networkResult.status) {
        Status.SUCCESS -> NetworkResult.success(networkResult.data?.toStated())
        Status.ERROR -> NetworkResult.error(networkResult.message ?: "Unknown error")
        Status.LOADING -> NetworkResult.loading()
      }
    }

  suspend fun getStatedTv(sessionId: String, id: Int): Flow<NetworkResult<Stated>> =
    movieDataSource.getStatedTv(sessionId, id).map { networkResult ->
      when (networkResult.status) {
        Status.SUCCESS -> NetworkResult.success(networkResult.data?.toStated())
        Status.ERROR -> NetworkResult.error(networkResult.message ?: "Unknown error")
        Status.LOADING -> NetworkResult.loading()
      }
    }
  // endregion DETAIL

  // region POST FAVORITE AND WATCHLIST
  suspend fun postFavorite(
    sessionId: String,
    fav: FavoritePostModel,
    userId: Int
  ): NetworkResult<PostResponse> = movieDataSource.postFavorite(sessionId, fav, userId)

  suspend fun postWatchlist(
    sessionId: String,
    wtc: WatchlistPostModel,
    userId: Int
  ): NetworkResult<PostResponse> = movieDataSource.postWatchlist(sessionId, wtc, userId)

  suspend fun postMovieRate(
    sessionId: String,
    data: RatePostModel,
    movieId: Int
  ): NetworkResult<PostRateResponse> = movieDataSource.postMovieRate(sessionId, data, movieId)

  suspend fun postTvRate(
    sessionId: String,
    data: RatePostModel,
    tvId: Int
  ): NetworkResult<PostRateResponse> = movieDataSource.postTvRate(sessionId, data, tvId)
  // endregion POST FAVORITE AND WATCHLIST

  // region PERSON
  suspend fun getDetailPerson(id: Int): Flow<NetworkResult<DetailPerson>> =
    movieDataSource.getDetailPerson(id).map { networkResult ->
      when (networkResult.status) {
        Status.SUCCESS -> NetworkResult.success(networkResult.data?.toDetailPerson())
        Status.ERROR -> NetworkResult.error(networkResult.message ?: "Unknown error")
        Status.LOADING -> NetworkResult.loading()
      }
    }

  suspend fun getKnownForPerson(id: Int): Flow<NetworkResult<CombinedCreditPerson>> =
    movieDataSource.getKnownForPerson(id).map { networkResult ->
      when (networkResult.status) {
        Status.SUCCESS -> NetworkResult.success(networkResult.data?.toCombinedCredit())
        Status.ERROR -> NetworkResult.error(networkResult.message ?: "Unknown error")
        Status.LOADING -> NetworkResult.loading()
      }
    }

  suspend fun getImagePerson(id: Int): Flow<NetworkResult<ImagePerson>> =
    movieDataSource.getImagePerson(id).map { networkResult ->
      when (networkResult.status) {
        Status.SUCCESS -> NetworkResult.success(networkResult.data?.toImagePerson())
        Status.ERROR -> NetworkResult.error(networkResult.message ?: "Unknown error")
        Status.LOADING -> NetworkResult.loading()
      }
    }

  suspend fun getExternalIDPerson(id: Int): Flow<NetworkResult<ExternalIDPerson>> =
    movieDataSource.getExternalIDPerson(id).map { networkResult ->
      when (networkResult.status) {
        Status.SUCCESS -> NetworkResult.success(networkResult.data?.toExternalIDPerson())
        Status.ERROR -> NetworkResult.error(networkResult.message ?: "Unknown error")
        Status.LOADING -> NetworkResult.loading()
      }
    }
  // endregion PERSON

  // region DATABASE
  val favoriteMoviesFromDB: Flow<List<Favorite>> =
    localDataSource.getFavoriteMovies.map { list ->
      mapEntitiesToDomainFavorite(list)
    }

  val watchlistMovieFromDB: Flow<List<Favorite>> =
    localDataSource.getWatchlistMovies.map { list ->
      mapEntitiesToDomainFavorite(list)
    }

  val watchlistTvFromDB: Flow<List<Favorite>> =
    localDataSource.getWatchlistTv.map { list ->
      mapEntitiesToDomainFavorite(list)
    }

  val favoriteTvFromDB: Flow<List<Favorite>> =
    localDataSource.getFavoriteTv.map { list ->
      mapEntitiesToDomainFavorite(list)
    }

  suspend fun insertToDB(fav: Favorite, callback: (Int) -> Unit) {
    val resultCode = localDataSource.insert(fav.toFavoriteEntity())
    callback.invoke(resultCode)
  }

  suspend fun deleteFromDB(fav: Favorite) {
    localDataSource.deleteItemFromDB(fav.mediaId, fav.mediaType)
  }

  suspend fun deleteAll(callback: (Int) -> Unit) {
    val resultCode = localDataSource.deleteAll()
    callback.invoke(resultCode)
  }

  suspend fun isFavoriteDB(id: Int, mediaType: String): Boolean =
    localDataSource.isFavorite(id, mediaType)

  suspend fun isWatchlistDB(id: Int, mediaType: String): Boolean =
    localDataSource.isWatchlist(id, mediaType)

  suspend fun updateFavoriteItemDB(isDelete: Boolean, fav: Favorite) {
    if (isDelete) { // update set is_favorite = false (item on favorite to delete)
      localDataSource.update(
        isFavorite = false,
        isWatchlist = fav.isWatchlist,
        id = fav.mediaId,
        mediaType = fav.mediaType
      )
    } else {  // update set is_favorite = true (add favorite item already on watchlist)
      localDataSource.update(
        isFavorite = true,
        isWatchlist = fav.isWatchlist,
        id = fav.mediaId,
        mediaType = fav.mediaType
      )
    }
  }

  suspend fun updateWatchlistItemDB(isDelete: Boolean, fav: Favorite) {
    if (isDelete) { // update set is_watchlist = false (item on watchlist to delete)
      localDataSource.update(
        isFavorite = fav.isFavorite,
        isWatchlist = false,
        id = fav.mediaId,
        mediaType = fav.mediaType
      )
    } else { // update set is_watchlist = true (add watchlist item already on favorite)
      localDataSource.update(
        isFavorite = fav.isFavorite,
        isWatchlist = true,
        id = fav.mediaId,
        mediaType = fav.mediaType
      )
    }
  }
  // endregion DATABASE

  companion object {
    private const val TAG = "MoviesRepository "
  }
}