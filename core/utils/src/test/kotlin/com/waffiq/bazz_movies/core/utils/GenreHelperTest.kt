package com.waffiq.bazz_movies.core.utils

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_action
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_action_and_adventure
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_adventure
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_animation
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_comedy
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_crime
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_documentary
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_drama
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_family
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_fantasy
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_history
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_horror
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_kids
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_music
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_mystery
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_news
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_reality
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_romance
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_science_fiction
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_scifi_and_fantasy
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_soap
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_talk
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_thriller
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_tv_movie
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_war
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_war_and_politics
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_western
import com.waffiq.bazz_movies.core.designsystem.R.string.not_available
import com.waffiq.bazz_movies.core.model.media.Genre
import com.waffiq.bazz_movies.core.model.media.GenresItem
import com.waffiq.bazz_movies.core.utils.GenreHelper.getGenre
import com.waffiq.bazz_movies.core.utils.GenreHelper.toGenre
import com.waffiq.bazz_movies.core.utils.GenreHelper.toGenreIds
import com.waffiq.bazz_movies.core.utils.GenreHelper.toGenreString
import com.waffiq.bazz_movies.core.utils.GenreHelper.toStringRes
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class GenreHelperTest {

  private val context: Context = ApplicationProvider.getApplicationContext()

  @Test
  fun toStringRes_validGenre_returnsCorrectStringRes() {
    assertEquals(genre_action, Genre.ACTION.toStringRes())
    assertEquals(genre_action_and_adventure, Genre.ACTION_AND_ADVENTURE.toStringRes())
    assertEquals(genre_adventure, Genre.ADVENTURE.toStringRes())
    assertEquals(genre_animation, Genre.ANIMATION.toStringRes())
    assertEquals(genre_comedy, Genre.COMEDY.toStringRes())
    assertEquals(genre_crime, Genre.CRIME.toStringRes())
    assertEquals(genre_documentary, Genre.DOCUMENTARY.toStringRes())
    assertEquals(genre_drama, Genre.DRAMA.toStringRes())
    assertEquals(genre_family, Genre.FAMILY.toStringRes())
    assertEquals(genre_fantasy, Genre.FANTASY.toStringRes())
    assertEquals(genre_history, Genre.HISTORY.toStringRes())
    assertEquals(genre_horror, Genre.HORROR.toStringRes())
    assertEquals(genre_kids, Genre.KIDS.toStringRes())
    assertEquals(genre_music, Genre.MUSIC.toStringRes())
    assertEquals(genre_mystery, Genre.MYSTERY.toStringRes())
    assertEquals(genre_news, Genre.NEWS.toStringRes())
    assertEquals(genre_reality, Genre.REALITY.toStringRes())
    assertEquals(genre_romance, Genre.ROMANCE.toStringRes())
    assertEquals(genre_scifi_and_fantasy, Genre.SCI_FI_AND_FANTASY.toStringRes())
    assertEquals(genre_science_fiction, Genre.SCIENCE_FICTION.toStringRes())
    assertEquals(genre_soap, Genre.SOAP.toStringRes())
    assertEquals(genre_talk, Genre.TALK.toStringRes())
    assertEquals(genre_thriller, Genre.THRILLER.toStringRes())
    assertEquals(genre_tv_movie, Genre.TV_MOVIE.toStringRes())
    assertEquals(genre_war, Genre.WAR.toStringRes())
    assertEquals(genre_war_and_politics, Genre.WAR_AND_POLITICS.toStringRes())
    assertEquals(genre_western, Genre.WESTERN.toStringRes())
  }

  @Test
  fun intToStringRes_validGenreId_returnsCorrectStringRes() {
    assertEquals(genre_comedy, 35.toStringRes())
    assertEquals(genre_action, 28.toStringRes())
  }

  @Test
  fun intToStringRes_invalidGenreId_returnsNotAvailable() {
    assertEquals(not_available, 999.toStringRes())
  }

  @Test
  fun toGenre_validId_returnsGenre() {
    assertEquals(Genre.ACTION, 28.toGenre())
    assertEquals(Genre.COMEDY, 35.toGenre())
  }

  @Test
  fun toGenre_invalidId_returnsNull() {
    assertNull(999.toGenre())
  }

  @Test
  fun toGenreString_validGenreId_returnsCorrectString() {
    val input = listOf(28, 12, 16)
    val expectedOutput = "Action, Adventure, Animation"
    val actualOutput = input.toGenreString(context)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun toGenreString_withEmptyList_returnsEmptyString() {
    val actualOutput = emptyList<Int>().toGenreString(context)
    assertEquals("", actualOutput)
  }

  @Test
  fun toGenreString_invalidIds_returnsEmptyString() {
    assertEquals("", listOf(999).toGenreString(context))
  }

  @Test
  fun toGenreString_mixedValidAndInvalidIds_filtersInvalid() {
    val actual = listOf(28, 999, 35).toGenreString(context)
    assertEquals("Action, Comedy", actual)
  }

  @Test
  fun getGenre_invalidList_returnsNotAvailable() {
    assertEquals(context.getString(not_available), context.getGenre(null))
    assertEquals(context.getString(not_available), context.getGenre(emptyList()))
  }

  @Test
  fun getGenre_validIds_returnsGenreString() {
    assertEquals("Action, Adventure", context.getGenre(listOf(28, 12)))
  }

  @Test
  fun toGenreIds_validItems_returnsIds() {
    val input = listOf(GenresItem(id = 28, name = "Action"), GenresItem(id = 35, name = "Comedy"))
    assertEquals(listOf(28, 35), input.toGenreIds())
  }

  @Test
  fun toGenreIds_invalidList_returnsEmptyList() {
    val input: List<GenresItem?>? = null
    assertEquals(emptyList<Int>(), input.toGenreIds())
    assertEquals(emptyList<Int>(), emptyList<GenresItem?>().toGenreIds())
  }

  @Test
  fun toGenreIds_nullItems_areFilteredOut() {
    val input = listOf(GenresItem(id = 28, name = "Action"), null)
    assertEquals(listOf(28), input.toGenreIds())
  }

  @Test
  fun toGenreIds_nullIds_areFilteredOut() {
    val input =
      listOf(GenresItem(id = null, name = "Unknown"), GenresItem(id = 35, name = "Comedy"))
    assertEquals(listOf(35), input.toGenreIds())
  }
}
