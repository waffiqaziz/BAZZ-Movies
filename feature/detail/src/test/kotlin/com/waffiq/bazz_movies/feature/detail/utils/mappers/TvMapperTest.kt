package com.waffiq.bazz_movies.feature.detail.utils.mappers

import com.waffiq.bazz_movies.core.domain.GenresItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.ContentRatingsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.DetailTvResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.ExternalIdResponse
import com.waffiq.bazz_movies.feature.detail.domain.model.ProductionCompaniesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.ProductionCountriesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.SpokenLanguagesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.ContentRatingsItem
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.CreatedByItem
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.NetworksItem
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.SeasonsItem
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvExternalIds
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.detailTvResponse
import com.waffiq.bazz_movies.feature.detail.utils.mappers.TvMapper.toDetailTv
import com.waffiq.bazz_movies.feature.detail.utils.mappers.TvMapper.toExternalTvID
import org.junit.Assert.assertEquals
import org.junit.Test

class TvMapperTest {

  @Test
  fun toDetailTv_withValidValues_returnsDetailTv() {
    val detailTv: TvDetail = detailTvResponse.toDetailTv()

    assertEquals("en", detailTv.originalLanguage)
    assertEquals(10, detailTv.numberOfEpisodes)
    assertEquals(1, detailTv.listNetworksItem?.size)
    assertEquals("HBO", detailTv.listNetworksItem?.get(0)?.name)
    assertEquals("Scripted", detailTv.type)
    assertEquals("/backdrop.jpg", detailTv.backdropPath)
    assertEquals(1, detailTv.listGenres?.size)
    assertEquals("Action", detailTv.listGenres?.get(0)?.name)
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
  fun toDetailTv_withDefaultValues_returnsDetailTv() {
    val detailTvResponse = DetailTvResponse()

    val detailTv: TvDetail = detailTvResponse.toDetailTv()
    assertEquals(null, detailTv.listNetworksItem)
    assertEquals(null, detailTv.backdropPath)
    assertEquals(null, detailTv.listGenres)
    assertEquals(null, detailTv.listSeasonsItem)
    assertEquals(null, detailTv.listLanguages)
    assertEquals(null, detailTv.listCreatedByItem)
    assertEquals(null, detailTv.lastEpisodeToAir)
    assertEquals(null, detailTv.listOriginCountry)
    assertEquals(null, detailTv.listEpisodeRunTime)
    assertEquals(null, detailTv.contentRatings)
  }

  @Test
  fun toDetailTv_withEmptyLists_returnsDetailTv() {
    val detailTvResponse = DetailTvResponse(
      networksResponse = emptyList(),
      genres = emptyList(),
      productionCountriesResponse = emptyList(),
      seasonsResponse = emptyList(),
      languages = emptyList(),
      createdByResponse = emptyList(),
      originCountry = emptyList(),
      spokenLanguagesResponse = emptyList(),
      productionCompaniesResponse = emptyList(),
      episodeRunTime = emptyList(),
      contentRatingsResponse = ContentRatingsResponse(
        contentRatingsItemResponse = emptyList()
      ),
    )

    val detailTv: TvDetail = detailTvResponse.toDetailTv()

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
      networksResponse = listOf(null, null),
      genres = listOf(null, null),
      productionCountriesResponse = listOf(null),
      seasonsResponse = listOf(null),
      languages = listOf("en"),
      createdByResponse = listOf(null),
      lastEpisodeToAirResponse = null,
      spokenLanguagesResponse = listOf(null),
      productionCompaniesResponse = listOf(null),
      episodeRunTime = listOf(60),
      contentRatingsResponse = ContentRatingsResponse(
        contentRatingsItemResponse = listOf(null)
      ),
      nextEpisodeToAir = null,
    )

    val detailTv: TvDetail = detailTvResponse.toDetailTv()
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
