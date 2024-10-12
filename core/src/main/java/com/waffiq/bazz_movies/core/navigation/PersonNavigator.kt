package com.waffiq.bazz_movies.core.navigation

import com.waffiq.bazz_movies.core.domain.model.person.MovieTvCastItem

interface PersonNavigator {
  fun openPersonDetails(cast: MovieTvCastItem)
}
