package com.waffiq.bazz_movies.feature.list.utils

import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE

@Suppress("MagicNumber", "CyclomaticComplexMethod")
object BackdropHelper {

  fun getBackdrop(mediaType: String, id: Int) =
    if (mediaType == MOVIE_MEDIA_TYPE) getBackdropMovieGenre(id) else getBackdropTvGenre(id)

  fun getBackdropMovieGenre(genreId: Int): String =
    when (genreId) {
      // Action
      28 -> "/jDnhdNm7XVwl0GisIV77o64EcDa.jpg"

      // Adventure
      12 -> "/9lm7drIvSPANclGcbVUYJlK4ivh.jpg"

      // Animation
      16 -> "/kPmE7vEwQWSvhQt5P0ZR8NIwNRN.jpg"

      // Comedy
      35 -> "/sV49hOlUky6AhVYl4K0d3rergTA.jpg"

      // Crime
      80 -> "/xKsnZDERG1dk95wuZ5q9iks3OL3.jpg"

      // Documentary
      99 -> "/hbqpPucPtkw4vhDYvXTDI75QLN1.jpg"

      // Drama
      18 -> "/b6HWTOxn1xevvyHU2K9ICvaRU6g.jpg"

      // Family
      10751 -> "/ddfaU3bt27hthphDwCLtQaMEx6g.jpg"

      // Fantasy
      14 -> "/rwcPe582tfTSVLwQzbO25InW3Hi.jpg"

      // History
      36 -> "/pTwF9hLkqAtuOqXMdOyPwz4AgnI.jpg"

      // Horror
      27 -> "/2OnTLW25V5JKEguy5FgFHcAEfXv.jpg"

      // Music
      10402 -> "/dcvbs8z0GEXslC1kCT77x19XDeR.jpg"

      // Mystery
      9648 -> "/3zCPI4JFc54xvLaJ71oI2KoP3az.jpg"

      // Romance
      10749 -> "/t6dKnZ1iosrYJFA6Z6p72qOsrP0.jpg"

      // Science Fiction
      878 -> "/gpZQNDndImEJvof9l3DeY0iACPj.jpg"

      // TV Movie
      10770 -> "/n36D2YOPEtGlVTUmDz0veSTnIa1.jpg"

      // Thriller
      53 -> "/uxTdeYQd7s2hwfZbOSDkUr1zWnk.jpg"

      // War
      10752 -> "/ddIkmH3TpR6XSc47jj0BrGK5Rbz.jpg"

      37 -> "/sGOw65ZmfgGfistCjuNsazjs6j9.jpg"

      // Western
      else -> "/izI6lR6Y1GpIDGwKvLQlW9gFZPE.jpg"
    }

  fun getBackdropTvGenre(genreId: Int): String =
    when (genreId) {
      // Action & Adventure
      10759 -> "/mhdP47zYd5GnLHD7RtIKXUNnafQ.jpg"

      // Animation
      16 -> "/cvytcYJFiVlp3tVUIfjSRHcSTfS.jpg"

      // Comedy
      35 -> "/4YKSbGevhHiMbOM5XtWifz6h7Ch.jpg"

      // Crime
      80 -> "/6iNWfGVCEfASDdlNb05TP5nG0ll.jpg"

      // Documentary
      99 -> "/mtyTSs7E8anw82dUzHw0INFazhL.jpg"

      // Drama
      18 -> "/m0GVaV4mgE8aFSHp8kTevHb8feu.jpg"

      // Family
      10751 -> "/edxIQJvFE0f0kJaGm6sJwbckyzM.jpg"

      // Kids
      10762 -> "/xIWIhXerwpZVpLnydPrjl1cF0DM.jpg"

      // Mystery
      9648 -> "/8mmDLb108DoFZlE8sfc64TRqLvm.jpg"

      // News
      10763 -> "/bfg3COqQzmaZqqaqmCruKoyjk72.jpg"

      // Reality
      10764 -> "/wsHj4oHQJoe7DMYaqNFwVoyLiAh.jpg"

      // Sci-Fi & Fantasy
      10765 -> "/wk37nNaZZYGn387Nyu7HXwWk1cC.jpg"

      // Soap
      10766 -> "/guQddHapsv4BdSH8CrMsfcTzIVg.jpg"

      // Talk
      10767 -> "/pVFWcrzPfoa6MxluwxZKrYsf9VU.jpg"

      // War & Politics
      10768 -> "/ap40FS5o3rYKh7A7Q39sGgazLKy.jpg"

      // Western
      37 -> "/eScHwfVVumgrHT9fHdDNhUpcFcR.jpg"

      else -> "/tsRy63Mu5cu8etL1X7ZLyf7UP1M.jpg"
    }
}
