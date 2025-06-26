# 🧪 Testing

We use testing to ensure the stability and quality of our code. For testing name convention,
please [follow this naming guide](../docs/BAZZMoviesTestNamingConvention.md).

## Module-Level Coverage

Below you can find a list of BAZZ Movies modules.

| Module Name                                              | Coverage                                                                                        |
| -------------------------------------------------------- | ----------------------------------------------------------------------------------------------- |
| [`:app`][app-link]                                       | [![Code Coverage][app-coverage-badge]][app-coverage-link]                                       |
| [`:core:common`][core-common-link]                       | [![Code Coverage][core-common-coverage-badge]][core-common-coverage-link]                       |
| [`:core:coroutines`][core-coroutines-link]               | [![Code Coverage][core-coroutines-coverage-badge]][core-coroutines-coverage-link]               |
| [`:core:data`][core-data-link]                           | [![Code Coverage][core-data-coverage-badge]][core-data-coverage-link]                           |
| [`:core:database`][core-database-link]                   | [![Code Coverage][core-database-coverage-badge]][core-database-coverage-link]                   |
| [`:core:designsystem`][core-designsystem-link]           | [![Code Coverage][core-designsystem-coverage-badge]][core-designsystem-coverage-link]           |
| [`:core:domain`][core-domain-link]                       | [![Code Coverage][core-domain-coverage-badge]][core-domain-coverage-link]                       |
| [`:core:favoritewatchlist`][core-favoritewatchlist-link] | [![Code Coverage][core-favoritewatchlist-coverage-badge]][core-favoritewatchlist-coverage-link] |
| [`:core:mappers`][core-mappers-link]                     | [![Code Coverage][core-mappers-coverage-badge]][core-mappers-coverage-link]                     |
| [`:core:movie`][core-movie-link]                         | [![Code Coverage][core-movie-coverage-badge]][core-movie-coverage-link]                         |
| [`:core:network`][core-network-link]                     | [![Code Coverage][core-network-coverage-badge]][core-network-coverage-link]                     |
| [`:core:test`][core-test-link]                           | [![Code Coverage][core-test-coverage-badge]][core-test-coverage-link]                           |
| [`:core:uihelper`][core-uihelper-link]                   | [![Code Coverage][core-uihelper-coverage-badge]][core-uihelper-coverage-link]                   |
| [`:core:user`][core-user-link]                           | [![Code Coverage][core-user-coverage-badge]][core-user-coverage-link]                           |
| [`:core:utils`][core-utils-link]                         | [![Code Coverage][core-utils-coverage-badge]][core-utils-coverage-link]                         |
| [`:feature:about`][feature-about-link]                   | [![Code Coverage][feature-about-coverage-badge]][feature-about-coverage-link]                   |
| [`:feature:detail`][feature-detail-link]                 | [![Code Coverage][feature-detail-coverage-badge]][feature-detail-coverage-link]                 |
| [`:feature:favorite`][feature-favorite-link]             | [![Code Coverage][feature-favorite-coverage-badge]][feature-favorite-coverage-link]             |
| [`:feature:home`][feature-home-link]                     | [![Code Coverage][feature-home-coverage-badge]][feature-home-coverage-link]                     |
| [`:feature:login`][feature-login-link]                   | [![Code Coverage][feature-login-coverage-badge]][feature-login-coverage-link]                   |
| [`:feature:more`][feature-more-link]                     | [![Code Coverage][feature-more-coverage-badge]][feature-more-coverage-link]                     |
| [`:feature:person`][feature-person-link]                 | [![Code Coverage][feature-person-coverage-badge]][feature-person-coverage-link]                 |
| [`:feature:search`][feature-search-link]                 | [![Code Coverage][feature-search-coverage-badge]][feature-search-coverage-link]                 |
| [`:feature:watchlist`][feature-watchlist-link]           | [![Code Coverage][feature-watchlist-coverage-badge]][feature-watchlist-coverage-link]           |

