# 🧪 Testing

We use testing to ensure the stability and quality of our code. The
testing reports are generated using [JaCoCo](https://github.com/jacoco/jacoco) and updated regularly
via [CI (GitHub Action)](../.github/workflows/android_test.yml).

## Test Frameworks

- [JUnit4](https://github.com/junit-team/junit4) → Main test framework used across the project
  for both unit tests and instrumentation tests.
- [Kotest](https://github.com/kotest/kotest) → Currently being explored for more expressive testing.
  For now, it is only adopted in unit tests within the [`:feature:favorite`](../feature/favorite)
  module

## Testing Library

We use various libraries for different testing purposes, including mocking, Android-specific tests,
coroutines, and assertions.

### Mocking

- [MockK](https://github.com/mockk/mockk) (preferred for Kotlin)
- [Mockito](https://github.com/mockito/mockito) (legacy/Java compatibility)

### Android Testing

- [Robolectric](https://github.com/robolectric/robolectric) → run Android code on JVM
- [Espresso](https://github.com/android/android-test) → UI testing

### Coroutines & Flow

- [kotlinx-coroutines-test](https://github.com/Kotlin/kotlinx.coroutines) → test coroutine
  dispatchers
- [Turbine](https://github.com/cashapp/turbine) → test Flow emissions

### Assertions

- [JUnit4](https://github.com/junit-team/junit4) → default assertions
- [Truth](https://github.com/google/truth)
  and [Kotest assertions](https://github.com/kotest/kotest) → human-readable checks

## Test Naming Convention

### Junit Testing

For main test we stick to the **`method_whenCondition_expected`** style for naming tests.
It follows Kotlin’s **camelCase** method style, with underscores (`_`) as separators.

Purpose:

- Makes the purpose of each test obvious
- Keeps consistent across the whole suite
- Groups related tests together, so they’re easier to find especially for test coverage
- Acts like documentation, no need to open the test body to know what it does

The structure is defined as:

- **`method`** → the unit or function under test
- **`whenCondition`** → the specific scenario or input being applied
- **`expected`** → the result or behavior expected under that condition

#### Examples

##### ✅ Do

```kotlin
loadInitialPage_whenApiCallSucceeds_returnCorrectPage()
tiggerButton_withDoubleTouch_shouldDoNothing()
fetchUser_withValidValue_callsTheAPI()
logout_whenSessionExpired_clearUserSession()
dataResponse_withCorrectValues_setsPropertiesCorrectly()
```

##### ❌ Don't

```kotlin
test1()
checkSomething()
shouldWork()
```

> [!NOTE]
> Stick to descriptive and consistent naming across all tests (unit and instrumentation).

### Kotest Testing

As for kotest, we use Behavior-Driven Development (BDD) style,
see [here](https://kotest.io/docs/framework/testing-styles.html#behavior-spec).

## Module-Level Coverage

Below you can find a list of BAZZ Movies modules.

| Module Name                                                  | Coverage                                                                                       |
|--------------------------------------------------------------|------------------------------------------------------------------------------------------------|
| [`:app`][app-link]                                           | [![Coverage][app-coverage-badge]][app-coverage-link]                                           |
| [`:core:common`][core-common-link]                           | [![Coverage][core-common-coverage-badge]][core-common-coverage-link]                           |
| [`:core:coroutines`][core-coroutines-link]                   | [![Coverage][core-coroutines-coverage-badge]][core-coroutines-coverage-link]                   |
| [`:core:data`][core-data-link]                               | [![Coverage][core-data-coverage-badge]][core-data-coverage-link]                               |
| [`:core:database`][core-database-link]                       | [![Coverage][core-database-coverage-badge]][core-database-coverage-link]                       |
| [`:core:designsystem`][core-designsystem-link]               | [![Coverage][core-designsystem-coverage-badge]][core-designsystem-coverage-link]               |
| [`:core:domain`][core-domain-link]                           | [![Coverage][core-domain-coverage-badge]][core-domain-coverage-link]                           |
| [`:core:favoritewatchlist`][core-favoritewatchlist-link]     | [![Coverage][core-favoritewatchlist-coverage-badge]][core-favoritewatchlist-coverage-link]     |
| [`:core:instrumentationtest`][core-instrumentationtest-link] | [![Coverage][core-instrumentationtest-coverage-badge]][core-instrumentationtest-coverage-link] |
| [`:core:mappers`][core-mappers-link]                         | [![Coverage][core-mappers-coverage-badge]][core-mappers-coverage-link]                         |
| [`:core:movie`][core-movie-link]                             | [![Coverage][core-movie-coverage-badge]][core-movie-coverage-link]                             |
| [`:core:network`][core-network-link]                         | [![Coverage][core-network-coverage-badge]][core-network-coverage-link]                         |
| [`:core:test`][core-test-link]                               | [![Coverage][core-test-coverage-badge]][core-test-coverage-link]                               |
| [`:core:uihelper`][core-uihelper-link]                       | [![Coverage][core-uihelper-coverage-badge]][core-uihelper-coverage-link]                       |
| [`:core:user`][core-user-link]                               | [![Coverage][core-user-coverage-badge]][core-user-coverage-link]                               |
| [`:core:utils`][core-utils-link]                             | [![Coverage][core-utils-coverage-badge]][core-utils-coverage-link]                             |
| [`:feature:about`][feature-about-link]                       | [![Coverage][feature-about-coverage-badge]][feature-about-coverage-link]                       |
| [`:feature:detail`][feature-detail-link]                     | [![Coverage][feature-detail-coverage-badge]][feature-detail-coverage-link]                     |
| [`:feature:favorite`][feature-favorite-link]                 | [![Coverage][feature-favorite-coverage-badge]][feature-favorite-coverage-link]                 |
| [`:feature:home`][feature-home-link]                         | [![Coverage][feature-home-coverage-badge]][feature-home-coverage-link]                         |
| [`:feature:list`][feature-list-link]                         | [![Coverage][feature-list-coverage-badge]][feature-list-coverage-link]                         |
| [`:feature:login`][feature-login-link]                       | [![Coverage][feature-login-coverage-badge]][feature-login-coverage-link]                       |
| [`:feature:more`][feature-more-link]                         | [![Coverage][feature-more-coverage-badge]][feature-more-coverage-link]                         |
| [`:feature:person`][feature-person-link]                     | [![Coverage][feature-person-coverage-badge]][feature-person-coverage-link]                     |
| [`:feature:search`][feature-search-link]                     | [![Coverage][feature-search-coverage-badge]][feature-search-coverage-link]                     |
| [`:feature:watchlist`][feature-watchlist-link]               | [![Coverage][feature-watchlist-coverage-badge]][feature-watchlist-coverage-link]               |
| **TOTAL COVERAGE**                                           | [![Codecov][BADGE-CODECOV]][CODECOV]                                                           |

## Unit Tests

Run all unit tests with the following command:

```terminal
./gradlew test
```

Results will be shown in the console, or you can view them in a more readable format at every module
`**/build/reports/tests/testDebugUnitTest/index.html`

## Instrumentation Tests

To run all instrumentation tests, use:

```terminal
./gradlew connectedAndroidTest
```

The results can be viewed at `build/reports/androidTests/connected/debug/index.html`

## Code Coverage Reports with [JaCoCo](https://github.com/jacoco/jacoco)

Generate a combined coverage report from unit and instrumentation test for all modules separately
using the following command:

```terminal
./gradlew create<Variant>CombinedCoverageReport
```

> This command will generate a coverage report only if the module contains test code. If not, the
> module will be skipped in the report.

### Generating a Report for a specific module

For [`:feature:detail`](../feature/detail/) module on debug variant:

- Clean and run unit tests:

  ```terminal
  ./gradlew clean
  ./gradlew :feature:detail:testDebugUnitTest
  ```

- Run instrumentation test

  ```terminal
  ./gradlew :feature:detail:connectedDebugAndroidTest   
  ```

- Generate a combined coverage report:

  ```terminal
  ./gradlew :feature:detail:createDebugCombinedCoverageReport
  ```

  > This step generates a report that combines results from unit tests and instrumentation tests.

- The generate report available at:

  ```terminal
  feature/detail/build/reports/jacoco/createDebugCombinedCoverageReport/html/index.html
  ```

### Generating a Report for all Module

- Clean, then run unit tests and instrumentation test:

  ```terminal
  ./gradlew clean
  ./gradlew testDebugUnitTest
  ./gradlew connectedDebugAndroidTest  
  ```

- Generate a combined coverage report:

  ```terminal
  ./gradlew createDebugCombinedCoverageReport
  ```

- The generate report available on all module separately.
  Example for [:core:mappers](../core/mappers/) module, the report will be available at:

  ```terminal
  core/mappers/build/reports/jacoco/createDebugCombinedCoverageReport/html/index.html
  ```

## Code Coverage Reports with [Kotlinx Kover](https://github.com/Kotlin/kotlinx-kover)

> [!NOTE]
> Kotlinx Kover coverage is currently enabled only for the [
`:feature:favorite`](../feature/favorite/)
> as an alternative coverage tool for Kotlin.
>
> However, since [Kover does not support instrumentation tests on Android devices][KOVER-FEATURES],
> see also here https://github.com/Kotlin/kotlinx-kover/issues/96
>
> **JaCoCo remains the primary coverage tool** due to its broader compatibility.

### Generating Kover Report for feature:favorite module

For [`:feature:favorite`](../feature/favorite/) module:

- Clean and run tests with coverage:

  ```bash
  # run test
  ./gradlew clean
  ./gradlew :feature:favorite:test

  # html format
  ./gradlew :feature:favorite:koverHtmlReport

  # xml format (useful for CI/CD integration)
  ./gradlew :feature:favorite:koverXmlReport

  # binary coverage
  ./gradlew :feature:favorite:koverBinaryReport
  ```

  The results can be viewed at:
    - `build/reports/kover/html/index.html`
    - `build/reports/kover/report.xml`
    - `build/reports/kover/report.bin`

### Coverage Verification

To verify that coverage meets minimum thresholds:

```terminal
./gradlew :feature:favorite:koverVerify
```

This will return failed if not passing the thresholds, and return nothing if passing.

### Using Kover in Other Modules

To enable Kover in additional modules, apply the custom Kover plugin inside each
module’s `build.gradle.kts` file:

```kt
plugins {
  alias(libs.plugins.bazzmovies.android.library.kover)
}
```

<!-- LINK -->

[app-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/app

[app-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=app

[app-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main?flags%5B0%5D=app

[core-common-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/core/common

[core-common-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=core-common

[core-common-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main?flags%5B0%5D=core-common

[core-coroutines-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/core/coroutines

[core-coroutines-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=core-coroutines

[core-coroutines-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main?flags%5B0%5D=core-coroutines

[core-data-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/core/data

[core-data-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=core-data

[core-data-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main?flags%5B0%5D=core-data

[core-database-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/core/database

[core-database-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=core-database

[core-database-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main?flags%5B0%5D=core-database

[core-designsystem-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/core/designsystem

[core-designsystem-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=core-designsystem

[core-designsystem-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main?flags%5B0%5D=core-designsystem

[core-domain-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/core/domain

[core-domain-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=core-domain

[core-domain-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main?flags%5B0%5D=core-domain

[core-favoritewatchlist-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/core/favoritewatchlist

[core-favoritewatchlist-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=core-favoritewatchlist

[core-favoritewatchlist-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main?flags%5B0%5D=core-favoritewatchlist

[core-instrumentationtest-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/core/instrumentationtest

[core-instrumentationtest-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=core-instrumentationtest

[core-instrumentationtest-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main?flags%5B0%5D=core-instrumentationtest

[core-mappers-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/core/mappers

[core-mappers-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=core-mappers

[core-mappers-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main?flags%5B0%5D=core-mappers

[core-movie-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/core/movie

[core-movie-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=core-movie

[core-movie-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main?flags%5B0%5D=core-movie

[core-network-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/core/network

[core-network-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=core-network

[core-network-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main?flags%5B0%5D=core-network

[core-test-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/core/test

[core-test-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=core-test

[core-test-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main?flags%5B0%5D=core-test

[core-uihelper-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/core/uihelper

[core-uihelper-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=core-uihelper

[core-uihelper-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main?flags%5B0%5D=core-uihelper

[core-user-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/core/user

[core-user-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=core-user

[core-user-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main?flags%5B0%5D=core-user

[core-utils-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/core/utils

[core-utils-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=core-utils

[core-utils-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main?flags%5B0%5D=core-utils

[feature-about-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/feature/about

[feature-about-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=feature-about

[feature-about-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main?flags%5B0%5D=feature-about

[feature-detail-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/feature/detail

[feature-detail-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=feature-detail

[feature-detail-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main?flags%5B0%5D=feature-detail

[feature-favorite-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/feature/favorite

[feature-favorite-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=feature-favorite

[feature-favorite-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main?flags%5B0%5D=feature-favorite

[feature-home-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/feature/home

[feature-home-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=feature-home

[feature-home-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main?flags%5B0%5D=feature-home

[feature-list-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/feature/list

[feature-list-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=feature-list

[feature-list-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main?flags%5B0%5D=feature-list

[feature-login-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/feature/login

[feature-login-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=feature-login

[feature-login-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main?flags%5B0%5D=feature-login

[feature-more-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/feature/more

[feature-more-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=feature-more

[feature-more-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main?flags%5B0%5D=feature-more

[feature-person-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/feature/person

[feature-person-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=feature-person

[feature-person-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main?flags%5B0%5D=feature-person

[feature-search-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/feature/search

[feature-search-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=feature-search

[feature-search-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main?flags%5B0%5D=feature-search

[feature-watchlist-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/feature/watchlist

[feature-watchlist-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=feature-watchlist

[feature-watchlist-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main?flags%5B0%5D=feature-watchlist

[CODECOV]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies

[BADGE-CODECOV]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/graph/badge.svg?token=4SV6Z18HKZ

[KOVER-FEATURES]: https://github.com/Kotlin/kotlinx-kover?tab=readme-ov-file#features