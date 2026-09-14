package com.waffiq.bazz_movies.core.model.user

sealed class Rated {
  data class Value(val value: Double) : Rated()
  object Unrated : Rated()
}
