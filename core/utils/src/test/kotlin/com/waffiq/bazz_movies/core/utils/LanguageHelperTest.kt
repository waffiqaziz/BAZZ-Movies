package com.waffiq.bazz_movies.core.utils

import org.junit.Test

class LanguageHelperTest {

  @Test
  fun getLanguageName_nullCode_returnsEmpty() {
    assert(LanguageHelper.getLanguageName(null) == "")
  }

  @Test
  fun getLanguageName_blankCode_returnsEmpty() {
    assert(LanguageHelper.getLanguageName("") == "")
  }

  @Test
  fun getLanguageName_noLanguageCode_returnsNoLanguage() {
    assert(LanguageHelper.getLanguageName("xx") == "No Language")
  }

  @Test
  fun getLanguageName_validCode_returnsEnglishName() {
    assert(LanguageHelper.getLanguageName("fr") == "French")
  }

  @Test
  fun getLanguageName_validCode_returnsEnglishNameNotDevice() {
    assert(LanguageHelper.getLanguageName("de") == "German")
  }

  @Test
  fun getLanguageName_invalidCode_returnsEmpty() {
    assert(LanguageHelper.getLanguageName("zz") == "")
  }

  @Test
  fun getLanguageName_whitespaceCode_returnsEmpty() {
    assert(LanguageHelper.getLanguageName("   ") == "")
  }
}
