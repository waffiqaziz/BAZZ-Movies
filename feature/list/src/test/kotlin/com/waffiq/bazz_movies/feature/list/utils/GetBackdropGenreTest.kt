package com.waffiq.bazz_movies.feature.list.utils

import com.waffiq.bazz_movies.feature.list.utils.BackdropHelper.getBackdropMovieGenre
import com.waffiq.bazz_movies.feature.list.utils.BackdropHelper.getBackdropTvGenre
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

class GetBackdropGenreTest : BehaviorSpec({

  given("a known movie genre id") {
    `when`("getBackdropMovieGenre is called") {
      withData(
        nameFn = { (genreId, _) -> "genreId=$genreId" },
        28 to "/jDnhdNm7XVwl0GisIV77o64EcDa.jpg",
        12 to "/9lm7drIvSPANclGcbVUYJlK4ivh.jpg",
        16 to "/kPmE7vEwQWSvhQt5P0ZR8NIwNRN.jpg",
        35 to "/sV49hOlUky6AhVYl4K0d3rergTA.jpg",
        80 to "/xKsnZDERG1dk95wuZ5q9iks3OL3.jpg",
        99 to "hbqpPucPtkw4vhDYvXTDI75QLN1.jpg",
        18 to "/b6HWTOxn1xevvyHU2K9ICvaRU6g.jpg",
        10751 to "/ddfaU3bt27hthphDwCLtQaMEx6g.jpg",
        14 to "/rwcPe582tfTSVLwQzbO25InW3Hi.jpg",
        36 to "/pTwF9hLkqAtuOqXMdOyPwz4AgnI.jpg",
        27 to "/2OnTLW25V5JKEguy5FgFHcAEfXv.jpg",
        10402 to "/dcvbs8z0GEXslC1kCT77x19XDeR.jpg",
        9648 to "/3zCPI4JFc54xvLaJ71oI2KoP3az.jpg",
        10749 to "/t6dKnZ1iosrYJFA6Z6p72qOsrP0.jpg",
        878 to "/gpZQNDndImEJvof9l3DeY0iACPj.jpg",
        10770 to "/n36D2YOPEtGlVTUmDz0veSTnIa1.jpg",
        53 to "/uxTdeYQd7s2hwfZbOSDkUr1zWnk.jpg",
        10752 to "/ddIkmH3TpR6XSc47jj0BrGK5Rbz.jpg",
        37 to "/sGOw65ZmfgGfistCjuNsazjs6j9.jpg",
      ) { (genreId, expected) ->
        then("it should return correct backdrop url") {
          getBackdropMovieGenre(genreId) shouldBe expected
        }
      }
    }
  }

  given("an unknown movie genre id") {
    `when`("getBackdropMovieGenre is called") {
      then("it should return the default backdrop") {
        getBackdropMovieGenre(-1) shouldBe "/izI6lR6Y1GpIDGwKvLQlW9gFZPE.jpg"
      }
    }
  }

  given("a known tv genre id") {
    `when`("getBackdropTvGenre is called") {
      withData(
        nameFn = { (genreId, _) -> "genreId=$genreId" },
        10759 to "/mhdP47zYd5GnLHD7RtIKXUNnafQ.jpg",
        16 to "/cvytcYJFiVlp3tVUIfjSRHcSTfS.jpg",
        35 to "/4YKSbGevhHiMbOM5XtWifz6h7Ch.jpg",
        80 to "/6iNWfGVCEfASDdlNb05TP5nG0ll.jpg",
        99 to "/mtyTSs7E8anw82dUzHw0INFazhL.jpg",
        18 to "/m0GVaV4mgE8aFSHp8kTevHb8feu.jpg",
        10751 to "/edxIQJvFE0f0kJaGm6sJwbckyzM.jpg",
        10762 to "/xIWIhXerwpZVpLnydPrjl1cF0DM.jpg",
        9648 to "/8mmDLb108DoFZlE8sfc64TRqLvm.jpg",
        10763 to "/bfg3COqQzmaZqqaqmCruKoyjk72.jpg",
        10764 to "/wsHj4oHQJoe7DMYaqNFwVoyLiAh.jpg",
        10765 to "/wk37nNaZZYGn387Nyu7HXwWk1cC.jpg",
        10766 to "/guQddHapsv4BdSH8CrMsfcTzIVg.jpg",
        10767 to "/pVFWcrzPfoa6MxluwxZKrYsf9VU.jpg",
        10768 to "/ap40FS5o3rYKh7A7Q39sGgazLKy.jpg",
        37 to "/eScHwfVVumgrHT9fHdDNhUpcFcR.jpg",
      ) { (genreId, expected) ->
        then("it should return correct backdrop url") {
          getBackdropTvGenre(genreId) shouldBe expected
        }
      }
    }
  }

  given("an unknown tv genre id") {
    `when`("getBackdropTvGenre is called") {
      then("it should return the default backdrop") {
        getBackdropTvGenre(-1) shouldBe "/tsRy63Mu5cu8etL1X7ZLyf7UP1M.jpg"
      }
    }
  }
})
