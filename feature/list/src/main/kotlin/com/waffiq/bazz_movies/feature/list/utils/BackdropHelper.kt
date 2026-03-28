package com.waffiq.bazz_movies.feature.list.utils

@Suppress("MagicNumber", "CyclomaticComplexMethod")
object BackdropHelper {
  fun getBackdropMovieGenre(genreId: Int): String =
    when (genreId) {
      28 -> "/jDnhdNm7XVwl0GisIV77o64EcDa.jpg"

      // Action
      12 -> "/9lm7drIvSPANclGcbVUYJlK4ivh.jpg"

      // Adventure
      16 -> "/kPmE7vEwQWSvhQt5P0ZR8NIwNRN.jpg"

      // Animation
      35 -> "/sV49hOlUky6AhVYl4K0d3rergTA.jpg"

      // Comedy
      80 -> "/xKsnZDERG1dk95wuZ5q9iks3OL3.jpg"

      // Crime
      99 -> "hbqpPucPtkw4vhDYvXTDI75QLN1.jpg"

      // Documentary
      18 -> "/b6HWTOxn1xevvyHU2K9ICvaRU6g.jpg"

      // Drama
      10751 -> "/ddfaU3bt27hthphDwCLtQaMEx6g.jpg"

      // Family
      14 -> "/rwcPe582tfTSVLwQzbO25InW3Hi.jpg"

      // Fantasy
      36 -> "/pTwF9hLkqAtuOqXMdOyPwz4AgnI.jpg"

      // History
      27 -> "/2OnTLW25V5JKEguy5FgFHcAEfXv.jpg"

      // Horror
      10402 -> "/dcvbs8z0GEXslC1kCT77x19XDeR.jpg"

      // Music
      9648 -> "/3zCPI4JFc54xvLaJ71oI2KoP3az.jpg"

      // Mystery
      10749 -> "/t6dKnZ1iosrYJFA6Z6p72qOsrP0.jpg"

      // Romance
      878 -> "/gpZQNDndImEJvof9l3DeY0iACPj.jpg"

      // Science Fiction
      10770 -> "/n36D2YOPEtGlVTUmDz0veSTnIa1.jpg"

      // TV Movie
      53 -> "/uxTdeYQd7s2hwfZbOSDkUr1zWnk.jpg"

      // Thriller
      10752 -> "/ddIkmH3TpR6XSc47jj0BrGK5Rbz.jpg"

      // War
      37 -> "/sGOw65ZmfgGfistCjuNsazjs6j9.jpg"

      // Western
      else -> "/izI6lR6Y1GpIDGwKvLQlW9gFZPE.jpg"
    }

  fun getBackdropTvGenre(genreId: Int): String =
    when (genreId) {
      10759 -> "/mhdP47zYd5GnLHD7RtIKXUNnafQ.jpg"

      // Action & Adventure
      16 -> "/cvytcYJFiVlp3tVUIfjSRHcSTfS.jpg"

      // Animation
      35 -> "/4YKSbGevhHiMbOM5XtWifz6h7Ch.jpg"

      // Comedy
      80 -> "/6iNWfGVCEfASDdlNb05TP5nG0ll.jpg"

      // Crime
      99 -> "/mtyTSs7E8anw82dUzHw0INFazhL.jpg"

      // Documentary
      18 -> "/m0GVaV4mgE8aFSHp8kTevHb8feu.jpg"

      // Drama
      10751 -> "/edxIQJvFE0f0kJaGm6sJwbckyzM.jpg"

      // Family
      10762 -> "/xIWIhXerwpZVpLnydPrjl1cF0DM.jpg"

      // Kids
      9648 -> "/8mmDLb108DoFZlE8sfc64TRqLvm.jpg"

      // Mystery
      10763 -> "/bfg3COqQzmaZqqaqmCruKoyjk72.jpg"

      // News
      10764 -> "/wsHj4oHQJoe7DMYaqNFwVoyLiAh.jpg"

      // Reality
      10765 -> "/wk37nNaZZYGn387Nyu7HXwWk1cC.jpg"

      // Sci-Fi & Fantasy
      10766 -> "/guQddHapsv4BdSH8CrMsfcTzIVg.jpg"

      // Soap
      10767 -> "/pVFWcrzPfoa6MxluwxZKrYsf9VU.jpg"

      // Talk
      10768 -> "/ap40FS5o3rYKh7A7Q39sGgazLKy.jpg"

      // War & Politics
      37 -> "/eScHwfVVumgrHT9fHdDNhUpcFcR.jpg"

      // Western
      else -> "/tsRy63Mu5cu8etL1X7ZLyf7UP1M.jpg"
    }
}
