package com.waffiq.bazz_movies.feature.favorite.domain.usecase

import com.waffiq.bazz_movies.feature.favorite.testutils.BaseFavoriteInteractorTest
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.SESSION_ID
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.fakeTvMediaItemPagingData
import io.kotest.matchers.shouldBe
import io.mockk.every
import kotlinx.coroutines.flow.flowOf

class GetFavoriteTvInteractorTest : BaseFavoriteInteractorTest() {

  init {
    should("return favorite movies") {
      val interactor = GetFavoriteTvInteractor(repository)
      every { repository.getFavoriteTv(SESSION_ID) } returns flowOf(fakeTvMediaItemPagingData)

      testPagingFlow(interactor.getFavoriteTv(SESSION_ID)) { list ->
        list[0].id shouldBe 1
        list[0].title shouldBe "Breaking Bad"
        list[0].overview shouldBe "A high school chemistry teacher turned methamphetamine producer"
        list[0].posterPath shouldBe "/poster3.jpg"
        list[0].mediaType shouldBe "tv"
        list[0].voteAverage shouldBe 9.5f
        list[0].releaseDate shouldBe "2008-01-20"

        list[1].id shouldBe 2
        list[1].title shouldBe "Game of Thrones"
        list[1].overview shouldBe "A fantasy drama series based on the novels by George.R.R. Martin"
        list[1].posterPath shouldBe "/poster4.jpg"
        list[1].mediaType shouldBe "tv"
        list[1].voteAverage shouldBe 9.3f
        list[1].releaseDate shouldBe "2011-04-17"
      }
    }
  }
}
