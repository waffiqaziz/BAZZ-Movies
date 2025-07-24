package com.waffiq.bazz_movies.feature.detail.testutils

import com.waffiq.bazz_movies.feature.detail.domain.model.ProductionCountriesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.MovieDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDates
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDatesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDatesItemValue
import com.waffiq.bazz_movies.feature.detail.utils.helpers.ReleaseDateHelper.getReleaseDateRegion
import org.junit.Assert.assertEquals

abstract class ReleaseDateHelperMovieTestBase {

  protected fun movieWithRelease(
    releaseRegion: String,
    releaseDate: String?,
    productionCountries: List<String>,
    fallbackDate: String,
  ): MovieDetail {
    return MovieDetail(
      releaseDates = ReleaseDates(
        listReleaseDatesItem = listOf(
          ReleaseDatesItem(
            iso31661 = releaseRegion,
            listReleaseDatesitemValue = listOf(
              ReleaseDatesItemValue(releaseDate = releaseDate)
            )
          )
        )
      ),
      listProductionCountriesItem = productionCountries.map { ProductionCountriesItem(it) },
      releaseDate = fallbackDate
    )
  }

  protected fun checkMovieReleaseDate(
    data: MovieDetail?,
    region: String,
    expectedRegion: String,
    expectedDate: String,
  ) {
    val result = getReleaseDateRegion(data, region)
    assertEquals(expectedRegion, result.regionRelease)
    assertEquals(expectedDate, result.releaseDate)
  }
}
