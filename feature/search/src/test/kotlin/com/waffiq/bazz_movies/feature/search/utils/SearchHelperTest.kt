package com.waffiq.bazz_movies.feature.search.utils

import com.waffiq.bazz_movies.core.common.Genre
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_IMG_LINK_POSTER_W185
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_backdrop_error
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_action
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_adventure
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_animation
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_comedy
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_crime
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_documentary
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_drama
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_family
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_fantasy
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_historical
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_horror
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_kids
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_mistery
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_musical
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_news
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_romance
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_sci_fi
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_soap
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_talk
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_thriller
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_tv_movie
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_tv_show
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_war
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_genre_western
import com.waffiq.bazz_movies.feature.search.domain.model.KnownForItem
import com.waffiq.bazz_movies.feature.search.domain.model.MultiSearchItem
import com.waffiq.bazz_movies.feature.search.utils.SearchHelper.getKnownFor
import com.waffiq.bazz_movies.feature.search.utils.SearchHelper.iconRes
import com.waffiq.bazz_movies.feature.search.utils.SearchHelper.profileImageSource
import junit.framework.TestCase.assertEquals
import org.junit.Test

class SearchHelperTest {

  @Test
  fun getKnownFor_whenCalled_returnsConcatenatedTitles() {
    val knownForItems = listOf(
      KnownForItem(title = "Movie 1"),
      KnownForItem(title = "Movie 2"),
      KnownForItem(title = "Movie 3"),
    )

    val result = getKnownFor(knownForItems)
    assertEquals("Movie 1, Movie 2, Movie 3", result)
  }

  @Test
  fun getKnownFor_whenListIsEmpty_returnsEmptyString() {
    val result = getKnownFor(emptyList())
    assertEquals("", result)
  }

  @Test
  fun getKnownFor_whenSomeTitlesAreNull_ignoresNullsAndJoinsOthers() {
    val knownForItems = listOf(
      KnownForItem(title = "Movie 1"),
      KnownForItem(title = null),
      KnownForItem(title = "Movie 3"),
    )

    val result = getKnownFor(knownForItems)
    assertEquals("Movie 1, Movie 3", result)
  }

  @Test
  fun getKnownFor_whenUsingNameField_ignoresNullsAndJoinsValidNames() {
    val knownForItems = listOf(
      KnownForItem(name = "Movie 2"),
      KnownForItem(name = null),
      KnownForItem(name = "Movie 4"),
    )

    val result = getKnownFor(knownForItems)
    assertEquals("Movie 2, Movie 4", result)
  }

  @Test
  fun getKnownFor_whenUsingOriginalNameField_ignoresNullsAndJoinsValidNames() {
    val knownForItems = listOf(
      KnownForItem(originalName = "Movie 2"),
      KnownForItem(originalName = null),
      KnownForItem(originalName = "Movie 4"),
    )

    val result = getKnownFor(knownForItems)
    assertEquals("Movie 2, Movie 4", result)
  }

  @Test
  fun profileImageSource_whenProfileIsAvailable_returnsProfileURL() {
    val data = MultiSearchItem(profilePath = "profile", id = 1)
    val result = data.profileImageSource
    assertEquals(TMDB_IMG_LINK_POSTER_W185 + "profile", result)
  }

  @Test
  fun profileImageSource_whenProfileIsMissing_returnsFallback() {
    // profile is null
    val data1 = MultiSearchItem(profilePath = null, id = 1)
    assertEquals(ic_backdrop_error, data1.profileImageSource)

    // profile is empty
    val data2 = MultiSearchItem(profilePath = "", id = 1)
    assertEquals(ic_backdrop_error, data2.profileImageSource)

    // profile is blank
    val data3 = MultiSearchItem(profilePath = " ", id = 1)
    assertEquals(ic_backdrop_error, data3.profileImageSource)
  }

  @Test
  fun iconRes_validGenre_returnCorrectly() {
    assertEquals(Genre.ACTION.iconRes(), ic_genre_action)
    assertEquals(Genre.ACTION_AND_ADVENTURE.iconRes(), ic_genre_action)
    assertEquals(Genre.ADVENTURE.iconRes(), ic_genre_adventure)
    assertEquals(Genre.ANIMATION.iconRes(), ic_genre_animation)
    assertEquals(Genre.COMEDY.iconRes(), ic_genre_comedy)
    assertEquals(Genre.CRIME.iconRes(), ic_genre_crime)
    assertEquals(Genre.DOCUMENTARY.iconRes(), ic_genre_documentary)
    assertEquals(Genre.DRAMA.iconRes(), ic_genre_drama)
    assertEquals(Genre.FAMILY.iconRes(), ic_genre_family)
    assertEquals(Genre.KIDS.iconRes(), ic_genre_kids)
    assertEquals(Genre.FANTASY.iconRes(), ic_genre_fantasy)
    assertEquals(Genre.SCI_FI_AND_FANTASY.iconRes(), ic_genre_fantasy)
    assertEquals(Genre.HISTORY.iconRes(), ic_genre_historical)
    assertEquals(Genre.HORROR.iconRes(), ic_genre_horror)
    assertEquals(Genre.MUSIC.iconRes(), ic_genre_musical)
    assertEquals(Genre.MYSTERY.iconRes(), ic_genre_mistery)
    assertEquals(Genre.NEWS.iconRes(), ic_genre_news)
    assertEquals(Genre.TALK.iconRes(), ic_genre_talk)
    assertEquals(Genre.REALITY.iconRes(), ic_genre_tv_show)
    assertEquals(Genre.ROMANCE.iconRes(), ic_genre_romance)
    assertEquals(Genre.SCIENCE_FICTION.iconRes(), ic_genre_sci_fi)
    assertEquals(Genre.SOAP.iconRes(), ic_genre_soap)
    assertEquals(Genre.THRILLER.iconRes(), ic_genre_thriller)
    assertEquals(Genre.TV_MOVIE.iconRes(), ic_genre_tv_movie)
    assertEquals(Genre.WAR.iconRes(), ic_genre_war)
    assertEquals(Genre.WAR_AND_POLITICS.iconRes(), ic_genre_war)
    assertEquals(Genre.WESTERN.iconRes(), ic_genre_western)
  }
}
