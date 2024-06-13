package com.waffiq.bazz_movies.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.waffiq.bazz_movies.data.local.datasource.LocalDataSource
import com.waffiq.bazz_movies.data.remote.post_body.FavoritePostModel
import com.waffiq.bazz_movies.data.remote.post_body.RatePostModel
import com.waffiq.bazz_movies.data.remote.post_body.WatchlistPostModel
import com.waffiq.bazz_movies.data.remote.datasource.MovieDataSource
import com.waffiq.bazz_movies.domain.model.Favorite
import com.waffiq.bazz_movies.domain.model.ResultItem
import com.waffiq.bazz_movies.domain.model.search.ResultsItemSearch
import com.waffiq.bazz_movies.domain.model.Stated
import com.waffiq.bazz_movies.domain.model.detail.DetailMovie
import com.waffiq.bazz_movies.domain.model.detail.tv.DetailTv
import com.waffiq.bazz_movies.domain.model.detail.tv.ExternalTvID
import com.waffiq.bazz_movies.domain.model.detail.MovieTvCredits
import com.waffiq.bazz_movies.domain.model.detail.Video
import com.waffiq.bazz_movies.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.domain.model.person.CombinedCreditPerson
import com.waffiq.bazz_movies.domain.model.person.DetailPerson
import com.waffiq.bazz_movies.domain.model.person.ExternalIDPerson
import com.waffiq.bazz_movies.domain.model.person.ImagePerson
import com.waffiq.bazz_movies.domain.model.post.PostFavoriteWatchlist
import com.waffiq.bazz_movies.domain.model.post.Post
import com.waffiq.bazz_movies.domain.repository.IMoviesRepository
import com.waffiq.bazz_movies.utils.mappers.DetailMovieTvMapper.toCombinedCredit
import com.waffiq.bazz_movies.utils.mappers.DetailMovieTvMapper.toDetailMovie
import com.waffiq.bazz_movies.utils.mappers.DetailMovieTvMapper.toDetailTv
import com.waffiq.bazz_movies.utils.mappers.UniversalMapper.toResultItem
import com.waffiq.bazz_movies.utils.mappers.DetailMovieTvMapper.toStated
import com.waffiq.bazz_movies.utils.mappers.DetailMovieTvMapper.toExternalTvID
import com.waffiq.bazz_movies.utils.mappers.DetailMovieTvMapper.toMovieTvCredits
import com.waffiq.bazz_movies.utils.mappers.DetailMovieTvMapper.toOMDbDetails
import com.waffiq.bazz_movies.utils.mappers.PostMapper.toPostFavoriteWatchlist
import com.waffiq.bazz_movies.utils.mappers.PostMapper.toPost
import com.waffiq.bazz_movies.utils.mappers.SearchMapper.toResultItemSearch
import com.waffiq.bazz_movies.utils.mappers.DetailMovieTvMapper.toVideo
import com.waffiq.bazz_movies.utils.mappers.DatabaseMapper.toFavorite
import com.waffiq.bazz_movies.utils.mappers.DatabaseMapper.toFavoriteEntity
import com.waffiq.bazz_movies.utils.mappers.PersonMapper.toDetailPerson
import com.waffiq.bazz_movies.utils.mappers.PersonMapper.toExternalIDPerson
import com.waffiq.bazz_movies.utils.mappers.PersonMapper.toImagePerson
import com.waffiq.bazz_movies.utils.NetworkResult
import com.waffiq.bazz_movies.utils.Status
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MoviesRepository(
  private val localDataSource: LocalDataSource,
  private val movieDataSource: MovieDataSource
) : IMoviesRepository {

  // region PAGING FUNCTION
  override fun getPagingTopRatedMovies(): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingTopRatedMovies().map { pagingData ->
      pagingData.map { response ->
        response.toResultItem()
      }
    }

  override fun getPagingPopularMovies(): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingPopularMovies().map { pagingData ->
      pagingData.map { response ->
        response.toResultItem()
      }
    }

  override fun getPagingFavoriteMovies(sessionId: String): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingFavoriteMovies(sessionId).map { pagingData ->
      pagingData.map { response ->
        response.toResultItem()
      }
    }

  override fun getPagingFavoriteTv(sessionId: String): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingFavoriteTv(sessionId).map { pagingData ->
      pagingData.map { response ->
        response.toResultItem()
      }
    }

  override fun getPagingWatchlistMovies(sessionId: String): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingWatchlistMovies(sessionId).map { pagingData ->
      pagingData.map { response ->
        response.toResultItem()
      }
    }

  override fun getPagingWatchlistTv(sessionId: String): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingWatchlistTv(sessionId).map { pagingData ->
      pagingData.map { response ->
        response.toResultItem()
      }
    }

  override fun getPagingPopularTv(): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingPopularTv().map { pagingData ->
      pagingData.map { response ->
        response.toResultItem()
      }
    }

  override fun getPagingOnTv(): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingOnTv().map { pagingData ->
      pagingData.map { response ->
        response.toResultItem()
      }
    }

  override fun getPagingAiringTodayTv(): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingAiringTodayTv().map { pagingData ->
      pagingData.map { response ->
        response.toResultItem()
      }
    }

  override fun getPagingTrendingWeek(region: String): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingTrendingWeek(region).map { pagingData ->
      pagingData.map { response ->
        response.toResultItem()
      }
    }

  override fun getPagingTrendingDay(region: String): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingTrendingDay(region).map { pagingData ->
      pagingData.map { response ->
        response.toResultItem()
      }
    }

  override fun getPagingMovieRecommendation(movieId: Int): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingMovieRecommendation(movieId).map { pagingData ->
      pagingData.map { response ->
        response.toResultItem()
      }
    }

  override fun getPagingTvRecommendation(tvId: Int): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingTvRecommendation(tvId).map { pagingData ->
      pagingData.map { response ->
        response.toResultItem()
      }
    }

  override fun getPagingUpcomingMovies(region: String): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingUpcomingMovies(region).map { pagingData ->
      pagingData.map { response ->
        response.toResultItem()
      }
    }

  override fun getPagingPlayingNowMovies(region: String): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingPlayingNowMovies(region).map { pagingData ->
      pagingData.map { response ->
        response.toResultItem()
      }
    }

  override fun getPagingTopRatedTv(): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingTopRatedTv().map { pagingData ->
      pagingData.map { response ->
        response.toResultItem()
      }
    }

  override fun getPagingSearch(query: String): Flow<PagingData<ResultsItemSearch>> =
    movieDataSource.getPagingSearch(query).map { pagingData ->
      pagingData.map { response ->
        response.toResultItemSearch()
      }
    }
  // endregion PAGING FUNCTION

  // region DETAIL
  override suspend fun getDetailOMDb(imdbId: String): Flow<NetworkResult<OMDbDetails>> =
    movieDataSource.getDetailOMDb(imdbId).map { networkResult ->
      when (networkResult.status) {
        Status.SUCCESS -> NetworkResult.success(networkResult.data?.toOMDbDetails())
        Status.ERROR -> NetworkResult.error(networkResult.message ?: "Unknown error")
        Status.LOADING -> NetworkResult.loading()
      }
    }

  override suspend fun getDetailMovie(movieId: Int): Flow<NetworkResult<DetailMovie>> =
    movieDataSource.getDetailMovie(movieId).map { networkResult ->
      when (networkResult.status) {
        Status.SUCCESS -> NetworkResult.success(networkResult.data?.toDetailMovie())
        Status.ERROR -> NetworkResult.error(networkResult.message ?: "Unknown error")
        Status.LOADING -> NetworkResult.loading()
      }
    }

  override suspend fun getDetailTv(tvId: Int): Flow<NetworkResult<DetailTv>> =
    movieDataSource.getDetailTv(tvId).map { networkResult ->
      when (networkResult.status) {
        Status.SUCCESS -> NetworkResult.success(networkResult.data?.toDetailTv())
        Status.ERROR -> NetworkResult.error(networkResult.message ?: "Unknown error")
        Status.LOADING -> NetworkResult.loading()
      }
    }

  override suspend fun getExternalTvId(tvId: Int): Flow<NetworkResult<ExternalTvID>> =
    movieDataSource.getExternalTvId(tvId).map { networkResult ->
      when (networkResult.status) {
        Status.SUCCESS -> NetworkResult.success(networkResult.data?.toExternalTvID())
        Status.ERROR -> NetworkResult.error(networkResult.message ?: "Unknown error")
        Status.LOADING -> NetworkResult.loading()
      }
    }

  override suspend fun getVideoMovies(movieId: Int): Flow<NetworkResult<Video>> =
    movieDataSource.getVideoMovies(movieId).map { networkResult ->
      when (networkResult.status) {
        Status.SUCCESS -> NetworkResult.success(networkResult.data?.toVideo())
        Status.ERROR -> NetworkResult.error(networkResult.message ?: "Unknown error")
        Status.LOADING -> NetworkResult.loading()
      }
    }

  override suspend fun getVideoTv(tvId: Int): Flow<NetworkResult<Video>> =
    movieDataSource.getVideoTv(tvId).map { networkResult ->
      when (networkResult.status) {
        Status.SUCCESS -> NetworkResult.success(networkResult.data?.toVideo())
        Status.ERROR -> NetworkResult.error(networkResult.message ?: "Unknown error")
        Status.LOADING -> NetworkResult.loading()
      }
    }

  override suspend fun getCreditMovies(movieId: Int): Flow<NetworkResult<MovieTvCredits>> =
    movieDataSource.getCreditMovies(movieId).map { networkResult ->
      when (networkResult.status) {
        Status.SUCCESS -> NetworkResult.success(networkResult.data?.toMovieTvCredits())
        Status.ERROR -> NetworkResult.error(networkResult.message ?: "Unknown error")
        Status.LOADING -> NetworkResult.loading()
      }
    }

  override suspend fun getCreditTv(tvId: Int): Flow<NetworkResult<MovieTvCredits>> =
    movieDataSource.getCreditTv(tvId).map { networkResult ->
      when (networkResult.status) {
        Status.SUCCESS -> NetworkResult.success(networkResult.data?.toMovieTvCredits())
        Status.ERROR -> NetworkResult.error(networkResult.message ?: "Unknown error")
        Status.LOADING -> NetworkResult.loading()
      }
    }

  override suspend fun getStatedMovie(
    sessionId: String,
    movieId: Int
  ): Flow<NetworkResult<Stated>> =
    movieDataSource.getStatedMovie(sessionId, movieId).map { networkResult ->
      when (networkResult.status) {
        Status.SUCCESS -> NetworkResult.success(networkResult.data?.toStated())
        Status.ERROR -> NetworkResult.error(networkResult.message ?: "Unknown error")
        Status.LOADING -> NetworkResult.loading()
      }
    }

  override suspend fun getStatedTv(sessionId: String, tvId: Int): Flow<NetworkResult<Stated>> =
    movieDataSource.getStatedTv(sessionId, tvId).map { networkResult ->
      when (networkResult.status) {
        Status.SUCCESS -> NetworkResult.success(networkResult.data?.toStated())
        Status.ERROR -> NetworkResult.error(networkResult.message ?: "Unknown error")
        Status.LOADING -> NetworkResult.loading()
      }
    }
  // endregion DETAIL

  // region POST FAVORITE AND WATCHLIST
  override suspend fun postFavorite(
    sessionId: String,
    fav: FavoritePostModel,
    userId: Int
  ): NetworkResult<PostFavoriteWatchlist> {
    val result = movieDataSource.postFavorite(sessionId, fav, userId)
    return when (result.status) {
      Status.SUCCESS -> NetworkResult.success(result.data?.toPostFavoriteWatchlist())
      Status.ERROR -> NetworkResult.error(result.message ?: "Unknown error")
      Status.LOADING -> NetworkResult.loading()
    }
  }

  override suspend fun postWatchlist(
    sessionId: String,
    wtc: WatchlistPostModel,
    userId: Int
  ): NetworkResult<PostFavoriteWatchlist> {
    val result = movieDataSource.postWatchlist(sessionId, wtc, userId)
    return when (result.status) {
      Status.SUCCESS -> NetworkResult.success(result.data?.toPostFavoriteWatchlist())
      Status.ERROR -> NetworkResult.error(result.message ?: "Unknown error")
      Status.LOADING -> NetworkResult.loading()
    }
  }

  override suspend fun postMovieRate(
    sessionId: String,
    data: RatePostModel,
    movieId: Int
  ): NetworkResult<Post> {
    val result = movieDataSource.postMovieRate(sessionId, data, movieId)
    return when (result.status) {
      Status.SUCCESS -> NetworkResult.success(result.data?.toPost())
      Status.ERROR -> NetworkResult.error(result.message ?: "Unknown error")
      Status.LOADING -> NetworkResult.loading()
    }
  }

  override suspend fun postTvRate(
    sessionId: String,
    data: RatePostModel,
    tvId: Int
  ): NetworkResult<Post> {
    val result = movieDataSource.postTvRate(sessionId, data, tvId)
    return when (result.status) {
      Status.SUCCESS -> NetworkResult.success(result.data?.toPost())
      Status.ERROR -> NetworkResult.error(result.message ?: "Unknown error")
      Status.LOADING -> NetworkResult.loading()
    }
  }
// endregion POST FAVORITE AND WATCHLIST

  // region PERSON
  override suspend fun getDetailPerson(id: Int): Flow<NetworkResult<DetailPerson>> =
    movieDataSource.getDetailPerson(id).map { networkResult ->
      when (networkResult.status) {
        Status.SUCCESS -> NetworkResult.success(networkResult.data?.toDetailPerson())
        Status.ERROR -> NetworkResult.error(networkResult.message ?: "Unknown error")
        Status.LOADING -> NetworkResult.loading()
      }
    }

  override suspend fun getKnownForPerson(id: Int): Flow<NetworkResult<CombinedCreditPerson>> =
    movieDataSource.getKnownForPerson(id).map { networkResult ->
      when (networkResult.status) {
        Status.SUCCESS -> NetworkResult.success(networkResult.data?.toCombinedCredit())
        Status.ERROR -> NetworkResult.error(networkResult.message ?: "Unknown error")
        Status.LOADING -> NetworkResult.loading()
      }
    }

  override suspend fun getImagePerson(id: Int): Flow<NetworkResult<ImagePerson>> =
    movieDataSource.getImagePerson(id).map { networkResult ->
      when (networkResult.status) {
        Status.SUCCESS -> NetworkResult.success(networkResult.data?.toImagePerson())
        Status.ERROR -> NetworkResult.error(networkResult.message ?: "Unknown error")
        Status.LOADING -> NetworkResult.loading()
      }
    }

  override suspend fun getExternalIDPerson(id: Int): Flow<NetworkResult<ExternalIDPerson>> =
    movieDataSource.getExternalIDPerson(id).map { networkResult ->
      when (networkResult.status) {
        Status.SUCCESS -> NetworkResult.success(networkResult.data?.toExternalIDPerson())
        Status.ERROR -> NetworkResult.error(networkResult.message ?: "Unknown error")
        Status.LOADING -> NetworkResult.loading()
      }
    }
  // endregion PERSON

  // region DATABASE
  override val favoriteMoviesFromDB: Flow<List<Favorite>> =
    localDataSource.getFavoriteMovies.map { list ->
      list.map { it.toFavorite() }
    }

  override val watchlistMovieFromDB: Flow<List<Favorite>> =
    localDataSource.getWatchlistMovies.map { list ->
      list.map { it.toFavorite() }
    }

  override val watchlistTvFromDB: Flow<List<Favorite>> =
    localDataSource.getWatchlistTv.map { list ->
      list.map { it.toFavorite() }
    }

  override val favoriteTvFromDB: Flow<List<Favorite>> =
    localDataSource.getFavoriteTv.map { list ->
      list.map { it.toFavorite() }
    }

  override suspend fun insertToDB(fav: Favorite, callback: (Int) -> Unit) {
    val resultCode = localDataSource.insert(fav.toFavoriteEntity())
    callback.invoke(resultCode)
  }

  override suspend fun deleteFromDB(fav: Favorite) {
    localDataSource.deleteItemFromDB(fav.mediaId, fav.mediaType)
  }

  override suspend fun deleteAll(callback: (Int) -> Unit) {
    val resultCode = localDataSource.deleteAll()
    callback.invoke(resultCode)
  }

  override suspend fun isFavoriteDB(id: Int, mediaType: String): Boolean =
    localDataSource.isFavorite(id, mediaType)

  override suspend fun isWatchlistDB(id: Int, mediaType: String): Boolean =
    localDataSource.isWatchlist(id, mediaType)

  override suspend fun updateFavoriteItemDB(isDelete: Boolean, fav: Favorite) {
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

  override suspend fun updateWatchlistItemDB(isDelete: Boolean, fav: Favorite) {
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