package com.waffiq.bazz_movies.feature.detail.testutils

import androidx.paging.AsyncPagingDataDiffer
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.domain.GenresItem
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.test.PagingDataHelperTest.TestDiffCallback
import com.waffiq.bazz_movies.core.test.PagingDataHelperTest.TestListCallback
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCredits
import com.waffiq.bazz_movies.feature.detail.domain.model.PostModelState
import com.waffiq.bazz_movies.feature.detail.domain.model.Video
import com.waffiq.bazz_movies.feature.detail.domain.model.VideoItem
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.MovieDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDates
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDatesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDatesItemValue
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.ContentRatings
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.ContentRatingsItem
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.DetailTv
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvExternalIds
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProvidersItem
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProviders
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow

// Used as data dumb testing
object HelperTest {

  const val IMDB_ID = "tt1234567"
  const val USER_REGION = "US"
  const val ERROR_MESSAGE = "Network error"

  // region MOVIE
  const val MOVIE_ID = 1001
  val detailMovie = MovieDetail(
    id = MOVIE_ID,
    runtime = 120,
    imdbId = IMDB_ID,
    voteAverage = 7.5,
    listGenres = listOf(GenresItem(id = 1, name = "Action")),
    releaseDates = ReleaseDates(
      listReleaseDatesItem = listOf(
        ReleaseDatesItem(
          iso31661 = "US",
          listReleaseDatesitemValue = listOf(
            ReleaseDatesItemValue(
              releaseDate = "2023-11-20T00:00:00.000Z",
              certification = "PG-13"
            )
          )
        )
      )
    )
  )

  val movieCredits = MediaCredits(
    cast = listOf(),
    crew = listOf(),
    id = MOVIE_ID
  )

  val movieMediaItem = MediaItem(
    mediaType = MOVIE_MEDIA_TYPE,
    title = "Transformers",
    id = 99999,
    adult = false,
    voteCount = 8000
  )
  // endregion MOVIE

  // region TV
  const val TV_ID = 2002
  val detailTv = DetailTv(
    id = TV_ID,
    status = "Returning Series",
    voteAverage = 8.2,
    listGenres = listOf(GenresItem(id = 5, name = "Drama")),
    contentRatings = ContentRatings(
      contentRatingsItem = listOf(
        ContentRatingsItem(iso31661 = "US", rating = "TV-MA")
      )
    ),
    firstAirDate = "2023-12-20T00:00:00.000Z"
  )
   val externalTvID = TvExternalIds(imdbId = "tt87654321")
   val tvCredits = MediaCredits(cast = listOf(), crew = listOf(), id = TV_ID)
  // endregion TV

  val video = Video(
    1234,
    results = listOf(VideoItem(name = "Trailer", type = "Trailer", key = "Link Trailer"))
  )

  val watchProviders = WatchProviders(
    results = mapOf(
      "US" to WatchProvidersItem(
        link = "https://some-provider.com",
        ads = null,
        buy = null,
        flatrate = null,
        free = null,
        rent = null
      )
    ),
    id = 1234
  )

  val omdbDetails = OMDbDetails(
    imdbID = IMDB_ID,
    title = "Some Movie",
    plot = "This is the plot.",
    genre = "Action, Adventure",
    director = "Jane Doe",
    writer = "John Smith",
    actors = "Actor A, Actor B",
    released = "2023-01-01",
    language = "English",
    country = "USA",
    awards = "3 wins",
    poster = "https://poster.url",
    metascore = "65",
    imdbRating = "7.8",
    imdbVotes = "120,000",
    boxOffice = "$100,000,000",
    website = "https://example.com"
  )

  val differ = AsyncPagingDataDiffer(
    diffCallback = TestDiffCallback<MediaItem>(),
    updateCallback = TestListCallback(),
    workerDispatcher = Dispatchers.Main
  )

  val dataMediaItem = MediaItem(
    firstAirDate = "",
    overview = "Overview",
    originalLanguage = "",
    listGenreIds = listOf(),
    posterPath = "Poster",
    backdropPath = "Backdrop",
    mediaType = "movie",
    originalName = "original name",
    popularity = 12345.0,
    voteAverage = 9.0f,
    name = "name",
    id = 1234,
    adult = false,
    voteCount = 999,
    originalTitle = "original title",
    video = false,
    title = "title",
    releaseDate = "release date",
    originCountry = listOf()
  )

  val postModelAddFavoriteStateSuccess =
    PostModelState(
      isSuccess = true,
      isDelete = false,
      isFavorite = true
    )

  val postModelDeleteFavoriteStateSuccess =
    PostModelState(
      isSuccess = true,
      isDelete = true,
      isFavorite = true
    )

  val postModelAddWatchlistStateSuccess =
    PostModelState(
      isSuccess = true,
      isDelete = false,
      isFavorite = false
    )

  val postModelDeleteWatchlistStateSuccess =
    PostModelState(
      isSuccess = true,
      isDelete = true,
      isFavorite = false
    )
}
