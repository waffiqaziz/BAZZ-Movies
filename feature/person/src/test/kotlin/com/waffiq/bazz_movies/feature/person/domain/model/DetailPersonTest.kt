package com.waffiq.bazz_movies.feature.person.domain.model

import junit.framework.TestCase.assertNull
import org.junit.Test

class DetailPersonTest {

  @Test
  fun detailPerson_nullValue_returnDefaultValue(){
    val detailPerson = DetailPerson()

    assertNull(detailPerson.alsoKnownAs)
    assertNull(detailPerson.birthday)
    assertNull(detailPerson.gender)
    assertNull(detailPerson.imdbId)
    assertNull(detailPerson.knownForDepartment)
    assertNull(detailPerson.profilePath)
    assertNull(detailPerson.biography)
    assertNull(detailPerson.deathday)
    assertNull(detailPerson.placeOfBirth)
    assertNull(detailPerson.popularity)
    assertNull(detailPerson.name)
    assertNull(detailPerson.id)
    assertNull(detailPerson.adult)
    assertNull(detailPerson.homepage)
  }
}
