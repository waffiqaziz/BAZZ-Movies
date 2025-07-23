package com.waffiq.bazz_movies.feature.detail.utils.mappers

import com.waffiq.bazz_movies.core.domain.GenresItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.GenresResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.ContentRatingsItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.ContentRatingsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.CreatedByItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.DetailTvResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.ExternalIdResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.LastEpisodeToAirResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.NetworksItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.SeasonsItemResponse
import com.waffiq.bazz_movies.feature.detail.domain.model.ProductionCompaniesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.ProductionCountriesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.SpokenLanguagesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.ContentRatingsItem
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.CreatedByItem
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.DetailTv
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvExternalIds
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.NetworksItem
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.SeasonsItem
import com.waffiq.bazz_movies.feature.detail.utils.mappers.TvMapper.toDetailTv
import com.waffiq.bazz_movies.feature.detail.utils.mappers.TvMapper.toExternalTvID
import org.junit.Assert.assertEquals
import org.junit.Test

class TvMapperTest {

  @Test
  fun toDetailTv_withValidValues_returnsDetailTv() {
    val networksItemResponse = NetworksItemResponse(
      logoPath = "/network_logo.jpg",
      name = "HBO",
      id = 1,
      originCountry = "US"
    )

    val genresItemResponse = GenresResponseItem(
      id = 1,
      name = "Drama"
    )

    val seasonsItemResponse = SeasonsItemResponse(
      airDate = "2024-01-01",
      overview = "Season overview",
      episodeCount = 10,
      name = "Season 1",
      seasonNumber = 1,
      id = 1,
      posterPath = "/season_poster.jpg"
    )

    val createdByItemResponse = CreatedByItemResponse(
      gender = 1,
      creditId = "credit123",
      name = "Creator Name",
      profilePath = "/creator_profile.jpg",
      id = 1
    )

    val lastEpisodeToAirResponse = LastEpisodeToAirResponse(
      productionCode = "PROD001",
      airDate = "2024-01-15",
      overview = "Episode overview",
      episodeNumber = 1,
      episodeType = "standard",
      showId = 1,
      voteAverage = 8.5,
      name = "Episode 1",
      seasonNumber = 1,
      runtime = 60,
      id = 1,
      stillPath = "/episode_still.jpg",
      voteCount = 100
    )

    val contentRatingsItemResponse = ContentRatingsItemResponse(
      descriptors = listOf("Violence"),
      iso31661 = "US",
      rating = "TV-MA"
    )

    val contentRatingsResponse = ContentRatingsResponse(
      contentRatingsItemResponse = listOf(contentRatingsItemResponse)
    )

    val detailTvResponse = DetailTvResponse(
      originalLanguage = "en",
      numberOfEpisodes = 10,
      networksResponse = listOf(networksItemResponse),
      type = "Scripted",
      backdropPath = "/backdrop.jpg",
      genres = listOf(genresItemResponse),
      popularity = 8.5,
      productionCountriesResponse = listOf(),
      id = 1,
      numberOfSeasons = 1,
      voteCount = 100,
      firstAirDate = "2024-01-01",
      overview = "Test TV show overview",
      seasonsResponse = listOf(seasonsItemResponse),
      languages = listOf("en"),
      createdByResponse = listOf(createdByItemResponse),
      lastEpisodeToAirResponse = lastEpisodeToAirResponse,
      posterPath = "/poster.jpg",
      originCountry = listOf("US"),
      spokenLanguagesResponse = listOf(),
      productionCompaniesResponse = listOf(),
      originalName = "Test TV Show Original",
      voteAverage = 8.5,
      name = "Test TV Show",
      tagline = "Test tagline",
      episodeRunTime = listOf(60),
      contentRatingsResponse = contentRatingsResponse,
      adult = false,
      nextEpisodeToAir = null,
      inProduction = true,
      lastAirDate = "2024-01-15",
      homepage = "https://testtv.com",
      status = "Returning Series"
    )

    val detailTv: DetailTv = detailTvResponse.toDetailTv()

    assertEquals("en", detailTv.originalLanguage)
    assertEquals(10, detailTv.numberOfEpisodes)
    assertEquals(1, detailTv.listNetworksItem?.size)
    assertEquals("HBO", detailTv.listNetworksItem?.get(0)?.name)
    assertEquals("Scripted", detailTv.type)
    assertEquals("/backdrop.jpg", detailTv.backdropPath)
    assertEquals(1, detailTv.listGenres?.size)
    assertEquals("Drama", detailTv.listGenres?.get(0)?.name)
    assertEquals(8.5, detailTv.popularity)
    assertEquals(1, detailTv.id)
    assertEquals(1, detailTv.numberOfSeasons)
    assertEquals(100, detailTv.voteCount)
    assertEquals("2024-01-01", detailTv.firstAirDate)
    assertEquals("Test TV show overview", detailTv.overview)
    assertEquals(1, detailTv.listSeasonsItem?.size)
    assertEquals("Season 1", detailTv.listSeasonsItem?.get(0)?.name)
    assertEquals(listOf("en"), detailTv.listLanguages)
    assertEquals(1, detailTv.listCreatedByItem?.size)
    assertEquals("Creator Name", detailTv.listCreatedByItem?.get(0)?.name)
    assertEquals("Episode 1", detailTv.lastEpisodeToAir?.name)
    assertEquals("/poster.jpg", detailTv.posterPath)
    assertEquals(listOf("US"), detailTv.listOriginCountry)
    assertEquals("Test TV Show Original", detailTv.originalName)
    assertEquals(8.5, detailTv.voteAverage)
    assertEquals("Test TV Show", detailTv.name)
    assertEquals("Test tagline", detailTv.tagline)
    assertEquals(listOf(60), detailTv.listEpisodeRunTime)
    assertEquals(1, detailTv.contentRatings?.contentRatingsItem?.size)
    assertEquals("TV-MA", detailTv.contentRatings?.contentRatingsItem?.get(0)?.rating)
    assertEquals(false, detailTv.adult)
    assertEquals(null, detailTv.nextEpisodeToAir)
    assertEquals(true, detailTv.inProduction)
    assertEquals("2024-01-15", detailTv.lastAirDate)
    assertEquals("https://testtv.com", detailTv.homepage)
    assertEquals("Returning Series", detailTv.status)
  }

