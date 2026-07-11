package com.waffiq.bazz_movies.navigation.utils

/*
  JaCoCo skips coverage checks on anything annotated with a name containing "Generated" this uses
  that on purpose.

  Coverage for Kotlin inline functions is calculated from the call site, not the function itself.
  This function is only ever called from a test class, and others module which cannot covered and
  JaCoCo's Gradle plugin doesn't analyze test classes (only main code), so it never sees the call
  site and always reports this as uncovered, even though it's tested. This is a confirmed
  JaCoCo/Gradle limitation:

  - https://github.com/jacoco/jacoco/issues/1873#issuecomment-2785754199
  - https://github.com/jacoco/jacoco/issues/1921#issuecomment-3098557834
*/
@Retention(AnnotationRetention.BINARY)
annotation class ExcludedFromGeneratedCoverageReport
