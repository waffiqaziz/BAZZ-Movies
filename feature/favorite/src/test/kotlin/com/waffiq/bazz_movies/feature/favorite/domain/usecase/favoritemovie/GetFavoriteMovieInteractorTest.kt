package com.waffiq.bazz_movies.feature.favorite.domain.usecase.favoritemovie

import com.waffiq.bazz_movies.feature.favorite.testutils.BaseFavoriteInteractorTest
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.SESSION_ID
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.fakeMovieMediaItemPagingData
import io.kotest.matchers.shouldBe
import io.mockk.every
import kotlinx.coroutines.flow.flowOf

class GetFavoriteMovieInteractorTest : BaseFavoriteInteractorTest() {

  init {
    should("return favorite movies") {
      val interactor = GetFavoriteMovieInteractor(repository)
      every { repository.getFavoriteMovies(SESSION_ID) } returns flowOf(fakeMovieMediaItemPagingData)

      testPagingFlow(interactor.getFavoriteMovies(SESSION_ID)) { list ->
        list[0].id shouldBe 1
        list[0].title shouldBe "Inception"
        list[0].overview shouldBe "A mind-bending thriller"
        list[0].posterPath shouldBe "/poster1.jpg"
        list[0].mediaType shouldBe "movie"
        list[0].voteAverage shouldBe 8.8f
        list[0].releaseDate shouldBe "2010-07-16"

        list[1].id shouldBe 2
        list[1].title shouldBe "The Dark Knight"
        list[1].overview shouldBe "A gritty superhero film"
        list[1].posterPath shouldBe "/poster2.jpg"
        list[1].mediaType shouldBe "movie"
        list[1].voteAverage shouldBe 9.0f
        list[1].releaseDate shouldBe "2008-07-18"
      }
    }
  }
}