  @Test
  fun toDetailTv_withNullValues_returnsDetailTv() {
    val detailTvResponse = DetailTvResponse(
      originalLanguage = "en",
      numberOfEpisodes = 10,
      networksResponse = null,
      type = "Scripted",
      backdropPath = null,
      genres = null,
      popularity = 8.5,
      productionCountriesResponse = null,
      id = 1,
      numberOfSeasons = 1,
      voteCount = 100,
      firstAirDate = "2024-01-01",
      overview = "Test TV show overview",
      seasonsResponse = null,
      languages = null,
      createdByResponse = null,
      lastEpisodeToAirResponse = null,
      posterPath = "/poster.jpg",
      originCountry = null,
      spokenLanguagesResponse = null,
      productionCompaniesResponse = null,
      originalName = "Test TV Show Original",
      voteAverage = 8.5,
      name = "Test TV Show",
      tagline = "Test tagline",
      episodeRunTime = null,
      contentRatingsResponse = null,
      adult = false,
      nextEpisodeToAir = null,
      inProduction = true,
      lastAirDate = "2024-01-15",
      homepage = "https://testtv.com",
      status = "Returning Series"
    )

    val detailTv: DetailTv = detailTvResponse.toDetailTv()

    assertEquals("en", detailTv.originalLanguage)
    assertEquals(10, detailTv.numberOfEpisodes)
    assertEquals(null, detailTv.listNetworksItem)
    assertEquals("Scripted", detailTv.type)
    assertEquals(null, detailTv.backdropPath)
    assertEquals(null, detailTv.listGenres)
    assertEquals(null, detailTv.listSeasonsItem)
    assertEquals(null, detailTv.listLanguages)
    assertEquals(null, detailTv.listCreatedByItem)
    assertEquals(null, detailTv.lastEpisodeToAir)
    assertEquals(null, detailTv.listOriginCountry)
    assertEquals(null, detailTv.listEpisodeRunTime)
    assertEquals(null, detailTv.contentRatings)
    assertEquals("Test TV Show", detailTv.name)
    assertEquals("https://testtv.com", detailTv.homepage)
  }

