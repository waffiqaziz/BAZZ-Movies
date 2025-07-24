package com.waffiq.bazz_movies.feature.detail.utils.mappers

import com.waffiq.bazz_movies.core.domain.GenresItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.GenresResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.ProductionCountriesResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.castcrew.MediaCastResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.castcrew.MediaCreditsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.castcrew.MediaCrewResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.ProductionCompaniesResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.SpokenLanguagesResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.videomedia.VideoResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.videomedia.VideoResponse
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCredits
import com.waffiq.bazz_movies.feature.detail.domain.model.ProductionCompaniesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.ProductionCountriesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.SpokenLanguagesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.Video
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaDetailMapper.toGenresItem
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaDetailMapper.toMediaCredits
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaDetailMapper.toProductionCompaniesItem
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaDetailMapper.toProductionCountriesItem
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaDetailMapper.toSpokenLanguagesItem
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaDetailMapper.toVideo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MediaDetailMapperTest {

  @Test
  fun toVideo_withValidValues_returnsVideo() {
    val videoItemResponse = VideoResponseItem(
      site = "YouTube",
      size = 1080,
      iso31661 = "US",
      name = "Official Trailer",
      official = true,
      id = "video123",
      publishedAt = "2024-01-01T00:00:00Z",
      type = "Trailer",
      iso6391 = "en",
      key = "abc123"
    )

    val videoResponse = VideoResponse(
      id = 12345678,
      results = listOf(videoItemResponse)
    )

    val video: Video = videoResponse.toVideo()
    assertEquals(12345678, video.id)
    assertEquals(1, video.results.size)
    assertEquals("YouTube", video.results[0].site)
    assertEquals(1080, video.results[0].size)
    assertEquals("US", video.results[0].iso31661)
    assertEquals("Official Trailer", video.results[0].name)
    assertTrue(video.results[0].official == true)
    assertEquals("video123", video.results[0].id)
    assertEquals("2024-01-01T00:00:00Z", video.results[0].publishedAt)
    assertEquals("Trailer", video.results[0].type)
    assertEquals("en", video.results[0].iso6391)
    assertEquals("abc123", video.results[0].key)
  }

  @Test
  fun toVideo_withEmptyResults_returnsVideoWithEmptyResults() {
    val videoResponse = VideoResponse(
      id = 12345678,
      results = emptyList()
    )

    val video: Video = videoResponse.toVideo()
    assertEquals(12345678, video.id)
    assertEquals(0, video.results.size)
  }

  @Test
  fun toMediaCredits_withValidValues_returnsMediaCredits() {
    val castItem = MediaCastResponseItem(
      castId = 1,
      character = "Hero",
      creditId = "credit123",
      gender = 2,
      id = 456,
      name = "John Doe",
      order = 0,
      profilePath = "/profile.jpg"
    )

    val crewItem = MediaCrewResponseItem(
      creditId = "crew123",
      department = "Directing",
      gender = 1,
      id = 789,
      job = "Director",
      name = "Jane Smith",
      profilePath = "/director.jpg"
    )

    val creditsResponse = MediaCreditsResponse(
      cast = listOf(castItem),
      id = 12345,
      crew = listOf(crewItem)
    )

    val credits: MediaCredits = creditsResponse.toMediaCredits()
    assertEquals(12345, credits.id)
    assertEquals(1, credits.cast.size)
    assertEquals(1, credits.crew.size)
    assertEquals(1, credits.cast[0].castId)
    assertEquals("Hero", credits.cast[0].character)
    assertEquals("John Doe", credits.cast[0].name)
    assertEquals("crew123", credits.crew[0].creditId)
    assertEquals("Directing", credits.crew[0].department)
    assertEquals("Jane Smith", credits.crew[0].name)
  }

  @Test
  fun toGenresItem_withValidValues_returnsGenresItem() {
    val genresItemResponse = GenresResponseItem(
      name = "Action",
      id = 28
    )

    val genresItem: GenresItem = genresItemResponse.toGenresItem()
    assertEquals("Action", genresItem.name)
    assertEquals(28, genresItem.id)
  }

  @Test
  fun toSpokenLanguagesItem_withValidValues_returnsSpokenLanguagesItem() {
    val spokenLanguagesItemResponse = SpokenLanguagesResponseItem(
      name = "English",
      iso6391 = "en",
      englishName = "English"
    )

    val spokenLanguagesItem: SpokenLanguagesItem =
      spokenLanguagesItemResponse.toSpokenLanguagesItem()
    assertEquals("English", spokenLanguagesItem.name)
    assertEquals("en", spokenLanguagesItem.iso6391)
    assertEquals("English", spokenLanguagesItem.englishName)
  }

  @Test
  fun toProductionCountriesItem_withValidValues_returnsProductionCountriesItem() {
    val productionCountriesItemResponse = ProductionCountriesResponseItem(
      iso31661 = "US",
      name = "United States",
      type = 1,
      iso6391 = "en",
      certification = "PG-13"
    )

    val productionCountriesItem: ProductionCountriesItem =
      productionCountriesItemResponse.toProductionCountriesItem()
    assertEquals("US", productionCountriesItem.iso31661)
    assertEquals("United States", productionCountriesItem.name)
    assertEquals(1, productionCountriesItem.type)
    assertEquals("en", productionCountriesItem.iso6391)
    assertEquals("PG-13", productionCountriesItem.certification)
  }

  @Test
  fun toProductionCompaniesItem_withValidValues_returnsProductionCompaniesItem() {
    val productionCompaniesItemResponse = ProductionCompaniesResponseItem(
      logoPath = "/logo.png",
      name = "Marvel Studios",
      id = 420,
      originCountry = "US"
    )

    val productionCompaniesItem: ProductionCompaniesItem =
      productionCompaniesItemResponse.toProductionCompaniesItem()
    assertEquals("/logo.png", productionCompaniesItem.logoPath)
    assertEquals("Marvel Studios", productionCompaniesItem.name)
    assertEquals(420, productionCompaniesItem.id)
    assertEquals("US", productionCompaniesItem.originCountry)
  }

  @Test
  fun toProductionCompaniesItem_withNullValues_returnsProductionCompaniesItem() {
    val productionCompaniesItemResponse = ProductionCompaniesResponseItem(
      logoPath = null,
      name = "Unknown Studio",
      id = 999,
      originCountry = null
    )

    val productionCompaniesItem: ProductionCompaniesItem =
      productionCompaniesItemResponse.toProductionCompaniesItem()
    assertNull(productionCompaniesItem.logoPath)
    assertEquals("Unknown Studio", productionCompaniesItem.name)
    assertEquals(999, productionCompaniesItem.id)
    assertNull(productionCompaniesItem.originCountry)
  }

  @Test
  fun toSpokenLanguagesItem_withNullValues_returnsSpokenLanguagesItemResponse() {
    val spokenLanguagesItemResponse = SpokenLanguagesResponseItem(
      name = null,
      iso6391 = "xx",
      englishName = null
    )

    val spokenLanguagesItem: SpokenLanguagesItem =
      spokenLanguagesItemResponse.toSpokenLanguagesItem()
    assertNull(spokenLanguagesItem.name)
    assertEquals("xx", spokenLanguagesItem.iso6391)
    assertNull(spokenLanguagesItem.englishName)
  }
}
