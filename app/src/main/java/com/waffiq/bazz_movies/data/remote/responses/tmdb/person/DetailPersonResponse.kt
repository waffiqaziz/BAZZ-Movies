package com.waffiq.bazz_movies.data.remote.responses.tmdb.person

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = false)
data class DetailPersonResponse(

  @Json(name = "also_known_as")
  val alsoKnownAs: List<String?>? = null,

  @Json(name = "birthday")
  val birthday: String? = null,

  @Json(name = "gender")
  val gender: Int? = null,

  @Json(name = "imdb_id")
  val imdbId: String? = null,

  @Json(name = "known_for_department")
  val knownForDepartment: String? = null,

  @Json(name = "profile_path")
  val profilePath: String? = null,

  @Json(name = "biography")
  val biography: String? = null,

  @Json(name = "deathday")
  val deathday: String? = null,

  @Json(name = "place_of_birth")
  val placeOfBirth: String? = null,

  @Json(name = "popularity")
  val popularity: Float? = null,

  @Json(name = "name")
  val name: String? = null,

  @Json(name = "id")
  val id: Int? = null,

  @Json(name = "adult")
  val adult: Boolean? = null,

  @Json(name = "homepage")
  val homepage: String? = null
) : Parcelable