  @Test
  fun toDetailTv_withEmptyLists_returnsDetailTv() {
    val detailTvResponse = DetailTvResponse(
      originalLanguage = "en",
      numberOfEpisodes = 10,
      networksResponse = emptyList(),
      type = "Scripted",
      backdropPath = "/backdrop.jpg",
      genres = emptyList(),
      popularity = 8.5,
      productionCountriesResponse = emptyList(),
      id = 1,
      numberOfSeasons = 1,
      voteCount = 100,
      firstAirDate = "2024-01-01",
      overview = "Test TV show overview",
      seasonsResponse = emptyList(),
      languages = emptyList(),
      createdByResponse = emptyList(),
      lastEpisodeToAirResponse = null,
      posterPath = "/poster.jpg",
      originCountry = emptyList(),
      spokenLanguagesResponse = emptyList(),
      productionCompaniesResponse = emptyList(),
      originalName = "Test TV Show Original",
      voteAverage = 8.5,
      name = "Test TV Show",
      tagline = "Test tagline",
      episodeRunTime = emptyList(),
      contentRatingsResponse = ContentRatingsResponse(
        contentRatingsItemResponse = emptyList()
      ),
      adult = false,
      nextEpisodeToAir = null,
      inProduction = true,
      lastAirDate = "2024-01-15",
      homepage = "https://testtv.com",
      status = "Returning Series"
    )

    val detailTv: DetailTv = detailTvResponse.toDetailTv()

    assertEquals("Test TV Show", detailTv.name)
    assertEquals(emptyList<NetworksItem>(), detailTv.listNetworksItem)
    assertEquals(emptyList<GenresItem>(), detailTv.listGenres)
    assertEquals(emptyList<ProductionCountriesItem>(), detailTv.listProductionCountriesItem)
    assertEquals(emptyList<SeasonsItem>(), detailTv.listSeasonsItem)
    assertEquals(emptyList<String>(), detailTv.listLanguages)
    assertEquals(emptyList<CreatedByItem>(), detailTv.listCreatedByItem)
    assertEquals(emptyList<String>(), detailTv.listOriginCountry)
    assertEquals(emptyList<SpokenLanguagesItem>(), detailTv.listSpokenLanguagesItem)
    assertEquals(emptyList<ProductionCompaniesItem>(), detailTv.listProductionCompaniesItem)
    assertEquals(emptyList<Int>(), detailTv.listEpisodeRunTime)
    assertEquals(emptyList<ContentRatingsItem>(), detailTv.contentRatings?.contentRatingsItem)
  }

  @Test
  fun toDetailTv_withNullItemsInList_returnsDetailTvWithEmptyGenres() {
    val detailTvResponse = DetailTvResponse(
      originalLanguage = "en",
      numberOfEpisodes = 10,
      networksResponse = listOf(null, null),
      type = "Scripted",
      backdropPath = "/backdrop.jpg",
      genres = listOf(null, null),
      popularity = 8.5,
      productionCountriesResponse = listOf(null),
      id = 1,
      numberOfSeasons = 1,
      voteCount = 100,
      firstAirDate = "2024-01-01",
      overview = "Test TV show overview",
      seasonsResponse = listOf(null),
      languages = listOf("en"),
      createdByResponse = listOf(null),
      lastEpisodeToAirResponse = null,
      posterPath = "/poster.jpg",
      originCountry = listOf("US"),
      spokenLanguagesResponse = listOf(null),
      productionCompaniesResponse = listOf(null),
      originalName = "Test TV Show Original",
      voteAverage = 8.5,
      name = "Test TV Show",
      tagline = "Test tagline",
      episodeRunTime = listOf(60),
      contentRatingsResponse = ContentRatingsResponse(
        contentRatingsItemResponse = listOf(null)
      ),
      adult = false,
      nextEpisodeToAir = null,
      inProduction = true,
      lastAirDate = "2024-01-15",
      homepage = "https://testtv.com",
      status = "Returning Series"
    )

    val detailTv: DetailTv = detailTvResponse.toDetailTv()

    assertEquals("Test TV Show", detailTv.name)
    assertEquals(2, detailTv.listNetworksItem?.size)
    assertEquals(2, detailTv.listGenres?.size)
    assertEquals(GenresItem(), detailTv.listGenres?.get(0))
    assertEquals(GenresItem(), detailTv.listGenres?.get(1))
    assertEquals(1, detailTv.listProductionCountriesItem?.size)
    assertEquals(1, detailTv.listSeasonsItem?.size)
    assertEquals(1, detailTv.listCreatedByItem?.size)
    assertEquals(1, detailTv.listSpokenLanguagesItem?.size)
    assertEquals(1, detailTv.listProductionCompaniesItem?.size)
    assertEquals(1, detailTv.contentRatings?.contentRatingsItem?.size)
  }

