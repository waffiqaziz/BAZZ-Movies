package com.bazz_movies.core.adapter

import android.content.Context
import android.view.LayoutInflater
import androidx.test.core.app.ApplicationProvider
import com.google.android.material.imageview.ShapeableImageView
import com.google.common.truth.Truth.assertThat
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemMediaBinding
import com.waffiq.bazz_movies.core.models.MediaItem
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class BindAdapterHelperTest {

  val mediaItem = MediaItem(
    title = "The Matrix",
    releaseDate = "1999-03-31",
    voteAverage = 8.7f,
    listGenreIds = listOf(28, 878),
    posterPath = "https://example.com/poster.jpg",
  )

  @Test
  fun bindPicture_shouldSetContentDescriptionAndTag() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val imageView = ShapeableImageView(context)

    with(BindAdapterHelper) {
      imageView.bindPicture(mediaItem)
    }

    assertEquals(imageView.contentDescription, "The Matrix")
    assertEquals(imageView.tag, "The Matrix")
  }

  @Test
  fun bindMetaData_shouldPopulateViews() {
    val context = ApplicationProvider.getApplicationContext<Context>()

    val binding = ItemMediaBinding.inflate(
      LayoutInflater.from(context),
    )

    with(BindAdapterHelper) {
      binding.bindMetaData(mediaItem)
    }

    assertThat(binding.tvTitle.text.toString()).isEqualTo("The Matrix")
    assertThat(binding.tvTitle.text.toString()).isNotEmpty()
    assertThat(binding.tvGenre.text.toString()).isNotEmpty()
    assertThat(binding.ratingBar.rating).isGreaterThan(0f)
    assertThat(binding.tvRating.text.toString()).isNotEmpty()
  }
}
