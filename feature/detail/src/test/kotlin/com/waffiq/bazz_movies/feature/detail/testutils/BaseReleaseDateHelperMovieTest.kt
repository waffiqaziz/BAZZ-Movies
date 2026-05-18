package com.waffiq.bazz_movies.feature.detail.testutils

import com.waffiq.bazz_movies.feature.detail.domain.model.ProductionCountriesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.MovieDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDates
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDatesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDatesItemValue
import com.waffiq.bazz_movies.feature.detail.utils.helpers.ReleaseDateHelper.getReleaseDateRegion
import org.junit.Assert.assertEquals

abstract class BaseReleaseDateHelperMovieTest {

  protected fun movieWithRelease(
    releaseRegion: String,
    releaseDate: String?,
    productionCountries: List<String>,
    fallbackDate: String,
  ): MovieDetail =
    MovieDetail(
      releaseDates = ReleaseDates(
        listReleaseDatesItem = listOf(
          ReleaseDatesItem(
            iso31661 = releaseRegion,
            listReleaseDatesItemValue = listOf(
              ReleaseDatesItemValue(releaseDate = releaseDate),
            ),
          ),
        ),
      ),
      listProductionCountriesItem = productionCountries.map { ProductionCountriesItem(it) },
      releaseDate = fallbackDate,
    )

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

  protected fun releaseDateItem(iso: String, date: String) =
    ReleaseDatesItem(
      iso31661 = iso,
      listReleaseDatesItemValue = listOf(
        ReleaseDatesItemValue(releaseDate = date),
      ),
    )

  protected fun movieDetail(
    releaseDates: List<ReleaseDatesItem>,
    productionCountries: List<ProductionCountriesItem?>?,
    releaseDate: String?,
  ) = MovieDetail(
    releaseDates = ReleaseDates(
      listReleaseDatesItem = releaseDates,
    ),
    listProductionCountriesItem = productionCountries,
    releaseDate = releaseDate,
  )

  protected fun movieDetail(
    releaseRegion: String,
    releaseDate: String,
    productionCountries: List<ProductionCountriesItem?>?,
    fallbackReleaseDate: String = "",
  ) = MovieDetail(
    releaseDates = ReleaseDates(
      listReleaseDatesItem = listOf(
        ReleaseDatesItem(
          iso31661 = releaseRegion,
          listReleaseDatesItemValue = listOf(
            ReleaseDatesItemValue(releaseDate = releaseDate),
          ),
        ),
      ),
    ),
    listProductionCountriesItem = productionCountries,
    releaseDate = fallbackReleaseDate,
  )

  @Suppress("LongParameterList")
  protected fun movie(
    iso: String? = null,
    releaseDate: String? = "2023-01-01T00:00:00.000Z",
    productionCountries: List<String?>? = null,
    fallback: String? = "",
    releaseDatesList: List<ReleaseDatesItem?>? = null,
    releaseDates: ReleaseDates? = null,
  ): MovieDetail =
    MovieDetail(
      releaseDates = releaseDates ?: ReleaseDates(
        listReleaseDatesItem = releaseDatesList ?: listOf(
          ReleaseDatesItem(
            iso31661 = iso,
            listReleaseDatesItemValue = listOf(
              ReleaseDatesItemValue(releaseDate = releaseDate),
            ),
          ),
        ),
      ),
      listProductionCountriesItem = productionCountries?.map { ProductionCountriesItem(it) },
      releaseDate = fallback,
    )
}