  @Test
  fun toExternalTvID_withValidValues_returnsExternalTvID() {
    val externalIdResponse = ExternalIdResponse(
      imdbId = "tt1234567",
      freebaseMid = "/m/123",
      tvdbId = 12345,
      freebaseId = "/en/test",
      id = 1,
      twitterId = "test_twitter",
      tvrageId = 54321,
      facebookId = "test_facebook",
      instagramId = "test_instagram"
    )

    val externalTvID: TvExternalIds = externalIdResponse.toExternalTvID()

    assertEquals("tt1234567", externalTvID.imdbId)
    assertEquals("/m/123", externalTvID.freebaseMid)
    assertEquals(12345, externalTvID.tvdbId)
    assertEquals("/en/test", externalTvID.freebaseId)
    assertEquals(1, externalTvID.id)
    assertEquals("test_twitter", externalTvID.twitterId)
    assertEquals(54321, externalTvID.tvrageId)
    assertEquals("test_facebook", externalTvID.facebookId)
    assertEquals("test_instagram", externalTvID.instagramId)
  }

  @Test
  fun toExternalTvID_withNullValues_returnsExternalTvID() {
    val externalIdResponse = ExternalIdResponse(
      imdbId = null,
      freebaseMid = null,
      tvdbId = null,
      freebaseId = null,
      id = 1,
      twitterId = null,
      tvrageId = null,
      facebookId = null,
      instagramId = null
    )

    val externalTvID: TvExternalIds = externalIdResponse.toExternalTvID()

    assertEquals(null, externalTvID.imdbId)
    assertEquals(null, externalTvID.freebaseMid)
    assertEquals(null, externalTvID.tvdbId)
    assertEquals(null, externalTvID.freebaseId)
    assertEquals(1, externalTvID.id)
    assertEquals(null, externalTvID.twitterId)
    assertEquals(null, externalTvID.tvrageId)
    assertEquals(null, externalTvID.facebookId)
    assertEquals(null, externalTvID.instagramId)
  }

  @Test
  fun toExternalTvID_withEmptyStringValues_returnsExternalTvID() {
    val externalIdResponse = ExternalIdResponse(
      imdbId = "",
      freebaseMid = "",
      tvdbId = 0,
      freebaseId = "",
      id = 1,
      twitterId = "",
      tvrageId = 0,
      facebookId = "",
      instagramId = ""
    )

    val externalTvID: TvExternalIds = externalIdResponse.toExternalTvID()

    assertEquals("", externalTvID.imdbId)
    assertEquals("", externalTvID.freebaseMid)
    assertEquals(0, externalTvID.tvdbId)
    assertEquals("", externalTvID.freebaseId)
    assertEquals(1, externalTvID.id)
    assertEquals("", externalTvID.twitterId)
    assertEquals(0, externalTvID.tvrageId)
    assertEquals("", externalTvID.facebookId)
    assertEquals("", externalTvID.instagramId)
  }
}
