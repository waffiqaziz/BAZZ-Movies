# :feature:list Module

[![Code Coverage][feature-list-coverage-badge]][feature-list-coverage-link]

## Dependency Graph

```mermaid
%%{
  init: {
    'theme': 'neo-dark'
  }
}%%

graph LR
  subgraph :core
    :core:designsystem["designsystem"]
    :core:test["test"]
    :core:testmodule["testmodule"]
    :core:data["data"]
    :core:adapter["adapter"]
    :core:instrumentationtest["instrumentationtest"]
  end
  :feature:list --> :core:instrumentationtest
  :feature:list --> :core:testmodule
  :feature:list --> :core:designsystem
  :feature:list --> :navigation
  :feature:list --> :core:adapter
  :feature:list --> :core:data
  :feature:list --> :core:test
```

## Overview

`:feature:list` is responsible for handling to show list of movie and tv-series based on genre.

## Testing

In this module, we use JUnit4 for standard unit and instrumentation testing but also Kotest for more expressive behavior-driven for unit tests.

<!-- LINK -->

[feature-list-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=feature-list
[feature-list-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main/feature/list/src/main/kotlin/com/waffiq/bazz_movies/feature/list
