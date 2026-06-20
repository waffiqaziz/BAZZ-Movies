package com.waffiq.bazz_movies.feature.detail.testutils

import com.waffiq.bazz_movies.feature.detail.domain.model.movie.MovieDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvDetail
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.mediaKeywords
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.releaseDateRegion
import com.waffiq.bazz_movies.feature.detail.utils.mappers.BasicMediaDetailMapper.toMediaDetail

object MapperHelperTest {

  fun TvDetail.stubToMediaDetail() = toMediaDetail("US", mediaKeywords)
  fun MovieDetail.stubToMediaDetail() = toMediaDetail(releaseDateRegion, mediaKeywords)
}