> [!NOTE]
> Reports are generated using [JaCoCo](https://github.com/jacoco/jacoco)
> and updated regularly via [CI](../.github/workflows/android_test.yml).

## Unit Tests

Run all unit tests with the following command:

```terminal
./gradlew test
```

The result can see through the console. Unit tests are also automatically executed via GitHub
Actions on every push or pull request. You can review the results directly on the GitHub.
interface.

## UI Tests

To run Android-specific UI tests, use:

```terminal
./gradlew connectedAndroidTest
```

This runs tests on a connected Android device or emulator.

_Note that UI test coverage is still work in progress._

## Code Coverage Reports with [JaCoCo](https://github.com/jacoco/jacoco)

Generate a combined coverage report using the following command:

```terminal
./gradlew create<Variant>CombinedCoverageReport
```

### Generating a Report for a specific module

For [`:core:user`](../core/user/) module on debug variant:

- Clean and run unit tests:

  ```terminal
  ./gradlew :core:user:testDebugUnitTest
  ```

- Generate a combined coverage report:

  ```terminal
  ./gradlew :core:user:createDebugCombinedCoverageReport
  ```

- The generate report available at:

  ```terminal
  core/user/build/reports/jacoco/createDebugCombinedCoverageReport/html/index.html
  ```

### Generating a Report for all Module

- Clean and run unit tests:

  ```terminal
  ./gradlew testDebugUnitTest
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

<!-- LINK -->

[app-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/app
[app-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=app
[app-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main/app
[core-common-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/core/common
[core-common-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=core-common
[core-common-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main/core/common/src/main/kotlin/com/waffiq/bazz_movies/core/common
[core-coroutines-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/core/coroutines
[core-coroutines-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=core-coroutines
[core-coroutines-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main/core/coroutines/src/main/kotlin/com/waffiq/bazz_movies/core/coroutines
[core-data-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/core/data
[core-data-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=core-data
[core-data-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main/core/data/src/main/kotlin/com/waffiq/bazz_movies/core/data
[core-database-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/core/database
[core-database-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=core-database
[core-database-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main/core/database/src/main/kotlin/com/waffiq/bazz_movies/core/database
[core-designsystem-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/core/designsystem
[core-designsystem-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=core-designsystem
[core-designsystem-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main/core/designsystem/src/main/kotlin/com/waffiq/bazz_movies/core/designsystem
[core-domain-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/core/domain
[core-domain-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=core-domain
[core-domain-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main/core/domain/src/main/kotlin/com/waffiq/bazz_movies/core/domain
[core-favoritewatchlist-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/core/favoritewatchlist
[core-favoritewatchlist-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=core-favoritewatchlist
[core-favoritewatchlist-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main/core/favoritewatchlist/src/main/kotlin/com/waffiq/bazz_movies/core/favoritewatchlist
[core-mappers-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/core/mappers
[core-mappers-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=core-mappers
[core-mappers-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main/core/mappers/src/main/kotlin/com/waffiq/bazz_movies/core/mappers
[core-movie-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/core/movie
[core-movie-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=core-movie
[core-movie-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main/core/movie/src/main/kotlin/com/waffiq/bazz_movies/core/movie
[core-network-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/core/network
[core-network-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=core-network
[core-network-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main/core/network/src/main/kotlin/com/waffiq/bazz_movies/core/network
[core-test-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/core/test
[core-test-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=core-test
[core-test-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main/core/test/src/main/kotlin/com/waffiq/bazz_movies/core/test
[core-uihelper-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/core/uihelper
[core-uihelper-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=core-uihelper
[core-uihelper-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main/core/uihelper/src/main/kotlin/com/waffiq/bazz_movies/core/uihelper
[core-user-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/core/user
[core-user-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=core-user
[core-user-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main/core/user/src/main/kotlin/com/waffiq/bazz_movies/core/user
[core-utils-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/core/utils
[core-utils-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=core-utils
[core-utils-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main/core/utils/src/main/kotlin/com/waffiq/bazz_movies/core/utils
[feature-about-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/feature/about
[feature-about-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=feature-about
[feature-about-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main/feature/about/src/main/kotlin/com/waffiq/bazz_movies/feature/about
[feature-detail-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/feature/detail
[feature-detail-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=feature-detail
[feature-detail-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main/feature/detail/src/main/kotlin/com/waffiq/bazz_movies/feature/detail
[feature-favorite-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/feature/favorite
[feature-favorite-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=feature-favorite
[feature-favorite-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main/feature/favorite/src/main/kotlin/com/waffiq/bazz_movies/feature/favorite
[feature-home-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/feature/home
[feature-home-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=feature-home
[feature-home-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main/feature/home/src/main/kotlin/com/waffiq/bazz_movies/feature/home
[feature-login-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/feature/login
[feature-login-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=feature-login
[feature-login-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main/feature/login/src/main/kotlin/com/waffiq/bazz_movies/feature/login
[feature-more-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/feature/more
[feature-more-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=feature-more
[feature-more-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main/feature/more/src/main/kotlin/com/waffiq/bazz_movies/feature/more
[feature-person-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/feature/person
[feature-person-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=feature-person
[feature-person-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main/feature/person/src/main/kotlin/com/waffiq/bazz_movies/feature/person
[feature-search-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/feature/search
[feature-search-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=feature-search
[feature-search-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main/feature/search/src/main/kotlin/com/waffiq/bazz_movies/feature/search
[feature-watchlist-link]: https://github.com/waffiqaziz/BAZZ-Movies/tree/main/feature/watchlist
[feature-watchlist-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=feature-watchlist
[feature-watchlist-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main/feature/watchlist/src/main/kotlin/com/waffiq/bazz_movies/feature/watchlist
