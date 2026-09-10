# :core:mapper Module

[![Code Coverage][core-mapper-coverage-badge]][core-mapper-coverage-link]

## Dependency Graph

![Dependency graph](../../docs/images/module-graphs/core-mapper.svg)

## Overview

The `:core:mapper` module is responsible for converting data models between different layers of
the application. It helps maintain separation of concerns by ensuring that domain models,
data models, and UI models remain distinct and properly formatted.

## Responsibilities

- **[PostMapper](../mapper/src/main/kotlin/com/waffiq/bazz_movies/core/mapper/PostMapper.kt)**

  - Converts `Post` data objects into their domain representations.
  - Ensures proper transformation of API responses before usage.

- **[MediaItemMapper](../mapper/src/main/kotlin/com/waffiq/bazz_movies/core/mapper/MediaItemMapper.kt)**

  - Maps `MediaItem` objects between data and domain layers.
  - Handles cases where fields require formatting or type conversion.

- **[StateMapper](../mapper/src/main/kotlin/com/waffiq/bazz_movies/core/mapper/StateMapper.kt)**
  - Maps different states (e.g., loading, success, error) between repository and UI layers.
  - Standardizes state management across the app.

## Example Usage

```kotlin
val postDomainModel = apiPostResponse.toPost()
```

## Best Practices

- **Keep mapper stateless** – They should be pure functions with no side effects.
- **Ensure proper error handling** – Handle nullability and type mismatches appropriately.
- **Follow a consistent mapping strategy** – Maintain clear naming conventions to improve
  readability.

This module plays a crucial role in ensuring data consistency and maintainability across the
application.

<!-- LINK -->

[core-mapper-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=core-mapper
[core-mapper-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main/core/mapper/src/main/kotlin/com/waffiq/bazz_movies/core/mapper
