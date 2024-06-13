package com.waffiq.bazz_movies.domain.repository

import androidx.paging.PagingData
import com.waffiq.bazz_movies.data.remote.post_body.FavoritePostModel
import com.waffiq.bazz_movies.data.remote.post_body.RatePostModel
import com.waffiq.bazz_movies.data.remote.post_body.WatchlistPostModel
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
import com.waffiq.bazz_movies.utils.NetworkResult
import kotlinx.coroutines.flow.Flow

interface IMoviesRepository {

  // region PAGING FUNCTION
  fun getPagingTopRatedMovies(): Flow<PagingData<ResultItem>>

  fun getPagingPopularMovies(): Flow<PagingData<ResultItem>>

  fun getPagingFavoriteMovies(sessionId: String): Flow<PagingData<ResultItem>>

  fun getPagingFavoriteTv(sessionId: String): Flow<PagingData<ResultItem>>

  fun getPagingWatchlistMovies(sessionId: String): Flow<PagingData<ResultItem>>

  fun getPagingWatchlistTv(sessionId: String): Flow<PagingData<ResultItem>>

  fun getPagingPopularTv(): Flow<PagingData<ResultItem>>

  fun getPagingOnTv(): Flow<PagingData<ResultItem>>

  fun getPagingAiringTodayTv(): Flow<PagingData<ResultItem>>

  fun getPagingTrendingWeek(region: String): Flow<PagingData<ResultItem>>

  fun getPagingTrendingDay(region: String): Flow<PagingData<ResultItem>>

  fun getPagingMovieRecommendation(movieId: Int): Flow<PagingData<ResultItem>>

  fun getPagingTvRecommendation(tvId: Int): Flow<PagingData<ResultItem>>

  fun getPagingUpcomingMovies(region: String): Flow<PagingData<ResultItem>>

  fun getPagingPlayingNowMovies(region: String): Flow<PagingData<ResultItem>>

  fun getPagingTopRatedTv(): Flow<PagingData<ResultItem>>

  fun getPagingSearch(query: String): Flow<PagingData<ResultsItemSearch>>
  // endregion PAGING FUNCTION

  // region DETAIL
  suspend fun getDetailOMDb(imdbId: String): Flow<NetworkResult<OMDbDetails>>

  suspend fun getDetailMovie(movieId: Int): Flow<NetworkResult<DetailMovie>>

  suspend fun getDetailTv(tvId: Int): Flow<NetworkResult<DetailTv>>

  suspend fun getExternalTvId(tvId: Int): Flow<NetworkResult<ExternalTvID>>

  suspend fun getVideoMovies(movieId: Int): Flow<NetworkResult<Video>>

  suspend fun getVideoTv(tvId: Int): Flow<NetworkResult<Video>>

  suspend fun getCreditMovies(movieId: Int): Flow<NetworkResult<MovieTvCredits>>

  suspend fun getCreditTv(tvId: Int): Flow<NetworkResult<MovieTvCredits>>

  suspend fun getStatedMovie(sessionId: String, movieId: Int): Flow<NetworkResult<Stated>>

  suspend fun getStatedTv(sessionId: String, tvId: Int): Flow<NetworkResult<Stated>>
  // endregion DETAIL

  // region POST FAVORITE AND WATCHLIST
  suspend fun postFavorite(
    sessionId: String,
    fav: FavoritePostModel,
    userId: Int
  ): NetworkResult<PostFavoriteWatchlist>

  suspend fun postWatchlist(
    sessionId: String,
    wtc: WatchlistPostModel,
    userId: Int
  ): NetworkResult<PostFavoriteWatchlist>

  suspend fun postMovieRate(
    sessionId: String,
    data: RatePostModel,
    movieId: Int
  ): NetworkResult<Post>

  suspend fun postTvRate(
    sessionId: String,
    data: RatePostModel,
    tvId: Int
  ): NetworkResult<Post>
  // endregion POST FAVORITE AND WATCHLIST

  // region PERSON
  suspend fun getDetailPerson(id: Int): Flow<NetworkResult<DetailPerson>>

  suspend fun getKnownForPerson(id: Int): Flow<NetworkResult<CombinedCreditPerson>>

  suspend fun getImagePerson(id: Int): Flow<NetworkResult<ImagePerson>>

  suspend fun getExternalIDPerson(id: Int): Flow<NetworkResult<ExternalIDPerson>>
  // endregion PERSON

  // region LOCAL DATABASE
  val favoriteMoviesFromDB: Flow<List<Favorite>>

  val watchlistMovieFromDB: Flow<List<Favorite>>

  val watchlistTvFromDB: Flow<List<Favorite>>

  val favoriteTvFromDB: Flow<List<Favorite>>

  suspend fun insertToDB(fav: Favorite, callback: (Int) -> Unit)

  suspend fun deleteFromDB(fav: Favorite)

  suspend fun deleteAll(callback: (Int) -> Unit)

  suspend fun isFavoriteDB(id: Int, mediaType: String): Boolean

  suspend fun isWatchlistDB(id: Int, mediaType: String): Boolean

  suspend fun updateFavoriteItemDB(isDelete: Boolean, fav: Favorite)

  suspend fun updateWatchlistItemDB(isDelete: Boolean, fav: Favorite)
// endregion DATABASE
}