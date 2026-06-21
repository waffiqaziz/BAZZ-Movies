package com.waffiq.bazz_movies.feature.detail.domain.repository

import com.waffiq.bazz_movies.core.models.Outcome
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.MovieDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvDetail
import kotlinx.coroutines.flow.Flow

interface IDetailRepository {
  fun getOMDbDetails(imdbId: String): Flow<Outcome<OMDbDetails>>
  fun getMovieDetail(movieId: Int): Flow<Outcome<MovieDetail>>
  fun getTvDetail(tvId: Int): Flow<Outcome<TvDetail>>
}
