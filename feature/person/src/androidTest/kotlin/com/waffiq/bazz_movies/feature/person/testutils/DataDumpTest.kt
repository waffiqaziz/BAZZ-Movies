package com.waffiq.bazz_movies.feature.person.testutils

import com.waffiq.bazz_movies.core.domain.MediaCastItem
import com.waffiq.bazz_movies.feature.person.domain.model.CastItem
import com.waffiq.bazz_movies.feature.person.domain.model.DetailPerson
import com.waffiq.bazz_movies.feature.person.domain.model.ExternalIDPerson
import com.waffiq.bazz_movies.feature.person.domain.model.ProfilesItem

object DataDumpTest {

  // helper methods to create test data
  val testMediaCastItem =
    MediaCastItem(
      id = 123,
      name = "Test Actor",
      originalName = "Test Actor Original",
      profilePath = "/test_profile.jpg",
    )

  val testDetailPerson =
    DetailPerson(
      id = 123,
      name = "Test Actor",
      biography = "Test biography for the actor",
      birthday = "1990-01-01",
      placeOfBirth = "Test City",
      homepage = "https://example.com"
    )

  val testCastItem =
    CastItem(
      id = 456,
      title = "Test Movie",
      character = "Test Character",
      posterPath = "/test_poster.jpg",
      releaseDate = "2023-01-01"
    )

  val testProfileItem =
    ProfilesItem(
      filePath = "/test_image.jpg",
      width = 500,
      height = 750
    )

  val testExternalIDPerson =
    ExternalIDPerson(
      id = 123,
      imdbId = "nm1234567",
      instagramId = "test_instagram",
      twitterId = "test_twitter",
      facebookId = "test_facebook",
      tiktokId = null,
      youtubeId = null,
      wikidataId = "Q123456"
    )

  val testKnownForList =
    listOf(
      CastItem(
        id = 1,
        title = "Test Movie",
        character = "Test Character",
        posterPath = "/test.jpg"
      )
    )

  val testImagesList =
    listOf(
      ProfilesItem(
        filePath = "/test1.jpg",
        width = 300,
        height = 450
      )
    )
}
